package com.upload.uploadfile.controller;

import com.upload.uploadfile.model.File;
import com.upload.uploadfile.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class FilesController {

    private final FileService fileService;

    @Autowired
    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files")
    public String listAllFiles(Model model) {

        model.addAttribute("files", fileService.getAllFiles().stream().map(file -> ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(file.getName())
                .toUriString())
                .collect(Collectors.toList()));

        return "listFiles";
    }

    @PostMapping("/upload-file")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            fileService.save(file);

            return "redirect:/files";
        } catch (Exception e) {
            return String.format("Could not upload the file: %s!", file.getOriginalFilename());
        }
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {

        File resource = fileService.getFile(filename).get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getName() + "\"")
                .contentType(MediaType.valueOf(resource.getContentType()))
                .body(resource.getData());
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Optional<File> fileEntityOptional = fileService.getFile(id);

        if (!fileEntityOptional.isPresent()) {
            return ResponseEntity.notFound()
                                 .build();
        }

        File file = fileEntityOptional.get();
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                             .contentType(MediaType.valueOf(file.getContentType()))
                             .body(file.getData());
    }
}
