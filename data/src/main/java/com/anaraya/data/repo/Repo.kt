package com.anaraya.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.anaraya.data.pagingsource.AllCompanyAddressPagingSource
import com.anaraya.data.pagingsource.AllProductByMainCatPagingSource
import com.anaraya.data.pagingsource.AllProductInsideMainCatAndCatPagingSource
import com.anaraya.data.pagingsource.AllProductPagingSource
import com.anaraya.data.pagingsource.AllTrendingProductPagingSource
import com.anaraya.data.pagingsource.MainCatPagingSource
import com.anaraya.data.pagingsource.SearchPagingSource
import com.anaraya.data.pagingsource.StoreProductServicePagingSource
import com.anaraya.data.pagingsource.TrendingProductPagingSource
import com.anaraya.data.remote.RemoteDataSource
import com.anaraya.domain.entity.Referrals
import com.anaraya.domain.repo.IRepo
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class Repo @Inject constructor(private val remoteDataSource: RemoteDataSource) : IRepo {
    companion object {
        const val PAGE_SIZE = 10
    }

    override suspend fun checkAuth(rayaId: String, nationalId: String) =
        remoteDataSource.checkAuth(rayaId, nationalId)

    override suspend fun signIn(rayaId: Int, nationalId: Long, password: String) =
        remoteDataSource.signIn(rayaId, nationalId, password)

    override suspend fun signIup(
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
    ) = remoteDataSource.signUp(
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

    override suspend fun getProductsAds() = remoteDataSource.getProductsAds()
    override suspend fun getTrendingProducts() = getData(::TrendingProductPagingSource)


    override suspend fun getHomeCategory() = getData(::MainCatPagingSource)
    override suspend fun getProductById(productId: Int) =
        remoteDataSource.getProductById(productId)

    override suspend fun getProductsByMainCategory(mainCatId: Int) =
        getDataById(mainCatId, ::AllProductByMainCatPagingSource)

    override suspend fun getProductsInsideMainCatAndCat(
        categoryId: Int,
        mainCatId: Int
    ) = getDataInsideMainCatAndCat(
        categoryId,
        mainCatId,
        ::AllProductInsideMainCatAndCatPagingSource
    )

    override suspend fun getAllCategoryInsideMainCat(mainCatId: Int) =
        remoteDataSource.getAllCategoryInsideMainCat(mainCatId)

    /*override suspend fun getProductsByCategory(categoryId: Int) =
        getDataById(categoryId, ::AllProductByCatPagingSource)*/

    //    override suspend fun getAllCategory() = remoteDataSource.getAllCategory()
    override suspend fun getAllProduct() = getData(::AllTrendingProductPagingSource)
    override suspend fun getAll() = getData(::AllProductPagingSource)
    override suspend fun addToCart(productId: Int, productQty: Int) =
        remoteDataSource.addToCart(productId, productQty)

    override suspend fun removeProductFromCart(productId: Int) =
        remoteDataSource.removeProductFromCart(productId)

    override suspend fun getCart() = remoteDataSource.getCart()
    override suspend fun getAddress() = remoteDataSource.getAddress()
    override suspend fun getProfileData() = remoteDataSource.getProfileData()
    override suspend fun changeDefaultAddress(addressId: String, userOrCompanyAddress: Boolean) =
        remoteDataSource.changeDefaultAddress(addressId, userOrCompanyAddress)

    override suspend fun addAddress(
        addressLabel: String,
        governorate: String,
        district: String,
        address: String,
        street: String,
        building: String,
        landmark: String
    ) = remoteDataSource.addAddress(
        addressLabel,
        governorate,
        district,
        address,
        street,
        building,
        landmark
    )

    override suspend fun addCompanyAddress(companyAddressId: String) =
        remoteDataSource.addCompanyAddress(companyAddressId)

    override suspend fun updateAddress(
        id: String,
        addressLabel: String,
        governorate: String,
        district: String,
        address: String,
        street: String,
        building: String,
        landmark: String
    ) = remoteDataSource.updateAddress(
        id,
        addressLabel,
        governorate,
        district,
        address,
        street,
        building,
        landmark
    )

    override suspend fun checkOut() = remoteDataSource.checkOut()
    override suspend fun placeOrder(paymentMethod: String) =
        remoteDataSource.placeOrder(paymentMethod)

    override suspend fun updateName(name: String) = remoteDataSource.updateName(name)

    override suspend fun updateEmail(email: String) = remoteDataSource.updateEmail(email)

    override suspend fun updatePhoneNumber(phone: String) =
        remoteDataSource.updatePhoneNumber(phone)

    override suspend fun getOrders() = remoteDataSource.getOrders()
    override suspend fun forgetPass(rayaId: String, nationalId: String) =
        remoteDataSource.forgetPass(rayaId, nationalId)

    override suspend fun forgetPassCheckCode(
        rayaId: String,
        nationalId: String,
        code: String
    ) = remoteDataSource.forgetPassCheckCode(rayaId, nationalId, code)

    override suspend fun resetPass(
        rayaId: String,
        nationalId: String,
        code: String,
        newPassword: String
    ) = remoteDataSource.resetPass(rayaId, nationalId, code, newPassword)

    override suspend fun getAllFavorite() = remoteDataSource.getAllFavorite()

    override suspend fun addToFav(productId: Int) = remoteDataSource.addToFav(productId)

    override suspend fun deleteFromFav(productId: Int) = remoteDataSource.deleteFromFav(productId)
    override suspend fun uploadImage(file: File) = remoteDataSource.uploadImage(file)
    override suspend fun getAllCompanies() = remoteDataSource.getAllCompanies()
    override suspend fun getAllGovernorateByCompanyId(companyId: Int) =
        remoteDataSource.getAllGovernorateByCompanyId(companyId)

    override suspend fun getAllCompanyAddresses(
        companyId: Int,
        governorate: String
    ) = getCompanyAddresses(companyId, governorate, ::AllCompanyAddressPagingSource)

    override suspend fun getAllHelp() = remoteDataSource.getAllHelp()
    override suspend fun getHelpDetails(helpId: Int) = remoteDataSource.getHelpDetails(helpId)

    override suspend fun getDeliverySchedule() = remoteDataSource.getDeliverySchedule()
    override suspend fun getAllCats() = remoteDataSource.getAllCats()

    override suspend fun getAllBrands() = remoteDataSource.getAllBrands()
    override suspend fun getFeedBackQuestion() = remoteDataSource.getFeedBackQuestion()
    override suspend fun addFeedBack(rating: Int, review: String?) =
        remoteDataSource.addFeedBack(rating, review)

    override suspend fun getAllPromoCodes() = remoteDataSource.getAllPromoCodes()
    override suspend fun applyPromo(promoCode: String) = remoteDataSource.applyPromo(promoCode)
    override suspend fun getAboutUs() = remoteDataSource.getAboutUs()
    override suspend fun getSupportContactNumber() = remoteDataSource.getSupportContactNumber()
    override suspend fun addAllToBasket() = remoteDataSource.addAllToBasket()
    override suspend fun deleteAddress(addressId: String) =
        remoteDataSource.deleteAddress(addressId)

    override suspend fun search(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
    ) = getSearchData(
        searchWord,
        searchLanguage,
        catIds,
        brandIds,
        highestOrLowest,
        ::SearchPagingSource
    )

    override suspend fun getStoreCategory(isProduct: Boolean) = remoteDataSource.getStoreCategory(isProduct)
    override suspend fun getStoreSubCategory(categoryId: Int) =
        remoteDataSource.getStoreSubCategory(categoryId)

    override suspend fun storeAdd(
        subCategoryId: String,
        condition: String,
        title: String,
        itemDescription: String,
        price: String,
        location: String,
        productImage: File,
        isProduct: Boolean
    ) =
        remoteDataSource.storeAdd(
            subCategoryId = createPart(subCategoryId),
            condition = createPart(subCategoryId),
            title = createPart(subCategoryId),
            itemDescription = createPart(subCategoryId),
            price = createPart(subCategoryId),
            location = createPart(subCategoryId),
            productImage = createMultiPart(
                productImage,
                if (isProduct) "ProductImage" else " Servicemage"
            ),
            isProduct
        )

    override suspend fun getStoreProductAndService(status: Int, isProduct: Boolean) =
        getStoreProductsAndServices(status, isProduct, ::StoreProductServicePagingSource)

    override suspend fun requestToDelete(customerProductId: Int, isProduct: Boolean) =
        remoteDataSource.requestToDelete(customerProductId, isProduct)

    override suspend fun verifyPhone(code: String) = remoteDataSource.verifyPhone(code)
    override suspend fun sendPhoneOtp() = remoteDataSource.sendPhoneOtp()
    override suspend fun getAllRelationships() = remoteDataSource.getAllRelationships()
    override suspend fun addNewReferral(
        name: String,
        phoneNumber: String,
        relationshipId: Int,
        email: String?
    ) = remoteDataSource.addNewReferral(name, phoneNumber, relationshipId, email)

    override suspend fun getAllReferrals() = remoteDataSource.getAllReferrals()


    private fun <T : Any> getData(sourceFactory: (RemoteDataSource) -> PagingSource<Int, T>): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(remoteDataSource) }).flow
    }

    private fun <T : Any> getDataById(
        categoryId: Int,
        sourceFactory: (RemoteDataSource, Int) -> PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(remoteDataSource, categoryId) }).flow
    }

    private fun <T : Any> getStoreProductsAndServices(
        status: Int,
        isProduct: Boolean,
        sourceFactory: (RemoteDataSource, Int, Boolean) -> PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(remoteDataSource, status, isProduct) }).flow
    }

    private fun <T : Any> getDataInsideMainCatAndCat(
        categoryId: Int,
        mainCatId: Int,
        sourceFactory: (RemoteDataSource, Int, Int) -> PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(remoteDataSource, categoryId, mainCatId) }).flow
    }

    private fun <T : Any> getCompanyAddresses(
        companyId: Int,
        governorate: String,
        sourceFactory: (RemoteDataSource, Int, String) -> PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(remoteDataSource, companyId, governorate) }).flow
    }

    private fun <T : Any> getSearchData(
        searchWord: String,
        searchLanguage: String?,
        catIds: List<Int>?,
        brandIds: List<Int>?,
        highestOrLowest: Int?,
        sourceFactory: (RemoteDataSource, String, String?, List<Int>?, List<Int>?, Int?) -> PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                sourceFactory(
                    remoteDataSource,
                    searchWord,
                    searchLanguage,
                    catIds,
                    brandIds,
                    highestOrLowest
                )
            }).flow
    }

    private fun createPart(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun createMultiPart(file: File, name: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name,
            file.name,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        )
    }
}