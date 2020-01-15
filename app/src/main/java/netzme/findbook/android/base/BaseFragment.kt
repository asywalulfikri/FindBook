package netzme.findbook.android.base

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(){


    val activity: BaseFragment
        get() = this

    open fun setToast(tipe: String?) {
        Toast.makeText(getActivity(), tipe, Toast.LENGTH_SHORT).show()
    }

}