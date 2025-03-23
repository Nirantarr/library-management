package user;

import chat.chatClient;
import database.DatabaseConnection;
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

public class studentDashBoard extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private int studentId = 1; // Example student ID (Replace with actual logged-in student ID)

    public studentDashBoard() {
        setTitle("Student Dashboard");
        setSize(700, 500);
        setLayout(new FlowLayout());

        JButton searchBookButton = new JButton("Search Books");
        JButton showYourBooksButton = new JButton("Show Your Books");
        JButton chatButton = new JButton("💬 Chat with Librarian");

        searchBookButton.addActionListener(e -> showBookList());
        showYourBooksButton.addActionListener(e -> showYourBooks());
        chatButton.addActionListener(e -> new chatClient());

        add(searchBookButton);
        add(showYourBooksButton);
        add(chatButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Custom Button Renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            setText(label);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom Button Editor
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int bookId;
        private studentDashBoard dashboard;

        public ButtonEditor(String label, studentDashBoard dashboard) {
            super(new JTextField());
            this.label = label;
            this.dashboard = dashboard;
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            bookId = (int) table.getValueAt(row, 0); // Assuming the book ID is in column 0
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                dashboard.requestBook(bookId);
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    private void showBookList() {
        JFrame bookFrame = new JFrame("Available Books");
        bookFrame.setSize(800, 400);
        bookFrame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        bookTable = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Stock");
        tableModel.addColumn("Action");

        bookTable.getColumn("Action").setCellRenderer(new ButtonRenderer("Request Book"));
        bookTable.getColumn("Action").setCellEditor(new ButtonEditor("Request Book", this));
        loadBooks();

        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookFrame.add(scrollPane, BorderLayout.CENTER);
        bookFrame.setVisible(true);
    }
    // Custom Button Editor for Returning Books
    class ReturnButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int requestId, bookId;
        private studentDashBoard dashboard;

        public ReturnButtonEditor(String label, studentDashBoard dashboard) {
            super(new JTextField());
            this.label = label;
            this.dashboard = dashboard;
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            requestId = (int) table.getValueAt(row, 0); // Request ID is in column 0
            bookId = (int) table.getValueAt(row, 1); // Book ID is in column 1
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                dashboard.returnBook(requestId, bookId);
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }


    private void showYourBooks() {
        JFrame bookFrame = new JFrame("Your Approved Books");
        bookFrame.setSize(800, 400);
        bookFrame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        bookTable = new JTable(tableModel);
        tableModel.addColumn("Request ID");
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Status");
        tableModel.addColumn("Action");

        bookTable.getColumn("Action").setCellRenderer(new ButtonRenderer("Return Book"));
        bookTable.getColumn("Action").setCellEditor(new ReturnButtonEditor("Return Book", this));
        loadYourBooks();

        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookFrame.add(scrollPane, BorderLayout.CENTER);
        bookFrame.setVisible(true);
    }

    private void loadBooks() {
        SwingWorker<Void, Object[]> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() { // Runs in a separate thread
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "SELECT * FROM books";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery();
                    tableModel.setRowCount(0);

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String title = rs.getString("title");
                        String author = rs.getString("author");
                        String genre = rs.getString("genre");
                        int stock = rs.getInt("stock");
                        publish(new Object[]{id, title, author, genre, stock, "Request Book"});
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading books: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Object[]> chunks) { // Updates UI
                for (Object[] row : chunks) {
                    tableModel.addRow(row);
                }
            }
        };
        worker.execute(); // Starts the background thread
    }


    private void loadYourBooks() {
        SwingWorker<Void, Object[]> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "SELECT r.id, r.book_id, b.title, r.status FROM book_requests r " +
                            "JOIN books b ON r.book_id = b.id WHERE r.status = 'Approved' AND r.student_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, studentId);
                    ResultSet rs = stmt.executeQuery();

                    tableModel.setRowCount(0);
                    while (rs.next()) {
                        int requestId = rs.getInt("id");
                        int bookId = rs.getInt("book_id");
                        String title = rs.getString("title");
                        String status = rs.getString("status");
                        publish(new Object[]{requestId, bookId, title, status, "Return Book"});
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading your books: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Object[]> chunks) {
                for (Object[] row : chunks) {
                    tableModel.addRow(row);
                }
            }
        };
        worker.execute();
    }


    private void requestBook(int bookId) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO book_requests (student_id, book_id, status) VALUES (?, ?, 'Pending')";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, studentId);
                    stmt.setInt(2, bookId);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error requesting book: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null, "Book request sent to Admin!");
            }
        };
        worker.execute();
    }


    private void returnBook(int requestId, int bookId) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);

                    String updateRequestSQL = "DELETE FROM book_requests WHERE id = ?";
                    PreparedStatement stmt1 = conn.prepareStatement(updateRequestSQL);
                    stmt1.setInt(1, requestId);
                    stmt1.executeUpdate();

                    String updateStockSQL = "UPDATE books SET stock = stock + 1 WHERE id = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(updateStockSQL);
                    stmt2.setInt(1, bookId);
                    stmt2.executeUpdate();

                    conn.commit();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error returning book: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null, "Book returned successfully!");
                loadYourBooks();  // Refresh the book list
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        new studentDashBoard();
    }

}
