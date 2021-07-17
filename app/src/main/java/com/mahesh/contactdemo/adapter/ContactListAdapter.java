package com.mahesh.contactdemo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.mahesh.contactdemo.R;
import com.mahesh.contactdemo.model.Contact;
import com.mahesh.contactdemo.roomdb.ContactDao;
import com.mahesh.contactdemo.utils.Utility;
import com.mahesh.contactdemo.view.contacts.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.security.auth.callback.Callback;

import static com.mahesh.contactdemo.utils.Utility.chkNull;
import static com.mahesh.contactdemo.utils.Utility.chknull;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{

    ContactsViewModel viewModel;
    String flag;
    ContactDao dao;
    private List<Contact> contactsArrayList;
    private Context context;
    private TextView txt;
    private RecyclerView recyclerView;

    public ContactListAdapter(List<Contact> contactsArrayList, Context context,
                              ContactsViewModel viewModel, String flag, TextView txt, RecyclerView recyclerView){
        this.contactsArrayList=contactsArrayList;
        this.context=context;
        this.viewModel=viewModel;
        this.flag=flag;
        this.txt = txt;
        this.recyclerView = recyclerView;
        dao = viewModel.getRepository().getDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i){
        View view=LayoutInflater.from(context).inflate(R.layout.contacts_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i){
        Contact contact=contactsArrayList.get(i);
        Random r = new Random();
        int red=r.nextInt(255 - 0 + 1)+0;
        int green=r.nextInt(255 - 0 + 1)+0;
        int blue=r.nextInt(255 - 0 + 1)+0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.rgb(red,green,blue));

        viewHolder.contactName.setText(chkNull(contact.getName(), "NA"));
        viewHolder.contactNumber.setText(chkNull(contact.getPhoneNumber(), "NA"));
        //viewHolder.textView.setBackgroundResource(R.drawable.bg_circle);
        viewHolder.textView.setTextColor(Color.WHITE);
        viewHolder.textView.setText(chknull(contact.getName().substring(0, 1), "NA"));
        viewHolder.buttonDelete.setVisibility(flag.equals("1")?View.VISIBLE:flag.equals("2")?View.GONE:View.VISIBLE);
        viewHolder.buttonFavourite.setVisibility(flag.equals("1")?View.VISIBLE:flag.equals("0")?View.VISIBLE:View.GONE);
        //viewHolder.buttonFavourite.setText(flag.equals("2")?"Remove from favourites":contact.getFavFlag()==1?context.getString(R.string.mark_as_favourite):"Favourite");
        //viewHolder.buttonDelete.setText(flag.equals("1")||contact.getDeleteFlag()==1?context.getString(R.string.restore):"Delete");
        //viewHolder.buttonDelete.setBackgroundColor(context.getColor(flag.equals("1")?R.color.green2:R.color.red));
        //viewHolder.buttonFavourite.setBackgroundColor(context.getColor(R.color.blue));
        viewHolder.buttonRestore.setVisibility(flag.equals("2")?View.VISIBLE:View.GONE);

        viewHolder.buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                alertbox.setMessage("Are you sure you want to restore "+contact.getName()+" this Contact?");
                alertbox.setTitle("Restore Contact");
                alertbox.setPositiveButton(android.R.string.yes, (dialog, which)->{
                    //contact.setDeleteFlag(contact.getDeleteFlag()==1?0:1);
                    //adapter.removeItem(position);
                    contact.setDeleteFlag(contact.getDeleteFlag()==1?0:1);
                    //dao.updateDel(contact.getPhoneNumber(), contact.getDeleteFlag());
                    viewModel.updateDelete(contact);
                    contactsArrayList.remove(contact);
                    if(contactsArrayList.size()==0)
                        empty(txt,recyclerView);
                    notifyDataSetChanged();
                }).setNegativeButton(android.R.string.no, (dialog, which)->{
                    notifyDataSetChanged();
                });


                alertbox.show();
            }
        });

        Utility.showLog(String.valueOf(viewHolder.imageTextView.getDrawable()==null));
        Utility.showLog(contact.getPhoto());
       /* viewHolder.buttonDelete.setOnClickListener(v->{
            String msg=flag.equals("1")?"Restore":"Delete";
            AlertDialog.Builder alertbox=new AlertDialog.Builder(v.getRootView().getContext());
            alertbox.setMessage("Are you sure you want to "+msg.toLowerCase()+" this Contact?");
            alertbox.setTitle(msg+" Contact");
            alertbox.setPositiveButton(android.R.string.yes, (dialog, which)->{
                contact.setDeleteFlag(contact.getDeleteFlag()==1?0:1);
                //dao.updateDel(contact.getPhoneNumber(), contact.getDeleteFlag());
                contactsArrayList.remove(contact);
                //if(contactsArrayList.size()==0)
                    //context.empty();
                notifyDataSetChanged();
            })
                    .setNegativeButton(android.R.string.no, (dialog, which)->dialog.dismiss());
            alertbox.show();

        });*/

        if(flag.equals("0") || flag.equals("1"))
        viewHolder.buttonFavourite.setImageDrawable(context.getResources().getDrawable(contact.getFavFlag()==1?R.drawable.ic_favorties_fill:R.drawable.ic_favorites));


        if(flag.equals("0"))
            viewHolder.relative.setOnClickListener(v -> {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setTitle("Select Action");
                alertbox.setSingleChoiceItems(new CharSequence[]{"Call", "SMS"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + Uri.encode(contact.getPhoneNumber())));
                            context.startActivity(intent);
                        } else if (which == 1) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(contact.getPhoneNumber())));
                            context.startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
                alertbox.setNegativeButton("Cancel", null);
                alertbox.show();
            });


        viewHolder.buttonFavourite.setOnClickListener(v->{
            contact.setFavFlag(contact.getFavFlag()==1?0:1);
            viewHolder.buttonFavourite.setImageDrawable(context.getResources().getDrawable(contact.getFavFlag()==1?R.drawable.ic_favorties_fill:R.drawable.ic_favorites));
            //dao.updateFav(contact.getPhoneNumber(), contact.getFavFlag());
            viewModel.updateFavourite(contact);
            if(flag.equals("1"))
                contactsArrayList.remove(contact);
            else
                contactsArrayList.set(i, contact);
            if(contactsArrayList.size()==0)
                empty(txt,recyclerView);
            notifyDataSetChanged();
        });

        /*Picasso.with(context)
                .load(contact.getPhoto())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .transform(new CircleTransform())
                .into(new Target(){
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from){
                viewHolder.imageTextView.setImageBitmap(bitmap);
                viewHolder.textView.setVisibility(View.GONE);
                viewHolder.imageTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable){
                viewHolder.textView.setVisibility(View.VISIBLE);
                viewHolder.imageTextView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable){

            }
        });*/

        Glide.with(context)
                .load(contact.getPhoto())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.textView.setVisibility(View.VISIBLE);
                        viewHolder.textView.setBackground(draw);
                        viewHolder.imageTextView.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.textView.setVisibility(View.GONE);
                        viewHolder.imageTextView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .circleCrop()
                .into(viewHolder.imageTextView);


    }

    @Override
    public int getItemCount(){
        return contactsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageTextView;
        private ImageView buttonFavourite, buttonDelete,buttonRestore;
        private TextView contactName, contactNumber, textView;
        public RelativeLayout relative,viewBackground,viewForeground;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            contactName=itemView.findViewById(R.id.contactName);
            contactNumber=itemView.findViewById(R.id.contactNumber);
            imageTextView=itemView.findViewById(R.id.imageView);
            buttonFavourite=itemView.findViewById(R.id.fav_icon);
            buttonDelete=itemView.findViewById(R.id.delete_icon);
            buttonRestore=itemView.findViewById(R.id.restore_icon);
            relative=itemView.findViewById(R.id.relative);
            textView=itemView.findViewById(R.id.textView);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }
    }

    public void removeItem(int position) {
        contactsArrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Contact item, int position) {
        contactsArrayList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void empty(TextView txt, RecyclerView recyclerView){
        txt.setVisibility(contactsArrayList==null||contactsArrayList.size()==0?View.VISIBLE:View.GONE);
        recyclerView.setVisibility(contactsArrayList!=null&&contactsArrayList.size()>0?View.VISIBLE:View.GONE);
        //binding.buttonRestoreAll.setVisibility(flag.equals("1")&&contactsArrayList!=null&&contactsArrayList.size()>0?View.VISIBLE:View.GONE);
    }
}
