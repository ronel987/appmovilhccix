package hccix.hotelcentral1

interface MainAux {
    fun hideFabNuevo(isVisible:Boolean=false)
    fun addReservaMemory(reservaEntity:ReservaEntity)
    fun editMemory(reservaEntity: ReservaEntity)
}