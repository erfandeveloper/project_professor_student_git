package project.data;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import project.model.Comment;
import project.model.Post;

public class DB {

    public static final String

            WEBSERVICE_URL = "http://172.20.10.2/prostd/webservice.php",
            IMAGE_UPLOAD_URL = "http://172.20.10.2/prostd/uploads/";

    private static final String
            ACTION_LOGIN = "login",
            ACTION_GET_PROFESSOR_POSTS = "get_professor_posts",
            ACTION_GET_ALL_POSTS = "get_all_posts",
            ACTION_GET_ALL_COMMENTS = "get_all_comments",
            ACTION_INSERT_POST = "insert_post",
            ACTION_INSERT_COMMENT = "insert_comment",
            ACTION_UPLOAD_IMAGE = "upload_image",
            ACTION_DELETE_IMAGE = "delete_image";

    public static void login(String username, String password, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_LOGIN)
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .build()
                .getAsJSONObject(listener);
    }

    public static void getProfessorPosts(String uid, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_GET_PROFESSOR_POSTS)
                .addBodyParameter("uid", uid)
                .build()
                .getAsJSONObject(listener);
    }

    public static void insertPost(Post post, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_INSERT_POST)
                .addBodyParameter("text", post.getText())
                .addBodyParameter("picture", post.getPicture())
                .addBodyParameter("uid", post.getUid())
                .build()
                .getAsJSONObject(listener);
    }

    public static void getAllPosts(JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_GET_ALL_POSTS)
                .build()
                .getAsJSONObject(listener);
    }

    public static void getAllComments(String pid, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_GET_ALL_COMMENTS)
                .addBodyParameter("pid", pid)
                .build()
                .getAsJSONObject(listener);
    }

    public static void insertComment(Comment comment, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_INSERT_COMMENT)
                .addBodyParameter("text", comment.getText())
                .addBodyParameter("pid", comment.getPid())
                .addBodyParameter("uid", comment.getUid())
                .build()
                .getAsJSONObject(listener);
    }

    public static void uploadImage(String name, String image, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_UPLOAD_IMAGE)
                .addBodyParameter("name", name)
                .addBodyParameter("image", image)
                .build()
                .getAsJSONObject(listener);
    }

    public static void deleteImage(String name, JSONObjectRequestListener listener) {
        AndroidNetworking.post(WEBSERVICE_URL)
                .addBodyParameter("action", ACTION_DELETE_IMAGE)
                .addBodyParameter("name", name)
                .build()
                .getAsJSONObject(listener);
    }
}