package netzme.findbook.android.menu.modelBook

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ListPrice : Serializable{

    @SerializedName("amount")
    @Expose
    var amount: Double?=null
    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String?=null
}