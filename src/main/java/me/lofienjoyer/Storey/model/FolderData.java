package me.lofienjoyer.Storey.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderData {

    private String name;
    private List<FileData> files;

}
