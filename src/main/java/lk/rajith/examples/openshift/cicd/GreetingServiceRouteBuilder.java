package lk.rajith.examples.openshift.cicd;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import com.fasterxml.jackson.core.JsonParseException;

public class GreetingServiceRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(JsonParseException.class)
        .handled(true)
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .process((Exchange exchange) -> {
            exchange.getIn().setBody(null);
        });

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json).skipBindingOnErrorCode(false);
    
        rest("/greeting/")
            .get()
            .outType(GreetingResponse.class)
            .route().routeId(GreetingServiceRouteBuilder.class.getCanonicalName() + "-get-greeting")
            .to("direct:build-greeting");
    
        from("direct:build-greeting")
            .routeId(GreetingServiceRouteBuilder.class.getCanonicalName() + "-build-greeting")
            .to("bean:greetingService?method=buildGreeting");       
    }
}
