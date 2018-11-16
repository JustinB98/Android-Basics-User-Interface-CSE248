package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import behrman.justin.financialmanager.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        extractViews();
        
    }

    private void extractViews() {
        emailField = findViewById(R.id.email_view);
        sendRequestBtn = findViewById(R.id.reset_password_btn);
    }

}
