package com.mahesh.contactdemo.view.contacts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mahesh.contactdemo.R;
import com.mahesh.contactdemo.adapter.ContactListAdapter;
import com.mahesh.contactdemo.adapter.RecyclerItemTouchHelper;
import com.mahesh.contactdemo.databinding.FragmentContactBinding;
import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.roomdb.ContactDao;
import com.mahesh.contactdemo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.getDefaultUIUtil;

public class ContactsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    //private static int count = 0;
    //private ProgressDialog dialog;

    private ContactsViewModel contactsViewModel;
    List<Contact> contactsArrayList;
    FragmentContactBinding binding;
    ContactListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);
        if(contactsViewModel.getRepository().getDao().getCount()>0){
            contactsViewModel.getContactList().observe(getViewLifecycleOwner(),contacts -> {
                contactsArrayList = contacts;
                adapter = new ContactListAdapter(contacts, getActivity(), contactsViewModel,"0",binding.empty,binding.recyclerView);
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.recyclerView.setAdapter(adapter);

            });
        }else {
            binding.empty.setVisibility(View.VISIBLE);
        }


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView);

        return binding.getRoot();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContactListAdapter.ViewHolder) {

            // get the removed item name to display it in snack bar
            String name = contactsArrayList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Contact deletedItem = contactsArrayList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            /*final View foregroundView = ((ContactListAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);*/


            // remove the item from recycler view
            ShowAlert(deletedItem,deletedIndex,viewHolder);
            //adapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            /*Snackbar snackbar = Snackbar
                    .make(binding.linear1, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("Restore", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/

            /*Snackbar snackbar = Snackbar
                    .make(binding.linear1, "Are you sure you want to remove "+name+" ?", Snackbar.LENGTH_LONG);
            snackbar.setAction("Remove", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.removeItem(deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
        }
    }

  /*  private void getContactList(){
        contactsArrayList=new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur=cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if((cur!=null?cur.getCount():0)>0){
            while(cur!=null&&cur.moveToNext()){
                String id=cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name=cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String contactID=cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Uri my_contact_Uri=Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
                if(cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    Cursor pCur=cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
                            new String[]{id}, null);
                    while(pCur.moveToNext()){
                        String phoneNo=pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if(Utility.chknull(name, "").length()>0){
                            Contact contacts=new Contact();
                            contacts.setName(name);
                            contacts.setPhoneNumber(phoneNo);
                            contacts.setPhoto(String.valueOf(my_contact_Uri));
                            contactsArrayList.add(contacts);
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
            setContactInfo(contactsArrayList);
        }
    }

    public void setContactInfo(List<Contact> contactsList){
        if(contactsViewModel.getRepository().getDao().getCount()<=0){
            count = contactsList.size();
            for(Contact contacts: contactsList){
                new InsertContactAsyncTask(*//*this,*//*contactsViewModel.getRepository().getDao(),dialog).execute(contacts);
            }
            contactsViewModel.getContactList().observe(getViewLifecycleOwner(),contacts -> {
                final ContactListAdapter adapter=new ContactListAdapter(contacts, getContext(), contactsViewModel,"0");
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.recyclerView.setAdapter(adapter);
            });
        }
    }

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDao dao;
        //HomePageActivity homePageActivity;
        AlertDialog dialog;
        List<Contact> contactsList = new ArrayList<>();
        private InsertContactAsyncTask(*//*HomePageActivity homePageActivity,*//* ContactDao dao, AlertDialog dialog){
            this.dao=dao;
            //this.homePageActivity=homePageActivity;
            this.dialog=dialog;
        }
        @Override
        protected Void doInBackground(Contact... model){
            dao.insert(model[0]);
            count--;
            //contactsList = dao.getAllContacts(0);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused){
            super.onPostExecute(unused);
            if(count==0){
                dialog.dismiss();
                //homePageActivity.startActivity(new Intent(homePageActivity, ViewContactsActivity.class).putExtra("flag", "0"));

            }
        }
    }*/

    public void ShowAlert(Contact contact, int position, RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
        alertbox.setMessage("Are you sure you want to delete "+contact.getName()+" this Contact?");
        alertbox.setTitle("Delete Contact");
        alertbox.setPositiveButton(android.R.string.yes, (dialog, which)->{
            //contact.setDeleteFlag(contact.getDeleteFlag()==1?0:1);
            //adapter.removeItem(position);
            contact.setDeleteFlag(contact.getDeleteFlag()==1?0:1);
            if(contact.getFavFlag()==1)
            contact.setFavFlag(0);
            //dao.updateDel(contact.getPhoneNumber(), contact.getDeleteFlag());
            contactsViewModel.updateDelete(contact);
            contactsArrayList.remove(contact);
            if(contactsArrayList.size()==0)
            empty();
            adapter.notifyDataSetChanged();
        }).setNegativeButton(android.R.string.no, (dialog, which)->{
            adapter.notifyDataSetChanged();
                });


        alertbox.show();
    }

    public void empty(){
        binding.empty.setVisibility(contactsArrayList==null||contactsArrayList.size()==0?View.VISIBLE:View.GONE);
        binding.recyclerView.setVisibility(contactsArrayList!=null&&contactsArrayList.size()>0?View.VISIBLE:View.GONE);
    }
}