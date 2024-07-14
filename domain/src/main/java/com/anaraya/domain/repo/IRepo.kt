package com.anaraya.domain.repo

import androidx.paging.PagingData
import com.anaraya.domain.entity.AboutUS
import com.anaraya.domain.entity.AddAllToBasket
import com.anaraya.domain.entity.AddDeleteFav
import com.anaraya.domain.entity.AddDeleteImage
import com.anaraya.domain.entity.AddRemoveCart
import com.anaraya.domain.entity.AddUpdateAddress
import com.anaraya.domain.entity.Addresses
import com.anaraya.domain.entity.ApplyPromo
import com.anaraya.domain.entity.Auth
import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Cart
import com.anaraya.domain.entity.Category
import com.anaraya.domain.entity.ChangeDefaultAddress
import com.anaraya.domain.entity.CheckAuth
import com.anaraya.domain.entity.CheckOut
import com.anaraya.domain.entity.Company
import com.anaraya.domain.entity.CompanyAddressItem
import com.anaraya.domain.entity.CompanyGovernorate
import com.anaraya.domain.entity.ContactNumber
import com.anaraya.domain.entity.DeliverySchedule
import com.anaraya.domain.entity.EditInfo
import com.anaraya.domain.entity.ExploreProduct
import com.anaraya.domain.entity.ExploreServices
import com.anaraya.domain.entity.FeedBack
import com.anaraya.domain.entity.Help
import com.anaraya.domain.entity.HelpDetails
import com.anaraya.domain.entity.MainCategory
import com.anaraya.domain.entity.Order
import com.anaraya.domain.entity.PlaceOrder
import com.anaraya.domain.entity.Product
import com.anaraya.domain.entity.ProductAd
import com.anaraya.domain.entity.ProductDetails
import com.anaraya.domain.entity.ProductStore
import com.anaraya.domain.entity.Profile
import com.anaraya.domain.entity.PromoCode
import com.anaraya.domain.entity.Referrals
import com.anaraya.domain.entity.Relationships
import com.anaraya.domain.entity.ResetChangePass
import com.anaraya.domain.entity.ServiceStoreItemList
import com.anaraya.domain.entity.Survey
import com.anaraya.domain.entity.SurveyBody
import com.anaraya.domain.entity.SurveyImage
import com.anaraya.domain.entity.Surveys
import com.anaraya.domain.entity.SurveysStatus
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IRepo {
    suspend fun checkAuth(
        rayaId: String,
        nationalId: String,
    ): CheckAuth

    suspend fun signIn(
        rayaId: Int,
        nationalId: Long,
        password: String,
    ): Auth

    suspend fun signIup(
        rayaId: String,
        nationalId: String,
        name: String,
        email: String?,
        password: String,
        phone: String,
        dateOfBirth: String?,
        gender: Int?,
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ): Auth

    suspend fun getProductsAds(): List<ProductAd>
    suspend fun getTrendingProducts(): Flow<PagingData<Product>>
    suspend fun getPointsProducts(): Flow<PagingData<Product>>

    suspend fun getHomeCategory(): Flow<PagingData<MainCategory>>
    suspend fun getProductById(productId: Int): ProductDetails
    suspend fun getProductsByCategory(mainCatId: Int): Flow<PagingData<Product>>
    suspend fun getProductsByMainCategory(mainCatId: Int): Flow<PagingData<Product>>
    suspend fun getProductsByBrand(brandId: Int): Flow<PagingData<Product>>

    suspend fun getProductsInsideMainCatAndCat(
        categoryId: Int,
        mainCatId: Int
    ): Flow<PagingData<Product>>

    //suspend fun getProductsByCategory(categoryId: Int): Flow<PagingData<Product>>
    suspend fun getAllCategoryInsideMainCat(mainCatId: Int): List<Category>
    suspend fun getAllProduct(): Flow<PagingData<Product>>
    suspend fun getAll(): Flow<PagingData<Product>>
    suspend fun addToCart(productId: Int, productQty: Int): AddRemoveCart
    suspend fun addPointToCart(productId: Int, productQty: Int): AddRemoveCart

    suspend fun removeProductFromCart(productId: Int): AddRemoveCart
    suspend fun removeProductPointFromCart(productId: Int): AddRemoveCart

    suspend fun getCart(): Cart

    suspend fun getAddress(): Addresses

    suspend fun getProfileData(): Profile

    suspend fun changeDefaultAddress(
        addressId: String,
        userOrCompanyAddress: Boolean
    ): ChangeDefaultAddress

    suspend fun addAddress(
        addressLabel: String,
        governorate: String,
        district: String,
        address: String,
        street: String,
        building: String,
        landmark: String,
    ): AddUpdateAddress

    suspend fun addCompanyAddress(companyAddressId: String): AddUpdateAddress


    suspend fun updateAddress(
        id: String,
        addressLabel: String,
        governorate: String,
        district: String,
        address: String,
        street: String,
        building: String,
        landmark: String,
    ): AddUpdateAddress

    suspend fun checkOut(): CheckOut
    suspend fun placeOrder(paymentMethod: String): PlaceOrder

    suspend fun updateName(name: String): EditInfo
    suspend fun updateEmail(email: String): EditInfo
    suspend fun updatePhoneNumber(phone: String): EditInfo
    suspend fun updateDOB(dob: String): EditInfo
    suspend fun updateGender(gender: Int): EditInfo
    suspend fun changePassword(currentPassword: String, newPassword: String): EditInfo

    suspend fun getOrders(): Order

    suspend fun forgetPass(rayaId: String, nationalId: String): ResetChangePass
    suspend fun forgetPassCheckCode(
        rayaId: String,
        nationalId: String,
        code: String
    ): ResetChangePass

    suspend fun resetPass(
        rayaId: String,
        nationalId: String,
        code: String,
        newPassword: String
    ): ResetChangePass

    suspend fun getAllFavorite(): List<Product>

    suspend fun addToFav(productId: Int): AddDeleteFav
    suspend fun deleteFromFav(productId: Int): AddDeleteFav

    suspend fun uploadImage(file: File): AddDeleteImage

    suspend fun getAllCompanies(): Company
    suspend fun getAllGovernorateByCompanyId(companyId: Int): CompanyGovernorate

    suspend fun getAllCompanyAddresses(
        companyId: Int,
        governorate: String
    ): Flow<PagingData<CompanyAddressItem>>


    suspend fun getAllHelp(): Help
    suspend fun getHelpDetails(helpId: Int): HelpDetails


    suspend fun getDeliverySchedule(): DeliverySchedule

    suspend fun getAllCats(): List<Category>

    suspend fun getAllBrands(): List<Category>
    suspend fun getFeedBackQuestion(): FeedBack
    suspend fun addFeedBack(rating: Int, review: String?): FeedBack

    suspend fun getAllPromoCodes(): PromoCode
    suspend fun applyPromo(promoCode: String): ApplyPromo
    suspend fun getAboutUs(): AboutUS
    suspend fun getTermsAndCondition(): BaseResponse
    suspend fun getPrivacyPolicy(): BaseResponse

    suspend fun getSupportContactNumber(): ContactNumber
    suspend fun addAllToBasket(): AddAllToBasket

    suspend fun deleteAddress(addressId: String): AddUpdateAddress

    suspend fun search(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
    ): Flow<PagingData<Product>>

    suspend fun getStoreProductCategory(): List<Category>
    suspend fun getStoreServicesCategory(): List<Category>
    suspend fun getStoreSubCategory(categoryId: Int): List<Category>

    suspend fun storeAddProduct(
        subCategoryId: String,
        condition: String,
        title: String,
        itemDescription: String,
        price: String,
        location: String,
        isAnonymous: Boolean,
        handleDelivery: Boolean,
        productImage: File,
    ): BaseResponse
    suspend fun storeUpdateProduct(
        id: Int,
        subCategoryId: String?,
        condition: String?,
        title: String?,
        itemDescription: String?,
        price: String?,
        location: String?,
        isAnonymous: Boolean?,
        handleDelivery: Boolean?,
        productImage: File?,
    ): BaseResponse
    suspend fun storeAddService(
        subCategoryId: String,
        title: String,
        itemDescription: String,
        price: String,
        location: String,
        serviceImage: File,
    ): BaseResponse

    suspend fun getStoreProduct(
        status: Int,
    ): Flow<PagingData<ProductStore>>
    suspend fun getStoreProductByIdForOwner(
        productId: Int
    ): ProductStore

    suspend fun getStoreService(
        status: Int,
    ): Flow<PagingData<ServiceStoreItemList>>

    suspend fun getExploreProducts(
        searchWord: String?,
        searchLanguage: String?,
        catID: Int?,
        subCatId: Int?,
    ): Flow<PagingData<ExploreProduct>>
    suspend fun getExploreServices(
        searchWord: String?,
        searchLanguage: String?,
        catID: Int?,
        subCatId: Int?,
    ): Flow<PagingData<ExploreServices>>

    suspend fun requestToDeleteProduct(
        customerProductId: Int,
    ): BaseResponse
    suspend fun requestToDeleteService(
        customerProductId: Int,
    ): BaseResponse

    suspend fun verifyPhone(
        code: String,
    ): BaseResponse

    suspend fun sendPhoneOtp(): BaseResponse

    suspend fun getAllRelationships(): List<Relationships>
    suspend fun addNewReferral(
        name: String,
        phoneNumber: String,
        relationshipId: Int,
        email: String? = null
    ): BaseResponse

    suspend fun getAllReferrals(): Referrals
    suspend fun checkHrIdFamily(hrId: String): BaseResponse
    suspend fun sendFamilyOTP(hrId: String, phoneNumber: String): BaseResponse
    suspend fun checkFamilyOTP(hrId: String, phoneNumber: String, otp: String): BaseResponse

    suspend fun signUpFamily(
        hrId: String,
        phoneNumber: String,
        otp: String,
        name: String,
        email: String?,
        password: String,
        dateOfBirth: String?,
        gender: Int?,
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ): Auth
    suspend fun signInFamily(
        rayaId: String,
        password: String,
    ): Auth

    suspend fun forgetPassFamily(rayaId: String): ResetChangePass
    suspend fun forgetPassCheckCodeFamily(
        rayaId: String,
        code: String
    ): ResetChangePass
    suspend fun resetPassFamily(
        rayaId: String,
        code: String,
        newPassword: String
    ): ResetChangePass
    suspend fun addToNotifyMe(productId: Int):BaseResponse
    suspend fun removeFromNotifyMe(productId: Int):BaseResponse
    suspend fun getUserLoyaltyPoints():BaseResponse
    suspend fun sendFCMToken(token: String, enabled: Boolean): BaseResponse
    suspend fun updateFCMToken(token: String, enabled: Boolean): BaseResponse
    suspend fun requestToBuy(productId: Int): BaseResponse
    suspend fun requestToRent(serviceId: Int, rentTo: String, rentFrom: String): BaseResponse
    suspend fun getSurveysStatus(): SurveysStatus
    suspend fun getAllSurveys(): Surveys
    suspend fun getSurvey(surveyId: Int): Survey
    suspend fun submitSurvey(surveyBody: SurveyBody): BaseResponse
    suspend fun getSurveyImage(): SurveyImage

}