package netzme.findbook.android.menu.search

import netzme.findbook.android.base.BaseView
import netzme.findbook.android.menu.modelBook.Items

interface BookContract {
    interface BookView : BaseView {

        fun showProgress()
        fun hideProgress()
        fun showToast(message: String?)
        fun setData(list:ArrayList<Items?>)
    }

    interface ActionListener {
        fun loadData(keyword :String?,page:Int)
        fun onRequestSuccess()
        fun showErrorResponse(message: String?)
        fun onRequestFailed(message: String?,error: Int)
    }
}