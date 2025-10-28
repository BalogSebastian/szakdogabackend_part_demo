// ez itt a rest vezerlo – regisztral, belep, elfelejtett jelszo, uj jelszo, kijelentkezes
package hu.szakdolgozat.azonositas.vezerlo;

import hu.szakdolgozat.azonositas.modell.dto.BelepKer;
import hu.szakdolgozat.azonositas.modell.dto.RegisztralKer;
import hu.szakdolgozat.azonositas.modell.dto.ElfelejtettJelszoKer;
import hu.szakdolgozat.azonositas.modell.dto.UjJelszoKer;
import hu.szakdolgozat.azonositas.szolgaltatas.AzonositasSzolgaltatas;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AzonositasVezerlo {

    private final AzonositasSzolgaltatas svc;

    public AzonositasVezerlo(AzonositasSzolgaltatas svc) {
        this.svc = svc;
    }

    // -- POST /api/regisztral
    @PostMapping("/regisztral")
    public ResponseEntity<?> regisztral(@Valid @RequestBody RegisztralKer ker) {
        String eredmeny = svc.regisztral(ker);
        if ("siker".equals(eredmeny)) {
            return ResponseEntity.ok("siker");
        }
        // lehet: "mar_letezik"
        return ResponseEntity.badRequest().body(eredmeny);
    }

    // -- POST /api/belep
    @PostMapping("/belep")
    public ResponseEntity<?> belep(@Valid @RequestBody BelepKer ker) {
        String tokenVagyHiba = svc.belep(ker);
        if ("hiba".equals(tokenVagyHiba)) {
            return ResponseEntity.status(401).body("hiba");
        }
        return ResponseEntity.ok(tokenVagyHiba); // pl. "token_..."
    }

    // -- POST /api/jelszo/elfelejtett  (fejlesztesben visszaadhat tokent is)
    @PostMapping("/jelszo/elfelejtett")
    public ResponseEntity<?> elfelejtett(@Valid @RequestBody ElfelejtettJelszoKer ker) {
        String valasz = svc.letrehozJelszoToken(ker.getEmail());
        return ResponseEntity.ok(valasz);
    }

    // -- POST /api/jelszo/uj  (token + uj jelszo)
    @PostMapping("/jelszo/uj")
    public ResponseEntity<?> ujJelszo(@Valid @RequestBody UjJelszoKer ker) {
        String eredmeny = svc.beallitUjJelszo(ker.getToken(), ker.getUjJelszo());
        if ("siker".equals(eredmeny)) {
            return ResponseEntity.ok("siker");
        }
        // lehet: "ervenytelen" | "lejart" | "hiba"
        return ResponseEntity.badRequest().body(eredmeny);
    }

    // -- POST /api/kijelentkezes  (egyszeru: kliens oldalon dobd el a tokent)
    @PostMapping("/kijelentkezes")
    public ResponseEntity<?> kijelentkezes() {
        return ResponseEntity.ok("ok");
    }
}
