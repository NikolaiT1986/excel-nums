package org.nikolait.excelnums.utills;

import lombok.experimental.UtilityClass;
import org.nikolait.excelnums.exception.ExcelValidationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@UtilityClass
public class ExcelFileUtils {

    public static Path validateExcelFile(Path path, List<String> allowedExtensions) {
        if (!Files.exists(path)) {
            throw new ExcelValidationException("Путь не существует: " + path);
        }
        if (!Files.isRegularFile(path)) {
            throw new ExcelValidationException("Указанный путь не является файлом: " + path);
        }
        if (!Files.isReadable(path)) {
            throw new ExcelValidationException("Файл недоступен для чтения: " + path);
        }

        String name = path.getFileName().toString().toLowerCase();

        boolean allowed = allowedExtensions.stream()
                .map(ext -> ext.startsWith(".") ? ext : "." + ext)
                .anyMatch(name::endsWith);

        if (!allowed) {
            throw new ExcelValidationException(
                    "Файл имеет недопустимое расширение. Разрешённые: " + allowedExtensions
            );
        }
        return path;
    }
}
