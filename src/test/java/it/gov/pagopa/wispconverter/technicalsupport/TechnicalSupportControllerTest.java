package it.gov.pagopa.wispconverter.technicalsupport;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.util.BinaryData;
import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.models.TableEntity;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.PayloadResponse;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEventResponse;
import it.gov.pagopa.wispconverter.technicalsupport.service.ReService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static it.gov.pagopa.wispconverter.technicalsupport.service.ReService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TechnicalSupportControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    ReService reService;

    @Mock
    TableServiceClient tableServiceClient;
    @Mock
    TableClient tableClient;
    @Mock
    PagedIterable<TableEntity> pagedIterable;
    @Mock
    BlobServiceClient blobServiceClient;
    @Mock
    BlobContainerClient blobContainerClient;
    @Mock
    BlobClient blobClient;

    @Test
    void findByIuvOK() {
        String organizationId = "12345678900";
        String iuv = generateIuv();
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";

        String partitionKey = dateFrom;
        String rowKey = String.format("%s_%s", organizationId, iuv);
        TableEntity tableEntity = new TableEntity(partitionKey, rowKey);
        tableEntity.addProperty(ID_DOMINIO, organizationId);
        tableEntity.addProperty(IUV, iuv);
        mockAzureTable(tableEntity);

        String url = String.format("http://localhost:%s/organizations/%s/iuv/%s?dateFrom=%s&dateTo=%s",
                port,
                organizationId,
                iuv,
                dateFrom,
                dateTo);

        ResponseEntity<ReEventResponse> forEntity = this.restTemplate.getForEntity(url, ReEventResponse.class);
        ReEventResponse reEventResponse = forEntity.getBody();

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(reEventResponse);
        assertThat(reEventResponse.getCount()).isEqualTo(1);
        assertThat(reEventResponse.getData().size()).isEqualTo(1);
        assertThat(reEventResponse.getData().get(0).getOrganizationId()).isEqualTo(organizationId);
        assertThat(reEventResponse.getData().get(0).getIuv()).isEqualTo(iuv);
    }

    @Test
    void findByNoticeNumberOK() {
        String organizationId = "12345678900";
        String iuv = generateIuv();
        String noticeNumber = getNoticeNumber(iuv);
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";

        String partitionKey = dateFrom;
        String rowKey = String.format("%s_%s", organizationId, iuv);
        TableEntity tableEntity = new TableEntity(partitionKey, rowKey);
        tableEntity.addProperty(ID_DOMINIO, organizationId);
        tableEntity.addProperty(NOTICE_NUMBER, noticeNumber);
        mockAzureTable(tableEntity);

        String url = String.format("http://localhost:%s/organizations/%s/notice-number/%s?dateFrom=%s&dateTo=%s",
                port,
                organizationId,
                noticeNumber,
                dateFrom,
                dateTo);

        ResponseEntity<ReEventResponse> forEntity = this.restTemplate.getForEntity(url, ReEventResponse.class);
        ReEventResponse reEventResponse = forEntity.getBody();

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(reEventResponse);
        assertThat(reEventResponse.getCount()).isEqualTo(1);
        assertThat(reEventResponse.getData().size()).isEqualTo(1);
        assertThat(reEventResponse.getData().get(0).getOrganizationId()).isEqualTo(organizationId);
        assertThat(reEventResponse.getData().get(0).getNoticeNumber()).isEqualTo(noticeNumber);
    }


    @Test
    void findBySessionIdOK() {
        String sessionId = generateSessionId();
        String organizationId = "12345678900";
        String iuv = generateIuv();
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";

        String partitionKey = dateFrom;
        String rowKey = String.format("%s_%s", organizationId, iuv);
        TableEntity tableEntity = new TableEntity(partitionKey, rowKey);
        tableEntity.addProperty(SESSION_ID, sessionId);
        mockAzureTable(tableEntity);

        String url = String.format("http://localhost:%s/session-id/%s?dateFrom=%s&dateTo=%s",
                port,
                sessionId,
                dateFrom,
                dateTo);

        ResponseEntity<ReEventResponse> forEntity = this.restTemplate.getForEntity(url, ReEventResponse.class);
        ReEventResponse reEventResponse = forEntity.getBody();

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(reEventResponse);
        assertThat(reEventResponse.getCount()).isEqualTo(1);
        assertThat(reEventResponse.getData().size()).isEqualTo(1);
        assertThat(reEventResponse.getData().get(0).getSessionId()).isEqualTo(sessionId);
    }

    @SneakyThrows
    @Test
    void getPayloadByPayloadRefIdOK() {
        String organizationId = "12345678900";
        String iuv = generateIuv();
        String payloadRefId = String.format("%s_%s_test.xml", organizationId, iuv);
        String payload = "<test>123</test>";

        byte[] compressedPayload = zip(payload);
        BinaryData binaryData = BinaryData.fromBytes(compressedPayload);
        Long length = binaryData.getLength();
        mockAzureBlob(binaryData);

        String url = String.format("http://localhost:%s/payload-ref-id/%s",
                port,
                payloadRefId);

        ResponseEntity<PayloadResponse> forEntity = this.restTemplate.getForEntity(url, PayloadResponse.class);
        PayloadResponse payloadResponse = forEntity.getBody();

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(payloadResponse);
        assertThat(payloadResponse.getPayloadRefId()).isEqualTo(payloadRefId);
        assertThat(payloadResponse.getPayloadLength()).isEqualTo(length);

        String base64Encoded = base64Encode(compressedPayload);
        assertThat(payloadResponse.getPayload()).isEqualTo(base64Encoded);

        byte[] payloadDecoded = base64Decode(payloadResponse.getPayload());
        assertThat(payloadDecoded).isEqualTo(compressedPayload);

        String payloadReverse = getUtf8String(unzip(payloadDecoded));
        assertThat(payloadReverse).isEqualTo(payload);
    }

    private static String generateSessionId(){
        return UUID.randomUUID().toString();
    }
    private static String generateIuv() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 17);
    }
    private static String getNoticeNumber(String iuv) {
        return "3"+iuv;
    }

    private void mockAzureTable(TableEntity tableEntity){
        org.springframework.test.util.ReflectionTestUtils.setField(reService, "tableServiceClient", tableServiceClient);

        Mockito.when(tableServiceClient.getTableClient(any())).thenReturn(tableClient);
        Mockito.when(tableClient.listEntities(any(), any(), any())).thenReturn(pagedIterable);
        Mockito.when(pagedIterable.stream()).thenReturn(Stream.of(tableEntity));
    }
    private void mockAzureBlob(BinaryData binaryData){
        org.springframework.test.util.ReflectionTestUtils.setField(reService, "blobServiceClient", blobServiceClient);

        Mockito.when(blobServiceClient.getBlobContainerClient(any())).thenReturn(blobContainerClient);
        Mockito.when(blobContainerClient.getBlobClient(any())).thenReturn(blobClient);
        Mockito.when(blobClient.downloadContent()).thenReturn(binaryData);
    }

    public static byte[] zip(String str) throws IOException {
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream bais = new ByteArrayOutputStream(strBytes.length);
        GZIPOutputStream gzipOut = new GZIPOutputStream(bais);
        gzipOut.write(strBytes);
        gzipOut.close();
        byte[] compressed = bais.toByteArray();
        bais.close();
        return compressed;
    }

    public static byte[] unzip(byte[] compressed) throws IOException {
      ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
      GZIPInputStream gzipInputStream = new GZIPInputStream(bais);
      return gzipInputStream.readAllBytes();
    }

    public static String getUtf8String(byte[] bytes){
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String base64Encode(byte[] compressed){
        return getUtf8String(Base64.getEncoder().encode(compressed));
    }
    private static byte[] base64Decode(String base64Encoded){
        return Base64.getDecoder().decode(base64Encoded);
    }

}
