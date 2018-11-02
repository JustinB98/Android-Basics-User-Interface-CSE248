package behrman.justin.financialmanager.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.ViewTransactionsForDateActivity;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public abstract class ViewHistoryActivity extends AppCompatActivity {

    private final static String LOG_TAG = ViewHistoryActivity.class.getSimpleName() + "debug";

    private ProgressBar progressBar;
    private CalendarView calendarView;
    private Button viewTransactionHistoryBtn;

    protected String cardName;

    private GregorianCalendar dateSelected = new GregorianCalendar(ProjectUtils.getCurrentYear(), ProjectUtils.getCurrentMonth() - 1, ProjectUtils.getCurrentDay());

    private HashMap<Date, LinkedList<Transaction>> cardTransactions;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Card card = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        cardName = card.getCardName();
        setContentView(R.layout.activity_view_history);
        extractViews();
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
                LinkedList<Transaction> transactions = cardTransactions.get(dateSelected.getTime());
                changeView(transactions);
            }
        });
    }

    private void changeView(LinkedList<Transaction> transactions) {
        if (transactions == null) {
            Toast.makeText(this, "there's no transactions for this date: " + dateSelected.getTime(), Toast.LENGTH_LONG).show();
        } else {
            switchToShowTransactionsActivity(transactions);
        }
    }

    private void switchToShowTransactionsActivity(LinkedList<Transaction> list) {
        Intent intent = new Intent(this, ViewTransactionsForDateActivity.class);
        intent.putExtra(StringConstants.TRANSACTIONS_KEY, list);
        intent.putExtra(StringConstants.DATE_KEY, dateSelected.getTime());
        startActivity(intent);
    }

    private void extractViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        viewTransactionHistoryBtn  = (Button) findViewById(R.id.view_transaction_btn);
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

    protected void setTransactionData(HashMap<Date, LinkedList<Transaction>> cardTransactions) {
        Log.i(LOG_TAG, "got some transactions");
        setToDateView();
        this.cardTransactions = cardTransactions;
    }

}
