package com.gor2.curlingtomorrow.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.dataclass.Result;
import com.gor2.curlingtomorrow.ui.ShowResultActivity;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private ArrayList<Result> results;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtPlayerRed, txtRedScore, txtPlayerYellow, txtYellowScore, txtRedWin, txtYellowWin;
        LinearLayout layoutRedScore, layoutYellowScore;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtRedWin = itemView.findViewById(R.id.txtRedWin);
            txtYellowWin = itemView.findViewById(R.id.txtYellowWin);
            layoutRedScore = itemView.findViewById(R.id.layoutRedScore);
            layoutYellowScore = itemView.findViewById(R.id.layoutYellowScore);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPlayerRed = itemView.findViewById(R.id.txtPlayerRed_item);
            txtPlayerYellow = itemView.findViewById(R.id.txtPlayerYellow_item);
            txtRedScore = itemView.findViewById(R.id.txtRedScore_item);
            txtYellowScore = itemView.findViewById(R.id.txtYellowScore_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ShowResultActivity.class);
                    intent.putExtra("index",index);
                    v.getContext().startActivity(intent);
                }
            });
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
        if(results.get(position).getPlayerRedScore()>0){
            holder.txtPlayerRed.setBackgroundColor(Color.parseColor("#fff287"));
            holder.layoutRedScore.setBackgroundColor(Color.parseColor("#fff287"));
            holder.txtYellowWin.setVisibility(View.INVISIBLE);
        }
        else{
            holder.txtPlayerYellow.setBackgroundColor(Color.parseColor("#fff287"));
            holder.layoutYellowScore.setBackgroundColor(Color.parseColor("#fff287"));
            holder.txtRedWin.setVisibility(View.INVISIBLE);

        }
        holder.txtPlayerRed.setText(results.get(position).getPlayerRedName());
        holder.txtRedScore.setText(String.valueOf(results.get(position).getPlayerRedScore()));
        holder.txtPlayerYellow.setText(results.get(position).getPlayerYellowName());
        holder.txtYellowScore.setText(String.valueOf(results.get(position).getPlayerYellowScore()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
