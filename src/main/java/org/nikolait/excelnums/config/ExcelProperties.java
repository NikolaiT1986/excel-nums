package org.nikolait.excelnums.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "excel.reader")
public class ExcelProperties {

    private int initialBufferCapacity = 128;

    private List<String> allowedExtensions = List.of("xlsx");

}
