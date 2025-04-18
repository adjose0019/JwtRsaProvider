package com.dizan.jwtrsaprovider.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Componente responsable de cargar claves criptográficas desde un almacén de llaves (JKS).
 * <p>
 * Esta clase permite obtener:
 * <ul>
 *     <li>La clave privada utilizada para firmar tokens JWT.</li>
 *     <li>El certificado público en formato PEM para exposición pública.</li>
 * </ul>
 * El archivo JKS debe estar ubicado en el classpath y su ruta, contraseña, alias y contraseña de la clave
 * son configurables mediante propiedades externas.
 *
 * Propiedades necesarias:
 * <ul>
 *     <li>{@code jks.path} - Ruta al archivo JKS (ejemplo: classpath:keystore.jks)</li>
 *     <li>{@code jks.password} - Contraseña de acceso al keystore</li>
 *     <li>{@code jks.alias} - Alias de la clave dentro del keystore</li>
 *     <li>{@code jks.key-password} - Contraseña de la clave privada</li>
 * </ul>
 *
 * @author Jose Antonio Diaz
 * GitHub adjose0019
 */
@Component
@Slf4j
public class JksKeyLoader {

    @Value("${jks.path}")
    private String jksPath;

    @Value("${jks.password}")
    private String jksPassword;

    @Value("${jks.alias}")
    private String alias;

    @Value("${jks.key-password}")
    private String keyPassword;

    private KeyStore keystore;

    /**
     * Inicializa el keystore al momento de crear el componente, validando que se pueda cargar correctamente.
     */
    @PostConstruct
    public void init() {
        log.info("JKS cargado desde: {}", jksPath);
        try (InputStream is = getClass().getResourceAsStream(jksPath.replace("classpath:", "/"))) {
            keystore = KeyStore.getInstance("JKS");
            keystore.load(is, jksPassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando el JKS al cargar el componente JksKeyLoader", e);
        }
    }

    /**
     * Carga la clave privada desde el archivo JKS configurado.
     *
     * @return la {@link PrivateKey} leída del keystore.
     * @throws RuntimeException si ocurre algún error al acceder al JKS o extraer la clave.
     */
    public PrivateKey loadPrivateKey() {
        try (InputStream is = getClass().getResourceAsStream(jksPath.replace("classpath:", "/"))) {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(is, jksPassword.toCharArray());
            Key key = keystore.getKey(alias, keyPassword.toCharArray());

            if (key == null) {
                throw new IllegalStateException("La clave privada no se encontró para el alias: " + alias);
            }
            if (!(key instanceof PrivateKey)) {
                throw new IllegalStateException("La clave obtenida no es de tipo PrivateKey para el alias: " + alias);
            }
            return (PrivateKey) key;
        } catch (Exception e) {
            throw new RuntimeException("Error cargando la clave privada desde el JKS", e);
        }
    }


    /**
     * Carga el certificado público desde el archivo JKS y lo devuelve en formato PEM (Base64 con encabezados).
     *
     * @return el certificado público codificado como String en formato PEM.
     * @throws RuntimeException si ocurre algún error al acceder al JKS o leer el certificado.
     */
    public String loadPublicKeyPem() {
        try (InputStream is = getClass().getResourceAsStream(jksPath.replace("classpath:", "/"))) {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(is, jksPassword.toCharArray());
            Certificate cert = keystore.getCertificate(alias);
            return "-----BEGIN CERTIFICATE-----\n"
                    + java.util.Base64.getEncoder().encodeToString(cert.getEncoded())
                    + "\n-----END CERTIFICATE-----";
        } catch (Exception e) {
            throw new RuntimeException("Error cargando la clave pública desde el JKS", e);
        }
    }
}
