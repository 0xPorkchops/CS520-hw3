package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Transaction;

public class ExpenseTrackerView extends JFrame {

  private JTable transactionsTable;
  private JButton addTransactionBtn;
  private JButton removeTransactionBtn;
  private JButton exportToCSVBtn;
  private JFormattedTextField amountField;
  private JTextField categoryField;
  private DefaultTableModel model;

  private JTextField categoryFilterField;
  private JButton categoryFilterBtn;

  private JTextField amountFilterField;
  private JButton amountFilterBtn;

  private JButton clearFilterBtn;
    
  private List<Transaction> displayedTransactions = new ArrayList<>(); // ✅ Moved here

  public ExpenseTrackerView() {
    setTitle("Expense Tracker");
    setSize(800, 400);

    String[] columnNames = {"serial", "Amount", "Category", "Date"};
    this.model = new DefaultTableModel(columnNames, 0);

    transactionsTable = new JTable(model);
    transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    addTransactionBtn = new JButton("Add Transaction");
    removeTransactionBtn = new JButton("Remove Transaction");
    exportToCSVBtn = new JButton("Export to CSV");

    JLabel amountLabel = new JLabel("Amount:");
    NumberFormat format = NumberFormat.getNumberInstance();
    amountField = new JFormattedTextField(format);
    amountField.setColumns(10);

    JLabel categoryLabel = new JLabel("Category:");
    categoryField = new JTextField(10);

    JLabel categoryFilterLabel = new JLabel("Filter by Category:");
    categoryFilterField = new JTextField(10);
    categoryFilterBtn = new JButton("Filter by Category");

    JLabel amountFilterLabel = new JLabel("Filter by Amount:");
    amountFilterField = new JTextField(10);
    amountFilterBtn = new JButton("Filter by Amount");

    clearFilterBtn = new JButton("Clear Filter");
    
    JPanel inputPanel = new JPanel();
    inputPanel.add(amountLabel);
    inputPanel.add(amountField);
    inputPanel.add(categoryLabel); 
    inputPanel.add(categoryField);
    inputPanel.add(addTransactionBtn);
    inputPanel.add(removeTransactionBtn);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(amountFilterBtn);
    buttonPanel.add(categoryFilterBtn);
    buttonPanel.add(clearFilterBtn);
    buttonPanel.add(exportToCSVBtn);
    
    add(inputPanel, BorderLayout.NORTH);
    add(new JScrollPane(transactionsTable), BorderLayout.CENTER); 
    add(buttonPanel, BorderLayout.SOUTH);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public DefaultTableModel getTableModel() {
    return model;
  }

  public JTable getTransactionsTable() {
    return transactionsTable;
  }

  public double getAmountField() {
    if (amountField.getText().isEmpty()) {
      return 0;
    } else {
      return Double.parseDouble(amountField.getText());
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

  public void addApplyCategoryFilterListener(ActionListener listener) {
    categoryFilterBtn.addActionListener(listener);
  }

  public String getCategoryFilterInput() {
    return JOptionPane.showInputDialog(this, "Enter Category Filter:");
  }

  public void addApplyAmountFilterListener(ActionListener listener) {
    amountFilterBtn.addActionListener(listener);
  }

  public double getAmountFilterInput() {
    String input = JOptionPane.showInputDialog(this, "Enter Amount Filter:");
    try {
      return Double.parseDouble(input);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  public void addClearFilterListener(ActionListener listener) {
    clearFilterBtn.addActionListener(listener);
  }
    
  public void refreshTable(List<Transaction> transactions) {
    model.setRowCount(0);
    this.displayedTransactions = transactions; // ✅ Track displayed transactions

    int rowNum = model.getRowCount();
    double totalCost = 0;

    for (Transaction t : transactions) {
      totalCost += t.getAmount();
    }

    for (Transaction t : transactions) {
      model.addRow(new Object[]{++rowNum, t.getAmount(), t.getCategory(), t.getTimestamp()}); 
    }

    model.addRow(new Object[]{"Total", null, null, totalCost});
    transactionsTable.updateUI();
  }

  public JButton getAddTransactionBtn() {
    return addTransactionBtn;
  }

  public JButton getRemoveTransactionBtn() {
    return removeTransactionBtn;
  }
  
  public JButton getExportToCSVBtn() {
    return exportToCSVBtn;
  }

  public String getExportFileName() {
    String fileName = JOptionPane.showInputDialog(this, "Enter file name:");
    
    if (fileName == null || fileName.trim().isEmpty()) {
      return null;
    }
    
    // Append .csv if not already there
    if (!fileName.toLowerCase().endsWith(".csv")) {
      fileName += ".csv";
    }
    
    return fileName;
  }

  public int getSelectedTransactionIndex() {
    int selectedRow = transactionsTable.getSelectedRow();
    // Check if selected row is not the "Total" row (last row) and is valid
    if (selectedRow >= 0 && selectedRow < model.getRowCount() - 1) {
      return selectedRow;
    }
    return -1; // No valid selection
  }

  public void displayFilteredTransactions(List<Transaction> filteredTransactions) {
    refreshTable(filteredTransactions);
  }

  public List<Transaction> getDisplayedTransactions() {
    return displayedTransactions;
  }

  // Optional: remove if no longer needed
  // public void highlightRows(List<Integer> rowIndexes) { ... }

  // public void highlightRows(List<Integer> rowIndexes) {
  //     // The row indices are being used as hashcodes for the transactions.
  //     // The row index directly maps to the the transaction index in the list.
  //     transactionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
  //         @Override
  //         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
  //                                                       boolean hasFocus, int row, int column) {
  //             Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  //             if (rowIndexes.contains(row)) {
  //                 c.setBackground(new Color(173, 255, 168)); // Light green
  //             } else {
  //                 c.setBackground(table.getBackground());
  //             }
  //             return c;
  //         }
  //     });

  //     transactionsTable.repaint();
  // }


}
