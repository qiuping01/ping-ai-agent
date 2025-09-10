package com.ping.aiagent.repository;

import com.ping.aiagent.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByCosPath(String cosPath);

    List<FileMetadata> findByUserId(Long userId);

    List<FileMetadata> findByStatus(Integer status);

    @Transactional
    @Modifying
    @Query("UPDATE FileMetadata f SET f.status = 0 WHERE f.id = :id")
    int softDelete(@Param("id") Long id);

    @Query("SELECT f FROM FileMetadata f WHERE f.originalFileName LIKE %:keyword%")
    List<FileMetadata> searchByFileName(@Param("keyword") String keyword);

    @Query("SELECT f FROM FileMetadata f WHERE f.cosPath = :cosPath AND f.status = 1")
    Optional<FileMetadata> findActiveByCosPath(@Param("cosPath") String cosPath);
}