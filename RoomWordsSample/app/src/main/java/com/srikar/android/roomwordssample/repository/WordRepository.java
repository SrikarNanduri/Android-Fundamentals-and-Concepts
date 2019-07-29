package com.srikar.android.roomwordssample.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.srikar.android.roomwordssample.database.WordDao;
import com.srikar.android.roomwordssample.database.WordRoomDatabase;
import com.srikar.android.roomwordssample.model.Word;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }


    // Get All Data
    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }


    // Insert
    public void insert (Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    // Delete All Data
    public void deleteAll()  {
        new deleteAllWordsAsyncTask(mWordDao).execute();
    }

    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    // Delete a Single Word
    public void deleteWord(Word word){
        new deleteWordAsyncTask(mWordDao).execute(word);
    }

    private static class deleteWordAsyncTask extends AsyncTask<Word, Void, Void>{
        private WordDao mAsyncTaskDao;

        deleteWordAsyncTask(WordDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            mAsyncTaskDao.deleteWord(words[0]);
            return null;
        }
    }

    // Update a word
    public void update(Word word){
        new updateAsyncTask(mWordDao).execute(word);
    }


    private static class updateAsyncTask extends AsyncTask<Word, Void, Void>{
        private WordDao mAsyncTaskDao;

        updateAsyncTask(WordDao dao){
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Word... words) {

            if(words != null){
                mAsyncTaskDao.update(words[0]);
            }

            return null;
        }
    }

}
