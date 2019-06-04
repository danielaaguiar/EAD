package com.eda.ead.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.eda.ead.Config.Config;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.eda.ead.Adapter.LessonRecycleViewAdapter;
import com.eda.ead.Model.Lesson;
import com.eda.ead.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class LessonActivity extends AppCompatActivity  implements YouTubePlayer.OnInitializedListener, View.OnClickListener{

    private static final String TAG = "LessonActivity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;


    private Lesson lesson;

    //widgets
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private View mParentLayout;
    private ArrayList<Lesson> mLessons = new ArrayList<>();
    private LessonRecycleViewAdapter mLessonRecyclerViewAdapter;
    private DocumentSnapshot mLastQueriedDocument;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    public TextView mNumber_lesson, mName_lesson, mYoutube_lesson;
    public Boolean isFABOpen = false;

    //widget
    FloatingActionButton fab_pdf;
    FloatingActionButton fab_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        fab_task = findViewById(R.id.fab_task);
        fab_pdf = findViewById(R.id.fab_pdf);
        fab_task.setOnClickListener(this);
        fab_pdf.setOnClickListener(this);


        FloatingActionButton fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);
        fab_task = (FloatingActionButton) findViewById(R.id.fab_task);
        fab_pdf  = (FloatingActionButton) findViewById(R.id.fab_pdf);
        //fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //botao voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão


        YouTubePlayerSupportFragment youTubeView = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        mNumber_lesson = (TextView) findViewById(R.id.lesson_number);
        mName_lesson = (TextView) findViewById(R.id.lesson_name);

        //Recupero a activity
        Intent intent = getIntent();
        mNumber_lesson.setText(intent.getStringExtra(LessonsActivity.KEY_LESSON_NUMBER));
        mName_lesson.setText(intent.getStringExtra(LessonsActivity.KEY_LESSON_NAME));

    }

    // ---------------------------------------------------------------------------
    
    private void showFABMenu(){
        isFABOpen=true;
        fab_task.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_pdf.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
       // fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_300));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_task.animate().translationY(0);
        fab_pdf.animate().translationY(0);
        //fab3.animate().translationY(0);
    }
// ---------------------------------------------------------------------------
 @Override
public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar

        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
               // finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }
// ---------------------------------------------------------------------------
@Override
public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
    if (!wasRestored) {

        Intent intent = getIntent();
        String yt = intent.getStringExtra(LessonsActivity.KEY_LESSON_YOUTUBE);
        player.cueVideo(yt); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
    }
}
// ---------------------------------------------------------------------------
 @Override
 public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
// ---------------------------------------------------------------------------
 @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }
// ---------------------------------------------------------------------------
protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
// ---------------------------------------------------------------------------
private void makeSnackBarMessage(String message){
    Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
}
// ---------------------------------------------------------------------------
public void onClick(View view) {

    switch (view.getId()){

        case R.id.fab_pdf:{
            Snackbar.make(view, "Boa leitura!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            Uri uri = Uri.parse("https://drive.google.com/file/d/0BzzLinwqA8EVeS1wcC1lUlE0WjQ/view");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            break;
        }
        case R.id.fab_task:{

            Snackbar.make(view, "TAREFA", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
            break;
        }
    }
}




}


