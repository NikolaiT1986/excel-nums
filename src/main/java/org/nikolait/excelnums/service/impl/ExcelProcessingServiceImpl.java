package org.nikolait.excelnums.service.impl;

import lombok.RequiredArgsConstructor;
import org.nikolait.excelnums.excel.ExcelReader;
import org.nikolait.excelnums.exception.ExcelValidationException;
import org.nikolait.excelnums.service.ExcelProcessingService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ExcelProcessingServiceImpl implements ExcelProcessingService {

    private final ExcelReader excelReader;

    @Override
    public int findNthMin(String filePath, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Параметр n должен быть положительным");
        }

        int[] numbers = excelReader.readNumbers(Path.of(filePath));

        if (n > numbers.length) {
            throw new ExcelValidationException(
                    "Параметр n (" + n + ") больше количества чисел в файле (" + numbers.length + ")"
            );
        }

        return quickSelect(numbers, n - 1);
    }

    // Средняя сложность O(n), худшая O(n²) при неудачном выборе опорного элемента
    private int quickSelect(int[] numbers, int targetIndex) {
        int leftIndex = 0;
        int rightIndex = numbers.length - 1;

        for (; ; ) {
            if (leftIndex == rightIndex) {
                return numbers[leftIndex];
            }

            int pivotIndex = rightIndex;
            pivotIndex = partition(numbers, leftIndex, rightIndex, pivotIndex);

            if (targetIndex == pivotIndex) {
                return numbers[targetIndex];
            } else if (targetIndex < pivotIndex) {
                rightIndex = pivotIndex - 1;
            } else {
                leftIndex = pivotIndex + 1;
            }
        }
    }

    // Разделяет элементы вокруг опорного
    private int partition(int[] numbers, int leftIndex, int rightIndex, int pivotIndex) {
        int pivotValue = numbers[pivotIndex];
        swap(numbers, pivotIndex, rightIndex);

        int storeIndex = leftIndex;
        for (int i = leftIndex; i < rightIndex; i++) {
            if (numbers[i] < pivotValue) {
                swap(numbers, storeIndex, i);
                storeIndex++;
            }
        }

        swap(numbers, rightIndex, storeIndex);
        return storeIndex;
    }

    // Меняет местами два элемента массива
    private void swap(int[] numbers, int firstIndex, int secondIndex) {
        int tempValue = numbers[firstIndex];
        numbers[firstIndex] = numbers[secondIndex];
        numbers[secondIndex] = tempValue;
    }

}
