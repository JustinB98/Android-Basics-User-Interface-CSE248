package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.fragments.InfoFragment;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, new InfoFragment())
                .commit();
    }
}
