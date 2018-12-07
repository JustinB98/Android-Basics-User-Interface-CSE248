package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.SelectableCardAdapter;
import behrman.justin.financialmanager.interfaces.Retriable;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardWrapper;
import behrman.justin.financialmanager.model.RetryHandler;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class MonthlyCalculationsActivity extends AppCompatActivity implements Observer, Retriable {

    public final static String LOG_TAG = MonthlyCalculationsActivity.class.getSimpleName() + "debug";

    private ProgressBar progressBar;
    private Button actionBtn;
    private EditText yearField;
    private Spinner monthSpinner;
    private ListView listView;
    private TextView noCardsFoundView;
    private View container;

    private List<Card> cards;

    private View root;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = getLayoutInflater().inflate(R.layout.activity_monthly_calculations, null);
        setContentView(root);
        extractViews();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE); // APPARENTLY IMPORTANT DON'T REMOVE
        setViewData();
        initClickListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSwipeListener();
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

    private void initSwipeListener() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CardWrapper.getInstance().refresh(MonthlyCalculationsActivity.this);
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

    private void setViewData() {
        int currentMonth = ProjectUtils.getCurrentMonth();
        int currentYear = ProjectUtils.getCurrentYear();
        monthSpinner.setSelection(currentMonth - 1);
        yearField.setText(currentYear + "");
    }

    private void initClickListener() {
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractData();
            }
        });
    }

    private void extractData() {
        LinkedList<Card> selectedCards = getSelectedCards();
        if (dataIsGood(selectedCards)) {
            switchActivities(selectedCards);
        }
    }

    private LinkedList<Card> getSelectedCards() {
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        LinkedList<Card> selectedCards = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                selectedCards.add(cards.get(i));
            }
        }
        return selectedCards;
    }

    private boolean dataIsGood(LinkedList<Card> selectedCards) {
        if (ProjectUtils.isEmpty(yearField.getText())) {
            Toast.makeText(this, R.string.year_is_empty_text, Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedCards.isEmpty()) {
            Toast.makeText(this, R.string.no_cards_selected_text, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private void switchActivities(LinkedList<Card> cards) {
        Intent intent = new Intent(this, MonthlyCalculationResultsActivity.class);
        intent.putExtra(StringConstants.MONTH_SPINNER_POSITION_KEY, monthSpinner.getSelectedItemPosition());
        int year = Integer.parseInt(yearField.getText().toString());
        intent.putExtra(StringConstants.YEAR_KEY, year);
        intent.putExtra(StringConstants.SELECTED_CARDS_KEY, cards);
        startActivity(intent);
    }

    private void update() {
        swipeRefresh.setRefreshing(false);
        onReceiveCards(CardWrapper.getInstance().getAllCards());
    }

    private void onReceiveCards(List<Card> cards) {
        this.cards = cards;
        if (cards != null) {
            if (!cards.isEmpty()) {
                setUpAdapter();
            } else {
                setToNoCardsFoundView();
            }
        }
    }

    private void setUpAdapter() {
        setToReadyView();
        SelectableCardAdapter adapter = new SelectableCardAdapter(MonthlyCalculationsActivity.this, cards, listView);
        listView.setAdapter(adapter);
    }

    private void setToNoCardsFoundView() {
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.GONE);
        noCardsFoundView.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    private void setToLoadView() {
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        noCardsFoundView.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
    }

    private void setToReadyView() {
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        noCardsFoundView.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    private void extractViews() {
        progressBar = findViewById(R.id.progress_bar);
        actionBtn = findViewById(R.id.action_btn);
        yearField = findViewById(R.id.year_field);
        monthSpinner = findViewById(R.id.month_spinner);
        listView = findViewById(R.id.list_view);
        noCardsFoundView = findViewById(R.id.no_cards_found_view);
        container = findViewById(R.id.container);
        swipeRefresh = findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CardWrapper.getInstance().deleteObserver(this);
    }

    @Override
    public void retry() {
        setContentView(root);
        update();
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
