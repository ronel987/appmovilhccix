package hccix.hotelcentral1
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=arrayOf(ReservaEntity::class,HabitacionEntity::class),version=3)
abstract class ReservaDatabase: RoomDatabase()
{
    //declaro un método abstracto
    abstract fun reservaDao(): ReservaDao
    abstract  fun habitaDao():HabitacionDao

}