package lk.rajith.examples.openshift.cicd;

import java.util.Date;

import javax.inject.Named;

@Named("greetingService")
public class GreetingService {

    String environment = System.getenv("deployment-env");

    public GreetingResponse buildGreeting() {
        GreetingResponse greeting = new GreetingResponse();
        greeting.setGreeting("Hello World");
        greeting.setEnvironment(environment);
        greeting.setTimeStamp(new Date());

        return greeting;
    }
}
