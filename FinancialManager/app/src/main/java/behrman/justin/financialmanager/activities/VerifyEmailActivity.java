package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ParseExceptionUtils;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class VerifyEmailActivity extends AppCompatActivity {

    public final static String LOG_TAG = VerifyEmailActivity.class.getSimpleName() + "debug";

    private EditText emailField;
    private Button verifiedBtn, resendEmailBtn;
    private ProgressBar progressBar;

    private ParseUser user;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        ParseUser.logOutInBackground();
        // user = getIntent().getParcelableExtra(StringConstants.USER_KEY); // generics are nice
        email = getIntent().getStringExtra(StringConstants.EMAIL_KEY);
        password = getIntent().getStringExtra(StringConstants.PASSWORD_KEY);
        getUser();
        extractViews();
        initButtons();
        emailField.setText(email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUser() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo(StringConstants.USER_EMAIL_COLUMN, email);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    user = object;
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e.getCode(), VerifyEmailActivity.this);
                }
            }
        });
    }

    private void initButtons() {
        verifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfVerified(true);
            }
        });
        resendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendEmail();
            }
        });
    }

    private void resendEmail() {
        String email = ProjectUtils.normalizeString(emailField).toLowerCase();
        if (!email.isEmpty()) {
            resendEmail0(email);
        } else {
            Toast.makeText(this, R.string.email_empty, Toast.LENGTH_SHORT).show();
        }
    }

    private void resendEmail0(final String email) {
        setToLoading();
        HashMap<String, Object> params = new HashMap<>(1);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_ORIGINAL_EMAIL, this.email);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_NEW_EMAIL, email);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_RESEND_VERIFICATION_EMAIL, params, new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                setToReady();
                if (object != null) {
                    Log.i(LOG_TAG, "object: " + object);
                    onReturn(object, email);
                }
            }
        }, this, LOG_TAG);
    }

    private void onReturn(String result, String email) {
        if (ProjectUtils.deepEquals(result, StringConstants.SUCCESS)) {
            this.email = email;
            Toast.makeText(this, R.string.resent_verification_email, Toast.LENGTH_SHORT).show();
        } else if (ProjectUtils.deepEquals(result, StringConstants.EXISTS)) {
            Toast.makeText(this, R.string.username_taken, Toast.LENGTH_SHORT).show();
        }
    }

    private void setToLoading() {
        progressBar.setVisibility(View.VISIBLE);
        resendEmailBtn.setEnabled(false);
        verifiedBtn.setEnabled(false);
    }

    private void setToReady() {
        progressBar.setVisibility(View.GONE);
        resendEmailBtn.setEnabled(true);
        verifiedBtn.setEnabled(true);
    }

    private void checkIfVerified(final boolean displayMessage) {
        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    checkIfVerified0(displayMessage);
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e.getCode(), VerifyEmailActivity.this);
                }
            }
        });
    }

    private void checkIfVerified0(boolean displayMessage) {
        if (user.getBoolean("emailVerified")) {
            Toast.makeText(this, R.string.welcome_to_the_app_msg, Toast.LENGTH_SHORT).show();
            signInUserIfNeeded();
            // switchToMenuActivity();
        } else {
            if (displayMessage) {
                Toast.makeText(this, R.string.email_still_not_verified, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signInUserIfNeeded() {
        if (ParseUser.getCurrentUser() != null) {
            switchToMenuActivity();
        } else { // need to sign in
            signInUser();
        }
    }

    private void signInUser() {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    switchToMenuActivity();
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e.getCode(), VerifyEmailActivity.this);
                }
            }
        });
    }

    private void switchToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        finish(); // don't want the user coming back to this activity
        startActivity(intent);
    }

    private void extractViews() {
        emailField = findViewById(R.id.email_view);
        verifiedBtn = findViewById(R.id.verified_email_btn);
        resendEmailBtn = findViewById(R.id.resend_email_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            checkIfVerified(false);
        }
    }
}
