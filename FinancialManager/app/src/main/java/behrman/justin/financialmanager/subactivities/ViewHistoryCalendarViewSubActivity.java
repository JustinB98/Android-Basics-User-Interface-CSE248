package behrman.justin.financialmanager.subactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.ViewTransactionsForDateActivity;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.model.TransactionCommunicator;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

// https://github.com/prolificinteractive/material-calendarview/
public class ViewHistoryCalendarViewSubActivity {

    private final static String LOG_TAG = ViewHistoryCalendarViewSubActivity.class.getSimpleName() + "debug";

    private AppCompatActivity activity;
    private TransactionCommunicator communicator;
    private View root;

    private MaterialCalendarView calendarView;
    private Button viewTransactionBtn;

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
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                int month = date.getMonth() + 1;
                int year = date.getYear();
                calendarView.clearSelection();
                communicator.requestNewTransactions(year, month);
            }
        });
    }

    private void initButtonClick() {
        viewTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay selectedDay = calendarView.getSelectedDate();
                if (selectedDay != null) {
                    ArrayList<Transaction> transactions = communicator.getTransactions().get(selectedDay.getDate());
                    changeView(transactions);
                }
            }
        });
    }

    private void changeView(ArrayList<Transaction> transactions) {
        if (transactions == null) {
            String fullDate = ProjectUtils.getFullDate(calendarView.getSelectedDate().getDate());
            Toast.makeText(activity, "there's no transactions for " + fullDate, Toast.LENGTH_LONG).show();
        } else {
            switchToShowTransactionsActivity(transactions);
        }
    }

    private void switchToShowTransactionsActivity(ArrayList<Transaction> list) {
        Intent intent = new Intent(activity, ViewTransactionsForDateActivity.class);
        intent.putExtra(StringConstants.TRANSACTIONS_KEY, list);
        intent.putExtra(StringConstants.DATE_KEY, calendarView.getSelectedDate().getDate());
        activity.startActivity(intent);
    }

    public int getMonthSelected() {
        CalendarDay cal = calendarView.getSelectedDate();
        if (cal == null) {
            return ProjectUtils.getCurrentMonth();
        } else {
            return calendarView.getSelectedDate().getMonth();
        }
    }

    public int getYearSelected() {
        CalendarDay cal = calendarView.getSelectedDate();
        if (cal == null) {
            return ProjectUtils.getCurrentYear();
        } else {
            return calendarView.getSelectedDate().getYear();
        }
    }

    public int getDaySelected() {
        CalendarDay cal = calendarView.getSelectedDate();
        if (cal == null) {
            return ProjectUtils.getCurrentDay();
        } else {
            return calendarView.getSelectedDate().getDay();
        }
    }

}
