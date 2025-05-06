package model.Export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Transaction;

public class CSVExporter implements TransactionExporter {
    
    private static final String CSV_HEADER = "Amount,Category,Date";
    
    /**
     * Export the transactions to a CSV file
     * 
     * @param transactions List of transactions to export
     * @param file The CSV file to export to
     * @return true if export was successful, false otherwise
     * @throws IOException if there was an error writing to the file
     */
    @Override
    public boolean exportTransactions(List<Transaction> transactions, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(CSV_HEADER);
            
            for (Transaction transaction : transactions) {
                String row = String.format("%f,%s,%s", 
                                           transaction.getAmount(),
                                           transaction.getCategory(),
                                           transaction.getTimestamp());
                writer.println(row);
            }
            
            return true;
        } catch (IOException e) {
            throw e;
        }
    }
}