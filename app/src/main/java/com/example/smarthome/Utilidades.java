package com.example.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.core.app.ActivityCompat;

public class Utilidades {

    public static void solicitarPermiso(final String permiso, String explicacion, final int codigoDePeticion, final Activity actividad) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)) {

            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(explicacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, codigoDePeticion);

                        }

                    }).show();

        } else {

            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, codigoDePeticion);

        }

    }

}
