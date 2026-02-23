package dto;

import lombok.Data;
// @Data es una anotación de Lombok
// Genera automáticamente:
// - Getters
// - Setters
// - toString()
// - equals()
// - hashCode()
@Data
public class AuthResponseDto {

    // Token principal (normalmente JWT)
    // Se usa para autenticar cada petición al backend
    String token;

    // Refresh token
    // Se usa para generar un nuevo token cuando el principal expira
    String refreshToken;

    Boolean success;
}