# Employee Payroll Management System

[![Java Version](https://img.shields.io/badge/Java-8%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/GUI-Java%20Swing-007396)](https://docs.oracle.com/javase/8/docs/technotes/guides/swing/)
[![Status](https://img.shields.io/badge/Capstone-Complete-brightgreen)](#)

A modular, event-driven desktop payroll management application written in Java. The project implements core Object-Oriented Programming (OOP) concepts, persistent character-stream File I/O, custom exception handling, and an interactive Swing GUI.

---

## 🎯 Course Outcomes (CO) Addressed

| Course Outcome | Implementation Details |
| :--- | :--- |
| **CO1: Constructors** | Implements **Default**, **Parameterized**, and **Copy** constructors inside `Employee.java` for secure object duplication and initialization. |
| **CO3: File I/O & Exceptions** | Employs `BufferedWriter`, `FileWriter`, `BufferedReader`, and `FileReader` for CSV-based file storage; enforces domain validation via `InvalidSalaryException`. |
| **CO4: Collections Framework** | Uses `ArrayList<Employee>` for dynamic in-memory record storage, search operations, and conditional deletions (`removeIf`). |
| **CO5: Swing GUI** | Multi-component graphical dashboard incorporating `JFrame`, `GridLayout`, `BorderLayout`, `JComboBox`, and `JOptionPane` alerts. |

---

## 📂 Project Architecture

```text
├── Employee.java                # Model class with 3 constructor types & salary logic
├── InvalidSalaryException.java  # Custom exception for salary & deduction validation
├── PayrollFileManager.java      # Dedicated I/O class for file persistence & slip export
├── PayrollSystemGUI.java        # Primary Swing GUI controller & analytics view
└── employees.txt                # Auto-generated CSV database storage file
