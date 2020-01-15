package netzme.findbook.android.menu.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search_book.et_searchView
import kotlinx.android.synthetic.main.activity_search_book.toolbar
import kotlinx.android.synthetic.main.content_search_book.*
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseActivity
import netzme.findbook.android.base.BasePresenter
import netzme.findbook.android.menu.adapter.BookAdapter
import netzme.findbook.android.menu.detaiBook.DetailBookActivity
import netzme.findbook.android.menu.modelBook.Items

class SearchBookActivity : BaseActivity(),BookContract.BookView {


    private lateinit var bookPresenter: BookPresenter
    private var bookAdapter: BookAdapter? = null
    private var  keyword:String? =null
    private var offset = 0
    private var stopload = false
    private var firstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_book)

        setToolbar(toolbar)
        bookPresenter = BookPresenter()
        bookPresenter.attachView(this)



        setupView()
        keyword = intent.getStringExtra("q")
        bookPresenter.loadData(keyword,offset)


        et_searchView.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                offset = 0
                firstLoad = true
                bookPresenter.loadData(et_searchView.text.toString(),offset)
            }
            false
        }

        swipeLayout.setOnRefreshListener {
            offset =0
            firstLoad = true
            bookPresenter.loadData(keyword,0)
        }
    }

    private fun setupView(){
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = mLayoutManager
    }


    override fun showProgress() {
        if(firstLoad){
            swipeLayout.isRefreshing = true
        }
        progressbar.visibility= View.GONE
    }

    override fun hideProgress() {
        progressbar.visibility= View.GONE
        swipeLayout.isRefreshing = false
    }

    override fun showToast(message :String?) {
       setToast(message.toString())
    }

    override fun setData(items: ArrayList<Items?>) {

        if (items.size <= 9) {
            rlLoadMore.visibility = View.GONE
            stopload = true

        } else {
            stopload = false
            rlLoadMore.visibility = View.VISIBLE
        }
        if(firstLoad){
            updateData(items)
        }else{
            getMoreItems(items)
        }
    }


    private fun getMoreItems(items: ArrayList<Items?>) {
        offset += 10
        bookAdapter!!.addItem(items)
        recyclerView.adapter = bookAdapter
        bookAdapter!!.notifyDataSetChanged()
    }

    private fun updateData(items: ArrayList<Items?>){
        firstLoad = false
        offset += 10
        bookAdapter = BookAdapter(items,this)
        recyclerView!!.adapter = bookAdapter
        bookAdapter!!.notifyDataSetChanged()

        bookAdapter!!.actionClick(object : BookAdapter.OnItemClickListener {
            override fun actionClick(position: Int) {
                val items = items[position]
                val intent = Intent(activity,DetailBookActivity::class.java)
                intent.putExtra("books",items)
                startActivity(intent)
            }
        })


        automaticLoadMore()
    }

    private fun automaticLoadMore() {
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                Handler().postDelayed({
                    if (!stopload) {
                        bookPresenter.loadData(keyword,offset)
                    }
                }, 500)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun setPresenter(presenter: BasePresenter<*>) {

    }
}
