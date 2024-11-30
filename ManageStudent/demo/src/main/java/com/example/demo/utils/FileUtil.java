package com.example.demo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileUtil {

  public static String saveImage(MultipartFile multipartFile) throws IOException {
    Path path = new File("images/").toPath();
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    String fileName = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "_" + multipartFile.getOriginalFilename();
    try (InputStream inputStream = multipartFile.getInputStream()) {
      Path filePath = path.resolve(fileName);
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ioe) {
      throw new IOException("Could not save file: " + fileName);
    }
    return "images/" + fileName;
  }

  public static String saveDocument(MultipartFile multipartFile) throws IOException {
    Path path = new File("document/").toPath();
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    String fileName = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "_" + multipartFile.getOriginalFilename();
    try (InputStream inputStream = multipartFile.getInputStream()) {
      Path filePath = path.resolve(fileName);
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ioe) {
      throw new IOException("Could not save file: " + fileName);
    }
    return "document/" + fileName;
  }

}
