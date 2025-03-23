package pages;

import database.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class bookSearchPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton, showAllButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public bookSearchPanel() {
        setLayout(new BorderLayout());

        // Search bar panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        showAllButton = new JButton("Show All Books");

        searchPanel.add(new JLabel("Enter Book Name: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        add(searchPanel, BorderLayout.NORTH);

        // Table for displaying results
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Stock");

        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // Load all books initially
        loadAllBooks();

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookTitle = searchField.getText().trim();
                if (!bookTitle.isEmpty()) {
                    searchBook(bookTitle);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a book name!");
                }
            }
        });

        // Show all books button action
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAllBooks();
            }
        });
    }

    private void searchBook(String bookTitle) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + bookTitle + "%");
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear previous results
            boolean found = false;

            while (rs.next()) {
                found = true;
                int bookId = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int stock = rs.getInt("stock");
                tableModel.addRow(new Object[]{bookId, title, author, genre, stock});
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "No books found with the given title.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching books: " + ex.getMessage());
        }
    }

    private void loadAllBooks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear previous results

            while (rs.next()) {
                int bookId = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int stock = rs.getInt("stock");
                tableModel.addRow(new Object[]{bookId, title, author, genre, stock});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching books: " + ex.getMessage());
        }
    }
}
