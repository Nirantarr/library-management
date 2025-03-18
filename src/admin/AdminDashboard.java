package admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setLayout(new FlowLayout());
        
        JButton addBook = new JButton("Add Book");
        JButton updateBook = new JButton("Update Book");
        JButton deleteBook = new JButton("Delete Book");
        JButton manageUsers = new JButton("Manage Users");
        
        add(addBook);
        add(updateBook);
        add(deleteBook);
        add(manageUsers);
        
        addBook.addActionListener(e -> new AddBook());
        deleteBook.addActionListener(e -> new DeleteBook());
        manageUsers.addActionListener(e -> new ManageUsers());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}