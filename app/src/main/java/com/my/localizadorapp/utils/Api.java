package com.my.localizadorapp.utils;


import com.my.localizadorapp.model.AddAddressModel;
import com.my.localizadorapp.model.CategoryModel;
import com.my.localizadorapp.model.ChangeMobileModel;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.CircleListNewModel;
import com.my.localizadorapp.model.CricleCreate;
import com.my.localizadorapp.model.DeleteModel;
import com.my.localizadorapp.model.FAQModel;
import com.my.localizadorapp.model.GetAddressModel;
import com.my.localizadorapp.model.GetUserChatModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.model.PrivacyModel;
import com.my.localizadorapp.model.ProductShopDeatils;
import com.my.localizadorapp.model.ProductShopModel;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.model.SignUpdataModel;
import com.my.localizadorapp.model.UpdateLocationModel;
import com.my.localizadorapp.model.UpdatedCircleModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String Api_signup = "signup";
    String Api_login = "login";
    String Api_get_profile = "get_profile";
    String Api_change_mobile = "change_mobile";
    String Api_create_circle = "create_circle";
    String Api_get_circle = "get_circle";
    String Api_update_circle_name = "update_circle_name";
    String Api_Join_circle = "join_circle";
    String Api_get_member_detail = "get_member_detail";
    String Api_delete_circle = "delete_circle";
    String Api_add_address = "add_address";
    String update_location = "update_location";
    String get_address = "get_address";
    String delete_member = "delete_member";
    String get_circle_user = "get_circle_user";

    String Api_PrivacyPolicy = "get_privacy";
    String Api_Terms = "get_terms";
    String Api_get_support = "get_support";
    String get_category = "get_category";
    String get_faq = "get_faq";
    String get_product_by_category = "get_product_by_category";
    String get_product_details = "get_product_details";
    String update_profile = "update_profile";

    @FormUrlEncoded
    @POST(Api_signup)
    Call<SignUpdataModel> Api_signup(
            @Field("name") String name,
            @Field("circle_name") String circle_name,
            @Field("mobile") String mobile,
            @Field("register_id") String register_id,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("password") String password,
            @Field("owner") String owner,
            @Field("battery") String battery,
            @Field("country_code") String country_code
    );

    @FormUrlEncoded
    @POST(Api_login)
    Call<SignUpModel> Api_login(
            @Field("mobile") String mobile,
            @Field("register_id") String register_id,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("password") String password,
            @Field("country_code") String country_code
    );

    @FormUrlEncoded
    @POST(Api_create_circle)
    Call<CricleCreate> Api_create_circle(
            @Field("user_id") String user_id,
            @Field("circle_name") String circle_name,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("owner") String owner,
            @Field("battery") String battery
    );

    @FormUrlEncoded
    @POST(Api_add_address)
    Call<AddAddressModel> Api_add_address(
            @Field("user_id") String user_id,
            @Field("address_type") String address_type,
            @Field("address") String address,
            @Field("lat") String lat,
            @Field("lon") String lon

    );

    @FormUrlEncoded
    @POST(Api_Join_circle)
    Call<CricleCreate> Api_Join_circle(
            @Field("user_id") String user_id,
            @Field("code") String code,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("battery") String battery
    );

    @FormUrlEncoded
    @POST(update_location)
    Call<UpdateLocationModel> update_location(
            @Field("user_id") String user_id,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("battery") String battery
    );

    @FormUrlEncoded
    @POST(Api_update_circle_name)
    Call<UpdatedCircleModel> Api_update_circle_name(
            @Field("circle_id") String circle_id,
            @Field("circle_name") String circle_name
    );

    @FormUrlEncoded
    @POST(Api_get_member_detail)
    Call<MemberListModel> Api_get_member_detail(
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST(get_circle_user)
    Call<GetUserChatModel> get_circle_user(
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST(delete_member)
    Call<ResponseBody> delete_member(
            @Field("user_id") String user_id,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST(Api_delete_circle)
    Call<DeleteModel> Api_delete_circle(
            @Field("circle_id") String circle_id
    );

    @FormUrlEncoded
    @POST(Api_get_circle)
    Call<CircleListModel> Api_get_circle(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(Api_get_circle)
    Call<CircleListNewModel> Api_get_circle_new(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(get_address)
    Call<GetAddressModel> get_address(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(Api_change_mobile)
    Call<ChangeMobileModel> Api_change_mobile(
            @Field("user_id") String user_id,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST(Api_get_profile)
    Call<SignUpModel> Api_get_profile(
            @Field("user_id") String user_id
    );

    @POST(Api_PrivacyPolicy)
    Call<PrivacyModel> Api_PrivacyPolicy();

    @POST(Api_Terms)
    Call<PrivacyModel> Api_Terms();

    @POST(Api_get_support)
    Call<PrivacyModel> Api_get_support();


    @POST(get_category)
    Call<CategoryModel> get_category();
    @POST(get_faq)
    Call<FAQModel> get_faq();

    @FormUrlEncoded
    @POST(get_product_by_category)
    Call<ProductShopModel> get_product_by_category(
            @Field("category_id") String category_id
    );

    @FormUrlEncoded
    @POST(get_product_details)
    Call<ProductShopDeatils> get_product_details(
            @Field("product_id") String product_id
    );

    @Multipart
    @POST(update_profile)
    Call<SignUpModel>update_profile(
            @Part("user_id") RequestBody user_id    ,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody cc,
            @Part MultipartBody.Part part1
    );

}
