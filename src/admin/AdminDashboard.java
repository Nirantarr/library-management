package admin;

import chat.chatServer;
import  chat.adminChatPanel;
import database.DatabaseConnection;
import pages.bookSearchPanel;
import java.net.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboard extends JFrame {
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setLayout(new FlowLayout());

        JButton addBook = new JButton("Add Book");
        JButton updateBook = new JButton("Update Book");
        JButton deleteBook = new JButton("Delete Book");
        JButton searchBook = new JButton("Search Book");
        JButton manageUsers = new JButton("Manage Users");
        JButton manageRequests = new JButton("Manage Book Requests");
        JButton startChatServerButton = new JButton("📡 Start Chat Server");
        JButton openChatButton = new JButton("💬 Open Admin Chat");

        add(addBook);
        add(updateBook);
        add(deleteBook);
        add(searchBook);
        add(manageUsers);
        add(manageRequests);
        add(startChatServerButton);
        add(openChatButton);

        addBook.addActionListener(e -> new AddBook());
        manageUsers.addActionListener(e -> new ManageUsers());
        startChatServerButton.addActionListener(e -> {
            try {
                if (!isServerRunning(12345)) { // Check if port 12345 is already in use
                    chatServer.main(new String[]{}); // Start Chat Server only if not running
                    JOptionPane.showMessageDialog(null, "Chat Server Started Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Chat Server is already running!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        updateBook.addActionListener(e -> {
            JFrame updateFrame = new JFrame("Update Book");
            updateFrame.setSize(500, 400);
            updateFrame.add(new UpdateBook());
            updateFrame.setVisible(true);
        });

        deleteBook.addActionListener(e -> {
            JFrame deleteFrame = new JFrame("Delete Book");
            deleteFrame.setSize(400, 200);
            deleteFrame.add(new DeleteBook());
            deleteFrame.setVisible(true);
        });

        searchBook.addActionListener(e -> {
            JFrame searchFrame = new JFrame("Search Books");
            searchFrame.setSize(500, 400);
            searchFrame.add(new bookSearchPanel());
            searchFrame.setVisible(true);
        });
        openChatButton.addActionListener(e -> new chat.adminChatPanel());

        manageRequests.addActionListener(e -> showRequests());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private boolean isServerRunning(int port) {
        try {
            new Socket("localhost", port).close();
            return true; // If we can connect, the server is running
        } catch (Exception e) {
            return false; // Server is not running
        }
    }
    private void showRequests() {
        JFrame requestFrame = new JFrame("Pending Book Requests");
        requestFrame.setSize(700, 400);
        requestFrame.setLayout(new BorderLayout());

        // Initialize the table model and table
        tableModel = new DefaultTableModel();
        requestTable = new JTable(tableModel);
        tableModel.addColumn("Request ID");
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Status");
        tableModel.addColumn("Action");

        // Set custom renderer and editor for button column
        requestTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        requestTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        loadRequests(); // Fetch book requests

        JScrollPane scrollPane = new JScrollPane(requestTable);
        requestFrame.add(scrollPane, BorderLayout.CENTER);
        requestFrame.setVisible(true);
    }

    private void loadRequests() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT r.id, r.book_id, b.title, r.status FROM book_requests r JOIN books b ON r.book_id = b.id WHERE r.status = 'Pending'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear previous data
            while (rs.next()) {
                int requestId = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String status = rs.getString("status");

                tableModel.addRow(new Object[]{requestId, bookId, title, status, "Approve"});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading requests: " + ex.getMessage());
        }
    }

    private void approveRequest(int requestId, int bookId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Update request status
            String updateRequestSQL = "UPDATE book_requests SET status = 'Approved' WHERE id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(updateRequestSQL);
            stmt1.setInt(1, requestId);
            stmt1.executeUpdate();

            // Decrease stock
            String updateStockSQL = "UPDATE books SET stock = stock - 1 WHERE id = ? AND stock > 0";
            PreparedStatement stmt2 = conn.prepareStatement(updateStockSQL);
            stmt2.setInt(1, bookId);
            stmt2.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(null, "Book request approved!");

            // Refresh the request table after approval
            loadRequests();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error approving request: " + ex.getMessage());
        }
    }

    // Custom Renderer to display JButton in JTable
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Approve");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom Editor to make JButton clickable in JTable
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Approve");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int requestId = (int) requestTable.getValueAt(selectedRow, 0);
                    int bookId = (int) requestTable.getValueAt(selectedRow, 1);
                    approveRequest(requestId, bookId);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Approve";
        }
    }
    public static void main(String[] args) {
        new AdminDashboard();
    }
}
