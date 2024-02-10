package hccix.hotelcentral1
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import hccix.hotelcentral1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),OnClickListener,MainAux {
    private  lateinit var auth:FirebaseAuth
    //referencia al layout(vista) activity_main
    private lateinit var mBinding:ActivityMainBinding
    //referencia a la clase Recycler1
    private lateinit var mAdapter: Recycler1
     //objeto para la grilla del Recycler
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sistema de vinculación de vistas
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        auth = Firebase.auth
        //configuración del RecyclerView... inicio
        mAdapter=Recycler1(mutableListOf(),this)
        mGridLayout=GridLayoutManager(this,2)

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            adapter=mAdapter
            layoutManager=mGridLayout
        }
        //configuración del RecyclerView... final
        //cargar colección desde BD, luego la sincroniza con el recyclerView
        doAsync {
            val reservasDB=ReservaAplication.database.reservaDao().findAll()
            uiThread {
                mAdapter.setReservaMemory(reservasDB)
            }
        }
        //evento boton flotante
        mBinding.fabNuevo.setOnClickListener {
             lanzarFragmento()
        }
                //cerrarsession()
    }
    private fun cerrarsession(){
        Firebase.auth.signOut()
        val intenta=Intent(this,SignInActivity::class.java)
        startActivity(intenta)
    }

    //evento al click sobre algun item:para Editar el Item
    override fun onClick(reservaEntity: ReservaEntity) {
        val bundle=Bundle()
        bundle.putLong("id",reservaEntity.reservaId)
        bundle.putInt("nrohabita",reservaEntity.habitaId)
        bundle.putInt("dias",reservaEntity.dias)
        bundle.putString("fecha",reservaEntity.fecha)
        bundle.putString("nombre",reservaEntity.nombre)
        bundle.putString("ape",reservaEntity.apellido)
        bundle.putString("dni",reservaEntity.dni)
        bundle.putString("celular",reservaEntity.celular)
        bundle.putBoolean("favorito",reservaEntity.isFavorite)
        lanzarFragmento(bundle)
    }
    //Heredado desde la Interfaz: el evento a la estrellita captura el obj reservaEntity
    override fun onClickFavorite(reservaEntity: ReservaEntity) {
        reservaEntity.isFavorite =!reservaEntity.isFavorite
        doAsync {
            ReservaAplication.database.reservaDao().update(reservaEntity)
            //para actualizar la memoria(vinculada al RV):
            uiThread {
                mAdapter.editMemory(reservaEntity)
            }
        }
    }
    //Heredado desde la Interfaz:click en ojo: Detalle: captura el obj reservaEntity
    override fun onClickDetalle(reservaEntity: ReservaEntity) {
        val bundle=Bundle()
        bundle.putLong("ideta",reservaEntity.reservaId)
        val fragmen=DetallerFragment()
        fragmen.arguments=bundle
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.containerMain,fragmen)
        fragmentTransaction.replace(R.id.containerMain,fragmen)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        mBinding.fabNuevo.hide()
        mBinding.containerMain.isVisible.apply { false }
    }

    //Heredado desde la Interfaz:click lapiz: captura el obj reservaEntity pa editar habita
    override fun onClickEditha(reservaEntity: ReservaEntity) {
        val minten= Intent(this,HabitacionActivity::class.java).apply {
            putExtra("habiid",reservaEntity.habitaId.toLong())
        }
        startActivity(minten)
        mBinding.fabNuevo.hide()
       // mBinding.containerMain.isVisible.apply { false }
    }

    override fun onClickOption(reservaEntity: ReservaEntity) {
        val array= arrayOf("Delete?","Call Cell?","Go Web Site?") //i=indice=0,1,2
        MaterialAlertDialogBuilder(this).setTitle(R.string.message_options_title)
            .setItems(array,DialogInterface.OnClickListener { dialogInterface, i ->
                when(i){
                   0 -> confirmDelete(reservaEntity)
                   1 -> callPhone(reservaEntity.celular)
                   2 -> callWebsite("http://www.chiclayo-hotelcentral.com")
                }
            })
            .show()
    }

    override fun hideFabNuevo(isVisible: Boolean) {
        if(isVisible){
            mBinding.fabNuevo.show()
        }else{
            mBinding.fabNuevo.hide()
        }
    }

    override fun addReservaMemory(reservaEntity: ReservaEntity) {
       mAdapter.addReservaMemory(reservaEntity)
    }

    override fun editMemory(reservaEntity: ReservaEntity){
        mAdapter.editMemory(reservaEntity)
    }
    private fun lanzarFragmento(args:Bundle?=null){  //pa ReservarFragment
        val fragmento=ReservarFragment()
        if(args!=null){
            fragmento.arguments=args
        }
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.containerMain,fragmento)
        fragmentTransaction.addToBackStack(null)  //activity_main.xml quedara debajo en la pila
        fragmentTransaction.commit()
        mBinding.fabNuevo.hide()  //ocultar
    }

    private fun confirmDelete(reservaEntity: ReservaEntity){
        //dialogInterface y i(identificador) son parametros de entrada
        MaterialAlertDialogBuilder(this).setTitle(R.string.message_delete)
            .setPositiveButton(R.string.message_confiirm,DialogInterface.OnClickListener { dialogInterface, i ->
                doAsync {
                    ReservaAplication.database.reservaDao().delete(reservaEntity)
                    //para borrar en la memoria:RV:
                    uiThread {
                        mAdapter.deleteMemory(reservaEntity)
                    }
                }
            })
            .setNegativeButton(R.string.message_del_cancel,null)
            .show()
    }
    private fun callPhone(number:String){  //hay q llamar a una app externa
        val call=Intent().apply {
            action=Intent.ACTION_DIAL
            data= Uri.parse("tel:$number")
        }
        if(call.resolveActivity(packageManager) !=null){
            startActivity(call)
        }else {
           Toast.makeText(this,R.string.error_no_resolve,Toast.LENGTH_LONG).show()
        }
    }
    private fun callWebsite(dominio:String){
        val call=Intent().apply {
            action=Intent.ACTION_VIEW
            data= Uri.parse(dominio)
        }
        if(call.resolveActivity(packageManager) !=null){
            startActivity(call)
        }else {
            Toast.makeText(this,R.string.error_no_resolve,Toast.LENGTH_LONG).show()
        }
    }

}