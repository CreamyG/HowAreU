package com.example.howareu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howareu.R;
import com.example.howareu.model.SimpleTodoModel;

import java.util.List;

public class HomeTodoAdapter extends RecyclerView.Adapter<HomeTodoAdapter.ViewHolder>{

    private List<SimpleTodoModel> simpleTodoModel;
    Context context;
    private HomeTodoAdapter.OnTodoMoodRateClickListener onTodoMoodRateClickListener;

    public HomeTodoAdapter(List<SimpleTodoModel> simpleTodoModel, Context context, HomeTodoAdapter.OnTodoMoodRateClickListener onTodoMoodRateClickListener) {
        this.simpleTodoModel = simpleTodoModel;
        this.context = context;
        this.onTodoMoodRateClickListener = onTodoMoodRateClickListener;
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

        holder.editTextToDoRate.setFocusable(false);
        holder.editTextToDoRate.setClickable(false);
        holder.editTextToDo.setText(simpleTodoModel.get(position).getTodoName());
        holder.editTextToDoRate.setText(simpleTodoModel.get(position).getMoodrate()+"");

        if(simpleTodoModel.get(position).isEnabled()){
            holder.editTextToDo.setEnabled(true);
            holder.editTextToDoRate.setEnabled(true);
            holder.btnRateMoodTodo.setEnabled(true);
        }
        else{
            holder.editTextToDo.setEnabled(false);
            holder.editTextToDoRate.setEnabled(false);
            holder.btnRateMoodTodo.setEnabled(false);
        }


        holder.btnRateMoodTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTodoMoodRateClickListener.onTodoMoodRateClicked(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return simpleTodoModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editTextToDo, editTextToDoRate;
        private Button btnRateMoodTodo;
        private CardView todolist_CardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todolist_CardView= itemView.findViewById(R.id.todolist_CardView);


            editTextToDo= itemView.findViewById(R.id.editTextToDo);
            editTextToDoRate= itemView.findViewById(R.id.editTextToDoRate);
            btnRateMoodTodo= itemView.findViewById(R.id.btnRateMoodTodo);

        }
    }
    public interface OnTodoMoodRateClickListener {
        void onTodoMoodRateClicked(int position);
    }



    public void setOnTodoMoodRateClickListener(HomeTodoAdapter.OnTodoMoodRateClickListener listener) {
        this.onTodoMoodRateClickListener = listener;
    }

}
