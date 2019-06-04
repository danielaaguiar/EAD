package com.eda.ead.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.eda.ead.Interface.IMainActivity;
import com.eda.ead.Model.Skill;
import com.eda.ead.R;

import android.support.v7.widget.RecyclerView;

//https://www.androidpro.com.br/blog/design-layout/recyclerview-cardview/

public class SkillRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SkillRecyclerViewAdapter";

    private ArrayList<Skill> mSkills = new ArrayList<>();
    private IMainActivity mIMainActivity;
    private Context mContext;
    private int mSelectedSkillIndex;

    public SkillRecycleViewAdapter(Context context, ArrayList<Skill> skills) {

        mSkills = skills;
        mContext = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_skill_list_item, parent, false);

        holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).name.setText(mSkills.get(position).getskill_name());
            ((ViewHolder) holder).number.setText(mSkills.get(position).getskill_number());
        }
    }

    @Override
    public int getItemCount() {
        return mSkills.size();
    }


    public void updateSkill(Skill skill){
        mSkills.get(mSelectedSkillIndex).setskill_number(skill.getskill_number());
        mSkills.get(mSelectedSkillIndex).setskill_name(skill.getskill_name());
        mSkills.get(mSelectedSkillIndex).setskill_description(skill.getskill_description());

        notifyDataSetChanged();
    }

    public void removeSkill(Skill skill){
        mSkills.remove(skill);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mIMainActivity = (IMainActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView number, name;



        public ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.skill_number);
            name = itemView.findViewById(R.id.skill_name);
           // timestamp = itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mSelectedSkillIndex = getAdapterPosition();
            mIMainActivity.onSkillSelected(mSkills.get(mSelectedSkillIndex));
        }
    }
}


