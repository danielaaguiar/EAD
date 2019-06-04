package com.eda.ead.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.eda.ead.Adapter.QuestionRecycleViewAdapter;
import com.eda.ead.Adapter.SkillRecycleViewAdapter;
import com.eda.ead.Dialog.GabaritoDialog;
import com.eda.ead.Interface.IQuestionActivity;
import com.eda.ead.Model.Question;
import com.eda.ead.Model.Skill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.method.ScrollingMovementMethod;

import android.widget.TextView;
import android.widget.Toast;

import com.eda.ead.R;


public class TaskActivity extends AppCompatActivity implements
        View.OnClickListener,
		IQuestionActivity,
		SwipeRefreshLayout.OnRefreshListener {

	private static final String TAG = "TaskeDialog";
	public static final String KEY_TOTAL_TASK_NUMBER = "total_task_number";
	public static final String KEY_LESSON_NUMBER = "lesson_number";
	public static final String KEY_SKILL_NUMBER = "skill_number";
	public static final String KEY_CONT_TASK_NUMBER = "task_number";


	//Firebase
	private FirebaseAuth.AuthStateListener mAuthListener;

	//widgets
	private TextView mEnunciado, bt_EnunciadoImagem;
	private FloatingActionButton mFab_next;
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	public ProgressBar mProgressivebar;

	//vars
	private View mParentLayout;
	private ArrayList<Question> mQuestions = new ArrayList<>();
	private ArrayList<com.eda.ead.Model.Task> mTask = new ArrayList<com.eda.ead.Model.Task>();
	private QuestionRecycleViewAdapter mQuestionRecyclerViewAdapter;
	private DocumentSnapshot mLastQueriedDocument;
	private float progress;
	private int control_task_number;
	String skill_number;
	String lesson_number;
	String task_number;
	String total_task_number;


	//---------------------------------------------------------------------------
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		mEnunciado = findViewById(R.id.enunciado);
		//bt_EnunciadoImagem = findViewById(R.id.image_enunciado);
		mFab_next = findViewById(R.id.fab_next_task);
		mParentLayout = findViewById(android.R.id.content);
		mRecyclerView = findViewById(R.id.recycler_view);
		mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
		mProgressivebar = findViewById(R.id.pBar);

		mFab_next = findViewById(R.id.fab_next_task);
		mFab_next.setOnClickListener(this);
		//botao voltar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
		getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão


		mEnunciado.setMovementMethod(new ScrollingMovementMethod());
		initRecyclerView();
		getQuestions();
		getTask();

	}

	//----------------------------------------------------------------------------
	@Override
	public void onRefresh() {
		getQuestions();
		mSwipeRefreshLayout.setRefreshing(false);
	}

	//----------------------------------------------------------------------------
	private void getQuestions() {
		// Recupero a activity
		Intent intent = getIntent();
		skill_number = intent.getStringExtra(LessonActivity.KEY_SKILL_NUMBER);
		lesson_number = intent.getStringExtra(LessonActivity.KEY_LESSON_NUMBER);
		task_number = intent.getStringExtra(LessonActivity.KEY_CONT_TASK_NUMBER);

		// Recupero a activity
		Question question = new Question();

		FirebaseFirestore db = FirebaseFirestore.getInstance();

		CollectionReference questionsCollectionRef = db
				.collection("questions");


		Query questionsQuery = null;
		if (mLastQueriedDocument != null) {
			questionsQuery = questionsCollectionRef
					.whereEqualTo("question_task", task_number)
					.whereEqualTo("question_lesson",lesson_number)
					.whereEqualTo("question_skill", skill_number)
					.orderBy("question_number", Query.Direction.ASCENDING);
		} else {
			questionsQuery = questionsCollectionRef
					.whereEqualTo("question_task", task_number)
					.whereEqualTo("question_lesson",lesson_number)
					.whereEqualTo("question_skill", skill_number)
					.orderBy("question_number", Query.Direction.ASCENDING);
		}

		questionsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if (task.isSuccessful()) {

					for (QueryDocumentSnapshot document : task.getResult()) {
						Question question = document.toObject(Question.class);
						mQuestions.add(question);
						Log.d(TAG, "onComplete: got a new question. Position: " + (mQuestions.size() - 1));
					}

					if (task.getResult().size() != 0) {
						mLastQueriedDocument = task.getResult().getDocuments()
								.get(task.getResult().size() - 1);
					}

					mQuestionRecyclerViewAdapter.notifyDataSetChanged();
				} else {
					makeSnackBarMessage("Query Failed. Check Logs.");
				}
			}
		});
	}
	//----------------------------------------------------------------------------
	private void getTask() {
//====================================================================================================================== Voltar aqui para melhorar a filtragem
		// Recupero a activity
		Intent intent = getIntent();
		skill_number = intent.getStringExtra(LessonActivity.KEY_SKILL_NUMBER);
		lesson_number = intent.getStringExtra(LessonActivity.KEY_LESSON_NUMBER);
		task_number = intent.getStringExtra(LessonActivity.KEY_CONT_TASK_NUMBER);


//			skill_number =  intent.getStringExtra(TaskActivity.KEY_SKILL_NUMBER);
//			lesson_number = intent.getStringExtra(TaskActivity.KEY_LESSON_NUMBER);
//			task_number = intent.getStringExtra(TaskActivity.KEY_CONT_TASK_NUMBER);
//

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference taskCollectionRef = db
                .collection("tasks");

        Query taskQuery = taskCollectionRef
                .whereEqualTo("task_number", skill_number)
                .whereEqualTo("task_lesson", lesson_number)
                .whereEqualTo("task_skill",task_number);
        taskQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        com.eda.ead.Model.Task task1 = document.toObject(com.eda.ead.Model.Task.class);
                        mEnunciado.setText(task1.getTask_enunciado());

                    }}else {
                    makeSnackBarMessage("ERRO!");
                }
            }
        });


		}
	//----------------------------------------------------------------------------
	private void initRecyclerView() {
		if (mQuestionRecyclerViewAdapter == null) {
			mQuestionRecyclerViewAdapter = new QuestionRecycleViewAdapter(this, mQuestions);
		}
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(mQuestionRecyclerViewAdapter);
	}

	//----------------------------------------------------------------------------
	@Override
	public void onClick(View v) {


		Intent intent = getIntent();
		total_task_number = intent.getStringExtra(LessonActivity.KEY_TOTAL_TASK_NUMBER);
		skill_number = intent.getStringExtra(LessonActivity.KEY_SKILL_NUMBER);
		lesson_number = intent.getStringExtra(LessonActivity.KEY_LESSON_NUMBER);
		task_number = intent.getStringExtra(LessonActivity.KEY_CONT_TASK_NUMBER);

		String cont = task_number;
		int cont_int = Integer.parseInt(cont) + 1;
		String cont2 = String.valueOf(cont_int);

		System.out.println("\n");
		System.out.println("\n===================================> Task number: " + cont2);
		System.out.println("\n===================================> Total task number: " + total_task_number);
		System.out.println("\n");

		if (total_task_number == null) {
			Intent intent_2 = new Intent(this, MainActivity.class);
			startActivity(intent_2);

		} else {

			Snackbar.make(v, "Task: " + task_number, Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			//Envia
			Intent intent_3 = new Intent(this, TaskActivity.class);
			intent_3.putExtra(KEY_SKILL_NUMBER, total_task_number);
			intent_3.putExtra(KEY_SKILL_NUMBER, skill_number);
			intent_3.putExtra(KEY_LESSON_NUMBER, lesson_number);
			intent_3.putExtra(KEY_CONT_TASK_NUMBER, cont2);
			startActivity(intent_3);
	}//fim if else
        }

//====================================================================================================================== Falta ir para Score
	//----------------------------------------------------------------------------
	@Override
	public void onQuestionSelected(Question question) {

		Context context = getApplicationContext();
		CharSequence text = question.getQuestion_number();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, "Question: "+text, duration);
		toast.show();

		GabaritoDialog dialog = GabaritoDialog.newInstance(question);
		dialog.show(getSupportFragmentManager(), "gabarito");


	}



	//----------------------------------------------------------------------------

	private void makeSnackBarMessage(String message) {
		Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
	}

	private void setProgressValue(final int progress) {

		mProgressivebar.setProgress(progress + 10);

//		// thread is used to change the progress value
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				setProgressValue(progress + 10);
//			}
//		});
//		thread.start();
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



		}


