package in.belenits.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    @Value("${employee.files.path}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        Path directory = Paths.get(uploadDir);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        String fileName = file.getOriginalFilename();

        Path targetPath = directory.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }
}