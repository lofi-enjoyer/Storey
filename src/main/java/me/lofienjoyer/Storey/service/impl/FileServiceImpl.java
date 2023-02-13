package me.lofienjoyer.Storey.service.impl;

import me.lofienjoyer.Storey.model.DeleteFolderDto;
import me.lofienjoyer.Storey.service.FileService;
import me.lofienjoyer.Storey.utils.StoreyRegex;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class FileServiceImpl implements FileService {

    @Value("${storey.root}")
    String root;

    @Value("${storey.fallbackFileImage}")
    String fallbackFileImage;

    @Value("${storey.fallbackFolderImage}")
    String fallbackFolderImage;

    @Override
    public Resource getFile(String filePath) {
        return getFile(filePath, false);
    }

    @Override
    public Resource getFilePreview(String filePath) {
        return getFile(filePath, true);
    }

    private Resource getFile(String filePath, boolean preview) {
        filePath = filePath.replaceFirst("/", "");

        String[] fullPath = filePath.split("/");
        String[] path = new String[fullPath.length - 1];
        System.arraycopy(fullPath, 0, path, 0, fullPath.length - 1);
        String fileName = fullPath[fullPath.length - 1];

        if (!StoreyRegex.validateFileName(fileName)) {
            if (preview) {
                File file = Path.of(fallbackFileImage).toFile();
                return new FileSystemResource(file);
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
        }

        if (!StoreyRegex.validatePath(path)) {
            if (preview) {
                File file = Path.of(fallbackFileImage).toFile();
                return new FileSystemResource(file);
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        File file = Path.of(root, fullPath).toFile();
        return new FileSystemResource(file);
    }

    @Override
    public void uploadFile(String path, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !StoreyRegex.validateFileName(fileName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
        }

        if (Arrays.stream(path.split("/")).noneMatch(StoreyRegex::validatePath)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        String[] fullPath = path.split("/");

        try {
            Path folder = Paths.get(root, fullPath);

            Files.copy(file.getInputStream(), folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new ResponseStatusException(HttpStatus.OK);
    }

    @Override
    public void deleteFile(DeleteFolderDto dto) {
        String filePath = dto.getPath().replaceFirst("/", "");

        String[] fullPath = filePath.split("/");
        String[] pathWithFolder = new String[fullPath.length + 1];
        System.arraycopy(fullPath, 0, pathWithFolder, 0, fullPath.length);
        pathWithFolder[fullPath.length] = dto.getName();

        // Check if path is valid
        if (Arrays.stream(pathWithFolder).noneMatch(StoreyRegex::validatePath)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        File fileToDelete = Paths.get(root, pathWithFolder).toFile();
        boolean result = fileToDelete.delete();
        if (!result) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to delete file");
        }

        throw new ResponseStatusException(HttpStatus.OK);
    }

}
