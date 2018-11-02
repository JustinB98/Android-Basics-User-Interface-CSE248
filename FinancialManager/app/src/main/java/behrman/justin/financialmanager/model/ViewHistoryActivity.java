package behrman.justin.financialmanager.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.ViewTransactionsForDateActivity;
import behrman.justin.financialmanager.adapters.TransactionForMonthAdapter;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public abstract class ViewHistoryActivity extends AppCompatActivity {

    private final static String LOG_TAG = ViewHistoryActivity.class.getSimpleName() + "debug";

    private ProgressBar progressBar;

    private Menu menu;

    private ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        switchMenu(R.menu.list_view_menu);
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
        setContentView(R.layout.view_all_transactions_for_month);
        extractListViews();
        setListViewContent();
        switchMenu(R.menu.calendar_view_menu);
    }

    private void setListViewContent() {
        ArrayList<Transaction> transactions = new ArrayList<>(cardTransactions.size()); // won't be exact
        for (Date date : cardTransactions.keySet()) {
            transactions.addAll(cardTransactions.get(date));
        }
        Collections.sort(transactions);
        TransactionForMonthAdapter adapter = new TransactionForMonthAdapter(this, transactions);
        listView.setAdapter(adapter);
    }

    private void switchToCalendarView() {
        setContentView(R.layout.activity_view_history);
        extractCalendarViews();
        initButtonClick();
        initCalendarChange();
        switchMenu(R.menu.list_view_menu);
    }

    private CalendarView calendarView;
    private Button viewTransactionHistoryBtn;

    protected String cardName;

    private GregorianCalendar dateSelected = new GregorianCalendar(ProjectUtils.getCurrentYear(), ProjectUtils.getCurrentMonth() - 1, ProjectUtils.getCurrentDay());

    private HashMap<Date, ArrayList<Transaction>> cardTransactions;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Card card = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        cardName = card.getCardName();
        // just get the list view reference
        setContentView(R.layout.activity_view_history);
        extractCalendarViews();
        initButtonClick();
        initCalendarChange();
        refresh();
    }

    private void initCalendarChange() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                if (calendar.get(Calendar.MONTH) != dateSelected.get(Calendar.MONTH) || calendar.get(Calendar.YEAR) != dateSelected.get(Calendar.YEAR)) {
                    Log.i(LOG_TAG, "getting new  transactions");
                    getTransactions(year, month + 1);
                }
                dateSelected = calendar;
            }
        });
    }

    private void refresh() {
        setToLoadView();
        getTransactions(ProjectUtils.getCurrentYear(), ProjectUtils.getCurrentMonth());
    }

    private void initButtonClick() {
        viewTransactionHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Transaction> transactions = cardTransactions.get(dateSelected.getTime());
                changeView(transactions);
            }
        });
    }

    private void changeView(ArrayList<Transaction> transactions) {
        if (transactions == null) {
            Toast.makeText(this, "there's no transactions for this date: " + dateSelected.getTime(), Toast.LENGTH_LONG).show();
        } else {
            switchToShowTransactionsActivity(transactions);
        }
    }

    private void switchToShowTransactionsActivity(ArrayList<Transaction> list) {
        Intent intent = new Intent(this, ViewTransactionsForDateActivity.class);
        intent.putExtra(StringConstants.TRANSACTIONS_KEY, list);
        intent.putExtra(StringConstants.DATE_KEY, dateSelected.getTime());
        startActivity(intent);
    }

    private void extractCalendarViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        viewTransactionHistoryBtn  = (Button) findViewById(R.id.view_transaction_btn);
    }

    private void extractListViews() {
        listView = findViewById(R.id.list_view);
    }

    public void setToLoadView() {
        viewTransactionHistoryBtn.setVisibility(View.GONE);
        calendarView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setToDateView() {
        viewTransactionHistoryBtn.setVisibility(View.VISIBLE);
        calendarView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public abstract void getTransactions(int year, int month);

    protected void setTransactionData(HashMap<Date, ArrayList<Transaction>> cardTransactions) {
        Log.i(LOG_TAG, "got some transactions");
        setToDateView();
        this.cardTransactions = cardTransactions;
    }

}
