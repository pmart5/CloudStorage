package com.pmart5a.cloudstorage.repository;

import com.pmart5a.cloudstorage.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    void deleteFileByUserIdAndFileName(Long userId, String fileName);

    Optional<File> findByUserIdAndFileName(Long userId, String fileName);

    @Modifying
    @Query(value = "update files f set f.file_name = ?1 where f.file_name = ?2 and f.user_Id = ?3", nativeQuery = true)
    void editFileNameByUserId(String newName, String fileName, Long userId);

    List<File> findAllByUserId(Long userId);

//    @Query(value = "select * from files f where f.user_id = ?1 order by f.id desc limit ?2", nativeQuery = true)
//    List<File> findFilesByUserIdWithLimit(Long userId, int limit);


}