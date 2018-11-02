package behrman.justin.financialmanager.subactivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.ViewTransactionsForDateActivity;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.model.TransactionCommunicator;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewHistoryCalendarViewSubActivity {

    private final static String LOG_TAG = ViewHistoryCalendarViewSubActivity.class.getSimpleName() + "debug";

    private AppCompatActivity activity;
    private TransactionCommunicator communicator;
    private View root;

    private CalendarView calendarView;
    private Button viewTransactionBtn;

    private GregorianCalendar dateSelected = new GregorianCalendar(ProjectUtils.getCurrentYear(), ProjectUtils.getCurrentMonth() - 1, ProjectUtils.getCurrentDay());

    public ViewHistoryCalendarViewSubActivity(AppCompatActivity activity, TransactionCommunicator communicator) {
        root = LayoutInflater.from(activity).inflate(R.layout.activity_view_history, null);
        this.activity = activity;
        this.communicator = communicator;
        extractViews();
        initButtonClick();
        initCalendarChange();
    }

    public void setToView() {
        activity.setContentView(root);
    }

    private void extractViews() {
        calendarView = root.findViewById(R.id.calendar_view);
        viewTransactionBtn = root.findViewById(R.id.view_transaction_btn);
    }

    private void initCalendarChange() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                if (calendar.get(Calendar.MONTH) != dateSelected.get(Calendar.MONTH) || calendar.get(Calendar.YEAR) != dateSelected.get(Calendar.YEAR)) {
                    Log.i(LOG_TAG, "getting new  transactions");
                    communicator.requestNewTransactions(year, month + 1);
                }
                dateSelected = calendar;
            }
        });
    }

    private void initButtonClick() {
        viewTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Transaction> transactions = communicator.getTransactions().get(dateSelected.getTime());
                changeView(transactions);
            }
        });
    }

    private void changeView(ArrayList<Transaction> transactions) {
        if (transactions == null) {
            String fullDate = ProjectUtils.getFullDate(dateSelected.getTime());
            Toast.makeText(activity, "there's no transactions for " + fullDate, Toast.LENGTH_LONG).show();
        } else {
            switchToShowTransactionsActivity(transactions);
        }
    }

    private void switchToShowTransactionsActivity(ArrayList<Transaction> list) {
        Intent intent = new Intent(activity, ViewTransactionsForDateActivity.class);
        intent.putExtra(StringConstants.TRANSACTIONS_KEY, list);
        intent.putExtra(StringConstants.DATE_KEY, dateSelected.getTime());
        activity.startActivity(intent);
    }

    public int getMonthSelected() {
        return dateSelected.get(Calendar.MONTH) + 1;
    }

    public int getYearSelected() {
        return dateSelected.get(Calendar.YEAR);
    }

    public int getDaySelected() {
        return dateSelected.get(Calendar.DAY_OF_MONTH);
    }

}
