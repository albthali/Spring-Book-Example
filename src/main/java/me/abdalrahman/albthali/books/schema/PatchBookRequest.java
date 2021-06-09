package me.abdalrahman.albthali.books.schema;

import javax.validation.constraints.Pattern;

public class PatchBookRequest {
    @Pattern(regexp = "^(public|private)$", message = "visibility is either public or private")
    private String visibility;

    public PatchBookRequest() {
    }

    public PatchBookRequest(String visibility) {
        this.visibility = visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
