package com.mahesh.contactdemo.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.roomdb.ContactDao;
import com.mahesh.contactdemo.roomdb.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;
    private MutableLiveData<List<Contact>> mutableContactLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Contact>> mutableFavLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Contact>> mutableDeleteLiveData = new MutableLiveData<>();
    private Application application;

    public ContactRepository(Application application) {

        this.application = application;
        ContactRoomDatabase soilRoomDatabase = ContactRoomDatabase.getDatabase(application);
        contactDao = soilRoomDatabase.Dao();

    }

    public MutableLiveData<List<Contact>> getMutableContactLiveData() {

        mutableContactLiveData.setValue(contactDao.getAllContacts(0));

        return mutableContactLiveData;
    }

    public MutableLiveData<List<Contact>> getMutableFavLiveData() {

        mutableFavLiveData.setValue(contactDao.getAllFavouriteContacts(1));

        return mutableFavLiveData;
    }

    public MutableLiveData<List<Contact>> getMutableDeleteLiveData() {

        mutableDeleteLiveData.setValue(contactDao.getAllDeletedContacts(1));

        return mutableDeleteLiveData;
    }

    public void updateDelete(Contact contact) {
        new updateDeleteAsyncTask(contactDao).execute(contact);
    }

    private static class updateDeleteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao mAsyncTaskDao;

        updateDeleteAsyncTask(ContactDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            try{
                mAsyncTaskDao.updateDel(params[0].getPhoneNumber(), params[0].getDeleteFlag(), params[0].getFavFlag());
            }catch (Exception e){e.printStackTrace();}
            return null;
        }
    }

    public void updateFavourite(Contact contact) {
        new updateFavouriteAsyncTask(contactDao).execute(contact);
    }

    private static class updateFavouriteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao mAsyncTaskDao;

        updateFavouriteAsyncTask(ContactDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            try{
                mAsyncTaskDao.updateFav(params[0].getPhoneNumber(), params[0].getFavFlag());
            }catch (Exception e){e.printStackTrace();}
            return null;
        }
    }

    public void restoreAll() {
        new restoreAllAsyncTask(contactDao).execute();
    }

    private static class restoreAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private ContactDao mAsyncTaskDao;

        restoreAllAsyncTask(ContactDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                mAsyncTaskDao.restoreAll();
            }catch (Exception e){e.printStackTrace();}
            return null;
        }
    }

    public ContactDao getDao(){
        return contactDao;
    }
}
