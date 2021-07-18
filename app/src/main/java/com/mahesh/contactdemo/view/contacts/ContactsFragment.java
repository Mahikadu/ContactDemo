package com.mahesh.contactdemo.view.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahesh.contactdemo.R;
import com.mahesh.contactdemo.adapter.ContactListAdapter;
import com.mahesh.contactdemo.adapter.RecyclerItemTouchHelper;
import com.mahesh.contactdemo.databinding.FragmentContactBinding;
import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.viewmodel.ContactsViewModel;

import java.util.List;

public class ContactsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    List<Contact> contactsArrayList;
    FragmentContactBinding binding;
    ContactListAdapter adapter;
    private ContactsViewModel contactsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        contactsViewModel =
                new ViewModelProvider(this).get(ContactsViewModel.class);
        if (contactsViewModel.getRepository().getDao().getCount() > 0) {
            contactsViewModel.getContactList().observe(getViewLifecycleOwner(), contacts -> {
                contactsArrayList = contacts;
                adapter = new ContactListAdapter(contacts, getActivity(), contactsViewModel, "0", binding.empty, binding.recyclerView);
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.recyclerView.setAdapter(adapter);

            });
        } else {
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

            // remove the item from recycler view
            ShowAlert(deletedItem, deletedIndex, viewHolder);

        }
    }

    public void ShowAlert(Contact contact, int position, RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
        alertbox.setMessage("Are you sure you want to delete " + contact.getName() + " this Contact?");
        alertbox.setTitle("Delete Contact");
        alertbox.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            contact.setDeleteFlag(contact.getDeleteFlag() == 1 ? 0 : 1);
            if (contact.getFavFlag() == 1)
                contact.setFavFlag(0);
            contactsViewModel.updateDelete(contact);
            contactsArrayList.remove(contact);
            if (contactsArrayList.size() == 0)
                empty();
            adapter.notifyDataSetChanged();
        }).setNegativeButton(android.R.string.no, (dialog, which) -> {
            adapter.notifyDataSetChanged();
        });


        alertbox.show();
    }

    public void empty() {
        binding.empty.setVisibility(contactsArrayList == null || contactsArrayList.size() == 0 ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(contactsArrayList != null && contactsArrayList.size() > 0 ? View.VISIBLE : View.GONE);
    }
}