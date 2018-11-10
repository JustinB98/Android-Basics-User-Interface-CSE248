package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.SelectableAdapter;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardReceiever;
import behrman.justin.financialmanager.utils.GetCardsUtil;

public class MonthlyCalculationsActivity extends AppCompatActivity {

    public final static String LOG_TAG = MonthlyCalculationsActivity.class.getSimpleName() + "debug";

    private ProgressBar progressBar;
    private Button actionBtn;
    private EditText yearField;
    private Spinner monthSpinner;
    private ListView listView;

    private List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_calculations);
        extractViews();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        initClickListener();
        update();
    }

    private void initClickListener() {
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "listing");
                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        Log.i(LOG_TAG, "card: " + cards.get(i));
                    }
                }
            }
        });
    }

    private void update() {
        setToLoadView();
        CardReceiever cardReceiever = new CardReceiever() {
            @Override
            public void receiveCards(List<Card> cards) {
                MonthlyCalculationsActivity.this.cards = cards;
                setToReadyView();
                SelectableAdapter adapter = new SelectableAdapter(MonthlyCalculationsActivity.this, cards, listView);
                listView.setAdapter(adapter);
            }
        };
        GetCardsUtil.findAllCards(cardReceiever);
    }

    private void setToLoadView() {
        progressBar.setVisibility(View.VISIBLE);
        actionBtn.setVisibility(View.GONE);
        yearField.setVisibility(View.GONE);
        monthSpinner.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    private void setToReadyView() {
        progressBar.setVisibility(View.GONE);
        actionBtn.setVisibility(View.VISIBLE);
        yearField.setVisibility(View.VISIBLE);
        monthSpinner.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    private void extractViews() {
        progressBar = findViewById(R.id.progress_bar);
        actionBtn = findViewById(R.id.action_btn);
        yearField = findViewById(R.id.year_field);
        monthSpinner = findViewById(R.id.month_spinner);
        listView = findViewById(R.id.list_view);
    }

}
