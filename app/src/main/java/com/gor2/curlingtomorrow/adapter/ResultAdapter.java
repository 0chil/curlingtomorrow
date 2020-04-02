package com.gor2.curlingtomorrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gor2.curlingtomorrow.Curlingtomorrow;
import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.result.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private ArrayList<Result> results;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtPlayerRed, txtRedScore, txtPlayerYelow, txtYellowScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPlayerRed = itemView.findViewById(R.id.txtPlayerRed_item);
            txtPlayerYelow = itemView.findViewById(R.id.txtPlayerYellow_item);
            txtRedScore = itemView.findViewById(R.id.txtRedScore_item);
            txtYellowScore = itemView.findViewById(R.id.txtYellowScore_item);
        }
    }

    public ResultAdapter(ArrayList<Result> results) {
        super();
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item,parent,false);
        ResultAdapter.ViewHolder viewHolder = new ResultAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDate.setText(results.get(position).getDateTime());
        holder.txtPlayerRed.setText(results.get(position).getPlayerRedName());
        holder.txtRedScore.setText(results.get(position).getPlayerRedScore());
        holder.txtPlayerYelow.setText(results.get(position).getPlayerYellowName());
        holder.txtYellowScore.setText(results.get(position).getPlayerYellowScore());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
