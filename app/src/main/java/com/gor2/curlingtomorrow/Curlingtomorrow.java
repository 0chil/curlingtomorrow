package com.gor2.curlingtomorrow;

import android.app.Application;

import com.gor2.curlingtomorrow.dataclass.Result;

import java.util.ArrayList;

public class Curlingtomorrow extends Application {
    ArrayList<Result> results = new ArrayList<>();

    public ArrayList<Result> GetResultsList(){
        return results;
    }

    public void AddResult(Result result){
        results.add(result);
    }

    public void DeleteResult(int index){
        results.remove(index);
    }
}
