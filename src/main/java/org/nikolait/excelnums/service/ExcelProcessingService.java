package org.nikolait.excelnums.service;

/**
 * Сервис для обработки Excel-файлов.
 */
public interface ExcelProcessingService {


    /**
     * Возвращает N-ое минимальное целое число из Excel-файла.
     * Числа читаются из первого столбца первого листа подряд,
     * чтение прекращается при первой пустой ячейке.
     *
     * @param filePath абсолютный путь к .xlsx файлу
     * @param n        порядковый номер минимального элемента (1-based)
     * @return N-ое минимальное целое число
     * @throws IllegalArgumentException если файл некорректен, не содержит чисел
     *                                  или параметр n выходит за допустимые пределы
     */
    int findNthMin(String filePath, int n);

}
