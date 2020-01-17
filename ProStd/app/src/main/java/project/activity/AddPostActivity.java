package project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.erfan.prostd.R;
import project.data.DB;
import project.model.Post;

public class AddPostActivity extends AppCompatActivity {
    private static final String TAG = "AddPostActivity";

    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 1001;
    private static final int RESULT_LOAD_IMAGE = 1002;

    private Toolbar toolbar_addPost;
    private ImageView img_uploadPicture;
    private ProgressBar prb_uploadPicture;
    private TextInputEditText edt_postText;
    private MaterialButton btn_addPost;

    private JSONObjectRequestListener uploadListener;

    private String pictureName;
    private boolean isPictureUploaded = false;
    private JSONObjectRequestListener listenerAddPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        initViews();
    }

    private void initViews() {
        toolbar_addPost = findViewById(R.id.toolbar_addPost);
        img_uploadPicture = findViewById(R.id.img_uploadPicture);
        prb_uploadPicture = findViewById(R.id.prb_uploadPicture);
        edt_postText = findViewById(R.id.edt_postText);
        btn_addPost = findViewById(R.id.btn_addPost);

        initListeners();
    }

    private void initListeners() {
        toolbar_addPost.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
            }
        });

        img_uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for RunTime Permission READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddPostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    createGalleryIntent();
                }
            }
        });

        btn_addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPictureUploaded) {
                    TastyToast.makeText(AddPostActivity.this,
                            getResources().getString(R.string.please_upload_picture),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.WARNING);
                    return;
                }

                if (edt_postText.getText().toString().equals("")) {
                    TastyToast.makeText(AddPostActivity.this,
                            getResources().getString(R.string.please_enter_description),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.WARNING);
                    return;
                }

                String postPicture = DB.IMAGE_UPLOAD_URL + pictureName + ".jpeg";
                String text = edt_postText.getText().toString();
                Post post = new Post(text, postPicture, HomeActivity.getUser().getUid());
                DB.insertPost(post, listenerAddPost);

            }
        });

        uploadListener = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        isPictureUploaded = true;
                        setVisualEffectsOnSuccess();
                    } else {
                        setVisualEffectsOnFail();
                        TastyToast.makeText(AddPostActivity.this,
                                getResources().getString(R.string.problem_with_upload_picture),
                                TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e.getMessage());
                    TastyToast.makeText(AddPostActivity.this,
                            getResources().getString(R.string.problem_with_upload_picture),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                    prb_uploadPicture.setVisibility(View.INVISIBLE);
                    img_uploadPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload));
                    img_uploadPicture.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.i(TAG, "onError: " + anError.getErrorDetail());
                TastyToast.makeText(AddPostActivity.this,
                        getResources().getString(R.string.problem_with_upload_picture),
                        TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);

                prb_uploadPicture.setVisibility(View.INVISIBLE);
                img_uploadPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload));
                img_uploadPicture.setVisibility(View.VISIBLE);

            }
        };

        listenerAddPost = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        setResult(Activity.RESULT_OK, getIntent());
                        finish();
                    } else {
                        TastyToast.makeText(AddPostActivity.this,
                                getResources().getString(R.string.problem_with_add_post),
                                TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e.getMessage());
                    TastyToast.makeText(AddPostActivity.this,
                            getResources().getString(R.string.problem_with_add_post),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                }
            }

            @Override
            public void onError(ANError anError) {
                TastyToast.makeText(AddPostActivity.this,
                        getResources().getString(R.string.problem_with_add_post),
                        TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
            }
        };
    }

    private void createGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    private void setVisualEffectsOnSuccess() {
        prb_uploadPicture.setVisibility(View.INVISIBLE);
        img_uploadPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload_success));
        img_uploadPicture.setVisibility(View.VISIBLE);
    }

    private void setVisualEffectsOnFail() {
        prb_uploadPicture.setVisibility(View.INVISIBLE);
        img_uploadPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload_fail));
        img_uploadPicture.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    createGalleryIntent();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            img_uploadPicture.setVisibility(View.INVISIBLE);
            prb_uploadPicture.setVisibility(View.VISIBLE);
            Uri uri = data.getData();

            img_uploadPicture.setImageURI(uri);
            Bitmap image = ((BitmapDrawable) img_uploadPicture.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            pictureName = "IMG_" + timeStamp;

            DB.uploadImage(pictureName, encodedImage, uploadListener);
        }
    }
}