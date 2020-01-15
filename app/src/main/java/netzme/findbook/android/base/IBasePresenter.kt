package netzme.findbook.android.base

interface IBasePresenter<in V : BaseView> {

    fun attachView(view: V)
    fun detachView()
}