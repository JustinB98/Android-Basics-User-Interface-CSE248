package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && addCardBtn.isEnabled()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void initClickListener() {
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCard();
            }
        });
    }

    private void saveCard() {
        String cardName = ProjectUtils.normalizeString(cardNameField);
        if (!cardName.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            addCardBtn.setEnabled(false);
            ProjectUtils.hideKeyboard(this);
            saveCard(cardName);
        } else {
            Toast.makeText(this, R.string.empty_card_name, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCard(final String cardName) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put(StringConstants.CARD_NAME_KEY, cardName);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_ADD_MANUAL_CARD, params, new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                progressBar.setVisibility(View.GONE);
                addCardBtn.setEnabled(true);
                if (object != null) { // if null, the call function handler handled it
                    afterReturn(object, cardName);
                }
            }
        }, this, LOG_TAG);
    }

    private void afterReturn(String result, String cardName) {
        if (ProjectUtils.deepEquals(result, StringConstants.SUCCESS)) {
            Toast.makeText(AddManualCardActivity.this, getString(R.string.added_card, cardName), Toast.LENGTH_SHORT).show();
            finish();
        } else if (ProjectUtils.deepEquals(result, StringConstants.EXISTS)) {
            Toast.makeText(AddManualCardActivity.this, R.string.card_already_exists, Toast.LENGTH_SHORT).show();
        }
    }

    private void extractViews() {
        cardNameField = findViewById(R.id.card_name);
        addCardBtn = findViewById(R.id.add_card_btn);
        progressBar = findViewById(R.id.progress_bar);
    }
}
