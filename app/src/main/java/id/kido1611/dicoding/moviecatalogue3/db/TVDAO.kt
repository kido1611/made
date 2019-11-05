package id.kido1611.dicoding.moviecatalogue3.db

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.kido1611.dicoding.moviecatalogue3.model.TV

@Dao
interface TVDAO {
    @Query("select * from tv where id=:id")
    fun findById(id: Int): TV?

    @Query("select * from tv")
    fun getAllTV(): List<TV>

    @Query("select * from tv")
    fun getAllTVProviders(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTV(tv: TV)

    @Query("delete from tv where id=:id")
    fun deleteTVById(id: Int)
}