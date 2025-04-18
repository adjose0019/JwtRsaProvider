
# JwtRsaProvider

Proyecto que expone un proveedor de tokens OAuth2 utilizando firma RSA (`RS256`) sobre JWT, preparado para ser desplegado como una aplicación `WAR` en un servidor externo (Tomcat, JBoss, WebLogic).

## Descripción

Este servicio permite a clientes autenticados mediante HTTP Basic obtener un token JWT firmado digitalmente usando una clave privada almacenada en un archivo JKS.

El JWT puede ser utilizado para autenticaciones posteriores en APIs o microservicios seguros.

## Tecnologías y Librerías

- Java 17
- Spring Boot 3.4.4
- Spring Security 6.2
- JJWT 0.12.6 (Json Web Token)
- Springdoc OpenAPI 2.5.0 (Swagger 3)
- BouncyCastle (para soporte de llaves criptográficas)
- Empaquetado como WAR

## Estructura del proyecto

```
src/main/java/com/dizan/jwtrsaprovider/
├── config/        # Configuración de carga del JKS, Swagger y Spring Security
├── controller/    # Controlador REST de OAuth
├── dto/           # DTOs para respuestas de token
├── exceptions/    # Excepciones personalizadas
├── service/       # Servicio para generación de JWT
src/main/resources/
├── application.properties
├── application-dev.properties
├── application-qa.properties
├── application-prod.properties
├── jks/
    ├── oauthsppi.jks
    └── oauthsppi.pem
```

## Seguridad

- Autenticación HTTP Basic para solicitar el token.
- El `clientId` y `clientSecret` están configurados por perfil de ambiente (`dev`, `qa`, `prod`).
- Spring Security maneja la autenticación automáticamente.
- El token JWT es firmado con una clave privada RSA extraída del `oauthsppi.jks`.

## Endpoints disponibles

| Método | Endpoint             | Descripción                                   |
|:-------|:---------------------|:---------------------------------------------|
| `POST` | `/oauth/token`        | Genera un token JWT tras autenticación básica. |
| `GET`  | `/oauth/public-key`   | Retorna el certificado público en formato PEM. |
| `GET`  | `/swagger-ui.html`    | Acceso a la documentación Swagger UI.         |

## Ejemplo de solicitud para obtener el token

```bash
  #Windows CMD
curl -X POST "http://localhost:8081/oauth/token?grant_type=client_credentials" ^
  -u "mi-cliente:secreto123"
```

```bash
   #Windows PowerShell
  curl -Method Post "http://localhost:8081/oauth/token?grant_type=client_credentials" `
  -Headers @{ Authorization = "Basic $([Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes('mi-cliente:secreto123')))" }
```

```bash
  #Linux bash
  curl -X POST "http://localhost:8081/oauth/token?grant_type=client_credentials" \
  -u "mi-cliente:secreto123"
```

Respuesta esperada:

```json
{
  "tokenType": "Bearer",
  "accessToken": "<token firmado>",
  "expiresIn": 3600
}
```

## Perfiles de ambiente

El proyecto soporta 3 perfiles:

- `dev`
- `qa`
- `prod`

El perfil activo se puede configurar en:

```properties
spring.profiles.active=prod
```

O bien pasando la variable en el arranque:

```bash
java -jar jwtrsaprovider-1.0.0.war --spring.profiles.active=qa
```

## Compilación y empaquetado

Para generar el `.war`:

```bash
mvn clean install -Pprod
```

El archivo `JwtRsaProvider-1.0.0` quedará en `target/`.

## Documentación Swagger

Disponible en:  
`http://localhost:8080/swagger-ui.html`

## Autor

- **Jose Antonio Diaz**
- GitHub: [adjose0019](https://github.com/adjose0019)
