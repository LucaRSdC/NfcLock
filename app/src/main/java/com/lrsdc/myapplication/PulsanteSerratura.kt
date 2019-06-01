package com.lrsdc.myapplication


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_carte.*
import kotlinx.android.synthetic.main.fragment_nfc.*
import kotlinx.android.synthetic.main.fragment_pulsante_serratura.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PulsanteSerratura : Fragment() {

    private lateinit var database: DatabaseReference //lateinit serve per non inizializzare la variabile
    private lateinit var value: String
    private lateinit var ref : DatabaseReference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //
        FirebaseApp.initializeApp(this.context);
        database = FirebaseDatabase.getInstance().reference //oggetto di tipo riferimento che ci permette di recuperare valori sul database
        val nodo = "LED_STATUS"
        ref = database.child(nodo) //riferimento al elemento da noi cercato
        ref.addValueEventListener(ReadData)//stiamo configurando il listener per sapere se il valore di cui abbiamo il riferimento viene modificato

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pulsante_serratura, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.button.setOnClickListener {
            //qui si chiama la funzione per leggere
            if(::value.isInitialized) {
                if (value == "OFF" ) {
                    ref.setValue("ON")//modifica valore presente sul DataBase
                    Log.d("MyActivity", "L'ho Acceso")
                } else if (value == "ON") {
                    ref.setValue("OFF")
                    Log.d("MyActivity", "L'ho spento")
                }
            }
        }
    }

    var ReadData = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            value = dataSnapshot.getValue(String::class.java)!!
            Log.d("MyActivity", "Value is: " + value)
            if(value =="OFF"){
                ledStatus?.text = resources.getString(R.string.OFF_String) //il punto interrogativo è necessario anche se è sempre non null, altrimenti crasha
            }else{
                ledStatus?.text = resources.getString(R.string.On_string)
            }

        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w("MyActivity", "Failed to read value.", error.toException())
        }
    }


}
