package netzme.findbook.android.menu.modelBook

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Book:Serializable {


    @SerializedName("items")
    @Expose
    var items =  ArrayList<Items?>()


}
