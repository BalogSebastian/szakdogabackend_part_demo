// hu/szakdolgozat/azonositas/modell/dto/DnsModositValasz.java
package hu.szakdolgozat.azonositas.modell.dto;

public class DnsModositValasz {
    private String ujPlaylistId;
    private String ujNev;

    public DnsModositValasz() {}
    public DnsModositValasz(String ujPlaylistId, String ujNev) {
        this.ujPlaylistId = ujPlaylistId;
        this.ujNev = ujNev;
    }

    public String getUjPlaylistId() { return ujPlaylistId; }
    public void setUjPlaylistId(String ujPlaylistId) { this.ujPlaylistId = ujPlaylistId; }
    public String getUjNev() { return ujNev; }
    public void setUjNev(String ujNev) { this.ujNev = ujNev; }
}
