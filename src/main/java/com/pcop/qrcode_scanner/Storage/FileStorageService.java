package com.pcop.qrcode_scanner.Storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

    public void init();
    FileInfo save(MultipartFile file);
    public Resource load(String filename);
    public void deleteAll();
    public void delete(String filename);
    public Stream<Path> loadAll();

}
