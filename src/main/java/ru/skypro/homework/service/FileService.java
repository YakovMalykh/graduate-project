package ru.skypro.homework.service;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    String getFileName(String fileName);
    String getExtensions(String fileName);
    byte[] generatePreview(Path filePath) throws IOException;
}
