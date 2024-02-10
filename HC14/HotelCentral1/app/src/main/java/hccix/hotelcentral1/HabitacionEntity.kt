package hccix.hotelcentral1

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="HabitaTable")
data class HabitacionEntity constructor(@PrimaryKey var habitaId:Long=101,
                                        var fotourl :String="",
                                        var tipo:String="",
                                        var ubicacion:String,
                                        var descripcion:String){

    fun getObject(habitaId: Long): HabitacionEntity {
        return this
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReservaEntity

        if (habitaId != other.reservaId) return false

        return true
    }

    override fun hashCode(): Int {
        return habitaId.hashCode()
    }
}
