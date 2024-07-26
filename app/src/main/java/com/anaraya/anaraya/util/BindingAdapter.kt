package com.anaraya.anaraya.util

import android.annotation.SuppressLint
import android.text.Html
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
import android.widget.RadioGroup
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
import com.anaraya.anaraya.screens.shared_data.AddressUiState
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import java.util.Locale

@BindingAdapter("bindImage")
fun bindImage(imageView: ImageView, url: String?) {
    if (url != null) Picasso.get().load(url).into(imageView)
}

@BindingAdapter("bindTextCondition")
fun bindTextCondition(textView: TextView, num: Int) {
    if (num == 1)
        textView.text = textView.context.getString(R.string.neww)
    else
        textView.text = textView.context.getString(R.string.used)

}

@BindingAdapter("bindButtonRequestToBuy")
fun bindButtonRequestToBuy(appCompatButton: AppCompatButton, status: Int) {
    if (status != 0) {
        appCompatButton.setBackgroundResource(R.drawable.shape_button_request_to_buy_sent)
        appCompatButton.setTextColor(appCompatButton.context.getColor(R.color.white))
        appCompatButton.text = appCompatButton.context.getString(R.string.request_to_buy)
        appCompatButton.isEnabled = false
    } else {
        appCompatButton.setBackgroundResource(R.drawable.shape_button_add_2)
        appCompatButton.setTextColor(appCompatButton.context.getColor(R.color.white))
        appCompatButton.text = appCompatButton.context.getString(R.string.request_to_buy)
        appCompatButton.isEnabled = true
    }
}
@BindingAdapter("bindButtonRequest")
fun bindButtonRequest(appCompatButton: AppCompatButton, status: Int) {
    if (status != 0) {
        appCompatButton.setBackgroundResource(R.drawable.shape_button_request_to_buy_sent)
        appCompatButton.setTextColor(appCompatButton.context.getColor(R.color.white))
        appCompatButton.text = appCompatButton.context.getString(R.string.request)
        appCompatButton.isEnabled = false
    } else {
        appCompatButton.setBackgroundResource(R.drawable.shape_button_add_2)
        appCompatButton.setTextColor(appCompatButton.context.getColor(R.color.white))
        appCompatButton.text = appCompatButton.context.getString(R.string.request)
        appCompatButton.isEnabled = true
    }
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
@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["from", "to"], requireAll = true)
fun bindTextPeriodFromTo(textView: TextView, from: String?,to:String?) {
    textView.text = textView.context.getString(R.string.this_service_is_available_from_to, from, to)
}
@SuppressLint("SetTextI18n")
@BindingAdapter("bindTextPeriodFrom")
fun bindTextPeriodFrom(textView: TextView, from: String?) {
    textView.text = textView.context.getString(R.string.rent_from, from)
}@SuppressLint("SetTextI18n")
@BindingAdapter("bindTextPeriodTo")
fun bindTextPeriodTo(textView: TextView, to: String?) {
    textView.text = textView.context.getString(R.string.rent_to, to)
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindTextNumOfPurchase")
fun bindTextNumOfPurchase(textView: TextView, num: Int) {
    textView.text = textView.context.getString(R.string.you_have_a_purchase_request_num, num)
}
@BindingAdapter("bindGender")
fun bindGender(radioGroup: RadioGroup, status: Int) {
    if (status > 0)
        radioGroup.check(if (status == 1) R.id.radioMaleInfo else R.id.radioFemaleInfo)
}

@BindingAdapter("bindGenderEditInfo")
fun bindGenderEditInfo(radioGroup: RadioGroup, status: Int) {
    if (status > 0)
        radioGroup.check(if (status == 1) R.id.radioMaleEditInfo else R.id.radioFemaleEditInfo)
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
    if (num > 0) {
        textView.visible()
        textView.text = num.toString()
    } else {
        textView.gone()
    }
}

@BindingAdapter("bindImgOrderStatus")
fun bindImgOrderStatus(imgView: ImageView, boolean: Boolean) {
    if (boolean)
        imgView.setImageResource(R.drawable.ic_order_complete_state)
    else
        imgView.setImageResource(R.drawable.ic_order_pending_state)
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindTextPoints")
fun bindTextPoints(textView: TextView, points: Int = 0) {
    textView.text = textView.context.getString(R.string.points_num, points)
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindTextPoints2")
fun bindTextPoints2(textView: TextView, points: Int = 0) {
    textView.text = textView.context.getString(R.string.points_num2, points)
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
    appCompatButton: AppCompatButton, boolean: Boolean,
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
    textView: TextView, address: String?,
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
    textView: TextView, method: String?,
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
    imageButton: ImageButton, boolean: Boolean,
) {
    if (boolean)
        imageButton.setImageResource(R.drawable.ic_fav)
    else
        imageButton.setImageResource(R.drawable.ic_no_fav)
}

@BindingAdapter(value = ["availableQty", "inBasket"], requireAll = true)
fun bindAddToCart(
    imageButton: ImageButton, availableQty: Int, inBasket: Boolean,
) {
    if (availableQty > 0) {
        imageButton.visible()
        if (inBasket) {
            imageButton.setImageResource(R.drawable.ic_correct)
            imageButton.isEnabled = false
            imageButton.alpha = 0.5f
        } else {
            imageButton.setImageResource(R.drawable.ic_add)
            imageButton.isEnabled = true
            imageButton.alpha = 1f
        }
    } else {
        imageButton.invisible()
    }
}

@BindingAdapter("bindTextUnavailable")
fun bindTextUnavailable(
    textView: TextView, availableQty: Int,
) {
    if (availableQty > 0) {
        textView.gone()
    } else {
        textView.visible()
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
    button: Button, inBasket: Boolean, availableQty: Int,
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
    view: View, qty: Int,
) {
    if (qty <= 0)
        view.visible()
    else
        view.invisible()
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindNotifyText")
fun bindNotifyText(
    appCompatButton: AppCompatButton, inList: Boolean,
) {
    if (inList)
        appCompatButton.text =
            appCompatButton.context.getString(R.string.remove_from_notification_list)
    else
        appCompatButton.text = appCompatButton.context.getString(R.string.notify_me_when_available)
}

@SuppressLint("StringFormatMatches")
@BindingAdapter(value = ["qtyInBasketCardBasket", "availableQtyCardBasket"], requireAll = true)
fun bindTextCardBasket(
    textView: TextView, qtyInBasket: Int, availableQty: Int,
) {
    val qty = availableQty - qtyInBasket
    if (qty > 0) {
        textView.text = textView.context.getString(R.string.only_left_in_stock, qty)
        textView.setTextColor(textView.context.getColor(R.color.colorGreyText))
    } else {
        textView.text = textView.context.getString(R.string.out_of_stock)
        textView.setTextColor(textView.context.getColor(R.color.red))
    }
}

@SuppressLint("StringFormatMatches", "SetTextI18n")
@BindingAdapter("bindTextLimitation")
fun bindTextLimitation(
    textView: TextView, limitation: Int,
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
    imageButton: ImageButton, boolean: Boolean,
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
    imageButton: ImageButton, boolean: Boolean,
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
    textView: TextView, type: String,
) {
    when (type) {
        "name" -> textView.text = textView.context.getString(R.string.please_enter_your_name)
        "email" -> textView.text = textView.context.getString(R.string.please_enter_your_email)
        else -> textView.text = textView.context.getString(R.string.please_enter_your_mobile)
    }
}

@BindingAdapter("bindInputType")
fun bindInputType(
    editText: EditText, type: String,
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

@BindingAdapter(value = ["status", "userAction", "isListed"], requireAll = false)
fun bindTextItemStoreStatus(text: TextView, status: Int, userAction: Int, isListed: Boolean) {
    if (isListed) {
        text.visible()
        when (status) {
            1 -> {
                when (userAction) {
                    1 -> {
                        text.text = text.context.getString(R.string.add_request_pending)
                        text.setTextColor(text.context.getColor(R.color.colorPrimary))
                    }

                    2 -> {
                        text.text = text.context.getString(R.string.edit_request_pending)
                        text.setTextColor(text.context.getColor(R.color.colorPrimary))
                    }
                    3 -> {
                        text.text = text.context.getString(R.string.delete_request_pending)
                        text.setTextColor(text.context.getColor(R.color.red))
                    }
                }
            }

            2 -> {
                text.text = text.context.getString(R.string.accepted)
                text.setTextColor(text.context.getColor(R.color.green))
            }

            3 -> {
                text.text = text.context.getString(R.string.rejected)
                text.setTextColor(text.context.getColor(R.color.red))
            }
        }
    } else {
        text.gone()
    }
}

@SuppressLint("SetTextI18n", "StringFormatMatches", "DefaultLocale")
@BindingAdapter(value = ["bindTxtPrice", "isPoints"], requireAll = false)
fun bindTxtPrice(textView: TextView, price: Double, isPoints: Boolean = false) {
    val lastNum = String.format(Locale.US, "%.2f", price).toDouble()
    if (isPoints)
        textView.text = textView.context.getString(R.string.points_price, lastNum)
    else
        textView.text = textView.context.getString(R.string.l_e, lastNum)
}

@SuppressLint("SetTextI18n", "StringFormatMatches")
@BindingAdapter("bindNumOfItems")
fun bindNumOfItems(textView: TextView, num: Int) {
    if (num > 1)
        textView.text = "$num ${textView.context.getString(R.string.items)}"
    else
        textView.text = "$num ${textView.context.getString(R.string.item)}"
}

@SuppressLint("SetTextI18n", "StringFormatMatches", "DefaultLocale")
@BindingAdapter(value = ["priceBefore", "discountBefore"], requireAll = false)
fun bindTxtBeforePrice(textView: TextView, priceBefore: Double, discountBefore: Double) {
    if (discountBefore > 0) {
        textView.visible()
        val lastNum = String.format(Locale.US, "%.2f", priceBefore).toDouble()
        val formattedString = textView.context.getString(R.string.l_e_before, lastNum)
//        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        textView.text = Html.fromHtml(formattedString, Html.FROM_HTML_MODE_LEGACY)
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
        textView.text = textView.context.getString(R.string.defaultt)
    } else {
        textView.text = textView.context.getString(R.string.make_default)
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

@BindingAdapter("bindPassSignInFamilyVisibility")
fun bindPassSignInFamilyVisibility(view: View, state: Int) {
    if (state == 2) view.visible()
    else view.gone()
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

@BindingAdapter("bindTextInputLayoutEmailMsgError")
fun bindTextInputLayoutEmailMsgError(textInputLayout: TextInputLayout, msg: String) {
    if (msg.isNotEmpty()) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = textInputLayout.context.getString(R.string.email_invalid_format_msg)
    } else
        textInputLayout.isErrorEnabled = false
}

@BindingAdapter("bindTextInputLayoutPassMsgError")
fun bindTextInputLayoutPassMsgError(textInputLayout: TextInputLayout, number: Int) {
    if (number > 0) {
        textInputLayout.isErrorEnabled = true
        when (number) {
            1 -> {
                textInputLayout.error =
                    textInputLayout.context.getString(R.string.password_format_counter_msg)
            }

            2 -> {
                textInputLayout.error =
                    textInputLayout.context.getString(R.string.password_format_upper_msg)
            }

            3 -> {
                textInputLayout.error =
                    textInputLayout.context.getString(R.string.password_format_lower_msg)
            }

            4 -> {
                textInputLayout.error =
                    textInputLayout.context.getString(R.string.password_format_digit_msg)
            }

            5 -> {
                textInputLayout.error =
                    textInputLayout.context.getString(R.string.password_format_special_msg)
            }
        }
    } else
        textInputLayout.isErrorEnabled = false
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTextReferralsItem")
fun bindTextReferralsItem(textView: TextView, text: String) {
    textView.text = textView.context.getString(R.string.referrals_sent_to, text)
}

//sign up visibility
@BindingAdapter("bindSignUp1")
fun bindSignUp1(view: View, state: Int) {
    if (state == 1) view.visible()
    else view.invisible()
}

@BindingAdapter("bindSignUp2")
fun bindSignUp2(view: View, state: Int) {
    if (state == 2) view.visible()
    else view.gone()
}

//family
@BindingAdapter("bindSignUpFamily1")
fun bindSignUpFamily1(view: View, state: Int) {
    if (state in 1..3) view.visible()
    else view.invisible()
}

@BindingAdapter("bindSignUpFamily2")
fun bindSignUpFamily2(view: View, state: Int) {
    if (state in 2..3) view.visible()
    else view.invisible()
}

@BindingAdapter("bindSignUpFamily3")
fun bindSignUpFamily3(view: View, state: Int) {
    if (state == 3) view.visible()
    else view.invisible()
}

@BindingAdapter("bindSignUpFamily4")
fun bindSignUpFamily4(view: View, state: Int) {
    if (state == 4) view.visible()
    else view.invisible()
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

//reset family
@BindingAdapter("bindResetPassVisibility2")
fun bindResetPassVisibility2(view: View, state: Int) {
    if (state == 2) view.visible()
    else view.invisible()
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
fun bindRequestToDelete(view: View, userActionDelete: Int, stateDelete: Int) {
    if (stateDelete in 1..2) {
        if (userActionDelete == 3)
            view.gone()
        else
            view.visible()
    } else
        view.gone()
}

@BindingAdapter(value = ["userActionSale", "stateSale"], requireAll = true)
fun bindRequestToProceedSale(view: View, userActionSale: Int, stateSale: Int) {
    if (stateSale == 2) {
//        if(userActionSale == 3)
//            view.gone()
//        else
//            view.visible()
    } else
        view.gone()
}

@BindingAdapter(value = ["userActionEdit", "stateEdit"], requireAll = true)
fun bindRequestToProceedEdit(view: View, userActionEdit: Int, stateEdit: Int) {
    if (stateEdit == 2) {
        if (userActionEdit == 2)
            view.gone()
        else
            view.visible()
    } else
        view.gone()
}
