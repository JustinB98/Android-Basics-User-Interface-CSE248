package behrman.justin.financialmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import behrman.justin.financialmanager.R;

// https://stackoverflow.com/questions/4905315/error-connection-refused
public class MainActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button loginBtn;
    private TextView createAccountView;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        initClickables();
    }

    private void initClickables() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        createAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCreateAccountIntent();
            }
        });
    }

    private void switchToCreateAccountIntent() {
        Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void login() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        authenticate(username, password);
    }

    private void authenticate(String username, String password) {
        boolean oneEmpty = checkIfEmpty(username, R.string.username_empty, false);
        oneEmpty |= checkIfEmpty(password, R.string.password_empty, oneEmpty);
        if (!oneEmpty) {
            sendCredentials(username, password);
        }
    }

    // just don't want two toasts showing
    private boolean checkIfEmpty(String s, int msgIfEmptyId, boolean toastHappening) {
        // if the string is empty and there's no toast happening, making a toast then return true
        // to show that there was a toast
        if (s.trim().isEmpty() && !toastHappening) {
            Toast.makeText(MainActivity.this, msgIfEmptyId, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void sendCredentials(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        hideKeyboard();
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    goToMainMenu();
                } else {
                    Toast.makeText(MainActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        initWritableViews();
        loginBtn = (Button) findViewById(R.id.login_btn);
        createAccountView = (TextView) findViewById(R.id.create_account);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    private void initWritableViews() {
        usernameField = (EditText) findViewById(R.id.username_input);
        passwordField = (EditText) findViewById(R.id.password_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUIIfNeeded(currentUser);
        passwordField.setText("");
    }

    private void goToMainMenu() {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }

    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void updateUIIfNeeded(FirebaseUser currentUser) {
        if (currentUser != null) {
            goToMainMenu();
        }
    }

}
