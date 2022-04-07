package com.example.androidlesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.androidlesson1.account.RegisterActivity;
import com.example.androidlesson1.constants.Urls;
import com.example.androidlesson1.network.ImageRequester;
import com.example.androidlesson1.image.ImageService;
import com.example.androidlesson1.image.dto.ImageDTO;
import com.example.androidlesson1.image.dto.ImageResponseDTO;
import com.example.androidlesson1.user.UserActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ImageRequester imageRequester;
    private NetworkImageView myImage;

    // One Preview Image
    ImageView IVPreviewImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageRequester = ImageRequester.getInstance();
        myImage=findViewById(R.id.myimg);
        String urlImg = Urls.BASE+"/images/1.jpg";
        imageRequester.setImageFromUrl(myImage,urlImg);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.m_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                return true;
            case R.id.m_users:
                intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSelectPhoto(View view){
        imageChooser();
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri uri = data.getData();
                if (null != uri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(uri);

                    Bitmap bitmap= null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // initialize byte stream
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    // compress Bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    // Initialize byte array
                    byte[] bytes=stream.toByteArray();
                    // get base64 encoded string
                    String sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setBase64(sImage);

                    ImageService.getInstance()
                            .jsonApi()
                            .uploadImage(imageDTO)
                            .enqueue(new Callback<ImageResponseDTO>() {
                                @Override
                                public void onResponse(Call<ImageResponseDTO> call, Response<ImageResponseDTO> response) {
                                    ImageResponseDTO data = response.body();
                                    //tvInfo.setText("response is good");
                                    String urlImg = Urls.BASE+"/images/"+data.getImageUrl();
                                    imageRequester.setImageFromUrl(myImage,urlImg);
                                }

                                @Override
                                public void onFailure(Call<ImageResponseDTO> call, Throwable t) {
                                    String str = t.toString();
                                    int a =12;
                                }
                            });
                }
            }
        }
    }


}