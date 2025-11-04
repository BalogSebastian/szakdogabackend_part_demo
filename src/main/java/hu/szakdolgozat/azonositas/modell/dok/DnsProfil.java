// hu/szakdolgozat/azonositas/modell/dok/DnsProfil.java
package hu.szakdolgozat.azonositas.modell.dok;

import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

public class DnsProfil {

    // pl. "ortiz"
    @Field("modszer")
    private String modszer;

    // pl. "deezer" | "heurisztika"
    @Field("forras")
    private String forras;

    // elemzés időpontja
    @Field("keszult")
    private Instant keszult = Instant.now();

    // hangulat-profil: {"Boldog":0.32, "Szomorú":0.48, ...} [0..1 összege ~1]
    @Field("profil")
    private Map<String, Double> profil;

    // opcionális: induló hangulat címke (pl. a max kategória)
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
