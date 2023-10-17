package controller;

import view.ExpenseTrackerView;

import java.util.ArrayList;
import java.util.List;



import model.ExpenseTrackerModel;
import model.Transaction;
import model.AmountFilter;
import model.CategoryFilter;
import model.TransactionFilter;
public class ExpenseTrackerController {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;

    // Set up view event handlers
  
  }
  public void applyFilter(TransactionFilter filter) {
    List<Transaction> transactions = model.getTransactions();
    List<Transaction> filteredTransactions = filter.filter(transactions);
    view.highlightFilteredRows(filteredTransactions);
}
  public void refresh() {

    // Get transactions from model
    List<Transaction> transactions = model.getTransactions();

    // Pass to view
    view.refreshTable(transactions);

  }

  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return false;
    }
    if (!InputValidation.isValidCategory(category)) {
      return false;
    }
    
    Transaction t = new Transaction(amount, category);
    model.addTransaction(t);
    view.getTableModel().addRow(new Object[]{t.getAmount(), t.getCategory(), t.getTimestamp()});
    refresh();
    return true;
  }
  public List<Transaction> getFilteredTransactions(TransactionFilter filter) {
    List<Transaction> transactions = model.getTransactions();
    List<Transaction> filteredTransactions = filter.filter(transactions);
    return filteredTransactions;
}
  
  // Other controller methods
}