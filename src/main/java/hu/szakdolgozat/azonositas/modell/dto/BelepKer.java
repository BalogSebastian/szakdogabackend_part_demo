// ez itt a belepes kerese
package hu.szakdolgozat.azonositas.modell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class BelepKer {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String jelszo;

    // getter/setter – egyszeruen
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getJelszo() { return jelszo; }
    public void setJelszo(String jelszo) { this.jelszo = jelszo; }
}
