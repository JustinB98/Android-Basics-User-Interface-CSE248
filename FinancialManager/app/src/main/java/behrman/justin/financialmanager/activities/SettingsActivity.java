package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import behrman.justin.financialmanager.R;

public class SettingsActivity extends AppCompatActivity {

    private ListView listView;
    private String[] settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = getResources().getStringArray(R.array.setting_strings);
        extractViews();
        initListView();
        initListViewClick();
    }

    private void initListViewClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = settings[position];
                onClicked(item);
            }
        });
    }

    private void onClicked(String itemClicked) {
        if (itemClicked.equals(settings[0])) {
            switchToDeleteUser();
        }
    }

    private void switchToDeleteUser() {
        Intent intent = new Intent(this, DeleteUserActivity.class);
        startActivity(intent);
    }

    private void initListView() {
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, settings);
        listView.setAdapter(adapter);
    }

    private void extractViews() {
        listView = findViewById(R.id.list_view);
    }

}
