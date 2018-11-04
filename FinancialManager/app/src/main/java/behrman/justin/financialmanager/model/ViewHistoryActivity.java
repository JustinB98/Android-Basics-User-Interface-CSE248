package behrman.justin.financialmanager.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.subactivities.ViewHistoryCalendarViewSubActivity;
import behrman.justin.financialmanager.subactivities.ViewHistoryListViewSubActivity;
import behrman.justin.financialmanager.utils.StringConstants;

public abstract class ViewHistoryActivity extends AppCompatActivity {

    private final static String LOG_TAG = ViewHistoryActivity.class.getSimpleName() + "debug";

    private ProgressBar progressBar;

    private Menu menu;

    protected String cardName;
    private boolean isManualCard;

    private HashMap<Date, ArrayList<Transaction>> cardTransactions;

    private ViewHistoryCalendarViewSubActivity calendarSubActivity;
    private ViewHistoryListViewSubActivity listViewSubActivity;

    private TransactionCommunicator communicator;

    private boolean menuCreated, activityCreated;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Card card = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        cardName = card.getCardName();
        getSupportActionBar().setTitle(getString(R.string.view_history_title, cardName));
        isManualCard = card.getCardType() == CardType.MANUAL;
        initCommunicator();
        calendarSubActivity = new ViewHistoryCalendarViewSubActivity(this, communicator);
        listViewSubActivity = new ViewHistoryListViewSubActivity(this, communicator);
        calendarSubActivity.setToView();
        getTransactions(calendarSubActivity.getYearSelected(), calendarSubActivity.getMonthSelected());
        activityCreated = true;
    }

    private void initCommunicator() {
        communicator = new TransactionCommunicator() {
            @Override
            public HashMap<Date, ArrayList<Transaction>> getTransactions() {
                return cardTransactions;
            }

            @Override
            public void requestNewTransactions(int year, int month) {
                Log.i(LOG_TAG, "getting transactions for: " + month + ", " + year);
                ViewHistoryActivity.this.getTransactions(year, month);
            }

            @Override
            public boolean isManualCard() {
                return isManualCard;
            }

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        switchMenu(R.menu.list_view_menu);
        menuCreated = true;
        return true;
    }

    private void switchMenu(int resId) {
        getMenuInflater().inflate(resId, menu);
    }

    public void menuAction(MenuItem item) {
        menu.clear();
        switch (item.getItemId()) {
            case R.id.list_menu_item:
                switchToListView();
                break;
            case R.id.calendar_menu_item:
                switchToCalendarView();
                break;
        }
    }

    private void switchToListView() {
        listViewSubActivity.setToView();
        listViewSubActivity.setListView();
        switchMenu(R.menu.calendar_view_menu);
    }

    private void switchToCalendarView() {
        calendarSubActivity.setToView();
        switchMenu(R.menu.list_view_menu);
    }

    public abstract void getTransactions(int year, int month);

    protected void setTransactionData(HashMap<Date, ArrayList<Transaction>> cardTransactions) {
        Log.i(LOG_TAG, "got some transactions");
        this.cardTransactions = cardTransactions;
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (menuCreated && activityCreated) {
            menu.clear();
            calendarSubActivity.setToView();
            switchToCalendarView();
            getTransactions(calendarSubActivity.getSavedYear(), calendarSubActivity.getSavedMonth());
        }
    }
}
