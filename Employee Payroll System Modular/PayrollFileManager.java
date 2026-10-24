import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dedicated File Management Class (CO3: File I/O & Exception Handling)
 * Encapsulates all reading, writing, and export operations.
 */
public class PayrollFileManager {

    /**
     * Saves list of employees to a text file using FileWriter and BufferedWriter.
     */
    public static void saveToFile(String filePath, List<Employee> employees) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Employee emp : employees) {
                bw.write(emp.toCsvRecord());
                bw.newLine();
            }
        }
    }

    /**
     * Loads employees from a text file using FileReader and BufferedReader.
     */
    public static ArrayList<Employee> loadFromFile(String filePath) throws IOException, IllegalArgumentException {
        ArrayList<Employee> loadedList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                try {
                    Employee emp = Employee.fromCsvRecord(line);
                    loadedList.add(emp);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Error parsing line " + lineNumber + ": " + ex.getMessage());
                }
            }
        }
        return loadedList;
    }

    /**
     * Interesting Addition: Generates an official formatted Pay Slip file for an individual employee.
     */
    public static void exportPaySlip(Employee emp, String outputPath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("============================================================\n");
            bw.write("                  OFFICIAL EMPLOYEE PAY SLIP                \n");
            bw.write("============================================================\n");
            bw.write(String.format("Employee ID   : %s\n", emp.getId()));
            bw.write(String.format("Employee Name : %s\n", emp.getName()));
            bw.write(String.format("Department    : %s\n", emp.getDepartment()));
            bw.write("------------------------------------------------------------\n");
            bw.write(String.format("Basic Salary  : Rs. %12.2f\n", emp.getBasicSalary()));
            bw.write(String.format("Allowances (+) : Rs. %12.2f\n", emp.getAllowances()));
            bw.write(String.format("Deductions (-) : Rs. %12.2f\n", emp.getDeductions()));
            bw.write("------------------------------------------------------------\n");
            bw.write(String.format("NET PAYABLE   : Rs. %12.2f\n", emp.calculateNetSalary()));
            bw.write("============================================================\n");
            bw.write("Status: Generated from Payroll Management System\n");
        }
    }
}
