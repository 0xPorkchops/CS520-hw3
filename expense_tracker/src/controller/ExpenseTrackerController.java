package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import model.ExpenseTrackerModel;
import model.Export.TransactionExporter;
import model.Filter.TransactionFilter;
import model.Transaction;
import view.ExpenseTrackerView;

public class ExpenseTrackerController {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  /** 
   * The Controller is applying the Strategy design pattern.
   * This is the has-a relationship with the Strategy class 
   * being used in the applyFilter method.
   */
  private TransactionFilter filter;
  private TransactionExporter exporter;

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;
  }

  public void setFilter(TransactionFilter filter) {
    // Sets the Strategy class being used in the applyFilter method.
    this.filter = filter;
  }
  
  public void setExporter(TransactionExporter exporter) {
    this.exporter = exporter;
  }

  public void refresh() {
    List<Transaction> transactions = model.getTransactions();
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
  
  /**
   * Remove a transaction from the model based on the selected index in the view
   * 
   * @return true if transaction was successfully removed, false otherwise
   */
  public boolean removeTransaction() {
    int selectedIndex = view.getSelectedTransactionIndex();
    
    if (selectedIndex == -1) {
      return false;
    }
    
    List<Transaction> transactions = model.getTransactions();
    
    if (selectedIndex < transactions.size()) {
      Transaction transactionToRemove = transactions.get(selectedIndex);
      model.removeTransaction(transactionToRemove);
      refresh();
      return true;
    }
    
    return false;
  }
  
  /**
   * Export transactions to a file
   * 
   * @param fileName The name of the file to export to
   * @return true if export was successful, false otherwise
   */
  public boolean exportTransactions(String fileName) {
    if (exporter == null) {
      return false;
    }
    
    if (fileName == null || fileName.trim().isEmpty()) {
      return false;
    }
    
    List<Transaction> transactions = model.getTransactions();
    if (transactions.isEmpty()) {
      return false;
    }
    
    try {
      File file = new File(fileName);
      return exporter.exportTransactions(transactions, file);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void applyFilter() {
    List<Transaction> filteredTransactions;
    // If no filter is specified, show all transactions.
    if (filter == null) {
      filteredTransactions = model.getTransactions();
    }
    // If a filter is specified, show only the transactions accepted by that filter.
    else {
      // Use the Strategy class to perform the desired filtering
      List<Transaction> transactions = model.getTransactions();
      filteredTransactions = filter.filter(transactions);
    }
    view.displayFilteredTransactions(filteredTransactions);
  }
    
}
