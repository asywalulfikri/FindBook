package netzme.findbook.android.menu.modelBook

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Items:Serializable {

    @SerializedName("id")
    @Expose
    var id: String?=null
    @SerializedName("selfLink")
    @Expose
    var selfLink: String? = null
    @SerializedName("volumeInfo")
    @Expose
    var volumeInfo = VolumeInfo()
    @SerializedName("saleInfo")
    @Expose
    var saleInfo = SaleInfo()
    @SerializedName("accessInfo")
    @Expose
    var accessInfo = AccessInfo()


}
