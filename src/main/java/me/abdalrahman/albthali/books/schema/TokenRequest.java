package me.abdalrahman.albthali.books.schema;

import javax.validation.constraints.NotBlank;

public class TokenRequest {
    @NotBlank
    private String refreshToken;

    public TokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
