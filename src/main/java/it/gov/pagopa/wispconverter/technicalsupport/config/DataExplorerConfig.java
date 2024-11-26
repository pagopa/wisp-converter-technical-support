package it.gov.pagopa.wispconverter.technicalsupport.config;

import com.microsoft.azure.kusto.data.Client;
import com.microsoft.azure.kusto.data.ClientFactory;
import com.microsoft.azure.kusto.data.auth.ConnectionStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
@EnableConfigurationProperties
@ConditionalOnExpression("'${info.properties.environment}'!='test'")
@Slf4j
public class DataExplorerConfig {

    @Value("${azure.dataexplorer.url}")
    private String dataExplorerUrl;

    @Value("${info.properties.environment}")
    private String environment;


    @Bean
    public Client getClient() throws URISyntaxException {
        Client client;
        if ("local".equals(environment)) {
            client = ClientFactory.createClient(ConnectionStringBuilder.createWithDeviceCode(dataExplorerUrl));
        } else {
            client = ClientFactory.createClient(ConnectionStringBuilder.createWithAadManagedIdentity(dataExplorerUrl));
        }
        return client;
    }

}
