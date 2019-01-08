<?php

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';
require_once '../includes/DbOperation.php';

//Creating a new app with the config to show errors
$app = new \Slim\App([
    'settings' => [
        'displayErrorDetails' => true
    ]
]);


//user login route
$app->post('/login', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('email', 'password'))) {
        $requestData = $request->getParsedBody();
        $email = $requestData['email'];
        $password = $requestData['password'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->userLogin($email, $password, $user)) {
            $responseData['error'] = false;
            $responseData['user'] = $user;
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Invalid email or password';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});

//registering a new user
$app->post('/register', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('name', 'email', 'password', 'number', 'address', 'url'))) {
        $requestData = $request->getParsedBody();
        $name = $requestData['name'];
        $email = $requestData['email'];
        $password = $requestData['password'];
        $number = $requestData['number'];
        $address = $requestData['address'];
        $url = $requestData['url'];
        $db = new DbOperation();
        $responseData = array();

        $result = $db->registerUser($name, $email, $password, $number, $address, $url);

        if ($result == USER_CREATED) {
            $responseData['error'] = false;
            $responseData['message'] = 'Registered successfully';
        } elseif ($result == USER_CREATION_FAILED) {
            $responseData['error'] = true;
            $responseData['message'] = 'Some error occurred';
        } elseif ($result == USER_EXIST) {
            $responseData['error'] = true;
            $responseData['message'] = 'This email already exist.';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});

//getting all products
$app->get('/products', function (Request $request, Response $response) {
    $db = new DbOperation();
    $products = $db->getAllProducts();
    $response->getBody()->write(json_encode(array("products" => $products)));
});

//getting product by name
$app->get('/productName/{productName}', function (Request $request, Response $response) {
    $productName = $request->getAttribute('productName');
    $db = new DbOperation();
    $product = $db->getProductbyName($productName);
    $response->getBody()->write(json_encode(array("product" => $product)));
});

//getting product by id
$app->get('/productId/{productId}', function (Request $request, Response $response) {
    $productId = $request->getAttribute('productId');
    $db = new DbOperation();
    $product = $db->getProductbyId($productId);
    $response->getBody()->write(json_encode(array("product" => $product)));
});

//post product
$app->post('/sellproduct', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('sellerId', 'productName', 'description', 'quantity', 'price', 'location', 'lat', 'lng', 'productUrl'))) {
        $sellerId = $request->getParsedBody();
        $productName = $requestData['productName'];
        $description = $requestData['description'];
        $quantity = $requestData['quantity'];
        $price = $requestData['price'];
        $location = $requestData['location'];
        $lat = $requestData['lat'];
        $lng = $requestData['lng'];
        $productUrl = $requestData['productUrl'];
        $db = new DbOperation();
        $responseData = array();

        $result = $db->postProduct($sellerId, $productName, $description, $quantity, $price, $location, $lat, $lng, $productUrl);

        if ($result == PRODUCT_CREATED) {
            $responseData['error'] = false;
            $responseData['message'] = 'Product has been posted';
        } elseif ($result == PRODUCT_CREATION_FAILED) {
            $responseData['error'] = true;
            $responseData['message'] = 'Some error occurred';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});

//getting deals
$app->get('/inbox/{userId}', function (Request $request, Response $response) {
    $deals = $request->getAttribute('userId');
    $db = new DbOperation();
    $deals = $db->getDeals($userid);
    $response->getBody()->write(json_encode(array("deals" => $deals)));
});

//getting messages for a deal
$app->get('/messages/{dealId}/{userId}', function (Request $request, Response $response) {
    $dealId = $request->getAttribute('dealId');
    $userId = $request->getAttribute('userId');
    $db = new DbOperation();
    $inbox = $db->getMessages($dealId, $userId);
    $response->getBody()->write(json_encode(array("messages" => $inbox)));
});

//sending message to user
$app->post('/sendmessage', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('dealId', 'userId', 'content'))) {
        $requestData = $request->getParsedBody();
        $dealId = $requestData['dealId'];
        $userId = $requestData['userId'];
        $content = $requestData['content'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->sendMessage($from, $to, $title, $message)) {
            $responseData['error'] = false;
            $responseData['message'] = 'Message sent successfully';
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Could not send message';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});

//ADMIN API
//getting all users
$app->get('/users', function (Request $request, Response $response) {
    $db = new DbOperation();
    $users = $db->getAllUsers();
    $response->getBody()->write(json_encode(array("users" => $users)));
});

//function to check parameters
function isTheseParametersAvailable($required_fields)
{
    $error = false;
    $error_fields = "";
    $request_params = $_REQUEST;

    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        $response = array();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echo json_encode($response);
        return false;
    }
    return true;
}


$app->run();