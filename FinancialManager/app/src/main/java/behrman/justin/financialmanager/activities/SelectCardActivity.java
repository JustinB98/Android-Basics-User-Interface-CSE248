package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.CardSelecterAdapter;
import behrman.justin.financialmanager.interfaces.CardTypeClassConverter;
import behrman.justin.financialmanager.interfaces.Retriable;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.CardWrapper;
import behrman.justin.financialmanager.model.RetryHandler;
import behrman.justin.financialmanager.utils.StringConstants;

public class SelectCardActivity extends AppCompatActivity implements Observer, Retriable {

    private String LOG_TAG = SelectCardActivity.class.getSimpleName() + "debug";

    private ListView listView;
    private ProgressBar progressBar;
    private TextView noCardsFoundView;

    private CardTypeClassConverter classConverter;

    // if null show all cards
    private CardType typeToShow;

    private View root;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = getLayoutInflater().inflate(R.layout.activity_select_card, null);
        setContentView(root);
        classConverter = (CardTypeClassConverter) getIntent().getSerializableExtra(StringConstants.NEXT_CLASS_KEY);
        typeToShow = (CardType) getIntent().getSerializableExtra(StringConstants.CARD_TYPE_KEY);
        extractViews();
        setListViewItemListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // swipeRefresh.setRefreshing(true);
        initRefreshListener();
        initCardWrapper();
    }

    private void initCardWrapper() {
        CardWrapper.getInstance().addObserver(this);
        if (!CardWrapper.getInstance().isLoading()) {
            update();
        } else {
            setToLoadView();
        }
    }

    private void initRefreshListener() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CardWrapper.getInstance().refresh(SelectCardActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractViews() {
        progressBar = findViewById(R.id.progress_bar);
        listView = findViewById(R.id.list_view);
        noCardsFoundView = findViewById(R.id.no_cards_found_view);
        swipeRefresh = findViewById(R.id.swipe_refresh);
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

    private void update() {
        swipeRefresh.setRefreshing(false);
        List<Card> listToShow = getListToShow();
        setListViewAdapter(listToShow);
    }

    private List<Card> getListToShow() {
        if (typeToShow == CardType.MANUAL) {
            return CardWrapper.getInstance().getManualCards();
        } else if (typeToShow == CardType.AUTO) {
            return CardWrapper.getInstance().getAutoCards();
        } else {
            return CardWrapper.getInstance().getAllCards();
        }
    }

    private void setToLoadView() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noCardsFoundView.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
    }

    private void setToListView() {
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        noCardsFoundView.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    private void setListViewAdapter(List<Card> cards) {
        if (cards != null) {
            if (cards.isEmpty()) {
                noCardsFoundView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setVisibility(View.VISIBLE);
            } else {
                CardSelecterAdapter adapter = new CardSelecterAdapter(this, cards);
                listView.setAdapter(adapter);
                setToListView();
            }
        }
    }

    @Override
    public void retry() {
        setContentView(root);
        setToLoadView();
        CardWrapper.getInstance().refresh(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CardWrapper.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (CardWrapper.getInstance().hasError()) {
            Log.i(LOG_TAG, "there was an error");
            RetryHandler.setToRetryScreen(this, this);
        } else {
            update();
        }
    }
}
