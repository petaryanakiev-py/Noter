package com.example.noter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class EditNoteActivity extends AppCompatActivity {

    private static final String NOTE_KEY = "key_note";

    private NoteViewModel mViewModel;

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

        mViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        Button editButton = findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedText = editNote.getText().toString();
                Note updatingNote = new Note(editedText);
                Note oldNote = new Note(noteText);
                mViewModel.delete(oldNote);
                mViewModel.insert(updatingNote);

                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
