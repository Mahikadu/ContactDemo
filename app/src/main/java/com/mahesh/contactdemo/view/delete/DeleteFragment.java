package com.mahesh.contactdemo.view.delete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahesh.contactdemo.R;
import com.mahesh.contactdemo.adapter.ContactListAdapter;
import com.mahesh.contactdemo.databinding.FragmentDeleteBinding;
import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.viewmodel.ContactsViewModel;

import java.util.List;

public class DeleteFragment extends Fragment {

    FragmentDeleteBinding binding;
    List<Contact> favContactsArrayList;
    ContactListAdapter adapter;
    private ContactsViewModel contactsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delete, container, false);
        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);

        if (contactsViewModel.getRepository().getDao().getDeleteCount() > 0) {
            contactsViewModel.getDeleteContactList().observe(getViewLifecycleOwner(), contacts -> {
                favContactsArrayList = contacts;
                adapter = new ContactListAdapter(contacts, getContext(), contactsViewModel, "2", binding.empty, binding.recyclerView);
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.recyclerView.setAdapter(adapter);

            });
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.empty.setVisibility(View.VISIBLE);
        }

        binding.fabRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favContactsArrayList.size() > 0) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
                    alertbox.setMessage("Are you sure you want to restore all contact?");
                    alertbox.setTitle("Restore All Contact");
                    alertbox.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        contactsViewModel.restoreAll();
                        favContactsArrayList.clear();
                        adapter.notifyDataSetChanged();
                        empty();
                    }).setNegativeButton(android.R.string.no, (dialog, which) -> {
                        adapter.notifyDataSetChanged();
                    });


                    alertbox.show();
                } else {
                    Toast.makeText(getActivity(), "Contact not found for restore..", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return binding.getRoot();
    }

    public void empty() {
        binding.empty.setVisibility(favContactsArrayList == null || favContactsArrayList.size() == 0 ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(favContactsArrayList != null && favContactsArrayList.size() > 0 ? View.VISIBLE : View.GONE);
    }
}