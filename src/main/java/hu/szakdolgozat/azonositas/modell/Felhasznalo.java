// ez itt a felhasznalo dokumentum (Mongo)
package hu.szakdolgozat.azonositas.modell;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("felhasznalok")
public class Felhasznalo {

    @Id
    private String id;
// kis string modositas
    //kis valami más is
    @Indexed(unique = true)
    private String email;

    private String jelszoHash;

    private String nev; // opcionális, megjelenítéshez

    // -- kesobb ide jonnek a sajat dolgai (pl. dashboard azonosítok)

    // -- getter/setter (egyszeruen)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email == null ? null : email.trim().toLowerCase(); }

    public String getJelszoHash() { return jelszoHash; }
    public void setJelszoHash(String jelszoHash) { this.jelszoHash = jelszoHash; }

    public String getNev() { return nev; }
    public void setNev(String nev) { this.nev = nev; }
}
