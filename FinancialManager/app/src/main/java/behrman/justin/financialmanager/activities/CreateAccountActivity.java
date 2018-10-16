package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import behrman.justin.financialmanager.R;

public class CreateAccountActivity extends AppCompatActivity {

    public final static String LOG_TAG = CreateAccountActivity.class.getSimpleName();

    private EditText usernameField, passwordField, confirmPasswordField;
    private Button createAccountBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();

        extractViews();
        initBtn();
    }

    private void initBtn() {
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if (fieldsAreValid(username, password)) {
            signUp0(username, password);
        }
    }

    private void switchToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private void signUp0(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(LOG_TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    switchToMenuActivity();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i(LOG_TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(CreateAccountActivity.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean fieldsAreValid(String username, String password) {
        if (!isValidEmail(username)) {
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidPassword(password)) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
     }

    private boolean isValidEmail(String email) {
        return email.matches("[\\w.]+@\\w+\\.(net|com|edu)");
    }

    // https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
    private boolean isValidPassword(String password) {
        // 5 characters, at least one uppercase and one number
        return password.matches("^(?=.*?[A-Z])(?=.*?[0-9]).{5,}$");
    }

    private void extractViews() {
        usernameField = (EditText) findViewById(R.id.username_input);
        passwordField = (EditText) findViewById(R.id.password_input);
        confirmPasswordField = (EditText) findViewById(R.id.confirm_password_input);
        createAccountBtn = (Button) findViewById(R.id.create_account_btn);
    }

}
