package behrman.justin.financialmanager.model;

import android.util.Log;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;

import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ManualCardTransactionParser extends HashMap<Date, LinkedList<Transaction>> {

    private final static String LOG_TAG = ManualCardTransactionParser.class.getSimpleName() + "debug";

    public ManualCardTransactionParser(HashMap<String, Object> responseMap) {
        super((int) responseMap.get(StringConstants.TRANSACTIONS_LENGTH));
        ArrayList<ParseObject> transactions = (ArrayList<ParseObject>) responseMap.get(StringConstants.TRANSACTIONS_KEY);
        parseData(transactions);
    }

    private void parseData(ArrayList<ParseObject> transactions) {
        for (int i = 0; i < transactions.size(); ++i) {
            Transaction t = getTransaction(transactions.get(i));
            insertToMap(t);
        }
    }

    private void insertToMap(Transaction transaction) {
        LinkedList<Transaction> transactionsForDate = get(transaction.getDate());
        if (transactionsForDate == null) {
            transactionsForDate = new LinkedList<>();
            put(transaction.getDate(), transactionsForDate);
        }
        transactionsForDate.add(transaction);
    }

    private Transaction getTransaction(ParseObject object) {
        String place = (String) object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE);
        Date date = (Date) object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE);
        date = reformatDate(date);
        String currencyCode = (String) object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE);
        double amount = getTransactionAmount(object.get(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT));
        return new Transaction(place, amount, date, currencyCode);
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
        for (Date date: keySet()) {
            sb.append(get(date));
        }
        Log.i(LOG_TAG, sb.toString());

    }

}
