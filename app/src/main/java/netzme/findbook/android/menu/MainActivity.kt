package netzme.findbook.android.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseActivity
import netzme.findbook.android.menu.collection.CollectionFragment
import netzme.findbook.android.menu.home.HomeFragment
import netzme.findbook.android.menu.search.SearchBookActivity
import netzme.findbook.android.widget.ViewPagerAdapter
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener


class MainActivity : BaseActivity() {


    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null
    internal var prevMenuItem: MenuItem? = null
    var back_pressed: Long = 0

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_searchView.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val intent = Intent(this,SearchBookActivity::class.java)
                intent.putExtra("q",et_searchView.text.toString().trim())
                startActivity(intent)
            }
            false
        }

        updateView()
        if (!getShowcaseHome()) {
            showCase()
            saveShowcaseHome(true)
        }

    }


    private fun updateView(){

        bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    viewPager.currentItem = 0
                }

                R.id.nav_collection -> {
                    viewPager.currentItem = 1

                }
            }

            false
        }

        viewPager.offscreenPageLimit = 2
        viewPager.disableScroll(true)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem!!.isChecked = false
                } else {
                    bottom_navigation.menu.getItem(0).isChecked = false
                }
                bottom_navigation.menu.getItem(position).isChecked = true
                prevMenuItem = bottom_navigation.menu.getItem(position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        setupViewPager()

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun showCase(){
        builder = GuideView.Builder(this)
            .setTitle("Search Book")
            .setContentText("Find your book")
            .setGravity(Gravity.center)
            .setDismissType(DismissType.outside)
            .setTargetView(rl_searchView)
            .setGuideListener(GuideListener { view ->
                when (view.id) {
                    R.id.rl_searchView -> {
                        builder?.setTargetView(findViewById(R.id.nav_home))?.build()
                        builder?.setTitle("Menu Home")
                        builder?.setContentText("Contain category and favorite")
                    }
                    R.id.nav_home -> {
                        builder?.setTargetView(findViewById(R.id.nav_collection))?.build()
                        builder?.setTitle("Bookmark")
                        builder?.setContentText("Save your bookmark")
                    }
                    R.id.nav_collection -> return@GuideListener
                }
                mGuideView = builder?.build()
                mGuideView?.show()
            })

        mGuideView = builder?.build()
        mGuideView?.show()
        updatingForDynamicLocationViews()
    }


    private fun updatingForDynamicLocationViews() {
        bottom_navigation.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> mGuideView?.updateGuideViewLocation() }
    }

    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed() else {
            setToast("Double Click to exit")
            back_pressed = System.currentTimeMillis()
        }
    }


    private fun setupViewPager() {
        val  adapter = ViewPagerAdapter(supportFragmentManager)
        val homeFragment = HomeFragment()
        val collectionFragment = CollectionFragment()

        adapter.addFragment(homeFragment)
        adapter.addFragment(collectionFragment)
        viewPager.adapter = adapter
    }

}
