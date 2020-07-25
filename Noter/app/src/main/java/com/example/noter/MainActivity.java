package com.example.noter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private WordViewModel mViewModel;
    private int wordsCount;
    public static final int NEW_WORD_ACTIVITY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordsCount = 0;

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        WordListAdapter adapter = new WordListAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        mViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                Log.i("MainActivity", "ViewModel data changed " + words.size());
                Collections.reverse(words);
                wordsCount = words.size();
                adapter.setWords(words);
                controlTextShowing(wordsCount);
            }
        });

        if (adapter.getItemCount() == 0) {
            findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST);
            }
        });

        FloatingActionButton fabDeleteAllWords = findViewById(R.id.fab_delete);
        fabDeleteAllWords.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to delete all notes?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mViewModel.deleteAllWords();
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word word = adapter.getWordAtPosition(position);
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "You deleted " +
                        word.getWord(), Snackbar.LENGTH_LONG);
                View view = snack.getView();
                view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBackground));
                FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                snack.show();
                mViewModel.delete(word);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            Word word = new Word(Objects.requireNonNull(data.
                    getStringExtra(NewWordActivity.EXTRA_REPLY)));
            Log.i("MainActivity", "before inserting word");
            mViewModel.insert(word);
            findViewById(R.id.empty_text).setVisibility(View.INVISIBLE);
        } else {
            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.empty_not_saved, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBackground));
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
        }
    }

    private void controlTextShowing(int numberOfWords) {
        if (numberOfWords == 0) {
            findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
            findViewById(R.id.fab_delete).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.empty_text).setVisibility(View.INVISIBLE);
            findViewById(R.id.fab_delete).setVisibility(View.VISIBLE);
        }
    }

    public void editNote(View view) {
        TextView tv = (TextView) view;
        String noteText = tv.getText().toString();

        String KEY_NOTE = "key_note";
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(KEY_NOTE, noteText);
        startActivity(intent);
    }
}
