package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.CardSelecterAdapter;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardReceiever;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardTypeClassConverter;
import behrman.justin.financialmanager.utils.GetCardsUtil;
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
        classConverter = (CardTypeClassConverter) getIntent().getSerializableExtra(StringConstants.NEXT_CLASS_KEY);
        typeToShow = (CardType) getIntent().getSerializableExtra(StringConstants.CARD_TYPE_KEY);
        extractViews();
        setListViewItemListener();
        update();
    }

    private void extractViews() {
        progressBar = findViewById(R.id.progress_bar);
        listView = findViewById(R.id.list_view);
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
        // intent.putExtra(StringConstants.CARD_NAME_KEY, item.getCardName());
        // intent.putExtra(StringConstants.CARD_TYPE, item.getCardType());
        intent.putExtra(StringConstants.CARD_KEY, card);
        startActivity(intent);
    }

    // just in case i decide implement a refresh feature in this activity
    private void update() {
        Log.i(LOG_TAG, "starting update");
        setToLoadView();
        // List<Card> cards = getCards();
        CardReceiever receiver = new CardReceiever() {
            @Override
            public void receiveCards(List<Card> cards) {
                setListViewAdapter(cards);
            }
        };
        if (typeToShow == CardType.AUTO) {
            GetCardsUtil.findAllAutoCards(receiver, this);
        } else if (typeToShow == CardType.MANUAL) {
            GetCardsUtil.findAllManualCards(receiver, this);
        } else {
            GetCardsUtil.findAllCards(receiver, this);
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

    private void setListViewAdapter(List<Card> cards) {
        CardSelecterAdapter adapter = new CardSelecterAdapter(this, cards);
        listView.setAdapter(adapter);
        setToListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        update();
    }

}
