// hu/szakdolgozat/azonositas/modell/dto/DnsElemzesValasz.java
package hu.szakdolgozat.azonositas.modell.dto;

import java.util.Map;

public class DnsElemzesValasz {
    private String modszer;
    private String induloHangulat;
    private Map<String, Double> profil;

    public DnsElemzesValasz() {}
    public DnsElemzesValasz(String modszer, String induloHangulat, Map<String, Double> profil) {
        this.modszer = modszer; this.induloHangulat = induloHangulat; this.profil = profil;
    }

    public String getModszer() { return modszer; }
    public void setModszer(String modszer) { this.modszer = modszer; }
    public String getInduloHangulat() { return induloHangulat; }
    public void setInduloHangulat(String induloHangulat) { this.induloHangulat = induloHangulat; }
    public Map<String, Double> getProfil() { return profil; }
    public void setProfil(Map<String, Double> profil) { this.profil = profil; }
}
