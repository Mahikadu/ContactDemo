package com.mahesh.contactdemo.view.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahesh.contactdemo.R;
import com.mahesh.contactdemo.adapter.ContactListAdapter;
import com.mahesh.contactdemo.databinding.FragmentFavoritesBinding;
import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.view.contacts.ContactsViewModel;

import java.util.List;

public class FavoritesFragment extends Fragment {

    FragmentFavoritesBinding binding;
    private ContactsViewModel contactsViewModel;
    List<Contact> favContactsArrayList;
    ContactListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);
        /*View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        favoritesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        if(contactsViewModel.getRepository().getDao().getFavCount()>0){
            contactsViewModel.getFavContactList().observe(getViewLifecycleOwner(),contacts -> {
                favContactsArrayList = contacts;
                adapter = new ContactListAdapter(contacts, getContext(), contactsViewModel,"1",binding.empty,binding.recyclerView);
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.recyclerView.setAdapter(adapter);

            });
        }else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.empty.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }
}