
package de.greenbyte.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import de.greenbyte.backend.GreenByteController;
import de.greenbyte.backend.model.Verbrauchswert;
import de.greenbyte.backend.repository.VerbrauchswertRepository;
import de.greenbyte.backend.service.BerechnungsService;

class GreenByteControllerTest {

    @Test
    void berechnetEnergieMitWertAusRepository() {

        // Datenbankzugriff simulieren
        VerbrauchswertRepository repository =
                mock(VerbrauchswertRepository.class);

        // Testdatensatz mit 80 Watt
        Verbrauchswert laptop = new Verbrauchswert();
        laptop.setKategorie("Laptop");
        laptop.setLeistungWatt(80.0);

        when(repository.findByKategorie("Laptop"))
                .thenReturn(Optional.of(laptop));

        // Controller mit echtem Berechnungsservice
        GreenByteController controller =
                new GreenByteController(
                        new BerechnungsService(),
                        repository
                );

        // Berechnung für drei Stunden
        var anfrage = new GreenByteController.BerechnungsAnfrage(
                "Laptop", 3
        );

        var ergebnis = controller.berechnen(anfrage);

        // Erwartet: 80 Watt × 3 Stunden / 1000
        assertEquals(0.24, ergebnis.energieKwh(), 0.000001);
    }
}
