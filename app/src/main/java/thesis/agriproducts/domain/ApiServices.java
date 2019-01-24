package thesis.agriproducts.domain;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.DELETE;
import thesis.agriproducts.model.entities.Message;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.model.entities.Deal;

public interface ApiServices {

//    String MAIN_URL = "http://agriproducts.000webhostapp.com/public/";
    String MAIN_URL = "http://192.168.1.6/AgriProducts/public/";

    //region Login and Sign Up
    @FormUrlEncoded
    @POST("login")
    Call<Result> login(@Field("email") String email, @Field("password") String password);

    @Multipart
    @POST("register")
    Call<Result> register(@Part("name") RequestBody name, @Part("email") RequestBody email,
                          @Part("password") RequestBody password, @Part("number") RequestBody number,
                          @Part("address") RequestBody address,
                          @Part("userImage\"; filename=\"validId.jpg\" ") RequestBody userImage);
    //endregion

    //region Home
    @GET("products/userId/{userId}")
    Call<Result> getProducts(@Path("userId") int userId);

    @GET("products/productName/{productName}")
    Call<Result> getProductByName(@Path("productName") String productName);
    //endregion

    //region Product Details
    @GET("products/productId/{productId}")
    Call<Result> getProduct(@Path("productId") int productId);

    @FormUrlEncoded
    @POST("deals/new")
    Call<Result> postDeal(@Field("productId") int productId, @Field("buyerId") int buyerId, @Field("content") String content);
    //endregion

    //region Sell Product
    @Multipart
    @POST("products/new")
    Call<Result> postProduct(@Part("sellerId") RequestBody sellerId, @Part("productName") RequestBody productName,
                             @Part("description") RequestBody description, @Part("quantity") RequestBody quantity,
                             @Part("price") RequestBody price, @Part("location") RequestBody location, @Part("lat") RequestBody lat,
                             @Part("lng") RequestBody lng,
                             @Part("productImage\"; filename=\"productImage.jpg\" ") RequestBody productImage);
    //endregion

    //region Inbox
    @GET("deals/selling/{userId}")
    Call<Result> getSelling(@Path("userId") int userId);

    @GET("deals/buying/{userId}")
    Call<Result> getBuying(@Path("userId") int userId);
    //endregion

    //region Messages
    @GET("messages/dealId/{dealId}/userId/{userId}")
    Call<Result> getMessages(@Path("dealId") int dealId, @Path("userId") int userId);

    @FormUrlEncoded
    @POST("messages/new")
    Call<Result> sendMessage(@Field("dealId") int dealId, @Field("userId") int userId, @Field("content") String content);
    //endregion

    //region My Products
    @GET("products/sellerId/{sellerId}")
    Call<Result> getMyProducts(@Path("sellerId") int userId);

    @DELETE("products/delete/{productId}")
    Call<Result> deleteProduct(@Path("productId") int productId);
    //endregion

    //region Account
    @FormUrlEncoded
    @POST("users/change/password")
    Call<Result> changePassoword(@Field("userId") int userId, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);
    //endregion

    //region Admin
    @GET("users")
    Call<Result> getAllUsers();

    @GET("products")
    Call<Result> getAllProducts();
    //endregion

}
