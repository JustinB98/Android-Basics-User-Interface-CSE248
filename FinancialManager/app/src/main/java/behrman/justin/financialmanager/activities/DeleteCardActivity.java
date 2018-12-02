package behrman.justin.financialmanager.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class DeleteCardActivity extends AppCompatActivity {

    public final static String LOG_TAG = DeleteCardActivity.class.getSimpleName() + "debug";

    private Card originalCard;
    private EditText confirmCardNameField;
    private ProgressBar progressBar;
    private Button deleteCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_card);
        originalCard = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        extractViews();
        initActionColor();
        removeManualCardViewIfNeeded();
        initClick();
        setForView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && deleteCardBtn.isEnabled()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractViews() {
        deleteCardBtn = findViewById(R.id.delete_btn);
        confirmCardNameField = findViewById(R.id.confirm_card_name_view);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initActionColor() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.red));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(getString(R.string.delete_card_title, originalCard.getCardName()));
    }

    private void initClick() {
        deleteCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard();
            }
        });
    }

    private void deleteCard() {
        if (originalCard.getCardName().equals(confirmCardNameField.getText().toString())) {
            deleteCard0();
        } else {
            Toast.makeText(this, R.string.card_name_not_confirmed, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCard0() {
        if (originalCard.getCardType() == CardType.MANUAL) {
            deleteCard1(StringConstants.PARSE_CLOUD_FUNCTION_DELETE_MANUAL_CARD);
        } else if (originalCard.getCardType() == CardType.AUTO) {
            deleteCard1(StringConstants.PARSE_CLOUD_FUNCTION_DELETE_AUTO_CARD);
        } else {
            Log.i(LOG_TAG, "unknown card type: " + originalCard.getCardType() + ", was there another added?");
        }
    }

    private void deleteCard1(String functionName) {
        setForLoading();
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(functionName, getParams(), new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                Log.i(LOG_TAG, "returned with object: " + object);
                setForView();
                if (object != null && ProjectUtils.deepEquals(object, StringConstants.SUCCESS)) {
                    Toast.makeText(DeleteCardActivity.this, R.string.deleted_card, Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }, this, LOG_TAG);
    }

    private void setForLoading() {
        ProjectUtils.hideKeyboard(this);
        progressBar.setVisibility(View.VISIBLE);
        deleteCardBtn.setEnabled(false);
    }

    private void setForView() {
        progressBar.setVisibility(View.GONE);
        deleteCardBtn.setEnabled(true);
    }

    private HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put(StringConstants.CARD_NAME_KEY, originalCard.getCardName());
        Log.i(LOG_TAG, "sending: " + params);
        return params;
    }

    private void removeManualCardViewIfNeeded() {
        if (originalCard.getCardType() == CardType.AUTO) {
            TextView manualView = findViewById(R.id.manual_card_view);
            manualView.setVisibility(View.GONE);
        }
    }

}
