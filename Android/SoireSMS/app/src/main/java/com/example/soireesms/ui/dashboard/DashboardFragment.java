package com.example.soireesms.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.soireesms.MainActivity;
import com.example.soireesms.R;
import com.google.android.material.textfield.TextInputEditText;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextInputEditText mUrl;
    private MainActivity main ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        main = (MainActivity) getActivity();

        mUrl = root.findViewById(R.id.domainLayout);
        mUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("DOMAIN UPDATE", s.toString());
                main.url = s.toString();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.putString("url", s.toString());
                editor.apply();
            }
        });
        if (null != main.url && main.url.length() != 0){
            mUrl.setText(main.url);
        }

        return root;
    }
}