package behrman.justin.financialmanager.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.BoringActivity;

public class InfoFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.help_screen, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        checkForClicks(preference.getKey());
        return super.onPreferenceTreeClick(preference);
    }

    private void checkForClicks(String key) {
        if (key.equals(getString(R.string.auto_vs_manual_key))) {
            BoringActivity.startActivity(getContext(), R.layout.auto_vs_manual, R.string.auto_vs_manual_title);
        } else if (key.equals(getString(R.string.plaid_info_key))) {
            BoringActivity.startActivity(getContext(), R.layout.plaid_info, R.string.plaid_info_title);
        }
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
