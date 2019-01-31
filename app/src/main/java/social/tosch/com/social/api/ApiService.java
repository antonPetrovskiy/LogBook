package social.tosch.com.social.api;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import social.tosch.com.social.entity.Boat;
import social.tosch.com.social.entity.CategoryList;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.entity.ContactList;
import social.tosch.com.social.entity.DialogList;
import social.tosch.com.social.entity.FeedList;
import social.tosch.com.social.entity.FriendList;
import social.tosch.com.social.entity.Message;
import social.tosch.com.social.entity.PhotoList;
import social.tosch.com.social.entity.MessageList;
import social.tosch.com.social.entity.PortList;

public interface ApiService {


    @FormUrlEncoded
    @POST("/api/auth")
    Call<ResponseBody> registartion(@Field("action") String action,
                               @Field("phone") String email,
                                @Field("phone_id") String pass,
                                @Field("name") String name);

    @POST("/api/user")
    Call<ContactList> getUserList(@Header("Authorization") String token,
                                  @Body JsonObject location);


    @FormUrlEncoded
    @POST("/api/port")
    Call<PortList> getPortList(@Header("Authorization") String token,
                               @Field("page") String page,
                               @Field("items_per_page") String items_per_page);


    @GET("/api/user-info")
    Call<Contact> getUser(@Header("Authorization") String token);

    @POST("/api/user")
    Call<Contact> getUserById(@Header("Authorization") String token,
                              @Body JsonObject location);

    @GET("/api/user-boat")
    Call<Boat> getBoat(@Header("Authorization") String token);

    @Multipart
    @POST("/api/user-boat/photo")
    Call<ResponseBody> setPhotoBoat(@Header("Authorization") String token,
                                @Part MultipartBody.Part photo);

    @Multipart
    @POST("/api/user-info/photo")
    Call<ResponseBody> setPhoto(@Header("Authorization") String token,
                                @Part MultipartBody.Part photo);

    @FormUrlEncoded
    @PUT("/api/user-info/update")
    Call<String> setUser(@Header("Authorization") String token,
                 @Field("name") String name,
                 @Field("country") String country,
                 @Field("age") String age,
                 @Field("email") String email);


    @PUT("/api/user-info/update")
    Call<ResponseBody> setCoord(@Header("Authorization") String token,
                                @Body JsonObject location);

    @PUT("/api/user-boat/update")
    Call<ResponseBody> setBoat(@Header("Authorization") String token,
                                @Body JsonObject location);


    @GET("/api/news")
    Call<FeedList> getFeedList(@Header("Authorization") String token,
                               @Query("category") String category);

    @FormUrlEncoded
    @POST("/api/news/create")
    Call<ResponseBody> setFeed(@Header("Authorization") String token,
                               @Field("title") String title,
                               @Field("text") String text,
                                @Field("category") String category);

    @GET("/api/message/get-correspondence-ids")
    Call<DialogList> getDialogList(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/message/create")
    Call<ResponseBody> sendMessage(@Header("Authorization") String token,
                               @Field("for_user_id") String id,
                               @Field("text") String text);

    @FormUrlEncoded
    @PUT("/api/message/update")
    Call<ResponseBody> editMessage(@Header("Authorization") String token,
                                    @Field("message_id") String id,
                                    @Field("text") String text);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/message/delete ", hasBody = true)
    Call<ResponseBody> deleteMessage(@Header("Authorization") String token,
                                      @Field("id") String id);

    @GET("/api/message")
    Call<MessageList> getMessages(@Header("Authorization") String token,
                                  @Query("user_id") String id);

    @GET("/api/message/read")
    Call<ResponseBody> readMessage(@Header("Authorization") String token,
                                  @Query("message_id") String id);

    @GET("/api/message/get-one")
    Call<Message> getMessage(@Header("Authorization") String token,
                              @Query("id") String id);

    @POST("/api/user-category/create")
    Call<ResponseBody> addCategory(@Header("Authorization") String token,
                                      @Body JsonObject categories);

    @HTTP(method = "DELETE", path = "/api/user-category/delete", hasBody = true)
    Call<ResponseBody> deleteCategory(@Header("Authorization") String token,
                                      @Body JsonObject categories_ids);

    @POST("/api/user")
    Call<CategoryList> getCategories(@Header("Authorization") String token,
                                     @Body JsonObject categories);

    @GET("/api/gallery")
    Call<PhotoList> getGallery(@Header("Authorization") String token);

    @GET("/api/gallery")
    Call<PhotoList> getGalleryById(@Header("Authorization") String token,
                                   @Query("user_id") String id);

    @Multipart
    @POST("/api/gallery/add")
    Call<ResponseBody> addGallery(@Header("Authorization") String token,
                                  @Part("title") String title,
                                  @Part MultipartBody.Part photo);

    @HTTP(method = "DELETE", path = "/api/gallery/delete", hasBody = true)
    Call<ResponseBody> deleteGallery(@Header("Authorization") String token,
                                     @Body JsonObject ids);

    @GET("/api/friend/invitation")
    Call<FriendList> getFriends(@Header("Authorization") String token,
                                @Query("type") String type);

    @GET("/api/friend/invite")
    Call<ResponseBody> setFriends(@Header("Authorization") String token,
                            @Query("id_user") String id_user);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/friend/delete-invitation", hasBody = true)
    Call<ResponseBody> deleteFriends(@Header("Authorization") String token,
                                    @Field("id") String id);



    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/message/remove-correspondence", hasBody = true)
    Call<ResponseBody> deleteDialog(@Header("Authorization") String token,
                                    @Field("user_id") String user_id);
}