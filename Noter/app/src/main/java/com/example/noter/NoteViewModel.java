package com.example.noter;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mWordRepository;
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mWordRepository = new WordRepository(application);
        mAllWords = mWordRepository.getAllWords();
    }

    LiveData<List<Word>> getAllWords() {
        Log.i("WordViewModel", "getting all words");
        return mAllWords;
    }

    public void insert(Word word) {
        Log.i("WordViewModel", "before inserting word");
        mWordRepository.insert(word);
    }

    public void deleteAllWords() {
        mWordRepository.deleteAllWords();
    }

    public void delete(Word word) {
        mWordRepository.delete(word);
    }
}
