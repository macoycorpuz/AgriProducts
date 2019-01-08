<?php

class DbOperation
{
    private $con;

    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }

    //Method to create a new user
    function registerUser($name, $email, $pass, $number, $address, $isAdmin)
    {
        if (!$this->isUserExist($email)) {
            $password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO users (name, email, password, number, address, isAdmin) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("sssssb", $name, $email, $password, $number, $address, $isAdmin);
            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;
        }
        return USER_EXIST;
    }

    //Method for user login
    function userLogin($email, $pass, &$userId)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("SELECT userId FROM users WHERE email = ? AND password = ?");
        $stmt->bind_param("ss", $email, $password);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    //Method to send a deal
    function sendDeal($productId, $userId)
    {
        $stmt = $this->con->prepare("INSERT INTO deals (productId, userId,time) VALUES (?, ?, NOW());");
        $stmt->bind_param("ii", $dealId, $userId);
        if ($stmt->execute())
            return true;
        return false;
    }

    //Method to send a message to another user
    function sendMessage($dealId, $userId, $content)
    {
        $stmt = $this->con->prepare("INSERT INTO messages (dealId, userId, content, time) VALUES (?, ?, ?, NOW());");
        $stmt->bind_param("iis", $dealId, $userId, $content);
        if ($stmt->execute())
            return true;
        return false;
    }

    //Method to post a product
    function postProduct($sellerId, $prodName, $desc, $quantity, $price, $location, $lat, $lng)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("INSERT INTO products (sellerId, productName, description, quantity, price, location, lat, lng, status)
        VALUES (?, ?, ?, ?, ?,  ?, ?, ?, 'Available')");
        $stmt->bind_param("iissidsss", $sellerId, $prodName, $desc, $quantity, $price, $location, $lat, $lng);
        if ($stmt->execute())
            return true;
        return false;
    }


    //Method to update profile of user
    function updateProfile($id, $name, $email, $pass, $gender)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("UPDATE users SET name = ?, email = ?, password = ?, gender = ? WHERE userId = ?");
        $stmt->bind_param("ssssi", $name, $email, $password, $gender, $id);
        if ($stmt->execute())
            return true;
        return false;
    }

    //Method to get inbox of a particular user
    function getInbox($userid)
    {
        $sql = "SELECT * FROM deals AS d
        INNER JOIN products AS p ON d.productId = p.productId
        INNER JOIN users AS u ON p.sellerId = u.userId
        WHERE d.userId = ? AND p.sellerId = ?
        ORDER BY d.time DESC";
        $stmt = $this->con->prepare($sql);
        $stmt->bind_param("ii", $userid, $userid);
        $stmt->execute();
        $inbox = array();
        while($row = $result->fetch_assoc()) {
            array_push($inbox,$row);
        }
        return $inbox;
    }

    //Method to get messages of a particular user
    function getMessages($dealId, $userId)
    {
        $sql = "SELECT * FROM messages AS m
        INNER JOIN deals AS d ON m.dealId = d.dealId
        INNER JOIN products AS p ON d.productId = p.productId
        INNER JOIN users AS u ON p.sellerId = u.userId
        WHERE m.dealId = ? AND m.userId = ?";
        $stmt = $this->con->prepare($sql);
        $stmt->bind_param("ii", $dealId, $userId);
        $stmt->execute();
        $messages = array();
        while($row = $result->fetch_assoc()) {
            array_push($messages,$row);
        }
        return $messages;
    }

    //Method to get all users
    function getAllUsers()
    {
        $stmt = $this->con->query("SELECT userId, name, email, number, address FROM users") or die($this->con->error);
        
        $users = array();
        while($row = $stmt->fetch_assoc()){
            array_push($users, $row);
        }
        return $users;
    }

    //Method to get all products
    function getAllProducts()
    {
        $sql = "SELECT * FROM products AS p
        INNER JOIN user AS u ON p.SellerID = u.UserID 
        ORDER BY ProductID DESC";
        $stmt = $this->con->prepare($sql);
        $stmt->execute();
        $products = array();
        while($row = $stmt->fetch_assoc()){
            array_push($products, $row);
        }
        return $products;
    }

    //Method to check if email already exist
    function isUserExist($email)
    {
        $stmt = $this->con->prepare("SELECT userId FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }
}