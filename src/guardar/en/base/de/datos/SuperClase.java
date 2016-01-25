/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardar.en.base.de.datos;

import java.util.ArrayList;

/**
 *
 * @author Xiao
 */
public class SuperClase {
    
    String palabra;
    int frecuencia;
    int documento;
    int visitado;
    
    public SuperClase()
    {
        palabra = null;
        frecuencia = 1;
        documento = 0;
        visitado = 0;
        
    }
    
    public SuperClase(String palabra, int f)
    {
        this.palabra = palabra;
        this.frecuencia = f;
    }
    
    
    
}

