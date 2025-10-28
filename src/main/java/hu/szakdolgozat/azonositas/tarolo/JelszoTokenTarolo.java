// ez itt a jelszo token tarolo
package hu.szakdolgozat.azonositas.tarolo;

import hu.szakdolgozat.azonositas.modell.JelszoToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JelszoTokenTarolo extends MongoRepository<JelszoToken, String> {

    Optional<JelszoToken> findByToken(String token);

    void deleteByFelhasznaloId(String felhasznaloId);
}
