<?php
require_once("connect.php");

class Action
{
    /**
     * returns user signin result
     * @param $username
     * @param $password
     * @return array
     */
    public function login($username, $password)
    {
        $connectObject = new Connection;
        $conn = $connectObject->connectToDatabase();

        $sql = "CALL sp_login('$username')";

        $resp = mysqli_query($conn, $sql);

        $result = array();
        $result['login'] = array();

        if (mysqli_num_rows($resp) == 1) {
            $result['success'] = 1;

            $row = mysqli_fetch_assoc($resp);
            if ($password == $row['password']) {

                $index['uid'] = $row['uid'];
                $index['username'] = $row['username'];
                $index['password'] = $row['password'];
                $index['role'] = $row['role'];

                $result['login'] = $index;

            } else {
                $result['success'] = 0; //password is wrong
            }
            mysqli_close($conn);
        } else {
            $result['success'] = -1; //user not found
        }

        return $result;
    }

    /**
     * returns professor inserted posts
     * @param $uid
     * @return array
     */
    public function getProfessorPosts($uid)
    {
        $connectObject = new Connection;
        $conn = $connectObject->connectToDatabase();
        $sql = "CALL sp_select_professor_posts('$uid')";
        $resp = mysqli_query($conn, $sql);
        $output = array();

        if (mysqli_num_rows($resp) >= 1) {

            while ($row = mysqli_fetch_array($resp)) {
                $record = array();
                $record['pid'] = $row['pid'];
                $record['text'] = $row['text'];
                $record['picture'] = $row['picture'];
                $record['uid'] = $row['uid'];
                $output[] = $record;
            }
        }
        mysqli_close($conn);
        $result = array();
        $result['result'] = $output;
        return $result;
    }

    /**
     * insert new post to system
     * @param $text
     * @param $picture
     * @param $uid
     * @return array
     */
    public function insertPost($text, $picture, $uid)
    {
        $connectObject = new Connection;
        $conn = $connectObject->connectToDatabase();
        $sql = "CALL sp_insert_post('$text', '$picture' , '$uid')";
        $resp = mysqli_query($conn, $sql);
        $result = array();

        if ($resp) {
            $result['success'] = 1;
        } else {
            $result['success'] = 0;
        }

        mysqli_close($conn);
        return $result;
    }

    /**
     * upload a image for a post
     * @param $name
     * @param $image
     * @return array
     */
    public function uploadImage($name, $image)
    {
        $decodedImage = base64_decode("$image");
        $path = "uploads/" . "$name.jpeg";
        $res = file_put_contents($path, $decodedImage);

        $result = array();
        if (file_exists("uploads/" . $name . ".jpeg")) {
            $result['success'] = 1;
        } else {
            $result['success'] = 0;
        }

        return $result;
    }

    /**
     * returns all posts
     * @return array
     */
    public function getAllPosts()
    {
        $connectObject = new Connection;
        $conn = $connectObject->connectToDatabase();

        $sql = "CALL sp_select_all_posts()";
        $resp = mysqli_query($conn, $sql);

        $output = array();

        if (mysqli_num_rows($resp) >= 1) {

            while ($row = mysqli_fetch_array($resp)) {
                $record = array();
                $record['pid'] = $row['pid'];
                $record['text'] = $row['text'];
                $record['picture'] = $row['picture'];
                $record['uid'] = $row['uid'];

                $output[] = $record;
            }
        }

        $result = array();
        $result['result'] = $output;

        return $result;
    }

    /**
     * returns all comments for a post
     * @param $pid
     * @return array
     */
    public function getAllComments($pid)
    {
        $connectObject = new Connection;
        $conn = $connectObject->connectToDatabase();

        $sql = "CALL sp_select_all_comments('$pid')";
        $resp = mysqli_query($conn, $sql);

        $output = array();

        if (mysqli_num_rows($resp) >= 1) {

            while ($row = mysqli_fetch_array($resp)) {
                $record = array();
                $record['cid'] = $row['cid'];
                $record['text'] = $row['text'];
                $record['pid'] = $row['pid'];
                $record['uid'] = $row['uid'];
                $record['username'] = $row['username'];

                $output[] = $record;
            }
        }

        $result = array();
        $result['result'] = $output;

        return $result;
    }

    /**
     * insert new course to system
     * @param $text
     * @param $pid
     * @param $uid
     * @return array
     */
    public function insertComment($text, $pid, $uid)
    {
        $connectObject = new Connection;
        $conn = $connectObject->connectToDatabase();
        $sql = "CALL sp_insert_comment('$text', '$pid' , '$uid')";

        $resp = mysqli_query($conn, $sql);

        $result = array();

        if ($resp) {
            $result['success'] = 1;
        } else {
            $result['success'] = 0;
        }

        mysqli_close($conn);
        return $result;
    }
}