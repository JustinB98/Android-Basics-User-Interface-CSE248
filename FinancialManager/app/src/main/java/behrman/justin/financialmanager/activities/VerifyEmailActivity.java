package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ParseExceptionUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class VerifyEmailActivity extends AppCompatActivity {

    private final static String LOG_TAG = VerifyEmailActivity.class.getSimpleName() + "debug";

    private EditText emailField;
    private Button verifiedBtn, resendEmailBtn;
    private ProgressBar progressBar;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        user = getIntent().getParcelableExtra(StringConstants.USER_KEY); // generics are nice
        extractViews();
        initButtons();
        emailField.setText(user.getEmail());
    }

    private void initButtons() {
        verifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        checkIfVerified();
                    }
                });

            }
        });
        resendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        resendEmail();
                    }
                });

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

    private void resendEmail0(String email) {
        setToLoading();
        user.setUsername(email);
        user.setEmail(email);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                setToReady();
                if (e == null) {
                    Toast.makeText(VerifyEmailActivity.this, R.string.resent_verification_email, Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e.getCode(), VerifyEmailActivity.this);
                }
            }
        });
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

    private void checkIfVerified() {
        if (user.getBoolean("emailVerified")) {
            Toast.makeText(this, R.string.welcome_to_the_app_msg, Toast.LENGTH_SHORT).show();
            switchToMenuActivity();
        } else {
            Toast.makeText(this, R.string.email_still_not_verified, Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private void extractViews() {
        emailField = findViewById(R.id.email_view);
        verifiedBtn = findViewById(R.id.verified_email_btn);
        resendEmailBtn = findViewById(R.id.resend_email_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

}
