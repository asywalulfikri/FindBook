package netzme.findbook.android.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import netzme.findbook.android.menu.modelBook.Items
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BookDb(private val context: Context) :
    SQLiteOpenHelper(
        context,
        Database_name,
        null,
        version
    ) {
    var sqlite //object of type SQLiteDatabase
            : SQLiteDatabase? = null

    override fun onCreate(db: SQLiteDatabase) { //No code because we have already created the database
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) { //No code because we have already created the database
    }

    fun createDatabase() {
        createDB()
    }

    private fun createDB() {
        val dbexist = DBexists() //calling the function to check db exists or not
        if (!dbexist) //if database doesnot exist
        {
            this.readableDatabase //Create an empty file
            copyDBfromResource() //copy the database file information of assets folder to newly create file
        }
    }

    private fun copyDBfromResource() {
        val `is`: InputStream
        val os: OutputStream
        val filePath =
            Database_path + Database_name
        try {
            `is` = context.assets
                .open(Database_name) //reading purpose
            os = FileOutputStream(filePath) //writing purpose
            val buffer = ByteArray(1024)
            var length: Int
            while (`is`.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length) //writing to file
            }
            os.flush() //flush the outputstream
            `is`.close() //close the inputstream
            os.close() //close the outputstream
        } catch (e: IOException) {
            throw Error("Problem copying database file:")
        }
    }

    @Throws(SQLException::class)
    fun openDatabase() //called by onCreate method of Questions Activity
    {
        val myPath =
            Database_path + Database_name
        sqlite = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun DBexists(): Boolean //Check whether the db file exists or not
    {
        var db: SQLiteDatabase? = null
        try {
            val databasePath =
                Database_path + Database_name
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE)
            db.setLocale(Locale.getDefault())
            db.version = 1
            db.setLockingEnabled(true)
        } catch (e: SQLException) {
            Log.e("Sqlite", "Database not found")
        }
        db?.close() ///close the opened file
        return db != null
    }

    fun update(items: Items) {
        if (sqlite == null || !sqlite!!.isOpen) {
            return
        }
        val values = ContentValues()
        values.put("id", items.id)
        values.put("selfLink", items.selfLink)
        values.put("authors",items.volumeInfo.authors[0])
        values.put("title", items.volumeInfo.title)
        values.put("publisher", items.volumeInfo.publisher)
        values.put("description", items.volumeInfo.description)
        values.put("language", items.volumeInfo.language)
        values.put("averageRating", items.volumeInfo.averageRating)
        values.put("ratingsCount", items.volumeInfo.ratingsCount)
        values.put("pageCount", items.volumeInfo.pageCount)
        values.put("thumbnail", items.volumeInfo.imageLinks.thumbnail)
        values.put("buyLink", items.saleInfo.buyLink)
        values.put("saleability", items.saleInfo.saleability)
        values.put("isEbook", items.saleInfo.isEbook)
        values.put("amount", items.saleInfo.listPrice.amount)
        values.put("webReaderLink",items.accessInfo.webReaderLink)
        if (exist(items.id)) {
            sqlite!!.update(Table_name, values, "id = '" + items.id + "'", null)
        } else {
            sqlite!!.insert(Table_name, null, values)
        }
    }

    fun delete(items: Items) {
        if (sqlite == null || !sqlite!!.isOpen) {
            return
        }
        val values = ContentValues()
        values.put("id", items.id)
        if (exist(items.id)) {
            sqlite!!.delete(Table_name, "id = '" + items.id.toString() + "'", null)
        } else {
            sqlite!!.delete(Table_name, "id = '" + items.id.toString() + "'", null)
        }
    }


    fun getList(): ArrayList<Items?> {
        var book = ArrayList<Items?>()
        val sql = "SELECT * FROM Items"
        val c = sqlite!!.rawQuery(sql, null)

        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast) {
                    val items = Items()
                    items.id = c.getString(c.getColumnIndex("id"))
                    items.selfLink = c.getString(c.getColumnIndex("selfLink"))
                    items.volumeInfo.title = c.getString(c.getColumnIndex("title"))
                    items.volumeInfo.authors = listOf(c.getString(c.getColumnIndex("authors")))
                    items.volumeInfo.publisher = c.getString(c.getColumnIndex("publisher"))
                    items.volumeInfo.description = c.getString(c.getColumnIndex("description"))
                    items.volumeInfo.averageRating = c.getFloat(c.getColumnIndex("averageRating"))
                    items.volumeInfo.ratingsCount = c.getLong(c.getColumnIndex("ratingsCount"))
                    items.volumeInfo.pageCount = c.getLong(c.getColumnIndex("pageCount"))
                    items.volumeInfo.imageLinks.thumbnail = c.getString(c.getColumnIndex("thumbnail"))
                    items.saleInfo.country = c.getString(c.getColumnIndex("country"))
                    items.saleInfo.buyLink = c.getString(c.getColumnIndex("buyLink"))
                    items.saleInfo.saleability = c.getString(c.getColumnIndex("saleability"))
                    items.saleInfo.listPrice.amount = c.getDouble(c.getColumnIndex("amount"))
                    items.accessInfo.webReaderLink = c.getString(c.getColumnIndex("webReaderLink"))
                    book.add(items)
                    c.moveToNext()
                }
            }
            c.close()
        }
        return book
    }



    fun exist(id: String?): Boolean {
        var result = false
        if (sqlite == null || !sqlite!!.isOpen) {
            return false
        }
        val sql = "SELECT * FROM $Table_name WHERE id = '$id'"
        val c = sqlite!!.rawQuery(sql, null)
        if (c != null) {
            result = c.count != 0
            c.close()
        }
        return result
    }


    companion object {
        private const val Database_path = "/data/data/netzme.findbook.android/databases/"
        private const val Database_name = "Book.db" //NAME of database stored in Assets folder
        private const val Table_name = "Items"
        private const val version = 1 //version of database signifies if there is any upgradation or not
    }

}