package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;

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
        originalCard = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        extractViews();
        initClickListener();
        cardNameField.setText(originalCard.getCardName());
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
                if (e == null) { // no errors, so there must be a result
                    Toast.makeText(EditCardActivity.this, "card already exists with this name!", Toast.LENGTH_SHORT).show();
                } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) { // no object found, so it must be an free name
                    saveIfNeeded(object);
                }
            }
        });
    }

    private void saveIfNeeded(ParseObject object) {
        if (object == null) {
            String functionName = getFunctionName();
            HashMap<String, Object> params = getParameters();
            ParseCloud.callFunctionInBackground(functionName, params, new FunctionCallback<String>() {
                @Override
                public void done(String object, ParseException e) {
                    Log.i(LOG_TAG, "object returned: " + object);
                    if (object.toLowerCase().equals("success")) {
                        Toast.makeText(EditCardActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Log.i(LOG_TAG, "card successfully changed");
                        finish();
                    } else {
                        Log.i(LOG_TAG, "there was an issue renaming the cards");
                    }
                }
            });
        }
    }

    private HashMap<String, Object> getParameters() {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put(StringConstants.PARSE_CLOUD_NEW_CARD_NAME_PARAMETER, cardNameField.getText().toString());
        params.put(StringConstants.PARSE_CLOUD_ORIGINAL_CARD_NAME_PARAMETER, originalCard.getCardName());
        return params;
    }

    private String getFunctionName() {
        if (originalCard.getCardType() == CardType.MANUAL) {
            return StringConstants.PARSE_CLOUD_FUNCTION_RENAME_MANUAL_CARD;
        } else if (originalCard.getCardType() == CardType.AUTO) {
            return StringConstants.PARSE_CLOUD_FUNCTION_RENAME_AUTO_CARD;
        } else {
            Log.i(LOG_TAG, "unknown case: " + originalCard.getCardType());
            return null;
        }
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
