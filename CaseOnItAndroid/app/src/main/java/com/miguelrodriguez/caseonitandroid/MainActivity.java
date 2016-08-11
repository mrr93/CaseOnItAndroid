package com.miguelrodriguez.caseonitandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    ServerSocket serverSocket;

    DatosDeRed datosDeRed = new DatosDeRed();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Creamos un TelephonyManager y lo inicializamos para poder obtener los datos del telefono
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        // Variable para localizar la celda conectada
        //GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();

        // Creamos una variable de tipo int para recoger y controlar a continuación si hay sim o no en el teléfono
        int estadoSIM = tm.getSimState();

        // ConnectivityManager para obtener la información de la red conectada
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        int networktype = tm.getNetworkType();

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean internet_movil = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        boolean wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        // Controlamos el estado de la sim --> ABSENT: no hay sim || READY --> hay sim y está preparada para usarse
        if (estadoSIM == tm.SIM_STATE_ABSENT) {

            datosDeRed.setEstadoSIM(tm.SIM_STATE_ABSENT);
            datosDeRed.setOperador(tm.getNetworkOperatorName().toString());

            // Controlamos que la red conectada es WIFI
            if (wifi == true) {

                datosDeRed.setTecnologia("WIFI");
                //datosDeRed.setCelda(String.valueOf(location.getCid()));

            } else {

                datosDeRed.setTecnologia("NO CONECTADO A INTERNET");

            }

        } else if (estadoSIM == tm.SIM_STATE_READY) {

            datosDeRed.setEstadoSIM(tm.SIM_STATE_READY);
            datosDeRed.setOperador(tm.getNetworkOperatorName().toString());

            // Controlamos que la red conectada es una Red Móvil y su tipo
            if (internet_movil == true) {

                switch (networktype) {
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        datosDeRed.setTecnologia("2G");
                        //datosDeRed.setCelda(String.valueOf(location.getCid()));
                        break;

                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        datosDeRed.setTecnologia("3G");
                        //datosDeRed.setCelda(String.valueOf(location.getCid()));
                        break;

                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        datosDeRed.setTecnologia("3G+/H+");
                        //datosDeRed.setCelda(String.valueOf(location.getCid()));
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                        datosDeRed.setTecnologia("4G");
                        //datosDeRed.setCelda(String.valueOf(location.getCid()));
                        break;

                    default:
                        datosDeRed.setTecnologia("INFERIOR A 2G");
                        //datosDeRed.setCelda(String.valueOf(location.getCid()));
                        break;
                }
            } else if (wifi == true) {

                datosDeRed.setTecnologia("WIFI");
                //datosDeRed.setCelda(String.valueOf(location.getCid()));
            } else {
                datosDeRed.setTecnologia("NO CONECTADO");
            }


        }


    }

    // Clase que inicia nuevos hilos l recibir peticiones
    public class ServidorSocket{

        public void main(String[] args){

            try {
                ServerSocket servidor = new ServerSocket(8080);

                while(true){

                    Socket socket = servidor.accept();

                    new HiloCliente(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


    // Clase que controla los hilos que se van creando en el Servidor y manda los datos de la red en formato JSON
    private class HiloCliente extends Thread {

        Socket socket;

        public HiloCliente(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run(){

            PrintStream salida = null;

            try{
                salida=new PrintStream(socket.getOutputStream());
                StringBuilder sb = new StringBuilder();
                Gson gson = new Gson();
                String JSON = gson.toJson(datosDeRed);
                salida.close();

            }catch (IOException exception){
                exception.printStackTrace();
            } finally {
                try{
                    socket.close();
                }catch (IOException exception){
                    exception.printStackTrace();

                }
            }


        }


    }


}