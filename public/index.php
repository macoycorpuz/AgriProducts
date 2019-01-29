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
$container['users_directory'] = __DIR__ . '/../images/users/';
$container['products_directory'] = __DIR__ . '/../images/products/';

//region Users
$app->post('/login', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('email', 'password'))) {
        $requestData = $request->getParsedBody();
        $email = $requestData['email'];
        $password = $requestData['password'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->userLogin($email, $password, $user)) {
            $responseData['error'] = false;
            $responseData['user'] = $user[0];
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Invalid email or password';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});
$app->post('/register', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('name', 'email', 'password', 'number', 'address'))) {
        $requestData = $request->getParsedBody();
        $name = $requestData['name'];
        $email = $requestData['email'];
        $password = $requestData['password'];
        $number = $requestData['number'];
        $address = $requestData['address'];
        $db = new DbOperation();
        $responseData = array();

        if ($_FILES["userImage"]) {
            $directory = $this->get('users_directory');
            $userFile = getFileName();
            $result = $db->registerUser($name, $email, $password, $number, $address, $userFile, false);

            if ($result == USER_CREATED) {
                move_uploaded_file($_FILES["userImage"]["tmp_name"], $directory.$userFile);
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
$app->get('/users', function (Request $request, Response $response) {
    $db = new DbOperation();
    $users = $db->getAllUsers();
    if($users != []) $response->getBody()->write(json_encode(array("users" => $users)));
    else $response->getBody()->write(json_encode(array("error" => true, "message" => "No users found")));
});
$app->post('/users/change/password', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('userId', 'oldPassword', 'newPassword'))) {
        $requestData = $request->getParsedBody();
        $userId = $requestData['userId'];
        $oldPassword = $requestData['oldPassword'];
        $newPassword = $requestData['newPassword'];
        $db = new DbOperation();
        $responseData = array();

        $result = $db->changePassword($userId, $oldPassword, $newPassword);
        if ($result) {
            $responseData['error'] = false;
            $responseData['message'] = 'Password has been changed.';
        } else{
            $responseData['error'] = true;
            $responseData['message'] = 'Incorrect password.';
        } 
        $response->getBody()->write(json_encode($responseData));   
    }
});
//endregion

//region Products
$app->get('/products', function (Request $request, Response $response) {
    $db = new DbOperation();
    $products = $db->getAllProducts();
    $responseData = array();
    if($products != []) {
        $responseData['error'] = false;
        $responseData['products'] = $products;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No product/s found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
$app->post('/products/new', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('sellerId', 'productName', 'description', 'quantity', 'price', 'location', 'lat', 'lng'))) {
        $requestData = $request->getParsedBody();
        $sellerId = $requestData['sellerId'];
        $productName = $requestData['productName'];
        $description = $requestData['description'];
        $quantity = $requestData['quantity'];
        $price = $requestData['price'];
        $location = $requestData['location'];
        $lat = $requestData['lat'];
        $lng = $requestData['lng'];
        $db = new DbOperation();
        $responseData = array();

        if ($_FILES["productImage"]) {
            $directory = $this->get('products_directory');
            $productFile = getFileName();
            $result = $db->postProduct($sellerId, $productName, $description, $quantity, $price, $location, $lat, $lng, $productFile);
    
            if ($result == PRODUCT_CREATED) {
                move_uploaded_file($_FILES["productImage"]["tmp_name"], $directory.$productFile);
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
$app->get('/products/productId/{productId}', function (Request $request, Response $response)  {
    $productId = $request->getAttribute('productId');
    $db = new DbOperation();
    $product = $db->getProductbyId($productId);
    $responseData = array();
    if($product != null) {
        $responseData['error'] = false;
        $responseData['product'] = $product;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No product/s found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
$app->get('/products/userId/{userId}', function (Request $request, Response $response) {
    $userId = $request->getAttribute('userId');
    $db = new DbOperation();
    $products = $db->getProducts($userId);
    $responseData = array();
    if($products != []) {
        $responseData['error'] = false;
        $responseData['products'] = $products;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No product/s found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
$app->get('/products/productName/{productName}', function (Request $request, Response $response) {
    $productName = $request->getAttribute('productName');
    $db = new DbOperation();
    $products = $db->getProductbyName($productName);
    $responseData = array();
    if($products != []) {
        $responseData['error'] = false;
        $responseData['products'] = $products;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No product/s found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
$app->get('/products/sellerId/{sellerId}', function (Request $request, Response $response) {
    $sellerId = $request->getAttribute('sellerId');
    $db = new DbOperation();
    $products = $db->getMyProducts($sellerId);
    $responseData = array();
    if($products != []) {
        $responseData['error'] = false;
        $responseData['products'] = $products;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No product/s found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
$app->delete('/products/delete/{productId}', function (Request $request, Response $response) {
    $productId = $request->getAttribute('productId');
    $db = new DbOperation();
    $product = $db->getProductbyId($productId);
    if(isset($product)) $response->getBody()->write(json_encode(array("product" => $product)));
    else $response->getBody()->write(json_encode(array("error" => true, "message" => "Product not found")));
});
//endregion

//region Deals
$app->post('/deals/new', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('productId', 'buyerId', 'content'))) {
        $requestData = $request->getParsedBody();
        $productId = $requestData['productId'];
        $buyerId = $requestData['buyerId'];
        $content = $requestData['content'];

        $db = new DbOperation();

        $responseData = array();

        if ($db->sendDeal($productId, $buyerId, $content)) {
            $responseData['error'] = false;
            $responseData['message'] = 'Deal sent successfully';
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Could not send deal';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});
$app->get('/deals/selling/{userId}', function (Request $request, Response $response) {
    $userId = $request->getAttribute('userId');
    $db = new DbOperation();
    $deals = $db->getSelling($userId);
    $responseData = array();
    if($deals != []) {
        $responseData['error'] = false;
        $responseData['deals'] = $deals;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No selling deals found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
$app->get('/deals/buying/{userId}', function (Request $request, Response $response) {
    $userId = $request->getAttribute('userId');
    $db = new DbOperation();
    $deals = $db->getBuying($userId);
    $responseData = array();
    if($deals != []) {
        $responseData['error'] = false;
        $responseData['deals'] = $deals;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No buying deals found';
    }
    $response->getBody()->write(json_encode($responseData)); 
});
//endregion

//region Messages
$app->get('/messages/dealId/{dealId}/userId/{userId}', function (Request $request, Response $response) {
    $dealId = $request->getAttribute('dealId');
    $userId = $request->getAttribute('userId');
    $db = new DbOperation();
    $messages = $db->getMessages($dealId, $userId);
    $responseData = array();
    if($messages != []) {
        $responseData['error'] = false;
        $responseData['messages'] = $messages;
    } else {
        $responseData['error'] = true;
        $responseData['message'] = 'No buying deals found';
    }
    $response->getBody()->write(json_encode($responseData));
});
$app->post('/messages/new', function (Request $request, Response $response) {
    if (isTheseParametersAvailable(array('dealId', 'userId', 'content'))) {
        $requestData = $request->getParsedBody();
        $dealId = $requestData['dealId'];
        $userId = $requestData['userId'];
        $content = $requestData['content'];
        $db = new DbOperation();
        $responseData = array();
        if ($db->sendMessage($dealId, $userId, $content)) {
            $responseData['error'] = false;
            $responseData['message'] = 'Message sent successfully';
        } else {
            $responseData['error'] = true;
            $responseData['message'] = 'Could not send message';
        }

        $response->getBody()->write(json_encode($responseData));
    }
});
//endregion

//region Methods
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

function getFileName()
{
    $extension = "jpg";
    $basename = bin2hex(random_bytes(8)); 
    $filename = sprintf('%s.%0.8s', $basename, $extension);

    return $filename;
}
//endregion

$app->run();