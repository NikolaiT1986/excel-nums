package org.nikolait.excelnums.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikolait.excelnums.dto.NthMinExcel;
import org.nikolait.excelnums.service.ExcelProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelProcessingController {

    private final ExcelProcessingService excelProcessingService;

    @PostMapping("/nth-min")
    @Operation(
            summary = "Поиск N-го минимального числа",
            description = "Принимает путь к локальному XLSX-файлу и число N, возвращает N-ое минимальное значение."
    )
    public ResponseEntity<Integer> getNthMin(@Valid @RequestBody NthMinExcel request) {
        int result = excelProcessingService.findNthMin(request.filePath(), request.n());
        return ResponseEntity.ok(result);
    }

}
