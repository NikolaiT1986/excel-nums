package org.nikolait.excelnums.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikolait.excelnums.excel.ExcelReader;
import org.nikolait.excelnums.exception.ExcelValidationException;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExcelProcessingServiceImplTest {

    @Mock
    private ExcelReader excelReader;

    @InjectMocks
    private ExcelProcessingServiceImpl excelProcessingService;

    @Test
    void findNthMin_whenNIsNonPositive_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> excelProcessingService.findNthMin("test.xlsx", 0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> excelProcessingService.findNthMin("test.xlsx", -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findNthMin_whenNGreaterThanDataLength_throwsExcelValidationException() {
        when(excelReader.readNumbers(any(Path.class)))
                .thenReturn(new int[]{10, 20, 30});

        assertThatThrownBy(() -> excelProcessingService.findNthMin("numbers.xlsx", 4))
                .isInstanceOf(ExcelValidationException.class);
    }

    @ParameterizedTest(name = "[{index}] n={1} для массива -> ожидаем {2}")
    @MethodSource("nthMinCases")
    void findNthMin_forDifferentArraysAndN_returnsExpectedValue(int[] source,
                                                                int n,
                                                                int expected) {
        // given
        when(excelReader.readNumbers(any(Path.class)))
                .thenReturn(source);

        // when
        int result = excelProcessingService.findNthMin("numbers.xlsx", n);

        // then
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Набор кейсов:
     * - отсортированный массив;
     * - неотсортированный;
     * - дубликаты;
     * - отрицательные числа;
     * - один элемент.
     */
    private static Stream<Arguments> nthMinCases() {
        return Stream.of(
                // отсортированный массив
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 1, 1),
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 3, 3),
                Arguments.of(new int[]{1, 2, 3, 4, 5}, 5, 5),

                // неотсортированный массив
                Arguments.of(new int[]{5, 1, 4, 2, 3}, 1, 1),
                Arguments.of(new int[]{5, 1, 4, 2, 3}, 2, 2),
                Arguments.of(new int[]{5, 1, 4, 2, 3}, 5, 5),

                // дубликаты (по позиции, а не по уникальным)
                // отсортированный вид: [2, 2, 5, 7, 9]
                Arguments.of(new int[]{7, 2, 2, 9, 5}, 1, 2),
                Arguments.of(new int[]{7, 2, 2, 9, 5}, 2, 2),
                Arguments.of(new int[]{7, 2, 2, 9, 5}, 3, 5),
                Arguments.of(new int[]{7, 2, 2, 9, 5}, 4, 7),
                Arguments.of(new int[]{7, 2, 2, 9, 5}, 5, 9),

                // отрицательные числа
                // отсортированный вид: [-10, -3, 0, 5, 8]
                Arguments.of(new int[]{0, -3, 8, 5, -10}, 1, -10),
                Arguments.of(new int[]{0, -3, 8, 5, -10}, 2, -3),
                Arguments.of(new int[]{0, -3, 8, 5, -10}, 3, 0),
                Arguments.of(new int[]{0, -3, 8, 5, -10}, 5, 8),

                // один элемент
                Arguments.of(new int[]{42}, 1, 42)
        );
    }
}
