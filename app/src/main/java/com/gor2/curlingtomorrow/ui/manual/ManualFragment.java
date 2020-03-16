package com.gor2.curlingtomorrow.ui.manual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gor2.curlingtomorrow.R;

public class ManualFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manual, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText("게임방법 표시");
        return root;
    }
}
