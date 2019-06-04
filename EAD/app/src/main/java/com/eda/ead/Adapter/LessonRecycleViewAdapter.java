package com.eda.ead.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eda.ead.Interface.ILessonActivity;
import com.eda.ead.Model.Lesson;
import com.eda.ead.R;

import java.util.ArrayList;

//https://www.androidpro.com.br/blog/design-layout/recyclerview-cardview/

public class LessonRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "LessonRecyclerViewAdapter";

    private ArrayList<Lesson> mLessons = new ArrayList<>();
    private ILessonActivity mILessonActivity;
    private Context mContext;
    private int mSelectedLessonIndex;

    public LessonRecycleViewAdapter(Context context, ArrayList<Lesson> lessons) {

        mLessons = lessons;
        mContext = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_lessons_list_item, parent, false);

        holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).name.setText(mLessons.get(position).getlesson_name());
            ((ViewHolder) holder).number.setText(mLessons.get(position).getlesson_number());
        }
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }


    public void updateLesson(Lesson lesson){
        mLessons.get(mSelectedLessonIndex).setlesson_number(lesson.getlesson_number());
        mLessons.get(mSelectedLessonIndex).setlesson_name(lesson.getlesson_name());
        mLessons.get(mSelectedLessonIndex).setlesson_description(lesson.getlesson_description());

        notifyDataSetChanged();
    }

    public void removeLesson(Lesson lesson){
        mLessons.remove(lesson);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mILessonActivity = (ILessonActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView number, name;



        public ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.lesson_number);
            name = itemView.findViewById(R.id.lesson_name);
           // timestamp = itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mSelectedLessonIndex = getAdapterPosition();
            mILessonActivity.onLessonSelected(mLessons.get(mSelectedLessonIndex));
        }
    }
}


