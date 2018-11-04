package behrman.justin.financialmanager.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class AutoCardTransactionsParser extends HashMap<Date, ArrayList<Transaction>>{

    public final static String LOG_TAG = AutoCardTransactionsParser.class.getSimpleName() + "debug";

    public AutoCardTransactionsParser(HashMap<String, Object> responseMap) {
        super((int) responseMap.get(StringConstants.TRANSACTIONS_LENGTH_KEY));
        ArrayList<HashMap<String, Object>> transactions = (ArrayList<HashMap<String, Object>>) responseMap.get(StringConstants.TRANSACTIONS_INTENT_KEY);
        parseData(transactions);
    }

    private void parseData(ArrayList<HashMap<String, Object>> transactions) {
        for (int i = 0; i < transactions.size(); ++i) {
            Transaction transactionObj = convertToTransaction(transactions.get(i));
            insertToMap(transactionObj);
        }
    }

    private void insertToMap(Transaction transaction) {
        ArrayList<Transaction> transactionsForDate = get(transaction.getDate());
        if (transactionsForDate == null) {
            transactionsForDate = new ArrayList<>();
            put(transaction.getDate(), transactionsForDate);
        }
        transactionsForDate.add(transaction);
    }

    private Transaction convertToTransaction(HashMap<String, Object> transaction) {
        String name = (String) transaction.get(StringConstants.PLAID_PLACE_NAME);
        Date date = getTransactionDate(transaction.get(StringConstants.PLAID_DATE));
        String currencyCode = (String) transaction.get(StringConstants.PLAID_CURRENCY_CODE);
        double amount = getTransactionAmount(transaction.get(StringConstants.PLAID_AMOUNT));
        return new Transaction(name, amount, date, currencyCode);
    }

    private Date getTransactionDate(Object date) {
        String dateStr = (String) date;
        return ProjectUtils.convertToDate(dateStr);
    }

    private double getTransactionAmount(Object amount) {
        if (amount instanceof Integer) {
            int a = (int) amount;
            return a;
        } else if (amount instanceof Double) {
            return (double) amount;
        } else {
            Log.i(LOG_TAG, "unknown case, amount is not int or double");
            return 0; // unknown case
        }
    }

    // for debugging
    public void listTransactions() {
        StringBuilder sb = new StringBuilder();
        for (Date date: keySet()) {
            sb.append(get(date));
        }
        Log.i(LOG_TAG, sb.toString());

    }

}
