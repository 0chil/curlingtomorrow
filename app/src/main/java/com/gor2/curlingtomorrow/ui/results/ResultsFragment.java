package com.gor2.curlingtomorrow.ui.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gor2.curlingtomorrow.R;

public class ResultsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_results, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        textView.setText("게임결과 표시");
        return root;
    }
}
