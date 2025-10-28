// ez itt a rest vezerlo – fajlfeltoltes + listazas + torles + Deezer API integráció
package hu.szakdolgozat.azonositas.vezerlo;

import hu.szakdolgozat.azonositas.modell.dok.PlaylistDokumentum;
import hu.szakdolgozat.azonositas.szolgaltatas.PlaylistSzolgaltatas;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistVezerlo {

    private final PlaylistSzolgaltatas svc;

    public PlaylistVezerlo(PlaylistSzolgaltatas svc) {
        this.svc = svc;
    }

    // --- 1️⃣ Fájlfeltöltés alapú playlist
    @PostMapping("/feltolt")
    public ResponseEntity<?> feltolt(
            @RequestParam String email,
            @RequestParam String nev,
            @RequestParam("fajl") MultipartFile fajl
    ) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("email_hianyzik");
        }

        String id = svc.letrehozFajlAlapjan(email.trim().toLowerCase(), nev, fajl);
        if ("hiba".equals(id)) {
            return ResponseEntity.badRequest().body("hiba");
        }
        return ResponseEntity.ok(id);
    }

    // --- 2️⃣ JSON alapú playlist mentés (pl. Deezer API-ból)
    // POST /api/playlist/feltolt-api
    @PostMapping("/feltolt-api")
    public ResponseEntity<?> feltoltApi(@RequestBody Map<String, Object> adat) {
        try {
            String email = (String) adat.get("email");
            String nev = (String) adat.get("nev");
            List<Map<String, String>> szamok = (List<Map<String, String>>) adat.get("szamok");

            if (email == null || email.isBlank() || nev == null || nev.isBlank() || szamok == null) {
                return ResponseEntity.badRequest().body("hianyzo_adat");
            }

            String id = svc.letrehozJsonAlapjan(email.trim().toLowerCase(), nev.trim(), szamok);
            if ("hiba".equals(id)) {
                return ResponseEntity.badRequest().body("mentesi_hiba");
            }

            return ResponseEntity.ok(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("json_hiba");
        }
    }

    // --- 3️⃣ Playlist lista (email szerint)
    @GetMapping
    public ResponseEntity<?> listaz(@RequestParam(required = false) String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("email_hianyzik");
        }

        List<PlaylistDokumentum> lista = svc.listaz(email.trim().toLowerCase());
        return ResponseEntity.ok(lista);
    }

    // --- 4️⃣ Törlés
    @DeleteMapping("/{id}")
    public ResponseEntity<?> torol(@PathVariable String id, @RequestParam String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("email_hianyzik");
        }

        String valasz = svc.torol(id, email.trim().toLowerCase());
        if (!"siker".equals(valasz)) {
            return ResponseEntity.badRequest().body(valasz);
        }
        return ResponseEntity.ok("siker");
    }
}
