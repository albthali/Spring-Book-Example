package me.abdalrahman.albthali.books.schema;

public class PublicKeyResponse {
    private String publicKey;

    public PublicKeyResponse(String publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKeyResponse() {
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
