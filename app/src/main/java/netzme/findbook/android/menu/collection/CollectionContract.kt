package netzme.findbook.android.menu.collection

interface CollectionContract {
    interface CollectionView  {

        fun showProgress()
        fun hideProgress()
        fun onRefresh()
        fun loadData()
    }
}