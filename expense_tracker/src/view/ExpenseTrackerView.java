package view;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.TransactionFilter;
import model.AmountFilter;
import model.CategoryFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.InputValidation;
import controller.ExpenseTrackerController;

import java.awt.*;
import java.text.NumberFormat;

import model.Transaction;
import model.TransactionFilter;

import java.util.ArrayList;
import java.util.List;

public class ExpenseTrackerView extends JFrame {

  private JTable transactionsTable;
  private JButton addTransactionBtn;
  private JFormattedTextField amountField;
  private JTextField categoryField;
  private DefaultTableModel model;
  private ExpenseTrackerController controller;
private List<Transaction> transactionsList = new ArrayList<>(); // Initialize as an empty list
  

  public ExpenseTrackerView() {
    setTitle("Expense Tracker"); // Set title
    setSize(600, 400); // Make GUI larger

    String[] columnNames = {"serial", "Amount", "Category", "Date"};
    this.model = new DefaultTableModel(columnNames, 0);

    addTransactionBtn = new JButton("Add Transaction");

    // Create UI components
    JLabel amountLabel = new JLabel("Amount:");
    NumberFormat format = NumberFormat.getNumberInstance();

    amountField = new JFormattedTextField(format);
    amountField.setColumns(10);

    
    JLabel categoryLabel = new JLabel("Category:");
    categoryField = new JTextField(10);

    // Create table
    transactionsTable = new JTable(model);
  
    // Layout components
    JPanel inputPanel = new JPanel();
    inputPanel.add(amountLabel);
    inputPanel.add(amountField);
    inputPanel.add(categoryLabel); 
    inputPanel.add(categoryField);
    inputPanel.add(addTransactionBtn);
  
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(addTransactionBtn);
  
    // Add panels to frame
    add(inputPanel, BorderLayout.NORTH);
    add(new JScrollPane(transactionsTable), BorderLayout.CENTER); 
    add(buttonPanel, BorderLayout.SOUTH);
  
    // Set frame properties
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // Inside the ExpenseTrackerView constructor
JButton filterByCategoryBtn = new JButton("Filter by Category");
JButton filterByAmountBtn = new JButton("Filter by Amount");

filterByCategoryBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String categoryToFilter = JOptionPane.showInputDialog("Enter category to filter:");
        if (InputValidation.isValidCategory(categoryToFilter)) {
            TransactionFilter filter = new CategoryFilter(categoryToFilter);
            controller.applyFilter(filter);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid category input.");
        }
    }
});

filterByAmountBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        double minAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter minimum amount:"));
        double maxAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter maximum amount:"));
        if (InputValidation.isValidAmount(minAmount) && InputValidation.isValidAmount(maxAmount)) {
            TransactionFilter filter = new AmountFilter(minAmount, maxAmount);
            controller.applyFilter(filter);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid amount input.");
        }
    }
});

// Add the filter buttons to the inputPanel
inputPanel.add(filterByCategoryBtn);
inputPanel.add(filterByAmountBtn);

  }
  

  public void highlightFilteredRows(List<Transaction> filteredTransactions) {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setBackground(new Color(173, 255, 168)); // Set the background color

    for (int i = 0; i < model.getRowCount(); i++) {
        Transaction transaction = getTransactionForRow(i);
        if (filteredTransactions.contains(transaction)) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                transactionsTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
            }
        }
    }
}
private Transaction getTransactionForRow(int row) {
  // In this example, we assume that your data structure is a list of Transaction objects.
  Object amountObj = model.getValueAt(row, 1);

  if (amountObj != null) {
    double amount = Double.parseDouble(amountObj.toString());
    String category = (String) model.getValueAt(row, 2);
    //String timestamp = (String) model.getValueAt(row, 3);
    return new Transaction(amount, category);
}
  return null; // Return null if the row is out of bounds.
}



public void setController(ExpenseTrackerController controller) {
  this.controller = controller;
}


  public void refreshTable(List<Transaction> transactions) {
      // Clear existing rows
      model.setRowCount(0);
      // Get row count
      int rowNum = model.getRowCount();
      double totalCost=0;
      // Calculate total cost
      for(Transaction t : transactions) {
        totalCost+=t.getAmount();
      }
      // Add rows from transactions list
      for(Transaction t : transactions) {
        model.addRow(new Object[]{rowNum+=1,t.getAmount(), t.getCategory(), t.getTimestamp()}); 
      }
        // Add total row
        Object[] totalRow = {"Total", null, null, totalCost};
        model.addRow(totalRow);
  
      // Fire table update
      transactionsTable.updateUI();
  
    }  
  

  
  
  public JButton getAddTransactionBtn() {
    return addTransactionBtn;
  }
  public DefaultTableModel getTableModel() {
    return model;
  }
  // Other view methods
    public JTable getTransactionsTable() {
    return transactionsTable;
  }

  public double getAmountField() {
    if(amountField.getText().isEmpty()) {
      return 0;
    }else {
    double amount = Double.parseDouble(amountField.getText());
    return amount;
    }
  }

  public void setAmountField(JFormattedTextField amountField) {
    this.amountField = amountField;
  }

  
  public String getCategoryField() {
    return categoryField.getText();
  }

  public void setCategoryField(JTextField categoryField) {
    this.categoryField = categoryField;
  }
}
