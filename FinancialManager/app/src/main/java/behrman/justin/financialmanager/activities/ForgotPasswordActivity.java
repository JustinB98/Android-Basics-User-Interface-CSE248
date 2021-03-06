package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    public final static String LOG_TAG = ForgotPasswordActivity.class.getSimpleName() + "debug";

    private EditText emailField;
    private Button sendRequestBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        extractViews();
        initClick();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initClick() {
        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String email = ProjectUtils.normalizeString(emailField);
        if (!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            sendRequestBtn.setEnabled(false);
            ProjectUtils.hideKeyboard(this);
            sendRequest(email);
        } else {
            Toast.makeText(this, R.string.username_empty, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRequest(String email) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_USERNAME, email);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_RESET_PASSWORD, params, new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                Log.i(LOG_TAG, "got " + object);
                progressBar.setVisibility(View.GONE);
                sendRequestBtn.setEnabled(true);
                if (object != null) {
                    onReturn(object);
                }
            }
        }, this, LOG_TAG);
    }

    private void onReturn(String result) {
        if (ProjectUtils.deepEquals(result, StringConstants.SUCCESS)) {
            Toast.makeText(this, R.string.sent_password_reset_request, Toast.LENGTH_LONG).show();
        } else if (ProjectUtils.deepEquals(result, StringConstants.INVALID)) {
            Toast.makeText(this, R.string.username_not_found_on_database, Toast.LENGTH_LONG).show();
        }
    }

    private void extractViews() {
        emailField = findViewById(R.id.email_view);
        sendRequestBtn = findViewById(R.id.reset_password_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

}
