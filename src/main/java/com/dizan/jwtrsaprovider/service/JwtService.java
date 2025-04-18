package com.dizan.jwtrsaprovider.service;

import com.dizan.jwtrsaprovider.config.JksKeyLoader;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Servicio responsable de la generación de tokens JWT firmados y de la exposición de la clave pública en formato PEM.
 * <p>
 * Este servicio utiliza un almacén de llaves (JKS) para obtener:
 * <ul>
 *     <li>La clave privada con la cual firma los tokens.</li>
 *     <li>El certificado público en formato PEM para la validación de tokens.</li>
 * </ul>
 *
 * El token JWT generado incluye:
 * <ul>
 *     <li>El identificador del cliente como "subject".</li>
 *     <li>Un claim "roles" con los roles asignados.</li>
 *     <li>Fechas de emisión y expiración.</li>
 * </ul>
 *
 * Actualmente, el tiempo de expiración del token es fijo de 3600 segundos (1 hora).
 *
 * Este servicio depende de {@link JksKeyLoader} para el acceso a las llaves criptográficas.
 *
 * @author Jose Antonio Diaz
 * GitHub adjose0019
 */
@Service
public class JwtService {

    private final JksKeyLoader keyLoader;

    /**
     * Lista de roles asignados de forma fija a los tokens emitidos.
     */
    List<String> roles = List.of("admin");

    /**
     * Constructor que inyecta el cargador de claves JKS necesario para firmar tokens y obtener certificados.
     *
     * @param keyLoader instancia de {@link JksKeyLoader} para carga de llaves.
     */
    public JwtService(JksKeyLoader keyLoader) {
        this.keyLoader = keyLoader;
    }

    /**
     * Genera un token JWT firmado utilizando la clave privada configurada.
     *
     * @param clientId identificador del cliente que se usará como "subject" en el token.
     * @return el token JWT generado como String.
     */
    public String generateToken(String clientId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(clientId)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(keyLoader.loadPrivateKey())
                .compact();
    }

    /**
     * Obtiene el certificado público en formato PEM que puede ser usado para validar la firma de los tokens JWT.
     *
     * @return la clave pública codificada en formato PEM como String.
     */
    public String getPublicKey() {
        return keyLoader.loadPublicKeyPem();
    }
}
