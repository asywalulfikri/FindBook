package netzme.findbook.android.menu.collection


/**
 * Description: Collection Fragment
 * Date: 2020/01/03
 *
 * @author asywalulfikri@gmail.com
 */

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_collection.*
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseFragment
import netzme.findbook.android.base.BasePresenter
import netzme.findbook.android.database.BookDb
import netzme.findbook.android.menu.adapter.BookAdapter
import netzme.findbook.android.menu.detaiBook.DetailBookActivity


class CollectionFragment : BaseFragment(),CollectionContract.CollectionView {

    private var bookAdapter: BookAdapter? = null
    private  var bookDb : BookDb? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_collection,
                container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()


    }


    @Override
    override fun onStart() {
        super.onStart()

    }



    private fun setupView(){
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity.context, 3)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = mLayoutManager

        swipeLayout.setOnRefreshListener { onRefresh()}
    }

    private fun getData(){
        showProgress()
        bookDb = activity.context?.let { BookDb(it) }
        bookDb!!.createDatabase()
        bookDb!!.openDatabase()
        bookDb!!.writableDatabase
        loadData()

    }


    override fun showProgress() {
        swipeLayout.isRefreshing = true
        progressbar.visibility= View.VISIBLE
    }

    override fun hideProgress() {
        progressbar.visibility= View.GONE
        swipeLayout.isRefreshing = false
    }

    override fun onResume(){
        super.onResume()
        getData()
    }

    override fun onRefresh() {

       loadData()

    }

    override fun loadData() {
        hideProgress()
        bookAdapter = activity.context?.let { BookAdapter(bookDb!!.getList(), it) }
        recyclerView!!.adapter = bookAdapter

        if(bookAdapter!!.itemCount>0){
            tvNoData.visibility = View.GONE
        }else{
            tvNoData.visibility = View.VISIBLE
        }
        bookAdapter!!.notifyDataSetChanged()
        bookAdapter!!.actionClick(object : BookAdapter.OnItemClickListener {
            override fun actionClick(position: Int) {
                val items = bookDb!!.getList()[position]
                val intent = Intent(activity.context, DetailBookActivity::class.java)
                intent.putExtra("books",items)
                startActivity(intent)
            }
        })
    }

}
