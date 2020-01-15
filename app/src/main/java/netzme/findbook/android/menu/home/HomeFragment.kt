package netzme.findbook.android.menu.home

/**
 * Description: Home Fragment
 * Date: 2020/14/01
 *
 * @author asywalulfikri@gmail.com
 */

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.FirebaseFirestore
import com.ms.banner.BannerConfig
import com.ms.banner.Transformer
import kotlinx.android.synthetic.main.content_search_book.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerView
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseFragment
import netzme.findbook.android.base.BasePresenter
import netzme.findbook.android.menu.adapter.BookAdapter
import netzme.findbook.android.menu.detaiBook.DetailBookActivity
import netzme.findbook.android.menu.home.adapter.CustomAdapterBanner
import netzme.findbook.android.menu.modelBook.Items
import netzme.findbook.android.menu.search.BookPresenter
import netzme.findbook.android.menu.search.SearchBookActivity

class HomeFragment : BaseFragment(),BannerContract.BannerView {


    private fun setupViews() {

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.layoutManager = layoutManager

        bannerPresenter.loadData("android", FirebaseFirestore.getInstance())
        bannerPresenter.getBook()

        ll_meat.setOnClickListener { intent("Pengetahuan") }


        ll_drinks.setOnClickListener { intent("Novel") }

        ll_seasoning.setOnClickListener { intent("Resep") }

        ll_fruits.setOnClickListener { intent("Bisnis") }

    }


    private fun intent(type: String) {

        val intent = Intent(context, SearchBookActivity::class.java)
        intent.putExtra("q", type)
        startActivity(intent)

    }

    private var bookAdapter: BookAdapter? = null
    private lateinit var bannerPresenter: HomePresenter
    private val mIndicatorSelectedResId = R.drawable.indicator_select
    private val mIndicatorUnselectedResId = R.drawable.indicator_unselect
    private var lastPosition = 0
    private val indicatorImages = ArrayList<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,
                container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerPresenter = HomePresenter()
        bannerPresenter.attachView(this)
        setupViews()

    }


    override fun onStop() {
        super.onStop()
        if (layout_banner != null && layout_banner.isPrepare && layout_banner.isStart) {
            layout_banner.stopAutoPlay()
        }
    }

    @Override
    override fun onStart() {
        super.onStart()
        if (layout_banner != null && layout_banner.isPrepare && !layout_banner.isStart) {
            layout_banner.startAutoPlay()
        }
    }


    private fun initIndicator(productArrayList: ArrayList<Items?>) {
        for (i in productArrayList.indices) {
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val customParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            customParams.leftMargin = 2
            customParams.rightMargin = 2
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId)
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId)
            }
            indicatorImages.add(imageView)
            indicator.addView(imageView,customParams)
        }
    }

    override fun showProgress() {
        progressBar.visibility =View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility =View.GONE
    }

    override fun onRefresh() {
    }

    override fun showToast(message: String?) {

        setToast(message.toString())
    }

    override fun setData(bannerArrayList: ArrayList<Items?>) {
        initIndicator(bannerArrayList)
        layout_banner.setAutoPlay(true)
            .setOffscreenPageLimit(bannerArrayList.size)
            .setPages(bannerArrayList, CustomAdapterBanner())
            .setBannerStyle(BannerConfig.NOT_INDICATOR)
            .setBannerAnimation(Transformer.Scale)
            .start()
        layout_banner.setDelayTime(5000)
        layout_banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                indicatorImages[(lastPosition + bannerArrayList.size) % bannerArrayList.size].setImageResource(mIndicatorUnselectedResId)
                indicatorImages[(position + bannerArrayList.size) % bannerArrayList.size].setImageResource(mIndicatorSelectedResId)
                lastPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        Handler().postDelayed({
            //progressBar.visibility = View.GONE
            layout_banner.visibility = View.VISIBLE
        }, 2000)
    }

    override fun setBook(items: ArrayList<Items?>) {
        bookAdapter = activity.context?.let { BookAdapter(items, it) }
        recyclerView!!.adapter = bookAdapter
        bookAdapter!!.notifyDataSetChanged()

        bookAdapter!!.actionClick(object : BookAdapter.OnItemClickListener {
            override fun actionClick(position: Int) {
                val items = items[position]
                val intent = Intent(activity.context, DetailBookActivity::class.java)
                intent.putExtra("books",items)
                startActivity(intent)
            }
        })
    }

    override fun setPresenter(presenter: BasePresenter<*>) {

    }

}
