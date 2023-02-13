package me.lofienjoyer.Storey.service;

import me.lofienjoyer.Storey.model.CreateFolderDto;
import me.lofienjoyer.Storey.model.DeleteFolderDto;
import me.lofienjoyer.Storey.model.FolderData;
import me.lofienjoyer.Storey.model.PathData;

public interface FolderService {

    FolderData getFolderData(PathData data);

    void createFolder(CreateFolderDto dto);

    void deleteFolder(DeleteFolderDto dto);

}
