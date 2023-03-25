package com.example.howareu.adapter;

import android.content.Context;
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
import com.example.howareu.model.SimpleTodoModel;

import java.util.List;

public class HomeTodoAdapter extends RecyclerView.Adapter<HomeTodoAdapter.ViewHolder>{

    private List<SimpleTodoModel> simpleTodoModel;
    Context context;
    private HomeTodoAdapter.OnTodoMoodRateClickListener onTodoMoodRateClickListener;
    private OnReplaceClickListener onReplaceClickListener;

    public HomeTodoAdapter(List<SimpleTodoModel> simpleTodoModel, Context context, HomeTodoAdapter.OnTodoMoodRateClickListener onTodoMoodRateClickListener,OnReplaceClickListener onReplaceClickListener) {
        this.simpleTodoModel = simpleTodoModel;
        this.context = context;
        this.onTodoMoodRateClickListener = onTodoMoodRateClickListener;
        this.onReplaceClickListener = onReplaceClickListener;
    }

    @NonNull
    @Override
    public HomeTodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todolist,parent,false);
        HomeTodoAdapter.ViewHolder vh= new HomeTodoAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTodoAdapter.ViewHolder holder, int position) {

        holder.editTextToDo.setFocusable(false);
        holder.editTextToDo.setClickable(false);

        holder.editTextToDo.setText(simpleTodoModel.get(position).getTodoName());

        if(simpleTodoModel.get(position).isEnabled()){
            holder.editTextToDo.setEnabled(true);
            holder.btnRateMoodTodo.setEnabled(true);
            holder.btnReplace.setEnabled(true);
        }
        else{
            holder.editTextToDo.setEnabled(false);
            holder.btnRateMoodTodo.setEnabled(false);
            holder.btnReplace.setEnabled(false);
        }

        switch(simpleTodoModel.get(position).getMoodrate()){

            case Integers.MOOD_PERCENT_SAD:
                holder.btnRateMoodTodo.setImageResource(R.drawable.sad);
                break;
            case Integers.MOOD_PERCENT_VERY_SAD:
                holder.btnRateMoodTodo.setImageResource(R.drawable.very_sad);
                break;
            case Integers.MOOD_PERCENT_NEUTRAL:
                holder.btnRateMoodTodo.setImageResource(R.drawable.neutral);
                break;
            case Integers.MOOD_PERCENT_HAPPY:
                holder.btnRateMoodTodo.setImageResource(R.drawable.smiling_face);
                break;
            case Integers.MOOD_PERCENT_VERY_HAPPY:
                holder.btnRateMoodTodo.setImageResource(R.drawable.happy);
                break;
            default:
                holder.btnRateMoodTodo.setImageResource(R.drawable.very_happy);
                break;

        }


        holder.btnRateMoodTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTodoMoodRateClickListener.onTodoMoodRateClicked(holder.getAdapterPosition());
            }
        });

        holder.btnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReplaceClickListener.onReplaceClicked(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return simpleTodoModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editTextToDo;

        private ImageView btnRateMoodTodo,btnReplace;
        private CardView todolist_CardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todolist_CardView= itemView.findViewById(R.id.todolist_CardView);


            editTextToDo= itemView.findViewById(R.id.editTextToDo);
            btnRateMoodTodo= itemView.findViewById(R.id.btnRateMoodTodo);
            btnReplace= itemView.findViewById(R.id.btnReplace);

        }
    }
    public interface OnTodoMoodRateClickListener {
        void onTodoMoodRateClicked(int position);
    }



    public void setOnTodoMoodRateClickListener(HomeTodoAdapter.OnTodoMoodRateClickListener listener) {
        this.onTodoMoodRateClickListener = listener;
    }

    public interface OnReplaceClickListener {
        void onReplaceClicked(int position);
    }

    public void setOnReplaceClickListener(OnReplaceClickListener onReplaceClickListener){
        this.onReplaceClickListener = onReplaceClickListener;
    }

}
