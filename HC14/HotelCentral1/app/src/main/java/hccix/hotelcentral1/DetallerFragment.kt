package hccix.hotelcentral1
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import hccix.hotelcentral1.databinding.FragmentDetallerBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

class DetallerFragment : Fragment() {
    private  lateinit var dBinding: FragmentDetallerBinding
    private var dActivity:MainActivity?=null
    private  var reserEntity:ReservaEntity?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        dBinding= FragmentDetallerBinding.inflate(inflater,container,false)
        return dBinding.root
    }
    //ciclo de vida del fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dActivity=activity as? MainActivity      //obtengo una instancia de la actividad
        val reservaId=arguments?.getLong("ideta",0)
        Toast.makeText(dActivity, "ReservaId: $reservaId", Toast.LENGTH_LONG).show()
        val datos= ArrayList<String>()
        if(reservaId!=null && reservaId!=0L) {
            doAsync {
                reserEntity = ReservaAplication.database.reservaDao().findById(reservaId)
                datos.add("ReservaId: "+reserEntity!!.reservaId.toString())
                datos.add("Fecha de Llegada: "+reserEntity!!.fecha)
                datos.add("Nombre de Pasajero: "+reserEntity!!.nombre)
                datos.add("Apellido de Pasajero: "+reserEntity!!.apellido)
                datos.add("Dni de Pasajero: "+reserEntity!!.dni)
                datos.add("Habitación Reservada: "+reserEntity!!.habitaId.toString())
                datos.add("Celular de Pasajero: "+reserEntity!!.celular)
                datos.add("Cantidad de Días Reservados: "+reserEntity!!.dias.toString())
                datos.add("Tipo de Habitación: ")
                datos.add("Ubicación de Habitación: ")
                datos.add("Descripción de Habitación: ")
            }
            val adaptador= dActivity?.let { ArrayAdapter(it,R.layout.elementodelista,datos) }
            dBinding.lvDetalle.adapter=adaptador
        }

        dActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)          //bar con flecha de retroceso
        setHasOptionsMenu(true)         //acceso
        dActivity?.supportActionBar?.title = "Detalle de Reserva"
    }
    //pa cargar desde menu_detalle.xml a la memoria real: y q aparezca en el CVida del fragment:habilita los botones dere
    /* Pero no lo usare
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detalle,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/
    //pa captar el evento(flecha) de la barra de menu:
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{    //flecha de retroceso
                dActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //terminar el CVida correctamente o volver al estado original
    override fun onDestroy() {
        dActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        dActivity?.supportActionBar?.title=getString(R.string.app_name)
        setHasOptionsMenu(false)
        dActivity?.hideFabNuevo(true)
        super.onDestroy()
    }
}