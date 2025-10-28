// ez itt a playlist tarolo (Mongo repository) – egyszeru lekerdezesek
package hu.szakdolgozat.azonositas.tarolo;

import hu.szakdolgozat.azonositas.modell.dok.PlaylistDokumentum;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistTarolo extends MongoRepository<PlaylistDokumentum, String> {

    // osszes playlist egy emailhez, legujabb elol
    List<PlaylistDokumentum> findByEmailOrderByLetrehozvaDesc(String email);

    // egy playlist id + email alapjan (tulajdon ellenorzeshez)
    Optional<PlaylistDokumentum> findByIdAndEmail(String id, String email);
}
