package pe.gob.congreso.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlfrescoCredentialDTO {
    private String server;
    private String spaceStore;
    private String user;
    private String site;
    private String password;
}
