package behrman.justin.financialmanager.utils;

import android.content.Context;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardReceiever;
import behrman.justin.financialmanager.model.CardType;

public class GetCardsUtil {

    public final static String LOG_TAG = GetCardsUtil.class.getSimpleName() + "debug";

    private final static HashMap<String, Object> EMPTY_MAP = new HashMap<>(0);

    public static void findAllCards(final CardReceiever onReceive, final Context context) {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_GET_ALL_CARDS, EMPTY_MAP, new FunctionCallback<ArrayList<ParseObject>>() {
            @Override
            public void done(ArrayList<ParseObject> object, ParseException e) {
                afterFind(object, e, onReceive, context);
            }
        });
    }

    public static void findAllManualCards(final CardReceiever onReceive, final Context context) {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_GET_ALL_MANUAL_CARDS, EMPTY_MAP, new FunctionCallback<ArrayList<ParseObject>>() {
            @Override
            public void done(ArrayList<ParseObject> object, ParseException e) {
                afterFind(object, e, onReceive, context);
            }
        });
    }

    public static void findAllAutoCards(final CardReceiever onReceive, final Context context) {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_GET_ALL_AUTO_CARDS, EMPTY_MAP, new FunctionCallback<ArrayList<ParseObject>>() {
            @Override
            public void done(ArrayList<ParseObject> object, ParseException e) {
                afterFind(object, e, onReceive, context);
            }
        });
    }

    private static void afterFind(List<ParseObject> objects, ParseException e, CardReceiever onReceive, Context context) {
        if (e == null) {
            onReceive.receiveCards(fillCards(objects));
        } else {
            Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
            ParseExceptionUtils.displayErrorMessage(e, context);
        }
    }


    private static ArrayList<Card> fillCards(List<ParseObject> objects) {
        ArrayList<Card> cards = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); ++i) {
            Card card = convertParseObjectToCard(objects.get(i));
            cards.add(card);
        }
        return cards;
    }

    private static Card convertParseObjectToCard(ParseObject obj) {
        CardType cardType = ProjectUtils.convertToCardTypeFromClass(obj.getClassName());
        String name = obj.getString(StringConstants.DATABASE_CARD_NAME_COLUMN);
        return new Card(name, cardType);
    }

}
