package view;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.TransactionFilter;
import model.AmountFilter;
import model.CategoryFilter;
import model.ExpenseTrackerModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.InputValidation;
import controller.ExpenseTrackerController;

import java.awt.*;
import java.text.NumberFormat;

import model.Transaction;
import model.TransactionFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseTrackerView extends JFrame {

  private JTable transactionsTable;
  private JButton addTransactionBtn;
  private JFormattedTextField amountField;
  private JTextField categoryField;
  private DefaultTableModel model;
  private ExpenseTrackerController controller;
  //private ExpenseTrackerModel model1;
  ExpenseTrackerModel model1 = new ExpenseTrackerModel();
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
//category filter button 
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
//Amount filter button 
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
// Add a JButton for the "Remove Selected" action


// Add an action listener to the "Remove Selected" button



  }
  
//calling for the green color 
  public void highlightFilteredRows(List<Transaction> transactions) {
     DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setBackground(Color.GREEN);
    Set<Integer> rowIndexes = findtransactions(transactions);

    transactionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        // Check if the current row should be highlighted
        if (rowIndexes.contains(row)) {
          c.setBackground(new Color(173, 255, 168));
        } else {
          c.setBackground(table.getBackground());
        }

        return c;
      }
    });

    transactionsTable.repaint();
}
  private Set<Integer> findtransactions(List<Transaction> transactions) {
    Set<Integer> rowIndices = new HashSet<>();

    for (Transaction transaction : transactions) {
      for (int row = 0; row < model.getRowCount()-1; row++) {
        if (model.getValueAt(row, 1).equals(transaction.getAmount())
                && model.getValueAt(row, 2).equals(transaction.getCategory())
                && model.getValueAt(row, 3).equals(transaction.getTimestamp())) {
          rowIndices.add(row);
        }
      }
    }

    return rowIndices;
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
