package model;

import java.util.ArrayList;
import java.util.List;

import controller.InputValidation;

public class CategoryFilter implements TransactionFilter {
    private String category;

    public CategoryFilter(String category) {
        this.category = category;
    }
        public boolean inputValidation() {
        if (!InputValidation.isValidCategory(category)) {
            return false;
        }
        return true;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getCategory().equalsIgnoreCase(category)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}

