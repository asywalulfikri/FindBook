package netzme.findbook.android.menu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import netzme.findbook.android.R
import netzme.findbook.android.menu.modelBook.Items
import netzme.findbook.android.widget.RoundRectCornerImageView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


class BookAdapter(arrayList: ArrayList<Items?>, context: Context) :

    RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    private val context: Context
    private var bookArrayList: ArrayList<Items?>
    private var onItemClickListener: OnItemClickListener? = null


    fun updateData(arrayList: ArrayList<Items?>) {
        bookArrayList = arrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_book2, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items: Items = bookArrayList[position]!!
        holder.tvTitle.text = items.volumeInfo.title.toString().trim()

        if(items.saleInfo.listPrice.amount!=null){

            val price: Double? = items.saleInfo.listPrice.amount
            val df =
                DecimalFormat.getCurrencyInstance() as DecimalFormat
            val dfs = DecimalFormatSymbols()
            dfs.currencySymbol = ""
            dfs.monetaryDecimalSeparator = ','
            dfs.groupingSeparator = '.'
            df.decimalFormatSymbols = dfs
            val k = df.format(price)

            holder.tvPrice.text = context.getString(R.string.text_rp)+ k
        }else{
            holder.tvPrice.visibility = View.INVISIBLE
        }

        Picasso.get()
            .load(items.volumeInfo.imageLinks.thumbnail)
            .placeholder(R.drawable.noimage) // can also be a drawable
            .error(R.drawable.noimage) // will be displayed if the image cannot be loaded
            .into(holder.ivBook)


        if(items.volumeInfo.averageRating!=null){
            holder.ratingBar.rating = items.volumeInfo.averageRating!!
            holder.tvRating.text = items.volumeInfo.averageRating.toString()
        }


        holder.tvAuthor.text = items.volumeInfo.publisher
        holder.cv.setOnClickListener{
            onItemClickListener!!.actionClick(position)
        }


    }


    override fun getItemCount(): Int {
        return bookArrayList.size
    }


    fun addItem(list: ArrayList<Items?>) {
        for (i in list.indices) {
            bookArrayList.add(list[i])
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvPrice: TextView = view.findViewById(R.id.tvPrice)
        var ivBook: RoundRectCornerImageView = view.findViewById(R.id.ivBook)
        var cv: CardView = view.findViewById(R.id.cvProduct)
        var tvRating : TextView = view.findViewById(R.id.tvRating)
        var tvAuthor : TextView = view.findViewById(R.id.tvAuthor)
        var ratingBar  : AppCompatRatingBar = view.findViewById(R.id.ratingBar)

    }

    fun actionClick(actionQuestion: OnItemClickListener) {
        onItemClickListener = actionQuestion
    }

    interface OnItemClickListener {
        fun actionClick(position: Int)
    }
    init {
        bookArrayList = arrayList
        this.context = context
    }
}
