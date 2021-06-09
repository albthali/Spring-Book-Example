package me.abdalrahman.albthali.books.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import me.abdalrahman.albthali.books.entity.JWTEntity;
import me.abdalrahman.albthali.books.repository.JWTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.*;

@Service
public class JWTUtils {
    private final String JWT_SECRET = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCqHLVvx8ETcsbpJnccArdFbqrmbEswGup72fxSP65NYW/bOpWMNWISQo8/emrRxy69p3mFKxH1sTcAOdiZOKOdwUaFthwPwdnoufDW1od0xGQw8eQKuijOQiHh2WMOj3Do7AAQM83iITO2l+k5fSS4nCG91KwwcvyxzWHVaexLfyrS6CXjgcol8kMJk5nBvHV4RlfpLvQzrtEVumUTHmabTVSpuBSn0HbUK48/xd2b/Qh3cvst0C1dxP3dk7p5mhGeA4NcjGDA/kqAR2lWpQAL7ROZoqi+wPJlQl/SbyS2TqZYNIJ7D6QAOsCYAQWrEs/BfemIhfCL0+134efSQS2HAgMBAAECggEAbr5728Z2Spv5JIW3GxQljd0LAzFTDYEoT/LMkhsE9TlOobEU9I84RlzwdsnSSrlaE3u439n3OjThcpM2ECHQWn4d1CSHRjbUDu8l/CbzTjAgbcC7zcP9lrF071wfYbY+MlSZgfLHwMQOr3tcCOIQiuKSEMwoQO5K1dG4NEBd5ZNSGQshVJNRVftfYmDykoFjM1nU7wLl2Y/9ZXBMCahtt//foqwUJG8CTm2+yaXcfMECeL44RQGTCSBrTH9EfLwKrzYVdrdnHAom/YE7JuVr8YWLVj4m7Z+46wc6vOFhVJR5x0MaddjckDos+B1DX3FbMafxsGZ0fWr4WMAZZVZuIQKBgQD+VBFrLBY+APsQD93geHH5vuVvnxlhglMAkIqO2ISVSoXhrldAhx5ZT7xdIlJO94to0ggirZCKScKDgFoZ7WOVZEt11ts2Bwmc0cu9AKUc/qLqlfWrjZ7rJUW3DNwkCANFA01oPRVbPHQB9AVmSm7Grv1Cc51e3ZqD/OkQPOjVtwKBgQCrOvBS+WURVfcc6HaT3bmY71IJfuGfstRlPtkTmwcfJFsWTI6fn2JA9US5jMs5zq64/6aXB/No6F4D1498DeSJnEe6+mviG+DGhkxW4sYLrgn+myF27Q/24HZbgAZZkxWlXSdd3vxsgfMH/2g2Pj/BdZuLLCodxM/4qN167NXmsQKBgQDGpvesFJWS/MUd+KbADJRKFRe1Jjp29YrLqBryFKEK01jbvIeeeWv1KtjgjkxoLecQiu2b2KJLwe1TNdAq7Eeaiup+MsKq4rhZa22ORZCyoxqxzUxFIat91HEt17ej1TZok3nE7+FckE20sNv+2CjKvChJ3XCQhtuIOOX6rvjW5QKBgENqijJxBMzlhsy37e/b7XX8S9V3QkgJNbzDGN6yPAST4NUPwVuxehLPPoa7m0wgRwSJi0KNm9VjH5jtpw2VvrQ7drZ0YEN+7GndeHMBCC6TEFZRJ8TWI2H1EESITHe6BFy+J4C7CbGDHHe6pIIu0hVOQ7Zw5gggdC2X9tOuKa2RAoGAGadMwhSsurNROzJynW6Sw2SNyvyL50lT7UDEGk9cYZZcY4rHCJrRUR/0mOLHWaiE2exoiCo2Qg8EvZ0UieI1HB4S7XGUDeKzvbOQ9Z4q8j7g/LJDx3qK7axqHpWUaMoshwwAOArtysgXTZonc2Vy6WRnPo9RnopeeWvgMTurVVg=";
    public final String publicKey;
    private final JwtParser parser;
    private final PrivateKey privateKey;
    @Autowired
    private JWTRepository jwtRepository;
    public JWTUtils() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        File file = new File("/etc/ssl/certs/private_key.der");
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);

        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kf.generatePrivate(spec);
        this.privateKey = rsaPrivateKey;

        //get the public key
        File file1 = new File("/etc/ssl/certs/public_key.der");
        FileInputStream fis1 = new FileInputStream(file1);
        DataInputStream dis1 = new DataInputStream(fis1);
        byte[] keyBytes1 = new byte[(int) file1.length()];
        dis1.readFully(keyBytes1);
        dis1.close();

        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(keyBytes1);
        KeyFactory kf1 = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) kf1.generatePublic(spec1);
        this.publicKey = "-----BEGIN RSA PRIVATE KEY-----\n" +Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()) + "\n-----END RSA PRIVATE KEY-----\n";
        this.parser = Jwts.parserBuilder().setSigningKey(this.privateKey).build();
    }
    public String generateSession(){
        return UUID.randomUUID().toString();
    }



    public String generateToken(UserDetails userDetails, Map<String,Object> claims, Expiration expiration){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration.getTime()))
                /*Idealy you would have a private key that you would */
                .signWith(this.privateKey,SignatureAlgorithm.RS256)
                .compact();

    }
    public JwtParser getParser(){
        return parser;
    }


    public boolean sessionExist(String session){
        return jwtRepository.findById(session).isPresent();
    }
    public void removeSession(String session){
        jwtRepository.deleteById(session);
    }

    public Claims getClaims(String jwt){
        return parser.parseClaimsJws(jwt).getBody();
    }
    public String getId(String jwt){
        return getClaims(jwt).getId();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        File file = new File("certs/private_key.der");
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);

        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kf.generatePrivate(spec);


        //get the public key
        File file1 = new File("certs/public_key.der");
        FileInputStream fis1 = new FileInputStream(file1);
        DataInputStream dis1 = new DataInputStream(fis1);
        byte[] keyBytes1 = new byte[(int) file1.length()];
        dis1.readFully(keyBytes1);
        dis1.close();

        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(keyBytes1);
        KeyFactory kf1 = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) kf1.generatePublic(spec1);
        String publicKey = "-----BEGIN RSA PRIVATE KEY-----\n" +Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()) + "\n-----END RSA PRIVATE KEY-----\n";
        System.out.println(publicKey);
        JwtParser build = Jwts.parserBuilder().setSigningKey(rsaPrivateKey).build();
        String jwt = Jwts.builder()

                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Expiration.FIVE_MINUTES.getTime()))
                /*Idealy you would have a private key that you would */
                .signWith(rsaPrivateKey,SignatureAlgorithm.RS256)
                .compact();
        Jwt parse = build.parse(jwt);
        System.out.println(build.isSigned(jwt));

    }

    public enum Expiration {

        FIVE_MINUTES(1000*60*5),
        TEN_MINUTES(1000*60*10);
        private final long time;

        Expiration(long time){
            this.time =time;
         }

        public long getTime() {
            return time;
        }
    }
}
