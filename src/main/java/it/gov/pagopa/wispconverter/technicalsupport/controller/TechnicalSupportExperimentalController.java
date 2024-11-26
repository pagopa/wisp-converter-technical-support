package it.gov.pagopa.wispconverter.technicalsupport.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring.*;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment.*;
import it.gov.pagopa.wispconverter.technicalsupport.service.ExperimentalService;
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
@RequestMapping("/experimental")
@Validated
@RequiredArgsConstructor
@Tag(name = "Technical Support - Enhanced features", description = "APIs for read events and other data about WISP Dismantling process")
public class TechnicalSupportExperimentalController {

    private final ExperimentalService experimentalService;

    @Operation(summary = "Get payment details searching by IUV", description = "Retrieve the details of a payment searching by IUV, analyzing all its status and events.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved details", content = @Content(schema = @Schema(implementation = ReEventExperimentalResponse.class)))
    })
    @GetMapping(value = "/organizations/{" + ORGANIZATION + "}/iuv/{" + IUV + "}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventExperimentalResponse findByIuv(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Creditor institution identifier") String organizationId,
            @PathVariable(name = IUV) @Schema(example = "000000999900000", description = "IUV payment code") String iuv,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-01-01", description = "Lower limit date (in yyyy-MM-dd)") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-01-01", description = "Upper limit date (in yyyy-MM-dd)") LocalDate dateTo,
            @RequestParam(name = SHOW_DETAILS, defaultValue = "false") @Schema(example = "false", description = "Show internal statuses of whole process", defaultValue = "false") Boolean showDetails,
            @RequestParam(name = SHOW_COMPACT_FORM, defaultValue = "true") @Schema(example = "true", description = "Show data in a compact form, reducing to essential information", defaultValue = "true") Boolean showCompactForm,
            @RequestParam(name = SHOW_PAYLOADS, defaultValue = "false") @Schema(example = "false", description = "Show request and response payloads in INTERFACE events", defaultValue = "false") Boolean showPayloads) {

        List<PaymentFlow> paymentFlows = experimentalService.findByIuvEnhanced(dateFrom, dateTo, organizationId, iuv, showDetails, showCompactForm, showPayloads);
        return ReEventExperimentalResponse.builder()
                .metadata(PaymentFlowMetadata.builder()
                        .searchFilters(PaymentFlowSearchFilterMetadata.builder()
                                .iuv(iuv)
                                .lowerBoundDate(dateFrom)
                                .upperBoundDate(dateTo)
                                .build())
                        .numberOfFlowsFound(paymentFlows.size())
                        .showDataInCompactForm(showCompactForm)
                        .showInternalStatusesDetails(showDetails)
                        .showPayloadsInInterfaceStatuses(showPayloads)
                        .build())
                .flows(paymentFlows)
                .build();
    }

    @Operation(summary = "Get payment details searching by notice number", description = "Retrieve the details of a payment searching by notice number, analyzing all its status and events.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved details", content = @Content(schema = @Schema(implementation = ReEventExperimentalResponse.class)))
    })
    @GetMapping(value = "/organizations/{" + ORGANIZATION + "}/notice-number/{" + NOTICE_NUMBER + "}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventExperimentalResponse findByNoticeNumber(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Creditor institution identifier") String organizationId,
            @PathVariable(name = NOTICE_NUMBER) @Schema(example = "982273480765907", description = "Notice number payment code") String noticeNumber,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-01-01", description = "Lower limit date (in yyyy-MM-dd)") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-01-01", description = "Upper limit date (in yyyy-MM-dd)") LocalDate dateTo,
            @RequestParam(name = SHOW_DETAILS, defaultValue = "false") @Schema(example = "false", description = "Show internal statuses of whole process", defaultValue = "false") Boolean showDetails,
            @RequestParam(name = SHOW_COMPACT_FORM, defaultValue = "true") @Schema(example = "true", description = "Show data in a compact form, reducing to essential information", defaultValue = "true") Boolean showCompactForm,
            @RequestParam(name = SHOW_PAYLOADS, defaultValue = "true") @Schema(example = "true", description = "Show request and response payloads in INTERFACE events", defaultValue = "true") Boolean showPayloads) {

        List<PaymentFlow> paymentFlows = experimentalService.findByNoticeNumberEnhanced(dateFrom, dateTo, organizationId, noticeNumber, showDetails, showCompactForm, showPayloads);
        return ReEventExperimentalResponse.builder()
                .metadata(PaymentFlowMetadata.builder()
                        .searchFilters(PaymentFlowSearchFilterMetadata.builder()
                                .noticeNumber(noticeNumber)
                                .lowerBoundDate(dateFrom)
                                .upperBoundDate(dateTo)
                                .build())
                        .numberOfFlowsFound(paymentFlows.size())
                        .showDataInCompactForm(showCompactForm)
                        .showInternalStatusesDetails(showDetails)
                        .showPayloadsInInterfaceStatuses(showPayloads)
                        .build())
                .flows(paymentFlows)
                .build();
    }

    @Operation(summary = "Get payment details searching by session ID", description = "Retrieve the details of a payment searching by session ID, analyzing all its status and events.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved event", content = @Content(schema = @Schema(implementation = ReEventExperimentalResponse.class)))
    })
    @GetMapping(value = "/session-id/{" + SESSION_ID + "}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReEventExperimentalResponse findBySessionId(
            @PathVariable(name = SESSION_ID) @Schema(example = "12345678900_aa0518a9-194d-4de2-999f-e462c3746e24", description = "...") String sessionId,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-01-01", description = "Lower limit date (in yyyy-MM-dd)") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-01-01", description = "Upper limit date (in yyyy-MM-dd)") LocalDate dateTo,
            @RequestParam(name = SHOW_DETAILS, defaultValue = "false") @Schema(example = "false", description = "Show internal statuses of whole process", defaultValue = "false") Boolean showDetails,
            @RequestParam(name = SHOW_COMPACT_FORM, defaultValue = "true") @Schema(example = "true", description = "Show data in a compact form, reducing to essential information", defaultValue = "true") Boolean showCompactForm,
            @RequestParam(name = SHOW_PAYLOADS, defaultValue = "true") @Schema(example = "true", description = "Show request and response payloads in INTERFACE events", defaultValue = "true") Boolean showPayloads) {

        List<PaymentFlow> paymentFlows = experimentalService.findBySessionIdEnhanced(dateFrom, dateTo, sessionId, showDetails, showCompactForm, showPayloads);
        return ReEventExperimentalResponse.builder()
                .metadata(PaymentFlowMetadata.builder()
                        .searchFilters(PaymentFlowSearchFilterMetadata.builder()
                                .sessionId(sessionId)
                                .lowerBoundDate(dateFrom)
                                .upperBoundDate(dateTo)
                                .build())
                        .numberOfFlowsFound(paymentFlows.size())
                        .showDataInCompactForm(showCompactForm)
                        .showInternalStatusesDetails(showDetails)
                        .showPayloadsInInterfaceStatuses(showPayloads)
                        .build())
                .flows(paymentFlows)
                .build();
    }

    @Operation(summary = "Get payment status searching by IUV", description = "Retrieve the status of a payment, analyzing also the status fo the receipt.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved payment status", content = @Content(schema = @Schema(implementation = PaymentFlowStatusResponse.class)))
    })
    @GetMapping(value = "/organizations/{" + ORGANIZATION + "}/iuv/{" + IUV + "}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentFlowStatusResponse findStatusByIuv(
            @PathVariable(name = ORGANIZATION) @Schema(example = "12345678900", description = "Creditor institution identifier") String organizationId,
            @PathVariable(name = IUV) @Schema(example = "982273480765907", description = "IUV payment code") String iuv,
            @RequestParam(name = DATE_FROM) @Schema(example = "2024-01-01", description = "Lower limit date (in yyyy-MM-dd)") LocalDate dateFrom,
            @RequestParam(name = DATE_TO) @Schema(example = "2024-01-01", description = "Upper limit date (in yyyy-MM-dd)") LocalDate dateTo) {

        List<PaymentFlowStatus> paymentsStatus = experimentalService.getPaymentStatusFindByIuv(dateFrom, dateTo, organizationId, iuv);
        return PaymentFlowStatusResponse.builder()
                .paymentStatuses(paymentsStatus)
                .lowerBoundDate(dateFrom)
                .upperBoundDate(dateTo)
                .build();
    }

    @Operation(summary = "Get snapshot of receipt status", description = "Retrieve a snapshot of receipt status at specific slot time.", tags = {"Technical Support - Enhanced features"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully extracted snapshot", content = @Content(schema = @Schema(implementation = ReceiptsStatusSnapshotResponse.class)))
    })
    @GetMapping(value = "/monitoring/receipts/snapshot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReceiptsStatusSnapshotResponse extractReceiptSnapshot(
            @RequestParam(name = DATE_FROM) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Schema(example = "2024-01-01T12:00:00", description = "Lower limit date time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone") LocalDateTime dateFrom,
            @RequestParam(name = DATE_TO) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Schema(example = "2024-01-01T13:00:00", description = "Upper limit date time (in yyyy-MM-ddThh:mm:ss) in 'Europe/Rome' timezone") LocalDateTime dateTo) {

        List<ReceiptsStatusSnapshot> receiptsStatusSnapshots = experimentalService.extractReceiptSnapshot(dateFrom, dateTo);
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
    public PendingReceiptsResponse extractPendingReceipts(@RequestBody PendingReceiptsRequest request) {

        List<PendingReceipt> pendingReceipts = experimentalService.extractPendingReceipts(request);
        return PendingReceiptsResponse.builder()
                .pendingReceipts(pendingReceipts)
                .build();
    }
}
