package com.example.a4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<ArrayList<String>> dataSet;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyAdapter(Context context, ArrayList<ArrayList<String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.dataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_note_view, parent, false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String title = dataSet.get(position).get(0);
        holder.myTitleView.setText(title);
        final String content = dataSet.get(position).get(1);
        holder.myContentView.setText(content);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = findPosition(title, content);
                dataSet.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public int findPosition(String title, String content){
        for(int i=0; i<dataSet.size(); ++i){
            ArrayList<String> temp = dataSet.get(i);
            if(temp.get(0)==title && temp.get(1)==content){
                return i;
            }
        }
        return -1;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTitleView;
        TextView myContentView;
        FloatingActionButton deleteButton;


        ViewHolder(View itemView) {
            super(itemView);
            myTitleView = itemView.findViewById(R.id.title);
            myContentView = itemView.findViewById(R.id.content);
            deleteButton = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        public FloatingActionButton getDeleteButton(){
            return deleteButton;
        }
    }

    // convenience method for getting data at click position
    ArrayList<String> getItem(int id) {
        return dataSet.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}