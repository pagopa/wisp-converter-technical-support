package it.gov.pagopa.wispconverter.technicalsupport.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReEvent {
  @Schema(example = "2024-03-18T09:04:27.42017Z", description = "Data di inserimento evento")
  private LocalDateTime insertedTimestamp;
  @Schema(example = "FESP", description = "Componente che identifica chi ha scritto l'evento. Esempio: FESP")
  private String componente;
  @Schema(example = "INTERFACCIA", description = "Componente che identifica se è un evento ai morsetti, ovvero di interfaccia, o interno, come cambio stato. Esempio: INTERFACCIA, INTERNO")
  private String categoriaEvento;
  @Schema(example = "RESP", description = "Componente che identifica il tipo di morsetto in base alla categoriaEvento. Esempio: REQ, RESP, INTERN")
  private String sottoTipoEvento;
  @Schema(example = "12345678900", description = "Id dell'EC")
  private String idDominio;
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
  @Schema(example = "11111111111_05", description = "Chi utilizza la primitiva")
  private String fruitore;
  @Schema(example = "NodoDeiPagamentiSPC", description = "Chi espone la primitiva")
  private String erogatore;
  @Schema(example = "11111111111_05", description = "Identificativo della stazione che eroga i servizi per la PA")
  private String stazione;
  @Schema(example = "60000000001_01", description = "Identificativo del canale che eroga i servizi per il PSP")
  private String canale;
  @Schema(example = "INVIATA", description = "Esito dell'operazione eseguita. Esempio: INVIATA, INVIATA_KO, RICEVUTA, RICEVUTA_KO, CAMBIO_STATO")
  private String esito;
  @Schema(example = "fcfcbe1e-987a-4914-8fd2-d1e7b67762bf", description = "Identificativo dell'operazione eseguita")
  private String sessionId;
  @Schema(example = "RPT_RICEVUTA_NODO", description = "Stato dell'RPT o del Carrello se la categoriaEvento è INTERN, altrienti è null")
  private String status;
  @Schema(example = "https://api.platform.pagopa.it/nodo/nodo-per-psp/v1", description = "Informazioni generiche, es URI di chi ha chiamato o di chi sta chiamando")
  private String info;
  @Schema(example = "?standIn", description = "Informazioni aggiuntive di chi ha chiamato o di chi sta chiamando")
  private String parametriSpecificiInterfaccia;
  @Schema(example = "nodoInviaRPT", description = "Campo che identifica il processo di business")
  private String businessProcess;
  @Schema(example = "11111111111_05", description = "Descrizione di chi utilizza la primitiva")
  private String fruitoreDescr;
  @Schema(example = "NodoDeiPagamentiSPC", description = "Descrizione di chi espone la primitiva")
  private String erogatoreDescr;
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
  @Schema(example = "2024-03-18_-230539074458600920", description = "Nome del payload")
  private String payloadRefId;
  @Schema(example = "347", description = "Lunghezza del payload")
  private Integer payloadLength;
}
