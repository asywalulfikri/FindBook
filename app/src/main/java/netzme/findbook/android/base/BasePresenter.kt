package netzme.findbook.android.base


open class BasePresenter<V : BaseView>() : IBasePresenter<V> {
    var view : V? = null

    override fun attachView(view: V) {
        view.setPresenter(this)
        this.view = view
    }

    override fun detachView() {
    }
}