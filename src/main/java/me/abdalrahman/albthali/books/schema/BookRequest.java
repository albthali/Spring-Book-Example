package me.abdalrahman.albthali.books.schema;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class BookRequest {
    @NotBlank(message = "name must not be blank")
    private String name;
    @NotBlank(message = "author must not be blank")
    private String author;
    @NotBlank(message = "author must not be blank")
    private String description;
    @NotBlank(message = "visibility must not be blank")
    @Pattern(regexp = "^(public|private)$", message = "visibility is either public or private")
    private String visibility;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public BookRequest() {
    }
}
