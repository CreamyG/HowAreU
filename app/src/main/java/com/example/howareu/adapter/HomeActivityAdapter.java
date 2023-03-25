package com.example.howareu.adapter;

import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howareu.R;
import com.example.howareu.constant.Integers;
import com.example.howareu.model.SimpleActivityModel;

import org.w3c.dom.Text;

import java.util.List;

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {

    private List<SimpleActivityModel> simpleActivityModel;
    Context context;

    private OnDeleteActivityClickListener onDeleteActivityClickListener;
    private OnActivityMoodRateClickListener onMoodRateClickListener;
    private OnTextChangeListener onTextChangeListener;

    public HomeActivityAdapter(List<SimpleActivityModel> simpleActivityModel, Context context, OnDeleteActivityClickListener onDeleteActivityClickListener, OnActivityMoodRateClickListener onMoodRateClickListener, OnTextChangeListener onTextChangeListener) {
        this.simpleActivityModel = simpleActivityModel;
        this.context = context;
        this.onDeleteActivityClickListener = onDeleteActivityClickListener;
        this.onMoodRateClickListener = onMoodRateClickListener;
        this.onTextChangeListener = onTextChangeListener;
    }

    @NonNull
    @Override
    public HomeActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activities,parent,false);
        ViewHolder vh= new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeActivityAdapter.ViewHolder holder, int position) {


        holder.editTextActivity.setText(simpleActivityModel.get(position).getActivityName());

        if(simpleActivityModel.get(position).isEnabled()){
            holder.editTextActivity.setEnabled(true);
            holder.btnRateMoodActivity.setEnabled(true);
            holder.btnDeleteMoodActivity.setEnabled(true);
        }
        else{
            holder.editTextActivity.setEnabled(false);
            holder.btnRateMoodActivity.setEnabled(false);
            holder.btnDeleteMoodActivity.setEnabled(false);
        }
        if(holder.editTextActivity.getText().toString().isEmpty()){
            holder.btnRateMoodActivity.setEnabled(false);
        }
         switch(simpleActivityModel.get(position).getMoodrate()){
             case Integers.MOOD_PERCENT_SAD:
                 holder.btnRateMoodActivity.setImageResource(R.drawable.sad);
                 break;
             case Integers.MOOD_PERCENT_VERY_SAD:
                 holder.btnRateMoodActivity.setImageResource(R.drawable.crying);
                 break;
             case Integers.MOOD_PERCENT_NEUTRAL:
                 holder.btnRateMoodActivity.setImageResource(R.drawable.cool);
                 break;
             case Integers.MOOD_PERCENT_HAPPY:
                 holder.btnRateMoodActivity.setImageResource(R.drawable.calm);
                 break;
             case Integers.MOOD_PERCENT_VERY_HAPPY:
                 holder.btnRateMoodActivity.setImageResource(R.drawable.happy);
                 break;
             default:
                 holder.btnRateMoodActivity.setImageResource(R.drawable.aboutus);
                 break;

         }



        holder.editTextActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    if(holder.editTextActivity.getText().toString().isEmpty()){
                        holder.btnRateMoodActivity.setEnabled(false);
                    }
                    else{
                        holder.btnRateMoodActivity.setEnabled(true);
                    }
                    int adapterPosition=holder.getAdapterPosition();
                    String qwe=holder.editTextActivity.getText().toString();
                    onTextChangeListener.onTextChanged(holder.editTextActivity.getText().toString(), adapterPosition);


            }
        });

        holder.editTextActivity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int adapterPosition=holder.getAdapterPosition();
                if(!hasFocus){
                    onTextChangeListener.onTextChanged(holder.editTextActivity.getText().toString(), adapterPosition);
                }
            }
        });


        holder.btnDeleteMoodActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusedView = v.getRootView().findFocus();
                if (focusedView != null && focusedView.hasFocus()) {
                    focusedView.clearFocus();
                }
                if(simpleActivityModel.size()>1){

                    onDeleteActivityClickListener.onDeleteActivityClicked(holder.getAdapterPosition());
                }

            }
        });

        holder.btnRateMoodActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusedView = v.getRootView().findFocus();
                if (focusedView != null && focusedView.hasFocus()) {
                    focusedView.clearFocus();
                }


                    onMoodRateClickListener.onMoodRateClicked(holder.editTextActivity.getText().toString(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {

        return simpleActivityModel.size();
    }






    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editTextActivity;
        private ImageView btnRateMoodActivity,btnDeleteMoodActivity;
        private CardView activity_CardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activity_CardView= itemView.findViewById(R.id.activity_CardView);

            editTextActivity= itemView.findViewById(R.id.editTextActivity);



            btnRateMoodActivity= itemView.findViewById(R.id.btnRateMoodActivity);
            btnDeleteMoodActivity= itemView.findViewById(R.id.btnDeleteMoodActivity);




        }
    }

    public interface OnDeleteActivityClickListener {
        void onDeleteActivityClicked(int position);
    }



    public void setOnDeleteItemClickListener(OnDeleteActivityClickListener listener) {
        this.onDeleteActivityClickListener = listener;
    }



    public interface OnActivityMoodRateClickListener {
        void onMoodRateClicked(String name,int position);
    }



    public void setOnMoodRateClickListener(OnActivityMoodRateClickListener listener) {
        this.onMoodRateClickListener = listener;
    }



    public interface OnTextChangeListener {
        void onTextChanged(String name,int position);
    }



    public void setOnTextChangeListener(OnTextChangeListener listener) {
        this.onTextChangeListener = listener;
    }

}
