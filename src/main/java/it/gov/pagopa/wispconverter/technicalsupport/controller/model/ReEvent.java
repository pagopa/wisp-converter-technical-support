package it.gov.pagopa.wispconverter.technicalsupport.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReEvent {

    @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Id")
    @JsonProperty("id")
    private String id;

    @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Identificativo dell'operazione eseguita")
    @JsonProperty("operation_id")
    private String operationId;

    @Schema(example = "nodoInviaRPT", description = "Campo che identifica il processo di business")
    @JsonProperty("business_process")
    private String businessProcess;

    @Schema(example = "2024-03-18T09:04:27.42017Z", description = "Data di inserimento evento")
    @JsonProperty("inserted_timestamp")
    private Instant insertedTimestamp;

    @Schema(example = "INTERFACE", description = "...")
    @JsonProperty("event_category")
    private EventCategoryEnum eventCategory;

    @Schema(example = "EXTRACTED_DATA_FROM_RPT", description = "Stato dell'RPT o del Carrello se la categoriaEvento è INTERN, altrienti è null")
    @JsonProperty("status")
    private String status;

    @Schema(example = "OK", description = "Esito dell'operazione eseguita. Esempio: SEND, SEND_FAILURE, RECEIVED, RECEIVED_FAILURE, NEVER_RECEIVED, EXECUTED_INTERNAL_STEP")
    @JsonProperty("outcome")
    private String outcome;

    @Schema(example = "POST", description = "HTTP method")
    @JsonProperty("http_method")
    private String httpMethod;

    @Schema(example = "https://api.platform.pagopa.it/nodo/nodo-per-psp/v1", description = "HTTP URI")
    @JsonProperty("http_uri")
    private String httpUri;

    @Schema(example = "200", description = "HTTP status")
    @JsonProperty("http_status_code")
    private Integer httpStatusCode;

    @Schema(example = "456", description = "HTTP execution time")
    @JsonProperty("execution_time")
    private Long executionTimeMs;

    @Schema(example = "Accept-Encoding :[\"gzip, deflate, br\"]", description = "HTTP headers")
    @JsonProperty("request_headers")
    private String requestHeaders;

    @Schema(example = "Accept-Encoding :[\"gzip, deflate, br\"]", description = "HTTP headers")
    @JsonProperty("response_headers")
    private String responseHeaders;

    @Schema(example = "abadasdbasbdasd", description = "HTTP payload compress GZIP")
    @JsonProperty("request_payload")
    private String requestPayload;

    @Schema(example = "abadasdbasbdasd", description = "HTTP payload compress GZIP")
    @JsonProperty("response_payload")
    private String responsePayload;

    @Schema(example = "WIC-1000", description = "Error code se stato Failed")
    @JsonProperty("operation_error_code")
    private String operationErrorCode;    //dettaglio response in uscita

    @Schema(example = "Generic parsing error", description = "Title se stato Failed")
    @JsonProperty("operation_error_line")
    private String operationErrorLine;

    @Schema(example = "Error while parsing payload. {0}", description = "Detail se stato Failed")
    @JsonProperty("operation_error_detail")
    private String operationErrorDetail;

    @Schema(example = "982273480765907", description = "Identificativo dell'operazione asincrona già eseguita")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(example = "12345678900982273480765907-0001", description = "Identificativo del carrello")
    @JsonProperty("cart_id")
    private String cartId;

    @Schema(example = "982273480765907", description = "Identificativo univoco versamento")
    @JsonProperty("iuv")
    private String iuv;

    @Schema(example = "3982273480765907", description = "Numero avviso")
    @JsonProperty("notice_number")
    private String noticeNumber;

    @Schema(example = "12345678900", description = "Id dell'EC")
    @JsonProperty("domain_id")
    private String domainId;

    @Schema(example = "TEST_4178733642", description = "Codice contesto pagmaneto")
    @JsonProperty("ccp")
    private String ccp;

    @Schema(example = "60000000001", description = "Id del prestatore servizi di pagamento")
    @JsonProperty("psp")
    private String psp;

    @Schema(example = "11111111111_05", description = "Identificativo della stazione che eroga i servizi per la PA")
    @JsonProperty("station")
    private String station;

    @Schema(example = "60000000001_01", description = "Identificativo del canale che eroga i servizi per il PSP")
    @JsonProperty("channel")
    private String channel;

    @Schema(example = "AABB123", description = "Numero univoco del pagamento")
    @JsonProperty("payment_token")
    private String paymentToken;

    @Schema(example = "https://api.platform.pagopa.it/nodo/nodo-per-psp/v1", description = "Informazioni generiche, es URI di chi ha chiamato o di chi sta chiamando")
    @JsonProperty("info")
    private String info;
}
