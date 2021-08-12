package me.discretesolutions.string.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.discretesolutions.string.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {
    ArrayList<me.discretesolutions.string.ui.chat> chats;
    String xyz;
    public chatAdapter(ArrayList<me.discretesolutions.string.ui.chat> chats) {
        this.chats = chats;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView message,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.msg);
            time=itemView.findViewById(R.id.msg_time);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_msg_layout,parent,false);
            return new MyViewHolder(view);
        }
        else if(viewType==0)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_layout,parent,false);
            return new MyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String msg=chats.get(position).getMessage();
        String time=chats.get(position).getTime();

        holder.message.setText(msg);
        holder.time.setText(time);

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xyz = "Message ";
                if (chats.get(position).getMsg_type().equalsIgnoreCase("friend")){
                    xyz+= "received on ";
                }
                else{
                    xyz+= "sent on ";
                }
                xyz += chats.get(position).getTime()+" "+chats.get(position).getDate();
                Toast.makeText(v.getContext(),xyz,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getMsg_type().equals("current user"))
        {
            return 1;
        }
        if(chats.get(position).getMsg_type().equals("friend"))
        {
            return 0;
        }
        return -1;
    }
}
