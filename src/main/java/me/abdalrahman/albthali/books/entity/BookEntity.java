package me.abdalrahman.albthali.books.entity;


import me.abdalrahman.albthali.books.schema.BookRequest;

public class BookEntity {
    private int id;
    private String name;
    private String author;
    private String description;
    private String owner;
    private String visibility;

    public BookEntity() {
    }

    public BookEntity(int id, String name, String author, String description, String owner, String visibility) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.owner = owner;
        this.visibility = visibility;
    }
    public BookEntity(int id, String username,  BookRequest bookRequest){
        this(id,bookRequest.getName(),bookRequest.getAuthor(),bookRequest.getDescription(),username,bookRequest.getVisibility());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
