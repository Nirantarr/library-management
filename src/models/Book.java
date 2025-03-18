package models;

class Book {
   public int id;
    public String title, author;
    public boolean isAvailable;
    
    public Book(int id, String title, String author, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }
}