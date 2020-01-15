package netzme.findbook.android.menu.search


import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import netzme.findbook.android.base.BasePresenter
import netzme.findbook.android.menu.modelBook.Book

class BookPresenter: BasePresenter<BookContract.BookView>(), BookContract.ActionListener {
    override fun loadData(keyword:String?,page : Int) {

        view?.showProgress()
        AndroidNetworking.get("https://www.googleapis.com/books/v1/volumes?")
            .addQueryParameter("q",keyword)
            .addQueryParameter("maxResults","10")
            .addQueryParameter("startIndex",page.toString())
            .addQueryParameter("key", "AIzaSyCY7qhKGmZNVofoSEnJh9wL3kJGzhj4tVU")
            .setTag(this)
            .setPriority(Priority.LOW)
            .build()
            .getAsObject(
                Book::class.java,
                object :
                    ParsedRequestListener<Book> {
                    override fun onResponse(book: Book) { // do anything with response
                        view!!.setData(book.items)
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
    }

    override fun onRequestFailed(message: String?, error: Int) {
        view!!.showToast(message + error)
    }


}