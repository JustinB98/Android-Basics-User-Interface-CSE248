package behrman.justin.financialmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import behrman.justin.financialmanager.R;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_to_auto_card_help:
                finish();
                startActivity(this, R.layout.auto_vs_manual, R.string.auto_vs_manual_title);
                break;
            case R.id.link_to_plaid_help:
                finish();
                startActivity(this, R.layout.plaid_info, R.string.plaid_info_title);
                break;
        }
    }

    public static void startActivity(Context context, int resId, int titleId) {
        Intent intent = new Intent(context, BoringActivity.class);
        intent.putExtra(StringConstants.RES_ID_KEY, resId);
        intent.putExtra(StringConstants.TITLE_KEY, titleId);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int layoutResId, String title) {
        Intent intent = new Intent(context, BoringActivity.class);
        intent.putExtra(StringConstants.RES_ID_KEY, layoutResId);
        intent.putExtra(StringConstants.TITLE_KEY, title);
        context.startActivity(intent);
    }

}
