// hu/szakdolgozat/azonositas/modell/dto/DnsModositKer.java
package hu.szakdolgozat.azonositas.modell.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class DnsModositKer {
    @NotBlank
    private String playlistId;

    @NotBlank
    private String email;

    @NotBlank
    private String celHangulat;

    // 0.1–0.6 tartományban, ha nincs megadva -> 0.3
    @Positive
    private Double csereArany;

    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCelHangulat() { return celHangulat; }
    public void setCelHangulat(String celHangulat) { this.celHangulat = celHangulat; }
    public Double getCsereArany() { return csereArany; }
    public void setCsereArany(Double csereArany) { this.csereArany = csereArany; }
}
