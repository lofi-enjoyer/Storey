package me.lofienjoyer.Storey.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFolderDto {

    private String path;
    private String name;

}
