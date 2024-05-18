package com.anaraya.anaraya.util

import android.annotation.SuppressLint
import android.graphics.Paint
import android.text.InputType
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.home.shared_data.AddressUiState
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

@BindingAdapter("bindImage")
fun bindImage(imageView: ImageView, url: String?) {
    if (url != null) Picasso.get().load(url).into(imageView)
}
@BindingAdapter("bindStatusOrderVisibility")
fun bindStatusOrderVisibility(constraintLayout: ConstraintLayout, boolean: Boolean) {
    if (boolean) {
        TransitionManager.beginDelayedTransition(
            constraintLayout.parent as ViewGroup,
            AutoTransition()
        )
        constraintLayout.visible()
    } else {
        TransitionManager.beginDelayedTransition(
            constraintLayout.parent as ViewGroup,
            AutoTransition()
        )
        constraintLayout.gone()
    }
}
@BindingAdapter("bindViewVisibilityGone")
fun bindViewVisibilityGone(view: View, boolean: Boolean) {
    if (boolean)
        view.visible()
    else
        view.gone()
}
@BindingAdapter("bindImageEmptyFav")
fun bindImageEmptyFav(constraintLayout: ConstraintLayout, boolean: Boolean) {
    if (boolean)
        constraintLayout.visible()
    else
        constraintLayout.gone()
}
@BindingAdapter("bindConsEmptyFav")
fun bindConsEmptyFav(view: View, boolean: Boolean) {
    if (!boolean)
        view.visible()
    else
        view.gone()
}
@BindingAdapter("bindTextNumOfBasket")
fun bindTextNumOfBasket(textView: TextView, num: Int) {
    textView.text = num.toString()
}
@BindingAdapter("bindImgOrderStatus")
fun bindImgOrderStatus(imgView: ImageView, boolean: Boolean) {
    if (boolean)
        imgView.setImageResource(R.drawable.ic_order_complete_state)
    else
        imgView.setImageResource(R.drawable.ic_order_pending_state)
}
@BindingAdapter("bindTextChangeVisibility")
fun bindTextChangeVisibility(textView: TextView, isUser: Boolean) {
    if (isUser)
        textView.visible()
    else
        textView.invisible()
}
@SuppressLint("SetTextI18n")
@BindingAdapter("bindTextAddress")
fun bindTextAddress(textView: TextView, defaultAddressUiState: AddressUiState?) {
    if (defaultAddressUiState == null) textView.text =
        textView.context.getString(R.string.please_add_addresses)
    else textView.text =
        "${defaultAddressUiState.governorate},${defaultAddressUiState.address}."
}

@BindingAdapter("bindButtonEditAddress")
fun bindButtonEditAddress(
    appCompatButton: AppCompatButton, boolean: Boolean
) {
    if (boolean) {
        appCompatButton.alpha = 1f
        appCompatButton.isEnabled = true
    } else {
        appCompatButton.alpha = 0.3f
        appCompatButton.isEnabled = false
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("bindTextAddressCheckOut")
fun bindTextAddressCheckOut(
    textView: TextView, address: String?
) {
    if (address.isNullOrEmpty()) {
        textView.text = textView.context.getString(R.string.please_add_address)
        textView.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            textView.context.getDrawable(R.drawable.ic_warning_bottom_sheet),
            null
        )
        textView.setTextColor(textView.context.getColorStateList(R.color.color_warning_text_clicked))
    } else {
        textView.text = address
        textView.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            textView.context.getDrawable(R.drawable.ic_arr_bottom_sheet),
            null
        )
        textView.setTextColor(textView.context.getColorStateList(R.color.color_text_clicked))
    }
}

//@BindingAdapter("bindPaymentVisibility")
//fun bindPaymentVisibility(
//    view: View, address: String?
//) {
//    if (address.isNullOrEmpty()) {
//        view.gone()
//    } else {
//        view.visible()
//    }
//}
@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("bindTextPaymentCheckOut")
fun bindTextPaymentCheckOut(
    textView: TextView, method: String?
) {
    if (method.isNullOrEmpty()) {
        textView.text = textView.context.getString(R.string.select_method)
        textView.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            textView.context.getDrawable(R.drawable.ic_down_warn_bottom_sheet),
            null
        )
        textView.setTextColor(textView.context.getColorStateList(R.color.color_warning_text_clicked))
    } else {
        textView.text = method
        textView.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            textView.context.getDrawable(R.drawable.ic_down_bottom_sheet),
            null
        )
        textView.setTextColor(textView.context.getColorStateList(R.color.color_text_clicked))
    }
}

//@BindingAdapter(value = ["status", "date"], requireAll = true)
//fun bindOrderDate(
//    textView: TextView, status: String, date: String
//) {
//    textView.text = "$status "
//}

@BindingAdapter("bindFav")
fun bindFav(
    imageButton: ImageButton, boolean: Boolean
) {
    if (boolean)
        imageButton.setImageResource(R.drawable.ic_fav)
    else
        imageButton.setImageResource(R.drawable.ic_no_fav)
}

@BindingAdapter(value = ["availableQty", "inBasket"], requireAll = true)
fun bindAddToCart(
    imageButton: ImageButton, availableQty: Int, inBasket: Boolean
) {
    if (availableQty > 0) {
        if (inBasket) {
            imageButton.setImageResource(R.drawable.ic_correct)
            imageButton.isEnabled = false
        } else {
            imageButton.setImageResource(R.drawable.ic_add)
            imageButton.isEnabled = true
        }
    } else {
        imageButton.invisible()
    }
}

@BindingAdapter(value = ["inBasket", "availableQty", "limitation"], requireAll = true)
fun bindButtonAddToBasket(
    button: Button, inBasket: Boolean, availableQty: Int, limitation: Int,
) {
    if (!inBasket && availableQty > 0) {
        button.visible()
        if (limitation == 0) {
            button.alpha = 1f
            button.isEnabled = true
        } else {
            button.alpha = 0.3f
            button.isEnabled = false
        }

    } else
        button.invisible()
}

@BindingAdapter(value = ["inBasketFrom", "availableQtyFrom"], requireAll = true)
fun bindButtonRemoveFromBasket(
    button: Button, inBasket: Boolean, availableQty: Int
) {
    if (inBasket && availableQty > 0)
        button.visible()
    else
        button.invisible()
}

//@BindingAdapter(value = ["inBasket", "availableQty"], requireAll = true)
//fun bindCardBasket(
//    cardView: CardView, inBasket: Boolean, availableQty: Int
//) {
//    if (inBasket && availableQty > 0)
//        cardView.visible()
//    else
//        cardView.invisible()
//}

@BindingAdapter("bindCardNoStocks")
fun bindCardNoStocks(
    cardView: CardView, qty: Int
) {
    if (qty <= 0)
        cardView.visible()
    else
        cardView.invisible()
}

@SuppressLint("StringFormatMatches")
@BindingAdapter(value = ["qtyInBasketCardBasket", "availableQtyCardBasket"], requireAll = true)
fun bindTextCardBasket(
    textView: TextView, qtyInBasket: Int, availableQty: Int
) {
    val qty = availableQty - qtyInBasket
    textView.text = textView.context.getString(R.string.only_left_in_stock, qty)
}

@SuppressLint("StringFormatMatches", "SetTextI18n")
@BindingAdapter("bindTextLimitation")
fun bindTextLimitation(
    textView: TextView, limitation: Int
) {
    if (limitation > 0) {
        textView.text =
            textView.context.getString(R.string.your_limitation_is_per_month, limitation)
        textView.visible()
    } else
        textView.invisible()
}

@BindingAdapter("bindButtonPlusQtyBasket")
fun bindButtonPlusQtyBasket(
    imageButton: ImageButton, boolean: Boolean
) {
    if (boolean) {
        imageButton.isEnabled = true
        imageButton.alpha = 1f
    } else {
        imageButton.isEnabled = false
        imageButton.alpha = 0.5f
    }
}

@BindingAdapter("bindButtonMinusQtyBasket")
fun bindButtonMinusQtyBasket(
    imageButton: ImageButton, boolean: Boolean
) {
    if (boolean) {
        imageButton.isEnabled = true
        imageButton.alpha = 1f
    } else {
        imageButton.isEnabled = false
        imageButton.alpha = 0.5f
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTextEditInfoType")
fun bindTextEditInfoType(
    textView: TextView, type: String
) {
    when (type) {
        "name" -> textView.text = textView.context.getString(R.string.please_enter_your_name)
        "email" -> textView.text = textView.context.getString(R.string.please_enter_your_email)
        else -> textView.text = textView.context.getString(R.string.please_enter_your_mobile)
    }
}

@BindingAdapter("bindInputType")
fun bindInputType(
    editText: EditText, type: String
) {
    when (type) {
        "phone" -> editText.inputType = InputType.TYPE_CLASS_PHONE
        "email" -> editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        else -> editText.inputType = InputType.TYPE_CLASS_TEXT
    }
}


@BindingAdapter("bindImageFeedback")
fun bindImageFeedback(imageView: ImageView, boolean: Boolean) {
    if (boolean) imageView.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).start()
    else imageView.animate().setDuration(500).scaleX(1.0f).scaleY(1.0f).start()
}

@BindingAdapter("bindRecyclerLayout")
fun bindRecyclerLayout(recyclerView: RecyclerView, horizontal: Boolean) {
    if (horizontal) {
        val layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
    } else {
        val layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
    }
}

@BindingAdapter("bindConsNoInternet")
fun bindConsNoInternet(constraintLayout: ConstraintLayout, error: String?) {
    if (!error.isNullOrEmpty()) {
        if (error == "No Internet") constraintLayout.visible()
        else constraintLayout.gone()
    } else constraintLayout.gone()

}

@BindingAdapter("bindConsInternet")
fun bindConsInternet(constraintLayout: ConstraintLayout, error: String?) {
    if (!error.isNullOrEmpty()) {
        if (error == "No Internet") constraintLayout.gone()
        else constraintLayout.visible()
    } else constraintLayout.visible()
}

@BindingAdapter("bindConsCart")
fun bindConsCart(constraintLayout: ConstraintLayout, showEmpty: Boolean) {
    if (!showEmpty) constraintLayout.visible()
    else constraintLayout.gone()
}

@BindingAdapter("bindConsEmptyCart")
fun bindConsEmptyCart(constraintLayout: ConstraintLayout, showEmpty: Boolean) {
    if (showEmpty) constraintLayout.visible()
    else constraintLayout.gone()
}

@BindingAdapter("bindRecyclerGridLayout")
fun bindRecyclerGridLayout(recyclerView: RecyclerView, spanCount: Int) {
    val layoutManager = GridLayoutManager(recyclerView.context, spanCount)
    recyclerView.layoutManager = layoutManager
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTxtDiscount")
fun bindTxtDiscount(textView: TextView, discount: Double) {
    if (discount > 0.0) {
        textView.text = "Discount $discount %"
    }

}

@BindingAdapter("bindCardDiscount")
fun bindCardDiscount(cardView: CardView, discount: Double) {
    if (discount > 0.0) {
        cardView.visible()
    } else cardView.invisible()

}

@BindingAdapter("bindConsApartment")
fun bindConsApartment(view: View, boolean: Boolean) {
    if (boolean)
        view.visible()
    else
        view.gone()
}

@BindingAdapter("bindConsOffice")
fun bindConsOffice(view: View, boolean: Boolean) {
    if (boolean)
        view.visible()
    else
        view.gone()
}

@BindingAdapter("bindConsLayout")
fun bindConsLayout(view: View, boolean: Boolean) {
    if (boolean)
        view.visible()
    else
        view.gone()
}

@BindingAdapter("bindBtnApartment")
fun bindBtnApartment(button: Button, selected: Boolean) {
    if (selected) {
        button.setBackgroundResource(R.drawable.shape_button_add_2)
        button.setTextColor(button.context.getColor(R.color.white))
    } else {
        button.setBackgroundResource(R.drawable.shape_button_apartment)
        button.setTextColor(button.context.getColor(R.color.colorPrimary))
    }
}

@BindingAdapter("bindBtnOffice")
fun bindBtnOffice(button: Button, selected: Boolean) {
    if (selected) {
        button.setBackgroundResource(R.drawable.shape_button_add_2)
        button.setTextColor(button.context.getColor(R.color.white))
    } else {
        button.setBackgroundResource(R.drawable.shape_button_apartment)
        button.setTextColor(button.context.getColor(R.color.colorPrimary))
    }
}

@BindingAdapter("bindVisibilityConsHomeTrending")
fun bindVisibilityConsHomeTrending(constraintLayout: ConstraintLayout, visibility: Boolean) {
    if (visibility)
        constraintLayout.visible()
    else
        constraintLayout.gone()
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindTxtPrice")
fun bindTxtPrice(textView: TextView, price: Double) {
    val lastNum = String.format("%.2f", price).toDouble()
    textView.text = textView.context.getString(R.string.l_e, lastNum)
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindNumOfItems")
fun bindNumOfItems(textView: TextView, num: Int) {
    if(num > 1)
        textView.text = "$num ${textView.context.getString(R.string.items)}"
    else
        textView.text = "$num ${textView.context.getString(R.string.item)}"
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter(value = ["priceBefore", "discountBefore"], requireAll = false)
fun bindTxtBeforePrice(textView: TextView, priceBefore: Double, discountBefore: Double) {
    if (discountBefore > 0) {
        textView.visible()
        val lastNum = String.format("%.2f", priceBefore).toDouble()
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        textView.text = textView.context.getString(R.string.l_e, lastNum)
    } else {
        textView.gone()
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTxtDiscountPrice")
fun bindTxtDiscountPrice(textView: TextView, discount: Double) {
//    if (discount > 0.0) {
//        textView.visible()
//    } else textView.gone()
    //val num = String.format(textView.context.getString(R.string.discount_round),discount).toDouble()
    textView.text = textView.context.getString(R.string.discount_format, discount.toInt())
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindCardDiscountPrice")
fun bindCardDiscountPrice(cardView: CardView, discount: Double) {
    if (discount > 0.0) {
        cardView.visible()
    } else cardView.invisible()
}

@BindingAdapter("bindTextDiscountOff")
fun bindTextDiscountOff(textView: TextView, discount: Int) {
    textView.text = textView.context.getString(R.string.discount_off, discount)
}

/*@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindLEText")
fun bindLEText(textView: TextView, price: Double) {
    val lastNum = String.format("%.2f", price).toDouble()
    textView.text = textView.context.getString(R.string.l_e, lastNum)
}*/

@BindingAdapter("bindSwitch")
fun bindSwitch(switchCompat: SwitchCompat, default: Boolean) {
    switchCompat.isChecked = default
    switchCompat.isEnabled = !default
}

@BindingAdapter("bindRadio")
fun bindRadio(radioButton: RadioButton, default: Boolean) {
    radioButton.isChecked = default
    radioButton.isEnabled = !default
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindSwitchText")
fun bindSwitchText(textView: TextView, default: Boolean) {
    if (default) {
        textView.text = "Default"
    } else {
        textView.text = "Make Default"
    }
}

//sign in visibility
@BindingAdapter("bindTextNationalIdSignInVisibility")
fun bindTextNationalIdSignInVisibility(textView: TextView, state: Int) {
    if (state != 1) textView.visible()
    else textView.gone()
}

@BindingAdapter("bindEditTextNationalIdSignInVisibility")
fun bindEditTextNationalIdSignInVisibility(editText: TextInputLayout, state: Int) {
    if (state != 1) editText.visible()
    else editText.gone()
}

@BindingAdapter("bindTextPasswordSignInVisibility")
fun bindTextPasswordSignInVisibility(textView: TextView, state: Int) {
    if (state == 3) textView.visible()
    else textView.gone()
}

@BindingAdapter("bindEditTextPasswordSignInVisibility")
fun bindEditTextPasswordSignInVisibility(editText: TextInputLayout, state: Int) {
    if (state == 3) editText.visible()
    else editText.gone()
}

@BindingAdapter("bindTextInputLayoutEmptyError")
fun bindTextInputLayoutEmptyError(textInputLayout: TextInputLayout, boolean: Boolean) {
    if (boolean) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = textInputLayout.context.getString(R.string.field_require)

    } else
        textInputLayout.isErrorEnabled = false
}

@BindingAdapter("bindChkTermsAndConditionError")
fun bindChkTermsAndConditionError(materialCheckBox: MaterialCheckBox, boolean: Boolean) {
    if (boolean)
        materialCheckBox.setTextColor(materialCheckBox.context.getColor(R.color.red))
    else
        materialCheckBox.setTextColor(materialCheckBox.context.getColor(R.color.colorGreyText))
}

@BindingAdapter("bindTextInputLayoutPassEmailMsgError")
fun bindTextInputLayoutPassEmailMsgError(textInputLayout: TextInputLayout, msg: String) {
    if (msg.isNotEmpty()) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = msg
    } else
        textInputLayout.isErrorEnabled = false
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTextReferralsItem")
fun bindTextReferralsItem(textView: TextView, text: String) {
    textView.text = textView.context.getString(R.string.referrals_sent_to, text)
}

//sign up visibility
@BindingAdapter("bindTextNationalIdSignUpVisibility")
fun bindTextNationalIdSignUpVisibility(textView: TextView, state: Int) {
    if (state == 1) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindEditTextNationalIdSignUpVisibility")
fun bindEditTextNationalIdSignUpVisibility(editText: TextInputLayout, state: Int) {
    if (state == 1) editText.visible()
    else editText.invisible()
}

@BindingAdapter("bindTextRayaIdSignUpVisibility")
fun bindTextRayaIdSignUpVisibility(textView: TextView, state: Int) {
    if (state == 1) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindEditTextRayaIdSignUpVisibility")
fun bindEditTextRayaIdSignUpVisibility(editText: TextInputLayout, state: Int) {
    if (state == 1) editText.visible()
    else editText.invisible()
}


@BindingAdapter("bindSignUp2")
fun bindSignUp2(view: View, state: Int) {
    if (state == 2) view.visible()
    else view.gone()
}

//reset password visibility
@BindingAdapter("bindTextEnterYourRayaResetVisibility")
fun bindTextEnterYourRayaResetVisibility(textView: TextView, state: Int) {
    if (state == 1 || state == 2) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindTextNationalIdResetVisibility")
fun bindTextNationalIdResetVisibility(textView: TextView, state: Int) {
    if (state == 1 || state == 2) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindEditTextNationalIdResetVisibility")
fun bindEditTextNationalIdResetVisibility(editText: TextInputLayout, state: Int) {
    if (state == 1 || state == 2) editText.visible()
    else editText.invisible()
}

@BindingAdapter("bindTextRayaIdResetVisibility")
fun bindTextRayaIdResetVisibility(textView: TextView, state: Int) {
    if (state == 1 || state == 2) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindEditTextRayaIdResetVisibility")
fun bindEditTextRayaIdResetVisibility(editText: TextInputLayout, state: Int) {
    if (state == 1 || state == 2) editText.visible()
    else editText.invisible()
}

@BindingAdapter("bindTextVerificationResetVisibility")
fun bindTextVerificationResetVisibility(textView: TextView, state: Int) {
    if (state == 2) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindEditTextVerificationResetVisibility")
fun bindEditTextVerificationResetVisibility(editText: TextInputLayout, state: Int) {
    if (state == 2) editText.visible()
    else {
        //editText.setText("")
        editText.invisible()
    }
}


@BindingAdapter("bindTextEnterPassResetVisibility")
fun bindTextEnterPassResetVisibility(textView: TextView, state: Int) {
    if (state == 3) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindTextNewPassResetVisibility")
fun bindTextNewPassResetVisibility(textView: TextView, state: Int) {
    if (state == 3) textView.visible()
    else textView.invisible()
}

@BindingAdapter("bindEditTextNewPassResetVisibility")
fun bindEditTextNewPassResetVisibility(editText: TextInputLayout, state: Int) {
    if (state == 3) editText.visible()
    else editText.invisible()
}


@BindingAdapter("bindProgress")
fun bindProgress(progressBar: ProgressBar, boolean: Boolean) {
    if (boolean) progressBar.visible()
    else progressBar.gone()
}

@BindingAdapter(value = ["userActionDelete", "stateDelete"], requireAll = true)
fun bindRequestToDelete(view: View, userActionDelete:Int,stateDelete: Int) {
    if (stateDelete in 1..2){
        if(userActionDelete == 3)
            view.gone()
        else
            view.visible()
    }
    else
        view.gone()
}

@BindingAdapter(value = ["userActionSale", "stateSale"], requireAll = true)
fun bindRequestToProceedSale(view: View, userActionSale:Int,stateSale: Int) {
    if (stateSale == 2){
//        if(userActionSale == 3)
//            view.gone()
//        else
//            view.visible()
    }
    else
        view.gone()
}

@BindingAdapter(value = ["userActionEdit", "stateEdit"], requireAll = true)
fun bindRequestToProceedEdit(view: View, userActionEdit:Int,stateEdit: Int) {
    if (stateEdit == 2){
        if(userActionEdit == 2)
            view.gone()
        else
            view.visible()
    }
    else
        view.gone()
}
