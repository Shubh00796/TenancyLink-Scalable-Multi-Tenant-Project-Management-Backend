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

    /**
     * Filters transactions above a minimum amount.
     */
    public List<Transaction> filterHighValue(List<Transaction> transactions, double minAmount) {
        return transactions.stream()
                .filter(tx -> tx.getAmount() >= minAmount)
                .collect(Collectors.toList());
    }

    /**
     * Sorts transactions by amount (descending) and then by date (ascending).
     */
    public List<Transaction> sortByAmountAndDate(List<Transaction> transactions) {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                        .thenComparing(Transaction::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Groups transactions by customer and sums their amounts.
     */
    public Map<String, Double> totalAmountPerCustomer(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCustomerName,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    /**
     * Finds the latest transaction based on date.
     */
    public Optional<Transaction> latestTransaction(List<Transaction> transactions) {
        return transactions.stream()
                .max(Comparator.comparing(Transaction::getDate));
    }

    /**
     * Extracts all unique tags from transactions.
     */
    public Set<String> extractUniqueTags(List<Transaction> transactions) {
        return transactions.stream()
                .flatMap(tx -> tx.getTags().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Calculates total amount using parallel stream.
     */
    public double parallelTotalAmount(List<Transaction> transactions) {
        return transactions.parallelStream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Filters transactions within a date range.
     */
    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(tx -> !tx.getDate().isBefore(start) && !tx.getDate().isAfter(end))
                .collect(Collectors.toList());
    }
}
