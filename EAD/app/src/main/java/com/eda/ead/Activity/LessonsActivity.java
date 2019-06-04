package com.eda.ead.Activity;

import android.content.Context;
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
import android.widget.TextView;

import com.eda.ead.Activity.MainActivity;
import com.eda.ead.Adapter.LessonRecycleViewAdapter;
import com.eda.ead.Interface.ILessonActivity;
import com.eda.ead.Interface.IMainActivity;
import com.eda.ead.Model.Lesson;
import com.eda.ead.Model.Skill;
import com.eda.ead.R;
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
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class LessonsActivity extends AppCompatActivity implements
        View.OnClickListener,
        ILessonActivity,
        SwipeRefreshLayout.OnRefreshListener
{

    private static final String TAG = "LessonsActivity";
    public static final String KEY_LESSON_NUMBER = "lesson_number";
    public static final String KEY_LESSON_NAME = "lesson_name";
    public static final String KEY_LESSON_YOUTUBE = "lesson_youtube";
    public static final String KEY_LESSON_PDF = "lesson_pdf";

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
    setContentView(R.layout.activity_lessons);
    // mFab = findViewById(R.id.fab);
    mParentLayout = findViewById(android.R.id.content);
    mRecyclerView = findViewById(R.id.recycler_view);
    mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

    // mFab.setOnClickListener(this);
    mSwipeRefreshLayout.setOnRefreshListener(this);

    //setupFirebaseAuth();
    initRecyclerView();
    getLessons();

    //botao voltar
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

    // Recupero a activity
    Intent intent = getIntent();
    String skill_number = intent.getStringExtra(MainActivity.KEY_SKILL_NUMBER);


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference lessonsCollectionRef = db
            .collection("lessons");


    Query lessonsQuery = null;
    if(mLastQueriedDocument != null){
        lessonsQuery = lessonsCollectionRef
                .whereEqualTo("lesson_skill", skill_number)
                .orderBy("lesson_number", Query.Direction.ASCENDING);
                //.startAfter(mLastQueriedDocument);
    }
    else{
        lessonsQuery = lessonsCollectionRef
                .whereEqualTo("lesson_skill", skill_number)
                .orderBy("lesson_number", Query.Direction.ASCENDING);
               // .startAfter(mLastQueriedDocument);
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

public void updateLesson(final Lesson lesson, Skill skill){

    FirebaseFirestore db = FirebaseFirestore.getInstance();

   // DocumentReference lessonRef = db
    //        .collection("lessons").document(lesson.getlesson_id());

    DocumentReference lessonRef = db
            .collection("lessons").document();


    lessonRef.update(
            "lesson_name", lesson.getlesson_name(),
            "lesson_number", lesson.getlesson_number()
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
     //makeSnackBarMessage("Clicou na lesson");
    //startActivity(new Intent(this, LessonActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
    //finishAffinity();
    //ViewLessonDialog dialog = ViewLessonDialog.newInstance(lesson);
    //dialog.show(getSupportFragmentManager(), getString(R.string.dialog_view_lesson));

    Context context = getApplicationContext();
    CharSequence text = lesson.getlesson_number();
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(context, "Lesson: "+text, duration);
    toast.show();


    Intent intent = new Intent(this, LessonActivity.class);
    intent.putExtra(KEY_LESSON_NUMBER, lesson.getlesson_number());
    intent.putExtra(KEY_LESSON_NAME, lesson.getlesson_name());
    intent.putExtra(KEY_LESSON_YOUTUBE, lesson.getLesson_youtube());
    intent.putExtra(KEY_LESSON_PDF, lesson.getLesson_pdf());
    startActivity(intent);
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
