package me.lofienjoyer.Storey.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreyRegex {

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_.-]+\\.[A-Za-z0-9]{0,4}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern FOLDER_PATTERN = Pattern.compile("^[A-Za-z0-9_.-]*$", Pattern.CASE_INSENSITIVE);

    public static boolean validateFileName(String fileName) {
        Matcher matcher = FILE_NAME_PATTERN.matcher(fileName);
        return matcher.find();
    }

    public static boolean validatePath(String... path) {
        return Arrays.stream(path).allMatch(pathElement -> {
            return FOLDER_PATTERN.matcher(pathElement).find();
        });
    }

}
