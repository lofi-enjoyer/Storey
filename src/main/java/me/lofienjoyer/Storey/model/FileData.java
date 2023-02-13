package me.lofienjoyer.Storey.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {

    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private String name;
    private String lastModified;
    private boolean isFolder;

    public FileData(File file) {
        this.name = file.getName();
        this.lastModified = FILE_DATE_FORMAT.format(Date.from(Instant.ofEpochMilli(file.lastModified())));
        this.isFolder = file.isDirectory();
    }

}
