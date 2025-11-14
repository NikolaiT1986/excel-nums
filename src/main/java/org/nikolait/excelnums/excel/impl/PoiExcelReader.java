package org.nikolait.excelnums.excel.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.nikolait.excelnums.config.ExcelProperties;
import org.nikolait.excelnums.excel.ExcelReader;
import org.nikolait.excelnums.exception.ExcelValidationException;
import org.nikolait.excelnums.utills.ExcelFileUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class PoiExcelReader implements ExcelReader {

    private final ExcelProperties properties;

    @Override
    public int[] readNumbers(Path path) {
        ExcelFileUtils.validateExcelFile(path, properties.getAllowedExtensions());

        int[] buffer = new int[properties.getInitialBufferCapacity()];
        int size = 0;

        try (InputStream is = Files.newInputStream(path);
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new ExcelValidationException("Файл не содержит листов");
            }

            for (Row row : sheet) {
                Cell cell = row.getCell(0);

                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    break;
                }

                if (size == buffer.length) {
                    int newCapacity = buffer.length * 2;
                    int[] newBuffer = new int[newCapacity];
                    System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                    buffer = newBuffer;
                }

                buffer[size++] = readIntFromCell(cell);
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения файла Excel: " + path, e);
        }

        return Arrays.copyOf(buffer, size);
    }

    // Преобразует ячейку в int; поддерживает NUMERIC и STRING
    private int readIntFromCell(Cell cell) {
        return switch (cell.getCellType()) {
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                if (d % 1 != 0) {
                    throw new ExcelValidationException(
                            "В ячейке " + cell.getAddress() + " не целое число: " + d
                    );
                }
                yield (int) d;
            }
            case STRING -> {
                String text = cell.getStringCellValue().trim();
                try {
                    yield Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException(
                            "Некорректное целое число в ячейке " + cell.getAddress() + ": " + text,
                            ex
                    );
                }
            }
            case FORMULA -> {
                CellType evaluatedType = cell.getCachedFormulaResultType();

                if (evaluatedType == CellType.NUMERIC) {
                    double d = cell.getNumericCellValue();
                    if (d % 1 != 0) {
                        throw new ExcelValidationException(
                                "В ячейке " + cell.getAddress() + " формула вычисляется в не целое число: " + d
                        );
                    }
                    yield (int) d;
                }

                throw new ExcelValidationException(
                        "Формула в ячейке " + cell.getAddress() +
                                " вычисляется в неподдерживаемый тип: " + evaluatedType
                );
            }
            default -> throw new ExcelValidationException("Неподдерживаемый тип ячейки " + cell.getCellType());
        };
    }
}
