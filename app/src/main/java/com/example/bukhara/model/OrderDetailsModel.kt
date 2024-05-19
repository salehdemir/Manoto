package com.example.bukhara.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetailsModel():Serializable{
    var userUid:String? = null
    var userName:String? = null
    var foodName :MutableList<String>? = null
    var foodPrice :MutableList<String>? = null
    var foodImage :MutableList<String>? = null
    var foodQuantity :MutableList<Int>? = null
    var address :String? = null
    var totalPrice :String? = null
    var phoneNum :String? = null
    var orderAccepted :Boolean =false
    var paymentReceived :Boolean =false
    var itemPushKey :String? = null
    var currentTime :Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNum = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        payName: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantity: ArrayList<Int>,
        payAddress: String,
        payPhone: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ) : this(){
        this.userUid =userId
        this.userName = payName
        this.foodName = foodItemName
        this.foodPrice = foodItemPrice
        this.foodImage = foodItemImage
        this.foodQuantity = foodItemQuantity
        this.address = payAddress
        this.phoneNum = payPhone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived

    }

   fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNum)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

     fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetailsModel> {
        override fun createFromParcel(parcel: Parcel): OrderDetailsModel {
            return OrderDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetailsModel?> {
            return arrayOfNulls(size)
        }
    }
}
