package it.gov.pagopa.wispconverter.technicalsupport.service;

import com.azure.core.util.BinaryData;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReService {

    @Value("${re-table-storage.connection-string}")
    private String reTableStorageConnString;

    @Value("${re-table-storage.table-name}")
    private String reTableStorageTableName;

    @Value("${re-blob-storage.connection-string}")
    private String reBlobStorageConnString;

    @Value("${re-blob-storage.blob-name}")
    private String reBlobStorageBlobName;

    private static final String INSERTED_TIMESTAMP = "inserted_timestamp";
    private static final String COMPONENTE = "componente";
    private static final String CACTEGORIA_EVENTO = "categoria_evento";
    private static final String SOTTO_TIPO_EVENTO = "sotto_tipo_evento";
    private static final String ID_DOMINIO = "id_dominio"; //FIXME
    private static final String IUV = "iuv";
    private static final String CCP = "ccp";
    private static final String PSP = "psp";
    private static final String TIPO_VERSAMENTO = "tipo_versamento";
    private static final String TIPO_EVENTO = "tipo_evento";
    private static final String FRUITORE = "fruitore";
    private static final String EROGATORE = "erogatore";
    private static final String STAZIONE = "stazione";
    private static final String CANALE = "canale";
    private static final String PARAMETRI_SPECIFICI_INTERFACCIA = "parametri_specifici_interfaccia";
    private static final String ESITO = "esito";
    private static final String SESSION_ID = "session_id";
    private static final String STATUS = "status";
    private static final String INFO = "info";
    private static final String BUSINESS_PROCESS = "business_process";
    private static final String FRUITORE_DESCR = "fruitore_descr";
    private static final String EROGATORE_DESCR = "erogatore_descr";
    private static final String PSP_DESCR = "psp_descr";
    private static final String NOTICE_NUMBER = "notice_number";
    private static final String CREDITOR_REFERENCE_ID = "creditor_reference_id";
    private static final String PAYMENT_TOKEN = "payment_token";
    private static final String SESSION_ID_ORIGINAL = "session_id_original";
    private static final String STAND_IN = "stand_in";
    private static final String PAYLOAD_REF_ID = "payload_ref_id";
    private static final String PAYLOAD_LENGTH = "payload_length";

    private List<String> propertiesToSelect = Arrays.asList(
        INSERTED_TIMESTAMP,
        COMPONENTE,
        CACTEGORIA_EVENTO,
        SOTTO_TIPO_EVENTO,
        ID_DOMINIO,
        IUV,
        CCP,
        PSP,
        TIPO_VERSAMENTO,
        TIPO_EVENTO,
        FRUITORE,
        EROGATORE,
        STAZIONE,
        CANALE,
        PARAMETRI_SPECIFICI_INTERFACCIA,
        ESITO,
        SESSION_ID,
        STATUS,
        INFO,
        BUSINESS_PROCESS,
        FRUITORE_DESCR,
        EROGATORE_DESCR,
        PSP_DESCR,
        NOTICE_NUMBER,
        CREDITOR_REFERENCE_ID,
        PAYMENT_TOKEN,
        SESSION_ID_ORIGINAL,
        STAND_IN,
        PAYLOAD_REF_ID,
        PAYLOAD_LENGTH);

    private TableServiceClient tableServiceClient = null;
    private BlobServiceClient blobServiceClient = null;


    @PostConstruct
    private void init() {
        this.tableServiceClient = new TableServiceClientBuilder().connectionString(reTableStorageConnString).buildClient();
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(reBlobStorageConnString).buildClient();
    }

    public List<ReEventDto> findByNoticeNumber(LocalDate datefrom, LocalDate dateTo, String organization, String noticeNumber){
        return runQuery(
                String.format("PartitionKey ge '%s' and PartitionKey le '%s' and %s eq '%s' and %s eq '%s'",
                        format(datefrom),
                        format(dateTo),
                        ID_DOMINIO, organization,
                        NOTICE_NUMBER, noticeNumber)
        );
    }

    public List<ReEventDto> findByIuv(LocalDate datefrom, LocalDate dateTo, String organization, String iuv){
        return runQuery(
                String.format("PartitionKey ge '%s' and PartitionKey le '%s' and %s eq '%s' and %s eq '%s'",
                        format(datefrom),
                        format(dateTo),
                        ID_DOMINIO, organization,
                        IUV, iuv)
        );
    }

    public List<ReEventDto> findBySessionId(LocalDate datefrom, LocalDate dateTo, String sessionId){
        return runQuery(
                String.format("PartitionKey ge '%s' and PartitionKey le '%s' and %s eq '%s'",
                        format(datefrom),
                        format(dateTo),
                        SESSION_ID, sessionId)
        );
    }

    public PayloadDto fetchPayload(String payloadRefId){
        BinaryData binaryData = this.blobServiceClient.getBlobContainerClient(reBlobStorageBlobName)
                .getBlobClient(payloadRefId)
                .downloadContent();
        byte[] byteArray = binaryData.toBytes();
        Long length = binaryData.getLength();
        String gZipPayload = new String(Base64.getEncoder().encode(byteArray), StandardCharsets.UTF_8);
        return PayloadDto.builder()
                .payloadRefId(payloadRefId)
                .payloadLength(length)
                .payload(gZipPayload)
                .build();
    }

    private List<ReEventDto> runQuery(String filter){
        ListEntitiesOptions options = new ListEntitiesOptions()
                .setFilter(filter)
                .setSelect(propertiesToSelect);
        return this.tableServiceClient.getTableClient(reTableStorageTableName)
                .listEntities(options, null, null)
                .stream()
                .map(this::tableEntityToEventEntity)
                .collect(Collectors.toList());
    }

    private String format(LocalDate d) {
        return d.format(DateTimeFormatter.ISO_DATE);
    }

    private <T> T convert(Object o, Class<T> clazz) {
        if (o == null) return null;
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }

    private ReEventDto tableEntityToEventEntity(TableEntity e) {
        return ReEventDto.builder()
                .insertedTimestamp(convert(e.getProperty(INSERTED_TIMESTAMP), LocalDateTime.class))
                .componente(convert(e.getProperty(COMPONENTE), String.class))
                .categoriaEvento(convert(e.getProperty(CACTEGORIA_EVENTO), String.class))
                .sottoTipoEvento(convert(e.getProperty(SOTTO_TIPO_EVENTO), String.class))
                .idDominio(convert(e.getProperty(ID_DOMINIO), String.class))
                .iuv(convert(e.getProperty(IUV), String.class))
                .ccp(convert(e.getProperty(CCP), String.class))
                .psp(convert(e.getProperty(PSP), String.class))
                .tipoVersamento(convert(e.getProperty(TIPO_VERSAMENTO), String.class))
                .tipoEvento(convert(e.getProperty(TIPO_EVENTO), String.class))
                .fruitore(convert(e.getProperty(FRUITORE), String.class))
                .erogatore(convert(e.getProperty(EROGATORE), String.class))
                .stazione(convert(e.getProperty(STAZIONE), String.class))
                .canale(convert(e.getProperty(CANALE), String.class))
                .parametriSpecificiInterfaccia(convert(e.getProperty(PARAMETRI_SPECIFICI_INTERFACCIA), String.class))
                .esito(convert(e.getProperty(ESITO), String.class))
                .sessionId(convert(e.getProperty(SESSION_ID), String.class))
                .status(convert(e.getProperty(STATUS), String.class))
                .info(convert(e.getProperty(INFO), String.class))
                .businessProcess(convert(e.getProperty(BUSINESS_PROCESS), String.class))
                .fruitoreDescr(convert(e.getProperty(FRUITORE_DESCR), String.class))
                .erogatoreDescr(convert(e.getProperty(EROGATORE_DESCR), String.class))
                .pspDescr(convert(e.getProperty(PSP_DESCR), String.class))
                .noticeNumber(convert(e.getProperty(NOTICE_NUMBER), String.class))
                .creditorReferenceId(convert(e.getProperty(CREDITOR_REFERENCE_ID), String.class))
                .paymentToken(convert(e.getProperty(PAYMENT_TOKEN), String.class))
                .sessionIdOriginal(convert(e.getProperty(SESSION_ID_ORIGINAL), String.class))
                .standIn(convert(e.getProperty(STAND_IN), Boolean.class))
                .payloadRefId(convert(e.getProperty(PAYLOAD_REF_ID), String.class))
                .payloadLength(convert(e.getProperty(PAYLOAD_LENGTH), Integer.class))
                .build();
    }

}
