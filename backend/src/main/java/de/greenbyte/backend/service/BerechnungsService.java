
package de.greenbyte.backend.service;

import org.springframework.stereotype.Service;

@Service
public class BerechnungsService {

    
    // UBA (2026): Deutscher Strommix 2025,
    // Treibhausgasemissionen inklusive Vorketten (vorläufig).
    // 406 g CO₂e/kWh = 0,406 kg CO₂e/kWh.
    private static final double CO2_FAKTOR = 0.406;


    public double berechneEnergie(double leistungWatt, int stunden) {
        return (leistungWatt * stunden) / 1000.0;
    }

    public double berechneCo2(double energieKwh) {
        return energieKwh * CO2_FAKTOR;
    }

    // EnBW Nachhaltigkeitskennzahlen 2025:
    // Wasserverbrauchsintensität: 0,41 Liter/kWh.
    // Unternehmensbezogener Referenzwert, kein
    // Durchschnitt des deutschen Strommixes.
    private static final double WASSER_FAKTOR = 0.41;

    public double berechneWasser(double energieKwh) {
        return energieKwh * WASSER_FAKTOR;
    }

}
