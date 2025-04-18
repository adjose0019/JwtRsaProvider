package com.dizan.jwtrsaprovider.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que representa un error de autenticación no autorizada (HTTP 401 Unauthorized).
 * <p>
 * Esta excepción se lanza cuando un cliente no proporciona credenciales válidas o cuando el acceso es denegado
 * debido a problemas de autenticación.
 * </p>
 *
 * Cuando esta excepción es lanzada dentro de un controlador, Spring automáticamente devuelve:
 * <ul>
 *     <li>Código de estado HTTP: 401 Unauthorized</li>
 *     <li>Mensaje de error basado en el contenido del {@code message} proporcionado</li>
 * </ul>
 *
 * Esta excepción está anotada con {@link ResponseStatus}, por lo que no requiere un {@code @ControllerAdvice} adicional para mapear el estado HTTP.
 *
 * @author Jose Antonio Diaz
 * GitHub adjose0019
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructor que crea una nueva excepción {@code UnauthorizedException} con un mensaje personalizado.
     *
     * @param message el mensaje que describe el motivo de la excepción.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
