package project.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.erfan.prostd.R;
import project.adapter.PostListAdapter;
import project.data.DB;
import project.helper.SessionManager;
import project.model.Post;
import project.model.User;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final int ADD_POST_REQUEST_CODE = 1000;

    private Toolbar toolbar_home;
    private SwipeRefreshLayout srl_homePosts;
    private RecyclerView rv_posts;
    private FloatingActionButton fab_addPost;

    public static User user;

    private PostListAdapter postListAdapter;
    private ArrayList<Post> posts;

    private JSONObjectRequestListener listenerGetPosts;

    private int mode; // 0 -> Professor , 1 -> Student

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(this);

        try {
            user = sessionManager.getUser();
            Log.i(TAG, "onCreate: user: " + user);

            if (user != null)
                switch (user.getRole()) {
                    case "professor":
                        mode = 0;
                        break;
                    case "student":
                        mode = 1;
                        break;
                }

        } catch (NullPointerException e) {
            Log.i(TAG, "onCreate: exception: " + e.getMessage());
            Log.i(TAG, "onCreate: user exception: " + user);
        }

        initViews();
    }

    private void initViews() {
        toolbar_home = findViewById(R.id.toolbar_home);
        fab_addPost = findViewById(R.id.fab_addPost);
        srl_homePosts = findViewById(R.id.srl_homePosts);

        srl_homePosts.setColorSchemeResources(
                R.color.materialGreen,
                R.color.materialAmber,
                R.color.materialGray);

        posts = new ArrayList<>();

        rv_posts = findViewById(R.id.rv_posts);
        rv_posts.setLayoutManager(new LinearLayoutManager(this));

        postListAdapter = new PostListAdapter(posts, HomeActivity.this);
        rv_posts.setAdapter(postListAdapter);

        if (mode == 1)
            ((View) fab_addPost).setVisibility(View.GONE);

        initListeners();

        srl_homePosts.setRefreshing(true);
        reloadData();

    }

    private void initListeners() {
        toolbar_home.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.logout();
                return true;
            }
        });

        fab_addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddPostActivity.class);
                startActivityForResult(intent, ADD_POST_REQUEST_CODE);
            }
        });

        srl_homePosts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });

        listenerGetPosts = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                posts.clear();
                try {
                    //Log.i(TAG, "onResponse: " + response.getJSONArray("result"));
                    JSONArray result = response.getJSONArray("result");
                    if (result.length() >= 1) {
                        for (int i = 0; i < result.length(); i++) {
                            String pid = result.getJSONObject(i).getString("pid");
                            String text = result.getJSONObject(i).getString("text");
                            String picture = result.getJSONObject(i).getString("picture");
                            String uid = result.getJSONObject(i).getString("uid");

                            Post post = new Post(pid, text, picture, uid);
                            posts.add(post);
                        }
                    } else {
                        Log.i(TAG, "onResponse: " + "Post Count is 0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: Exception" + e.getMessage());
                }
                srl_homePosts.setRefreshing(false);
                postListAdapter.update(posts);
            }

            @Override
            public void onError(ANError anError) {
                srl_homePosts.setRefreshing(false);
                TastyToast.makeText(HomeActivity.this,
                        getResources().getString(R.string.problem_with_connection),
                        TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        };
    }

    private void reloadData() {
        if (mode == 0) {
            DB.getProfessorPosts(user.getUid(), listenerGetPosts);
        } else {
            DB.getAllPosts(listenerGetPosts);
        }
    }

    public static User getUser() {
        return user;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_POST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            srl_homePosts.setRefreshing(true);
            reloadData();
        }
    }
}
