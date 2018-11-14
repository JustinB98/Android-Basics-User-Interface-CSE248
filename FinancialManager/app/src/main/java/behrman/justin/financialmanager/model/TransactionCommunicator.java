package behrman.justin.financialmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface TransactionCommunicator {

    HashMap<Date, ArrayList<Transaction>> getTransactions();

    ArrayList<Transaction> getTransactionAsList();

    /**
     * month is 1-12
     * @param year
     * @param month
     */
    void requestNewTransactions(int year, int month);

    boolean isManualCard();

    BooleanProperty loadingProperty();

    void refresh();

    void setToLoadingScreen();

}
