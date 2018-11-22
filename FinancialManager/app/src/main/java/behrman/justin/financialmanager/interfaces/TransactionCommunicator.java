package behrman.justin.financialmanager.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import behrman.justin.financialmanager.model.BooleanProperty;
import behrman.justin.financialmanager.model.Transaction;

public interface TransactionCommunicator {

    HashMap<Date, ArrayList<Transaction>> getTransactions();

    ArrayList<Transaction> getTransactionAsList();

    /**
     * month is 1-12
     * @param year
     * @param month
     */
    void requestNewTransactions(int year, int month);

    void regetTransactions();

    boolean isManualCard();

    BooleanProperty loadingProperty();

    void refresh();

    void setToLoadingScreen();

    double getTotal();

}
