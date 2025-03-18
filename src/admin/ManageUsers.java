package admin;
import javax.swing.*;
import java.awt.*;

class ManageUsers extends JFrame {
    public ManageUsers() {
        setTitle("Manage Users");
        setSize(300, 200);
        setLayout(new FlowLayout());
        
        JButton addUser = new JButton("Add User");
        JButton deleteUser = new JButton("Delete User");
        
        add(addUser);
        add(deleteUser);
        
        addUser.addActionListener(e -> new AddUser());
        deleteUser.addActionListener(e -> new DeleteUser());
        
        setVisible(true);
    }
}

// Add User
class AddUser extends JFrame {
    JTextField nameField, emailField;
    JButton addButton;
    
    public AddUser() {
        setTitle("Add User");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));
        
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);
        
        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);
        
        addButton = new JButton("Add");
        add(addButton);
        
        addButton.addActionListener(e -> addUserToDB());
        
        setVisible(true);
    }
    
    private void addUserToDB() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, nameField.getText());
            pst.setString(2, emailField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Added Successfully");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

// Delete User
class DeleteUser extends JFrame {
    JTextField idField;
    JButton deleteButton;
    
    public DeleteUser() {
        setTitle("Delete User");
        setSize(300, 200);
        setLayout(new GridLayout(2, 2));
        
        add(new JLabel("User ID:"));
        idField = new JTextField();
        add(idField);
        
        deleteButton = new JButton("Delete");
        add(deleteButton);
        
        deleteButton.addActionListener(e -> deleteUserFromDB());
        
        setVisible(true);
    }
    
    private void deleteUserFromDB() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(idField.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Deleted Successfully");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}