package lk.rajith.examples.openshift.cicd;

import org.junit.Assert;
import org.junit.Test;

public class TestGreetingService {

    @Test
    public void testGreetingResponse() {
        GreetingService service = new GreetingService();
        GreetingResponse resp = service.buildGreeting();
        Assert.assertTrue(String.format("Invalid greeting '%s', excepted it to start with '%s'", resp.getGreeting(), "Hello"), resp.getGreeting().startsWith("Hello"));
    }
}
