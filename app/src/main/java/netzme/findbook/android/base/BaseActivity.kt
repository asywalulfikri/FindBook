package netzme.findbook.android.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import netzme.findbook.android.BuildConfig
import netzme.findbook.android.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

open class BaseActivity : AppCompatActivity(){

    private var mSharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        )
    }
    @SuppressLint("ResourceAsColor", "PrivateResource")
    fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        val upArrow = resources.getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp)
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setTitleTextColor(R.color.white)
        toolbar.setTitleTextAppearance(this, R.style.textSizeToolbar)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    open fun convertPrice(price: Int): String? {
        val fmt = DecimalFormat()
        val fmts = DecimalFormatSymbols()
        fmts.groupingSeparator = '.'
        fmt.groupingSize = 3
        fmt.isGroupingUsed = true
        fmt.decimalFormatSymbols = fmts
        return fmt.format(price.toLong())
    }


    open fun hideKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v: View = activity.currentFocus!!
        if (v != null) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    open fun saveShowcaseHome(showcaseHome: Boolean) {
        val editor: SharedPreferences.Editor = mSharedPref!!.edit()
        editor.putBoolean("showcase", showcaseHome)
        editor.commit()
    }

    open fun getShowcaseHome(): Boolean {
        return mSharedPref!!.getBoolean("showcase", false)
    }

    val activity: BaseActivity
        get() = this


    fun  setToast(message : String){
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show()
    }
}