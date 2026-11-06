
package de.greenbyte.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import de.greenbyte.backend.service.BerechnungsService;
import de.greenbyte.backend.repository.VerbrauchswertRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class GreenByteController {

private final BerechnungsService berechnungsService;
private final VerbrauchswertRepository repository;


public GreenByteController(
        BerechnungsService berechnungsService,
        VerbrauchswertRepository repository) {

    this.berechnungsService = berechnungsService;
    this.repository = repository;
}



    @GetMapping("/api/status")
    public String status() {
        return "GreenByte Backend läuft!";
    }
    

    @PostMapping("/api/berechnung")
    public BerechnungsErgebnis berechnen(
            @RequestBody BerechnungsAnfrage anfrage) {

                
                var verbrauchswert = repository
                        .findByKategorie(anfrage.geraet())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Für die Kategorie '" + anfrage.geraet()
                                        + "' sind noch keine Verbrauchsdaten vorhanden."
                        ));


double energieKwh = berechnungsService.berechneEnergie(
        verbrauchswert.getLeistungWatt(),
        anfrage.stunden());

double co2Kg = berechnungsService.berechneCo2(energieKwh);

double wasserLiter = berechnungsService.berechneWasser(energieKwh);


        return new BerechnungsErgebnis(
                anfrage.geraet(),
                anfrage.stunden(),
                energieKwh,
                co2Kg,
                wasserLiter
        );
    }
    

public record BerechnungsAnfrage(
        String geraet,
        int stunden
) {}


public record BerechnungsErgebnis(
        String geraet,
        int stunden,
        double energieKwh,
        double co2kg,
        double wasserLiter

) {}

}

