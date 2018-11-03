package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.adapters.CardSelecterAdapter;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardTypeClassConverter;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class SelectCardActivity extends AppCompatActivity {

    private String LOG_TAG = SelectCardActivity.class.getSimpleName() + "debug";

    private ListView listView;
    private ProgressBar progressBar;

    private CardTypeClassConverter classConverter;

    // if null show all cards
    private CardType typeToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);
        classConverter = (CardTypeClassConverter) getIntent().getSerializableExtra(StringConstants.NEXT_CLASS);
        typeToShow = (CardType) getIntent().getSerializableExtra(StringConstants.CARD_TYPE_KEY);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        listView = (ListView) findViewById(R.id.list_view);
        setListViewItemListener();
        update();
    }

    private void setListViewItemListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card itemSelected = (Card) listView.getAdapter().getItem(position);
                switchLayout(itemSelected);
            }
        });
    }

    private void switchLayout(Card item) {
        Class<?> classToOpen = classConverter.convertCardTypeToClass(item.getCardType());
        Intent intent = new Intent(this, classToOpen);
        Card card = new Card(item.getCardName(), item.getCardType());
        // intent.putExtra(StringConstants.CARD_NAME, item.getCardName());
        // intent.putExtra(StringConstants.CARD_TYPE, item.getCardType());
        intent.putExtra(StringConstants.CARD_KEY, card);
        startActivity(intent);
    }

    // just in case i decide implement a refresh feature in this activity
    private void update() {
        Log.i(LOG_TAG, "starting update");
        setToLoadView();
        // List<Card> cards = getCards();
        if (typeToShow == CardType.AUTO) {
            findAutoCards(null);
        } else {
            findManualCards();
        }
    }

    private void setToLoadView() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setToListView() {
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void findManualCards() {
        ParseQuery<ParseObject> manualCardQuery = ParseQuery.getQuery(StringConstants.MANUAL_CARD_CLASS);
        manualCardQuery.whereEqualTo(StringConstants.MANUAL_CARD_OWNER, ParseUser.getCurrentUser());
        Log.i(LOG_TAG, "starting manual find");
        manualCardQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.i(LOG_TAG, "found manual objects " + (objects == null ? "null" : objects.size()));
                if (typeToShow == null) {
                    findAutoCards(objects);
                } else {
                    afterFind(objects, e); // if we don't need to look for auto cards, then just set the list view
                }
            }
        });
    }

    private void findAutoCards(final List<ParseObject> manualObjects) {
        ParseQuery<ParseObject> autoCardQuery = ParseQuery.getQuery(StringConstants.AUTO_CARD_CLASS);
        autoCardQuery.whereEqualTo(StringConstants.AUTO_CARD_OWNER, ParseUser.getCurrentUser());
        Log.i(LOG_TAG, "starting auto card query");
        autoCardQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.i(LOG_TAG, "found auto objects " + (objects == null ? "null" : objects.size()));
                Log.i(LOG_TAG, e == null ? "null" : e.toString());
                if (typeToShow == null && manualObjects != null) {
                    objects.addAll(manualObjects);
                }
                afterFind(objects, e);
            }
        });
    }

    private void afterFind(List<ParseObject> objects, ParseException e) {
        if (e == null) {
            ArrayList<Card> cards = new ArrayList<>(objects.size());
            fillCards(cards, objects);
            setListViewAdapter(cards);
        } else {
            Log.i(LOG_TAG, "ERROR", e);
            Toast.makeText(this, "ERROR " + e.toString(), Toast.LENGTH_LONG);
            setToListView();
        }
    }

    private void setListViewAdapter(ArrayList<Card> cards) {
        CardSelecterAdapter adapter = new CardSelecterAdapter(this, cards);
        listView.setAdapter(adapter);
        setToListView();
    }

    private void fillCards(ArrayList<Card> cards, List<ParseObject> objects) {
        for (int i = 0; i < objects.size(); ++i) {
            Card card = convertParseObjectToCard(objects.get(i));
            cards.add(card);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        update();
    }

    private Card convertParseObjectToCard(ParseObject obj) {
        CardType cardType = ProjectUtils.convertToCardType(obj.getClassName());
        String key = cardType == CardType.AUTO ? StringConstants.AUTO_CARD_NAME : StringConstants.MANUAL_CARD_NAME;
        String name = obj.getString(key);
        return new Card(name, cardType);
    }

}
