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

    //Method for user login
    function userLogin($email, $pass, &$userId)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("SELECT * FROM users WHERE email = ? AND password = ?");
        $stmt->bind_param("ss", $email, $password);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    //Method to create a new user
    function registerUser($name, $email, $pass, $number, $address, $url)
    {
        if (!$this->isUserExist($email)) {
            $password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO users (name, email, password, number, address, url) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssss", $name, $email, $password, $number, $address, $url);
            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;
        }
        return USER_EXIST;
    }

    //Method to get all products
    function getAllProducts()
    {
        $sql = "SELECT p.*, u.name FROM products AS p INNER JOIN users AS u ON p.sellerId = u.userId ORDER BY p.productId DESC";
        $stmt = $this->con->prepare($sql);
        $stmt->execute();
        $products = array();
        while($row = $stmt->fetch_assoc()){
            array_push($products, $row);
        }
        return $products;
    }

    //Method to get product by name
    function getProductbyName($productName)
    {
        $sql = "SELECT p.*, u.name FROM products AS p INNER JOIN users AS u ON p.sellerId = u.userId WHERE productName LIKE '%$productName%'";
        $stmt = $this->con->prepare($sql);
        $stmt->execute();
        $product = array();
        while($row = $stmt->fetch_assoc()){
            array_push($product, $row);
        }
        return $product;
    }

    //Method to get all products
    function getProductbyId($productId)
    {
        $sql = "SELECT p.*, u.name FROM products AS p INNER JOIN users AS u ON p.sellerId = u.userId WHERE productId = $productId";
        $stmt = $this->con->prepare($sql);
        $stmt->execute();
        $product = array();
        while($row = $stmt->fetch_assoc()){
            array_push($product, $row);
        }
        return $product;
    }

    //Method to post a product
    function postProduct($sellerId, $prodName, $desc, $quantity, $price, $location, $lat, $lng, $status, $prodUrl)
    {
        $password = md5($pass);
        $stmt = $this->con->prepare("INSERT INTO products (sellerId, productName, description, quantity, price, location, lat, lng, status, productUrl)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("issidsssss", $sellerId, $prodName, $desc, $quantity, $price, $location, $lat, $lng, 'Available', $prodUrl);
        if ($stmt->execute())
            return PRODUCT_CREATED;
        return PRODUCT_CREATION_FAILED;
    }

    //Method to get inbox of a particular user
    function getDeals($userid)
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

    //Method to get messages of a particular deal
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

    //Method to send a deal
    function sendDeal($productId, $userId)
    {
        $stmt = $this->con->prepare("INSERT INTO deals (productId, userId, time) VALUES (?, ?, NOW());");
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