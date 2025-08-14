package com.Multi.tenant_SaaS_Project_Management_System.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class Transaction {
    private String id;
    private String customerName;
    private double amount;
    private LocalDate date;
    private List<String> tags; // Example: ["grocery", "food"]
}
