package netzme.findbook.android.menu.detaiBook

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_book.*
import kotlinx.android.synthetic.main.activity_detail_book.toolbar
import kotlinx.android.synthetic.main.content_detail_book.*
import netzme.findbook.android.R
import netzme.findbook.android.base.BaseActivity
import netzme.findbook.android.database.BookDb
import netzme.findbook.android.menu.modelBook.Items


class DetailBookActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_book)
        setToolbar(toolbar)
        var items: Items = intent.getSerializableExtra("books") as Items

        updateView(items)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateView(items: Items){

        val bookDb = BookDb(this)
        bookDb.createDatabase()
        bookDb.openDatabase()
        bookDb.writableDatabase


        Picasso.get()
            .load(items.volumeInfo.imageLinks.thumbnail)
            .into(ivBook)

        tvTitle.text = items.volumeInfo.title
        tvPublisher.text=items.volumeInfo.publisher
        if(items.volumeInfo.authors.isNotEmpty()){
            tvAuthor.text = "Authors " + items.volumeInfo.authors[0]
        }


        if(items.volumeInfo.description==null){
            tvDescription.text= "Description not available"
        }else{
            tvDescription.text = items.volumeInfo.description
        }

        if(items.volumeInfo.pageCount==null){
            tvPage.text = "Not found"
        }else{
            tvPage.text = items.volumeInfo.pageCount.toString()
        }

        if(items.volumeInfo.averageRating==null){
            tvRating.text = "No have Rating"
        }else{
            tvRating.text = items.volumeInfo.averageRating.toString()
        }

        if(items.saleInfo.listPrice.amount!=null){
            tvPrice.text = getString(R.string.text_rp) + convertPrice(items.saleInfo.listPrice.amount!!.toInt())
        }

        ivShare.setOnClickListener {
            if(items.saleInfo.buyLink==null){
                setToast("URL not available")
            }else{
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, items.saleInfo.buyLink)
                startActivity(shareIntent)
            }

        }

        btnRead.setOnClickListener {
            if(items.accessInfo.webReaderLink!=null){
                val intent = Intent(this, ReadExample::class.java)
                intent.putExtra("url",items.accessInfo.webReaderLink)
                startActivity(intent)
            }else{
                setToast("Example not available")
            }
        }

        ivBookmark.setOnClickListener {
            if (bookDb.exist(items.id)) {
                bookDb.delete(items)
                setToast("Success deleted")
            } else {
                bookDb.update(items)
                setToast("Success saved")
            }

        }



    }

}
