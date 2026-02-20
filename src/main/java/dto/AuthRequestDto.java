package dto;

import lombok.Data;

/* @Data genera automaticamente
getters, setters, tostring(), equals, hashcode, constructor()
* */

@Data
public class AuthRequestDto {
    String user;

    String password;

    //se usa para generar un nuevo token cuando el prinicipal expira
    String refreshToken;
}
