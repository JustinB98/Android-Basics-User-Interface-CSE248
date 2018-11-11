package behrman.justin.financialmanager.model;

import android.util.Log;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ManualCardTransactionParser {

    private final static String LOG_TAG = ManualCardTransactionParser.class.getSimpleName() + "debug";

    private HashMap<Date, ArrayList<Transaction>> mapData;
    private ArrayList<Transaction> listData;

    // the length of the response map isn't exactly the size as the arraylist, so a general offset is good to have
    private final int LIST_SIZE_OFFSET = 20;

    public DataCollection parse(HashMap<String, Object> responseMap) {
        int length = (int) responseMap.get(StringConstants.TRANSACTIONS_LENGTH_KEY);
        mapData = new HashMap<>(length);
        listData = new ArrayList<>(length + LIST_SIZE_OFFSET);
        ArrayList<ParseObject> transactions = (ArrayList<ParseObject>) responseMap.get(StringConstants.TRANSACTIONS_INTENT_KEY);
        parseData(transactions);
        return new DataCollection(listData, mapData);
    }

    private void parseData(ArrayList<ParseObject> transactions) {
        for (int i = 0; i < transactions.size(); ++i) {
            Transaction t = getTransaction(transactions.get(i));
            insertToMap(t);
            listData.add(t);
            listData.add(t);
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

    private Transaction getTransaction(ParseObject object) {
        String place = (String) object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE_COLUMN);
        Date date = (Date) object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE_COLUMN);
        date = reformatDate(date);
        String currencyCode = (String) object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE_COLUMN);
        double amount = getTransactionAmount(object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT_COLUMN));
        String objectId = object.getObjectId();
        Log.i(LOG_TAG, "objectId: " + objectId);
        return new Transaction(place, amount, date, currencyCode, objectId);
    }

    // need to reformat the date so the times aren't compared
    // the date parameter has a time, this method gets rid of that and just
    // has the year, month, and day
    private Date reformatDate(Date date) {
        String dateStr = ProjectUtils.convertDateToString(date);
        String[] dateTokens = dateStr.split("-");
        int year = Integer.parseInt(dateTokens[0]);
        int month = Integer.parseInt(dateTokens[1]);
        int day = Integer.parseInt(dateTokens[2]);
        Log.i(LOG_TAG, year + "-" + month + "-" + day);
        return new GregorianCalendar(year, month - 1, day).getTime();
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
