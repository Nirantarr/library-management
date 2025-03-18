package models;

class User {
    public int id;
    public String name, email;
    
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}