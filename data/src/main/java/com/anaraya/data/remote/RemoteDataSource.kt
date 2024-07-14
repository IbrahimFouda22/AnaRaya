package com.anaraya.data.remote

import android.content.res.Resources.NotFoundException
import com.anaraya.data.mapper.toDto
import com.anaraya.data.mapper.toEntity
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
import com.anaraya.domain.entity.CompanyAddress
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
import com.anaraya.domain.exception.BadRequest
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.IllegalStateException
import com.anaraya.domain.exception.InternalServerException
import com.anaraya.domain.exception.NoInternetException
import com.anaraya.domain.exception.ResetPasswordBlockedException
import com.anaraya.domain.exception.ServerException
import com.anaraya.domain.exception.SignUpDataException
import com.anaraya.domain.exception.UnAuthException
import com.google.gson.JsonSyntaxException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: ApiService) : IRemoteDataSource {
    override suspend fun checkAuth(rayaId: String, nationalId: String): CheckAuth {
        return wrapApiResponse {
            apiService.checkAuth(rayaId, nationalId)
        }.toEntity()
    }

    override suspend fun signIn(rayaId: Int, nationalId: Long, password: String): Auth {
        return wrapApiResponse {
//            val json = JsonObject()
//            json.addProperty("hrid",rayaId)
//            json.addProperty("nationalID",nationalId)
//            json.addProperty("Password",password)
            apiService.signIn(rayaId, nationalId, password)
        }.toEntity()
    }

    override suspend fun signUp(
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
    ): Auth {
        return wrapApiResponse {
            apiService.signUp(
                rayaId,
                nationalId,
                name,
                email,
                password,
                phone,
                dateOfBirth,
                gender,
                addressLabel,
                governorate,
                district,
                address,
                street,
                building,
                landmark
            )
        }.toEntity()
    }

    override suspend fun getProductsAds(): List<ProductAd> {
        return wrapApiResponse {
            apiService.getProductsAds()
        }.toEntity()
    }

    override suspend fun getTrendingProducts(pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getTrendingProducts(pageNumber)
        }.toEntity()
    }

    override suspend fun getPointsProducts(pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getPointsProducts(pageNumber)
        }.toEntity()
    }

    override suspend fun getAll(pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getAll(pageNumber)
        }.toEntity()
    }

    override suspend fun getHomeCategory(pageNumber: Int): List<MainCategory> {
        return wrapApiResponse {
            apiService.getHomeCategory(pageNumber)
        }.toEntity()
    }

    override suspend fun getProductById(productId: Int): ProductDetails {
        return wrapApiResponse {
            apiService.getProductById(productId)
        }.toEntity()
    }

    override suspend fun getProductsByCategory(
        mainCatId: Int, pageNumber: Int,
    ): List<Product> {
        return wrapApiResponse {
            apiService.getProductsByCategory(mainCatId, pageNumber)
        }.toEntity()
    }

    override suspend fun getProductsByMainCategory(mainCatId: Int, pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getProductsByMainCategory(mainCatId, pageNumber)
        }.toEntity()
    }

    override suspend fun getProductsByBrand(brandId: Int, pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getProductsByBrand(brandId, pageNumber)
        }.toEntity()
    }


    override suspend fun getProductsInsideMainCatAndCat(
        categoryId: Int, mainCatId: Int, pageNumber: Int,
    ): List<Product> {
        return wrapApiResponse {
            apiService.getProductsInsideMainCatAndCat(categoryId, mainCatId, pageNumber)
        }.toEntity()
    }

    override suspend fun getAllCategoryInsideMainCat(mainCatId: Int): List<Category> {
        return wrapApiResponse {
            apiService.getAllCatInsideMainCat(mainCatId)
        }.toEntity()
    }


    /*override suspend fun getProductsByCategory(categoryId: Int, pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getProductsByCategory(categoryId, pageNumber)
        }.toEntity()
    }*/

    /*override suspend fun getAllCategory(): List<Category> {
        return wrapApiResponse {
            apiService.getAllCategory()
        }.toEntity()
    }*/

    override suspend fun getAllProduct(pageNumber: Int): List<Product> {
        return wrapApiResponse {
            apiService.getAllTrendingProduct(pageNumber)
        }.toEntity()
    }

    override suspend fun addToCart(productId: Int, productQty: Int): AddRemoveCart {
        return wrapApiResponse {
            apiService.addProductToCart(productId, productQty)
        }.toEntity()
    }

    override suspend fun addPointToCart(productId: Int, productQty: Int): AddRemoveCart {
        return wrapApiResponse {
            apiService.addProductPointToCart(productId, productQty)
        }.toEntity()
    }

    override suspend fun removeProductFromCart(productId: Int): AddRemoveCart {
        return wrapApiResponse {
            apiService.removeProductFromCart(productId)
        }.toEntity()
    }

    override suspend fun removeProductPointFromCart(productId: Int): AddRemoveCart {
        return wrapApiResponse {
            apiService.removeProductPointsFromCart(productId)
        }.toEntity()
    }

    override suspend fun getCart(): Cart {
        return wrapApiResponse {
            apiService.getProductCart()
        }.toEntity()
    }

    override suspend fun getAddress(): Addresses {
        return wrapApiResponse {
            apiService.getAddresses()
        }.toEntity()
    }

    override suspend fun getProfileData(): Profile {
        return wrapApiResponse {
            apiService.getProfileData()
        }.toEntity()
    }

    override suspend fun changeDefaultAddress(
        addressId: String, userOrCompanyAddress: Boolean,
    ): ChangeDefaultAddress {
        return wrapApiResponse {
            apiService.changeDefaultAddress(addressId, userOrCompanyAddress)
        }.toEntity()
    }

    override suspend fun addAddress(
        addressLabel: String,
        governorate: String,
        district: String,
        address: String,
        street: String,
        building: String,
        landmark: String,
    ): AddUpdateAddress {
        return wrapApiResponse {
            apiService.addAddress(
                addressLabel, governorate, district, address, street, building.toInt(), landmark
            )
        }.toEntity()
    }

    override suspend fun addCompanyAddress(companyAddressId: String): AddUpdateAddress {
        return wrapApiResponse {
            apiService.addCompanyAddress(companyAddressId)
        }.toEntity()
    }

    override suspend fun updateAddress(
        id: String,
        addressLabel: String,
        governorate: String,
        district: String,
        address: String,
        street: String,
        building: String,
        landmark: String,
    ): AddUpdateAddress {
        return wrapApiResponse {
            apiService.updateAddress(
                id, addressLabel, governorate, district, address, street, building, landmark
            )
        }.toEntity()
    }

    override suspend fun checkOut(): CheckOut {
        return wrapApiResponse {
            apiService.checkOut()
        }.toEntity()
    }

    override suspend fun placeOrder(paymentMethod: String): PlaceOrder {
        return wrapApiResponse {
            apiService.placeOrder(paymentMethod)
        }.toEntity()
    }

    override suspend fun updateName(name: String): EditInfo {
        return wrapApiResponse {
            apiService.updateName(name)
        }.toEntity()
    }

    override suspend fun updateEmail(email: String): EditInfo {
        return wrapApiResponse {
            apiService.updateEmail(email)
        }.toEntity()
    }

    override suspend fun updatePhoneNumber(phone: String): EditInfo {
        return wrapApiResponse {
            apiService.updatePhoneNumber(phone)
        }.toEntity()
    }

    override suspend fun updateDOB(dob: String): EditInfo {
        return wrapApiResponse {
            apiService.updateDOB(dob)
        }.toEntity()
    }

    override suspend fun updateGender(gender: Int): EditInfo {
        return wrapApiResponse {
            apiService.updateGender(gender)
        }.toEntity()
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): EditInfo {
        return wrapApiResponse {
            apiService.changePass(currentPassword, newPassword)
        }.toEntity()
    }

    override suspend fun getOrders(): Order {
        return wrapApiResponse {
            apiService.getOrders()
        }.toEntity()
    }

    override suspend fun forgetPass(rayaId: String, nationalId: String): ResetChangePass {
        return wrapApiResponse {
            apiService.forgetPass(rayaId, nationalId)
        }.toEntity()
    }

    override suspend fun forgetPassCheckCode(
        rayaId: String, nationalId: String, code: String,
    ): ResetChangePass {
        return wrapApiResponse {
            apiService.forgetPassCheckCode(rayaId, nationalId, code)
        }.toEntity()
    }

    override suspend fun resetPass(
        rayaId: String, nationalId: String, code: String, newPassword: String,
    ): ResetChangePass {
        return wrapApiResponse {
            apiService.resetPass(rayaId, nationalId, code, newPassword)
        }.toEntity()
    }

    override suspend fun getAllFavorite(): List<Product> {
        return wrapApiResponse {
            apiService.getAllFavorite()
        }.toEntity()
    }

    override suspend fun addToFav(productId: Int): AddDeleteFav {
        return wrapApiResponse {
            apiService.addToFav(productId)
        }.toEntity()
    }

    override suspend fun deleteFromFav(productId: Int): AddDeleteFav {
        return wrapApiResponse {
            apiService.deleteFromFav(productId)
        }.toEntity()
    }

    override suspend fun uploadImage(file: File): AddDeleteImage {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val photo = MultipartBody.Part.createFormData("File", file.name, requestFile)
        return wrapApiResponse {
            apiService.uploadImage(photo)
        }.toEntity()
    }

    override suspend fun getAllCompanies(): Company {
        return wrapApiResponse {
            apiService.getAllCompanies()
        }.toEntity()
    }

    override suspend fun getAllGovernorateByCompanyId(companyId: Int): CompanyGovernorate {
        return wrapApiResponse {
            apiService.getAllGovernorateByCompanyId(companyId)
        }.toEntity()
    }

    override suspend fun getAllCompanyAddresses(
        pageNumber: Int, companyId: Int, governorate: String,
    ): CompanyAddress {
        return wrapApiResponse {
            apiService.getAllCompanyAddress(pageNumber, companyId, governorate)
        }.toEntity()
    }

    override suspend fun getAllHelp(): Help {
        return wrapApiResponse {
            apiService.getAllHelp()
        }.toEntity()
    }

    override suspend fun getHelpDetails(helpId: Int): HelpDetails {
        return wrapApiResponse {
            apiService.getHelpDetails(helpId)
        }.toEntity()
    }

    override suspend fun getDeliverySchedule(): DeliverySchedule {
        return wrapApiResponse {
            apiService.getDeliverySchedule()
        }.toEntity()
    }

    override suspend fun getAllCats(): List<Category> {
        return wrapApiResponse {
            apiService.getAllCats()
        }.toEntity()
    }

    override suspend fun getAllBrands(): List<Category> {
        return wrapApiResponse {
            apiService.getAllBrands()
        }.toEntity()
    }

    override suspend fun getFeedBackQuestion(): FeedBack {
        return wrapApiResponse {
            apiService.getFeedBackQuestion()
        }.toEntity()
    }

    override suspend fun addFeedBack(rating: Int, review: String?): FeedBack {
        return wrapApiResponse {
            apiService.addFeedBack(rating, review)
        }.toEntity()
    }

    override suspend fun getAllPromoCodes(): PromoCode {
        return wrapApiResponse {
            apiService.getAllPromoCodes()
        }.toEntity()
    }

    override suspend fun applyPromo(promoCode: String): ApplyPromo {
        return wrapApiResponse {
            apiService.applyPromoCode(promoCode)
        }.toEntity()
    }

    override suspend fun getAboutUs(): AboutUS {
        return wrapApiResponse {
            apiService.getAboutUs()
        }.toEntity()
    }

    override suspend fun getTermsAndCondition(): BaseResponse {
        return wrapApiResponse {
            apiService.getTermsAndCondition()
        }.toEntity()
    }

    override suspend fun getPrivacyPolicy(): BaseResponse {
        return wrapApiResponse {
            apiService.getPrivacyPolicy()
        }.toEntity()
    }

    override suspend fun getSupportContactNumber(): ContactNumber {
        return wrapApiResponse {
            apiService.getSupportContactNumber()
        }.toEntity()
    }

    override suspend fun addAllToBasket(): AddAllToBasket {
        return wrapApiResponse {
            apiService.addAllToBasket()
        }.toEntity()
    }

    override suspend fun deleteAddress(addressId: String): AddUpdateAddress {
        return wrapApiResponse {
            apiService.deleteAddress(addressId)
        }.toEntity()
    }

    override suspend fun search(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
        pageNumber: Int,
    ): List<Product> {
        return wrapApiResponse {
            apiService.search(
                pageNumber = pageNumber,
                catIds = catIds,
                searchWord = searchWord,
                highestOrLowestDiscount = highestOrLowest,
                searchLanguage = searchLanguage,
                brandIds = brandIds
            )
        }.toEntity()
    }

    override suspend fun getStoreProductCategory(): List<Category> {
        return wrapApiResponse {
            apiService.getProductCategory()
        }.toEntity()
    }

    override suspend fun getStoreServicesCategory(): List<Category> {
        return wrapApiResponse {
            apiService.getServiceCategory()
        }.toEntity()
    }

    override suspend fun getStoreSubCategory(categoryId: Int): List<Category> {
        return wrapApiResponse {
            apiService.getStoreSubCategory(categoryId)
        }.toEntity()
    }

    override suspend fun storeAddProduct(
        subCategoryId: RequestBody,
        condition: RequestBody,
        title: RequestBody,
        itemDescription: RequestBody,
        price: RequestBody,
        location: RequestBody,
        isAnonymous: RequestBody,
        handleDelivery: RequestBody,
        productImage: MultipartBody.Part,
    ): BaseResponse {
        return wrapApiResponse {
            apiService.storeAddProduct(
                subCategoryId,
                condition,
                title,
                itemDescription,
                price,
                location,
                isAnonymous,
                handleDelivery,
                productImage
            )
        }.toEntity()

    }

    override suspend fun storeUpdateProduct(
        id: RequestBody,
        subCategoryId: RequestBody?,
        condition: RequestBody?,
        title: RequestBody?,
        itemDescription: RequestBody?,
        price: RequestBody?,
        location: RequestBody?,
        isAnonymous: RequestBody?,
        handleDelivery: RequestBody?,
        productImage: MultipartBody.Part?,
    ): BaseResponse {
        return wrapApiResponse {
            apiService.storeUpdateProduct(
                id,
                subCategoryId,
                condition,
                title,
                itemDescription,
                price,
                location,
                isAnonymous,
                handleDelivery,
                productImage
            )
        }.toEntity()
    }

    override suspend fun storeAddServices(
        subCategoryId: RequestBody,
        title: RequestBody,
        itemDescription: RequestBody,
        price: RequestBody,
        location: RequestBody,
        serviceImage: MultipartBody.Part,
    ): BaseResponse {
        return wrapApiResponse {
            apiService.storeAddService(
                subCategoryId, title, itemDescription, price, location, serviceImage
            )
        }.toEntity()
    }

    override suspend fun getStoreProduct(
        pageNumber: Int, status: Int,
    ): List<ProductStore> {
        return wrapApiResponse {
            apiService.getStoreMyProduct(pageNumber, status)
        }.toEntity()
    }

    override suspend fun getStoreProductByIdForOwner(productId: Int): ProductStore {
        return wrapApiResponse {
            apiService.getStoreProductByIdForOwner(productId)
        }.toEntity()
    }

    override suspend fun getStoreService(
        pageNumber: Int, status: Int,
    ): List<ServiceStoreItemList> {
        return wrapApiResponse {
            apiService.getStoreMyService(pageNumber, status)
        }.toEntity()
    }

    override suspend fun getExploreProducts(
        pageNumber: Int,
        searchWord: String?,
        searchLanguage: String?,
        catID: Int?,
        subCatId: Int?,
    ): List<ExploreProduct> {
        return wrapApiResponse {
            apiService.getExploreProducts(pageNumber, searchWord, searchLanguage, catID, subCatId)
        }.toEntity()
    }

    override suspend fun getExploreServices(
        pageNumber: Int,
        searchWord: String?,
        searchLanguage: String?,
        catID: Int?,
        subCatId: Int?,
    ): List<ExploreServices> {
        return wrapApiResponse {
            apiService.getExploreServices(pageNumber, searchWord, searchLanguage, catID, subCatId)
        }.toEntity()
    }


    override suspend fun requestToDeleteProduct(customerProductId: Int): BaseResponse {
        return wrapApiResponse {
            apiService.requestToDeleteProduct(customerProductId)
        }.toEntity()
    }

    override suspend fun requestToDeleteService(customerProductId: Int): BaseResponse {
        return wrapApiResponse {
            apiService.requestToDeleteService(customerProductId)
        }.toEntity()
    }

    override suspend fun verifyPhone(code: String): BaseResponse {
        return wrapApiResponse {
            apiService.verifyPhone(code)
        }.toEntity()
    }

    override suspend fun sendPhoneOtp(): BaseResponse {
        return wrapApiResponse {
            apiService.sendPhoneOtp()
        }.toEntity()
    }

    override suspend fun getAllRelationships(): List<Relationships> {
        return wrapApiResponse {
            apiService.getAllRelationships()
        }.toEntity()
    }

    override suspend fun addNewReferral(
        name: String,
        phoneNumber: String,
        relationshipId: Int,
        email: String?,
    ): BaseResponse {
        return wrapApiResponse {
            apiService.addNewReferral(name, phoneNumber, relationshipId, email)
        }.toEntity()
    }

    override suspend fun getAllReferrals(): Referrals {
        return wrapApiResponse {
            apiService.getAllReferrals()
        }.toEntity()
    }

    override suspend fun checkHrIdFamily(hrId: String): BaseResponse {
        return wrapApiResponse {
            apiService.checkHrIdFamily(hrId)
        }.toEntity()
    }

    override suspend fun sendFamilyOTP(hrId: String, phoneNumber: String): BaseResponse {
        return wrapApiResponse {
            apiService.sendFamilyOTP(hrId, phoneNumber)
        }.toEntity()
    }

    override suspend fun checkFamilyOTP(
        hrId: String,
        phoneNumber: String,
        otp: String,
    ): BaseResponse {
        return wrapApiResponse {
            apiService.checkFamilyOTP(hrId, phoneNumber, otp)
        }.toEntity()
    }

    override suspend fun signUpFamily(
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
    ): Auth {
        return wrapApiResponse {
            apiService.signUpFamily(
                hrId,
                phoneNumber,
                otp,
                name,
                email,
                password,
                dateOfBirth,
                gender,
                addressLabel,
                governorate,
                district,
                address,
                street,
                building,
                landmark
            )
        }.toEntity()
    }

    override suspend fun signInFamily(rayaId: String, password: String): Auth {
        return wrapApiResponse {
            apiService.signInFamily(hrCode = rayaId, password = password)
        }.toEntity()
    }

    override suspend fun forgetPassFamily(rayaId: String): ResetChangePass {
        return wrapApiResponse {
            apiService.forgetPassFamily(rayaId)
        }.toEntity()
    }

    override suspend fun forgetPassCheckCodeFamily(rayaId: String, code: String): ResetChangePass {
        return wrapApiResponse {
            apiService.forgetPassCheckCodeFamily(rayaId, code)
        }.toEntity()
    }

    override suspend fun resetPassFamily(
        rayaId: String,
        code: String,
        newPassword: String,
    ): ResetChangePass {
        return wrapApiResponse {
            apiService.resetPassFamily(rayaId, code, newPassword)
        }.toEntity()
    }

    override suspend fun addToNotifyMe(productId: Int): BaseResponse {
        return wrapApiResponse {
            apiService.addToNotifyMe(productId)
        }.toEntity()
    }

    override suspend fun removeFromNotifyMe(productId: Int): BaseResponse {
        return wrapApiResponse {
            apiService.removeFromNotifyMe(productId)
        }.toEntity()
    }

    override suspend fun getUserLoyaltyPoints(): BaseResponse {
        return wrapApiResponse {
            apiService.getUserLoyaltyPoints()
        }.toEntity()
    }

    override suspend fun sendFCMToken(token: String, enabled: Boolean): BaseResponse {
        return wrapApiResponse {
            apiService.sendFCMToken(token, enabled)
        }.toEntity()
    }

    override suspend fun updateFCMToken(token: String, enabled: Boolean): BaseResponse {
        return wrapApiResponse {
            apiService.updateFCMToken(token, enabled)
        }.toEntity()
    }

    override suspend fun requestToBuy(productId: Int): BaseResponse {
        return wrapApiResponse {
            apiService.requestToBuy(productId)
        }.toEntity()
    }

    override suspend fun requestToRent(
        serviceId: Int,
        rentTo: String,
        rentFrom: String,
    ): BaseResponse {
        return wrapApiResponse {
            apiService.requestToRent(
                serviceId, rentTo, rentFrom
            )
        }.toEntity()
    }

    override suspend fun getSurveysStatus(): SurveysStatus {
        return wrapApiResponse {
            apiService.getSurveysStatus()
        }.toEntity()
    }

    override suspend fun getAllSurveys(): Surveys {
        return wrapApiResponse {
            apiService.getAllSurveys()
        }.toEntity()
    }

    override suspend fun getSurvey(surveyId: Int): Survey {
        return wrapApiResponse {
            apiService.getSurvey(surveyId)
        }.toEntity()
    }

    override suspend fun submitSurvey(surveyBody: SurveyBody): BaseResponse {
        return wrapApiResponse {
            apiService.submitSurvey(surveyBody.toDto())
        }.toEntity()
    }

    override suspend fun getSurveyImage(): SurveyImage {
        return wrapApiResponse {
            apiService.getSurveyImage()
        }.toEntity()
    }


    private suspend fun <T> wrapApiResponse(
        request: suspend () -> Response<T>,
    ): T {
        try {
            val response = request()
            if (response.isSuccessful) {
                return response.body() ?: throw EmptyDataException("No data")
            } else {
                throw when (response.code()) {
                    400 -> {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        BadRequest(jObjError.getString("message"))
                        //BadRequest(response.errorBody()!!.string())
                    }

                    401 -> {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        UnAuthException(jObjError.getString("message"))
                        //UnAuthException(response.errorBody()!!.string())
                    }

                    403 -> {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        UnAuthException(jObjError.getString("message"))
                        //UnAuthException(response.errorBody()!!.string())
                    }

                    405 -> {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        UnAuthException(jObjError.getString("message"))
                        //UnAuthException(response.errorBody()!!.string())
                    }

                    409 -> {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        SignUpDataException(jObjError.getString("message"))
//                        SignUpDataException(response.errorBody()!!.string())
                    }

                    429 -> {
                        val jObjError = JSONObject(
                            response.errorBody()!!.string()
                        )
                        ResetPasswordBlockedException(jObjError.getString("message"))
//                        ResetPasswordBlockedException(response.errorBody()!!.string())
                    }

                    500 -> {
//                        val jObjError = JSONObject(
//                            response.errorBody()!!.string()
//                        )
                        InternalServerException("Internal Server Error")
                    }

                    404 -> {
//                        val jObjError = JSONObject(
//                            response.errorBody()!!.string()
//                        )
                        NotFoundException("Not Found")
                    }
                    //Connection reset

                    else -> ServerException("Server error")
                }
            }
        } catch (e: JsonSyntaxException) {
            throw IllegalStateException("IllegalStateException")
        } catch (e: UnknownHostException) {
            throw NoInternetException("No Internet")
        } catch (io: IOException) {
            throw NoInternetException(io.message)
        } catch (e: SocketTimeoutException) {
            throw NoInternetException("No Internet")
        } catch (e: SocketException) {
            throw NoInternetException("No Internet")
        }
    }

}