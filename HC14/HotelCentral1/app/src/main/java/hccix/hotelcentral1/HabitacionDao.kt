package hccix.hotelcentral1

import androidx.room.*

@Dao
interface HabitacionDao {
    @Insert
    fun insert(habitaEntity:HabitacionEntity):Long

    @Update
    fun update(habitaEntity:HabitacionEntity)

    @Delete
    fun delete(habitaEntity:HabitacionEntity)

    @Query("select * from HabitaTable where habitaId=:habitaId")
    fun findById(habitaId:Long): HabitacionEntity

    @Query("select * from HabitaTable")
    fun findAll(): MutableList<HabitacionEntity>
}