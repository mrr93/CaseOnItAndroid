package com.miguelrodriguez.clientesocket;

/**
 * Created by MiguelRodriguez on 10/08/2016.
 */
public class DatosDeRed {

    private int estadoSIM;
    private String operador;
    private String tecnologia;
    //private Srting celda;

    public DatosDeRed(int estadoSIM, String operador, String tecnologia) {
        this.estadoSIM = estadoSIM;
        this.operador = operador;
        this.tecnologia = tecnologia;
        //this.celda = celda;
    }

    public DatosDeRed() {
    }

    public int getEstadoSIM() {
        return estadoSIM;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public String getOperador() {
        return operador;
    }

    /*public String getCelda() {
        return celda;
    }*/

    /*public void setCelda(String celda){
        this.celda = celda;

     */

    public void setEstadoSIM(int estadoSIM) {
        this.estadoSIM = estadoSIM;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }
}
