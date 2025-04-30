package com.example.testapp.entities;

import com.example.testapp.serializer.CoursSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Cours")
@JsonSerialize(using = CoursSerializer.class)
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCour;
    private String titreCours;
    private String descriptionCours;
    private String imagePath;

    public Cours(String titreCours, String descriptionCours,String imagePath ,Tuteur tuteur, List<Chapitre> chapitres, List<Etudiant> etudiants) {
        this.titreCours = titreCours;
        this.descriptionCours = descriptionCours;
        this.imagePath = imagePath;
        this.tuteur = tuteur;
        this.chapitres = chapitres;
        this.etudiants = etudiants;
    }

    public Cours() {

    }


    @Override
    public String toString() {
        return "Cours{" +
                "idCour=" + idCour +
                ", titreCours='" + titreCours + '\'' +
                ", descriptionCours='" + descriptionCours + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", tuteur=" + (tuteur != null ? tuteur.getNomTuteur() : "null") +
                ", chapitres=" + (chapitres != null ? chapitres.size() : 0) +
                ", etudiants=" + (etudiants != null ? etudiants.size() : 0) +
                '}';
    }


    public Long getIdCour() {
        return idCour;
    }

    public String getTitreCours() {
        return titreCours;
    }

    public String getDescriptionCours() {
        return descriptionCours;
    }


    public Tuteur getTuteur() {
        return tuteur;
    }

    public List<Chapitre> getChapitres() {
        return chapitres;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setIdCour(Long idCour) {
        this.idCour = idCour;
    }

    public void setTitreCours(String titreCours) {
        this.titreCours = titreCours;
    }

    public void setDescriptionCours(String descriptionCours) {
        this.descriptionCours = descriptionCours;
    }

    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
    }

    public void setChapitres(List<Chapitre> chapitres) {
        this.chapitres = chapitres;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @ManyToOne
    @JoinColumn(name = "tuteur_id")
    private Tuteur tuteur;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    private List<Chapitre> chapitres;

    @ManyToMany(mappedBy = "coursInscrits")
    private List<Etudiant> etudiants;

}
