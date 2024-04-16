package it.gov.pagopa.wispconverter.technicalsupport.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.wispconverter.technicalsupport.service.model.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReEvent {
  //// START KEY
  @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Id")
  private String id;
  //// END KEY

  //// START LOGICAL REF
  @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Request Id")
  private String requestId;           //gruppo, tutte le chiamate fatte con lo stesso X-Request-ID
  @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Identificativo dell'operazione eseguita")
  private String operationId;         //id operation associato a un requestId
  @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Identificativo dell'operazione client eseguita")
  private String clientOperationId;   //id client operation associato a un operationId
  @Schema(example = "WISP_CONVERTER", description = "Identifica chi ha scritto l'evento. Esempio: WISP_CONVERTER")
  private ComponenteEnum componente;  //componente che scrive l'evento
  @Schema(example = "2024-03-18T09:04:27.42017Z", description = "Data di inserimento evento")
  private Instant insertedTimestamp;  //ora di inserimento evento
  //// END LOGICAL REF

  //// START FIELD FOR INTERFACE AND INTERN CHANGE
  @Schema(example = "INTERFACCIA", description = "Identifica se è un evento ai morsetti, ovvero di interfaccia, o interno, come cambio stato. Esempio: INTERFACCIA, INTERNO")
  private CategoriaEventoEnum categoriaEvento;
  @Schema(example = "RESP", description = "Identifica il tipo di morsetto in base alla categoriaEvento. Esempio: REQ, RESP, INTERN")
  private SottoTipoEventoEnum sottoTipoEvento;
  //// END FIELD FOR INTERFACE AND INTERN CHANGE

  //// START FIELD FOR INTERFACE
  @Schema(example = "SERVER", description = "Identifica il tipo di chiamata. Esempio: SERVER, CLIENT")
  private CallTypeEnum callType;

  @Schema(example = "11111111111_05", description = "Chi utilizza la primitiva")
  private String fruitore;
  @Schema(example = "11111111111_05", description = "Descrizione di chi utilizza la primitiva")
  private String fruitoreDescr;
  @Schema(example = "NodoDeiPagamentiSPC", description = "Chi espone la primitiva")
  private String erogatore;
  @Schema(example = "NodoDeiPagamentiSPC", description = "Descrizione di chi espone la primitiva")
  private String erogatoreDescr;

  @Schema(example = "INVIATA", description = "Esito dell'operazione eseguita. Esempio: INVIATA, INVIATA_KO, RICEVUTA, RICEVUTA_KO, NO_RICEVUTA, CAMBIO_STATO")
  private EsitoEnum esito;

  @Schema(example = "POST", description = "HTTP method")
  private String httpMethod;
  @Schema(example = "https://api.platform.pagopa.it/nodo/nodo-per-psp/v1", description = "HTTP URI")
  private String httpUri;
  @Schema(example = "Accept-Encoding :[\"gzip, deflate, br\"]", description = "HTTP headers")
  private String httpHeaders;
  @Schema(example = "127.0.0.1", description = "HTTP remote address")
  private String httpCallRemoteAddress;

  @Schema(example = "200", description = "HTTP status")
  private Integer httpStatusCode;
  @Schema(example = "456", description = "HTTP execution time")
  private Long executionTimeMs;

  @Schema(example = "abadasdbasbdasd", description = "HTTP payload compress GZIP")
  private String compressedPayload;       //zip+Base64
  @Schema(example = "454", description = "HTTP payload compress length")
  private Integer compressedPayloadLength;

  @Schema(example = "nodoInviaRPT", description = "Campo che identifica il processo di business")
  private String businessProcess;

  @Schema(example = "Success", description = "Stato complessivo. Success|Failed")
  private String operationStatus;       //dettaglio response in uscita
  @Schema(example = "Generic parsing error", description = "Title se stato Failed")
  private String operationErrorTitle;   //dettaglio response in uscita
  @Schema(example = "Error while parsing payload. {0}", description = "Detail se stato Failed")
  private String operationErrorDetail;  //dettaglio response in uscita
  @Schema(example = "WIC-1000", description = "Error code se stato Failed")
  private String operationErrorCode;    //dettaglio response in uscita
  //// END FIELD FOR INTERFACE

  //// START FIELD FOR INTERN CHANGE
  @Schema(example = "12345678900", description = "Id dell'EC")
  private String organizationId;
  @Schema(example = "982273480765907", description = "Identificativo univoco versamento")
  private String iuv;
  @Schema(example = "TEST_4178733642", description = "Codice contesto pagmaneto")
  private String ccp;
  @Schema(example = "60000000001", description = "Id del prestatore servizi di pagamento")
  private String psp;
  @Schema(example = "PO", description = "Tipo di pagmaneto. Esempio: BBT, BP, AD, CP, PO, OBEP, OTH, JIF, MYBK, PPAL")
  private String tipoVersamento;
  @Schema(example = "nodoInviaRPT", description = "Nome della primitiva")
  private String tipoEvento;
  @Schema(example = "11111111111_05", description = "Identificativo della stazione che eroga i servizi per la PA")
  private String stazione;
  @Schema(example = "60000000001_01", description = "Identificativo del canale che eroga i servizi per il PSP")
  private String canale;
  @Schema(example = "?standIn", description = "Informazioni aggiuntive di chi ha chiamato o di chi sta chiamando")
  private String parametriSpecificiInterfaccia;
  @Schema(example = "RPT_RICEVUTA_NODO", description = "Stato dell'RPT o del Carrello se la categoriaEvento è INTERN, altrienti è null")
  private String status;
  @Schema(example = "https://api.platform.pagopa.it/nodo/nodo-per-psp/v1", description = "Informazioni generiche, es URI di chi ha chiamato o di chi sta chiamando")
  private String info;
  @Schema(example = "PSP Test", description = "Descrizione del prestatore servizi di pagamento")
  private String pspDescr;
  @Schema(example = "3982273480765907", description = "Numero avviso")
  private String noticeNumber;
  @Schema(example = "982273480765907", description = "Identificativo univoco assegnato dall EC")
  private String creditorReferenceId;
  @Schema(example = "AABB123", description = "Numero univoco del pagamento")
  private String paymentToken;
  @Schema(example = "982273480765907", description = "Identificativo dell'operazione asincrona già eseguita")
  private String sessionIdOriginal;
  @Schema(example = "true", description = "Se il pagamneto è eseguito in Stand-In")
  private Boolean standIn;
  //// END FIELD FOR INTERN CHANGE

}
