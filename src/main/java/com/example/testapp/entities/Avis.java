package com.example.testapp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Avis")
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvis;
    private String commentaireAvis;

    public Avis(Long idAvis, String commentaireAvis, Etudiant etudiant, Cours cours) {
        this.idAvis = idAvis;
        this.commentaireAvis = commentaireAvis;
        this.etudiant = etudiant;
        this.cours = cours;
    }
    public Avis(){

    }

    @Override
    public String toString() {
        return "Avis{" +
                "idAvis=" + idAvis +
                ", commentaireAvis='" + commentaireAvis + '\'' +
                ", etudiant=" + etudiant +
                ", cours=" + cours +
                '}';
    }

    public Long getIdAvis() {
        return idAvis;
    }

    public String getCommentaireAvis() {
        return commentaireAvis;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public Cours getCours() {
        return cours;
    }

    public void getIdAvis(Long idAvis) {
        this.idAvis = idAvis;
    }

    public void setCommentaireAvis(String commentaireAvis) {
        this.commentaireAvis = commentaireAvis;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "cours_id")
    private Cours cours;


}
