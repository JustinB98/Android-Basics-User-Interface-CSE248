package behrman.justin.financialmanager.subactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import behrman.justin.financialmanager.interfaces.TransactionCommunicator;
import behrman.justin.financialmanager.model.Transaction;
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

    // saved month and year because when the calendar view is not on the screen, the getSelectedDate returns null.
    // basically just to get the month and year selected last so a refresh can be performed
    private int savedMonth = ProjectUtils.getCurrentMonth();
    private int savedYear = ProjectUtils.getCurrentYear();

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
                savedMonth = date.getMonth() + 1;
                savedYear = date.getYear();
                Log.i(LOG_TAG, "month was changed: " + savedMonth + ", " + savedYear);
                calendarView.clearSelection();
                communicator.requestNewTransactions(savedYear, savedMonth);
            }
        });
    }

    private void initButtonClick() {
        viewTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay selectedDay = calendarView.getSelectedDate();
                if (communicator.loadingProperty().getValue()) {
                    Toast.makeText(activity, R.string.transactions_still_loading, Toast.LENGTH_SHORT).show();
                } else if (selectedDay != null) {
                    ArrayList<Transaction> transactions = communicator.getTransactions().get(selectedDay.getDate());
                    changeView(transactions);
                }
            }
        });
    }

    private void changeView(ArrayList<Transaction> transactions) {
        if (transactions == null) {
            String fullDate = ProjectUtils.getFullDate(activity, calendarView.getSelectedDate().getDate());
            Toast.makeText(activity, activity.getString(R.string.no_transactions_for_date, fullDate), Toast.LENGTH_LONG).show();
        } else {
            switchToShowTransactionsActivity(transactions);
        }
    }

    private void switchToShowTransactionsActivity(ArrayList<Transaction> list) {
        Intent intent = new Intent(activity, ViewTransactionsForDateActivity.class);
        intent.putExtra(StringConstants.TRANSACTIONS_INTENT_KEY, list);
        intent.putExtra(StringConstants.DATE_KEY, calendarView.getSelectedDate().getDate());
        intent.putExtra(StringConstants.IS_MANUAL_CARD_KEY, communicator.isManualCard());
        activity.startActivity(intent);
    }

    public int getMonthSelected() {
        CalendarDay cal = calendarView.getSelectedDate();
        if (cal == null) {
            Log.i(LOG_TAG, "return current month: " + ProjectUtils.getCurrentMonth());
            return ProjectUtils.getCurrentMonth();
        } else {
            Log.i(LOG_TAG, "returning selected month: " + calendarView.getSelectedDate().getMonth());
            return calendarView.getSelectedDate().getMonth() + 1;
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

    public CalendarDay getSelectedDate() {
        return calendarView.getSelectedDate();
    }

    // instead of returning the selected date, this method will return
    // the current month and year the calendar view is selected. The default day is -1
    public CalendarDay getSmartSelectedDate() {
        CalendarDay selectedDay = calendarView.getSelectedDate();
        if (selectedDay == null) {
            // saved month is 1-12, but api uses 0-11
            selectedDay = CalendarDay.from(savedYear, savedMonth - 1, -1);
        }
        return selectedDay;
    }

    public int getSavedMonth() {
        return savedMonth;
    }

    public int getSavedYear() {
        return savedYear;
    }

}
