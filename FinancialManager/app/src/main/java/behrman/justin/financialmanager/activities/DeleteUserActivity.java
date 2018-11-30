package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class DeleteUserActivity extends AppCompatActivity {

    public final static String LOG_TAG = DeleteUserActivity.class.getSimpleName() + "debug";

    private EditText usernameField;
    private Button deleteUserBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        extractViews();
        initActionColor();
        initClick();
    }

    private void initClick() {
        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    private void deleteUser() {
        String username = usernameField.getText().toString(); // not normalizing
        if (ParseUser.getCurrentUser().getUsername().equals(username)) {
            progressBar.setVisibility(View.VISIBLE);
            deleteUserBtn.setEnabled(false);
            deleteUser0();
        } else {
            Toast.makeText(this, R.string.username_doesnt_match_with_account, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser0() {
        HashMap<String, Object> emptyParams = new HashMap<>(0);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_DELETE_USER, emptyParams, new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                Log.i(LOG_TAG, "returned with " + object);
                progressBar.setVisibility(View.GONE);
                deleteUserBtn.setEnabled(true);
                if (object != null) {
                    onReturn(object);
                }
            }
        }, this, LOG_TAG);
    }

    private void onReturn(String result) {
        if (ProjectUtils.deepEquals(result, StringConstants.SUCCESS)) {
            logout();
        } else {
            Log.i(LOG_TAG, "result: " + result);
        }
    }

    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(DeleteUserActivity.this, R.string.account_deleted, Toast.LENGTH_SHORT).show();
                switchToLoginScreen();
            }
        });
    }

    private void switchToLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initActionColor() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.red));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    private void extractViews() {
        usernameField = findViewById(R.id.username_input);
        deleteUserBtn = findViewById(R.id.delete_user_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

}
