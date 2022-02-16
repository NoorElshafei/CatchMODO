package com.bbi.catchmodo.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.data.model.RoomModel;
import com.bbi.catchmodo.ui.fragments.room_password.RoomPasswordDialogFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class RoomsAdapter extends FirestoreRecyclerAdapter<RoomModel, RoomsAdapter.ViewHolder> {

    private Context context;
    private FragmentActivity activity;
    private UserSharedPreference sharedPreferences;


    public RoomsAdapter(FragmentActivity activity, Context context, @NonNull FirestoreRecyclerOptions<RoomModel> options) {
        super(options);
        this.context = context;
        this.activity = activity;
        sharedPreferences = new UserSharedPreference(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RoomModel model) {

        holder.name.setText(model.getName());

        holder.itemView.setOnClickListener(view -> {
            sharedPreferences.setRoomId(model.getId());
            sharedPreferences.setRoomName(model.getName());

            RoomPasswordDialogFragment newFragment = RoomPasswordDialogFragment.newInstance();
            newFragment.show(activity.getSupportFragmentManager(), "dialog");
        });

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, roomStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.room_name);


        }


    }
}
