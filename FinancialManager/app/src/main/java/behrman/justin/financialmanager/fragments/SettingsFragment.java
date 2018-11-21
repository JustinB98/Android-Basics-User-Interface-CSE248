package behrman.justin.financialmanager.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import behrman.justin.financialmanager.R;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
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
