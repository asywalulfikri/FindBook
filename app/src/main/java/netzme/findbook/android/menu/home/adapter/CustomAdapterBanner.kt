package netzme.findbook.android.menu.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.ms.banner.holder.BannerViewHolder
import com.squareup.picasso.Picasso
import netzme.findbook.android.R
import netzme.findbook.android.menu.modelBook.Items

class CustomAdapterBanner : BannerViewHolder<Items> {

    @SuppressLint("InflateParams")
    override fun createView(context: Context, position: Int, items: Items): View {
        val view = LayoutInflater.from(context).inflate(R.layout.banner_item, null)
        val ivBanner = view.findViewById<ImageView>(R.id.ivBanner)
        Log.d("gambarnya",items.volumeInfo.imageLinks.thumbnail+"--")
        Picasso.get()
                .load(items.volumeInfo.imageLinks.thumbnail)
                .into(ivBanner)

        return view
    }

}