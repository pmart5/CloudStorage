package com.pmart5a.cloudstorage.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "files", indexes = @Index(name = "fileNameIndex", columnList = "fileName"))
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    Long fileSize;

    @Column(nullable = false)
    String fileType;

    @Column(nullable = false)
    LocalDateTime fileDateUpdate;

    @Lob
    @Column(nullable = false)
    byte[] fileByte;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    UserEntity user;
}