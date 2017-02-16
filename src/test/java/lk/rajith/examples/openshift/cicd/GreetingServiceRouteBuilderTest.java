package lk.rajith.examples.openshift.cicd;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingServiceRouteBuilderTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(GreetingServiceRouteBuilderTest.class);

    @Produce
    protected ProducerTemplate prodTemplate;

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    public boolean isUseDebugger() {
        return true;
    }

    @Override
    protected void debugBefore(Exchange exchange, Processor processor, ProcessorDefinition<?> definition, String id, String shortName) {
        // Set break points here to debug before each step
        LOG.info("Definition: {}, Id: {}, ShortName: {}", definition, id, shortName);
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RouteBuilder[] { new GreetingServiceRouteBuilder() };
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        registry.bind("greetingService", new GreetingService());
        return registry;
    }

    @Test
    public void testValidGreeting() throws Exception {
        context.start();
        try {
            Object response = (Object) prodTemplate.sendBody("direct:build-greeting", ExchangePattern.InOut, null);
            Assert.assertTrue(String.format("Invalid type '%s', excepted '%s'", response.getClass(), GreetingResponse.class), response instanceof GreetingResponse);
            GreetingResponse greeting = (GreetingResponse) response;
            Assert.assertTrue(String.format("Invalid greeting '%s', excepted it to start with '%s'", greeting.getGreeting(), "Hello"),greeting.getGreeting().startsWith("Hello"));
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            Assert.fail();
        }

    }
}