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

        int[] data = excelReader.readNumbers(Path.of(filePath));

        if (n > data.length) {
            throw new ExcelValidationException(
                    "Параметр n (" + n + ") больше количества чисел в файле (" + data.length + ")"
            );
        }

        return quickSelect(data, n - 1);
    }

    // Средняя сложность O(n), худшая O(n²) при неудачном выборе опорного элемента
    private int quickSelect(int[] a, int k) {
        int left = 0;
        int right = a.length - 1;

        for (; ; ) {
            if (left == right) {
                return a[left];
            }

            int pivotIndex = right;
            pivotIndex = partition(a, left, right, pivotIndex);

            if (k == pivotIndex) {
                return a[k];
            } else if (k < pivotIndex) {
                right = pivotIndex - 1;
            } else {
                left = pivotIndex + 1;
            }
        }
    }

    // Разделяет элементы вокруг опорного
    private int partition(int[] a, int left, int right, int pivotIndex) {
        int pivotValue = a[pivotIndex];
        swap(a, pivotIndex, right);

        int store = left;
        for (int i = left; i < right; i++) {
            if (a[i] < pivotValue) {
                swap(a, store, i);
                store++;
            }
        }

        swap(a, right, store);
        return store;
    }

    // Меняет местами два элемента массива
    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

}
