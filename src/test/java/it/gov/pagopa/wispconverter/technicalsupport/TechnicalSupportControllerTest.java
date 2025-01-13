package it.gov.pagopa.wispconverter.technicalsupport;


import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEventResponse;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.re.ReEventEntity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TechnicalSupportControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private ReEventRepository reEventRepository;

    private static String generateOperationId() {
        return UUID.randomUUID().toString();
    }

    private static String generateIuv() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 17);
    }

    private static String getNoticeNumber(String iuv) {
        return "3" + iuv;
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

    public static String getUtf8String(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String base64Encode(byte[] compressed) {
        return getUtf8String(Base64.getEncoder().encode(compressed));
    }

    private static byte[] base64Decode(String base64Encoded) {
        return Base64.getDecoder().decode(base64Encoded);
    }

    @Test
    void findByIuvOK() {
        String organizationId = "12345678900";
        String iuv = generateIuv();
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";


        List<ReEventEntity> reEventEntityList = Collections.singletonList(ReEventEntity.builder()
                .partitionKey(dateFrom)
                .domainId(organizationId)
                .iuv(iuv)
                .build());

        //Mockito.when(reEventRepository.findByOperationIdAndSessionId(any(), any(), any(), any())).thenReturn(reEventEntityList);

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
        assertThat(reEventResponse.getData()).hasSize(1);
        assertThat(reEventResponse.getData().get(0).getDomainId()).isEqualTo(organizationId);
        assertThat(reEventResponse.getData().get(0).getIuv()).isEqualTo(iuv);
    }

    @Test
    void findByNoticeNumberOK() {
        String organizationId = "12345678900";
        String iuv = generateIuv();
        String noticeNumber = getNoticeNumber(iuv);
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";

        List<ReEventEntity> reEventEntityList = Collections.singletonList(ReEventEntity.builder()
                .partitionKey(dateFrom)
                .domainId(organizationId)
                .noticeNumber(noticeNumber)
                .build());
        //Mockito.when(reEventRepository.findByOperationIdAndSessionId(any(), any(), any(), any())).thenReturn(reEventEntityList);

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
        assertThat(reEventResponse.getData()).hasSize(1);
        assertThat(reEventResponse.getData().get(0).getDomainId()).isEqualTo(organizationId);
        assertThat(reEventResponse.getData().get(0).getNoticeNumber()).isEqualTo(noticeNumber);
    }

    @Test
    void findByOperationIdOK() {
        String operationId = generateOperationId();
        String organizationId = "12345678900";
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";

        List<ReEventEntity> reEventEntityList = Collections.singletonList(ReEventEntity.builder()
                .partitionKey(dateFrom)
                .operationId(operationId)
                .build());

        //Mockito.when(reEventRepository.findByOperationId(any(), any(), any())).thenReturn(reEventEntityList);

        String url = String.format("http://localhost:%s/operation-id/%s?dateFrom=%s&dateTo=%s",
                port,
                operationId,
                dateFrom,
                dateTo);

        ResponseEntity<ReEventResponse> forEntity = this.restTemplate.getForEntity(url, ReEventResponse.class);
        ReEventResponse reEventResponse = forEntity.getBody();

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(reEventResponse);
        assertThat(reEventResponse.getCount()).isEqualTo(1);
        assertThat(reEventResponse.getData()).hasSize(1);
        assertThat(reEventResponse.getData().get(0).getOperationId()).isEqualTo(operationId);
    }

    @SneakyThrows
    @Test
    void findByOperationIdVerifyPayloadOK() {
        String operationId = generateOperationId();
        String organizationId = "12345678900";
        String dateFrom = "2024-03-18";
        String dateTo = "2024-03-18";

        String payload = "<test>123</test>";
        String compressedPayload = base64Encode(zip(payload));
        Integer compressedPayloadLength = compressedPayload.length();

        List<ReEventEntity> reEventEntityList = Collections.singletonList(ReEventEntity.builder()
                .partitionKey(dateFrom)
                .operationId(operationId)
                //.compressedPayload(compressedPayload)
                //.compressedPayloadLength(compressedPayloadLength)
                .build());
        //Mockito.when(reEventRepository.findByOperationId(any(), any(), any())).thenReturn(reEventEntityList);

        String url = String.format("http://localhost:%s/operation-id/%s?dateFrom=%s&dateTo=%s",
                port,
                operationId,
                dateFrom,
                dateTo);

        ResponseEntity<ReEventResponse> forEntity = this.restTemplate.getForEntity(url, ReEventResponse.class);
        ReEventResponse reEventResponse = forEntity.getBody();

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(reEventResponse);
        assertThat(reEventResponse.getCount()).isEqualTo(1);
        assertThat(reEventResponse.getData()).hasSize(1);
        assertThat(reEventResponse.getData().get(0).getOperationId()).isEqualTo(operationId);

        /*assertThat(reEventResponse.getData().get(0).getCompressedPayload()).isEqualTo(compressedPayload);
        assertThat(reEventResponse.getData().get(0).getCompressedPayloadLength()).isEqualTo(compressedPayloadLength);

        String payloadReverse = getUtf8String(unzip(base64Decode(reEventResponse.getData().get(0).getCompressedPayload())));
        assertThat(payloadReverse).isEqualTo(payload);*/
    }

}
