
package de.greenbyte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.greenbyte.backend.service.BerechnungsService;

class BerechnungsServiceTest {

    private final BerechnungsService service =
            new BerechnungsService();

    @Test
    void berechnetEnergieverbrauchKorrekt() {
        double ergebnis = service.berechneEnergie(50.0, 3);

        assertEquals(0.15, ergebnis, 0.000001);
    }

    @Test
    void berechnetCo2EmissionenKorrekt() {
        double ergebnis = service.berechneCo2(0.15);

        // 0,15 kWh × 0,406 kg CO₂e/kWh = 0,0609 kg CO₂e
        assertEquals(0.0609, ergebnis, 0.000001);
    }


    @Test
    void berechnetWasserverbrauchKorrekt() {
        double ergebnis = service.berechneWasser(0.15);

        // 0,15 kWh × 0,41 Liter/kWh = 0,0615 Liter
        assertEquals(0.0615, ergebnis, 0.000001);
    }

}
