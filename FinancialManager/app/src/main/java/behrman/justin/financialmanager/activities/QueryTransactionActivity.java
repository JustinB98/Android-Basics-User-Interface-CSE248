package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
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

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.SelectableCardAdapter;
import behrman.justin.financialmanager.interfaces.CardReceiever;
import behrman.justin.financialmanager.interfaces.Retriable;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.utils.GetCardsUtil;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class QueryTransactionActivity extends AppCompatActivity implements Retriable {

    private EditText placeField, minAmountField, maxAmountField, minYearField, maxYearField;
    private Spinner minMonthSpinner, maxMonthSpinner;
    private ListView listView;
    private ProgressBar progressBar;
    private View container;
    private Button queryTransactionsBtn;
    private TextView noCardsFoundView;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = getLayoutInflater().inflate(R.layout.activity_query_transaction, null);
        setContentView(root);
        extractViews();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE); // IMPORTANT DON'T REMOVE
        initClick();
        initGetCards();
    }

    private void initGetCards() {
        setToLoading();
        GetCardsUtil.findAllCards(new CardReceiever() {
            @Override
            public void receiveCards(List<Card> cards) {
                onReceiveCards(cards);
            }
        }, QueryTransactionActivity.this, this);
    }

    private void onReceiveCards(List<Card> cards) {
        if (cards != null) {
            if (!cards.isEmpty()) {
                setUpAdapter(cards);
            } else {
                setToNoCardsView();
            }
        }
    }

    private void setToNoCardsView() {
        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        noCardsFoundView.setVisibility(View.VISIBLE);
    }

    private void setUpAdapter(List<Card> cards) {
        SelectableCardAdapter adapter = new SelectableCardAdapter(QueryTransactionActivity.this, cards, listView);
        listView.setAdapter(adapter);
        setToView();
    }

    private void initClick() {
        queryTransactionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForGoodInput();
            }
        });
    }

    private void checkForGoodInput() {
        if (isDateGood() && isAmountGood()) {
            checkForGoodInput0();
        }
    }

    private void checkForGoodInput0() {
        LinkedList<Card> selectedCards = getSelectedCards();
        if (!selectedCards.isEmpty()) {
            switchToResultsActivity(selectedCards);
        } else {
            Toast.makeText(this, R.string.no_cards_selected_text, Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToResultsActivity(LinkedList<Card> selectedCards) {
        Intent intent = new Intent(this, QueryTransactionsResultActivity.class);
        insertDataToIntent(intent, selectedCards);
        startActivity(intent);
    }

    private void insertDataToIntent(Intent intent, LinkedList<Card> selectedCards) {
        intent.putExtra(StringConstants.PLACE_KEY, ProjectUtils.normalizeString(placeField));
        intent.putExtra(StringConstants.MIN_AMOUNT_KEY, ProjectUtils.parseOrDefault(minAmountField, -1.0));
        intent.putExtra(StringConstants.MAX_AMOUNT_KEY, ProjectUtils.parseOrDefault(maxAmountField, -1.0));
        intent.putExtra(StringConstants.MIN_YEAR_KEY, Integer.parseInt(minYearField.getText().toString()));
        intent.putExtra(StringConstants.MAX_YEAR_KEY, Integer.parseInt(maxYearField.getText().toString()));
        intent.putExtra(StringConstants.MIN_MONTH_KEY, minMonthSpinner.getSelectedItemPosition() + 1);
        intent.putExtra(StringConstants.MAX_MONTH_KEY, maxMonthSpinner.getSelectedItemPosition() + 1);
        intent.putExtra(StringConstants.CARDS_KEY, selectedCards);
    }

    private boolean isAmountGood() {
        // mark it as some value that amounts don't matter
        double minAmount = ProjectUtils.parseOrDefault(minAmountField, -1.0);
        double maxAmount = ProjectUtils.parseOrDefault(maxAmountField, -1.0);
        if (minAmount > maxAmount) {
            Toast.makeText(this, R.string.min_amount_larger_than_max_amount, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean isDateGood() {
        String minYear = ProjectUtils.normalizeString(minYearField.getText());
        String maxYear = ProjectUtils.normalizeString(maxYearField.getText().toString());
        if (minYear.isEmpty() || maxYear.isEmpty()) {
            Toast.makeText(this, R.string.no_year_inputs, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            int minMonth = minMonthSpinner.getSelectedItemPosition();
            int maxMonth = maxMonthSpinner.getSelectedItemPosition();
            int minYearNum = Integer.parseInt(minYearField.getText().toString());
            int maxYearNum = Integer.parseInt(maxYearField.getText().toString());
            return isDateGood0(minMonth, minYearNum, maxMonth, maxYearNum);
        }
    }

    private boolean isDateGood0(int minMonth, int minYear, int maxMonth, int maxYear) {
        if (maxYear < minYear) {
            Toast.makeText(this, R.string.min_year_larger_than_max_year, Toast.LENGTH_SHORT).show();
            return false;
        } else if (maxYear == minYear && minMonth > maxMonth) { // same year, min month is larger than max month
            Toast.makeText(this, R.string.min_month_larger_than_max_month, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private LinkedList<Card> getSelectedCards() {
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        LinkedList<Card> selectedCards = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                selectedCards.add((Card) listView.getAdapter().getItem(i));
            }
        }
        return selectedCards;
    }

    private void setToLoading() {
        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noCardsFoundView.setVisibility(View.GONE);
    }

    private void setToView() {
        container.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        noCardsFoundView.setVisibility(View.GONE);
    }

    private void extractViews() {
        placeField = findViewById(R.id.place_input);
        minAmountField = findViewById(R.id.min_amount_input);
        maxAmountField = findViewById(R.id.max_amount_input);
        minMonthSpinner = findViewById(R.id.min_month_spinner);
        maxMonthSpinner = findViewById(R.id.max_month_spinner);
        minYearField = findViewById(R.id.min_year_input);
        maxYearField = findViewById(R.id.max_year_input);
        listView = findViewById(R.id.list_view);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.container);
        queryTransactionsBtn = findViewById(R.id.query_transactions_btn);
        noCardsFoundView = findViewById(R.id.no_cards_found_view);
    }

    @Override
    public void retry() {
        setContentView(root);
        initGetCards();
    }

}
