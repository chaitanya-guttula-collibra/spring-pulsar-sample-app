package com.collibra.csp.client.performance;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.extern.slf4j.Slf4j;

import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.repeat;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
@Slf4j
public class SpringPulsarProducerSimulation extends Simulation {

    String baseUrl = "http://localhost:7070";//System.getProperty("baseUrl");
    int repeatCount = Integer.parseInt(System.getProperty("repeatCount"));
    int userCount = Integer.parseInt(System.getProperty("userCount"));
    int rampUpDuration = Integer.parseInt(System.getProperty("rampUpDuration"));

    int messagesCount = Integer.parseInt(System.getProperty("messagesCount"));

    ChainBuilder ingestSchema = repeat(repeatCount)
            .on(
                    http("Start")
                            .post("/send/exampleMessageWithSchema")
                            .check(status().is(200)));

    ScenarioBuilder flows = scenario("Simple Spring Pulsar Producer flow").exec(ingestSchema);

    HttpProtocolBuilder httpProtocol = http.baseUrl(baseUrl);

    {
        logEnvironment();
        setUp(flows.injectOpen(rampUsers(userCount).during(rampUpDuration))).protocols(httpProtocol);
    }

    private void logEnvironment() {
        log.info("-------------------------------------------------");
        log.info("Starting simulation with the following settings: ");
        log.info("baseUrl: {}", baseUrl);
        log.info("repeatCount: {} (Number of executions of the test flow)", repeatCount);
        log.info("userCount: {} (Number of parallel users executing the flow)", userCount);
        log.info("rampUpDuration: {} (Ramp up duration in seconds)", rampUpDuration);
        log.info("Number of events to be produced: {}", userCount * repeatCount );
        log.info("-------------------------------------------------");
    }
}

