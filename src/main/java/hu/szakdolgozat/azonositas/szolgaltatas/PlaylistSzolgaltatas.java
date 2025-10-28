package hu.szakdolgozat.azonositas.szolgaltatas;

import hu.szakdolgozat.azonositas.modell.dok.PlaylistDokumentum;
import hu.szakdolgozat.azonositas.tarolo.PlaylistTarolo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PlaylistSzolgaltatas {

    private final PlaylistTarolo tarolo;

    public PlaylistSzolgaltatas(PlaylistTarolo tarolo) {
        this.tarolo = tarolo;
    }

    // --- 1️⃣ Fájlfeltöltés alapján új playlist létrehozása
    public String letrehozFajlAlapjan(String email, String nev, MultipartFile fajl) {
        try {
            if (ures(email) || ures(nev) || fajl == null || fajl.isEmpty()) return "hiba";

            List<String> sorok = new ArrayList<>();
            try (var br = new BufferedReader(new InputStreamReader(fajl.getInputStream(), StandardCharsets.UTF_8))) {
                String sor;
                while ((sor = br.readLine()) != null) {
                    if (!sor.isBlank()) sorok.add(sor.strip());
                }
            }

            String fn = Optional.ofNullable(fajl.getOriginalFilename())
                    .orElse("")
                    .toLowerCase(Locale.ROOT);

            List<PlaylistDokumentum.SzamAdat> szamok;
            if (fn.endsWith(".m3u") || fn.endsWith(".m3u8")) {
                szamok = parsolM3U(sorok);
            } else if (fn.endsWith(".csv")) {
                szamok = parsolCSV(sorok);
            } else {
                szamok = parsolEgyszeruSzoveg(sorok);
            }

            var dok = new PlaylistDokumentum(email.trim().toLowerCase(), nev.trim(), szamok);
            tarolo.save(dok);
            return dok.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "hiba";
        }
    }

    // --- 2️⃣ JSON alapú playlist mentés (Deezer API-ból)
    public String letrehozJsonAlapjan(String email, String nev, List<Map<String, String>> szamLista) {
        try {
            if (ures(email) || ures(nev) || szamLista == null) return "hiba";

            List<PlaylistDokumentum.SzamAdat> szamok = new ArrayList<>();
            for (Map<String, String> z : szamLista) {
                String eloado = z.getOrDefault("eloado", "").trim();
                String cim = z.getOrDefault("cim", "").trim();
                if (!eloado.isEmpty() && !cim.isEmpty()) {
                    szamok.add(new PlaylistDokumentum.SzamAdat(eloado, cim));
                }
            }

            var dok = new PlaylistDokumentum(email.trim().toLowerCase(), nev.trim(), szamok);
            tarolo.save(dok);
            return dok.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "hiba";
        }
    }

    // --- 3️⃣ Listázás (csak saját email)
    public List<PlaylistDokumentum> listaz(String email) {
        if (ures(email)) return List.of();
        return tarolo.findByEmailOrderByLetrehozvaDesc(email.trim().toLowerCase());
    }

    // --- 4️⃣ Törlés (csak saját playlist)
    public String torol(String id, String email) {
        if (ures(id) || ures(email)) return "hiba";
        var opt = tarolo.findByIdAndEmail(id.trim(), email.trim().toLowerCase());
        if (opt.isEmpty()) return "nincs_ilyen_vagy_nem_a_tied";
        tarolo.deleteById(id.trim());
        return "siker";
    }

    // --- Parszolások
    private List<PlaylistDokumentum.SzamAdat> parsolM3U(List<String> sorok) {
        List<PlaylistDokumentum.SzamAdat> lista = new ArrayList<>();
        String extinf = null;

        for (String s : sorok) {
            if (s.isBlank()) continue;
            if (s.startsWith("#EXTM3U")) continue;

            if (s.startsWith("#EXTINF")) {
                int i = s.indexOf(',');
                extinf = (i >= 0 && i < s.length() - 1) ? s.substring(i + 1).strip() : null;
                continue;
            }

            if (extinf != null) {
                var z = bontEloadoCim(extinf);
                if (z != null) lista.add(z);
                extinf = null;
            }
        }
        return lista;
    }

    private List<PlaylistDokumentum.SzamAdat> parsolCSV(List<String> sorok) {
        List<PlaylistDokumentum.SzamAdat> lista = new ArrayList<>();
        for (String s : sorok) {
            if (s.isBlank() || s.startsWith("#")) continue;

            String[] r = s.split("[;,]");
            if (r.length >= 2) {
                String eloado = r[0].strip();
                String cim = r[1].strip();
                if (!eloado.isEmpty() && !cim.isEmpty()) {
                    lista.add(new PlaylistDokumentum.SzamAdat(eloado, cim));
                }
            } else {
                var z = bontEloadoCim(s);
                if (z != null) lista.add(z);
            }
        }
        return lista;
    }

    private List<PlaylistDokumentum.SzamAdat> parsolEgyszeruSzoveg(List<String> sorok) {
        List<PlaylistDokumentum.SzamAdat> lista = new ArrayList<>();
        for (String s : sorok) {
            if (s.isBlank() || s.startsWith("#")) continue;
            var z = bontEloadoCim(s);
            if (z != null) lista.add(z);
        }
        return lista;
    }

    private PlaylistDokumentum.SzamAdat bontEloadoCim(String szoveg) {
        int i = szoveg.indexOf(" - ");
        if (i <= 0) return null;
        String eloado = szoveg.substring(0, i).strip();
        String cim = szoveg.substring(i + 3).strip();
        if (eloado.isEmpty() || cim.isEmpty()) return null;
        return new PlaylistDokumentum.SzamAdat(eloado, cim);
    }

    private static boolean ures(String s) {
        return s == null || s.trim().isEmpty();
    }
}
