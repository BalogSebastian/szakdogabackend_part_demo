// ez itt az elfelejtett jelszo token (egyszeru)
package hu.szakdolgozat.azonositas.modell;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("jelszo_tokenek")
public class JelszoToken {

    @Id
    private String id;

    private String felhasznaloId;

    private String token; // random uuid string

    private Instant lejar; // pl. most + 30 perc

    // -- getter/setter egyszeruen
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFelhasznaloId() { return felhasznaloId; }
    public void setFelhasznaloId(String felhasznaloId) { this.felhasznaloId = felhasznaloId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Instant getLejar() { return lejar; }
    public void setLejar(Instant lejar) { this.lejar = lejar; }
}
