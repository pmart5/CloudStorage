package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.entity.FileEntity;
import com.pmart5a.cloudstorage.entity.UserEntity;
import com.pmart5a.cloudstorage.exception.FileOperationsException;
import com.pmart5a.cloudstorage.exception.InputDataException;
import com.pmart5a.cloudstorage.model.FileResponse;
import com.pmart5a.cloudstorage.repository.FileRepository;
import com.pmart5a.cloudstorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Transactional
    public void uploadFile(String authToken, String fileName, MultipartFile file) {
        UserEntity userEntity = userRepository.findById(1L).get();
        if (isFile(1L, fileName)) {
            throw new InputDataException(String.format("Ошибка ввода данных. Файл с именем %s есть в облаке." +
                    " Для повторения операции измените имя у сохранённого или загружаемого файла.", fileName));
        } else {
            try {
                fileRepository.save(FileEntity.builder()
                        .fileName(fileName)
                        .fileSize(file.getSize())
                        .fileType(file.getContentType())
                        .fileDateUpdate(LocalDateTime.now())
                        .fileByte(file.getBytes())
                        .user(userEntity)
                        .build());
            } catch (IOException e) {
                throw new FileOperationsException("Операция загрузки файла завершилась ошибкой.");
            }
        }
    }

    @Transactional
    public void deleteFile(String authToken, String fileName) {
        if (isFile(1L, fileName)) {
            fileRepository.deleteFileByUserIdAndFileName(1L, fileName);
        } else {
            throw new InputDataException(String.format("Ошибка ввода данных. Файл с именем %s не найден.", fileName));
        }
    }

    public FileEntity downloadFile(String authToken, String fileName) {
        final var fileEntity = fileRepository.findByUserIdAndFileName(1L, fileName);
        if (fileEntity.isEmpty()) {
            throw new InputDataException(String.format("Ошибка ввода данных. Файл с именем %s не найден.", fileName));
        }
        return fileEntity.get();
    }

    @Transactional
    public void editFileName(String authToken, String fileName, String newFileName) {
        if (newFileName != null && isFile(1L, fileName) && !isFile(1L, newFileName)) {
            fileRepository.editFileNameByUserId(newFileName, fileName, 1L);
        } else {
            throw new InputDataException("Ошибка ввода данных. Отсутствует новое имя файла, или новое имя файла" +
                    " совпадает с именем файла в облаке, или файл не найден.");
        }
    }

    public List<FileResponse> getAllFiles(String authToken, Integer limit) {
        return fileRepository.findAllByUserId(1L).stream()
                .map(file -> new FileResponse(file.getFileName(), file.getFileSize()))
                .collect(Collectors.toList());
    }

    private boolean isFile(Long userId, String fileName) {
        final var fileEntity = fileRepository.findByUserIdAndFileName(userId, fileName);
        return fileEntity.isPresent() ? true : false;
    }

    private boolean isUser(Long userId) {
        final var userEntity = userRepository.findById(userId);
        return userEntity.isPresent() ? true : false;
    }
}