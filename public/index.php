<?php

use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;
use \Slim\Http\UploadedFile;

require '../vendor/autoload.php';
require_once '../includes/DbOperation.php';

//Creating a new app with the config to show errors
$app = new \Slim\App([
    'settings' => [
        'displayErrorDetails' => true
    ]
]);

$container = $app->getContainer();
$container['users_directory'] = __DIR__ . '/../images/users';
$container['products_directory'] = __DIR__ . '/../images/products';

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
    if (isTheseParametersAvailable(array('name', 'email', 'password', 'number', 'address'))) {
        $requestData = $request->getParsedBody();
        $uploadedFiles = $request->getUploadedFiles();
        $name = $requestData['name'];
        $email = $requestData['email'];
        $password = $requestData['password'];
        $number = $requestData['number'];
        $address = $requestData['address'];
        $userImage = $uploadedFiles['userImage'];
        $db = new DbOperation();
        $responseData = array();

        if ($userImage->getError() === UPLOAD_ERR_OK) {
            $directory = $this->get('users_directory');
            $userFile = getFileName($userImage);
            $result = $db->registerUser($name, $email, $password, $number, $address, $userFile);

            if ($result == USER_CREATED) {
                moveUploadedFile($directory, $userFile, $userImage);
                $responseData['error'] = false;
                $responseData['message'] = 'Registered successfully';
            } elseif ($result == USER_CREATION_FAILED) {
                $responseData['error'] = true;
                $responseData['message'] = 'Unable to register user';
            } elseif ($result == USER_EXIST) {
                $responseData['error'] = true;
                $responseData['message'] = 'This email already exist';
            }            
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Image upload error';
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
$app->get('/productname/{productName}', function (Request $request, Response $response) {
    $productName = $request->getAttribute('productName');
    $db = new DbOperation();
    $products = $db->getProductbyName($productName);
    $response->getBody()->write(json_encode(array("product" => $products)));
});

//post product
$app->post('/product', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('sellerId', 'productName', 'description', 'quantity', 'price', 'location', 'lat', 'lng'))) {
        $requestData = $request->getParsedBody();
        $uploadedFiles = $request->getUploadedFiles();
        $sellerId = $requestData['sellerId'];
        $productName = $requestData['productName'];
        $description = $requestData['description'];
        $quantity = $requestData['quantity'];
        $price = $requestData['price'];
        $location = $requestData['location'];
        $lat = $requestData['lat'];
        $lng = $requestData['lng'];
        $productImage = $uploadedFiles['productImage'];
        $db = new DbOperation();
        $responseData = array();

        if ($productImage->getError() === UPLOAD_ERR_OK) {
            $directory = $this->get('products_directory');
            $productFile = getFileName($productImage);
            $result = $db->postProduct($sellerId, $productName, $description, $quantity, $price, $location, $lat, $lng, $productFile);
    
            if ($result == PRODUCT_CREATED) {
                moveUploadedFile($directory, $productFile, $productImage);
                $responseData['error'] = false;
                $responseData['message'] = 'Product has been posted';
            } elseif ($result == PRODUCT_CREATION_FAILED) {
                $responseData['error'] = true;
                $responseData['message'] = 'Unable to post product';
            }     
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Image upload error';
        }



        $response->getBody()->write(json_encode($responseData));
    }
});

//getting product by id
$app->get('/product/{id}', function (Request $request, Response $response) {
    $productId = $request->getAttribute('id');
    $db = new DbOperation();
    $product = $db->getProductbyId($productId);
    $response->getBody()->write(json_encode(array("product" => $product)));
});

//getting product by id
$app->delete('/product/{id}', function (Request $request, Response $response) {
    $productId = $request->getAttribute('id');
    $db = new DbOperation();
    $product = $db->getProductbyId($productId);
    $response->getBody()->write(json_encode(array("product" => $product)));
});

//getting selling deals
$app->get('/selling/{userId}', function (Request $request, Response $response) {
    $deals = $request->getAttribute('userId');
    $db = new DbOperation();
    $deals = $db->getSelling($userid);
    $response->getBody()->write(json_encode(array("deals" => $deals)));
});

//getting buying
$app->get('/buying/{userId}', function (Request $request, Response $response) {
    $deals = $request->getAttribute('userId');
    $db = new DbOperation();
    $deals = $db->getBuying($userid);
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

function getFileName(UploadedFile $uploadedFile)
{
    $extension = pathinfo($uploadedFile->getClientFilename(), PATHINFO_EXTENSION);
    $basename = bin2hex(random_bytes(8)); // see http://php.net/manual/en/function.random-bytes.php
    $filename = sprintf('%s.%0.8s', $basename, $extension);

    return $filename;
}

function moveUploadedFile($directory, $filename, UploadedFile $uploadedFile)
{
    $uploadedFile->moveTo($directory . DIRECTORY_SEPARATOR . $filename);
}

$app->run();