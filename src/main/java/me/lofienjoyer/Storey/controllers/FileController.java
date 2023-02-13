package me.lofienjoyer.Storey.controllers;

import me.lofienjoyer.Storey.model.DeleteFolderDto;
import me.lofienjoyer.Storey.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("")
    @ResponseBody
    public Resource getFile(@RequestParam("file") String filePath) {
        return fileService.getFile(filePath);
    }

    @GetMapping("/preview")
    @ResponseBody
    public Resource getFilePreview(@RequestParam("file") String filePath) {
        return fileService.getFilePreview(filePath);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file) {
        fileService.uploadFile(path, file);
    }

    @PostMapping("/delete")
    public void uploadFile(@RequestBody DeleteFolderDto dto) {
        fileService.deleteFile(dto);
    }

}
