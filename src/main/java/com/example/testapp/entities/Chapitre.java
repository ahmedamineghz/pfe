package com.example.testapp.entities;

import com.example.testapp.serializer.ChapitreSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="Chapitre")
@JsonSerialize(using = ChapitreSerializer.class)
public class Chapitre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long idChapitre;
    private String titreChapitre;
    private String typeChapitre;
    private String contenuChapitre;


    public Chapitre(Long idChapitre, String titreChapitre, String typeChapitre, String contenuChapitre, Cours cours, Video video) {
        this.idChapitre = idChapitre;
        this.titreChapitre = titreChapitre;
        this.typeChapitre = typeChapitre;
        this.contenuChapitre = contenuChapitre;
        this.cours = cours;
        this.video = video;
    }

    public Chapitre(){

    }
    @Override
    public String toString() {
        return "Chapitre{" +
                "idChapitre=" + idChapitre +
                ", titreChapitre='" + titreChapitre + '\'' +
                ", typeChapitre='" + typeChapitre + '\'' +
                ", contenuChapitre='" + contenuChapitre + '\'' +
                ", cours=" + cours +
                ", video=" + video +
                '}';
    }

    public Long getIdChapitre() {
        return idChapitre;
    }

    public String getTitreChapitre() {
        return titreChapitre;
    }

    public String getTypeChapitre() {
        return typeChapitre;
    }

    public String getContenuChapitre() {
        return contenuChapitre;
    }

    public Cours getCours() {
        return cours;
    }

    public Video getVideo() {
        return video;
    }

    public void setIdChapitre(Long idChapitre) {
        this.idChapitre = idChapitre;
    }

    public void setTitreChapitre(String titreChapitre) {
        this.titreChapitre = titreChapitre;
    }

    public void setTypeChapitre(String typeChapitre) {
        this.typeChapitre = typeChapitre;
    }

    public void setContenuChapitre(String contenuChapitre) {
        this.contenuChapitre = contenuChapitre;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @ManyToOne
    @JoinColumn(name = "cours_id")
    private Cours cours;

    @OneToOne(mappedBy = "chapitre", cascade = CascadeType.ALL)
    private Video video;
}
