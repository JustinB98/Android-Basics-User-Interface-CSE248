package behrman.justin.financialmanager.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.AddManualTransactionActivity;
import behrman.justin.financialmanager.interfaces.TransactionCommunicator;
import behrman.justin.financialmanager.subactivities.ViewHistoryCalendarViewSubActivity;
import behrman.justin.financialmanager.subactivities.ViewHistoryListViewSubActivity;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public abstract class ViewHistoryActivity extends AppCompatActivity {

    public final static String LOG_TAG = ViewHistoryActivity.class.getSimpleName() + "debug";

    private Menu menu;

    protected String cardName;
    private boolean isManualCard;

    private DataCollection transactionData;

    private ViewHistoryCalendarViewSubActivity calendarSubActivity;
    private ViewHistoryListViewSubActivity listViewSubActivity;

    private TransactionCommunicator communicator;

    private boolean menuCreated, activityCreated;

    private BooleanProperty loading;

    private enum CurrentScreen { CALENDAR, LISTVIEW }

    private CurrentScreen currentScreen;

    private View loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        getPassedInValues();
        // getSupportActionBar().setTitle(getString(R.string.view_history_title, cardName));
        getSupportActionBar().setTitle(cardName);
        initCommunicator();
        initSubActivities();
        calendarSubActivity.setToView();
        initLoadingBar();
        getTransactions(calendarSubActivity.getYearSelected(), calendarSubActivity.getMonthSelected());
        loading = new BooleanProperty(true);
        activityCreated = true;
        initLoadingScreen();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLoadingScreen() {
        loadingScreen = getLayoutInflater().inflate(R.layout.loading_screen, null);
    }

    // https://stackoverflow.com/questions/26443490/appcompat-show-progress-in-action-bar-causes-npe
    private void initLoadingBar() {
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(progressBar);
    }

    private void getPassedInValues() {
        Card card = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        cardName = card.getCardName();
        isManualCard = card.getCardType() == CardType.MANUAL;
    }

    // https://stackoverflow.com/questions/26443490/appcompat-show-progress-in-action-bar-causes-npe
    private void setLoadingVisibility(boolean visibility) {
        getSupportActionBar().getCustomView().setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void setLoadingVisibility() {
        setLoadingVisibility(loading.getValue());
    }

    private void initSubActivities() {
        calendarSubActivity = new ViewHistoryCalendarViewSubActivity(this, communicator);
        listViewSubActivity = new ViewHistoryListViewSubActivity(this, communicator);
    }

    private void initCommunicator() {
        communicator = new TransactionCommunicator() {
            @Override
            public HashMap<Date, ArrayList<Transaction>> getTransactions() {
                return transactionData.getDataMap();
            }

            public ArrayList<Transaction> getTransactionAsList() {
                return transactionData.getDataList();
            }

            @Override
            public void requestNewTransactions(int year, int month) {
                loading.setValue(true);
                setLoadingVisibility();
                Log.i(LOG_TAG, "getting transactions for: " + month + ", " + year);
                ViewHistoryActivity.this.getTransactions(year, month);
            }

            @Override
            public boolean isManualCard() {
                return isManualCard;
            }

            @Override
            public BooleanProperty loadingProperty() {
                return loading;
            }

            @Override
            public void refresh() {
                ViewHistoryActivity.this.refresh();
            }

            @Override
            public void setToLoadingScreen() {
                setToLoading();
            }

            @Override
            public double getTotal() {
                return transactionData.getTotal();
            }

            @Override
            public void regetTransactions() {
                requestNewTransactions(calendarSubActivity.getSavedYear(), calendarSubActivity.getSavedMonth());
            }

        };
    }

    public void refresh() {
        // reget transactions
        Log.i(LOG_TAG, "getting transactions for: " + calendarSubActivity.getSavedMonth() + ", " + calendarSubActivity.getSavedYear());
        loading.setValue(true);
        setLoadingVisibility();
        getTransactions(calendarSubActivity.getSavedYear(), calendarSubActivity.getSavedMonth());
        setToRightView();
    }

    private void setToRightView() {
        Log.i(LOG_TAG, "currentScreen: " + currentScreen);
        if (currentScreen == CurrentScreen.LISTVIEW) {
            // change just in case there are no more transactions so the text view will show
            //  instead of the list view
            switchToListView();
        } else if (currentScreen == CurrentScreen.CALENDAR) {
            switchToCalendarView();
        } else {
            Log.i(LOG_TAG, "unknown state: " + currentScreen);
        }
    }

    private void setToLoading() {
        setContentView(loadingScreen);
        setLoadingVisibility(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        switchMenu(R.menu.list_view_menu);
        menuCreated = true;
        return true;
    }

    private void switchMenu(int resId) {
        menu.clear();
        if (isManualCard) {
            getMenuInflater().inflate(R.menu.add_item_menu, menu);
        }
        getMenuInflater().inflate(resId, menu);
    }

    public void menuAction(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_menu_item:
                switchToListView();
                break;
            case R.id.calendar_menu_item:
                switchToCalendarView();
                break;
            case R.id.add_item:
                switchToAddTransactionActivity();
                break;
        }
    }

    private void switchToAddTransactionActivity() {
        Intent intent = new Intent(ViewHistoryActivity.this, AddManualTransactionActivity.class);
        intent.putExtra(StringConstants.SELECTED_DATE_KEY, calendarSubActivity.getSmartSelectedDate());
        intent.putExtra(StringConstants.CARD_KEY, new Card(cardName, CardType.MANUAL));
        startActivity(intent);
    }

    private void switchToListView() {
        loading.removeListener();
        setLoadingVisibility();
        checkIfRefreshNeeded();
        listViewSubActivity.setToView();
        listViewSubActivity.setListView();
        switchMenu(R.menu.calendar_view_menu);
        currentScreen = CurrentScreen.LISTVIEW;
    }

    private void checkIfRefreshNeeded() {
        // odds are this method won't be called from the refresh method
        // so there's practically no change there will be two runs of the
        // refresh method. This is really only called when the views are being
        // switched from the menu item icons
        if (ProjectUtils.itemDeleted()) {
            // set the item deleted for FALSE FIRST otherwise there WILL be infinite recursion
            ProjectUtils.setItemDeleted(false);
            refresh();
        }
    }

    private void switchToCalendarView() {
        loading.removeListener();
        setLoadingVisibility();
        checkIfRefreshNeeded();
        calendarSubActivity.setToView();
        switchMenu(R.menu.list_view_menu);
        currentScreen = CurrentScreen.CALENDAR;
    }

    public abstract void getTransactions(int year, int month);

    protected void setTransactionData(DataCollection data) {
        Log.i(LOG_TAG, "got some transactions");
        this.transactionData = data;
        loading.setValue(false);
        setLoadingVisibility();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart called");
        if (menuCreated && activityCreated) {
            refresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume called");
        if (menuCreated && activityCreated) {
            refresh();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
