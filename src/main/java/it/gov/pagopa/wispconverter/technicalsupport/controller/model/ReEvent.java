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

    @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "The event unique identifier")
    @JsonProperty("id")
    private String id;

    @Schema(example = "9ef8dd1b-da3a-428d-81fe-d567d5eb615e", description = "The unique identifier of the micro-step")
    @JsonProperty("operation_id")
    private String operationId;

    @Schema(example = "nodoInviaRPT", description = "The name of the step, either named business process in NDP terminology")
    @JsonProperty("business_process")
    private String businessProcess;

    @Schema(example = "2024-01-01T12:30:25.42017Z", description = "The date on which the event is occurred")
    @JsonProperty("inserted_timestamp")
    private Instant insertedTimestamp;

    @Schema(example = "INTERFACE", description = "The category of the event")
    @JsonProperty("event_category")
    private EventCategory eventCategory;

    @Schema(example = "RECEIPT_SEND_PROCESSED", description = "The status of the event, related to the action done by the micro-step")
    @JsonProperty("status")
    private String status;

    @Schema(example = "OK", description = "The outcome related to the event, generally obtained if an events could ends in error")
    @JsonProperty("outcome")
    private String outcome;

    @Schema(example = "POST", description = "The HTTP method related to an event that describe the communication with an external entity")
    @JsonProperty("http_method")
    private String httpMethod;

    @Schema(example = "https://api.platform.pagopa.it/nodo/nodo-per-psp/v1", description = "The HTTP URI related to an event that describe the communication with an external entity")
    @JsonProperty("http_uri")
    private String httpUri;

    @Schema(example = "200", description = "The HTTP status code related to an event that describe the communication with an external entity")
    @JsonProperty("http_status_code")
    private Integer httpStatusCode;

    @Schema(example = "456", description = "The execution time of the communication with an external entity")
    @JsonProperty("execution_time")
    private Long executionTimeMs;

    @Schema(example = "Accept-Encoding :[\"gzip, deflate, br\"]", description = "The HTTP headers related to the request used in the communication with an external entity")
    @JsonProperty("request_headers")
    private String requestHeaders;

    @Schema(example = "X-Request-Id: [\"433cc0000009eca66aef0000004dabd0\"]", description = "The HTTP headers related to the response used in the communication with an external entity")
    @JsonProperty("response_headers")
    private String responseHeaders;

    @Schema(example = "{\"content\":\"<...>\"}", description = "The HTTP payload related to the request used in the communication with an external entity")
    @JsonProperty("request_payload")
    private String requestPayload;

    @Schema(example = "{\"outcome\": \"OK\"}", description = "The HTTP payload related to the response used in the communication with an external entity")
    @JsonProperty("response_payload")
    private String responsePayload;

    @Schema(example = "WIC-1000", description = "The specific applicative error code, used in order to better track the root cause of the payment flow failure")
    @JsonProperty("operation_error_code")
    private String operationErrorCode;

    @Schema(example = "Generic parsing error", description = "The stack trace reference related to the specific applicative error code, used in order to better track the root cause of the payment flow failure")
    @JsonProperty("operation_error_line")
    private String operationErrorLine;

    @Schema(example = "Error while parsing payload. {0}", description = "The user-friendly description of the specific applicative error code, used in order to better track the root cause of the payment flow failure")
    @JsonProperty("operation_error_detail")
    private String operationErrorDetail;

    @Schema(example = "12345678901_aa0518a9-194d-4de2-999f-e462c3746e24", description = "The session identifier related to the single payment flow")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(example = "12345678900982273480765907-0001", description = "The cart identifier related to the nodoInviaCarrelloRPT request")
    @JsonProperty("cart_id")
    private String cartId;

    @Schema(example = "001924757343397669", description = "The IUV code of the payment")
    @JsonProperty("iuv")
    private String iuv;

    @Schema(example = "351147559280022097", description = "The notice number code of the payment")
    @JsonProperty("notice_number")
    private String noticeNumber;

    @Schema(example = "012345678901", description = "The identifier of the creditor institution")
    @JsonProperty("domain_id")
    private String domainId;

    @Schema(example = "001924757343397669-00001", description = "The payment context code (aka CCP) of the payment tentative")
    @JsonProperty("ccp")
    private String ccp;

    @Schema(example = "60000000001", description = "The identifier of the PSP service institute")
    @JsonProperty("psp")
    private String psp;

    @Schema(example = "11111111111_05", description = "The identifier of the station that provides services for the creditor institution")
    @JsonProperty("station")
    private String station;

    @Schema(example = "60000000001_01", description = "The identifier of the channel that provides services for the PSP")
    @JsonProperty("channel")
    private String channel;

    @Schema(example = "ea694073-883a-46f1-9b19-bf3ed6bd3c3e", description = "The unique token of the payment handled by NDP")
    @JsonProperty("payment_token")
    private String paymentToken;

    @Schema(example = "Scheduled eCommerce hang release: ...", description = "The additional description, useful for certain steps")
    @JsonProperty("info")
    private String info;
}
