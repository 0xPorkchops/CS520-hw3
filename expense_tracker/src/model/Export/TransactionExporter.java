package model.Export;

import java.io.File;
import java.io.IOException;
import java.util.List;
import model.Transaction;

public interface TransactionExporter {
    
    /**
     * Export transactions to a file
     * 
     * @param transactions List of transactions to export
     * @param file The file to export to
     * @return true if export was successful, false otherwise
     * @throws IOException if there was an error writing to the file
     */
    boolean exportTransactions(List<Transaction> transactions, File file) throws IOException;
}