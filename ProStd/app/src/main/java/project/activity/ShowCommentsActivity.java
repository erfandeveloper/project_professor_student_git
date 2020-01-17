package project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.erfan.prostd.R;
import project.adapter.CommentListAdapter;
import project.data.DB;
import project.model.Comment;
import project.model.Post;

public class ShowCommentsActivity extends AppCompatActivity {
    private static final String TAG = "ShowCommentsActivity";

    private Toolbar toolbar_showComments;
    private EditText edt_comment;
    private MaterialButton btn_addComment;
    private SwipeRefreshLayout srl_postComments;
    private RecyclerView rv_comments;

    private Post post;
    private JSONObjectRequestListener listenerAddComment;

    private CommentListAdapter commentListAdapter;
    private ArrayList<Comment> comments;

    private JSONObjectRequestListener listenerGetComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);
        initViews();
    }

    private void initViews() {
        toolbar_showComments = findViewById(R.id.toolbar_showComments);
        edt_comment = findViewById(R.id.edt_comment);
        btn_addComment = findViewById(R.id.btn_addComment);
        srl_postComments = findViewById(R.id.srl_postComments);
        srl_postComments.setColorSchemeResources(
                R.color.materialGreen,
                R.color.materialAmber,
                R.color.materialGray);

        if (getIntent().hasExtra("post")) {
            post = (Post) getIntent().getSerializableExtra("post");
            Log.i(TAG, "initViews: post" + post);
        }

        comments = new ArrayList<>();
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(this));
        commentListAdapter = new CommentListAdapter(comments, ShowCommentsActivity.this);
        rv_comments.setAdapter(commentListAdapter);
        initListeners();

        srl_postComments.setRefreshing(true);
        DB.getAllComments(post.getPid(), listenerGetComments);
    }

    private void initListeners() {
        toolbar_showComments.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        srl_postComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DB.getAllComments(post.getPid(), listenerGetComments);
            }
        });


        btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_comment.getText().toString().equals("")) {
                    TastyToast.makeText(ShowCommentsActivity.this,
                            getResources().getString(R.string.please_enter_your_comment),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.WARNING);
                    return;
                }

                Comment comment = new Comment(edt_comment.getText().toString(),
                        post.getPid(),
                        HomeActivity.getUser().getUid());

                edt_comment.setText("");

                //hide keyboard
                View view = ShowCommentsActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                edt_comment.clearFocus();


                DB.insertComment(comment, listenerAddComment);
            }
        });

        listenerAddComment = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        TastyToast.makeText(ShowCommentsActivity.this,
                                getResources().getString(R.string.your_comment_registered),
                                TastyToast.LENGTH_SHORT,
                                TastyToast.SUCCESS);
                        srl_postComments.setRefreshing(true);
                        DB.getAllComments(post.getPid(), listenerGetComments);
                    } else {
                        TastyToast.makeText(ShowCommentsActivity.this,
                                getResources().getString(R.string.problem_with_add_comment),
                                TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.i(TAG, "onResponse: " + e.getMessage());
                    TastyToast.makeText(ShowCommentsActivity.this,
                            getResources().getString(R.string.problem_with_add_comment),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                }
            }

            @Override
            public void onError(ANError anError) {
                //Log.i(TAG, "onError: " + anError.getErrorDetail());
                TastyToast.makeText(ShowCommentsActivity.this,
                        getResources().getString(R.string.problem_with_add_comment),
                        TastyToast.LENGTH_SHORT,
                        TastyToast.WARNING);
            }
        };

        listenerGetComments = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                comments.clear();
                try {
                    Log.i(TAG, "onResponse: " + response.getJSONArray("result"));
                    JSONArray result = response.getJSONArray("result");
                    if (result.length() >= 1) {
                        for (int i = 0; i < result.length(); i++) {
                            String cid = result.getJSONObject(i).getString("cid");
                            String text = result.getJSONObject(i).getString("text");
                            String pid = result.getJSONObject(i).getString("pid");
                            String uid = result.getJSONObject(i).getString("uid");
                            String username = result.getJSONObject(i).getString("username");

                            Comment comment = new Comment(cid, text, pid, uid, username);
                            comments.add(comment);
                        }
                    } else {
                        Log.i(TAG, "onResponse: " + "Comment Count is 0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: Exception" + e.getMessage());
                    TastyToast.makeText(ShowCommentsActivity.this,
                            getResources().getString(R.string.problem_with_connection),
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                }
                srl_postComments.setRefreshing(false);
                commentListAdapter.update(comments);
            }

            @Override
            public void onError(ANError anError) {
                TastyToast.makeText(ShowCommentsActivity.this,
                        getResources().getString(R.string.problem_with_connection),
                        TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR);
            }
        };
    }
}
