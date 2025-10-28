// ez itt a felhasznalo tarolo (Mongo repo)
package hu.szakdolgozat.azonositas.tarolo;

import hu.szakdolgozat.azonositas.modell.Felhasznalo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FelhasznaloTarolo extends MongoRepository<Felhasznalo, String> {
    Optional<Felhasznalo> findByEmail(String email);
}
