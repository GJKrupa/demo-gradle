package uk.temp.xyz.service;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;
import uk.temp.xyz.dto.SayResponseData;

/**
 * Created by kevinrudland on 07/10/2016.
 */
@Component
public class ServiceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").contextPath("/demo-service").port(8080).host("0.0.0.0").bindingMode(RestBindingMode.json);
        rest("/say")
                .get("/hello").description("says hello").to("direct:hello")
                .get("/bye").description("says goodbye").consumes("application/json").to("direct:bye")
                .post("/bye").to("mock:update");

        from("direct:hello")
                .transform().constant(new SayResponseData("Hello World"));
        from("direct:bye")
                .transform().constant(new SayResponseData("Bye World"));
    }
}
