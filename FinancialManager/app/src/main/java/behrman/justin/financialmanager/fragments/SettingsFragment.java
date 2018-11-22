package behrman.justin.financialmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.DeleteUserActivity;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        checkPreference(preference.getKey());
        return super.onPreferenceTreeClick(preference);
    }

    private void checkPreference(String key) {
        if ("deleteAccount".equals(key)) {
            openDeleteAccountActivity();
        }
    }

    private void openDeleteAccountActivity() {
        Intent intent = new Intent(getContext(), DeleteUserActivity.class);
        startActivity(intent);
    }

    // https://stackoverflow.com/questions/18509369/android-how-to-get-remove-margin-padding-in-preference-screen/18509566
    @Override
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        super.setPreferenceScreen(preferenceScreen);
        if (preferenceScreen != null) {
            int count = preferenceScreen.getPreferenceCount();
            for (int i = 0; i < count; i++)
                preferenceScreen.getPreference(i).setIconSpaceReserved(false);
        }
    }

}
