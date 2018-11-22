package behrman.justin.financialmanager.model;

import android.util.Log;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import behrman.justin.financialmanager.utils.ParseUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ManualCardTransactionParser {

    private final static String LOG_TAG = ManualCardTransactionParser.class.getSimpleName() + "debug";

    private HashMap<Date, ArrayList<Transaction>> mapData;
    private ArrayList<Transaction> listData;
    private double total;

    // the length of the response map isn't exactly the size as the arraylist, so a general offset is good to have
    private final int LIST_SIZE_OFFSET = 20;

    public DataCollection parse(HashMap<String, Object> responseMap) {
        int length = (int) responseMap.get(StringConstants.TRANSACTIONS_LENGTH_KEY);
        mapData = new HashMap<>(length);
        listData = new ArrayList<>(length + LIST_SIZE_OFFSET);
        ArrayList<ParseObject> transactions = (ArrayList<ParseObject>) responseMap.get(StringConstants.TRANSACTIONS_INTENT_KEY);
        parseData(transactions);
        return new DataCollection(listData, mapData, total);
    }

    private void parseData(ArrayList<ParseObject> transactions) {
        for (int i = 0; i < transactions.size(); ++i) {
            Transaction t = ParseUtils.getManualTransaction(transactions.get(i));
            insertToMap(t);
            listData.add(t);
            total += t.getAmount();
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

    // for debugging
    public void listTransactions() {
        StringBuilder sb = new StringBuilder();
        for (Date date: mapData.keySet()) {
            sb.append(mapData.get(date));
        }
        Log.i(LOG_TAG, sb.toString());
    }

}
