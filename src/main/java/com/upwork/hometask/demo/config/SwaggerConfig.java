package com.upwork.hometask.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return buildDocket()
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.upwork.hometask.demo"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiEndPointsInfo())
        .forCodeGeneration(true);
  }

  private Docket buildDocket() {
    Docket docket = null;
      docket = new Docket(DocumentationType.SWAGGER_2);

    return docket;
  }

  private ApiInfo apiEndPointsInfo() {
    return new ApiInfoBuilder()
        .title("VGP Billings Backend Service")
        .description("VGP Markets Backend Service")
        .version("1.0.0")
        .build();
  }

}
