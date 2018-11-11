package behrman.justin.financialmanager.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class AutoCardTransactionsParser {

    public final static String LOG_TAG = AutoCardTransactionsParser.class.getSimpleName() + "debug";

    private HashMap<Date, ArrayList<Transaction>> mapData;
    private ArrayList<Transaction> listData;

    // the length of the response map isn't exactly the size as the arraylist, so a general offset is good to have
    private final int LIST_SIZE_OFFSET = 20;

    public DataCollection parse(HashMap<String, Object> responseMap) {
        int length = (int) responseMap.get(StringConstants.TRANSACTIONS_LENGTH_KEY);
        mapData = new HashMap<>(length);
        listData = new ArrayList<>(length + LIST_SIZE_OFFSET);
        ArrayList<HashMap<String, Object>> transactions = (ArrayList<HashMap<String, Object>>) responseMap.get(StringConstants.TRANSACTIONS_INTENT_KEY);
        parseData(transactions);
        return new DataCollection(listData, mapData);
    }

    private void parseData(ArrayList<HashMap<String, Object>> transactions) {
        for (int i = 0; i < transactions.size(); ++i) {
            Transaction transactionObj = convertToTransaction(transactions.get(i));
            listData.add(transactionObj);
            insertToMap(transactionObj);
        }
    }

    private void insertToMap(Transaction transaction) {
        ArrayList<Transaction> transactionsForDate = mapData.get(transaction.getDate());
        if (transactionsForDate == null) {
            transactionsForDate = new ArrayList<>();
            mapData.put(transaction.getDate(), transactionsForDate);
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
        for (Date date: mapData.keySet()) {
            sb.append(mapData.get(date));
        }
        Log.i(LOG_TAG, sb.toString());

    }

}
