package com.example.androidlesson1.user.card;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.androidlesson1.R;
import com.example.androidlesson1.constants.Urls;
import com.example.androidlesson1.network.ImageRequester;
import com.example.androidlesson1.user.dto.UserDTO;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    List<UserDTO> mItems;
    private ImageRequester imageRequester;
    private NetworkImageView myImage;
    private final OnItemClickListener listener;
    private final OnItemClickListener editUser;

    public CardAdapter(List<UserDTO> items, OnItemClickListener listener, OnItemClickListener editUser) {
        super();
        this.listener=listener;
        this.editUser=editUser;
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        UserDTO user = mItems.get(i);

        imageRequester = ImageRequester.getInstance();
        String urlImg = Urls.BASE+user.getPhoto();

        imageRequester.setImageFromUrl(viewHolder.userPhoto,urlImg);

        viewHolder.userEmail.setText(user.getEmail());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(user);
            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUser.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView userPhoto;
        public Button btnEdit;
        public TextView userEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            userPhoto = (NetworkImageView)itemView.findViewById(R.id.user_photo);
            userEmail = (TextView)itemView.findViewById(R.id.user_email);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        }
    }
}
