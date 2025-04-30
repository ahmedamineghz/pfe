package com.example.testapp.controller;

import com.example.testapp.dto.ApiResponse;
import com.example.testapp.entities.Cours;
import com.example.testapp.entities.Tuteur;
import com.example.testapp.services.CoursInterface;
import com.example.testapp.services.TuteurInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/cours")
public class CoursController {

    private static final String UPLOAD_DIR = "uploads/images";

    @Autowired
    private CoursInterface coursInterface;

    @Autowired
    private TuteurInterface tuteurInterface;

    // ✅ Ajouter un cours → TUTEUR
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('TUTEUR')") 
    public ApiResponse<Cours> addCours(
            @RequestParam("titreCours") String titreCours,
            @RequestParam("descriptionCours") String descriptionCours,
            @RequestParam("tuteur_id") Long tuteurId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {
            String imagePath = saveImage(imageFile);
            Tuteur tuteur = tuteurInterface.getTuteurById(tuteurId);
            Cours cours = new Cours(
                    titreCours,
                    descriptionCours,
                    imagePath,
                    tuteur,
                    new ArrayList<>(),
                    new ArrayList<>()
            );

            Cours savedCours = coursInterface.addCours(cours);
            return new ApiResponse<>(true, "Cours ajouté avec succès", savedCours);

        } catch (IOException e) {
            return new ApiResponse<>(false, "Erreur lors de l'upload de l'image : " + e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de l'ajout du cours : " + e.getMessage(), null);
        }
    }

    // ✅ Modifier un cours → TUTEUR ou ADMIN
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('TUTEUR', 'ADMIN')")
    public ApiResponse<Cours> updateCours(
            @PathVariable Long id,
            @RequestParam("titreCours") String titreCours,
            @RequestParam("descriptionCours") String descriptionCours,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            Cours existingCours = coursInterface.getById(id);
            if (existingCours == null) {
                return new ApiResponse<>(false, "Cours introuvable", null);
            }

            existingCours.setTitreCours(titreCours);
            existingCours.setDescriptionCours(descriptionCours);

            // Remplacer l'image si une nouvelle est fournie
            if (imageFile != null && !imageFile.isEmpty()) {
                // Supprimer l'ancienne image
                String oldImagePath = existingCours.getImagePath();
                if (oldImagePath != null) {
                    Path oldPath = Paths.get(UPLOAD_DIR).resolve(oldImagePath);
                    if (Files.exists(oldPath)) {
                        Files.delete(oldPath);
                    }
                }

                // Enregistrer la nouvelle
                String newImagePath = saveImage(imageFile);
                existingCours.setImagePath(newImagePath);
            }

            Cours updatedCours = coursInterface.updateCours(id, existingCours);
            return new ApiResponse<>(true, "Cours mis à jour avec succès", updatedCours);

        } catch (IOException e) {
            return new ApiResponse<>(false, "Erreur image : " + e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur : " + e.getMessage(), null);
        }
    }

    // ✅ Supprimer un cours
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('TUTEUR', 'ADMIN')")
    public ApiResponse<Void> deleteCours(@PathVariable Long id) {
        try {
            Cours cours = coursInterface.getById(id);
            if (cours == null) {
                return new ApiResponse<>(false, "Cours introuvable", null);
            }

            // ✅ Supprimer le fichier image du dossier uploads/images
            String imagePath = cours.getImagePath(); // ex: images/xxx.jpg
            if (imagePath != null && !imagePath.isEmpty()) {
                Path imageFile = Paths.get(System.getProperty("user.dir"))
                        .resolve("uploads")
                        .resolve(imagePath); // imagePath est déjà "images/xxx.jpg"

                if (Files.exists(imageFile)) {
                    Files.delete(imageFile);
                }
            }

            // ✅ Supprimer le cours en base
            coursInterface.deleteCours(id);

            return new ApiResponse<>(true, "Cours supprimé avec succès", null);

        } catch (IOException e) {
            return new ApiResponse<>(false, "Erreur lors de la suppression de l'image : " + e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de la suppression du cours : " + e.getMessage(), null);
        }
    }


    // ✅ Lister tous les cours
    @GetMapping("/getAllCours")
    public ApiResponse<List<Cours>> getAllCours() {
        try {
            return new ApiResponse<>(true, "Liste des cours récupérée", coursInterface.getAllCours());
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur récupération des cours : " + e.getMessage(), null);
        }
    }

    // ✅ Récupérer un cours par ID
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ETUDIANT', 'TUTEUR', 'ADMIN')")
    public ApiResponse<Cours> getById(@PathVariable Long id) {
        Cours cours = coursInterface.getById(id);
        return (cours != null)
                ? new ApiResponse<>(true, "Cours trouvé", cours)
                : new ApiResponse<>(false, "Cours introuvable", null);
    }

    // ✅ Obtenir les cours d'un tuteur
    @GetMapping("/tuteur/{tuteurId}")
    @PreAuthorize("hasAnyAuthority('TUTEUR', 'ADMIN')")
    public ApiResponse<List<Cours>> getCoursByTuteur(@PathVariable Long tuteurId) {
        try {
            List<Cours> coursList = coursInterface.getCoursByTuteur(tuteurId);
            return new ApiResponse<>(true, "Cours du tuteur récupérés", coursList);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur récupération des cours du tuteur", null);
        }
    }

    // ✅ Méthode utilitaire d'enregistrement d'image
    private String saveImage(MultipartFile file) throws IOException {
        String baseDir = System.getProperty("user.dir");
        Path imageFolder = Paths.get(baseDir, UPLOAD_DIR);
        if (!Files.exists(imageFolder)) {
            Files.createDirectories(imageFolder);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path imagePath = imageFolder.resolve(fileName);
        file.transferTo(imagePath.toFile());

        return "images/"+fileName; // On stocke juste le nom du fichier
    }
}
