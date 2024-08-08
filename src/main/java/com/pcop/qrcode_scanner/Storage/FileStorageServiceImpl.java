package com.pcop.qrcode_scanner.Storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    private final Path root = Paths.get("src/main/resources/static/uploads/");
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public FileInfo save(MultipartFile file) {
        try {
            // save the file to a unique identifier
            String uniqueIdentifier = UUID.randomUUID().toString();
            String fileExtension = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = uniqueIdentifier + fileExtension;

            // save the file to the uploads directory
            Path destination = this.root.resolve(fileName);
            Files.copy(file.getInputStream(), destination);
            return new FileInfo(fileName, "/static/uploads/" + fileName, file.getContentType());
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("File already exists");
            }
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public void delete(String filename) {
        Path fileToDelete = root.resolve(filename);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete the file: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load files from the directory!");
        }
    }
}
