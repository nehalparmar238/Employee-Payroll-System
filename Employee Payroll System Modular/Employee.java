public class Employee {
    private String id;
    private String name;
    private String department;
    private double basicSalary;
    private double allowances;
    private double deductions;

    // Default Constructor (CO1)
    public Employee() {
        this.id = "EMP000";
        this.name = "Unknown";
        this.department = "General";
        this.basicSalary = 0.0;
        this.allowances = 0.0;
        this.deductions = 0.0;
    }

    // Parameterized Constructor (CO1)
    public Employee(String id, String name, String department, double basicSalary, double allowances, double deductions) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.deductions = deductions;
    }

    // Copy Constructor (CO1)
    public Employee(Employee other) {
        this.id = other.id;
        this.name = other.name;
        this.department = other.department;
        this.basicSalary = other.basicSalary;
        this.allowances = other.allowances;
        this.deductions = other.deductions;
    }

    // Business Logic: Net Salary Calculation
    public double calculateNetSalary() {
        return basicSalary + allowances - deductions;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getBasicSalary() { return basicSalary; }
    public double getAllowances() { return allowances; }
    public double getDeductions() { return deductions; }

    // Convert to CSV record for file storage
    public String toCsvRecord() {
        return id + "," + name + "," + department + "," + basicSalary + "," + allowances + "," + deductions;
    }

    // Parse from CSV record
    public static Employee fromCsvRecord(String line) throws IllegalArgumentException {
        String[] parts = line.split(",");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid record format. Expected 6 fields, received " + parts.length);
        }
        String id = parts[0].trim();
        String name = parts[1].trim();
        String dept = parts[2].trim();
        double basic = Double.parseDouble(parts[3].trim());
        double allow = Double.parseDouble(parts[4].trim());
        double ded = Double.parseDouble(parts[5].trim());

        return new Employee(id, name, dept, basic, allow, ded);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-16s | %-12s | Basic: %9.2f | Allow: %8.2f | Ded: %8.2f | Net: %9.2f",
                id, name, department, basicSalary, allowances, deductions, calculateNetSalary());
    }
}
