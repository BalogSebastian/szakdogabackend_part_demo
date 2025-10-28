// ez itt az elfelejtett jelszo kerese (email alapjan)
package hu.szakdolgozat.azonositas.modell.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ElfelejtettJelszoKer {

    @Email
    @NotBlank
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
