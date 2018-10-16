package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import behrman.justin.financialmanager.R;

public class MenuActivity extends AppCompatActivity {

    private Button addCardBtn, editCardBtn, checkHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        extractViews();
        initClickListeners();
    }

    private void initClickListeners() {

    }

    private void initSingleClickListener(Button btn, final Class<AppCompatActivity> classToOpen) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, classToOpen));
            }
        });
    }

    private void extractViews() {
        addCardBtn = (Button) findViewById(R.id.add_card_btn);
        editCardBtn = (Button) findViewById(R.id.edit_card_btn);
        checkHistoryBtn = (Button) findViewById(R.id.card_history_btn);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }
}
