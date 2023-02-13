package me.lofienjoyer.Storey.service.impl;

import me.lofienjoyer.Storey.model.*;
import me.lofienjoyer.Storey.service.FolderService;
import me.lofienjoyer.Storey.utils.StoreyRegex;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Value("${storey.root}")
    String root;

    @Override
    public FolderData getFolderData(PathData data) {
        // Check if path is valid
        if (Arrays.stream(data.getPath()).noneMatch(StoreyRegex::validatePath)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        File folder = Paths.get(root, data.getPath()).toFile();
        List<FileData> fileList = new ArrayList<>();

        Arrays.stream(folder.listFiles()).iterator().forEachRemaining(file -> {
            FileData fileData = new FileData(file);
            fileList.add(fileData);
        });

        return new FolderData(folder.getPath().replaceFirst(root, ""), fileList);
    }

    @Override
    public void createFolder(CreateFolderDto dto) {
        String filePath = dto.getPath().replaceFirst("/", "");

        String[] fullPath = filePath.split("/");
        String[] pathWithFolder = new String[fullPath.length + 1];
        System.arraycopy(fullPath, 0, pathWithFolder, 0, fullPath.length);
        pathWithFolder[fullPath.length] = dto.getName();

        // Check if path is valid
        if (Arrays.stream(pathWithFolder).noneMatch(StoreyRegex::validatePath)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        File folderToCreate = Paths.get(root, pathWithFolder).toFile();
        boolean result = folderToCreate.mkdir();
        if (!result) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create folder");
        }
    }

    @Override
    public void deleteFolder(DeleteFolderDto dto) {
        String filePath = dto.getPath().replaceFirst("/", "");

        String[] fullPath = filePath.split("/");
        String[] pathWithFolder = new String[fullPath.length + 1];
        System.arraycopy(fullPath, 0, pathWithFolder, 0, fullPath.length);
        pathWithFolder[fullPath.length] = dto.getName();

        // Check if path is valid
        if (Arrays.stream(pathWithFolder).noneMatch(StoreyRegex::validatePath)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        try {
            FileUtils.deleteDirectory(Paths.get(root, pathWithFolder).toFile());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to delete folder");
        }
    }

}
