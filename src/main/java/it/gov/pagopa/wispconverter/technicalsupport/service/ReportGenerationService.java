package it.gov.pagopa.wispconverter.technicalsupport.service;

import com.azure.cosmos.models.PartitionKey;
import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.ReportMapper;
import it.gov.pagopa.wispconverter.technicalsupport.repository.*;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RPTRequestEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.re.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.report.ReportEntity;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.report.RPTStatistic;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.report.RPTStatisticDetail;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.report.ReportType;
import it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility;
import it.gov.pagopa.wispconverter.technicalsupport.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportGenerationService {

    private final ReportMapper reportMapper;
    private final ReEventDataExplorerRepository dataExplorerRepository;
    private final RPTRequestRepository rptRequestRepository;
    private final RTRepository rtRepository;
    private final ReEventRepository reEventRepository;
    private final ReportRepository reportRepository;

    private static void setStatisticsForReceipt(RTEntity receipt,
                                                RPTStatistic rptStats,
                                                Pair<String, String> triggerPrimitivePair,
                                                Set<String> sessionIdsInError) {

        RPTStatisticDetail statistics;

        String sessionId = triggerPrimitivePair.getFirst();
        String primitiveType = triggerPrimitivePair.getSecond();

        String receiptStatus = receipt.getReceiptStatus();
        String receiptId = receipt.getId();
        String receiptType = receipt.getReceiptType();

        switch (receiptStatus) {

            // The receipt was correctly sent (either by direct send or by retry), add it as completed
            case "SENT":
                if ("OK".equals(receiptType)) {
                    rptStats.addReceiptOkSent();
                    // Include sessionId for define the counter of completed RPT carts/single RPT
                    if (Constants.STEP_TRIGGER_PRIMITIVE_NODOINVIARPT.equals(primitiveType)) {
                        rptStats.addCompletedRPTSingles(sessionId);
                    } else {
                        rptStats.addCompletedRPTCarts(sessionId);
                    }
                } else {
                    sessionIdsInError.add(sessionId);
                    rptStats.addReceiptKoSent();
                }
                break;
            case "SENT_REJECTED_BY_EC":
                statistics = rptStats.getNotCompletedRPTs().getRejected();
                updateStatsForOkOrKo(rptStats, sessionIdsInError, receiptType, statistics, receiptId, primitiveType, sessionId);
                break;
            case "NOT_SENT":
                statistics = rptStats.getNotCompletedRPTs().getNotSentEndRetry();
                updateStatsForOkOrKo(rptStats, sessionIdsInError, receiptType, statistics, receiptId, primitiveType, sessionId);
                break;
            case "SCHEDULED", "SENDING":
                statistics = rptStats.getNotCompletedRPTs().getSendingOrScheduled();
                updateStatsForOkOrKo(rptStats, sessionIdsInError, receiptType, statistics, receiptId, primitiveType, sessionId);
                break;
            case "REDIRECT", "PAYING":
                sessionIdsInError.add(sessionId);
                rptStats.getNotCompletedRPTs().getOngoing().addAsKoReceipt(receiptId);
                break;
            default:
                statistics = rptStats.getNotCompletedRPTs().getNeverSent();
                if ("OK".equals(receiptType)) {
                    statistics.addAsOkReceipt(receiptId);
                } else {
                    sessionIdsInError.add(sessionId);
                    statistics.addAsKoReceipt(receiptId);
                    rptStats.addNotCompletedTriggeredPrimitives("no_state");
                }
                break;
        }
    }

    private static void updateStatsForOkOrKo(RPTStatistic rptStats,
                                             Set<String> sessionIdsInError,
                                             String receiptType,
                                             RPTStatisticDetail statistics,
                                             String receiptId,
                                             String primitiveType,
                                             String sessionId) {

        if ("OK".equals(receiptType)) {
            statistics.addAsOkReceipt(receiptId);
            // Include sessionId for define the counter of completed RPT carts/single RPT
            if (Constants.STEP_TRIGGER_PRIMITIVE_NODOINVIARPT.equals(primitiveType)) {
                rptStats.addCompletedRPTSingles(sessionId);
            } else {
                rptStats.addCompletedRPTCarts(sessionId);
            }
        } else {
            sessionIdsInError.add(sessionId);
            statistics.addAsKoReceipt(receiptId);
        }
    }

    public void generateDailyReport(String day) {

        log.info("[Report Generation][Start] Started report generation for {}.", day);
        RPTStatistic rptStats = new RPTStatistic();
        rptStats.setDate(day);
        rptStats.setType(ReportType.DAILY);

        log.info("[Report Generation][Step ] Executing count for event from Data Explorer on NdP...");
        long numberOfTriggerPrimitivesOnNdp = dataExplorerRepository.countTriggerPrimitives(day, day);
        rptStats.setTotalTriggerPrimitivesOnNdp(numberOfTriggerPrimitivesOnNdp);
        log.info("[Report Generation][Step ] Executed count for event from Data Explorer on NdP! Retrieved count: [{}]", numberOfTriggerPrimitivesOnNdp);

        log.info("[Report Generation][Step ] Executing count for trigger primitives on WISP Dismantling...");
        List<RPTRequestEntity> triggerPrimitives = rptRequestRepository.findAllByPartitionKeyExcludingPayload(day);
        Set<Pair<String, String>> allSessionIds = triggerPrimitives.stream()
                .map(triggerPrimitive -> Pair.of(triggerPrimitive.getId(), triggerPrimitive.getPrimitive()))
                .collect(Collectors.toUnmodifiableSet());
        rptStats.setTotalTriggerPrimitivesOnWisp(allSessionIds.size());
        log.info("[Report Generation][Step ] Executed count for trigger primitives on WISP Dismantling! Retrieved count: [{}]", rptStats.getTotalTriggerPrimitivesOnWisp());


        log.info("[Report Generation][Step ] Executing extraction of statistics for each retrieved trigger primitive...");
        int numberOfPrimitives = triggerPrimitives.size();
        int alreadyAnalyzedCount = 0;
        Set<String> sessionIdsInError = new HashSet<>();

        for (Pair<String, String> triggerPrimitivePair : allSessionIds) {

            // Showing "already analyzed" count for better tracking process
            alreadyAnalyzedCount++;
            if (alreadyAnalyzedCount % 5000 == 0) {
                float percentageAnalyzed = CommonUtility.safeDivide(alreadyAnalyzedCount * 100f, numberOfPrimitives);
                log.info("[Report Generation][Step ] At [{}] preliminary checks on receipts were generated on [{}/{}] triggered primitives ({}%)...", LocalDateTime.now(), alreadyAnalyzedCount, numberOfPrimitives, percentageAnalyzed);
            }

            if (Constants.STEP_TRIGGER_PRIMITIVE_NODOINVIARPT.equals(triggerPrimitivePair.getSecond())) {
                rptStats.addNoCartOnTotal();
            } else {
                rptStats.addCartOnTotal();
            }

            String sessionId = triggerPrimitivePair.getFirst();
            List<RTEntity> receipts = rtRepository.findStatusInfoBySessionId(sessionId);

            // Fallback if no RT is written in receipts-rt
            if (receipts.isEmpty()) {
                sessionIdsInError.add(sessionId);
            }

            for (RTEntity receipt : receipts) {
                setStatisticsForReceipt(receipt, rptStats, triggerPrimitivePair, sessionIdsInError);
            }
        }
        log.info("[Report Generation][Step ] At [{}] preliminary checks on receipts were generated on all triggered primitives!", LocalDateTime.now());

        // Cataloguing errors by triggered business process
        alreadyAnalyzedCount = 0;
        int numberOfSessionIdsInError = sessionIdsInError.size();
        Map<String, String> pairs = new HashMap<>();

        log.info("[Report Generation][Step ] Analysing the causes for which KO receipts were generated on [{}] sessions...", numberOfSessionIdsInError);
        for (String sessionId : sessionIdsInError) {

            // Showing "already analyzed" count for better tracking process
            alreadyAnalyzedCount++;
            if (alreadyAnalyzedCount % 5000 == 0) {
                float percentageAnalyzed = CommonUtility.safeDivide(alreadyAnalyzedCount * 100f, numberOfSessionIdsInError);
                log.info("[Report Generation][Step ] At [{}] analysis of causes on which KO receipts were made on [{}/{}] triggered primitives ({}%)...", LocalDateTime.now(), alreadyAnalyzedCount, numberOfSessionIdsInError, percentageAnalyzed);
            }

            // Add only one business process for each distinct sessionId
            List<ReEventEntity> events = reEventRepository.findRtTriggerRelatedEventBySessionId(sessionId);
            events.forEach(event -> pairs.putIfAbsent(event.getSessionId(), event.getBusinessProcess()));
        }

        // aggiorna il conteggio delle primitive non concluse con successo per stato di errore
        pairs.forEach((sessionId, businessProcess) -> rptStats.addNotCompletedTriggeredPrimitives(businessProcess.replace("-", "_")));

        reportRepository.save(reportMapper.toEntity(rptStats));
        log.info("[Report Generation][End  ] Ended report generation for {}.", day);
    }

    public void generateWeeklyReport(String dayOfThisWeek) {

        log.info("[Report Generation][Start] Started weekly report generation for week previous than day {}.", dayOfThisWeek);
        String yesterday = CommonUtility.getYesterday(dayOfThisWeek);
        mergeMultipleReports(CommonUtility.getWeekInDate(yesterday), ReportType.WEEKLY);
        log.info("[Report Generation][End  ] Ended monthly report generation for week that includes day {}.", dayOfThisWeek);
    }

    public void generateMonthlyReport(String dayOfThisMonth) {

        log.info("[Report Generation][Start] Started monthly report generation for month previous than day {}.", dayOfThisMonth);
        String yesterday = CommonUtility.getYesterday(dayOfThisMonth);
        mergeMultipleReports(CommonUtility.getMonthInDate(yesterday), ReportType.MONTHLY);
        log.info("[Report Generation][End  ] Ended monthly report generation for month that includes day {}.", dayOfThisMonth);
    }

    public void mergeMultipleReports(List<String> days, ReportType type) {

        ReportEntity mergedReportEntity = new ReportEntity();
        String dateRange = days.get(0) + "_" + days.get(days.size() - 1);
        mergedReportEntity.setId(dateRange + "_" + type.name().toLowerCase());
        mergedReportEntity.setDate(dateRange);

        for (String day : days) {
            String reportId = String.format("%s_%s", day, ReportType.DAILY.name().toLowerCase());
            Optional<ReportEntity> entityOpt = reportRepository.findById(reportId, new PartitionKey(day));
            entityOpt.ifPresent(mergedReportEntity::merge);
        }

        reportRepository.save(mergedReportEntity);
    }
}
