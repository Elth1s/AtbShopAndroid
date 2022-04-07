package com.example.androidlesson1.user;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.androidlesson1.R;
import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.network.AccountService;
import com.example.androidlesson1.constants.Urls;
import com.example.androidlesson1.network.ImageRequester;
import com.example.androidlesson1.user.dto.UserDTO;
import com.example.androidlesson1.user.network.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    List<UserDTO> mItems;
    private ImageRequester imageRequester;
    private NetworkImageView myImage;
    public CardAdapter(List<UserDTO> items) {
        super();
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
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView userPhoto;
        public TextView userEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            userPhoto = (NetworkImageView)itemView.findViewById(R.id.user_photo);
            userEmail = (TextView)itemView.findViewById(R.id.user_email);
        }
    }
}
