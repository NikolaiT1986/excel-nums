package org.nikolait.excelnums.excel.impl;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nikolait.excelnums.config.ExcelProperties;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PoiExcelReaderTest {

    private ExcelProperties properties;

    @BeforeEach
    void setUp() {
        properties = new ExcelProperties();
        properties.setAllowedExtensions(List.of("xlsx"));
    }

    @Test
    void readNumbers_resizesBufferAndReadsAllValues() throws Exception {
        Path excelPath = createExcelWithFirstColumn(1, 2, 3, 4, 5);

        properties.setInitialBufferCapacity(2);
        PoiExcelReader reader = new PoiExcelReader(properties);

        int[] result = reader.readNumbers(excelPath);

        assertThat(result).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void readNumbers_returnsArrayExactlyOfReadSize_noTrailingZeros() throws Exception {
        Path excelPath = createExcelWithFirstColumn(10, 20, 30);

        properties.setInitialBufferCapacity(100);
        PoiExcelReader reader = new PoiExcelReader(properties);

        int[] result = reader.readNumbers(excelPath);

        assertThat(result).containsExactly(10, 20, 30);
    }

    private Path createExcelWithFirstColumn(int... values) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        for (int i = 0; i < values.length; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0, CellType.NUMERIC).setCellValue(values[i]);
        }

        Path tempFile = Files.createTempFile("numbers-", ".xlsx");
        try (OutputStream os = Files.newOutputStream(tempFile)) {
            workbook.write(os);
        }
        workbook.close();
        return tempFile;
    }
}