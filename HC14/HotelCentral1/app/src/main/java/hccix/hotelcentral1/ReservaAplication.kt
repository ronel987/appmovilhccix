package hccix.hotelcentral1
import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//clase que permite acceder a la BD desde cualquier punto de la aplicación
class ReservaAplication:Application() {
    //implementando el patrón SINGLETON:usa un solo obj global pa acceder a la BD: database
    companion object {
        lateinit var database:ReservaDatabase
    }

    //cargar en autom el database
    override fun onCreate() {
        super.onCreate()
        //pa migrar versiones
        val migration=object: Migration(1,2){
           override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL("alter table ReservaTable add column celular text not null default '' ")
           }
        }

        database=Room.databaseBuilder(this,ReservaDatabase::class.java, "ReservaDatabase")
            .addMigrations(migration)   //esto iria antes del build
            .build()
       //
        val migration2=object: Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL("create table HabitaTable(habitaId Long primary key, fotourl text," +
                        "tipo text, ubicacion text, descripcion text) ")
            }
        }
        database=Room.databaseBuilder(this,ReservaDatabase::class.java, "ReservaDatabase")
            .addMigrations(migration2)
            .build()
    }

}