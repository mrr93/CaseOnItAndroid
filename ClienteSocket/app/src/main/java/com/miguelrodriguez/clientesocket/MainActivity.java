package com.miguelrodriguez.clientesocket;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView operador, sim, tecnologia, celda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Enlazamos los textviews del XML a la Clase MainActivity
        operador = (TextView) findViewById(R.id.operador);
        sim = (TextView) findViewById(R.id.sim);
        tecnologia = (TextView) findViewById(R.id.tecnologia);
        celda = (TextView) findViewById(R.id.celda);

    }

    public void enviar(View view){

        ComunicacionTask com = new ComunicacionTask();
        com.execute();
    }

    private class ComunicacionTask extends AsyncTask<String, Void, String> {

        private String IP = "10.0.2.2";
        private int PUERTO = 8080;

        @Override
        protected String doInBackground(String... datosDeRed) {
            BufferedReader bf = null;
            Socket socket = null;
            String res = "";

            try {

                socket = new Socket(IP, PUERTO);
                PrintStream printStream = new PrintStream(socket.getOutputStream());

                Gson gson = new Gson();
                DatosDeRed datosDeRedRespuesta = gson.fromJson(String.valueOf(datosDeRed),DatosDeRed.class);

                // En este momento, se trataría el objeto JSON recibido.


                printStream.flush();


                InputStream is = socket.getInputStream();
                bf = new BufferedReader(new InputStreamReader(is));
                res = bf.readLine();
            } catch (IOException exception) {
                exception.printStackTrace();

            } finally {
                if (bf != null) {
                    try {
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return res;

        }


        @Override
        protected void onPostExecute(String result) {
            DatosDeRed datosDeRed = new DatosDeRed();

            operador.setText(datosDeRed.getOperador());
            tecnologia.setText(datosDeRed.getTecnologia());
            sim.setText(datosDeRed.getEstadoSIM());

        }
    }
}
