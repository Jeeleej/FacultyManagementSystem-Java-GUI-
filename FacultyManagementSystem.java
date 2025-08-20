import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FacultyManagementSystem extends JFrame implements ActionListener 
{
    JLabel label;
    JTextField nameField, idField, departmentField, emailField, salaryField, postField;
    JButton addButton, viewButton, deleteButton, updateButton, uploadImageButton;
    JTextArea displayArea;
    String facultyImagePath;
    JPanel imagePanel; 
    JLabel imageLabel; 

    ArrayList<Faculty> facultyDatabase;

    public FacultyManagementSystem() 
    {
        setTitle("Faculty Management System");
        setSize(500, 750); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);

        label = new JLabel("Faculty Management System");
        label.setBounds(150, 10, 200, 30);
        add(label);

        JLabel idLabel = new JLabel("Faculty ID:");
        idLabel.setBounds(50, 50, 100, 20);
        add(idLabel);
        idField = new JTextField();
        idField.setBounds(150, 50, 150, 20);
        add(idField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 80, 100, 20);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 80, 150, 20);
        add(nameField);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setBounds(50, 110, 100, 20);
        add(deptLabel);
        departmentField = new JTextField();
        departmentField.setBounds(150, 110, 150, 20);
        add(departmentField);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(50, 140, 100, 20);
        add(salaryLabel);
        salaryField = new JTextField();
        salaryField.setBounds(150, 140, 150, 20);
        add(salaryField);

        JLabel postLabel = new JLabel("Post:");
        postLabel.setBounds(50, 170, 100, 20);
        add(postLabel);
        postField = new JTextField();
        postField.setBounds(150, 170, 150, 20);
        add(postField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 200, 100, 20);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(150, 200, 150, 20);
        add(emailField);

        JLabel imageTextLabel = new JLabel("Faculty Image:");
        imageTextLabel.setBounds(50, 230, 100, 20);
        add(imageTextLabel);
        uploadImageButton = new JButton("Upload Image");
        uploadImageButton.setBounds(150, 230, 120, 30);
        add(uploadImageButton);

        addButton = new JButton("Add Faculty");
        addButton.setBounds(50, 280, 120, 30);
        add(addButton);
        viewButton = new JButton("View Faculty");
        viewButton.setBounds(180, 280, 120, 30);
        add(viewButton);
        deleteButton = new JButton("Delete Faculty");
        deleteButton.setBounds(50, 320, 120, 30);
        add(deleteButton);
        updateButton = new JButton("Update Faculty");
        updateButton.setBounds(180, 320, 120, 30);
        add(updateButton);

        imagePanel = new JPanel();
        imagePanel.setBounds(50, 360, 175, 175); 
        imagePanel.setBorder(BorderFactory.createTitledBorder("Faculty Image"));
        imagePanel.setLayout(new BorderLayout());

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel);

        displayArea = new JTextArea();
        displayArea.setBounds(50, 550, 400, 100); 
        displayArea.setEditable(false);
        add(displayArea);

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);
        uploadImageButton.addActionListener(this); 

        facultyDatabase = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == addButton) 
        {
            String id = idField.getText();
            String name = nameField.getText();
            String department = departmentField.getText();
            String email = emailField.getText();
            String salary = salaryField.getText();
            String post = postField.getText(); 

            if (!id.isEmpty() && !name.isEmpty() && !department.isEmpty() && !email.isEmpty() && !salary.isEmpty() && !post.isEmpty()) 
            {
                if (isValidEmail(email)) 
                { 
                    Faculty newFaculty = new Faculty(id, name, department, email, Double.parseDouble(salary), post, facultyImagePath);
                    facultyDatabase.add(newFaculty);
                    JOptionPane.showMessageDialog(this, "Faculty added successfully!");
                    clearFields();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Invalid email format! Please enter a valid email address.");
                }
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            }
        } 
        
        else if (e.getSource() == viewButton) 
        {
            String id = JOptionPane.showInputDialog(this, "Enter Faculty ID:");
            Faculty faculty = findFacultyById(id);

            if (faculty != null) {
                displayArea.setText(faculty.toString());
                
                if (faculty.getImagePath() != null && !faculty.getImagePath().isEmpty()) 
                {
                    ImageIcon imageIcon = new ImageIcon(faculty.getImagePath());
                    Image img = imageIcon.getImage().getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img)); 
                } 
                else 
                {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No image available."); 
                }
                
                imagePanel.revalidate(); 
                imagePanel.repaint(); 
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Faculty not found!");
            }
        } 
        else if (e.getSource() == deleteButton) 
        {
            String id = JOptionPane.showInputDialog(this, "Enter Faculty ID to delete:");
            Faculty faculty = findFacultyById(id);

            if (faculty != null) 
            {
                facultyDatabase.remove(faculty);
                JOptionPane.showMessageDialog(this, "Faculty deleted successfully!");
                displayArea.setText("");
                imageLabel.setIcon(null); 
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Faculty not found!");
            }
        } 
        else if (e.getSource() == updateButton) 
        {
            String id = JOptionPane.showInputDialog(this, "Enter Faculty ID to update:");
            Faculty faculty = findFacultyById(id);

            if (faculty != null) 
            {
                String newName = JOptionPane.showInputDialog(this, "Enter new name:", faculty.getName());
                String newDepartment = JOptionPane.showInputDialog(this, "Enter new department:", faculty.getDepartment());
                String newSalary = JOptionPane.showInputDialog(this, "Enter new salary:", String.valueOf(faculty.getSalary()));
                String newPost = JOptionPane.showInputDialog(this, "Enter new post:", faculty.getPost());
                String newEmail = JOptionPane.showInputDialog(this, "Enter new email:", faculty.getEmail());

                if (isValidEmail(newEmail)) 
                { 
                    faculty.setName(newName);
                    faculty.setDepartment(newDepartment);
                    faculty.setEmail(newEmail);
                    faculty.setSalary(Double.parseDouble(newSalary));
                    faculty.setPost(newPost); 

                    int updateImage = JOptionPane.showConfirmDialog(this, "Would you like to update the faculty image?", "Update Image", JOptionPane.YES_NO_OPTION);
                    if (updateImage == JOptionPane.YES_OPTION) 
                    {
                        uploadImage();
                        faculty.setImagePath(facultyImagePath); 
                    }

                    JOptionPane.showMessageDialog(this, "Faculty updated successfully!");
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Invalid email format! Please enter a valid email address.");
                }
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Faculty not found!");
            }
        } 
        else if (e.getSource() == uploadImageButton) 
        { 
            uploadImage();
        }
    }

    public Faculty findFacultyById(String id) 
    {
        for (Faculty faculty : facultyDatabase) 
        {
            if (faculty.getId().equals(id)) 
            {
                return faculty;
            }
        }
        return null;
    }

    public void clearFields() 
    {
        idField.setText("");
        nameField.setText("");
        departmentField.setText("");
        emailField.setText("");
        salaryField.setText("");
        postField.setText("");
        facultyImagePath = null; 
        imageLabel.setIcon(null); 
        imagePanel.revalidate();
        imagePanel.repaint(); 
    }

    public void uploadImage() 
    {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileChooser.getSelectedFile();
            facultyImagePath = selectedFile.getAbsolutePath(); 
            JOptionPane.showMessageDialog(this, "Image uploaded successfully!");
        } 
        else 
        {
            facultyImagePath = null;
        }
    }

    public boolean isValidEmail(String email) 
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static void main(String[] args) 
    {
        new FacultyManagementSystem();
    }
}

class Faculty 
{
    String id;
    String name;
    String department;
    String email;
    double salary;
    String post; 
    String imagePath;

    public Faculty(String id, String name, String department, String email, double salary, String post, String imagePath) 
    {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.salary = salary;
        this.post = post; 
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }

    public String getPost() {
        return post; 
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPost(String post) {
        this.post = post; 
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() 
    {
        return "ID: " + id + "\nName: " + name + "\nDepartment: " + department + "\nSalary: " + salary + "\nPost: " + post + "\nEmail: " + email;
    }
}