package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardWrapper;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && editCardBtn.isEnabled()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initClickListener() {
        editCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = ProjectUtils.normalizeString(cardNameField);
                if (!cardName.isEmpty()) {
                    sendEditNameRequest(cardName);
                } else {
                    Toast.makeText(EditCardActivity.this, R.string.empty_card_name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEditNameRequest(final String cardName) {
        if (originalCard.getCardName().equals(cardName)) {
            finish();
            return;
        }
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_EDIT_CARD_NAME, params(cardName), new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                if (object != null) {
                    onReturn(object, cardName);
                }
            }
        }, this, LOG_TAG);
    }

    private void onReturn(String result, String cardName) {
        if (ProjectUtils.deepEquals(result, StringConstants.SUCCESS)) {
            Toast.makeText(EditCardActivity.this, R.string.edited_card_successfully, Toast.LENGTH_SHORT).show();
            Card newCard = new Card(cardName, originalCard.getCardType());
            CardWrapper.getInstance().updateCard(originalCard, newCard);
            finish();
        } else if (ProjectUtils.deepEquals(result, StringConstants.EXISTS)) {
            Toast.makeText(EditCardActivity.this, R.string.card_already_exists, Toast.LENGTH_SHORT).show();
        } else if (ProjectUtils.deepEquals(result, StringConstants.ERROR)) {
            Toast.makeText(this, R.string.card_changed_refreshing, Toast.LENGTH_SHORT).show();
            CardWrapper.getInstance().refresh(this);
            finish();
        }
    }

    private HashMap<String, Object> params(String newCardName) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_ORIGINAL_CARD_NAME, originalCard.getCardName());
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_NEW_CARD_NAME, newCardName);
        params.put(StringConstants.CARD_TYPE_KEY, originalCard.getCardType().toString());
        return params;
    }

    private void extractViews() {
        cardNameField = findViewById(R.id.card_name);
        editCardBtn = findViewById(R.id.edit_card_btn);
    }

}
