package com.lrsdc.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_pulsante_serratura.*


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val  RC_SIGN_IN = 101
    lateinit var toolBar : ActionBar

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance(); //firebase

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso);

        //bottom menù config
        toolBar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = bottom_nav
        val navController = findNavController(this@MainActivity, R.id.fragment_nav)

        setupWithNavController(this.bottom_nav, navController)
        setupActionBarWithNavController(this@MainActivity,navController)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        signIn()
        updateUI(currentUser)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.pulsanteSerratura -> {
                Navigation.findNavController(this,R.id.fragment_nav).navigate(R.id.pulsanteSerratura)
                return@OnNavigationItemSelectedListener true
            }
            R.id.frameCarte -> {
                Navigation.findNavController(this,R.id.fragment_nav).navigate(R.id.frameCarte)
                return@OnNavigationItemSelectedListener true
            }
            R.id.frameNFC -> {
                Navigation.findNavController(this,R.id.fragment_nav).navigate(R.id.frameNFC)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("MyActivity", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("MyActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MyActivity", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    Toast.makeText(applicationContext, "Connesso", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("MyActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(applicationContext, "Connessione Fallita", Toast.LENGTH_SHORT).show()
                    //Snackbar.make(pulsanteSerratura, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }

    private fun updateUI(U : FirebaseUser?){
    //non fa ancora niente
        //
    }
}
