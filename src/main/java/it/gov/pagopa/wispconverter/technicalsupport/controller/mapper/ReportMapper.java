package it.gov.pagopa.wispconverter.technicalsupport.controller.mapper;

import it.gov.pagopa.wispconverter.technicalsupport.repository.model.report.*;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.report.NotCompletedRPTStatistic;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.report.RPTStatistic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ReportMapper {

    public ReportEntity toEntity(RPTStatistic dto) {
        Map<String, Integer> notCompletedTriggeredPrimitives = dto.getNotCompletedTriggeredPrimitivesByStatus();
        NotCompletedRPTStatistic notCompletedRPTs = dto.getNotCompletedRPTs();

        ReportPaymentEntity reportPayments = ReportPaymentEntity.builder()
                .totalOnNdp(dto.getTotalTriggerPrimitivesOnNdp())
                .totalOnWisp(dto.getTotalTriggerPrimitivesOnWisp())
                .triggerPrimitives(ReportTriggeredPrimitivesEntity.builder()
                        .totalCarts(dto.getTotalRPTCarts())
                        .totalNoCarts(dto.getTotalRPTSingles())
                        .cartsCompleted(dto.getCompletedRPTCarts())
                        .noCartsCompleted(dto.getCompletedRPTSingles())
                        .allNotCompleted(NotCompletedTriggerPrimitivesEntity.builder()
                                .rptTimeoutTrigger(notCompletedTriggeredPrimitives.get("rpt_timeout_trigger"))
                                .receiptKo(notCompletedTriggeredPrimitives.get("receipt_ko"))
                                .ecommerceHangTimeoutTrigger(notCompletedTriggeredPrimitives.get("ecommerce_hang_timeout_trigger"))
                                .paymentTokenTimeoutTrigger(notCompletedTriggeredPrimitives.get("payment_token_timeout_trigger"))
                                .receiptKo(notCompletedTriggeredPrimitives.get("receipt_ko"))
                                .noState(notCompletedTriggeredPrimitives.get("no_state"))
                                .build())
                        .build())
                .build();
        ReportReceiptEntity reportReceipts = ReportReceiptEntity.builder()
                .completed(ReportSentReceiptEntity.builder()
                        .withOkReceipts(dto.getReceiptOkSent())
                        .withKoReceipts(dto.getReceiptKoSent())
                        .build())
                .notCompleted(ReportNotSentReceiptEntity.builder()
                        .rejected(ReportNotSentReceiptStatsEntity.builder()
                                .receiptOkCount(notCompletedRPTs.getRejected().getReceiptOkCount())
                                .receiptKoCount(notCompletedRPTs.getRejected().getReceiptKoCount())
                                .receipts(notCompletedRPTs.getRejected().getReceiptsIds())
                                .build())
                        .notSentEndRetry(ReportNotSentReceiptStatsEntity.builder()
                                .receiptOkCount(notCompletedRPTs.getNotSentEndRetry().getReceiptOkCount())
                                .receiptKoCount(notCompletedRPTs.getNotSentEndRetry().getReceiptKoCount())
                                .receipts(notCompletedRPTs.getNotSentEndRetry().getReceiptsIds())
                                .build())
                        .ongoing(ReportNotSentReceiptStatsEntity.builder()
                                .receiptOkCount(notCompletedRPTs.getOngoing().getReceiptOkCount())
                                .receiptKoCount(notCompletedRPTs.getOngoing().getReceiptKoCount())
                                .receipts(notCompletedRPTs.getOngoing().getReceiptsIds())
                                .build())
                        .scheduled(ReportNotSentReceiptStatsEntity.builder()
                                .receiptOkCount(notCompletedRPTs.getSendingOrScheduled().getReceiptOkCount())
                                .receiptKoCount(notCompletedRPTs.getSendingOrScheduled().getReceiptKoCount())
                                .receipts(notCompletedRPTs.getSendingOrScheduled().getReceiptsIds())
                                .build())
                        .neverSent(ReportNotSentReceiptStatsEntity.builder()
                                .receiptOkCount(notCompletedRPTs.getNeverSent().getReceiptOkCount())
                                .receiptKoCount(notCompletedRPTs.getNeverSent().getReceiptKoCount())
                                .receipts(notCompletedRPTs.getNeverSent().getReceiptsIds())
                                .build())
                        .build())
                .build();

        String type = dto.getType().name();
        return ReportEntity.builder()
                .id(dto.getDate() + "_" + type)
                .date(type)
                .payments(reportPayments)
                .receipts(reportReceipts)
                .build();
    }
}
