// hu/szakdolgozat/azonositas/modell/dok/PlaylistDokumentum.java
package hu.szakdolgozat.azonositas.modell.dok;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "playlistek")
public class PlaylistDokumentum {

    @Id
    private String id;

    @Field("email")
    private String email;

    @Field("nev")
    private String nev;

    @Field("szamok")
    private List<SzamAdat> szamok;

    @Field("letrehozva")
    private LocalDateTime letrehozva;

    // ÚJ: playlist típus (alapértelmezetten "eredeti"; módosított példány: "dns_mod")
    @Field("tipus")
    private String tipus = "eredeti";

    // ÚJ: ha dns_mod, itt hivatkozunk az eredetire
    @Field("derivedFrom")
    private String derivedFrom;

    // ÚJ: legutóbbi DNS elemzés mentése (Ortiz vagy más módszer)
    @Field("dns")
    private DnsProfil dns;

    // ÚJ: ha módosított (dns_mod), itt tároljuk a választott célhangulatot
    @Field("celHangulat")
    private String celHangulat;

    // --- Alap konstruktor
    public PlaylistDokumentum() {
        this.id = UUID.randomUUID().toString();
        this.letrehozva = LocalDateTime.now();
    }

    // --- Fő konstruktor (eredeti playlisthez)
    public PlaylistDokumentum(String email, String nev, List<SzamAdat> szamok) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.nev = nev;
        this.szamok = szamok;
        this.letrehozva = LocalDateTime.now();
        this.tipus = "eredeti";
    }

    // --- Kiegészítő konstruktor (származtatott / dns_mod)
    public PlaylistDokumentum(String email, String nev, List<SzamAdat> szamok, String tipus, String derivedFrom) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.nev = nev;
        this.szamok = szamok;
        this.letrehozva = LocalDateTime.now();
        this.tipus = (tipus == null || tipus.isBlank()) ? "eredeti" : tipus;
        this.derivedFrom = derivedFrom;
    }

    // --- Getterek / Setterek
    public String getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNev() { return nev; }
    public void setNev(String nev) { this.nev = nev; }

    public List<SzamAdat> getSzamok() { return szamok; }
    public void setSzamok(List<SzamAdat> szamok) { this.szamok = szamok; }

    public LocalDateTime getLetrehozva() { return letrehozva; }
    public void setLetrehozva(LocalDateTime letrehozva) { this.letrehozva = letrehozva; }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public String getDerivedFrom() { return derivedFrom; }
    public void setDerivedFrom(String derivedFrom) { this.derivedFrom = derivedFrom; }

    public DnsProfil getDns() { return dns; }
    public void setDns(DnsProfil dns) { this.dns = dns; }

    public String getCelHangulat() { return celHangulat; }
    public void setCelHangulat(String celHangulat) { this.celHangulat = celHangulat; }

    // --- Belső osztály a zeneszámokhoz
    public static class SzamAdat {
        private String eloado;
        private String cim;

        public SzamAdat() {}

        public SzamAdat(String eloado, String cim) {
            this.eloado = eloado;
            this.cim = cim;
        }

        public String getEloado() { return eloado; }
        public void setEloado(String eloado) { this.eloado = eloado; }

        public String getCim() { return cim; }
        public void setCim(String cim) { this.cim = cim; }
    }
}
