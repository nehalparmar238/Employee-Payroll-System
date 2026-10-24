import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main GUI & Controller Class (CO5: Swing GUI, CO4: Collections)
 */
public class PayrollSystemGUI extends JFrame implements ActionListener {
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private final String DATA_FILE = "employees.txt";

    // Form Components
    private JTextField txtId, txtName, txtBasic, txtAllowances, txtDeductions, txtNetSalary;
    private JComboBox<String> cbDepartment;
    private JButton btnAdd, btnSave, btnLoad, btnViewAll, btnDelete, btnExportSlip, btnClear;
    private JTextArea displayArea;
    private JLabel lblMetrics;

    public PayrollSystemGUI() {
        super("Employee Payroll System - Capstone Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Input Form Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 8, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        inputPanel.add(new JLabel("Employee ID:"));
        txtId = new JTextField();
        inputPanel.add(txtId);

        inputPanel.add(new JLabel("Employee Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Department:"));
        String[] depts = {"IT & Development", "Human Resources", "Finance", "Operations", "Marketing"};
        cbDepartment = new JComboBox<>(depts);
        inputPanel.add(cbDepartment);

        inputPanel.add(new JLabel("Basic Salary (Rs.):"));
        txtBasic = new JTextField();
        inputPanel.add(txtBasic);

        inputPanel.add(new JLabel("Allowances (Rs.):"));
        txtAllowances = new JTextField();
        inputPanel.add(txtAllowances);

        inputPanel.add(new JLabel("Deductions (Rs.):"));
        txtDeductions = new JTextField();
        inputPanel.add(txtDeductions);

        inputPanel.add(new JLabel("Net Salary (Preview):"));
        txtNetSalary = new JTextField();
        txtNetSalary.setEditable(false);
        txtNetSalary.setBackground(new Color(245, 245, 245));
        inputPanel.add(txtNetSalary);

        // 2. Button Control Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnAdd = new JButton("Add Employee");
        btnDelete = new JButton("Delete by ID");
        btnSave = new JButton("Save to File");
        btnLoad = new JButton("Load from File");
        btnExportSlip = new JButton("Export Pay Slip");
        btnViewAll = new JButton("View All");
        btnClear = new JButton("Clear Form");

        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnSave.addActionListener(this);
        btnLoad.addActionListener(this);
        btnExportSlip.addActionListener(this);
        btnViewAll.addActionListener(this);
        btnClear.addActionListener(this);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnExportSlip);
        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnClear);

        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(inputPanel, BorderLayout.CENTER);
        northContainer.add(buttonPanel, BorderLayout.SOUTH);
        add(northContainer, BorderLayout.NORTH);

        // 3. Center Display Log
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Payroll Master Register"));
        add(scrollPane, BorderLayout.CENTER);

        // 4. Bottom Analytics Status Bar (Interesting addition!)
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        lblMetrics = new JLabel("Records: 0 | Total Payroll: Rs. 0.00 | Avg Net: Rs. 0.00");
        lblMetrics.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusPanel.add(lblMetrics);
        add(statusPanel, BorderLayout.SOUTH);

        // Auto-load existing records if file exists
        autoLoadData();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnAdd) {
            handleAddEmployee();
        } else if (src == btnDelete) {
            handleDeleteEmployee();
        } else if (src == btnSave) {
            handleSaveToFile();
        } else if (src == btnLoad) {
            handleLoadFromFile();
        } else if (src == btnExportSlip) {
            handleExportPaySlip();
        } else if (src == btnViewAll) {
            refreshDisplay();
        } else if (src == btnClear) {
            clearForm();
        }
    }

    private void handleAddEmployee() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String dept = (String) cbDepartment.getSelectedItem();

        try {
            if (id.isEmpty() || name.isEmpty() || txtBasic.getText().trim().isEmpty() ||
                txtAllowances.getText().trim().isEmpty() || txtDeductions.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("All fields are mandatory.");
            }

            // Check duplicate ID
            for (Employee existing : employeeList) {
                if (existing.getId().equalsIgnoreCase(id)) {
                    throw new IllegalArgumentException("Employee ID '" + id + "' already exists!");
                }
            }

            double basic = Double.parseDouble(txtBasic.getText().trim());
            double allow = Double.parseDouble(txtAllowances.getText().trim());
            double ded = Double.parseDouble(txtDeductions.getText().trim());

            if (basic < 0 || allow < 0 || ded < 0) {
                throw new InvalidSalaryException("Salary components (basic, allowances, deductions) cannot be negative.");
            }
            if (ded > (basic + allow)) {
                throw new InvalidSalaryException("Deductions cannot exceed total gross earnings (Basic + Allowances).");
            }

            // Use Parameterized Constructor (CO1)
            Employee newEmp = new Employee(id, name, dept, basic, allow, ded);

            // Use Copy Constructor (CO1)
            Employee cloneEmp = new Employee(newEmp);

            // Add to ArrayList (CO4)
            employeeList.add(cloneEmp);

            txtNetSalary.setText(String.format("%.2f", cloneEmp.calculateNetSalary()));
            JOptionPane.showMessageDialog(this, "Employee successfully registered!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshDisplay();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Numeric format error: Enter valid decimal numbers for salary.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidSalaryException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteEmployee() {
        String targetId = JOptionPane.showInputDialog(this, "Enter Employee ID to remove:", "Delete Employee", JOptionPane.QUESTION_MESSAGE);
        if (targetId == null || targetId.trim().isEmpty()) return;

        boolean removed = employeeList.removeIf(emp -> emp.getId().equalsIgnoreCase(targetId.trim()));
        if (removed) {
            JOptionPane.showMessageDialog(this, "Employee " + targetId + " deleted from memory.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "No employee found with ID: " + targetId, "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSaveToFile() {
        if (employeeList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records to save.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            PayrollFileManager.saveToFile(DATA_FILE, employeeList);
            JOptionPane.showMessageDialog(this, "Successfully saved " + employeeList.size() + " records to " + DATA_FILE, "File Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "I/O Error while saving: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoadFromFile() {
        try {
            ArrayList<Employee> loaded = PayrollFileManager.loadFromFile(DATA_FILE);
            employeeList = loaded;
            JOptionPane.showMessageDialog(this, "Loaded " + employeeList.size() + " records from " + DATA_FILE, "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshDisplay();
        } catch (IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExportPaySlip() {
        String targetId = JOptionPane.showInputDialog(this, "Enter Employee ID to export slip:", "Generate Pay Slip", JOptionPane.QUESTION_MESSAGE);
        if (targetId == null || targetId.trim().isEmpty()) return;

        Employee found = null;
        for (Employee emp : employeeList) {
            if (emp.getId().equalsIgnoreCase(targetId.trim())) {
                found = emp;
                break;
            }
        }

        if (found == null) {
            JOptionPane.showMessageDialog(this, "Employee ID " + targetId + " not found.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fileName = "PaySlip_" + found.getId() + ".txt";
        try {
            PayrollFileManager.exportPaySlip(found, fileName);
            JOptionPane.showMessageDialog(this, "Individual pay slip generated: " + fileName, "Pay Slip Exported", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to generate pay slip: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void autoLoadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                employeeList = PayrollFileManager.loadFromFile(DATA_FILE);
                refreshDisplay();
            } catch (Exception ignored) {}
        }
    }

    private void refreshDisplay() {
        if (employeeList.isEmpty()) {
            displayArea.setText("No active records. Add an employee or load from file.");
            lblMetrics.setText("Records: 0 | Total Payroll: Rs. 0.00 | Avg Net: Rs. 0.00");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s | %-16s | %-12s | %-18s | %-17s | %-17s | %-10s\n",
                "ID", "Name", "Department", "Basic (Rs.)", "Allowances (Rs.)", "Deductions (Rs.)", "Net Salary (Rs.)"));
        sb.append("=".repeat(110)).append("\n");

        double totalPayroll = 0;
        for (Employee emp : employeeList) {
            sb.append(emp.toString()).append("\n");
            totalPayroll += emp.calculateNetSalary();
        }

        displayArea.setText(sb.toString());
        double avgNet = totalPayroll / employeeList.size();
        lblMetrics.setText(String.format("Records: %d | Total Monthly Payroll: Rs. %,.2f | Average Net Salary: Rs. %,.2f",
                employeeList.size(), totalPayroll, avgNet));
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtBasic.setText("");
        txtAllowances.setText("");
        txtDeductions.setText("");
        txtNetSalary.setText("");
        cbDepartment.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PayrollSystemGUI().setVisible(true);
        });
    }
}
