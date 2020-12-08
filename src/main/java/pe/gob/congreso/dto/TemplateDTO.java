package pe.gob.congreso.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TemplateDTO {
    private String registroUnico;
    private String numeroDocumento;
    private List<DerivaDTO> dirigidos;
    private String conCopia;
    private DerivaDTO origen;
    private String asunto;
    private String referencia;
    private String fechaDocumento;
    private String nombreArchivo;
    private String nombreAnio;
    private String uuid;
    private String templateUuid;
    private boolean exist;

    private AlfrescoCredentialDTO alfrescoCredentials;
}
