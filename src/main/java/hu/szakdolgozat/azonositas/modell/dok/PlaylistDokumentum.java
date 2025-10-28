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

    // --- Alap konstruktor
    public PlaylistDokumentum() {
        this.id = UUID.randomUUID().toString();
        this.letrehozva = LocalDateTime.now();
    }

    // --- Fő konstruktor
    public PlaylistDokumentum(String email, String nev, List<SzamAdat> szamok) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.nev = nev;
        this.szamok = szamok;
        this.letrehozva = LocalDateTime.now();
    }

    // --- Getterek / Setterek
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public List<SzamAdat> getSzamok() {
        return szamok;
    }

    public void setSzamok(List<SzamAdat> szamok) {
        this.szamok = szamok;
    }

    public LocalDateTime getLetrehozva() {
        return letrehozva;
    }

    public void setLetrehozva(LocalDateTime letrehozva) {
        this.letrehozva = letrehozva;
    }

    // --- Belső osztály a zeneszámokhoz
    public static class SzamAdat {
        private String eloado;
        private String cim;

        public SzamAdat() {}

        public SzamAdat(String eloado, String cim) {
            this.eloado = eloado;
            this.cim = cim;
        }

        public String getEloado() {
            return eloado;
        }

        public void setEloado(String eloado) {
            this.eloado = eloado;
        }

        public String getCim() {
            return cim;
        }

        public void setCim(String cim) {
            this.cim = cim;
        }
    }
}
