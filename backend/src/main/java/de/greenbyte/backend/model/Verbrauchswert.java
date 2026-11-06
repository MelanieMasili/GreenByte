package de.greenbyte.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Verbrauchswert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kategorie;

    public String getKategorie() {
        return kategorie;
    }


    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }


    public Double getLeistungWatt() {
        return leistungWatt;
    }


    public void setLeistungWatt(Double leistungWatt) {
        this.leistungWatt = leistungWatt;
    }


    public String getQuelle() {
        return quelle;
    }


    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }


    public String getNutzungsszenario() {
        return nutzungsszenario;
    }


    public void setNutzungsszenario(String nutzungsszenario) {
        this.nutzungsszenario = nutzungsszenario;
    }


    public String getModellannahme() {
        return modellannahme;
    }


    public void setModellannahme(String modellannahme) {
        this.modellannahme = modellannahme;
    }


    private Double leistungWatt;

    private String quelle;
    private String nutzungsszenario;
    private String modellannahme;


    public Verbrauchswert() {
    }
}
