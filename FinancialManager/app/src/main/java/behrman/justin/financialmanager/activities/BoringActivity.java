package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import behrman.justin.financialmanager.utils.StringConstants;

/**
 * An activity that you can pass in a resource id for the layout, and pass in a title (string or id) </br>
 * and will be displayed on the screen. This is just for general cases such as any help screen item or faq </br>
 * because there's no interaction between the user and the device besides maybe scrolling
 */
public class BoringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resId = getIntent().getIntExtra(StringConstants.RES_ID_KEY, 0);
        setContentView(resId);
        setActionBarTitle();
    }

    private void setActionBarTitle() {
        String title = getIntent().getStringExtra(StringConstants.TITLE_KEY);
        int titleResId = getIntent().getIntExtra(StringConstants.TITLE_KEY, -1);
        if (title != null) {
            getSupportActionBar().setTitle(title);
        } else if (titleResId != -1) {
            getSupportActionBar().setTitle(titleResId);
        }
    }
}
