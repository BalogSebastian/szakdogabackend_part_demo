// hu/szakdolgozat/azonositas/vezerlo/DnsVezerlo.java
package hu.szakdolgozat.azonositas.vezerlo;

import hu.szakdolgozat.azonositas.modell.dto.*;
import hu.szakdolgozat.azonositas.modell.dok.DnsProfil;
import hu.szakdolgozat.azonositas.modell.dok.PlaylistDokumentum;
import hu.szakdolgozat.azonositas.szolgaltatas.DnsSzolgaltatas;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dns")
public class DnsVezerlo {

    private final DnsSzolgaltatas svc;

    public DnsVezerlo(DnsSzolgaltatas svc) {
        this.svc = svc;
    }

    // POST /api/dns/elemzes  {playlistId, email}
    @PostMapping("/elemzes")
    public ResponseEntity<?> elemzes(@Valid @RequestBody DnsElemzesKer ker) {
        DnsProfil dns = svc.elemezOrtiz(ker.getPlaylistId(), ker.getEmail());
        if (dns == null) return ResponseEntity.badRequest().body("nincs_ilyen_playlist_vagy_nem_a_tied");

        return ResponseEntity.ok(
                new DnsElemzesValasz(dns.getModszer(), dns.getInduloHangulat(), dns.getProfil())
        );
    }

    // POST /api/dns/modosit  {playlistId, email, celHangulat, csereArany?}
    @PostMapping("/modosit")
    public ResponseEntity<?> modosit(@Valid @RequestBody DnsModositKer ker) {
        double arany = ker.getCsereArany() == null ? 0.3 : ker.getCsereArany();
        PlaylistDokumentum uj = svc.modositOrtiz(ker.getPlaylistId(), ker.getEmail(), ker.getCelHangulat(), arany);
        if (uj == null) return ResponseEntity.badRequest().body("nincs_ilyen_playlist_vagy_uress");

        return ResponseEntity.ok(new DnsModositValasz(uj.getId(), uj.getNev()));
    }
}
