<?php

require_once("action.php");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $action = $_POST['action'];
    $actionObject = new Action;

    switch ($action) {

        case 'login':
            $username = $_POST['username'];
            $password = $_POST['password'];
            $result = $actionObject->login($username, $password);
            break;

        case 'get_professor_posts':
            $uid = $_POST['uid'];
            $result = $actionObject->getProfessorPosts($uid);
            break;

        case 'insert_post':
            $text = $_POST['text'];
            $picture = $_POST['picture'];
            $uid = $_POST['uid'];
            $result = $actionObject->insertPost($text, $picture, $uid);
            break;

        case 'upload_image':
            $name = $_POST['name'];
            $image = $_POST['image'];
            $result = $actionObject->uploadImage($name, $image);
            break;

        case 'get_all_posts':
            $result = $actionObject->getAllPosts();
            break;

        case 'get_all_comments':
            $pid = $_POST['pid'];
            $result = $actionObject->getAllComments($pid);
            break;

        case 'insert_comment':
            $text = $_POST['text'];
            $pid = $_POST['pid'];
            $uid = $_POST['uid'];
            $result = $actionObject->insertComment($text, $pid, $uid);
            break;

        default:
            $result = "wrong action";
            break;
    }

    echo json_encode($result);
}
