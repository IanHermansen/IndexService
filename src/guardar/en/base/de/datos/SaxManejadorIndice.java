/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardar.en.base.de.datos;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import static guardar.en.base.de.datos.funciones.StopWordsArray;
import static guardar.en.base.de.datos.funciones.calcularFrecuencias;
import static guardar.en.base.de.datos.funciones.calcularFrecuencias2;
import static guardar.en.base.de.datos.funciones.fn;
import static guardar.en.base.de.datos.funciones.lista;
import static guardar.en.base.de.datos.funciones.quitarBasura;
import static guardar.en.base.de.datos.funciones.quitarStop;
import static guardar.en.base.de.datos.funciones.quitarStopwords;
import static guardar.en.base.de.datos.funciones.textoArray;
//import static guardar.en.base.de.datos.funciones.fn;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Xiao
 */
public class SaxManejadorIndice extends DefaultHandler {

    ArrayList<String> listaTextos = new ArrayList<>();
    ArrayList<String> listaTitulos = new ArrayList<>();
    StringBuilder buffer = new StringBuilder();
    Mongo mongo;
    DB database;
    DBCollection collection;
    int contador = 0;
    int documento = 0;
    double id = 0;
    ArrayList listaStopWords;

    public SaxManejadorIndice() throws UnknownHostException, IOException {
        mongo = new Mongo("localhost", 27017);

        // nombre de la base de datos
        database = mongo.getDB("paginas");
        // coleccion de la db
        collection = database.getCollection("indice");
        ArrayList lee = new ArrayList<>();
        lee = lista();
        listaStopWords = StopWordsArray((String) lee.get(1));
        //collection.createIndex(new Document("palabra", 1));
        
        

    }

    public ArrayList<String> getListaTextos() {
        return listaTextos;
    }

    public ArrayList<String> getListaTitulos() {
        return listaTitulos;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length); //To change body of generated methods, choose Tools | Templates.

        buffer.append(ch, start, length);

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName); //To change body of generated methods, choose Tools | Templates.
        
        switch (qName) {

            case "title":

                break;
            case "text":
                documento++;
                //cambio el buffer a la variable texto
                String texto = buffer.toString();
                texto = quitarBasura(texto);
                // le quito las stopwords al texto
                texto = quitarStop(texto, this.listaStopWords);

                //mando el texto a separarse
                ArrayList<String> texto_separado = new ArrayList<>();

                try {
                    texto_separado = textoArray(texto);
                } catch (IOException ex) {
                    Logger.getLogger(SaxManejadorIndice.class.getName()).log(Level.SEVERE, null, ex);
                }

               texto = "";

                ArrayList<SuperClase> nuevo = new ArrayList<>();
                nuevo = fn(texto_separado);
                //libero
                //texto_sin_stopwords.clear();
                texto_separado.clear();
                //calculo las frecuencias de las palabras
                nuevo = calcularFrecuencias(nuevo);
            //ingreso a la base de datos

                for (int i = 0; i < nuevo.size(); i++) {
                    contador++;
                    //creo la lista de documentos que al principio va a ser uno
                    BasicDBList lista = new BasicDBList();
                    lista.add(new BasicDBObject("documento", this.documento)
                            .append("frecuencia", nuevo.get(i).frecuencia));

                    // luego creo el objeto db con id, palabra y una lista de docs
                    DBObject document = new BasicDBObject("id", contador)
                            .append("palabra", nuevo.get(i).palabra)
                            .append("documentos", lista);
                    //documents.add(document);
                    
              //*********Buscar************************************
                    // Solo se agrega el archivo cuando se encuentra una palabra que ya esta ingresada
                    //sin embargo es muy lento
            BasicDBObject query = new BasicDBObject("palabra",nuevo.get(i).palabra );
                     
                     try (DBCursor c = collection.find(query)) {
                         int exist = 0;
                     if(c.hasNext())
                     {
                     
                     //si existe solo agrego el archivo del que proviene
                     DBObject o = new BasicDBObject("documentos", new BasicDBObject("documento",this.documento).append("frecuencia",nuevo.get(i).frecuencia));
                     DBObject updateQuery = new BasicDBObject("$push", o);
                     collection.update(query, updateQuery);
                     exist = 1;
                     //System.out.print(c.next());  
                     }
                     if(exist == 0)
                     {
                     // agrego a la base de datos
                     this.collection.insert(document);
                
                     }
            
                     }
                     lista.clear();
        //*************************************************
                    //this.collection.insert(document);
                }
                System.out.println("Documento " + this.documento + " procesado");
                nuevo.clear();

                break;
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes); //To change body of generated methods, choose Tools | Templates.

        switch (qName) {

            case "title":
                buffer.delete(0, buffer.length());
                break;
            case "text":
                buffer.delete(0, buffer.length());
                break;
        }
    }

}
