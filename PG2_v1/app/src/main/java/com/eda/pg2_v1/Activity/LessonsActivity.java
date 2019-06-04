package com.eda.pg2_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.eda.pg2_v1.Adapter.LessonRecycleViewAdapter;
import com.eda.pg2_v1.Interface.ILessonActivity;
import com.eda.pg2_v1.Interface.IMainActivity;
import com.eda.pg2_v1.Model.Lesson;
import com.eda.pg2_v1.Model.Skill;
import com.eda.pg2_v1.R;
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

import java.util.ArrayList;


public class LessonActivity extends AppCompatActivity implements
        View.OnClickListener,
        ILessonActivity,
        SwipeRefreshLayout.OnRefreshListener
{

    private static final String TAG = "MainActivity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;


    //widgets
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private View mParentLayout;
    private ArrayList<Lesson> mLessons = new ArrayList<>();
    private LessonRecycleViewAdapter mLessonRecyclerViewAdapter;
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
    getLessons();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
    getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

}
//----------------------------------------------------------------------------
@Override
public void onRefresh() {
    getLessons();
    mSwipeRefreshLayout.setRefreshing(false);
}
// ---------------------------------------------------------------------------
private void getLessons(){

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference lessonsCollectionRef = db
            .collection("skills");


    Query lessonsQuery = null;
    if(mLastQueriedDocument != null){
        lessonsQuery = lessonsCollectionRef
               // .whereEqualTo("lesson_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("lesson_id", Query.Direction.ASCENDING)
                .startAfter(mLastQueriedDocument);
    }
    else{
        lessonsQuery = lessonsCollectionRef
                //.whereEqualTo("lesson_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("lesson_id", Query.Direction.ASCENDING);
    }

    lessonsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){

                for(QueryDocumentSnapshot document: task.getResult()){
                    Lesson lesson = document.toObject(Lesson.class);
                    mLessons.add(lesson);
                    Log.d(TAG, "onComplete: got a new lesson. Position: " + (mLessons.size() - 1));
                }

                if(task.getResult().size() != 0){
                    mLastQueriedDocument = task.getResult().getDocuments()
                            .get(task.getResult().size() -1);
                }

                mLessonRecyclerViewAdapter.notifyDataSetChanged();
            }
            else{
                makeSnackBarMessage("Query Failed. Check Logs.");
            }
        }
    });
}
// ---------------------------------------------------------------------------
private void initRecyclerView(){
    if(mLessonRecyclerViewAdapter == null){
        mLessonRecyclerViewAdapter = new LessonRecycleViewAdapter(this, mLessons);
    }
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mLessonRecyclerViewAdapter);
}
// ---------------------------------------------------------------------------
@Override
public void updateLesson(final Lesson lesson, Skill skill){

    FirebaseFirestore db = FirebaseFirestore.getInstance();

   // DocumentReference lessonRef = db
    //        .collection("lessons").document(lesson.getlesson_id());

    DocumentReference lessonRef = db
            .collection("skills").document()
            .collection("lessons").document(lesson.getlesson_id());


    lessonRef.update(
            "lesson_name", lesson.getlesson_name(),
            "lesson_number", lesson.getlesson_number(),
            "lesson_description", lesson.getlesson_description()
    ).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                makeSnackBarMessage("Atualizado!");
                mLessonRecyclerViewAdapter.updateLesson(lesson);
            }
            else{
                makeSnackBarMessage("Failed. Check log.");
            }
        }
    });
}
// ---------------------------------------------------------------------------
@Override
public void onLessonSelected(Lesson lesson) {
     makeSnackBarMessage("Clicou na lesson");
    //ViewLessonDialog dialog = ViewLessonDialog.newInstance(lesson);
    //dialog.show(getSupportFragmentManager(), getString(R.string.dialog_view_lesson));
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
public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
    switch (item.getItemId()) {
        case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
            finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
            break;
        default:break;
    }
    return true;
}
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------

}//fim main activity
