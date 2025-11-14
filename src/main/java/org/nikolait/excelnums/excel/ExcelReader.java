package org.nikolait.excelnums.excel;

import org.nikolait.excelnums.exception.ExcelValidationException;

import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Компонент для чтения чисел из Excel-файлов.
 */
public interface ExcelReader {

    /**
     * Считывает целые числа из первого столбца Excel-файла.
     * <p>
     * Чтение производится последовательно до первой пустой ячейки.
     *
     * @param path путь к файлу Excel
     * @return массив считанных целых чисел
     * @throws ExcelValidationException если файл недоступен,
     *                                  имеет неподдерживаемое расширение
     *                                  или содержит некорректные данные
     * @throws UncheckedIOException     если произошла ошибка ввода-вывода при чтении файла
     */
    int[] readNumbers(Path path);
}
