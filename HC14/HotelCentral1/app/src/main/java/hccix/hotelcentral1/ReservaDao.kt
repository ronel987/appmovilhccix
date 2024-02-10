package hccix.hotelcentral1
import androidx.room.*

@Dao
interface ReservaDao {

    @Insert
    fun insert(reservaEntity:ReservaEntity):Long

    @Update
    fun update(reservaEntity:ReservaEntity)

    @Delete
    fun delete(reservaEntity:ReservaEntity)

    @Query("select * from ReservaTable where reservaId=:reservaId")
    fun findById(reservaId:Long): ReservaEntity

    @Query("select * from ReservaTable")
    fun findAll(): MutableList<ReservaEntity>
}