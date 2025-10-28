// ez itt a fo logika – regisztral, belep, elfelejtett jelszo
package hu.szakdolgozat.azonositas.szolgaltatas;

import hu.szakdolgozat.azonositas.modell.Felhasznalo;
import hu.szakdolgozat.azonositas.modell.JelszoToken;
import hu.szakdolgozat.azonositas.modell.dto.BelepKer;
import hu.szakdolgozat.azonositas.modell.dto.RegisztralKer;
import hu.szakdolgozat.azonositas.tarolo.FelhasznaloTarolo;
import hu.szakdolgozat.azonositas.tarolo.JelszoTokenTarolo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AzonositasSzolgaltatas {

    private final FelhasznaloTarolo felhasznaloTarolo;
    private final JelszoTokenTarolo jelszoTokenTarolo;
    private final PasswordEncoder jelszoKodolo;

    public AzonositasSzolgaltatas(FelhasznaloTarolo felhasznaloTarolo,
                                  JelszoTokenTarolo jelszoTokenTarolo,
                                  PasswordEncoder jelszoKodolo) {
        this.felhasznaloTarolo = felhasznaloTarolo;
        this.jelszoTokenTarolo = jelszoTokenTarolo;
        this.jelszoKodolo = jelszoKodolo;
    }

    // -- regisztracio: ha az email foglalt, "mar_letezik", kulonben "siker"
    public String regisztral(RegisztralKer ker) {
        String email = normalizalEmail(ker.getEmail());
        Optional<Felhasznalo> letezo = felhasznaloTarolo.findByEmail(email);
        if (letezo.isPresent()) {
            return "mar_letezik";
        }

        Felhasznalo f = new Felhasznalo();
        f.setEmail(email);
        f.setNev(ker.getNev());
        f.setJelszoHash(jelszoKodolo.encode(ker.getJelszo()));
        felhasznaloTarolo.save(f);

        return "siker";
    }

    // -- belepes: ha ok, visszaad egy egyszeru "token_..." sztringet, kulonben "hiba"
    public String belep(BelepKer ker) {
        String email = normalizalEmail(ker.getEmail());
        Optional<Felhasznalo> talalat = felhasznaloTarolo.findByEmail(email);
        if (talalat.isEmpty()) {
            return "hiba";
        }
        Felhasznalo f = talalat.get();
        if (!jelszoKodolo.matches(ker.getJelszo(), f.getJelszoHash())) {
            return "hiba";
        }
        // NEM JWT – csak fejleszteshez egy egyszeru token (frontend majd tarolja)
        return "token_" + UUID.randomUUID();
    }

    // -- elfelejtett jelszo: letrehoz egy egyszeru reset tokent (30 percig jo)
    public String letrehozJelszoToken(String email) {
        String norm = normalizalEmail(email);
        Optional<Felhasznalo> talalat = felhasznaloTarolo.findByEmail(norm);
        if (talalat.isEmpty()) {
            // ne fecsegjunk: nem aruljuk el, hogy nincs ilyen email
            return "ok";
        }

        Felhasznalo f = talalat.get();

        // regi tokenek torlese ehhez a userhez (egy token elv)
        jelszoTokenTarolo.deleteByFelhasznaloId(f.getId());

        // uj token
        JelszoToken t = new JelszoToken();
        t.setFelhasznaloId(f.getId());
        t.setToken(UUID.randomUUID().toString());
        t.setLejar(Instant.now().plus(30, ChronoUnit.MINUTES));
        jelszoTokenTarolo.save(t);

        // fejleszteshez visszaadjuk magat a tokent (kesőbb email)
        return t.getToken();
    }

    // -- uj jelszo beallitasa token alapjan
    public String beallitUjJelszo(String token, String ujJelszo) {
        Optional<JelszoToken> talalat = jelszoTokenTarolo.findByToken(token);
        if (talalat.isEmpty()) {
            return "ervenytelen";
        }
        JelszoToken t = talalat.get();

        if (t.getLejar() == null || t.getLejar().isBefore(Instant.now())) {
            return "lejart";
        }

        Optional<Felhasznalo> felh = felhasznaloTarolo.findById(t.getFelhasznaloId());
        if (felh.isEmpty()) {
            return "hiba";
        }

        Felhasznalo f = felh.get();
        f.setJelszoHash(jelszoKodolo.encode(ujJelszo));
        felhasznaloTarolo.save(f);

        // token eldobasa, hogy egyszer hasznalhato legyen
        jelszoTokenTarolo.deleteByFelhasznaloId(f.getId());

        return "siker";
    }

    // -- kis seged: email trim + kisbetu
    private String normalizalEmail(String raw) {
        return raw == null ? null : raw.trim().toLowerCase();
    }
}
