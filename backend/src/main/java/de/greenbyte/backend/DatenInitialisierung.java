
package de.greenbyte.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.greenbyte.backend.model.Verbrauchswert;
import de.greenbyte.backend.repository.VerbrauchswertRepository;

@Component
public class DatenInitialisierung implements CommandLineRunner {

    private final VerbrauchswertRepository repository;

    public DatenInitialisierung(VerbrauchswertRepository repository) {
        this.repository = repository;
    }

    

@Override
public void run(String... args) {

    if (repository.findByKategorie("Laptop").isEmpty()) {

        Verbrauchswert laptop = new Verbrauchswert();

        laptop.setKategorie("Laptop");
        laptop.setLeistungWatt(50.0);
        laptop.setQuelle("TESTDATEN – kein wissenschaftlicher Wert");
        laptop.setNutzungsszenario("Technischer Funktionstest");
        laptop.setModellannahme("Fiktive Leistung von 50 Watt");

        repository.save(laptop);

        System.out.println("GreenByte: Laptop-Testdatensatz gespeichert!");

    } else {
        System.out.println("GreenByte: Laptop-Datensatz bereits vorhanden.");
    }
}
}
