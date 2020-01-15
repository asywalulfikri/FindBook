package netzme.findbook.android.menu.home

import com.google.firebase.firestore.FirebaseFirestore
import netzme.findbook.android.base.BaseView
import netzme.findbook.android.menu.modelBook.Items

interface BannerContract {
    interface BannerView : BaseView {

        fun showProgress()
        fun hideProgress()
        fun onRefresh()
        fun showToast(message: String?)
        fun setData(list:ArrayList<Items?>)
        fun setBook(items:ArrayList<Items?>)
    }

    interface ActionListener {
        fun loadData(keyword :String?,fireBaseFireStore: FirebaseFirestore)
        fun getBook()
        fun onRequestSuccess()
        fun showErrorResponse(message: String?)
        fun onRequestFailed(message: String?,error: Int)
    }
}