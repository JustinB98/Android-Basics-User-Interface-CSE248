package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_root, new SettingsFragment())
                .commit();
    }

}
