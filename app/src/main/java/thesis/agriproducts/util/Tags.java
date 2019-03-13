package thesis.agriproducts.util;

public interface Tags {

    String HOME_FRAGMENT = "HomeFragment";
    String SELL_FRAGMENT = "SellFragment";
    String INBOX_FRAGMENT = "InboxFragment";
    String MESSAGES_FRAGMENT = "MessagesFragment";
    String MY_PRODUCTS_FRAGMENT = "MyProductsFragment";
    String ACCOUNT_FRAGMENT = "AccountFragment";
    String PRODUCT_DETAILS_FRAGMENT = "ProductDetailsFragment";
    String USER_DETAILS_FRAGMENT = "UserDetailsFragment";

    String USERS_FRAGMENT = "Users";
    String PRODUCTS_FRAGMENT = "Products";
    String ACCOUNT_ADMIN_FRAGMENT = "Admin Account";

    String SHARED_PREF_NAME = "agriprodsharedpref";
    String KEY_USER_ID = "userId";
    String KEY_ACCOUNT_ID = "accountId";
    String KEY_USER_NAME = "name";
    String KEY_USER_EMAIL = "email";
    String KEY_USER_NUMBER = "number";
    String KEY_USER_ADDRESS = "address";

    int ADMIN = 1;
    int USER = 2;

    int BUYING_FLAG = 1;
    int SELLING_FLAG = 2;
}
