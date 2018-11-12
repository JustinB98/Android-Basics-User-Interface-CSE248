package behrman.justin.financialmanager.utils;

import android.util.Log;

import com.parse.ParseObject;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import behrman.justin.financialmanager.model.Transaction;

public class ParseUtils {

    public final static String LOG_TAG = ParseUtils.class.getSimpleName() + "debug";

    // ------------------------ MANUAL ------------------------

    public static Transaction getManualTransaction(ParseObject object) {
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
    private static Date reformatDate(Date date) {
        String dateStr = ProjectUtils.convertDateToString(date);
        String[] dateTokens = dateStr.split("-");
        int year = Integer.parseInt(dateTokens[0]);
        int month = Integer.parseInt(dateTokens[1]);
        int day = Integer.parseInt(dateTokens[2]);
        Log.i(LOG_TAG, year + "-" + month + "-" + day);
        return new GregorianCalendar(year, month - 1, day).getTime();
    }

    public static double getTransactionAmount(Object amount) {
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

    // ------------------------ AUTO ------------------------

    public static Transaction getAutoTransaction(HashMap<String, Object> transaction) {
        String name = (String) transaction.get(StringConstants.PLAID_PLACE_NAME);
        Date date = getTransactionDate(transaction.get(StringConstants.PLAID_DATE));
        String currencyCode = (String) transaction.get(StringConstants.PLAID_CURRENCY_CODE);
        double amount = getTransactionAmount(transaction.get(StringConstants.PLAID_AMOUNT));
        return new Transaction(name, amount, date, currencyCode);
    }

    private static Date getTransactionDate(Object date) {
        String dateStr = (String) date;
        return ProjectUtils.convertToDate(dateStr);
    }

}
