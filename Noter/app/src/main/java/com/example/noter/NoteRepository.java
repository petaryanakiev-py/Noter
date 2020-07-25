package com.example.noter;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao mWordDao;
    private LiveData<List<Note>> mAllWords;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    LiveData<List<Note>> getAllWords() {
        Log.i("WordRepository", "getting all words");
        return mAllWords;
    }

    void insert(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.i("WordRepository", "before inserting word");
            mWordDao.insert(note);
            });
    }

    void deleteAllWords() {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.deleteAll();
        });
    }

    void delete(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.delete(note);
        });
    }
}
