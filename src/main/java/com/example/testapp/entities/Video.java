package com.example.testapp.entities;

import com.example.testapp.serializer.VideoSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Video")
@JsonSerialize(using = VideoSerializer.class)
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;

    public Video(Long id, String titre, Chapitre chapitre) {
        this.id = id;
        this.titre = titre;
        this.chapitre = chapitre;
    }

    public Video(){
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", chapitre=" + chapitre +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public Chapitre getChapitre() {
        return chapitre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setChapitre(Chapitre chapitre) {
        this.chapitre = chapitre;
    }

    @OneToOne
    @JoinColumn(name = "chapitre_id")
    private Chapitre chapitre;

}
