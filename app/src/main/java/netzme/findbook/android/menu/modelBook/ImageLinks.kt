package netzme.findbook.android.menu.modelBook

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageLinks : Serializable{

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String?=null
}