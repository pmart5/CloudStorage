package com.pmart5a.cloudstorage.controller;

import com.pmart5a.cloudstorage.entity.FileEntity;
import com.pmart5a.cloudstorage.model.FileNameRequest;
import com.pmart5a.cloudstorage.model.FileResponse;
import com.pmart5a.cloudstorage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/cloud")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/file")
    public ResponseEntity<Void> uploadFile(@RequestHeader("auth-token") String authToken,
                                           @RequestParam("filename") String fileName,
                                           @RequestPart("file") MultipartFile file) {
        fileService.uploadFile(authToken, fileName, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/file")
    public ResponseEntity<Void> deleteFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String fileName) {
        fileService.deleteFile(authToken, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String fileName) {
        FileEntity file = fileService.downloadFile(authToken, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileByte());
    }

    @PutMapping(value = "/file")
    public ResponseEntity<Void> editFileName(@RequestHeader("auth-token") String authToken,
                                          @RequestParam("filename") String fileName,
                                          @RequestBody FileNameRequest fileNameRequest) {
        fileService.editFileName(authToken, fileName, fileNameRequest.getFileName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public List<FileResponse> getAllFiles(@RequestHeader("auth-token") String authToken,
                                                          @RequestParam("limit") Integer limit) {
        return fileService.getAllFiles(authToken, limit);
    }
}