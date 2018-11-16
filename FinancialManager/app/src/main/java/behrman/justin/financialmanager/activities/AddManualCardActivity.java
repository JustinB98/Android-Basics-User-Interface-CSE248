package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ParseExceptionUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class AddManualCardActivity extends AppCompatActivity {

    private EditText cardNameField;
    private Button addCardBtn;
    private ProgressBar progressBar;

    private final static String LOG_TAG = AddManualCardActivity.class.getSimpleName() + "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_card);
        extractViews();
        initClickListener();
    }
    
    private void initClickListener() {
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    private void sendData() {
        String cardName = ProjectUtils.normalizeString(cardNameField);
        if (!cardName.isEmpty()) {
            ProjectUtils.hideKeyboard(AddManualCardActivity.this);
            addCardBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            sendData(cardName);
        } else {
            Toast.makeText(this, R.string.empty_card_name, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendData(final String cardName) {
        ParseQuery<ParseObject> cardInformation = ParseQuery.getQuery(StringConstants.MANUAL_CARD_CLASS_NAME);
        cardInformation.whereEqualTo(StringConstants.DATABASE_CARD_NAME_COLUMN, cardName);
        cardInformation.whereEqualTo(StringConstants.DATABASE_CARD_OWNER_COLUMN, ParseUser.getCurrentUser());
        cardInformation.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                Log.i(LOG_TAG, e == null ? "null" : e.toString());
                afterFind(count, cardName);
                progressBar.setVisibility(View.GONE);
                addCardBtn.setEnabled(true);
            }
        });
    }

    private void afterFind(int count, final String cardName) {
        if (count == 0) {
            Log.i(LOG_TAG, "creating a card object");
            ParseObject card = createCardObject(cardName);
            card.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    afterSave(e, cardName);
                }
            });
        } else {
            Toast.makeText(AddManualCardActivity.this, R.string.card_already_exists, Toast.LENGTH_SHORT).show();
        }
    }

    private void afterSave(ParseException e, String cardName) {
        if (e == null) {
            Toast.makeText(AddManualCardActivity.this, getString(R.string.added_card, cardName), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
            // int errorId = ParseExceptionUtils.returnParseExceptionMessage(e);
            // Toast.makeText(AddManualCardActivity.this, R.string.card_already_exists, Toast.LENGTH_SHORT).show();
            // Toast.makeText(AddManualCardActivity.this, errorId, Toast.LENGTH_SHORT).show();
            ParseExceptionUtils.displayErrorMessage(e, this);
        }
    }

    private ParseObject createCardObject(String cardName) {
        ParseObject card = new ParseObject(StringConstants.MANUAL_CARD_CLASS_NAME);
        card.put(StringConstants.DATABASE_CARD_NAME_COLUMN, cardName);
        card.put(StringConstants.DATABASE_CARD_OWNER_COLUMN, ParseUser.getCurrentUser());
        return card;
    }

    private void extractViews() {
        cardNameField = findViewById(R.id.card_name);
        addCardBtn = findViewById(R.id.add_card_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

}
