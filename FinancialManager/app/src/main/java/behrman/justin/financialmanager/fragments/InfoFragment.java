package behrman.justin.financialmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.BoringActivity;
import behrman.justin.financialmanager.utils.StringConstants;

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
        switch (key) {

        }
    }

    private void openBoringActivity(int layoutResId, int titleId) {
        Intent intent = new Intent(getContext(), BoringActivity.class);
        intent.putExtra(StringConstants.RES_ID_KEY, layoutResId);
        intent.putExtra(StringConstants.TITLE_KEY, titleId);
        startActivity(intent);
    }

    private void openBoringActivity(int layoutResId, String title) {
        Intent intent = new Intent(getContext(), BoringActivity.class);
        intent.putExtra(StringConstants.RES_ID_KEY, layoutResId);
        intent.putExtra(StringConstants.TITLE_KEY, title);
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
