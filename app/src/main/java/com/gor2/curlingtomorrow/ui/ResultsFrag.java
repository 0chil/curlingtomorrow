package com.gor2.curlingtomorrow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gor2.curlingtomorrow.Curlingtomorrow;
import com.gor2.curlingtomorrow.MainActivity;
import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.adapter.ResultAdapter;
import com.gor2.curlingtomorrow.dataclass.Result;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ResultsFrag extends Fragment {
    ResultAdapter resultAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_results, container, false);

        ArrayList<Result> results = ((Curlingtomorrow)container.getContext().getApplicationContext()).GetResultsList();

        RecyclerView recyclerView = root.findViewById(R.id.recycler_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        resultAdapter = new ResultAdapter(results,getActivity());
        recyclerView.setAdapter(resultAdapter);

        Refresh();
        return root;
    }

    public void Refresh(){
        if(resultAdapter != null)
            resultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.REQUESTCODE){
            if(resultCode==RESULT_OK){
                Refresh();
            }
        }
    }
}
