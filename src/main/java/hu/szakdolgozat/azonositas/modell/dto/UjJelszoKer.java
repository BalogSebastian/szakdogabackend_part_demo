// ez itt az uj jelszo beallitasa (token + uj jelszo)
package hu.szakdolgozat.azonositas.modell.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UjJelszoKer {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6)
    private String ujJelszo;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUjJelszo() { return ujJelszo; }
    public void setUjJelszo(String ujJelszo) { this.ujJelszo = ujJelszo; }
}
