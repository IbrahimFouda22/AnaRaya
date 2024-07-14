package com.anaraya.data.remote

import com.anaraya.data.dto.AboutUSDto
import com.anaraya.data.dto.AddAllToBasketDto
import com.anaraya.data.dto.AddDeleteFavDto
import com.anaraya.data.dto.AddDeleteImageDto
import com.anaraya.data.dto.AddRemoveCartDto
import com.anaraya.data.dto.AddUpdateAddressDto
import com.anaraya.data.dto.AddressesDto
import com.anaraya.data.dto.ApplyPromoDto
import com.anaraya.data.dto.AuthDto
import com.anaraya.data.dto.BaseResponseDto
import com.anaraya.data.dto.CartDto
import com.anaraya.data.dto.CategoryDto
import com.anaraya.data.dto.ChangeDefaultAddressDto
import com.anaraya.data.dto.CheckAuthDto
import com.anaraya.data.dto.CheckOutDto
import com.anaraya.data.dto.CompanyAddressDto
import com.anaraya.data.dto.CompanyDto
import com.anaraya.data.dto.CompanyGovernorateDto
import com.anaraya.data.dto.ContactNumberDto
import com.anaraya.data.dto.DeliveryScheduleDto
import com.anaraya.data.dto.EditInfoDto
import com.anaraya.data.dto.ExploreProductsDto
import com.anaraya.data.dto.ExploreServicesDto
import com.anaraya.data.dto.FavoriteDto
import com.anaraya.data.dto.FeedBackDto
import com.anaraya.data.dto.HelpDetailsDto
import com.anaraya.data.dto.HelpDto
import com.anaraya.data.dto.MainCategoryDto
import com.anaraya.data.dto.OrderDto
import com.anaraya.data.dto.PlaceOrderDto
import com.anaraya.data.dto.ProductDetailsDto
import com.anaraya.data.dto.ProductDto
import com.anaraya.data.dto.ProductStoreDto
import com.anaraya.data.dto.ProductStoreDtoDetails
import com.anaraya.data.dto.ProductsAdsDto
import com.anaraya.data.dto.ProfileDto
import com.anaraya.data.dto.PromoCodeDto
import com.anaraya.data.dto.ReferralsDto
import com.anaraya.data.dto.RelationshipsDto
import com.anaraya.data.dto.ResetChangePassDto
import com.anaraya.data.dto.ServiceStoreDto
import com.anaraya.data.dto.SurveyBodyDto
import com.anaraya.data.dto.SurveyDto
import com.anaraya.data.dto.SurveyImageDto
import com.anaraya.data.dto.SurveysDto
import com.anaraya.data.dto.SurveysStatusDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("CheckAuth")
    suspend fun checkAuth(
        @Field("hrid") rayaId: String,
        @Field("nationalID") nationalId: String,
    ): Response<CheckAuthDto>

    @FormUrlEncoded
    @POST("SignIn")
    suspend fun signIn(
        @Field("Hrid") rayaId: Int,
        @Field("NationalID") nationalId: Long,
        @Field("Password") password: String,
    ): Response<AuthDto>

    @FormUrlEncoded
    @POST("SignUp")
    suspend fun signUp(
        @Field("Hrid") rayaId: String,
        @Field("NationalID") nationalId: String,
        @Field("Name") name: String,
        @Field("Email") email: String? = null,
        @Field("Password") password: String,
        @Field("PhoneNumber") phoneNumber: String,
        @Field("DateOfBirth") dateOfBirth: String? = null,
        @Field("Gender") gender: Int? = null,
        @Field("AdressLabel") addressLabel: String? = null,
        @Field("Governorate") governorate: String? = null,
        @Field("District") district: String? = null,
        @Field("Address") address: String? = null,
        @Field("Street") street: String? = null,
        @Field("Building") building: String? = null,
        @Field("Landmark") landmark: String? = null,
        @Field("Lat") lat: Double? = null,
        @Field("Lng") lng: Double? = null,
    ): Response<AuthDto>

    @GET("Stocks/GetAllAdsStocks")
    suspend fun getProductsAds(): Response<ProductsAdsDto>

    @GET("Stocks/GetAllTrendingStocks")
    suspend fun getTrendingProducts(
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>

    @GET("Stocks/GetAllRedemptionStocks")
    suspend fun getPointsProducts(
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>

    @GET("Stocks/GetAll")
    suspend fun getAll(
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>

    @GET("Stocks/GetAllMainCatsWithImages")
    suspend fun getHomeCategory(
        @Query("PageNumber") pageNumber: Int,
    ): Response<MainCategoryDto>


    @GET("Stocks/GetStockByID")
    suspend fun getProductById(
        @Query("productId") productId: Int,
    ): Response<ProductDetailsDto>

    @GET("Stocks/FilterByMainCat")
    suspend fun getProductsByMainCategory(
        @Query("MainCatId") catId: Int,
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>
    @GET("Stocks/FilterByCat")
    suspend fun getProductsByCategory(
        @Query("CatId") catId: Int,
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>

    @GET("Stocks/FilterByBrand")
    suspend fun getProductsByBrand(
        @Query("BrandId") brandId: Int,
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>

    @FormUrlEncoded
    @POST("Stocks/Filter")
    suspend fun getProductsInsideMainCatAndCat(
        @Field("CatId") catId: Int,
        @Field("MainCatId") mainCatId: Int,
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>


    @GET("Stocks/GetAllCatsByMainCatId")
    suspend fun getAllCatInsideMainCat(
        @Query("MainCatId") mainCatId: Int,
    ): Response<CategoryDto>


    @GET("Stocks/GetAllTrendingStocks")
    suspend fun getAllTrendingProduct(
        @Query("PageNumber") pageNumber: Int,
    ): Response<ProductDto>

    @GET("Stocks/GetAllFavouriteStocks")
    suspend fun getAllFavorite(): Response<FavoriteDto>

    @POST("Stocks/AddStockToFavouriteStocks")
    suspend fun addToFav(
        @Query("productId") productId: Int,
    ): Response<AddDeleteFavDto>

    @DELETE("Stocks/RemoveStockFromFavouriteStocks")
    suspend fun deleteFromFav(
        @Query("productId") productId: Int,
    ): Response<AddDeleteFavDto>

    @POST("UserBasket/AddStock")
    suspend fun addProductToCart(
        @Query("productId") productId: Int,
        @Query("productQty") productQty: Int,
    ): Response<AddRemoveCartDto>

    @POST("UserBasket/LoyaltyPoints/AddStock")
    suspend fun addProductPointToCart(
        @Query("productId") productId: Int,
        @Query("productQty") productQty: Int,
    ): Response<AddRemoveCartDto>

    @GET("UserBasket/GetAll")
    suspend fun getProductCart(
    ): Response<CartDto>

    @GET("User/GetAllAddresses")
    suspend fun getAddresses(): Response<AddressesDto>

    @GET("User/UserProfile")
    suspend fun getProfileData(): Response<ProfileDto>

    @PUT("User/ChangeDefaultAddress")
    suspend fun changeDefaultAddress(
        @Query("AddressId") addressId: String,
        @Query("UserOrCompanyAddress") userOrCompanyAddress: Boolean,
    ): Response<ChangeDefaultAddressDto>

    @FormUrlEncoded
    @POST("User/AddAddress")
    suspend fun addAddress(
        @Field("AdressLabel") addressLabel: String,
        @Field("Governorate") governorate: String,
        @Field("District") district: String,
        @Field("Address") address: String,
        @Field("Street") street: String,
        @Field("Building") building: Int,
        @Field("Landmark") landmark: String,
    ): Response<AddUpdateAddressDto>


    @POST("CompanyAddresses/AddToFavourites")
    suspend fun addCompanyAddress(
        @Query("CompanyAddressId") companyAddressId: String,
    ): Response<AddUpdateAddressDto>

    @FormUrlEncoded
    @PUT("User/UpdateAddress")
    suspend fun updateAddress(
        @Field("ID") id: String,
        @Field("AdressLabel") addressLabel: String,
        @Field("Governorate") governorate: String,
        @Field("District") district: String,
        @Field("Address") address: String,
        @Field("Street") street: String,
        @Field("Building") building: String,
        @Field("Landmark") landmark: String,
    ): Response<AddUpdateAddressDto>

    @DELETE("UserBasket/RemoveStock")
    suspend fun removeProductFromCart(
        @Query("productId") productId: Int,
    ): Response<AddRemoveCartDto>

    @DELETE("UserBasket/LoyaltyPoints/RemoveStock")
    suspend fun removeProductPointsFromCart(
        @Query("productId") productId: Int,
    ): Response<AddRemoveCartDto>

    @GET("UserCheckOut/Get")
    suspend fun checkOut(
    ): Response<CheckOutDto>

    @POST("UserCheckOut/PlaceOrder")
    suspend fun placeOrder(
        @Query("PaymentMethod") paymentMethod: String,
    ): Response<PlaceOrderDto>

    @PUT("User/UpdateName")
    suspend fun updateName(
        @Query("name") name: String,
    ): Response<EditInfoDto>

    @PUT("User/UpdateEmail")
    suspend fun updateEmail(
        @Query("email") email: String,
    ): Response<EditInfoDto>

    @PUT("User/UpdatePhoneNumber")
    suspend fun updatePhoneNumber(
        @Query("phoneNumber") phoneNumber: String,
    ): Response<EditInfoDto>

    @PUT("User/UpdateDateOfBirth")
    suspend fun updateDOB(
        @Query("dateOfBirth") dateOfBirth: String,
    ): Response<EditInfoDto>

    @PUT("User/UpdateGender")
    suspend fun updateGender(
        @Query("gender") gender: Int,
    ): Response<EditInfoDto>

    @FormUrlEncoded
    @POST("User/ChangePassword")
    suspend fun changePass(
        @Field("CurrentPassword") currentPassword: String,
        @Field("NewPassword") newPassword: String,
    ): Response<EditInfoDto>

    @GET("Orders/GetAll")
    suspend fun getOrders(
    ): Response<OrderDto>

    @FormUrlEncoded
    @POST("ForgetPassword")
    suspend fun forgetPass(
        @Field("Hrid") rayaId: String,
        @Field("NationalID") nationalId: String,
    ): Response<ResetChangePassDto>

    @FormUrlEncoded
    @POST("ForgetPassword/CheckCode")
    suspend fun forgetPassCheckCode(
        @Field("Hrid") rayaId: String,
        @Field("NationalID") nationalId: String,
        @Field("Code") code: String,
    ): Response<ResetChangePassDto>

    @FormUrlEncoded
    @POST("ResetPassword")
    suspend fun resetPass(
        @Field("Hrid") rayaId: String,
        @Field("NationalID") nationalId: String,
        @Field("Code") code: String,
        @Field("NewPassword") newPassword: String,
    ): Response<ResetChangePassDto>

    @Multipart
    @PUT("User/UpdateProfilePicture")
    suspend fun uploadImage(
        @Part File: MultipartBody.Part,
    ): Response<AddDeleteImageDto>

    @GET("Company/GetAll")
    suspend fun getAllCompanies(
    ): Response<CompanyDto>

    @GET("CompanyAddresses/GetGovernorateByCompanyId")
    suspend fun getAllGovernorateByCompanyId(
        @Query("CompanyId") companyId: Int,
    ): Response<CompanyGovernorateDto>

    @GET("CompanyAddress/GetByCompanyIdAndGovernorate")
    suspend fun getAllCompanyAddress(
        @Query("PageNumber") pageNumber: Int,
        @Query("CompanyId") companyId: Int,
        @Query("Governorate") governorate: String,
    ): Response<CompanyAddressDto>

    @GET("Help/GetAllHelp")
    suspend fun getAllHelp(): Response<HelpDto>

    @GET("Help/GetHelpProplemsAndAnswersByHelpId")
    suspend fun getHelpDetails(
        @Query("HelpId") helpId: Int,
    ): Response<HelpDetailsDto>

    @GET("OfficesDeliverySchedule/Get")
    suspend fun getDeliverySchedule(): Response<DeliveryScheduleDto>

    @GET("Stocks/GetAllCats")
    suspend fun getAllCats(): Response<CategoryDto>

    @GET("Stocks/GetAllBrands")
    suspend fun getAllBrands(): Response<CategoryDto>

    @GET("FeedBackQuestion/Get")
    suspend fun getFeedBackQuestion(): Response<FeedBackDto>

    @FormUrlEncoded
    @POST("User/AddUserFeedBack")
    suspend fun addFeedBack(
        @Field("Rating") rating: Int,
        @Field("Review") review: String?,
    ): Response<FeedBackDto>

    @POST("UserCheckOut/ApplyPromoCode")
    suspend fun applyPromoCode(
        @Query("PromoCode") promoCode: String,
    ): Response<ApplyPromoDto>

    @GET("PromoCodes/GetValidPromo")
    suspend fun getAllPromoCodes(): Response<PromoCodeDto>

    @GET("AboutUs/Get")
    suspend fun getAboutUs(): Response<AboutUSDto>

    @GET("TermsAndConditions/Get")
    suspend fun getTermsAndCondition(): Response<BaseResponseDto>

    @GET("PrivacyPolicy/Get")
    suspend fun getPrivacyPolicy(): Response<BaseResponseDto>

    @GET("Support/GetSupportNumber")
    suspend fun getSupportContactNumber(): Response<ContactNumberDto>

    @POST("Stocks/AddAllFavouritesToBasket")
    suspend fun addAllToBasket(): Response<AddAllToBasketDto>

    @DELETE("User/DeleteAddress")
    suspend fun deleteAddress(
        @Query("AddressId") addressId: String,
    ): Response<AddUpdateAddressDto>

    @FormUrlEncoded
    @POST("Stocks/Filter")
    suspend fun search(
        @Query("PageNumber") pageNumber: Int,
        @Query("SearchWord") searchWord: String,
        @Query("SearchLanguage") searchLanguage: String?,
        @Field("CatIds[]") catIds: List<Int>?,
        @Field("BrandIds[]") brandIds: List<Int>?,
        @Field("HighstOrLowestDiscount") highestOrLowestDiscount: Int?,
    ): Response<ProductDto>

    @GET("MarketPlaceCategory/GetAllForProducts")
    suspend fun getProductCategory(): Response<CategoryDto>

    @GET("MarketPlaceSubCategory/GetAllByCatId")
    suspend fun getStoreSubCategory(
        @Query("CategoryId") categoryId: Int,
    ): Response<CategoryDto>

    @GET("MarketPlaceCategory/GetAllForServices")
    suspend fun getServiceCategory(): Response<CategoryDto>

    @Multipart
    @POST("MarketPlaceProduct/Add")
    suspend fun storeAddProduct(
        @Part("MarketPlaceSubCategoryId") subCategoryId: RequestBody,
        @Part("Condition") condition: RequestBody,
        @Part("Title") title: RequestBody,
        @Part("ItemDescription") itemDescription: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("Location") location: RequestBody,
        @Part("IsAnonymous") isAnonymous: RequestBody,
        @Part("HandleDelivery") handleDelivery: RequestBody,
        @Part ProductImage: MultipartBody.Part,
    ): Response<BaseResponseDto>

    @Multipart
    @POST("MarketPlaceService/Add")
    suspend fun storeAddService(
        @Part("MarketPlaceSubCategoryId") subCategoryId: RequestBody,
        @Part("Title") title: RequestBody,
        @Part("ServiceDescription") itemDescription: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("Location") location: RequestBody,
        @Part Servicemage: MultipartBody.Part,
    ): Response<BaseResponseDto>

    @GET("MarketPlaceProduct/GetByStatus")
    suspend fun getStoreMyProduct(
        @Query("PageNumber") pageNumber: Int,
        @Query("status") status: Int,
    ): Response<ProductStoreDto>
    @GET("MarketPlaceProduct/GetByIdForOwner")
    suspend fun getStoreProductByIdForOwner(
        @Query("ProductId") productId: Int,
    ): Response<ProductStoreDtoDetails>

    @GET("MarketPlaceService/GetByStatus")
    suspend fun getStoreMyService(
        @Query("PageNumber") pageNumber: Int,
        @Query("status") status: Int,
    ): Response<ServiceStoreDto>

    @PUT("MarketPlaceProduct/Cancel")
    suspend fun requestToDeleteProduct(
        @Query("CustomerProductId") customerProductId: Int,
    ): Response<BaseResponseDto>

    @PUT("MarketPlaceService/Cancel")
    suspend fun requestToDeleteService(
        @Query("CustomerProductId") customerProductId: Int,
    ): Response<BaseResponseDto>

    @Multipart
    @PUT("MarketPlaceProduct/Update")
    suspend fun storeUpdateProduct(
        @Part("id") id: RequestBody?,
        @Part("MarketPlaceSubCategoryId") subCategoryId: RequestBody?,
        @Part("Condition") condition: RequestBody?,
        @Part("Title") title: RequestBody?,
        @Part("ItemDescription") itemDescription: RequestBody?,
        @Part("Price") price: RequestBody?,
        @Part("Location") location: RequestBody?,
        @Part("IsAnonymous") isAnonymous: RequestBody?,
        @Part("HandleDelivery") handleDelivery: RequestBody?,
        @Part ProductImage: MultipartBody.Part?,
    ): Response<BaseResponseDto>

    @Multipart
    @PUT("MarketPlaceService/Update")
    suspend fun storeUpdateService(
        @Part("MarketPlaceSubCategoryId") subCategoryId: RequestBody,
        @Part("Condition") condition: RequestBody,
        @Part("Title") title: RequestBody,
        @Part("ItemDescription") itemDescription: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("Location") location: RequestBody,
        @Part ProductImage: MultipartBody.Part,
    ): Response<BaseResponseDto>

    @POST("User/VerifyPhoneNumber")
    suspend fun verifyPhone(
        @Query("code") code: String,
    ): Response<BaseResponseDto>

    @POST("User/PhoneNumberOTP")
    suspend fun sendPhoneOtp(): Response<BaseResponseDto>

    @GET("Family/GetAllRelationships")
    suspend fun getAllRelationships(): Response<RelationshipsDto>

    @POST("Family/AddNewReferral")
    suspend fun addNewReferral(
        @Query("Name") name: String,
        @Query("PhoneNumber") phoneNumber: String,
        @Query("RelationshipId") relationshipId: Int,
        @Query("Email") email: String? = null,
    ): Response<BaseResponseDto>

    @GET("Family/GetAllReferrals")
    suspend fun getAllReferrals(): Response<ReferralsDto>

    @POST("Family/CheckHrId")
    suspend fun checkHrIdFamily(
        @Query("HrId") hrId: String,
    ): Response<BaseResponseDto>

    @POST("Family/SendOTP")
    suspend fun sendFamilyOTP(
        @Query("HrId") hrId: String,
        @Query("PhoneNumber") phoneNumber: String,
    ): Response<BaseResponseDto>

    @POST("Family/CheckOTP")
    suspend fun checkFamilyOTP(
        @Query("HrId") hrId: String,
        @Query("PhoneNumber") phoneNumber: String,
        @Query("Otp") otp: String,
    ): Response<BaseResponseDto>

    @FormUrlEncoded
    @POST("Family/SignUp")
    suspend fun signUpFamily(
        @Query("HrId") hrId: String,
        @Query("PhoneNumber") phoneNumber: String,
        @Query("Otp") otp: String,
        @Field("Name") name: String,
        @Field("Email") email: String? = null,
        @Field("Password") password: String,
        @Field("DateOfBirth") dateOfBirth: String? = null,
        @Field("Gender") gender: Int? = null,
        @Field("AdressLabel") addressLabel: String? = null,
        @Field("Governorate") governorate: String? = null,
        @Field("District") district: String? = null,
        @Field("Address") address: String? = null,
        @Field("Street") street: String? = null,
        @Field("Building") building: String? = null,
        @Field("Landmark") landmark: String? = null,
        @Field("Lat") lat: Double? = null,
        @Field("Lng") lng: Double? = null,
    ): Response<AuthDto>

    @FormUrlEncoded
    @POST("Family/SignIn")
    suspend fun signInFamily(
        @Field("HrCode") hrCode: String,
        @Field("Password") password: String,
    ): Response<AuthDto>

    @POST("Family/ForgetPassword")
    suspend fun forgetPassFamily(
        @Query("HrCode") hrCode: String,
    ): Response<ResetChangePassDto>

    @FormUrlEncoded
    @POST("Family/ForgetPassword/CheckCode")
    suspend fun forgetPassCheckCodeFamily(
        @Field("HrCode") hrCode: String,
        @Field("Code") code: String,
    ): Response<ResetChangePassDto>

    @FormUrlEncoded
    @POST("Family/ResetPassword")
    suspend fun resetPassFamily(
        @Field("HrCode") hrCode: String,
        @Field("Code") code: String,
        @Field("NewPassword") newPassword: String,
    ): Response<ResetChangePassDto>

    @GET("Stocks/AddToNotifyMe")
    suspend fun addToNotifyMe(
        @Query("StockId") productId: Int,
    ): Response<BaseResponseDto>

    @GET("Stocks/RemoveFromNotifyMe")
    suspend fun removeFromNotifyMe(
        @Query("StockId") productId: Int,
    ): Response<BaseResponseDto>

    @GET("User/LoyaltyPoints")
    suspend fun getUserLoyaltyPoints(
    ): Response<BaseResponseDto>

    @POST("PushNotification/SendDeviceToken")
    suspend fun sendFCMToken(
        @Query("DeviceToken", encoded = true) deviceToken: String,
        @Query("IsPushNotificationEnabled") isPushNotificationEnabled: Boolean,
    ): Response<BaseResponseDto>

    @PUT("PushNotification/EnableAndDisable")
    suspend fun updateFCMToken(
        @Query("DeviceToken", encoded = true) deviceToken: String,
        @Query("EnableOrDisable") enableOrDisable: Boolean,
    ): Response<BaseResponseDto>

    @GET("MarketPlaceProduct/GetAll")
    suspend fun getExploreProducts(
        @Query("PageNumber") pageNumber: Int,
        @Query("SearchWord") searchWord: String? = null,
        @Query("SearchLanguage") searchLanguage: String? = null,
        @Query("CatID") catID: Int? = null,
        @Query("SubCatId") subCatId: Int? = null,
    ): Response<ExploreProductsDto>

    @GET("MarketPlaceService/GetAll")
    suspend fun getExploreServices(
        @Query("PageNumber") pageNumber: Int,
        @Query("SearchWord") searchWord: String? = null,
        @Query("SearchLanguage") searchLanguage: String? = null,
        @Query("CatID") catID: Int? = null,
        @Query("SubCatId") subCatId: Int? = null,
    ): Response<ExploreServicesDto>

    @POST("MarketPlaceProduct/RequestToBuy")
    suspend fun requestToBuy(
        @Query("ProductId") productId: Int
    ):Response<BaseResponseDto>
    @POST("MarketPlaceService/RequestToRent")
    suspend fun requestToRent(
        @Query("ServiceId") serviceId: Int,
        @Query("RentTo") rentTo: String,
        @Query("RentFrom") rentFrom: String
    ):Response<BaseResponseDto>
    @GET("Surveys/GetStatus")
    suspend fun getSurveysStatus(): Response<SurveysStatusDto>
    @GET("Survey/GetAll")
    suspend fun getAllSurveys(): Response<SurveysDto>
    @GET("Survey/GetById")
    suspend fun getSurvey(
        @Query("SurveyId") surveyId:Int
    ): Response<SurveyDto>
    @POST("Survey/Subbmit")
    suspend fun submitSurvey(
        @Body survey: SurveyBodyDto
    ): Response<BaseResponseDto>
    @GET("Survey/GetSurveyImage")
    suspend fun getSurveyImage():Response<SurveyImageDto>
}