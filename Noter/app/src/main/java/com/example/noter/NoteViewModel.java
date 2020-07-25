package com.example.noter;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mNoteRepository;
    private LiveData<List<Note>> mAllWords;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mAllWords = mNoteRepository.getAllWords();
    }

    LiveData<List<Note>> getAllWords() {
        Log.i("WordViewModel", "getting all words");
        return mAllWords;
    }

    public void insert(Note note) {
        Log.i("WordViewModel", "before inserting word");
        mNoteRepository.insert(note);
    }

    public void deleteAllWords() {
        mNoteRepository.deleteAllWords();
    }

    public void delete(Note note) {
        mNoteRepository.delete(note);
    }
}
