package me.lofienjoyer.Storey.service;

import me.lofienjoyer.Storey.model.DeleteFolderDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Resource getFile(String filePath);

    Resource getFilePreview(String filePath);

    void uploadFile(String path, MultipartFile file);

    void deleteFile(DeleteFolderDto dto);

}
