package netzme.findbook.android.menu.modelBook

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class VolumeInfo: Serializable {

    @SerializedName("title")
    @Expose
    var title: String?=null
    @SerializedName("authors")
    @Expose
    var authors: List<String> = ArrayList()
    @SerializedName("publisher")
    @Expose
    var publisher: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("language")
    @Expose
    var language: String? = null
    @SerializedName("averageRating")
    @Expose
    var averageRating: Float?=null
    @SerializedName("ratingsCount")
    @Expose
    var ratingsCount: Long?=null
    @SerializedName("pageCount")
    @Expose
    var pageCount: Long?=null
    @SerializedName("imageLinks")
    @Expose
    var imageLinks = ImageLinks()

}
