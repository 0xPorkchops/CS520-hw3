# Export to CSV Feature Design Plan

### Model
#### TransactionExporter
```
public interface TransactionExporter {
    public boolean exportTransactions(List<Transaction> transactions, File file) throws IOException;
}
```
- This is an interface for exporting transactions. It doesn't specify the file type to allow for multiple supported file export types.

#### CSVExporter
```
public class CSVExporter implements TransactionExporter {
    public boolean exportTransactions(List<Transaction> transactions, File file) throws IOException;
}
```
- This is an implementation of the TransactionExporter interface which exports the transactions list as a CSV file.

### Controller
#### ExpenseTrackerController
```
public class ExpenseTrackerController {
    public void setExporter(TransactionExporter exporter);
    public boolean exportTransactions(String fileName);
}
```
- Adds a setter for the type of exporter to use. It uses the isValidName method in InputValidation to validate the export file name, then uses the exporter to export to a file. The exporter is passed via a setter instead of the constructor to allow different file types at runtime in the future.

#### InputValidation
```
  public static boolean isValidName(String fileName);
```
- Validates the export file name (not null, erroneous characters, etc.)

### View
#### ExpenseTrackerView
```
public class ExpenseTrackerView {
    JButton getExportToCsvBtn();
    String getExportFileName();
}
```
- Adds an export button and a method to open a dialog for entering the CSV file name.

### App
#### ExpenseTrackerApp
```
public class ExpenseTrackerApp {
    view.getExportToCSVBtn().addActionListener(e -> {
        // ...
    }
}
```
- Adds a listener to the export button that opens a dialog from the view to enter a file name and uses the controller to export the transactions. If there are any errors, it shows the user.

## Requirement Satisfaction

### Feature Requirements
1. **Output File Name Specification**:
   - The user specifies the output file name via a dialog prompt in the view (getExportFileName method).
   - File name validation ensures the input is valid.
   - The .csv extension is automatically added if missing.

2. **CSV File Format**:
   - First line contains column headers: "Amount,Category,Date"
   - Each subsequent line contains one transaction with appropriate formatting.

### Design Requirements

#### MVC Architecture
- **Model**: The TransactionExporter interface and CSVExporter implementation handle data transformation and file operations.
- **View**: Dialog prompts for file name input and feedback messages for operation results.
- **Controller**: Coordinates the export process by getting transactions from the model and sending them to the exporter.

#### UI Design Laws
- **Feedback**: Success/failure messages inform the user about the operation outcome.
- **Error Prevention**: Automatic .csv extension addition and input validation prevent errors.
- **Help**: Dialog includes guidance text to assist users with correct file name entry.

#### OO Design Principles
- **Open-Closed Principle**: The TransactionExporter interface allows adding new export formats (e.g., XML, JSON) without modifying existing code. New exporters can be implemented and plugged in.
- **Single Responsibility**: Each component has a clear, focused purpose (file I/O, input validation, UI interaction).

#### Best Practices
- **No Magic Strings**: Constants are used for headers, file extensions, and user messages.
- **Proper Exception Handling**: I/O exceptions are caught and handled appropriately with user feedback.
- **Input Validation**: File name is validated before export operations are attempted.