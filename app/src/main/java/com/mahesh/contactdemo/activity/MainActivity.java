package com.mahesh.contactdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mahesh.contactdemo.R;
import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.roomdb.ContactDao;
import com.mahesh.contactdemo.utils.Utility;
import com.mahesh.contactdemo.view.contacts.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static int count = 0;
    ArrayList<Contact> contactsArrayList;
    private ContactsViewModel contactsViewModel;
    private ProgressDialog dialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            checkPermission(this);
        }

    }



    public void checkPermission(Activity context) {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
                    + ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                //Request Location Permission
                checkALLPermission(context);
            }else{
                getContact();
            }
        }
    }
    //}

    public void checkALLPermission(final Activity activity) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CONTACTS)) {

            // Show an explanation to the user asynchronously -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(activity)
                    .setTitle("Permission Needed")
                    .setMessage("This app needs all the Permissions for proper functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utility.showLog("permission granted");
                            //Prompt the user once explanation has been shown
                            //showRationale(activity);

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivityForResult(intent,100);
                        }
                    })
                    .create()
                    .show();

        } else {
            // No explanation needed, we can request the permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,new String[]{
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS},
                        REQUEST_ID_MULTIPLE_PERMISSIONS);

            }else{
                getContact();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    getContact();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    checkPermission(MainActivity.this);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    BottomNavigationView navView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_contact, R.id.navigation_favorites, R.id.navigation_delete)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        checkPermission(MainActivity.this);


    }

    public void getContact(){
        if (contactsViewModel.getRepository().getDao().getCount() > 0) {
            navigationUI();
        } else {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.show();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getContactList();
                }
            }, 2000);
        }
    }

    private void getContactList() {
        contactsArrayList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String contactID = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (Utility.chknull(name, "").length() > 0) {
                            Contact contacts = new Contact();
                            contacts.setName(name);
                            contacts.setPhoneNumber(phoneNo.replaceAll(" ",""));
                            contacts.setPhoto(String.valueOf(my_contact_Uri));
                            if(!contactsArrayList.contains(contacts))
                            contactsArrayList.add(contacts);
                        }
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
            setContactInfo(contactsArrayList);
        }
    }

    public void setContactInfo(List<Contact> contactsList) {
        if (contactsViewModel.getRepository().getDao().getCount() <= 0) {
            count = contactsList.size();
            for (Contact contacts : contactsList) {
                new InsertContactAsyncTask(this,contactsViewModel.getRepository().getDao(), dialog).execute(contacts);
            }
            /* contactsViewModel.getContactList().observe(getViewLifecycleOwner(),contacts -> {
                final ContactListAdapter adapter=new ContactListAdapter(contacts, getContext(), contactsViewModel,"0");
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.recyclerView.setAdapter(adapter);
            });*/
        }
    }

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        AlertDialog dialog;
        List<Contact> contactsList = new ArrayList<>();
        private ContactDao dao;
        MainActivity homePageActivity;

        private InsertContactAsyncTask(MainActivity homePageActivity, ContactDao dao, AlertDialog dialog) {
            this.dao = dao;
            this.dialog = dialog;
            this.homePageActivity = homePageActivity;
        }

        @Override
        protected Void doInBackground(Contact... model) {
            dao.insert(model[0]);
            count--;
            //contactsList = dao.getAllContacts(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (count == 0) {
                dialog.dismiss();
                homePageActivity.navigationUI();

                //homePageActivity.startActivity(new Intent(homePageActivity, ViewContactsActivity.class).putExtra("flag", "0"));

            }
        }
    }

    public void navigationUI(){
        navController.navigate(R.id.navigation_contact);
    }

}