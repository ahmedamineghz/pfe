package com.example.testapp.controller;

import com.example.testapp.dto.ApiResponse;
import com.example.testapp.entities.Chapitre;
import com.example.testapp.entities.Cours;
import com.example.testapp.services.ChapitreIntreface;
import com.example.testapp.services.CoursInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping( "/chapitre")
public class ChapitreController {

    @Autowired
    private ChapitreIntreface chapitreIntreface;

    @Autowired
    private CoursInterface coursInterface;

    // ✅ Ajouter un chapitre (TUTEUR uniquement)
    @PostMapping("/addChapitre")
    @PreAuthorize("hasAuthority('TUTEUR')")
    public ApiResponse<Chapitre> addChapitre(@RequestBody Map<String, Object> requestData) {
        try {
            Long coursId = requestData.get("id_cour") != null
                    ? Long.valueOf(requestData.get("id_cour").toString()) : null;

            Cours cours = coursInterface.getCoursById(coursId);

            Chapitre chapitre = new Chapitre(
                    null,
                    requestData.get("titreChapitre").toString(),
                    requestData.get("typeChapitre").toString(),
                    requestData.get("contenuChapitre").toString(),
                    cours,
                    null
            );

            Chapitre saved = chapitreIntreface.addChapitre(chapitre);
            return new ApiResponse<>(true, "Chapitre ajouté avec succès", saved);

        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de l'ajout du chapitre : " + e.getMessage(), null);
        }
    }

    // ✅ Supprimer un chapitre (TUTEUR ou ADMIN)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('TUTEUR', 'ADMIN')")
    public ApiResponse<Void> deleteChapitre(@PathVariable Long id) {
        try {
            chapitreIntreface.deletChapitre(id);
            return new ApiResponse<>(true, "Chapitre supprimé avec succès", null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de la suppression : " + e.getMessage(), null);
        }
    }

    // ✅ Modifier un chapitre (TUTEUR ou ADMIN)
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('TUTEUR', 'ADMIN')")
    public ApiResponse<Chapitre> updateChappitre(@PathVariable Long id, @RequestBody Chapitre chapitre) {
        try {
            Chapitre updated = chapitreIntreface.updateChapitre(id, chapitre);
            return new ApiResponse<>(true, "Chapitre mis à jour", updated);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de la mise à jour", null);
        }
    }

    // ✅ Lister tous les chapitres (accessible à tous)
    @GetMapping("/getAllChapitre")
    @PreAuthorize("hasAnyAuthority('ETUDIANT', 'TUTEUR', 'ADMIN')")
    public ApiResponse<List<Chapitre>> getAllChapitre() {
        try {
            List<Chapitre> list = chapitreIntreface.getAllChapitre();
            return new ApiResponse<>(true, "Liste des chapitres récupérée", list);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de la récupération des chapitres", null);
        }
    }

    // ✅ Obtenir un chapitre par ID (accessible à tous)
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ETUDIANT', 'TUTEUR', 'ADMIN')")
    public ApiResponse<Chapitre> getChapitreById(@PathVariable Long id) {
        Chapitre chapitre = chapitreIntreface.getChapirteById(id);
        if (chapitre != null) {
            return new ApiResponse<>(true, "Chapitre trouvé", chapitre);
        } else {
            return new ApiResponse<>(false, "Chapitre introuvable", null);
        }
    }
}
