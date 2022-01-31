package com.bbi.catchmodo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.ui.activities.ProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class TopUserAdapter extends RecyclerView.Adapter<TopUserAdapter.viewHolder> {

    private Context context;
    private ArrayList<RegisterModel> registerModels;
    FirebaseUser firebaseUser;


    public TopUserAdapter(Context context, ArrayList<RegisterModel> registerModels) {
        this.context = context;
        this.registerModels = registerModels;
    }

    @NonNull
    @Override
    public TopUserAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUserAdapter.viewHolder holder, int position) {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.getUid().equals(registerModels.get(position).getId()))
        {
            holder.name.setText(registerModels.get(position).getUser_name()+'('+"You"+')');
        }else {
            holder.name.setText(registerModels.get(position).getUser_name());
        }
        holder.score.setText(registerModels.get(position).getScore());
        Glide.with(context).load(registerModels.get(position).getImage_url()).placeholder(R.drawable.moodo_icon).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return registerModels!=null?registerModels.size():0;
    }
    public void setData(ArrayList<RegisterModel> registerModels) {
        this.registerModels = registerModels;
        notifyDataSetChanged();

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name,score;
        ImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView4);
            score=itemView.findViewById(R.id.textView5);
            imageView=itemView.findViewById(R.id.cart_image1);
        }
    }
}
