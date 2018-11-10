package behrman.justin.financialmanager.utils;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardReceiever;
import behrman.justin.financialmanager.model.CardType;

public class GetCardsUtil {

    public final static String LOG_TAG = GetCardsUtil.class.getSimpleName() + "debug";

    public static void findAllCards(final CardReceiever onReceive) {
        CardReceiever next = new CardReceiever() {
            @Override
            public void receiveCards(List<Card> cards) {
                findAllCards0(cards, onReceive);
            }
        };
        findAllAutoCards(next);
    }

    private static void findAllCards0(final List<Card> autoCards, final CardReceiever onReceive) {
        CardReceiever next = new CardReceiever() {
            @Override
            public void receiveCards(List<Card> cards) {
                List<Card> newList = ProjectUtils.combineLists(autoCards, cards);
                onReceive.receiveCards(newList);
            }
        };
        findAllManualCards(next);
    }


    public static void findAllManualCards(CardReceiever onReceive) {
        findCards(StringConstants.MANUAL_CARD_CLASS_NAME, onReceive);
    }

    public static void findAllAutoCards(CardReceiever onReceive) {
        findCards(StringConstants.AUTO_CARD_CLASS_NAME, onReceive);
    }

    private static void findCards(String className, final CardReceiever onReceive) {
        ParseQuery<ParseObject> manualCardQuery = ParseQuery.getQuery(className);
        manualCardQuery.whereEqualTo(StringConstants.DATABASE_CARD_OWNER_COLUMN, ParseUser.getCurrentUser());
        manualCardQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<Card> cardList = fillCards(objects);
                onReceive.receiveCards(cardList);
            }
        });
    }

    private static Card convertParseObjectToCard(ParseObject obj) {
        CardType cardType = ProjectUtils.convertToCardType(obj.getClassName());
        String name = obj.getString(StringConstants.DATABASE_CARD_NAME_COLUMN);
        return new Card(name, cardType);
    }

    private static ArrayList<Card> fillCards(List<ParseObject> objects) {
        ArrayList<Card> cards = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); ++i) {
            Card card = convertParseObjectToCard(objects.get(i));
            cards.add(card);
        }
        return cards;
    }

}
