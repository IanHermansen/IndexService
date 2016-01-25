/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





package guardar.en.base.de.datos;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 *
 * @author Xiao
 */
public class funciones {
    
    
    public static String quitarBasura(String texto)
    {
        String aux = new String();
        for (String str : texto.split("[^a-zA-Z]+")) {
            aux = aux+str+" ";
             //System.out.println(str);
}
        return aux;
    }
    
    public static ArrayList<String> lista() throws FileNotFoundException, IOException
    {
        ArrayList<String> leer = new ArrayList<>();
        String cadena;
        String xml;
        String stop;
        FileReader f = new FileReader("config.txt");
        try (BufferedReader b = new BufferedReader(f)) {
            cadena = b.readLine();
            StringTokenizer st = new StringTokenizer(cadena, " =");

            String palabra = st.nextToken();
            stop = st.nextToken();
            //*****************************
            
            cadena = b.readLine();
            st = new StringTokenizer(cadena, " =");

            palabra = st.nextToken();
            xml = st.nextToken();

        }
        leer.add(xml);
        leer.add(stop);
        return leer;
    }
    
    
    
    // funcion que separa un texto en palabras
    public static ArrayList<String> textoArray(String texto) throws FileNotFoundException, IOException
    {
        
                ArrayList<String> texto_separado = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(texto, " ,.[[//\\']]\n\n|()==[]||{{}}+*:;?¿!¡<>-");
                while(st.hasMoreTokens())
                {
                    String palabra = st.nextToken();
                    texto_separado.add(palabra);
                    //System.out.println(palabra);
                }
               
                
            
            return texto_separado;
            
        }
    
    // funcion que lee el archivo de stopwords y lo pasa a un array
    public static ArrayList StopWordsArray(String archivo) throws FileNotFoundException, IOException {
        ArrayList lista = new ArrayList<>();
        String cadena;
        FileReader f = new FileReader(archivo);
        try (BufferedReader b = new BufferedReader(f)) {
            while((cadena = b.readLine())!=null) {
                
                lista.add(cadena);
                
            }
        }
        return lista;
    }
    
    
    // funcion que quita las stopwords de un texto
     public static ArrayList quitarStopwords(ArrayList texto,ArrayList listaStopWords) throws IOException
    {
        ArrayList listaIndices = new ArrayList<>();
        
        int tam = texto.size();
        for(int j = 0;j<listaStopWords.size();j++)
        {
            for(int i = 0;i<tam;i++)
            {
                // si la palabra en el array corresponde a una en la lista de stopwords se agrega a la lista de indices para luego borrarlo
                if(((String)texto.get(i)).compareTo((String)listaStopWords.get(j))== 0)
                {
                    //System.out.println("Se ha removido: "+texto.get(i));
                    listaIndices.add(i);
                }
            }
        }
        // se ordena de menor a mayor la lista de indices
        Collections.sort(listaIndices);
        
        // se borran las stopwords del texto que esta en el array
        for(int a = 0;a<listaIndices.size();a++)
        {
            if(a ==0)
            {
                //System.out.println("Se ha removido: "+ texto.get((int)listaIndices.get(a)));
                int r = (int)listaIndices.get(a);
                texto.remove(r);
                
                    
            }
            else
            {
                //System.out.println("Se ha removido: "+ texto.get((int)listaIndices.get(a)));
                int r = ((int)listaIndices.get(a))-a;
                texto.remove(r);
        
            }
        }
        return texto;
        
    }
     
     // funcion que devuelve un array de superclases
    public static ArrayList<SuperClase> fn(ArrayList texto)
    {
        
        ArrayList<SuperClase> listaBolas = new ArrayList<>();
        
        for(int i = 0;i<texto.size();i++){
        
            
            //System.out.println(texto.get(i));
            SuperClase sp = new SuperClase();
            sp.palabra = (String)texto.get(i);
            //sp.frecuencia = 0;
            listaBolas.add(sp);
            
    }
       
        
        return listaBolas;
    
    }
    
    public static int esta( ArrayList<SuperClase> lista_vacia, SuperClase sp)
            
    {
        int esta = 0;
        for (int i = 0; i < lista_vacia.size(); i++)
        {
            if ((lista_vacia.get(i).palabra).compareTo(sp.palabra) == 0)
                {
                    esta = 1;
                }
        }
        return esta;
    }
    
   
    
    
    
    
    
    public static ArrayList<SuperClase> calcularFrecuencias(ArrayList<SuperClase> listaBolas)
    {
        ArrayList listaIndices = new ArrayList<>();
        
       
        for (int i = 0; i < listaBolas.size(); i++)
        {
            for (int j = i + 1; j <listaBolas.size(); j++)
            {
                if ((listaBolas.get(i).palabra).compareTo(listaBolas.get(j).palabra) ==0)
                {
                    if(listaBolas.get(j).visitado == 0)
                    {
                    // si encuentras palabras iguales aumento la freceuncia en la primera lista
                    
                    listaBolas.get(i).frecuencia++;
                    listaBolas.get(j).visitado = 1;
                    
                   
                    
                    //agrego el indice de la palabra que tengo que borrar a mi lista de indices
                    listaIndices.add(j);
                    }
                    
                }
            }
        
        }
        //ordeno los indices de menor a mayor
        Collections.sort(listaIndices);
        HashSet hs = new HashSet(listaIndices);
        listaIndices.clear();
        listaIndices.addAll(hs);
        Collections.sort(listaIndices);
        
        
        // para posteriormente borrar las repetidas
        for(int k = 0; k<listaIndices.size();k++)
        {
                if(k == 0)
                {
                    int r = (int)listaIndices.get(k);
                    listaBolas.remove(r);
                }
                else
                {
                    int r = ((int)listaIndices.get(k))-k;
                    listaBolas.remove(r);
                }
                
        }
        //System.out.println("\n");
        //System.out.println("Repetidos eliminados");
        //imprimirArrayDeSuperClase(listaBolas);
        return listaBolas;
    }
    
    
    public static String quitarStop(String texto, ArrayList<String> lista)
    {
        String b = new String();
        for(int a = 0;a<lista.size();a++)
        {
            String stop = " "+lista.get(a)+" ";
             texto = texto.replaceAll(stop, " ");
        }
        return texto;
    }
    
    public static ArrayList<SuperClase> calcularFrecuencias2(String texto, ArrayList<SuperClase> lista) {

        //StringTokenizer st = new StringTokenizer(texto, " ,.[[//\\']]\n\n|()=={{}}+*:;?¿!¡<>-");
        ArrayList<SuperClase> l = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            while (texto.contains(" "+lista.get(i).palabra+" "))// && ! texto.contains(aux2));
            {

                lista.get(i).frecuencia++;
                texto = texto.replaceFirst(" " + lista.get(i).palabra + " ", " ");

            }
            //lista.add(sp);

        }
        return lista;

    }
    
    
}
