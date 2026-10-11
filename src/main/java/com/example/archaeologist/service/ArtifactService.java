package com.example.archaeologist.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.archaeologist.entity.Artifact;
import com.example.archaeologist.repository.ArtifactRepository;

@Service
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    @Value("${app.upload.dir}")
    private String uploadDirectory;

    public ArtifactService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public Artifact saveArtifact(Artifact artifact) {
        return artifactRepository.save(artifact);
    }

    public List<Artifact> getAllArtifacts() {
        return artifactRepository.findAll();
    }

    public Artifact getArtifactById(Long id) {
        return artifactRepository.findById(id).orElse(null);
    }

    public void deleteArtifact(Long id) {
        artifactRepository.deleteById(id);
    }

    public Artifact uploadArtifact(MultipartFile file, String artifactType,
                                   String description) throws IOException {

        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IOException("Invalid file name");
        }

        String safeFileName = UUID.randomUUID() + "_" + originalFileName;

        Path filePath = uploadPath.resolve(safeFileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        Artifact artifact = new Artifact();

        artifact.setFileName(originalFileName);
        artifact.setFilePath(filePath.toString());
        artifact.setArtifactType(artifactType);
        artifact.setContentType(file.getContentType());
        artifact.setFileSize(file.getSize());
        artifact.setDescription(description);
        artifact.setConfidence(0.0);

        return artifactRepository.save(artifact);
    }
}