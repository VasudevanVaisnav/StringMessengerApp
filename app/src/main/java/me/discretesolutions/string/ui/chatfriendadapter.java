package me.discretesolutions.string.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.discretesolutions.string.R;

public class chatfriendadapter extends RecyclerView.Adapter<chatfriendadapter.MyViewHolder> {

    private ArrayList<me.discretesolutions.string.ui.chatfriends> chatfriends;
    private chatclicklistener chatclicklistener;

    public chatfriendadapter(ArrayList<me.discretesolutions.string.ui.chatfriends> chatfriends,chatclicklistener chatclicklistener) {
        this.chatfriends = chatfriends;
        this.chatclicklistener=chatclicklistener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView friend_name,msg_time,n_msgs,msg_content;
        private ImageView dp_image,on_off;
        private LinearLayout notify_circle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            friend_name=itemView.findViewById(R.id.chatname);
            msg_time=itemView.findViewById(R.id.chattime);
            n_msgs=itemView.findViewById(R.id.numberofmsgs);
            msg_content=itemView.findViewById(R.id.chatcontent);
            dp_image=itemView.findViewById(R.id.userimage);
            notify_circle=itemView.findViewById(R.id.noticationcircle);
            on_off=itemView.findViewById(R.id.online_offline);
        }

        @Override
        public void onClick(View v) {
            chatclicklistener.onClick(itemView,getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_single_card,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String frname = chatfriends.get(position).getFriendname();
        String mtime = chatfriends.get(position).getMsgtime();
        String mcontent = chatfriends.get(position).getMsgcontent();
        String nmsgs = chatfriends.get(position).getNmsgs();
        int ncircle=chatfriends.get(position).getNotifycircle();
        if(Integer.parseInt(nmsgs)>0)
        {
            holder.notify_circle.setVisibility(View.INVISIBLE);
        }
        int dpim = chatfriends.get(position).getDp();
        holder.friend_name.setText(frname);
        holder.msg_time.setText(mtime);
        holder.msg_content.setText(mcontent);
        holder.dp_image.setImageResource(dpim);
        holder.n_msgs.setText(nmsgs);
    }

    @Override
    public int getItemCount() {
        return chatfriends.size();
    }

    public interface chatclicklistener
    {
        void onClick(View v,int position);
    }

    public void filterchatlist(ArrayList<me.discretesolutions.string.ui.chatfriends> filteredchatlist)
    {
        chatfriends = filteredchatlist;
        notifyDataSetChanged();
    }
}
