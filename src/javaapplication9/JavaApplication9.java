package javaapplication9;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



interface Taxable {
    double TAX_RATE = 0.15;        
    double calculateTax();
}

interface BonusCalculable {
    double calculateBonus(double performanceScore);  
}



abstract class Employee implements Comparable<Employee>, Taxable, BonusCalculable {

    private String id;
    private String name;
    protected double baseSalary;
    private LocalDate joinDate;

    public Employee(String id, String name, double baseSalary, LocalDate joinDate) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.joinDate = joinDate;
    }

   
    public abstract double calculateNetSalary();

   
    @Override
    public int compareTo(Employee other) {
        return Double.compare(other.calculateNetSalary(), this.calculateNetSalary());
    }

  
    @Override
    public double calculateTax() {
        return calculateNetSalary() * TAX_RATE;
    }

   
    @Override
    public double calculateBonus(double performanceScore) {
        return 0.0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBaseSalary() { return baseSalary; }
    public LocalDate getJoinDate() { return joinDate; }

    @Override
    public String toString() {
        return String.format("[%s] %-10s | Type: %-18s | Join: %s | Net Salary: %.2f | Tax: %.2f",
                id, name, getClass().getSimpleName(), joinDate, calculateNetSalary(), calculateTax());
    }
}



class FullTimeEmployee extends Employee {

    private double allowance;

    public FullTimeEmployee(String id, String name, double baseSalary,
                            LocalDate joinDate, double allowance) {
        super(id, name, baseSalary, joinDate);
        this.allowance = allowance;
    }

    @Override
    public double calculateNetSalary() {
        return baseSalary + allowance;
    }


    @Override
    public double calculateBonus(double performanceScore) {
        return baseSalary * 0.10 * performanceScore;
    }
}



class ContractorEmployee extends Employee {

    private int hourlyRate;
    private int hoursWorked;

    public ContractorEmployee(String id, String name, LocalDate joinDate,
                              int hourlyRate, int hoursWorked) {
        super(id, name, 0, joinDate);   
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateNetSalary() {
        return (double) hourlyRate * hoursWorked;
    }

   
    @Override
    public double calculateBonus(double performanceScore) {
        return performanceScore > 4.0 ? 500.0 : 0.0;
    }
}



public class JavaApplication9 {

    public static void main(String[] args) {

        
        List<Employee> employees = new ArrayList<>();
        employees.add(new FullTimeEmployee("E001", "Ahmed", 3000, LocalDate.of(2018, 3, 15), 500));
        employees.add(new FullTimeEmployee("E002", "Sara", 4500, LocalDate.of(2015, 7, 1), 800));
        employees.add(new ContractorEmployee("C001", "Omar", LocalDate.of(2022, 1, 10), 40, 120));
        employees.add(new ContractorEmployee("C002", "Laila", LocalDate.of(2020, 9, 5), 55, 100));
        employees.add(new FullTimeEmployee("E003", "Khaled", 2500, LocalDate.of(2021, 11, 20), 300));

       
        Map<String, Double> scores = new HashMap<>();
        scores.put("E001", 4.2);
        scores.put("E002", 4.8);
        scores.put("C001", 4.5);
        scores.put("C002", 3.5);
        scores.put("E003", 3.0);

        System.out.println("===== Original List =====");
        for (Employee e : employees) {
            System.out.println(e);
        }


        Collections.sort(employees);
        System.out.println("\n===== Sorted by Net Salary (Descending) - Comparable =====");
        for (Employee e : employees) {
            System.out.println(e);
        }

 
        Comparator<Employee> byJoinDate = new Comparator<Employee>() {
            @Override
            public int compare(Employee a, Employee b) {
                return a.getJoinDate().compareTo(b.getJoinDate());
            }
        };
        
        employees.sort(byJoinDate);
        System.out.println("\n===== Sorted by Join Date (Oldest First) - Comparator =====");
        for (Employee e : employees) {
            System.out.println(e);
        }

        
        double totalSalaries = 0;
        double totalBonuses = 0;
        System.out.println("\n===== Bonus Details =====");
        for (Employee e : employees) {
            double score = scores.get(e.getId());
            double bonus = e.calculateBonus(score);   
            totalSalaries += e.calculateNetSalary();
            totalBonuses += bonus;
            System.out.printf("%-10s | Score: %.1f | Bonus: %.2f%n", e.getName(), score, bonus);
        }

        System.out.println("\n===== Total Cost =====");
        System.out.printf("Total Salaries : %.2f%n", totalSalaries);
        System.out.printf("Total Bonuses  : %.2f%n", totalBonuses);
        System.out.printf("Grand Total    : %.2f%n", totalSalaries + totalBonuses);
    }
}
