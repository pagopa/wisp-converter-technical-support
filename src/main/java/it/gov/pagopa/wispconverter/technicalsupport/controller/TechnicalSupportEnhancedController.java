package it.gov.pagopa.wispconverter.technicalsupport.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring.*;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment.*;
import it.gov.pagopa.wispconverter.technicalsupport.service.EnhancedFeaturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static it.gov.pagopa.wispconverter.technicalsupport.util.Constants.*;

@RestController
@RequestMapping("/enhanced")
@Validated
@RequiredArgsConstructor
@Tag(name = "Technical Support - Enhanced features", description = "APIs for reading events and other data on the WISP Dismantling process")
public class TechnicalSupportEnhancedController {

    private final EnhancedFeaturesService enhancedFeaturesService;

    @Operation(summary = "Get payment status searching by IUV", description = "Retrieve the status of a payment, analyzing also the status fo the receipt.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved payment status", content = @Content(schema = @Schema(implementation = PaymentFlowStatusResponse.class)))
    })
    @GetMapping(value = "/organizations/{" + ORGANIZATION + "}/iuv/{" + IUV + "}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentFlowStatusResponse findStatusByIuv(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "The identifier of the creditor institution") String organizationId,
            @PathVariable(name = IUV) @Schema(example = "982273480765907", description = "The IUV code of the payment") String iuv,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-01-01", description = "The lower limit of the date slot, used as delimiter for the search (in yyyy-MM-dd)") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-01-01", description = "The upper limit of the date slot, used as delimiter for the search (in yyyy-MM-dd)") LocalDate dateTo) {

        List<PaymentFlowStatus> paymentsStatus = enhancedFeaturesService.getPaymentStatusFindByIuv(dateFrom, dateTo, organizationId, iuv);
        return PaymentFlowStatusResponse.builder()
                .paymentStatuses(paymentsStatus)
                .lowerBoundDate(dateFrom)
                .upperBoundDate(dateTo)
                .build();
    }

    @Operation(summary = "Get payment details searching by IUV", description = "Retrieve the details of a payment searching by IUV, analyzing all its status and events.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved details", content = @Content(schema = @Schema(implementation = PaymentFlowsResponse.class)))
    })
    @PostMapping(value = "/organizations/{" + ORGANIZATION + "}/iuv/{" + IUV + "}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentFlowsResponse findByIuv(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "The identifier of the creditor institution") String organizationId,
            @PathVariable(name = IUV) @Schema(example = "000000999900000", description = "The IUV code of the payment") String iuv,
            @RequestBody @Schema(description = "The filters to be applied on the request") PaymentFlowsFilterRequest filters) {

        List<PaymentFlow> paymentFlows = enhancedFeaturesService.findByIuvEnhanced(organizationId, iuv, filters);
        return PaymentFlowsResponse.builder()
                .metadata(PaymentFlowMetadata.builder()
                        .searchFilters(PaymentFlowSearchFilterMetadata.builder()
                                .iuv(iuv)
                                .lowerBoundDate(filters.getLowerBoundDate())
                                .upperBoundDate(filters.getUpperBoundDate())
                                .build())
                        .numberOfFlowsFound(paymentFlows.size())
                        .showDataInCompactForm(filters.getShowCompactForm())
                        .showInternalStatusesDetails(filters.getShowDetails())
                        .showPayloadsInInterfaceStatuses(filters.getShowPayloads())
                        .build())
                .flows(paymentFlows)
                .build();
    }

    @Operation(summary = "Get payment details searching by notice number", description = "Retrieve the details of a payment searching by notice number, analyzing all its status and events.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved details", content = @Content(schema = @Schema(implementation = PaymentFlowsResponse.class)))
    })
    @PostMapping(value = "/organizations/{" + ORGANIZATION + "}/notice-number/{" + NOTICE_NUMBER + "}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentFlowsResponse findByNoticeNumber(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "The identifier of the creditor institution") String organizationId,
            @PathVariable(name = NOTICE_NUMBER) @Schema(example = "351147559280022097", description = "The notice number code of the payment") String noticeNumber,
            @RequestBody @Schema(description = "The filters to be applied on the request") PaymentFlowsFilterRequest filters) {

        List<PaymentFlow> paymentFlows = enhancedFeaturesService.findByNoticeNumberEnhanced(organizationId, noticeNumber, filters);
        return PaymentFlowsResponse.builder()
                .metadata(PaymentFlowMetadata.builder()
                        .searchFilters(PaymentFlowSearchFilterMetadata.builder()
                                .noticeNumber(noticeNumber)
                                .lowerBoundDate(filters.getLowerBoundDate())
                                .upperBoundDate(filters.getUpperBoundDate())
                                .build())
                        .numberOfFlowsFound(paymentFlows.size())
                        .showDataInCompactForm(filters.getShowCompactForm())
                        .showInternalStatusesDetails(filters.getShowDetails())
                        .showPayloadsInInterfaceStatuses(filters.getShowPayloads())
                        .build())
                .flows(paymentFlows)
                .build();
    }

    @Operation(summary = "Get payment details searching by session ID", description = "Retrieve the details of a payment searching by session ID, analyzing all its status and events.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved event", content = @Content(schema = @Schema(implementation = PaymentFlowsResponse.class)))
    })
    @PostMapping(value = "/session-id/{" + SESSION_ID + "}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentFlowsResponse findBySessionId(
            @PathVariable(name = SESSION_ID) @Schema(example = "12345678901_aa0518a9-194d-4de2-999f-e462c3746e24", description = "The session ID related to the single payment flow") String sessionId,
            @RequestBody @Schema(description = "The filters to be applied on the request") PaymentFlowsFilterRequest filters) {

        List<PaymentFlow> paymentFlows = enhancedFeaturesService.findBySessionIdEnhanced(sessionId, filters);
        return PaymentFlowsResponse.builder()
                .metadata(PaymentFlowMetadata.builder()
                        .searchFilters(PaymentFlowSearchFilterMetadata.builder()
                                .sessionId(sessionId)
                                .lowerBoundDate(filters.getLowerBoundDate())
                                .upperBoundDate(filters.getUpperBoundDate())
                                .build())
                        .numberOfFlowsFound(paymentFlows.size())
                        .showDataInCompactForm(filters.getShowCompactForm())
                        .showInternalStatusesDetails(filters.getShowDetails())
                        .showPayloadsInInterfaceStatuses(filters.getShowPayloads())
                        .build())
                .flows(paymentFlows)
                .build();
    }

    @Operation(summary = "Get snapshot of receipt status", description = "Retrieve a snapshot of receipt status at specific slot time.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully extracted snapshot", content = @Content(schema = @Schema(implementation = ReceiptsStatusSnapshotResponse.class)))
    })
    @GetMapping(value = "/monitoring/receipts/snapshot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReceiptsStatusSnapshotResponse extractReceiptSnapshot(
            @RequestParam(name = DATE_FROM) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Schema(example = "2024-01-01T12:00:00", description = "The lower limit of the date slot, used as delimiter for the search time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone") LocalDateTime dateFrom,
            @RequestParam(name = DATE_TO) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Schema(example = "2024-01-01T13:00:00", description = "The upper limit of the date slot, used as delimiter for the search time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone") LocalDateTime dateTo) {

        List<ReceiptsStatusSnapshot> receiptsStatusSnapshots = enhancedFeaturesService.extractReceiptSnapshot(dateFrom, dateTo);
        return ReceiptsStatusSnapshotResponse.builder()
                .snapshot(receiptsStatusSnapshots)
                .lowerBoundDate(dateFrom)
                .upperBoundDate(dateTo)
                .build();
    }

    @Operation(summary = "Get pending receipts", description = "Retrieve the list of pending receipt that could have not been sent to creditor institution", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully extracted data", content = @Content(schema = @Schema(implementation = ReceiptsStatusSnapshotResponse.class)))
    })
    @PostMapping(value = "/monitoring/receipts/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public PendingReceiptsResponse extractPendingReceipts(@RequestBody PendingReceiptsFilterRequest request) {

        List<PendingReceipt> pendingReceipts = enhancedFeaturesService.extractPendingReceipts(request);
        return PendingReceiptsResponse.builder()
                .pendingReceipts(pendingReceipts)
                .build();
    }
}
