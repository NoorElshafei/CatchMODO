package com.bbi.catchmodo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.model.RegisterModel;
import com.bbi.catchmodo.data.model.UserRoomModel;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TopScoreRoomAdapter extends FirestoreRecyclerAdapter<UserRoomModel, TopScoreRoomAdapter.ViewHolder> {

    private Context context;
    private FragmentActivity activity;
    private DatabaseReference reference;
    private RegisterModel userModel;


    public TopScoreRoomAdapter(FragmentActivity activity, Context context, @NonNull FirestoreRecyclerOptions<UserRoomModel> options) {
        super(options);
        this.context = context;
        this.activity = activity;
        reference = FirebaseDatabase.getInstance().getReference("UserRegister");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserRoomModel model) {

        holder.score.setText(model.getScore()+"");

        reference.child(model.getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModel = snapshot.getValue(RegisterModel.class);
                holder.name.setText(userModel.getUser_name());
                Glide.with(context).load(userModel.getImage_url()).placeholder(R.drawable.fun_moodo).into(holder.imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, score;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView4);
            score = itemView.findViewById(R.id.textView5);
            imageView = itemView.findViewById(R.id.cart_image1);

        }


    }
}
