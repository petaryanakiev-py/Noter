package com.example.noter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class EditNoteActivity extends AppCompatActivity {

    private static final String NOTE_KEY = "key_note";

    private WordViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        String noteText = intent.getStringExtra(NOTE_KEY);

        EditText editNote = findViewById(R.id.edit_note);
        editNote.setText(noteText, EditText.BufferType.EDITABLE);
        if (editNote.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.
                    SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        Button editButton = findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedText = editNote.getText().toString();
                Word updatingWord = new Word(editedText);
                Word oldWord = new Word(noteText);
                mViewModel.delete(oldWord);
                mViewModel.insert(updatingWord);

                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
