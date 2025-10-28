// ez itt a regisztracio kerese
package hu.szakdolgozat.azonositas.modell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisztralKer {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "A jelszo legyen legalabb 6 karakter")
    private String jelszo;

    private String nev; // opcionális

    // getter/setter – egyszeruen
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getJelszo() { return jelszo; }
    public void setJelszo(String jelszo) { this.jelszo = jelszo; }

    public String getNev() { return nev; }
    public void setNev(String nev) { this.nev = nev; }
}
