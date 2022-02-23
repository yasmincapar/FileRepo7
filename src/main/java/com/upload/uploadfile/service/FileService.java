package com.upload.uploadfile.service;

import com.upload.uploadfile.model.File;
import com.upload.uploadfile.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void save(MultipartFile file) throws IOException {
        File fileEntity = new File();
        fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());

        fileRepository.save(fileEntity);
    }

    public Optional<File> getFile(Long id) {
        return fileRepository.findById(id);
    }

    @Transactional
    public Optional<File> getFile(String fileName) {
        return fileRepository.findByName(fileName);
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }
}
