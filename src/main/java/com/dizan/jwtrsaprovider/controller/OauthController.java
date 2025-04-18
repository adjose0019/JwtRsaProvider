package com.dizan.jwtrsaprovider.controller;

import com.dizan.jwtrsaprovider.exceptions.UnauthorizedException;
import com.dizan.jwtrsaprovider.service.JwtService;
import com.dizan.jwtrsaprovider.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Controlador REST que expone endpoints de autenticación utilizando el flujo OAuth2 Client Credentials.
 * <p>
 * Permite generar un token JWT para clientes autenticados correctamente mediante HTTP Basic Authentication
 * y también expone la clave pública usada para validar firmas de tokens.
 * </p>
 *
 * Endpoints principales:
 * <ul>
 *     <li>POST /oauth/token - Obtiene un JWT para el cliente autenticado.</li>
 *     <li>GET /oauth/public-key - Obtiene la clave pública en formato PEM.</li>
 * </ul>
 *
 * Requiere que el cliente se autentique enviando un header HTTP `Authorization: Basic Base64(clientId:clientSecret)`.
 * Las credenciales válidas se configuran en las propiedades:
 * <ul>
 *     <li>{@code security.oauth.client-id}</li>
 *     <li>{@code security.oauth.client-secret}</li>
 * </ul>
 *
 * @author Jose Antonio Diaz
 * GitHub adjose0019
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final JwtService jwtService;

    @Value("${security.oauth.client-id}")
    private String expectedClientId;

    @Value("${security.oauth.client-secret}")
    private String expectedClientSecret;

    /**
     * Constructor que inyecta el servicio JWT requerido para la generación de tokens.
     *
     * @param jwtService instancia de {@link JwtService}
     */
    public OauthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Endpoint para obtener un token JWT utilizando client credentials.
     *
     * @param grant_type tipo de flujo solicitado, debe ser igual a "client_credentials".
     * @return {@link TokenResponse} con los datos del token generado.
     * @throws BadRequestException si el flujo solicitado no es soportado.
     * @throws UnauthorizedException si las credenciales proporcionadas son inválidas.
     */
    @Operation(summary = "Obtiene un token JWT usando client credentials", description = "Requiere autenticación Basic")
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestParam String grant_type) throws BadRequestException {
        if (!"client_credentials".equals(grant_type)) {
            throw new BadRequestException("unsupported_grant_type");
        }

        // Obtener el clientId del contexto de seguridad
        String clientId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Generar el token JWT
        String token = jwtService.generateToken(clientId);

        // Retornar el token en el formato esperado
        return ResponseEntity.ok(new TokenResponse("Bearer", token, 3600));
    }


    /**
     * Endpoint que devuelve la clave pública en formato PEM.
     *
     * @return clave pública como String en formato PEM.
     */
    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok(jwtService.getPublicKey());
    }

    /**
     * Extrae las credenciales codificadas en Base64 del encabezado Authorization HTTP.
     *
     * @param authHeader encabezado HTTP Authorization en formato Basic Base64(clientId:clientSecret).
     * @return arreglo de Strings donde la posición 0 es el clientId y la posición 1 es el clientSecret.
     * @throws UnauthorizedException si el formato del header es inválido o está ausente.
     */
    private String[] extractCredentials(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new UnauthorizedException("missing_authorization_header");
        }

        String base64Credentials = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        String[] parts = decoded.split(":", 2);

        if (parts.length != 2) {
            throw new UnauthorizedException("invalid_credentials_format");
        }
        return parts;
    }
}
