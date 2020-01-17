<?php
class Connection {
  public function connectToDatabase() {

    $connection=mysqli_connect("localhost", "root", "Saeed#", "prostd");
    
    if (mysqli_connect_errno()) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }

    $connection->set_charset("utf8");
    
    return $connection;
    }
}
