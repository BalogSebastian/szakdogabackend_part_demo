// hu/szakdolgozat/azonositas/modell/dto/DnsElemzesKer.java
package hu.szakdolgozat.azonositas.modell.dto;

import jakarta.validation.constraints.NotBlank;

public class DnsElemzesKer {
    @NotBlank
    private String playlistId;

    @NotBlank
    private String email;

    // opcionális, most fixen "ortiz"
    private String modszer = "ortiz";

    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getModszer() { return modszer; }
    public void setModszer(String modszer) { this.modszer = modszer; }
}
