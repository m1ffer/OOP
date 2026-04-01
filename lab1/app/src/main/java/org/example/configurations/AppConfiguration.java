package org.example.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "org.example.controllers",
        "org.example.models",
        "factories"
})
public class AppConfiguration {
}
