package com.resume.horan.eugene.eugenehoranresume.model;

public class Like {
    private Author author;
    private Object timestamp;

    public Like() {
    }

    public Like(Author author, Object timestamp) {
        this.author = author;
        this.timestamp = timestamp;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
