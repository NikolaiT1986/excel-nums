package org.nikolait.excelnums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ExcelNumsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelNumsApplication.class, args);
    }

}
