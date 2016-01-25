/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardar.en.base.de.datos;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import static guardar.en.base.de.datos.funciones.lista;
import static guardar.en.base.de.datos.funciones.quitarBasura;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Document;
//import static lab2sd.Funciones.ingresar_a_bd;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Xiao
 */
public class SaxManejadordb extends DefaultHandler{
    
    ArrayList<String> listaTextos = new ArrayList<>();
    ArrayList<String> listaTitulos = new ArrayList<>();
     StringBuilder buffer = new StringBuilder();
     Mongo mongo;
     DB database;
     DBCollection collection;
    int contador = 0;
    double id = 0;
    
    
    public SaxManejadordb() throws UnknownHostException
    {
        mongo = new Mongo("localhost", 27017);

        // nombre de la base de datos
        database = mongo.getDB("paginas");
        // coleccion de la db
        collection = database.getCollection("tabla");
       
      
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
    
    buffer.append(ch,start,length);
    
    
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName); //To change body of generated methods, choose Tools | Templates.
        
    
    switch(qName)
    {
            
        case "title":
            listaTitulos.add(buffer.toString());
            contador++;
            break;
        case "text":
            String text = buffer.toString();
            text = quitarBasura(text);
            listaTextos.add(text);
            //voy guardando de 10 elementos (paginas)
            if(contador== 10)
            {
                contador = 0;
                
                //mandar a la lista de paginas a guardarse a la bd
                List<DBObject> documents = new ArrayList<>();
                //armo una lista con cambos id titulo y texto todo en el array list de dbobject
                for(int j = 0;j<listaTextos.size();j++)
                {
                    id++;
                    DBObject document = new BasicDBObject("id",id)
                        .append("titulo", listaTitulos.get(j))
                        .append("texto", listaTextos.get(j));
                    documents.add(document);
                    System.out.println("Ingresados "+id+" elementos de 103820");
                    
                    
                }
                listaTextos.clear();
                listaTitulos.clear();
                //ingresar_a_bd(documents);
                //DBCollection collection = database.getCollection("tabla");
                this.collection.insert(documents);
                
                
                documents.clear();
            }
            break;
    }
    
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes); //To change body of generated methods, choose Tools | Templates.
    
    switch(qName)
            {
       
        case "title":
            buffer.delete(0, buffer.length());
            break;
        case "text":
            buffer.delete(0, buffer.length());
            break;
            }
    }
    
    
    
    
    
}
