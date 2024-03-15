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
    private static final String ID_DOMINIO = "id_dominio";
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
    private static final String PAYLOAD_CONTAINER_NAME = "payload_container_name";
    private static final String PAYLOAD_STORAGE_ACCOUNT = "payload_storage_account";

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
        PAYLOAD_LENGTH,
        PAYLOAD_CONTAINER_NAME,
        PAYLOAD_STORAGE_ACCOUNT);

    private TableServiceClient tableServiceClient = null;
    private BlobServiceClient blobServiceClient = null;


    @PostConstruct
    private void init() {
        this.tableServiceClient = new TableServiceClientBuilder().connectionString(reTableStorageConnString).buildClient();
        this.tableServiceClient.createTableIfNotExists(reTableStorageTableName);

        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(reBlobStorageConnString).buildClient();
    }

    public List<ReEventDto> findByIuv(LocalDate datefrom, LocalDate dateTo, String creditorInstitution, String noticeNumber){
        return runQuery(
                String.format("PartitionKey ge '%s' and PartitionKey le '%s' and "+ID_DOMINIO+" eq '%s' and "+NOTICE_NUMBER+" eq '%s'",
                        format(datefrom),
                        format(dateTo),
                        creditorInstitution,
                        noticeNumber)
        );
    }

    public List<ReEventDto> findByNav(LocalDate datefrom, LocalDate dateTo, String creditorInstitution, String iuv){
        return runQuery(
                String.format("PartitionKey ge '%s' and PartitionKey le '%s' and "+ID_DOMINIO+" eq '%s' and "+IUV+" eq '%s'",
                        format(datefrom),
                        format(dateTo),
                        creditorInstitution,
                        iuv)
        );
    }

    public String fetchPayload(String payloadRefId){
        BinaryData binaryData = this.blobServiceClient.getBlobContainerClient(reBlobStorageBlobName)
                .getBlobClient(payloadRefId)
                .downloadContent();
        byte[] byteArray = binaryData.toBytes();
        return new String(Base64.getEncoder().encode(byteArray), StandardCharsets.UTF_8);
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
    private String getString(Object o) {
        if (o == null) return null;
        return (String) o;
    }
    private LocalDateTime getLocalDateTime(Object o) {
        if (o == null) return null;
        return (LocalDateTime) o;
    }
    private Boolean getBoolean(Object o) {
        if (o == null) return null;
        return (Boolean) o;
    }
    private Integer getInteger(Object o) {
        if (o == null) return null;
        return (Integer) o;
    }

    private ReEventDto tableEntityToEventEntity(TableEntity e) {
        ReEventDto ee = new ReEventDto();
        ee.setInsertedTimestamp(getLocalDateTime(e.getProperty(INSERTED_TIMESTAMP)));
        ee.setComponente(getString(e.getProperty(COMPONENTE)));
        ee.setCategoriaEvento(getString(e.getProperty(CACTEGORIA_EVENTO)));
        ee.setSottoTipoEvento(getString(e.getProperty(SOTTO_TIPO_EVENTO)));
        ee.setIdDominio(getString(e.getProperty(ID_DOMINIO)));
        ee.setIuv(getString(e.getProperty(IUV)));
        ee.setCcp(getString(e.getProperty(CCP)));
        ee.setPsp(getString(e.getProperty(PSP)));
        ee.setTipoVersamento(getString(e.getProperty(TIPO_VERSAMENTO)));
        ee.setTipoEvento(getString(e.getProperty(TIPO_EVENTO)));
        ee.setFruitore(getString(e.getProperty(FRUITORE)));
        ee.setErogatore(getString(e.getProperty(EROGATORE)));
        ee.setStazione(getString(e.getProperty(STAZIONE)));
        ee.setCanale(getString(e.getProperty(CANALE)));
        ee.setParametriSpecificiInterfaccia(getString(e.getProperty(PARAMETRI_SPECIFICI_INTERFACCIA)));
        ee.setEsito(getString(e.getProperty(ESITO)));
        ee.setSessionId(getString(e.getProperty(SESSION_ID)));
        ee.setStatus(getString(e.getProperty(STATUS)));
        ee.setInfo(getString(e.getProperty(INFO)));
        ee.setBusinessProcess(getString(e.getProperty(BUSINESS_PROCESS)));
        ee.setFruitoreDescr(getString(e.getProperty(FRUITORE_DESCR)));
        ee.setErogatoreDescr(getString(e.getProperty(EROGATORE_DESCR)));
        ee.setPspDescr(getString(e.getProperty(PSP_DESCR)));
        ee.setNoticeNumber(getString(e.getProperty(NOTICE_NUMBER)));
        ee.setCreditorReferenceId(getString(e.getProperty(CREDITOR_REFERENCE_ID)));
        ee.setPaymentToken(getString(e.getProperty(PAYMENT_TOKEN)));
        ee.setSessionIdOriginal(getString(e.getProperty(SESSION_ID_ORIGINAL)));
        ee.setStandIn(getBoolean(e.getProperty(STAND_IN)));
        ee.setPayloadRefId(getString(e.getProperty(PAYLOAD_REF_ID)));
        ee.setPayloadLength(getInteger(e.getProperty(PAYLOAD_LENGTH)));
        ee.setPayloadContainerName(getString(e.getProperty(PAYLOAD_CONTAINER_NAME)));
        ee.setPayloadStorageAccount(getString(e.getProperty(PAYLOAD_STORAGE_ACCOUNT)));
        return ee;
    }

}
