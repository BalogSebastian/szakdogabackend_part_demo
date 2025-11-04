// hu/szakdolgozat/azonositas/szolgaltatas/DnsSzolgaltatas.java
package hu.szakdolgozat.azonositas.szolgaltatas;

import hu.szakdolgozat.azonositas.dns.Hangulatok;
import hu.szakdolgozat.azonositas.dns.OrtizHeurisztika;
import hu.szakdolgozat.azonositas.modell.dok.DnsProfil;
import hu.szakdolgozat.azonositas.modell.dok.PlaylistDokumentum;
import hu.szakdolgozat.azonositas.tarolo.PlaylistTarolo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DnsSzolgaltatas {

    private final PlaylistTarolo tarolo;
    private final RestClient http;

    public DnsSzolgaltatas(PlaylistTarolo tarolo) {
        this.tarolo = tarolo;
        this.http = RestClient.create();
    }

    public DnsProfil elemezOrtiz(String playlistId, String email) {
        var opt = tarolo.findByIdAndEmail(Objects.toString(playlistId, "").trim(),
                Objects.toString(email, "").trim().toLowerCase());
        if (opt.isEmpty()) return null;

        var p = opt.get();
        var cimLista    = p.getSzamok() == null ? List.<String>of() : p.getSzamok().stream().map(PlaylistDokumentum.SzamAdat::getCim).toList();
        var eloadoLista = p.getSzamok() == null ? List.<String>of() : p.getSzamok().stream().map(PlaylistDokumentum.SzamAdat::getEloado).toList();

        Map<String, Double> profil = OrtizHeurisztika.profil(cimLista, eloadoLista);
        String fo = OrtizHeurisztika.foHangulat(profil);

        DnsProfil dns = new DnsProfil("ortiz", "heurisztika", profil, fo);
        p.setDns(dns);
        tarolo.save(p); // elmentjük a legutóbbi elemzést
        return dns;
    }

    public PlaylistDokumentum modositOrtiz(String playlistId, String email, String celHangulat, double csereArany) {
        csereArany = Math.max(0.1, Math.min(csereArany, 0.6)); // 10–60% közt
        var opt = tarolo.findByIdAndEmail(Objects.toString(playlistId, "").trim(),
                Objects.toString(email, "").trim().toLowerCase());
        if (opt.isEmpty()) return null;

        var eredeti = opt.get();
        var lista = new ArrayList<>(Optional.ofNullable(eredeti.getSzamok()).orElse(List.of()));
        if (lista.isEmpty()) return null;

        List<Double> celPont = new ArrayList<>();
        for (var s : lista) {
            Map<String, Double> pr = OrtizHeurisztika.profil(
                    List.of(Objects.toString(s.getCim(),"")),
                    List.of(Objects.toString(s.getEloado(),""))
            );
            celPont.add(pr.getOrDefault(celHangulat, 0.0));
        }

        int darabCsere = Math.max(1, (int)Math.round(lista.size() * csereArany));
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < celPont.size(); i++) idx.add(i);
        idx.sort(Comparator.comparingDouble(celPont::get)); // alulról felfelé
        List<Integer> cserelendoIndexek = idx.subList(0, Math.min(darabCsere, idx.size()));

        List<PlaylistDokumentum.SzamAdat> ajanlott = ajanlottDalokDeezer(celHangulat, darabCsere * 3);

        Set<String> marVan = lista.stream()
                .map(s -> (Objects.toString(s.getEloado(),"")+"—"+Objects.toString(s.getCim(),"")).toLowerCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Iterator<PlaylistDokumentum.SzamAdat> it = ajanlott.iterator();
        for (int i : cserelendoIndexek) {
            PlaylistDokumentum.SzamAdat uj = null;
            while (it.hasNext()) {
                var jel = it.next();
                String key = (Objects.toString(jel.getEloado(),"")+"—"+Objects.toString(jel.getCim(),"")).toLowerCase(Locale.ROOT);
                if (!marVan.contains(key)) { uj = jel; marVan.add(key); break; }
            }
            if (uj != null) lista.set(i, uj);
        }

        String ujNev = eredeti.getNev() + " (DNS – Ortiz → " + celHangulat + ")";
        var uj = new PlaylistDokumentum(eredeti.getEmail(), ujNev, lista, "dns_mod", eredeti.getId());
        uj.setCelHangulat(celHangulat);
        Map<String, Double> profilUj = OrtizHeurisztika.profil(
                lista.stream().map(PlaylistDokumentum.SzamAdat::getCim).toList(),
                lista.stream().map(PlaylistDokumentum.SzamAdat::getEloado).toList()
        );
        uj.setDns(new DnsProfil("ortiz","deezer", profilUj, OrtizHeurisztika.foHangulat(profilUj)));
        uj.getDns().setKeszult(Instant.now());

        tarolo.save(uj);
        return uj;
    }

    private List<PlaylistDokumentum.SzamAdat> ajanlottDalokDeezer(String celHangulat, int limit) {
        limit = Math.max(3, Math.min(limit, 60));
        List<String> kws = OrtizHeurisztika.kulcsszavai(celHangulat);
        String query = String.join(" ", kws);

        try {
            ResponseEntity<Map> resp = http.get()
                    .uri("https://api.deezer.com/search?q={q}&limit={limit}", query, limit)
                    .retrieve()
                    .toEntity(Map.class);

            Object data = Objects.requireNonNull(resp.getBody()).get("data");
            if (!(data instanceof List<?> list)) return List.of();

            List<PlaylistDokumentum.SzamAdat> out = new ArrayList<>();
            for (Object o : list) {
                if (!(o instanceof Map<?,?> m)) continue;
                String title = Objects.toString(m.get("title"), "");
                String artist = "";
                Object a = m.get("artist");
                if (a instanceof Map<?,?> am) artist = Objects.toString(am.get("name"), "");
                if (!title.isBlank() && !artist.isBlank()) {
                    out.add(new PlaylistDokumentum.SzamAdat(artist, title));
                }
            }
            return out;
        } catch (Exception e) {
            return List.of();
        }
    }
}
