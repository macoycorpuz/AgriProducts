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
    @GET("products/{userId}")
    Call<Result> getProducts(@Path("userId") int userId);

    @GET("productName/{productName}")
    Call<Result> getProductbyName(@Path("productName") String productName);
    //endregion

    //region Product Details
    @GET("product/{productId}")
    Call<Result> getProduct(@Path("productId") int productId);

    @POST("deal")
    Call<Result> postDeal(@Body Deal deal);
    //endregion

    //region Sell Product
    @Multipart
    @POST("product")
    Call<Result> postProduct(@Part("sellerId") RequestBody sellerId, @Part("productName") RequestBody productName,
                             @Part("description") RequestBody description, @Part("quantity") RequestBody quantity,
                             @Part("price") RequestBody price, @Part("location") RequestBody location, @Part("lat") RequestBody lat,
                             @Part("lng") RequestBody lng,
                             @Part("productImage\"; filename=\"productImage.jpg\" ") RequestBody productImage);
    //endregion

    //region Inbox
    @GET("selling/userId")
    Call<Result> getSelling(@Path("userId") int userId);

    @GET("buying/userId")
    Call<Result> getBuying(@Path("userId") int userId);
    //endregion

    //region Messages
    @GET("messages/{dealId}/{userId}")
    Call<Result> getMessages(@Path("dealId") int dealId, @Path("userId") int userId);

    @POST("message")
    Call<Result> sendMessage(@Body Message message);
    //endregion

    //region My Products
    @GET("myproducts/{sellerId}")
    Call<Result> getMyProducts(@Path("sellerId") int userId);

    @DELETE("product/{productId}")
    Call<Result> deleteProduct(@Path("productId") int productId);
    //endregion

    //region Account
    @FormUrlEncoded
    @POST("changepassword")
    Call<Result> changePassoword(@Field("userId") int userId, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);
    //endregion

    //region Admin
    @GET("users")
    Call<Result> getUsers();
    //endregion

}
