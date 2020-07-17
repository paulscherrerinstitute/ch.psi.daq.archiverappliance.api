package ch.psi.daq.archiverappliance.api.api.v1;

import ch.psi.daq.archiverappliance.api.ArchiverQueryManager;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResult;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Instant;

public class ArchiverQueryManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(ArchiverQueryManagerTest.class);

    @Test
    public void query() {
        ArchiverQueryManager manager = new ArchiverQueryManager();

        ArchiverQueryResult result = manager.query(
                "S10CB01-RACC100-TUN10:SIG-AMPLT-MAX",
                Instant.parse("2020-07-06T09:02:34.336Z"),
                Instant.parse("2020-07-06T09:02:35.336Z"));

        System.out.println(result.getMeta().getName());
    }

    @Test
    public void queryStream() throws InterruptedException {
        ArchiverQueryManager manager = new ArchiverQueryManager();

        Flux<ArchiverQueryResultEvent> result = manager.queryStream(
                "S10CB01-RACC100-TUN10:SIG-AMPLT-MAX",
                Instant.parse("2020-07-06T09:02:34.336Z"),
                Instant.parse("2020-07-06T09:02:35.336Z"));

        logger.info("START");
//        List<ArchiverQueryResultEvent> r = result.collectList().block();
//        logger.info("SIZE: "+r.size());
        result.subscribe(r -> logger.info("TEST: " + r.getValue()));
        logger.info("FINISHED");

        Thread.sleep(10000);
    }
}