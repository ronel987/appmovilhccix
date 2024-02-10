package hccix.hotelcentral1

interface OnClickListener {
    fun onClick(reservaEntity: ReservaEntity)
    fun onClickFavorite(reservaEntity: ReservaEntity)
    fun onClickOption(reservaEntity: ReservaEntity)
    fun onClickDetalle(reservaEntity: ReservaEntity)
    fun onClickEditha(reservaEntity: ReservaEntity)
}