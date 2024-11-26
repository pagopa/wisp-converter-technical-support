package it.gov.pagopa.wispconverter.technicalsupport.service;

import com.azure.cosmos.models.PartitionKey;
import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.ReEventDataExplorerMapper;
import it.gov.pagopa.wispconverter.technicalsupport.controller.mapper.ReEventMapper;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.EventCategory;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.ReEvent;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring.PendingReceipt;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring.PendingReceiptsFilterRequest;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.monitoring.ReceiptsStatusSnapshot;
import it.gov.pagopa.wispconverter.technicalsupport.controller.model.experimental.payment.*;
import it.gov.pagopa.wispconverter.technicalsupport.repository.RTRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventDataExplorerRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.ReEventExperimentalRepository;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.RTGroupedByStatusEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventDataExplorerEntity;
import it.gov.pagopa.wispconverter.technicalsupport.repository.model.ReEventEntity;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.PaymentUniqueID;
import it.gov.pagopa.wispconverter.technicalsupport.util.CommonUtility;
import it.gov.pagopa.wispconverter.technicalsupport.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnhancedFeaturesService {

    private final ReEventExperimentalRepository reEventRepository;

    private final ReEventDataExplorerRepository reEventDataExplorerRepository;

    private final RTRepository rtRepository;

    private final ReEventMapper reEventMapper;

    private final ReEventDataExplorerMapper reEventDataExplorerMapper;

    public List<ReEvent> findByNoticeNumber(LocalDate dateFromAsLocalDate,
                                            LocalDate dateToAsLocalDate,
                                            String organization,
                                            String noticeNumber) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByNoticeNumberAndDomainId(dateFrom, dateTo, organization, noticeNumber);

        return extractReEventsFromWispDismantling(dateFrom, dateTo, sessionIds, true);
    }

    public List<ReEvent> findByIuv(LocalDate dateFromAsLocalDate,
                                   LocalDate dateToAsLocalDate,
                                   String organization,
                                   String iuv) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByIuvAndDomainId(dateFrom, dateTo, organization, iuv);

        return extractReEventsFromWispDismantling(dateFrom, dateTo, sessionIds, true);
    }

    public List<ReEvent> findBySessionId(LocalDate dateFromAsLocalDate,
                                         LocalDate dateToAsLocalDate,
                                         String sessionId) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        return extractReEventsFromWispDismantling(dateFrom, dateTo, Set.of(sessionId), true);
    }

    /**
     * @param organization
     * @param iuv
     * @param filters
     * @return
     */
    public List<PaymentFlow> findByIuvEnhanced(String organization,
                                               String iuv,
                                               PaymentFlowsFilterRequest filters) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(filters.getLowerBoundDate());
        String dateTo = CommonUtility.partitionKeyFromInstant(filters.getUpperBoundDate());

        Set<String> sessionIds = reEventRepository.findSessionIdByIuvAndDomainId(dateFrom, dateTo, organization, iuv);

        List<PaymentFlow> paymentFlows = new LinkedList<>();
        for (String sessionId : sessionIds) {
            List<ReEvent> reEvents = extractReEventsFromBothStorage(dateFrom, dateTo, sessionId, filters.getShowDetails());
            paymentFlows.add(convertEventsToPaymentFlow(reEvents, sessionId, filters.getShowCompactForm(), filters.getShowPayloads()));
        }

        return paymentFlows;
    }

    /**
     * @param organization
     * @param noticeNumber
     * @param filters
     * @return
     */
    public List<PaymentFlow> findByNoticeNumberEnhanced(String organization,
                                                        String noticeNumber,
                                                        PaymentFlowsFilterRequest filters) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(filters.getLowerBoundDate());
        String dateTo = CommonUtility.partitionKeyFromInstant(filters.getUpperBoundDate());

        Set<String> sessionIds = reEventRepository.findSessionIdByNoticeNumberAndDomainId(dateFrom, dateTo, organization, noticeNumber);

        List<PaymentFlow> paymentFlows = new LinkedList<>();
        for (String sessionId : sessionIds) {
            List<ReEvent> reEvents = extractReEventsFromBothStorage(dateFrom, dateTo, sessionId, filters.getShowDetails());
            paymentFlows.add(convertEventsToPaymentFlow(reEvents, sessionId, filters.getShowCompactForm(), filters.getShowPayloads()));
        }

        return paymentFlows;
    }

    /**
     * @param sessionId
     * @param filters
     * @return
     */
    public List<PaymentFlow> findBySessionIdEnhanced(String sessionId,
                                                     PaymentFlowsFilterRequest filters) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(filters.getLowerBoundDate());
        String dateTo = CommonUtility.partitionKeyFromInstant(filters.getUpperBoundDate());

        List<ReEvent> reEvents = extractReEventsFromBothStorage(dateFrom, dateTo, sessionId, filters.getShowDetails());

        return List.of(convertEventsToPaymentFlow(reEvents, sessionId, filters.getShowCompactForm(), filters.getShowPayloads()));
    }

    /**
     * @param dateFromAsLocalDate
     * @param dateToAsLocalDate
     * @param organization
     * @param iuv
     * @return
     */
    public List<PaymentFlowStatus> getPaymentStatusFindByIuv(LocalDate dateFromAsLocalDate,
                                                             LocalDate dateToAsLocalDate,
                                                             String organization,
                                                             String iuv) {

        String dateFrom = CommonUtility.partitionKeyFromInstant(dateFromAsLocalDate);
        String dateTo = CommonUtility.partitionKeyFromInstant(dateToAsLocalDate);

        Set<String> sessionIds = reEventRepository.findSessionIdByIuvAndDomainId(dateFrom, dateTo, organization, iuv);

        List<PaymentFlowStatus> paymentFlows = new LinkedList<>();
        for (String sessionId : sessionIds) {

            List<ReEvent> events = extractReEventsFromBothStorage(dateFrom, dateTo, sessionId, true);
            List<PaymentUniqueID> paymentsUniqueIDs = events.stream()
                    .filter(event -> Constants.ACCEPTED_TRIGGER_PRIMITIVE.contains(event.getBusinessProcess()) && "SEMANTIC_CHECK_PASSED".equals(event.getStatus()))
                    .map(event -> new PaymentUniqueID(event.getDomainId(), event.getIuv(), event.getCcp()))
                    .toList();

            Set<String> steps = new LinkedHashSet<>();
            events.stream()
                    .map(ReEvent::getBusinessProcess)
                    .forEach(steps::add);

            for (PaymentUniqueID paymentsUniqueID : paymentsUniqueIDs) {

                String paymentDomainId = paymentsUniqueID.getDomainId();
                String paymentIuv = paymentsUniqueID.getCode();
                String paymentCcp = paymentsUniqueID.getCcp();
                if (paymentCcp == null || "n/a".equalsIgnoreCase(paymentCcp)) {
                    paymentCcp = "na";
                }

                String id = String.format("%s_%s_%s", paymentDomainId, paymentIuv, paymentCcp);
                Optional<RTEntity> receipt = rtRepository.findById(id, new PartitionKey(id));

                paymentFlows.add(PaymentFlowStatus.builder()
                        .paymentIdentifier(PaymentIdentifier.builder()
                                .domainId(paymentDomainId)
                                .iuv(paymentIuv)
                                .ccp(paymentCcp)
                                .build())
                        .paymentOutcome(receipt.isPresent() ? receipt.get().getReceiptType() : "NOT_FOUND")
                        .receiptSendingStatus(receipt.isPresent() ? receipt.get().getReceiptStatus() : "NOT_FOUND")
                        .paymentDetails(extractPaymentFlowDetail(events, paymentDomainId, paymentIuv))
                        .build());
            }

        }

        return paymentFlows;
    }

    /**
     * @param dateFromAsLocalDateTime
     * @param dateToAsLocalDateTime
     * @return
     */
    public List<ReceiptsStatusSnapshot> extractReceiptSnapshot(LocalDateTime dateFromAsLocalDateTime,
                                                               LocalDateTime dateToAsLocalDateTime) {

        String dateFrom = CommonUtility.timestampFromInstant(dateFromAsLocalDateTime.minusHours(1));
        String dateTo = CommonUtility.timestampFromInstant(dateToAsLocalDateTime.minusHours(1));

        Set<RTGroupedByStatusEntity> rtCountByStatus = rtRepository.findByTimestampGroupByStatus(dateFrom, dateTo);
        return rtCountByStatus.stream()
                .map(entity -> ReceiptsStatusSnapshot.builder()
                        .status(entity.getReceiptStatus())
                        .count(entity.getEventStatusCount())
                        .build())
                .toList();
    }


    public List<PendingReceipt> extractPendingReceipts(PendingReceiptsFilterRequest request) {

        List<PendingReceipt> pendingReceipts = new LinkedList<>();
        String dateTimeFrom = CommonUtility.timestampFromInstant(request.getLowerBoundDate().minusHours(1));
        String dateTimeTo = CommonUtility.timestampFromInstant(request.getUpperBoundDate().minusHours(1));

        String dateFrom = CommonUtility.partitionKeyFromInstant(request.getLowerBoundDate().toLocalDate());
        String dateTo = CommonUtility.partitionKeyFromInstant(request.getUpperBoundDate().toLocalDate());

        List<RTEntity> rtsInPendingStatus;

        Set<String> iuvs = request.getIuvs();
        if (iuvs == null || iuvs.isEmpty()) {
            rtsInPendingStatus = rtRepository.findAllInPendingStatus(dateTimeFrom, dateTimeTo, request.getCreditorInstitution());
        } else {
            rtsInPendingStatus = rtRepository.findInPendingStatus(dateTimeFrom, dateTimeTo, request.getCreditorInstitution(), iuvs);
        }

        for (RTEntity rtInPendingStatus : rtsInPendingStatus) {

            String iuv = rtInPendingStatus.getIuv();
            String domainId = rtInPendingStatus.getDomainId();

            List<ReEventDataExplorerEntity> events = reEventDataExplorerRepository.findSendRTV2Event(dateFrom, dateTo, iuv, domainId);

            if (events == null || events.isEmpty()) {

                pendingReceipts.add(PendingReceipt.builder()
                        .receiptToSend("KO")
                        .payment(PaymentIdentifier.builder()
                                .iuv(iuv)
                                .domainId(domainId)
                                .sessionId(rtInPendingStatus.getSessionId())
                                .build())
                        .build());

            } else {

                for (ReEventDataExplorerEntity event : events) {

                    pendingReceipts.add(PendingReceipt.builder()
                            .receiptContent(String.format("{\"content\":\"%s\"}", CommonUtility.decodeBase64(event.getPayload())))
                            .receiptToSend("OK")
                            .payment(PaymentIdentifier.builder()
                                    .iuv(iuv)
                                    .domainId(domainId)
                                    .sessionId(rtInPendingStatus.getSessionId())
                                    .build())
                            .build());
                }
            }


        }

        return pendingReceipts;
    }

    private PaymentFlowDetail extractPaymentFlowDetail(List<ReEvent> events, String domainId, String iuv) {

        PaymentFlowDetail paymentFlowDetail = new PaymentFlowDetail();

        LinkedHashMap<String, List<ReEvent>> eventsByBusinessProcess = events.stream()
                .collect(Collectors.groupingBy(ReEvent::getBusinessProcess, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<ReEvent>> entrySet : eventsByBusinessProcess.entrySet()) {

            String key = entrySet.getKey();
            List<ReEvent> eventsExtracted = entrySet.getValue();
            List<ReEvent> interfaceEvents = eventsExtracted.stream()
                    .filter(event -> EventCategory.INTERFACE.equals(event.getEventCategory()))
                    .toList();

            String outcome;
            switch (key) {
                case Constants.STEP_TRIGGER_PRIMITIVE_NODOINVIARPT, Constants.STEP_TRIGGER_PRIMITIVE_NODOINVIACARRELLORPT:
                    outcome = !interfaceEvents.isEmpty() ? interfaceEvents.get(0).getOutcome() : "NOT_FOUND";
                    paymentFlowDetail.setTriggerPrimitive(outcome != null ? outcome : "OK");
                    break;
                case Constants.STEP_REDIRECT:
                    outcome = !interfaceEvents.isEmpty() ? interfaceEvents.get(0).getOutcome() : null;
                    paymentFlowDetail.setRedirect(outcome);
                    break;
                case Constants.STEP_CHECKPOSITION:
                    String checkPositionResponsePayload = !interfaceEvents.isEmpty() ? interfaceEvents.get(0).getResponsePayload() : null;
                    outcome = checkPositionResponsePayload != null && checkPositionResponsePayload.contains("\"outcome\":\"OK\"") ? "OK" : "ERROR";
                    paymentFlowDetail.setCheckPosition(outcome);
                    break;
                case Constants.STEP_ACTIVATEPAYMENTNOTICE:
                    String activateResponsePayload = !interfaceEvents.isEmpty() ? interfaceEvents.get(0).getResponsePayload() : null;
                    outcome = activateResponsePayload != null && activateResponsePayload.contains("<outcome>OK</outcome>") ? "OK" : "ERROR";
                    paymentFlowDetail.setActivatePaymentNoticeV2(outcome);
                    break;
                case Constants.STEP_CLOSEPAYMENT:
                    String closePaymentResponsePayload = !interfaceEvents.isEmpty() ? interfaceEvents.get(0).getResponsePayload() : null;
                    outcome = closePaymentResponsePayload != null && closePaymentResponsePayload.contains("{\"outcome\":\"OK\"}") ? "OK" : "ERROR";
                    paymentFlowDetail.setClosePaymentV2(outcome);
                    break;
                case Constants.STEP_SENDPAYMENTOUTCOME:
                    String spoResponsePayload = !interfaceEvents.isEmpty() ? interfaceEvents.get(0).getResponsePayload() : null;
                    outcome = spoResponsePayload != null && spoResponsePayload.contains("<outcome>OK</outcome>") ? "OK" : "ERROR";
                    paymentFlowDetail.setSendPaymentOutcomeV2(outcome);
                    break;
                case Constants.STEP_RPTTIMEOUTTRIGGER:
                    paymentFlowDetail.setRptTimeout(!interfaceEvents.isEmpty() ? interfaceEvents.get(0).getOutcome() : null);
                    break;
                case Constants.STEP_ECOMMERCETIMEOUTTRIGGER:
                    paymentFlowDetail.setEcommerceTimeout(!interfaceEvents.isEmpty() ? interfaceEvents.get(0).getOutcome() : null);
                    break;
                case Constants.STEP_PAYMENTTOKENTIMEOUTTRIGGER:
                    paymentFlowDetail.setPaymentTokenTimeout(!interfaceEvents.isEmpty() ? interfaceEvents.get(0).getOutcome() : null);
                    break;
                case Constants.STEP_RECEIPTKO:
                    String koOperationId = eventsExtracted.stream()
                            .filter(event -> event.getDomainId().equals(domainId) && event.getIuv().equals(iuv))
                            .map(ReEvent::getOperationId)
                            .findFirst()
                            .orElse(null);
                    if (koOperationId != null) {
                        ReEvent koReEvent = interfaceEvents.stream()
                                .filter(event -> koOperationId.equals(event.getOperationId()))
                                .findFirst()
                                .orElse(null);
                        paymentFlowDetail.setSendReceiptKO(koReEvent != null ? koReEvent.getOutcome() : null);
                    }
                    break;
                case Constants.STEP_RECEIPTOK:
                    String okOperationId = eventsExtracted.stream()
                            .filter(event -> event.getRequestPayload() != null &&
                                    event.getRequestPayload().contains("<creditorReferenceId>" + iuv + "</creditorReferenceId>") &&
                                    event.getRequestPayload().contains("<fiscalCode>" + domainId + "</fiscalCode>"))
                            .map(ReEvent::getOperationId)
                            .findFirst()
                            .orElse(null);
                    if (okOperationId != null) {
                        ReEvent okReEvent = interfaceEvents.stream()
                                .filter(event -> okOperationId.equals(event.getOperationId()))
                                .findFirst()
                                .orElse(null);
                        paymentFlowDetail.setSendReceiptOk(okReEvent != null ? okReEvent.getOutcome() : null);
                    }
                    break;
                case Constants.STEP_RESEND_RECEIPT:
                    outcome = !interfaceEvents.isEmpty() ? interfaceEvents.get(interfaceEvents.size() - 1).getOutcome() : null;
                    paymentFlowDetail.setSendResendReceipt(outcome);
                    break;
                default:
                    break;
            }
        }

        return paymentFlowDetail;
    }


    /**
     * @param dateFrom
     * @param dateTo
     * @param sessionId
     * @param showDetails
     * @return
     */
    private List<ReEvent> extractReEventsFromBothStorage(String dateFrom, String dateTo, String sessionId, boolean showDetails) {

        // retrieve all events from D-WISP database
        List<ReEvent> reEvents = extractReEventsFromWispDismantling(dateFrom, dateTo, Set.of(sessionId), showDetails);

        // retrieve all events from Nodo's DataExplorer
        extractReEventsFromDataExplorer(dateFrom, dateTo, reEvents);

        // sorting events by persistence time
        reEvents.sort(Comparator.comparing(ReEvent::getInsertedTimestamp));

        // removing all events that are written before trigger primitives and were included in this read (i.e. checkPosition ones)
        boolean include = false;
        List<ReEvent> purgedReEvents = new LinkedList<>();
        for (ReEvent event : reEvents) {
            if (Constants.ACCEPTED_TRIGGER_PRIMITIVE.contains(event.getBusinessProcess())) {
                include = true;
            }
            if (include) {
                purgedReEvents.add(event);
            }
        }

        return purgedReEvents;
    }

    /**
     * @param dateFrom
     * @param dateTo
     * @param sessionIds
     * @param showDetails
     * @return
     */
    private List<ReEvent> extractReEventsFromWispDismantling(String dateFrom, String dateTo, Set<String> sessionIds, boolean showDetails) {

        List<ReEventEntity> reEventEntities = new LinkedList<>();

        // show all statuses, intern-related and interface-related
        if (showDetails) {
            sessionIds.stream()
                    .flatMap(sessionId -> reEventRepository.findAllStatuses(dateFrom, dateTo, sessionId).stream())
                    .forEach(reEventEntities::add);
        }

        // show only INTERFACE statuses
        else {
            sessionIds.stream()
                    .flatMap(sessionId -> reEventRepository.findOnlyMainStatuses(dateFrom, dateTo, sessionId).stream())
                    .forEach(reEventEntities::add);
        }

        // convert all entities to DTOs then sort them by inserted timestamp
        List<ReEvent> reEvents = reEventMapper.toDto(reEventEntities);
        reEvents.sort(Comparator.comparing(ReEvent::getInsertedTimestamp));
        return reEvents;
    }

    /**
     * @param dateFrom
     * @param dateTo
     * @param reEvents
     */
    private void extractReEventsFromDataExplorer(String dateFrom, String dateTo, List<ReEvent> reEvents) {

        // extract the distinct notice numbers from passed RE events, then search events related to them in DataExplorer
        List<ReEventDataExplorerEntity> reEventsFromDataExplorer = new LinkedList<>();
        reEvents.stream()
                .map(event -> event.getNoticeNumber() != null
                        ? new PaymentUniqueID(event.getDomainId(), event.getNoticeNumber(), event.getCcp())
                        : null)
                .collect(Collectors.toSet())
                .stream()
                .filter(Objects::nonNull)
                .flatMap(eventTriple -> reEventDataExplorerRepository.find(dateFrom, dateTo, eventTriple.getCode(), eventTriple.getDomainId(), eventTriple.getCcp()).stream())
                .forEach(reEventsFromDataExplorer::add);

        // sort the extracted RE events by timestamp
        reEventsFromDataExplorer.sort(Comparator.comparing(ReEventDataExplorerEntity::getInsertedTimestamp));

        // grouping the events by operation ID: this will permit to cluster events by entrypoint primitives
        Map<String, List<ReEventDataExplorerEntity>> eventsByOperationId = reEventsFromDataExplorer.stream()
                .collect(Collectors.groupingBy(ReEventDataExplorerEntity::getOperationId));

        // analyzing single cluster of events
        for (List<ReEventDataExplorerEntity> eventsBySingleOperationId : eventsByOperationId.values()) {

            // grouping the event by business process in order to merge REQ and RESP events
            Map<String, List<ReEventDataExplorerEntity>> eventsByStep = eventsBySingleOperationId.stream()
                    .collect(Collectors.groupingBy(ReEventDataExplorerEntity::getTipoEvento));

            for (Map.Entry<String, List<ReEventDataExplorerEntity>> entrySet : eventsByStep.entrySet()) {

                List<ReEventDataExplorerEntity> events = entrySet.getValue();
                int numberOfEvents = events.size();

                // if there are two or more events, they can be merged
                if (numberOfEvents >= 2) {
                    reEvents.add(reEventDataExplorerMapper.toReEvent(events.get(0), events.get(1)));
                }

                // if there are only one event, only the first is analyzed
                else if (numberOfEvents == 1) {
                    reEvents.add(reEventDataExplorerMapper.toReEvent(events.get(0), null));
                }
            }
        }
    }

    /**
     * @param reEvents
     * @param sessionId
     * @param showCompactForm
     * @param showPayloads
     * @return
     */
    private PaymentFlow convertEventsToPaymentFlow(List<ReEvent> reEvents, String sessionId, boolean showCompactForm, boolean showPayloads) {

        List<PaymentFlowStep> steps = new LinkedList<>();

        // grouping events by operation ID, in order to show them as separate cluster
        LinkedHashMap<String, List<ReEvent>> reEventByOperationId = reEvents.stream()
                .collect(Collectors.groupingBy(ReEvent::getOperationId, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<ReEvent>> entry : reEventByOperationId.entrySet()) {

            List<ReEvent> eventsGrouped = entry.getValue();

            // if the compact form of data are required, each of them will be converted removing the not needed fields
            if (!showPayloads) {
                eventsGrouped.forEach(event -> {
                    event.setRequestPayload(null);
                    event.setResponsePayload(null);
                });
            }
            if (showCompactForm) {
                eventsGrouped.forEach(reEventMapper::compact);
            }

            // then, add the extracted steps in the final response
            steps.add(PaymentFlowStep.builder()
                    .step(eventsGrouped.get(0).getBusinessProcess())
                    .startedAt(eventsGrouped.get(0).getInsertedTimestamp())
                    .events(eventsGrouped)
                    .build());
        }

        // finally, generate the whole payment flow
        return PaymentFlow.builder()
                .sessionId(sessionId)
                .startedAt(reEvents.get(0).getInsertedTimestamp())
                .stepsDetails(steps)
                .build();
    }
}
