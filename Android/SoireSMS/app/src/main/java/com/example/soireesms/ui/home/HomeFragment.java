package com.example.soireesms.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.soireesms.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ListView lView;
    private ListAdapter lAdapter;

    private static final String TAG = "SMS Fragment";
    private List<Sms> messages;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages = new ArrayList<>();
        List<Sms> messagesUnsorted = SmsMethods.getAllSms((Activity) getContext());

        for(int i=0;i<messagesUnsorted.size();i++){
            if (messagesUnsorted.get(i).getFolderName().equals("inbox")) {
                messages.add(messagesUnsorted.get(i));
            } // else {continue}
        }
        SmsMethods.bubbleSort(messages);
        // TODO SOrting them
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        lView = root.findViewById(R.id.allSMS);
        lAdapter = new ListAdapter(getActivity(), messages);
        lView.setAdapter(lAdapter);

        return root;
    }
}
