package org.nikolait.excelnums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NthMinExcel(
        @NotBlank
        String filePath,

        @NotNull
        @Positive
        @Schema(example = "1")
        Integer n
) {
}
