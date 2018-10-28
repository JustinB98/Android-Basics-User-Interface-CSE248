package behrman.justin.financialmanager.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import behrman.justin.financialmanager.utils.ProjectUtils;

public class CardTransactionsJSON {

    public final static String LOG_TAG = CardTransactionsJSON.class.getSimpleName();

    private HashMap<Date, LinkedList<Transaction>> cardTransactionsMap;

    public CardTransactionsJSON(String jsonResponse) {
        try {
            parseJsonResponse(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "json response couldn't be parsed!", e);
        }
    }

    // ---------------------------- START PARSE JSON ----------------------------
    private void parseJsonResponse(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        int length = root.getInt("length");
        cardTransactionsMap = new HashMap<>(length);
        JSONArray transactions = root.getJSONArray("transactions");
        parseTransactionResponse(transactions);
    }

    private void parseTransactionResponse(JSONArray transactionsArray) throws JSONException {
        for (int i = 0; i < transactionsArray.length(); ++i) {
            JSONObject transaction = transactionsArray.getJSONObject(i);
            Transaction t = extractJSONData(transaction);
            insertTransactionToMap(t);
        }
    }

    private void insertTransactionToMap(Transaction t) {
        LinkedList<Transaction> transactionList = cardTransactionsMap.get(t.getDate());
        if (transactionList == null) {
            transactionList = new LinkedList<>();
            transactionList.add(t);
            cardTransactionsMap.put(t.getDate(), transactionList);
        } else {
            transactionList.add(t);
        }
    }

    private Transaction extractJSONData(JSONObject transaction) throws JSONException {
        String date = transaction.getString("date");
        String currentCode = transaction.getString("iso_currency_code");
        double amount = transaction.getDouble("amount");
        String place = transaction.getString("name");
        Date dateObj = ProjectUtils.convertToDate(date);
        return new Transaction(place, amount, dateObj, currentCode);
    }

    // ---------------------------- END PARSE JSON ----------------------------

    // for debugging
    public void listTransactions() {
        StringBuilder sb = new StringBuilder();
        for (Date date: cardTransactionsMap.keySet()) {
            sb.append(cardTransactionsMap.get(date));
        }

        Log.i(LOG_TAG, sb.toString());

    }

}
