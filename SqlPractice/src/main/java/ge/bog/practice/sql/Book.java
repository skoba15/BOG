package ge.bog.practice.sql;

public class Book {

    private String name;

    private int pagesNumber;

    private Author author;

    public Book(String name, int pagesNumber, Author author) {
        this.name = name;
        this.pagesNumber = pagesNumber;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
