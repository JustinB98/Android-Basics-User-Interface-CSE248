package behrman.justin.financialmanager.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import behrman.justin.financialmanager.utils.StringConstants;

public abstract class ViewHistoryActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CalendarView calendarView;
    private Button viewTransactionHistoryBtn;

    protected String cardName;

    private Date dateSelected;

    private HashMap<Date, LinkedList<Transaction>> cardTransactions;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        cardName = getIntent().getStringExtra(StringConstants.CARD_NAME);
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
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                dateSelected = calendar.getTime();
            }
        });
    }

    private void refresh() {
        setToLoadView();
        getTransactions();
    }

    private void initButtonClick() {
        viewTransactionHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(calendarView.getDate());
                LinkedList<Transaction> transactions = cardTransactions.get(dateSelected);
                changeView(transactions);
            }
        });
    }

    private void changeView(LinkedList<Transaction> transactions) {
        if (transactions == null) {
            Toast.makeText(this, "there's no transactions for this date: " + dateSelected, Toast.LENGTH_LONG).show();
        } else {
            switchToShowTransactionsActivity(transactions);
        }
    }

    private void switchToShowTransactionsActivity(LinkedList<Transaction> list) {
        Intent intent = new Intent(this, ViewTransactionsForDateActivity.class);
        intent.putExtra(StringConstants.TRANSACTIONS_KEY, list);
        startActivity(intent);
    }

    private void extractViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        viewTransactionHistoryBtn  = (Button) findViewById(R.id.view_transaction_btn);
    }

    public void setToLoadView() {
        calendarView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setToDateView() {
        calendarView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public abstract void getTransactions();

    protected void setTransactionData(HashMap<Date, LinkedList<Transaction>> cardTransactions) {
        setToDateView();
        this.cardTransactions = cardTransactions;
    }

}
