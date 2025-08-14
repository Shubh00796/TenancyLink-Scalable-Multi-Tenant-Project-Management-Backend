package com.Multi.tenant_SaaS_Project_Management_System.ServiceImplimentations;


import com.Multi.tenant_SaaS_Project_Management_System.DTOs.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportGenerationService {

    // 1. Filter high value transactions
    public List<Transaction> filterHighValue(List<Transaction> transactions, double minAmt) {
        return transactions
                .stream()
                .filter(transaction -> transaction.getAmount() >= minAmt)
                .collect(Collectors.toList());
    }

    // 2. Sort by amount then date
    public List<Transaction> sortByAmountAndDate(List<Transaction> transactions) {
        return transactions
                .stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                        .thenComparing(Transaction::getDate))
                .collect(Collectors.toList());
    }

    // 3. Group by customer and sum amounts
    public Map<String, Double> totalAmountPerCustomer(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerName,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    // 4. Find latest transaction using Optional
    public Optional<Transaction> latestTransaction(List<Transaction> transactions) {
        return transactions
                .stream()
                .max(Comparator.comparing(Transaction::getDate));
    }

    // 5. Extract unique tags using flatMap
    public Set<String> extractUniqueTags(List<Transaction> transactions) {
        return transactions
                .stream()
                .flatMap(transaction -> transaction.getTags().stream())
                .collect(Collectors.toSet());

    }

    // 7. Process in parallel
    public double parallelTotalAmount(List<Transaction> transactions) {
        return transactions.parallelStream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // 8. Filter by date range using Java 8 Date API
    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
    }
}
