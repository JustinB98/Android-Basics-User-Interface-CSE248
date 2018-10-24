package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.utils.StringConstants;

public class SelectCardActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);
        listView = (ListView) findViewById(R.id.list_view);
    }

    // just in case i decide implement a refresh feature in this activity
    private void update() {
        setToLoadView();
        List<Card> cards = getCards();
    }

    private void setToLoadView() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setToListView() {
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private List<Card> getCards() {
        ParseQuery autoCards = ParseQuery.getQuery(StringConstants.MANUAL_CARD_CLASS);
        autoCards.whereEqualTo(StringConstants.MANUAL_CARD_OWNER, ParseUser.getCurrentUser());
        // autoCards.find()
        return null;
    }

}
