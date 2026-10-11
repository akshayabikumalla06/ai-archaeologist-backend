package com.example.archaeologist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.archaeologist.entity.Artifact;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {

}