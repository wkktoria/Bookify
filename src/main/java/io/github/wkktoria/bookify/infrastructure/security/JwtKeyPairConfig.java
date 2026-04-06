package io.github.wkktoria.bookify.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
class JwtKeyPairConfig {

    @Bean
    KeyPair keyPair() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        saveToFile("public_key.pem", keyPair.getPublic().getEncoded(), true);
        saveToFile("private_key.pem", keyPair.getPrivate().getEncoded(), false);
        return keyPair;
    }

    private void saveToFile(final String filename, final byte[] keyBytes, final boolean isPublicKey) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Paths
                .get("src", "main", "resources", filename).toString())) {
            fileOutputStream.write(getPemEncoded(keyBytes, isPublicKey));
        }
    }

    private byte[] getPemEncoded(final byte[] keyBytes, final boolean isPublicKey) {
        String pemHeader = isPublicKey ? "-----BEGIN PUBLIC KEY-----\n" : "-----BEGIN PRIVATE KEY-----\n";
        String pemFooter = isPublicKey ? "\n-----END PUBLIC KEY-----\n" : "\n-----END PRIVATE KEY-----\n";
        String pemEncoded = pemHeader + Base64
                .getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyBytes) + pemFooter;
        return pemEncoded.getBytes();
    }

}
