
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

        speichereWennNichtVorhanden(
            "Laptop",
            12.5,
            "Notebookcheck – Lenovo ThinkPad T14, Leistungsmessungen",
            "80 % Leerlauf, 20 % Last",
            "Vorläufiger Modellwert: 12,5 Watt; angenommene Nutzungsverteilung"
        );

        speichereWennNichtVorhanden(
            "Desktop-PC",
            67.0,
            "Notebookcheck – Desktop versus Notebook (2015)",
            "80 % Leerlauf, 20 % höhere Auslastung",
            "Vorläufiger Modellwert: 67 Watt; Monitor nicht berücksichtigt"
        );

        speichereWennNichtVorhanden(
            "Konsole",
            154.0,
            "Microsoft Game Dev – Gaming Sustainability Lab Platform Baselines",
            "Xbox Series X – aktives Gameplay",
            "Referenzwert 154 Watt; ohne Fernseher und Standby"
        );

        speichereWennNichtVorhanden(
            "Streaming",
            77.0,
            "IEA – The carbon footprint of streaming video (2020)",
            "Eine Stunde Videostreaming",
            "77 Watt Äquivalentleistung; IEA-Modellwert für 2019 inklusive Endgerät, Netzwerk und Rechenzentrum"
        );

        speichereWennNichtVorhanden(
            "Smartphone",
            3.65,
            "Vorläufige Literaturannahme: 4 kWh/Jahr",
            "3 angenommene aktive Nutzungsstunden pro Tag",
            "4000 Wh / (365 x 3 h) = ca. 3,65 W Äquivalentleistung; ohne Herstellung und Netzwerkinfrastruktur"
        );

        System.out.println("GreenByte: Dateninitialisierung abgeschlossen!");
    }

    private void speichereWennNichtVorhanden(
            String kategorie,
            double leistungWatt,
            String quelle,
            String nutzungsszenario,
            String modellannahme) {

        if (repository.findByKategorie(kategorie).isPresent()) {
            System.out.println(
                "GreenByte: " + kategorie + " bereits vorhanden."
            );
            return;
        }

        Verbrauchswert wert = new Verbrauchswert();

        wert.setKategorie(kategorie);
        wert.setLeistungWatt(leistungWatt);
        wert.setQuelle(quelle);
        wert.setNutzungsszenario(nutzungsszenario);
        wert.setModellannahme(modellannahme);

        repository.save(wert);

        System.out.println(
            "GreenByte: " + kategorie + " gespeichert!"
        );
    }
}
