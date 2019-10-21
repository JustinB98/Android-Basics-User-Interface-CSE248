package behrman.justin.financialmanager.model;

import android.util.Log;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.utils.ParseUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class CardsWithTransactionsParser {

    public final static String LOG_TAG = CardsWithTransactionsParser.class.getSimpleName() + "debug";

    private HashMap<Card, List<Transaction>> childData;
    private ArrayList<Card> groupData;
    private ArrayList<Double> totals;

    public CardsWithTransactionsParser(ArrayList<Object> responseTransactions, int length) {
        childData = new HashMap<>(length);
        groupData = new ArrayList<>(length);
        totals = new ArrayList<>(length);
        parseData(responseTransactions);
    }

    public HashMap<Card, List<Transaction>> getChildData() {
        return childData;
    }

    public ArrayList<Card> getGroupData() {
        return groupData;
    }

    public ArrayList<Double> getTotals() {
        return totals;
    }


    private void parseData(List<Object> response) {
        for (int i = 0; i < response.size(); ++i) {
            HashMap<String, Object> cardData = (HashMap<String, Object>) response.get(i);
            Card card = parseCard(cardData);
            groupData.add(card);
            parseTransactions(card, cardData);
        }
    }

    private void parseTransactions(Card card, HashMap<String, Object> cardData) {
        if (card.getCardType() == CardType.MANUAL) {
            parseManualTransactions(card, cardData);
        } else if (card.getCardType() == CardType.AUTO) {
            parseAutoTransactions(card, cardData);
        } else {
            Log.i(LOG_TAG, "unknown case: " + card.getCardType() + ", returning null...");
        }
    }

    private void parseManualTransactions(Card card, HashMap<String, Object> cardData) {
        ArrayList<ParseObject> transactionData = (ArrayList<ParseObject>) cardData.get(StringConstants.TRANSACTION_DATA_KEY);
        ArrayList<Transaction> formattedData = new ArrayList<>(transactionData.size());
        double total = 0;
        for (int i = 0; i < transactionData.size(); ++i) {
            Transaction t = ParseUtils.getManualTransaction(transactionData.get(i));
            formattedData.add(t);
            total += t.getAmount();
        }
        Collections.sort(formattedData);
        totals.add(total);
        childData.put(card, formattedData);
    }

    private void parseAutoTransactions(Card card, HashMap<String, Object> cardData) {
        ArrayList<HashMap<String, Object>> transactionData = (ArrayList<HashMap<String, Object>>) cardData.get(StringConstants.TRANSACTION_DATA_KEY);
        ArrayList<Transaction> formattedData = new ArrayList<>(transactionData.size());
        double total = 0;
        for (int i = 0; i < transactionData.size(); ++i) {
            Transaction t = ParseUtils.getAutoTransaction(transactionData.get(i));
            formattedData.add(t);
            total += t.getAmount();
        }
        Collections.sort(formattedData);
        totals.add(total);
        childData.put(card, formattedData);
    }

    private Card parseCard(HashMap<String, Object> cardData) {
        String cardName = (String) cardData.get(StringConstants.CARD_NAME_KEY);
        String cardType = (String) cardData.get(StringConstants.CARD_TYPE_KEY);
        return new Card(cardName, ProjectUtils.convertToCardType(cardType));
    }

}
