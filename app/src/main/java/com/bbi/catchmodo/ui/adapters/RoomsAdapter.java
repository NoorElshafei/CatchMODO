package com.bbi.catchmodo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.viewHolder> {

    private Context context;
    private ArrayList<RegisterModel> registerModels;
    FirebaseUser firebaseUser;


    public RoomsAdapter(Context context, ArrayList<RegisterModel> registerModels) {
        this.context = context;
        this.registerModels = registerModels;
    }

    @NonNull
    @Override
    public RoomsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.viewHolder holder, int position) {



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
        TextView name,roomStatus;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.room_name);
            roomStatus=itemView.findViewById(R.id.room_status);


        }
    }
}
