package org.nikolait.excelnums.excel;

import java.nio.file.Path;

public interface ExcelReader {
    int[] readNumbers(Path path);
}
