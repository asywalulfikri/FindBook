package netzme.findbook.android.menu.modelBook

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AccessInfo : Serializable{

    @SerializedName("webReaderLink")
    @Expose
    var webReaderLink: String?=null

}