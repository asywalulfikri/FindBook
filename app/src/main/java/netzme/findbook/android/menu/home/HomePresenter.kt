package netzme.findbook.android.menu.home


import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.firebase.firestore.FirebaseFirestore
import netzme.findbook.android.base.BasePresenter
import netzme.findbook.android.menu.modelBook.Book
import netzme.findbook.android.menu.modelBook.Items
import java.util.*


class HomePresenter: BasePresenter<BannerContract.BannerView>(), BannerContract.ActionListener {
    override fun loadData(keyword:String?,fireBaseFireStore: FirebaseFirestore) {
        view?.showProgress()
        val bannerArrayList: ArrayList<Items?> = ArrayList()
        val collectionReference = fireBaseFireStore.collection("Items")
        val query = collectionReference
            .limit(10)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val items = document.toObject(Items::class.java)
                    bannerArrayList.add(items)
                }
                view?.hideProgress()
                view?.setData(bannerArrayList)
                onRequestSuccess()
            } else {
                view?.hideProgress()
                onRequestFailed(task.exception!!.message,task.hashCode())
            }
        }
    }

    override fun getBook() {
        view?.showProgress()
        AndroidNetworking.get("https://www.googleapis.com/books/v1/volumes?")
            .addQueryParameter("q","favorite")
            .addQueryParameter("key", "AIzaSyCY7qhKGmZNVofoSEnJh9wL3kJGzhj4tVU")
            .setTag(this)
            .setPriority(Priority.LOW)
            .build()
            .getAsObject(
                Book::class.java,
                object :
                    ParsedRequestListener<Book> {
                    override fun onResponse(book: Book) { // do anything with response
                        view!!.setBook(book.items)
                        view!!.hideProgress()
                        onRequestSuccess()
                    }

                    override fun onError(anError: ANError) { // handle error
                        view!!.hideProgress()
                        onRequestFailed(anError.message,anError.errorCode)
                    }
                })
    }


    override fun onRequestSuccess() {
        Log.d("success","1")
    }

    override fun showErrorResponse(message: String?) {
        Log.d("erorrr",message)
        view!!.showToast(message+"xx")
    }

    override fun onRequestFailed(message: String?, error: Int) {
        view!!.showToast(message + error)
    }


}