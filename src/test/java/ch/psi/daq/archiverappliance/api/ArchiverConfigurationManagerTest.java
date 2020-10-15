package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.data.ArchiverChannelConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ArchiverConfigurationManagerTest {

    @Test
    public void getChannelConfiguration() {
        ArchiverConfigurationManager manager = new ArchiverConfigurationManager(
//                "sf-archapp-05.psi.ch",
                "twlha-archapp-01.psi.ch",
                "channels.json",
                "channels_configuration.json",
                Jackson2ObjectMapperBuilder.json().build());
//        ArchiverChannelConfiguration configuration = manager.getChannelConfiguration("S10-CPPS-DALA01:CH2READ");
//        ArchiverChannelConfiguration configuration = manager.getChannelConfiguration("CR0808:CURRENT-3-3");

        // See whether ISO characters are correctly translated
//        ArchiverChannelConfiguration configuration = manager.getChannelConfigurationFromArchiver("SGE-EDRPS01-DDA0302:DOSE-MEAN-R").block();
        ArchiverChannelConfiguration configuration = manager.getChannelConfigurationFromArchiver("S10-CPCL-VM1MGC:LOAD").block();
//        ArchiverChannelConfiguration configuration = manager.getChannelConfiguration(".*");
        if(configuration != null) {
            System.out.println(configuration.getName());
            System.out.println(configuration.getUnit());
            System.out.println(configuration.getType());
        }
        else{
            System.out.println("no config");
        }
    }

    @Test
    public void fetchChannelConfigurations() {
        ArchiverConfigurationManager manager = new ArchiverConfigurationManager(
                "sf-archapp-05.psi.ch",
                "channels.json",
                "channels_configuration.json",
                Jackson2ObjectMapperBuilder.json().build());
        manager.fetchChannelConfigurations().block();
    }

    @Test
    public void fetchChannels() {
        ArchiverConfigurationManager manager = new ArchiverConfigurationManager(
                "sf-archapp-05.psi.ch",
                "channels.json",
                "channels_configuration.json",
                Jackson2ObjectMapperBuilder.json().build());
        assertTrue(manager.fetchChannels().block());
    }

    @Test
    void getChannels() {
        ArchiverConfigurationManager manager = new ArchiverConfigurationManager(
                "sf-archapp-05.psi.ch",
                "channels.json",
                "channels_configuration.json",
                Jackson2ObjectMapperBuilder.json().build());
        List<String> channels = manager.getChannels(true).collectList().block();
        System.out.println(channels);
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        List<String> list = mapper.readValue(Paths.get("response.json").toFile(), new TypeReference<List<String>>() {});
        System.out.println(list);
    }

}