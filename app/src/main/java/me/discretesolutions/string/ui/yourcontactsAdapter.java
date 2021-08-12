package me.discretesolutions.string.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import me.discretesolutions.string.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class yourcontactsAdapter extends RecyclerView.Adapter<yourcontactsAdapter.MyViewHolder> {

    private ArrayList<yourcontacts> yourcontacts;
    private contactclick contactclick;
    public yourcontactsAdapter(ArrayList<yourcontacts> yourcontacts,contactclick contactclick) {
        this.yourcontacts = yourcontacts;
        this.contactclick = contactclick;
    }

    public void filtercontactlist(ArrayList<me.discretesolutions.string.ui.yourcontacts> filteredcontacts) {
//        yourcontacts.clear();
        yourcontacts=filteredcontacts;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView username,status;
        private ImageView dp;
        contactclick contactclick;
        public MyViewHolder(@NonNull View itemView, contactclick contactclick) {
            super(itemView);
            username=itemView.findViewById(R.id.contactname);
            status=itemView.findViewById(R.id.status);
            dp=itemView.findViewById(R.id.userimage);
            this.contactclick = contactclick;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            contactclick.onClick(getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_single_card,parent,false);
        return new MyViewHolder(view,contactclick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name=yourcontacts.get(position).getUsername();
        String status=yourcontacts.get(position).getstatus();
        String number = yourcontacts.get(position).getNumber();
        int dpimg=yourcontacts.get(position).getDpimg();

        holder.username.setText(name);
        holder.status.setText(status);
        holder.dp.setImageResource(dpimg);
    }

    @Override
    public int getItemCount() {
        return yourcontacts.size();
    }

    public interface contactclick
    {
        void onClick(int position);
    }
}
