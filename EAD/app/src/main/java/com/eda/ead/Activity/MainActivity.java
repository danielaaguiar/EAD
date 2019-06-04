package com.eda.ead.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.eda.ead.R;

import com.eda.ead.Interface.IMainActivity;
import com.eda.ead.Model.Skill;
import com.eda.ead.Adapter.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        IMainActivity,
        SwipeRefreshLayout.OnRefreshListener
{

    public static final String KEY_SKILL_NUMBER = "skill_number";
    private static final String TAG = "MainActivity";



    public int value_skill_id;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;


    //widgets
   // private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private View mParentLayout;
    private ArrayList<Skill> mSkills = new ArrayList<>();
    private SkillRecycleViewAdapter mSkillRecyclerViewAdapter;
    private DocumentSnapshot mLastQueriedDocument;
//---------------------------------------------------------------------------
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
   // mFab = findViewById(R.id.fab);
    mParentLayout = findViewById(android.R.id.content);
    mRecyclerView = findViewById(R.id.recycler_view);
    mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

   // mFab.setOnClickListener(this);
    mSwipeRefreshLayout.setOnRefreshListener(this);

    //setupFirebaseAuth();
    initRecyclerView();
    getSkills();
}
//----------------------------------------------------------------------------
@Override
public void onRefresh() {
    getSkills();
    mSwipeRefreshLayout.setRefreshing(false);
}
// ---------------------------------------------------------------------------
private void getSkills(){

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference skillsCollectionRef = db
            .collection("skills");

    Query skillsQuery = null;
    if(mLastQueriedDocument != null){
        skillsQuery = skillsCollectionRef
               // .whereEqualTo("skill_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("skill_number", Query.Direction.ASCENDING)
                .startAfter(mLastQueriedDocument);
    }
    else{
        skillsQuery = skillsCollectionRef
                //.whereEqualTo("skill_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("skill_number", Query.Direction.ASCENDING);
    }

    skillsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){

                for(QueryDocumentSnapshot document: task.getResult()){
                    Skill skill = document.toObject(Skill.class);
                    mSkills.add(skill);
                    Log.d(TAG, "onComplete: got a new skill. Position: " + (mSkills.size() - 1));
                }

                if(task.getResult().size() != 0){
                    mLastQueriedDocument = task.getResult().getDocuments()
                            .get(task.getResult().size() -1);
                }

                mSkillRecyclerViewAdapter.notifyDataSetChanged();
            }
            else{
                makeSnackBarMessage("Query Failed. Check Logs.");
            }
        }
    });
}
// ---------------------------------------------------------------------------
private void initRecyclerView(){
    if(mSkillRecyclerViewAdapter == null){
        mSkillRecyclerViewAdapter = new SkillRecycleViewAdapter(this, mSkills);
    }
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mSkillRecyclerViewAdapter);
}
// ---------------------------------------------------------------------------
@Override
public void updateSkill(final Skill skill){

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DocumentReference skillRef = db
            .collection("skills")
            .document(skill.getskill_id());

    skillRef.update(
            "skill_name", skill.getskill_name(),
            "skill_number", skill.getskill_number(),
            "skill_description", skill.getskill_description()
    ).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                makeSnackBarMessage("Atualizado!");
                mSkillRecyclerViewAdapter.updateSkill(skill);
            }
            else{
                makeSnackBarMessage("Failed. Check log.");
            }
        }
    });
}
// ---------------------------------------------------------------------------
private void makeSnackBarMessage(String message){
    Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
}
// ---------------------------------------------------------------------------
@Override
public void onClick(View view) {
}
// ---------------------------------------------------------------------------
@Override
public void onSkillSelected(Skill skill) {

    Context context = getApplicationContext();
    CharSequence text = skill.getskill_number();
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(context, "Skill: "+text, duration);
    toast.show();

    String skill_number = skill.getskill_number();

    Intent intent = new Intent(this, LessonsActivity.class);
    intent.putExtra(KEY_SKILL_NUMBER, skill_number);
    startActivity(intent);
    }
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------

}//fim main activity
