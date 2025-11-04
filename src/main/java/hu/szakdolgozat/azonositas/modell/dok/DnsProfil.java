// hu/szakdolgozat/azonositas/modell/dok/DnsProfil.java
package hu.szakdolgozat.azonositas.modell.dok;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

public class DnsProfil {

    @Field("modszer")
    private String modszer;

    @Field("forras")
    private String forras;

    @Field("keszult")
    private Instant keszult = Instant.now();

    @Field("profil")
    private Map<String, Double> profil;

    @Field("induloHangulat")
    private String induloHangulat;

    public DnsProfil() {}

    public DnsProfil(String modszer, String forras, Map<String, Double> profil, String induloHangulat) {
        this.modszer = modszer;
        this.forras = forras;
        this.keszult = Instant.now();
        this.profil = profil;
        this.induloHangulat = induloHangulat;
    }

    public String getModszer() { return modszer; }
    public void setModszer(String modszer) { this.modszer = modszer; }

    public String getForras() { return forras; }
    public void setForras(String forras) { this.forras = forras; }

    public Instant getKeszult() { return keszult; }
    public void setKeszult(Instant keszult) { this.keszult = keszult; }

    public Map<String, Double> getProfil() { return profil; }
    public void setProfil(Map<String, Double> profil) { this.profil = profil; }

    public String getInduloHangulat() { return induloHangulat; }
    public void setInduloHangulat(String induloHangulat) { this.induloHangulat = induloHangulat; }
}
