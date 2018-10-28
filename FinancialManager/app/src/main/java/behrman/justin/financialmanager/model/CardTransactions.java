package behrman.justin.financialmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class CardTransactions {

    private HashMap<Date, LinkedList<Transaction>> transactionMap;
    private int length;

    public CardTransactions(HashMap<String, Object> responseMap) {
        length = (int) responseMap.get(StringConstants.TRANSACTIONS_LENGTH);
        transactionMap = new HashMap<>(length);
        ArrayList<HashMap<String, Object>> transactions = (ArrayList<HashMap<String, Object>>) responseMap.get(StringConstants.TRANSACTIONS_KEY);
        parseData(transactions);
    }

    private void parseData(ArrayList<HashMap<String, Object>> transactions) {
        for (int i = 0; i < transactions.size(); ++i) {
            Transaction transactionObj = convertToTransaction(transactions.get(i));
            insertToMap(transactionObj);
        }
    }

    private void insertToMap(Transaction transaction) {
        LinkedList<Transaction> transactionsForDate = transactionMap.get(transaction.getDate());
        if (transactionsForDate == null) {
            transactionsForDate = new LinkedList<>();
            transactionMap.put(transaction.getDate(), transactionsForDate);
        }
        transactionsForDate.add(transaction);
    }

    private Transaction convertToTransaction(HashMap<String, Object> transaction) {
        String name = (String) transaction.get(StringConstants.PLAID_PLACE_NAME);
        String dateStr = (String) transaction.get(StringConstants.PLAID_DATE);
        String currencyCode = (String) transaction.get(StringConstants.PLAID_CURRENCY_CODE);
        double amount = (double) transaction.get(StringConstants.PLAID_AMOUNT);
        Date date = ProjectUtils.convertToDate(dateStr);
        return new Transaction(name, amount, date, currencyCode);
    }

}
