package behrman.justin.financialmanager.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.utils.StringConstants;

public class EditCardActivity extends AppCompatActivity {

    private final static String LOG_TAG = EditCardActivity.class.getSimpleName() + "debug";

    private Card originalCard;

    private EditText cardNameField;
    private Button editCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        originalCard = (Card) getIntent().getSerializableExtra(StringConstants.ORIGINAL_CARD_KEY);
        extractViews();
        initClickListener();
    }

    private void initClickListener() {
        editCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEditNameRequest();
            }
        });
    }

    private void sendEditNameRequest() {
        ParseQuery<ParseObject> query = getQuery();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    saveIfNeeded(object);
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString());
                }
            }
        });
    }

    private void saveIfNeeded(ParseObject object) {
        
    }

    private ParseQuery<ParseObject> getQuery() {
        if (originalCard.getCardType() == CardType.MANUAL) {
            return getManualQuery();
        } else if (originalCard.getCardType() == CardType.AUTO){
            return getAutoQuery();
        } else {
            Log.i(LOG_TAG, "unknown case: " + originalCard.getCardType());
            return null;
        }
    }

    private ParseQuery<ParseObject> getManualQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(StringConstants.MANUAL_CARD_CLASS);
        query.whereEqualTo(StringConstants.MANUAL_CARD_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(StringConstants.MANUAL_CARD_NAME, cardNameField.getText().toString());
        return query;
    }

    private ParseQuery<ParseObject> getAutoQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(StringConstants.AUTO_CARD_CLASS);
        query.whereEqualTo(StringConstants.AUTO_CARD_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(StringConstants.AUTO_CARD_NAME, cardNameField.getText().toString());
        return query;
    }

    private void extractViews() {
        cardNameField = findViewById(R.id.card_name);
        editCardBtn = findViewById(R.id.edit_card_btn);
    }

}
