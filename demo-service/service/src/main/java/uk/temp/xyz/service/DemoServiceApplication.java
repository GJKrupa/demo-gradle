package uk.temp.xyz.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.component.swagger.DefaultCamelSwaggerServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"uk.temp.xyz.service"})
public class DemoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoServiceApplication.class, args);
	}

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(
                new CamelHttpTransportServlet(), "/demo-service/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

    @Bean
    public ServletRegistrationBean swaggerServlet() {
        ServletRegistrationBean swagger = new ServletRegistrationBean(new DefaultCamelSwaggerServlet(), "/api-docs/*");
        Map<String, String> params = new HashMap<>();
        params.put("base.path", "http://0.0.0.0:8080/api");
        params.put("api.title", "Demo Service");
        params.put("api.description", "A simple service to prove the CI pipeline");
        swagger.setInitParameters(params);
        return swagger;
    }
}
