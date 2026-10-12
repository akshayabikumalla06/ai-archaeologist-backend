package com.example.archaeologist.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.archaeologist.entity.Artifact;
import com.example.archaeologist.service.ArtifactService;

@RestController
@CrossOrigin(origins = "https://ai-archaeologist-frontend.onrender.com")
@RequestMapping("/api/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    public ArtifactController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    // Create artifact
    @PostMapping
    public ResponseEntity<Artifact> createArtifact(
            @RequestBody Artifact artifact) {

        Artifact savedArtifact =
                artifactService.saveArtifact(artifact);

        return ResponseEntity.ok(savedArtifact);
    }

    // Upload artifact image
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Artifact> uploadArtifact(
            @RequestPart("file") MultipartFile file,
            @RequestParam("artifactType") String artifactType,
            @RequestParam("description") String description)
            throws IOException {

        Artifact artifact =
                artifactService.uploadArtifact(
                        file,
                        artifactType,
                        description
                );

        return ResponseEntity.ok(artifact);
    }

    // Get all artifacts
    @GetMapping
    public ResponseEntity<List<Artifact>> getAllArtifacts() {

        return ResponseEntity.ok(
                artifactService.getAllArtifacts()
        );
    }

    // Get artifact by ID
    @GetMapping("/{id}")
    public ResponseEntity<Artifact> getArtifactById(
            @PathVariable Long id) {

        Artifact artifact =
                artifactService.getArtifactById(id);

        if (artifact == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(artifact);
    }

    // Delete artifact
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtifact(
            @PathVariable Long id) {

        artifactService.deleteArtifact(id);

        return ResponseEntity.ok(
                "Artifact deleted successfully"
        );
    }
}