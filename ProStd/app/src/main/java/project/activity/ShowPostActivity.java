package project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ir.erfan.prostd.R;
import project.data.DB;
import project.model.Comment;
import project.model.Post;

public class ShowPostActivity extends AppCompatActivity {
    private static final String TAG = "ShowPostActivity";

    private Toolbar toolbar_showPost;
    private PhotoView img_postPicture;
    private TextView txt_postText;
    private MaterialButton btn_showComments;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        initViews();
    }

    private void initViews() {
        toolbar_showPost = findViewById(R.id.toolbar_showPost);
        img_postPicture = findViewById(R.id.img_postPicture);
        txt_postText = findViewById(R.id.txt_postText);
        btn_showComments = findViewById(R.id.btn_showComments);


        if (getIntent().hasExtra("post")) {
            post = (Post) getIntent().getSerializableExtra("post");
            Picasso.get().load(post.getPicture()).into(img_postPicture);
            txt_postText.setText(post.getText());
        }

        initListeners();
    }

    private void initListeners() {
        toolbar_showPost.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_showComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPostActivity.this, ShowCommentsActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
    }
}