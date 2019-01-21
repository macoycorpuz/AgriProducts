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
    function userLogin($email, $pass, &$user)
    {
        $password = md5($pass);
        $stmt = $this->con->query("SELECT * FROM users WHERE email = '$email' AND password = '$password'");
        $user = array();
        while($row = $stmt->fetch_assoc()){
            array_push($user, $row);
        }
        return $stmt->num_rows > 0;
    }

    //Method to create a new user
    function registerUser($name, $email, $pass, $number, $address, $userFile, $isActivated)
    {
        if (!$this->isUserExist($email)) {
            $password = md5($pass);
            $userUrl = 'http://' . gethostbyname(gethostname()) . API_PATH . USERS_PATH . $userFile;
            $stmt = $this->con->prepare("INSERT INTO users (name, email, password, number, address, url, isActivated) VALUES (?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssssb", $name, $email, $password, $number, $address, $userUrl, $isActivated);
            if ($stmt->execute())
                return USER_CREATED;
            return USER_CREATION_FAILED;
        }
        return USER_EXIST;
    }

    //Method to get all products
    function getAllProducts($userId)
    {
        $sql = "SELECT p.*, u.name FROM products AS p INNER JOIN users AS u ON p.sellerId = u.userId WHERE NOT p.sellerId = $userId ORDER BY p.productId DESC";
        $stmt = $this->con->query($sql);
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
        $stmt = $this->con->query($sql);
        $products = array();
        while($row = $stmt->fetch_assoc()){
            array_push($products, $row);
        }
        return $products;
    }

    //Method to get all products
    function getProductbyId($productId)
    {
        $sql = "SELECT p.*, u.name FROM products AS p INNER JOIN users AS u ON p.sellerId = u.userId WHERE p.productId = $productId";
        $stmt = $this->con->query($sql);
        $product = $stmt->fetch_assoc();
        return $product;
    }

    //Method to post a product
    function postProduct($sellerId, $prodName, $desc, $quantity, $price, $location, $lat, $lng, $prodFile)
    {
        $status = 'Available';
        $prodUrl = 'http://' . gethostbyname(gethostname()) . API_PATH . PRODUCTS_PATH . $prodFile;
        $stmt = $this->con->prepare("INSERT INTO products (sellerId, productName, description, quantity, price, location, lat, lng, status, productUrl)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("issidsssss", $sellerId, $prodName, $desc, $quantity, $price, $location, $lat, $lng, $status, $prodUrl);
        if ($stmt->execute())
            return PRODUCT_CREATED;
        return PRODUCT_CREATION_FAILED;
    }

    //Method to get inbox of a particular user

    function getSelling($userid)
    {
        $sql = "SELECT d.*, p.productUrl, p.productName, u.name FROM deals AS d
        INNER JOIN products AS p ON d.productId = p.productId
        INNER JOIN users AS u ON d.buyerId = u.userId
        WHERE p.sellerId = $userId
        ORDER BY d.time DESC";
        $stmt = $this->con->query($sql);
        $selling = array();
        while($row = $stmt->fetch_assoc()) {
            array_push($selling,$row);
        }
        return $selling;
    }

    function getBuying($userid)
    {
        $sql = "SELECT d.*, p.productUrl, p.productName, u.name FROM deals AS d
        INNER JOIN products AS p ON d.productId = p.productId
        INNER JOIN users AS u ON p.sellerId = u.userId
        WHERE d.buyerId = $userId
        ORDER BY d.time DESC";
        $stmt = $this->con->query($sql);
        $buying = array();
        while($row = $stmt->fetch_assoc()) {
            array_push($buying, $row);
        }
        return $buying;
    }

    //Method to get messages of a particular deal
    function getMessages($dealId, $userId)
    {
        $sql = "SELECT m.* FROM messages AS m
        WHERE m.dealId = $dealId AND m.userId = $userId";
        $stmt = $this->con->query($sql);
        $messages = array();
        while($row = $result->fetch_assoc()) {
            array_push($messages,$row);
        }
        return $messages;
    }

    //Method to send a deal
    function sendDeal($productId, $userId, $content)
    {
        $sqlInsertDeal = "INSERT INTO deals (productId, buyerId, time) VALUES (?, ?, NOW())";
        $sqlInsertMessage = "INSERT INTO messages (dealId, userId, content, time) VALUES (LAST_INSERT_ID(), ?, ?, NOW())";
        $stmt = $this->con->prepare($sqlInsertDeal);
        $stmt->bind_param("ii", $dealId, $userId);
        $dealExecute = $stmt->execute();
        $stmt = $this->con->prepare($sqlInsertMessage);
        $stmt->bind_param("is", $userId, $content);
        $messageExectue = $stmt->execute();
        if($dealExecute && $messageExectue) return true;
        return false;
    }

    //Method to send a message to another user
    function sendMessage($dealId, $userId, $content)
    {        
        $stmt = $this->con->prepare("INSERT INTO messages (dealId, userId, content, time) VALUES (?, ?, ?, NOW());");
        $stmt->bind_param("iis", $dealId, $userId, $content);
        if ($stmt->execute()) return true;
        return false;
    }

    function getMyProducts($sellerId)
    {
        $sql = "SELECT p.*, u.name FROM products AS p INNER JOIN users AS u ON p.sellerId = u.userId WHERE p.sellerId = $sellerId ORDER BY p.productId DESC";
        $stmt = $this->con->query($sql);
        $products = array();
        while($row = $stmt->fetch_assoc()){
            array_push($products, $row);
        }
        return $products;
    }

    function changePassword($userId, $oldPassword, $newPassword) {
        $oldP = md5($oldPassword);
        $newP = md5($newPassword);
        $stmt = $this->con->query("SELECT * FROM users WHERE userId = $userId AND password = '$oldP'");
        if($stmt->num_rows > 0) {
            $stmt = $this->con->prepare("UPDATE users SET password = ? WHERE userId = ?");
            $stmt->bind_param("si", $newP, $userId);
            if ($stmt->execute()) 
                return true;
        }
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
        $stmt = $this->con->query("SELECT * FROM (SELECT email FROM users UNION SELECT email FROM admin) a WHERE email = '$email'") or die($this->con->error);
        return $stmt->num_rows > 0;
    }
}