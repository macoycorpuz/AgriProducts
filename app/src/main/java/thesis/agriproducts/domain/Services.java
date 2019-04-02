package thesis.agriproducts.domain;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import thesis.agriproducts.model.entities.Result;

public interface Services {

    String MAIN_URL = "http://agriproducts.000webhostapp.com/public/";
    //String MAIN_URL = "http://192.168.1.7/AgriProducts/public/";

    //region User
    @GET("login/email/{email}/password/{password}")
    Call<Result> login(@Path("email") String email, @Path("password") String password);

    @FormUrlEncoded
    @POST("users")
    Call<Result> setAdmin(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @Multipart
    @POST("users")
    Call<Result> setUser(@Part("name") RequestBody name, @Part("email") RequestBody email,
                         @Part("password") RequestBody password, @Part("number") RequestBody number,
                         @Part("address") RequestBody address,
                         @Part("userImage\"; filename=\"validId.jpg\" ") RequestBody userImage);

    @GET("users")
    Call<Result> getUsers();

    @GET("users/delete/{userId}")
    Call<Result> deleteUser(@Path("userId") int userId);

    @FormUrlEncoded
    @POST("users/change/password")
    Call<Result> changePassword(@Field("userId") int userId, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @GET("users/activate/{userId}")
    Call<Result> activateUser(@Path("userId") int userId);

    @Multipart
    @POST("users/update")
    Call<Result> updateUser(@Part("userId") RequestBody userId,
                            @Part("name") RequestBody name,
                            @Part("email") RequestBody email,
                            @Part("number") RequestBody number,
                            @Part("address") RequestBody address);

    //endregion

    //region Products
    @Multipart
    @POST("products")
    Call<Result> setProduct(@Part("sellerId") RequestBody sellerId,
                            @Part("productName") RequestBody productName,
                            @Part("description") RequestBody description,
                            @Part("quantity") RequestBody quantity,
                            @Part("unit") RequestBody unit,
                            @Part("price") RequestBody price,
                            @Part("location") RequestBody location,
                            @Part("lat") RequestBody lat,
                            @Part("lng") RequestBody lng,
                            @Part("status") RequestBody status,
                            @Part("productImage\"; filename=\"productImage.jpg\" ") RequestBody productImage);

    @Multipart
    @POST("products/update")
    Call<Result> updateProduct(@Part("productId") RequestBody productId,
                               @Part("productName") RequestBody productName,
                               @Part("description") RequestBody description,
                               @Part("quantity") RequestBody quantity,
                               @Part("unit") RequestBody unit,
                               @Part("price") RequestBody price,
                               @Part("location") RequestBody location,
                               @Part("lat") RequestBody lat,
                               @Part("lng") RequestBody lng,
                               @Part("status") RequestBody status);

    @GET("products")
    Call<Result> getProducts();

    @GET("products/delete/{productId}")
    Call<Result> deleteProduct(@Path("productId") int productId);
    //endregion

    //region Deals
    @FormUrlEncoded
    @POST("deals")
    Call<Result> setDeal(@Field("productId") int productId, @Field("buyerId") int buyerId, @Field("content") String content);

    @GET("deals/{userId}")
    Call<Result> getDeals(@Path("userId") int userId);

    @GET("deals/selling/{userId}")
    Call<Result> getSelling(@Path("userId") int userId);

    @GET("deals/buying/{userId}")
    Call<Result> getBuying(@Path("userId") int userId);
    //endregion

    //region Messages
    @FormUrlEncoded
    @POST("messages")
    Call<Result> setMessage(@Field("dealId") int dealId, @Field("userId") int userId, @Field("content") String content);

    @GET("messages/{dealId}")
    Call<Result> getMessages(@Path("dealId") int dealId);
    //endregion

    //region Orders
    @FormUrlEncoded
    @POST("orders")
    Call<Result> setOrder(
            @Field("order_quantity") int quantity,
            @Field("order_status") String status,
            @Field("active") boolean active,
            @Field("total") double total,
            @Field("cash") double cash,
            @Field("productId") int product_id,
            @Field("buyerId") int user_id,
            @Field("credit_number") String number,
            @Field("expiry") String expiry,
            @Field("csv") int csv);

    @GET("orders/userId/{userId}/buying/{buying}")
    Call<Result> getOrders(
            @Path("userId") int user_id,
            @Path("buying") boolean buying);

    @FormUrlEncoded
    @POST("orders/update")
    Call<Result> updateOrderStatus(
            @Field("orderId") int id,
            @Field("order_status") String status,
            @Field("active") boolean active);

    @GET("orders/delete/{orderId}")
    Call<Result> deleteOrder(@Path("orderId") int id);
    //endregion
}
