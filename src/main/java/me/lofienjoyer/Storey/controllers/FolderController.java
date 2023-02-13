package me.lofienjoyer.Storey.controllers;

import me.lofienjoyer.Storey.model.CreateFolderDto;
import me.lofienjoyer.Storey.model.DeleteFolderDto;
import me.lofienjoyer.Storey.model.FolderData;
import me.lofienjoyer.Storey.model.PathData;
import me.lofienjoyer.Storey.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    FolderService folderService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FolderData getFolderData(@RequestBody PathData data) {
        return folderService.getFolderData(data);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createFolder(@RequestBody CreateFolderDto dto) {
        folderService.createFolder(dto);
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteFolder(@RequestBody DeleteFolderDto dto) {
        folderService.deleteFolder(dto);
    }

}
