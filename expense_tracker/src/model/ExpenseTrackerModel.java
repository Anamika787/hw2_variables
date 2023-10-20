package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenseTrackerModel {

  private final List<Transaction> transactions;
  private final List<Transaction> removedTransactions; // List to store removed transactions

  public ExpenseTrackerModel() {
    transactions = new ArrayList<>();
    removedTransactions = new ArrayList<>(); // Initialize the removedTransactions list

  }

  public void addTransaction(Transaction t) {
    transactions.add(t);
  }

  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    removedTransactions.add(t); // Store removed transactions
  }
 
  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions); // Apply immutability return an unmodifiable list to ensure immutability and prevent external modifications.
  }
}
