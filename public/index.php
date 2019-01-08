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


//registering a new user
$app->post('/register', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('name', 'email', 'password', 'number', 'address', 'isAdmin'))) {
        $requestData = $request->getParsedBody();
        $name = $requestData['name'];
        $email = $requestData['email'];
        $password = $requestData['password'];
        $number = $requestData['number'];
        $address = $requestData['address'];
        $isAdmin = $requestData['isAdmin'];
        $db = new DbOperation();
        $responseData = array();

        $result = $db->registerUser($name, $email, $password, $number, $address, $isAdmin);

        if ($result == USER_CREATED) {
            $responseData['error'] = false;
            $responseData['message'] = 'Registered successfully';
        } elseif ($result == USER_CREATION_FAILED) {
            $responseData['error'] = true;
            $responseData['message'] = 'Some error occurred';
        } elseif ($result == USER_EXIST) {
            $responseData['error'] = true;
            $responseData['message'] = 'This email already exist, please login';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});


//user login route
$app->post('/login', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('email', 'password'))) {
        $requestData = $request->getParsedBody();
        $email = $requestData['email'];
        $password = $requestData['password'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->userLogin($email, $password, $userId)) {
            $responseData['error'] = false;
            // $responseData['user'] = $db->getUserByEmail($email);
            $responseData['user'] = $userId;
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Invalid email or password';
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

//getting all products
$app->get('/products', function (Request $request, Response $response) {
    $db = new DbOperation();
    $products = $db->getAllProducts();
    $response->getBody()->write(json_encode(array("products" => $products)));
});

//getting messages for a user
$app->get('/inbox/{id}', function (Request $request, Response $response) {
    $userid = $request->getAttribute('id');
    $db = new DbOperation();
    $inbox = $db->getMessages($userid);
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