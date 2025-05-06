import controller.ExpenseTrackerController;
import javax.swing.JOptionPane;
import model.ExpenseTrackerModel;
import model.Export.CSVExporter;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;
import view.ExpenseTrackerView;

public class ExpenseTrackerApp {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    // Create MVC components
    ExpenseTrackerModel model = new ExpenseTrackerModel();
    ExpenseTrackerView view = new ExpenseTrackerView();
    ExpenseTrackerController controller = new ExpenseTrackerController(model, view);
    
    controller.setExporter(new CSVExporter());

    // Initialize view
    view.setVisible(true);

    // Handle add transaction button clicks
    view.getAddTransactionBtn().addActionListener(e -> {
      // Get transaction data from view
      double amount = view.getAmountField();
      String category = view.getCategoryField();
      
      // Call controller to add transaction
      boolean added = controller.addTransaction(amount, category);
      
      if (!added) {
        JOptionPane.showMessageDialog(view, "Invalid amount or category entered");
        view.toFront();
      }
    });

    // Handle remove transaction button clicks
    view.getRemoveTransactionBtn().addActionListener(e -> {
      boolean removed = controller.removeTransaction();
      
      if (!removed) {
        JOptionPane.showMessageDialog(view, "Please select a valid transaction to remove");
        view.toFront();
      }
    });

    // Handle export to CSV button clicks
    view.getExportToCSVBtn().addActionListener(e -> {
      String fileName = view.getExportFileName();
      
      if (fileName != null && !fileName.isEmpty()) {
        boolean exported = controller.exportTransactions(fileName);
        
        if (exported) {
          JOptionPane.showMessageDialog(view, 
              "Transactions exported successfully to " + fileName,
              "Export Successful", 
              JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(view, 
              "Failed to export transactions. Ensure there are transactions to export.",
              "Export Failed", 
              JOptionPane.ERROR_MESSAGE);
        }
        view.toFront();
      }
    });

      // Add action listener to the "Apply Category Filter" button
    view.addApplyCategoryFilterListener(e -> {
      try{
      String categoryFilterInput = view.getCategoryFilterInput();
      CategoryFilter categoryFilter = new CategoryFilter(categoryFilterInput);
      if (categoryFilterInput != null) {
          // controller.applyCategoryFilter(categoryFilterInput);
          controller.setFilter(categoryFilter);
          controller.applyFilter();
      }
     }catch(IllegalArgumentException exception) {
    JOptionPane.showMessageDialog(view, exception.getMessage());
    view.toFront();
   }});


    // Add action listener to the "Apply Amount Filter" button
    view.addApplyAmountFilterListener(e -> {
      try{
      double amountFilterInput = view.getAmountFilterInput();
      AmountFilter amountFilter = new AmountFilter(amountFilterInput);
      if (amountFilterInput != 0.0) {
          controller.setFilter(amountFilter);
          controller.applyFilter();
      }
    }catch(IllegalArgumentException exception) {
    JOptionPane.showMessageDialog(view,exception.getMessage());
    view.toFront();
   }});


   // Add action listener to the "Clear Filter" button
   view.addClearFilterListener(e -> {
     controller.setFilter(null);
     controller.applyFilter();
   });
    
  }
}
