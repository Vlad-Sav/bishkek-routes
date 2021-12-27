package com.android.bishkekroutes

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.view.View

import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.DialogInterface
import android.provider.Settings

import android.app.Dialog;
import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.location.LocationManager
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import android.widget.Toast

import com.google.android.gms.common.GoogleApiAvailability

import com.google.android.gms.common.ConnectionResult
import androidx.annotation.NonNull
import com.android.bishkekroutes.utils.Constants.Companion.ERROR_DIALOG_REQUEST
import com.android.bishkekroutes.utils.Constants.Companion.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.android.bishkekroutes.utils.Constants.Companion.PERMISSIONS_REQUEST_ENABLE_GPS


class SplashActivity : AppCompatActivity(){
    private var mLocationPermissionGranted = false
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        Handler().postDelayed({
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView) as BottomNavigationView
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.routesFragment, R.id.placesFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)

    }
    override fun onResume() {
        super.onResume()
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {

            } else {
                getLocationPermission()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    //check permissions
    private fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true
            }
        }
        return false
    }
    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@SplashActivity)
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
    fun isMapsEnabled(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
    fun isServicesOK(): Boolean {

        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@SplashActivity)
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests

            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it

            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this, available, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this@SplashActivity, "You can't make map requests", Toast.LENGTH_SHORT).show()
        }
        return false
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {

                } else {
                    getLocationPermission()
                }
            }
        }
    }
}