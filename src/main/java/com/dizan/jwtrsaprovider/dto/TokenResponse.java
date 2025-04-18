package com.dizan.jwtrsaprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa la respuesta del endpoint de generación de tokens.
 * <p>
 * Contiene los datos básicos del token JWT emitido:
 * <ul>
 *     <li>El tipo de token (por ejemplo, "Bearer").</li>
 *     <li>El token de acceso (access token) generado.</li>
 *     <li>El tiempo de expiración del token en segundos.</li>
 * </ul>
 * Esta clase es utilizada para estructurar la respuesta enviada al cliente tras una autenticación exitosa.
 *
 * @author Jose Antonio Diaz
 * GitHub adjose0019
 */
@Data
@AllArgsConstructor
public class TokenResponse {

    /**
     * Tipo de token, típicamente "Bearer".
     */
    private String tokenType;

    /**
     * Token de acceso (JWT) generado para el cliente autenticado.
     */
    private String accessToken;

    /**
     * Tiempo de expiración del token en segundos.
     */
    private int expiresIn;

    /**
     * Constructor vacío requerido para la deserialización automática (por ejemplo, al usar frameworks como Jackson).
     */
    public TokenResponse() {
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
