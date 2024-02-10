package hccix.hotelcentral1
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ReservaTable")
data class ReservaEntity constructor(@PrimaryKey(autoGenerate=true) var reservaId:Long=0,
                                     var habitaId:Int=101,
                                    var fecha:String="",
                                     var dias:Int=2,
                                     var nombre:String="",
                                     var apellido:String="",
                                     var dni:String="",
                                     var celular:String="",
                                     var isFavorite:Boolean=false){ //evitar duplicidad de ids
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReservaEntity

        if (reservaId != other.reservaId) return false

        return true
    }

    override fun hashCode(): Int {
        return reservaId.hashCode()
    }
}
