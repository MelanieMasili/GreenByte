package de.greenbyte.backend.repository;


import de.greenbyte.backend.model.Verbrauchswert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerbrauchswertRepository
        extends JpaRepository<Verbrauchswert, Long> {
                
        Optional<Verbrauchswert> findByKategorie(String kategorie);
        
}
