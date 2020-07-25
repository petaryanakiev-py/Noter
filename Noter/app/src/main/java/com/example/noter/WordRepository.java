package com.example.noter;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    LiveData<List<Word>> getAllWords() {
        Log.i("WordRepository", "getting all words");
        return mAllWords;
    }

    void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.i("WordRepository", "before inserting word");
            mWordDao.insert(word);
            });
    }

    void deleteAllWords() {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.deleteAll();
        });
    }

    void delete(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.delete(word);
        });
    }
}
