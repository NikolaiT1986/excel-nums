package org.nikolait.excelnums.service;

import org.nikolait.excelnums.exception.ExcelValidationException;

/**
 * Сервис для обработки Excel-файлов.
 */
public interface ExcelProcessingService {

    /**
     * Возвращает N-е минимальное число из Excel-файла.
     * <p>
     * Ожидается, что в файле в первом столбце находятся целые числа.
     * Чтение продолжается до первой пустой ячейки.
     *
     * @param filePath путь к файлу Excel
     * @param n        порядковый номер минимального элемента (начиная с 1)
     * @return N-е минимальное число
     * @throws IllegalArgumentException если {@code n <= 0}
     * @throws ExcelValidationException если файл некорректен, содержит неверные данные
     *                                  или {@code n} превышает количество чисел
     */
    int findNthMin(String filePath, int n);

}
