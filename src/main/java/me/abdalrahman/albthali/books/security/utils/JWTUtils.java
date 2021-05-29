package me.abdalrahman.albthali.books.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.abdalrahman.albthali.books.entity.JWTEntity;
import me.abdalrahman.albthali.books.repository.JWTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.*;

@Service
public class JWTUtils {
    private final String JWT_SECRET = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCqHLVvx8ETcsbpJnccArdFbqrmbEswGup72fxSP65NYW/bOpWMNWISQo8/emrRxy69p3mFKxH1sTcAOdiZOKOdwUaFthwPwdnoufDW1od0xGQw8eQKuijOQiHh2WMOj3Do7AAQM83iITO2l+k5fSS4nCG91KwwcvyxzWHVaexLfyrS6CXjgcol8kMJk5nBvHV4RlfpLvQzrtEVumUTHmabTVSpuBSn0HbUK48/xd2b/Qh3cvst0C1dxP3dk7p5mhGeA4NcjGDA/kqAR2lWpQAL7ROZoqi+wPJlQl/SbyS2TqZYNIJ7D6QAOsCYAQWrEs/BfemIhfCL0+134efSQS2HAgMBAAECggEAbr5728Z2Spv5JIW3GxQljd0LAzFTDYEoT/LMkhsE9TlOobEU9I84RlzwdsnSSrlaE3u439n3OjThcpM2ECHQWn4d1CSHRjbUDu8l/CbzTjAgbcC7zcP9lrF071wfYbY+MlSZgfLHwMQOr3tcCOIQiuKSEMwoQO5K1dG4NEBd5ZNSGQshVJNRVftfYmDykoFjM1nU7wLl2Y/9ZXBMCahtt//foqwUJG8CTm2+yaXcfMECeL44RQGTCSBrTH9EfLwKrzYVdrdnHAom/YE7JuVr8YWLVj4m7Z+46wc6vOFhVJR5x0MaddjckDos+B1DX3FbMafxsGZ0fWr4WMAZZVZuIQKBgQD+VBFrLBY+APsQD93geHH5vuVvnxlhglMAkIqO2ISVSoXhrldAhx5ZT7xdIlJO94to0ggirZCKScKDgFoZ7WOVZEt11ts2Bwmc0cu9AKUc/qLqlfWrjZ7rJUW3DNwkCANFA01oPRVbPHQB9AVmSm7Grv1Cc51e3ZqD/OkQPOjVtwKBgQCrOvBS+WURVfcc6HaT3bmY71IJfuGfstRlPtkTmwcfJFsWTI6fn2JA9US5jMs5zq64/6aXB/No6F4D1498DeSJnEe6+mviG+DGhkxW4sYLrgn+myF27Q/24HZbgAZZkxWlXSdd3vxsgfMH/2g2Pj/BdZuLLCodxM/4qN167NXmsQKBgQDGpvesFJWS/MUd+KbADJRKFRe1Jjp29YrLqBryFKEK01jbvIeeeWv1KtjgjkxoLecQiu2b2KJLwe1TNdAq7Eeaiup+MsKq4rhZa22ORZCyoxqxzUxFIat91HEt17ej1TZok3nE7+FckE20sNv+2CjKvChJ3XCQhtuIOOX6rvjW5QKBgENqijJxBMzlhsy37e/b7XX8S9V3QkgJNbzDGN6yPAST4NUPwVuxehLPPoa7m0wgRwSJi0KNm9VjH5jtpw2VvrQ7drZ0YEN+7GndeHMBCC6TEFZRJ8TWI2H1EESITHe6BFy+J4C7CbGDHHe6pIIu0hVOQ7Zw5gggdC2X9tOuKa2RAoGAGadMwhSsurNROzJynW6Sw2SNyvyL50lT7UDEGk9cYZZcY4rHCJrRUR/0mOLHWaiE2exoiCo2Qg8EvZ0UieI1HB4S7XGUDeKzvbOQ9Z4q8j7g/LJDx3qK7axqHpWUaMoshwwAOArtysgXTZonc2Vy6WRnPo9RnopeeWvgMTurVVg=";
    private final JwtParser parser;
    private final PrivateKey privateKey;
    @Autowired
    private JWTRepository jwtRepository;
    public JWTUtils() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(JWT_SECRET.getBytes()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(ks);
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
                //.signWith(SignatureAlgorithm.HS256,JWT_SECRET)
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

//    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//        kpg.initialize(2048);
//        KeyPair kp = kpg.generateKeyPair();
//        Key pub = kp.getPublic();
//        Key pvt = kp.getPrivate();
//        Base64.Encoder encoder = Base64.getEncoder();
//
//
//        Writer out = new FileWriter("private.key");
//        out.write("-----BEGIN RSA PRIVATE KEY-----\n");
//        out.write(encoder.encodeToString(pvt.getEncoded()));
//        out.write("\n-----END RSA PRIVATE KEY-----\n");
//        out.close();
//        out = new FileWriter("public.pub");
//        out.write("-----BEGIN RSA PUBLIC KEY-----\n");
//        out.write(encoder.encodeToString(pub.getEncoded()));
//        out.write("\n-----END RSA PUBLIC KEY-----\n");
//        out.close();
//        Path path = Paths.get("private.key");
//        byte[] bytes = Files.readAllBytes(path);
//        System.out.println(new String(bytes));
//        System.out.println(new String(pvt.getEncoded()));
//        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pvt.getEncoded());
//        System.out.println(keySpec.getEncoded());
//        System.out.println(keySpec.getFormat());
//
//        /* Generate private key. */
//       // PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        PrivateKey pvtfromfile = kf.generatePrivate(keySpec);
//        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Files.readAllBytes(Paths.get("public.pub")));
//        kf = KeyFactory.getInstance("RSA");
//      //  PublicKey pubFromFile = kf.generatePublic(x509EncodedKeySpec.);
//        String jwt = Jwts.builder()
//                .setClaims(Collections.emptyMap())
//                .setSubject("me")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(Expiration.TEN_MINUTES.getTime()))
//                /*Idealy you would have a private key that you would */
//                .signWith(pvt,SignatureAlgorithm.RS256)
//                .compact();
//        System.out.println("jwt: "+jwt);
//        JwtParser parser = Jwts.parserBuilder().setSigningKey(pubFromFile).build();
//        System.out.println("token is valid:"+parser.isSigned(jwt));
//
//
//    }
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
