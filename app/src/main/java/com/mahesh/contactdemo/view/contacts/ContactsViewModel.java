package com.mahesh.contactdemo.view.contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.repository.ContactRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private ContactRepository contactRepository;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        contactRepository = new ContactRepository(application);
    }

    public ContactRepository getRepository(){
        return contactRepository;
    }

    public MutableLiveData<List<Contact>> getContactList(){
        return contactRepository.getMutableContactLiveData();
    }

    public MutableLiveData<List<Contact>> getFavContactList(){
        return contactRepository.getMutableFavLiveData();
    }

    public MutableLiveData<List<Contact>> getDeleteContactList(){
        return contactRepository.getMutableDeleteLiveData();
    }

    public void updateDelete(Contact contact){
        contactRepository.updateDelete(contact);
    }

    public void updateFavourite(Contact contact){
        contactRepository.updateFavourite(contact);
    }

    public void restoreAll(){
        contactRepository.restoreAll();
    }


    public LiveData<String> getText() {
        return mText;
    }
}