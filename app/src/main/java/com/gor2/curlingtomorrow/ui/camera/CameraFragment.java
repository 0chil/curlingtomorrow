package com.gor2.curlingtomorrow.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gor2.curlingtomorrow.R;

public class CameraFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        textView.setText("카메라 작동");
        return root;
    }
}
