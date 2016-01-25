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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

/**
 *
 * @author Xiao
 */
public class MainServidor {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {

        Mongo mongo = new Mongo("localhost", 27017);

        // nombre de la base de datos
        DB database = mongo.getDB("paginas");
        // coleccion de la db
        DBCollection collection = database.getCollection("indice");
        DBCollection collection_textos = database.getCollection("tabla");
        ArrayList<String> lista_textos = new ArrayList();

        try {
            ServerSocket servidor = new ServerSocket(4545); // Crear un servidor en pausa hasta que un cliente llegue.
            while (true) {
                String aux = new String();
                lista_textos.clear();
                Socket clienteNuevo = servidor.accept();// Si llega se acepta.
                // Queda en pausa otra vez hasta que un objeto llegue.
                ObjectInputStream entrada = new ObjectInputStream(clienteNuevo.getInputStream());

                JSONObject request = (JSONObject) entrada.readObject();
                String b = (String) request.get("id");
         //hacer una query a la base de datos con la palabra que se quiere obtener

                BasicDBObject query = new BasicDBObject("palabra", b);
                DBCursor cursor = collection.find(query);
                ArrayList<DocumentosDB> lista_doc = new ArrayList<>();
                // de la query tomo el campo documentos y los agrego a una lista
                try {
                    while (cursor.hasNext()) {
                        //System.out.println(cursor.next());
                        BasicDBList campo_documentos = (BasicDBList) cursor.next().get("documentos");
                        // en el for voy tomando uno por uno los elementos en el campo documentos
                        for (Iterator< Object> it = campo_documentos.iterator(); it.hasNext();) {
                            BasicDBObject dbo = (BasicDBObject) it.next();
                            //DOC tiene id y frecuencia
                            DocumentosDB doc = new DocumentosDB();
                            doc.makefn2(dbo);
                            //int id = (int)doc.getId_documento();
                            //int f = (int)doc.getFrecuencia();
                            
                            
                            lista_doc.add(doc);
                            
                            //*******************************************
                           
                            
                            //********************************************

                            //QUERY A LA COLECCION DE TEXTOS
                           /* BasicDBObject query_textos = new BasicDBObject("id", doc.getId_documento());//query
                            DBCursor cursor_textos = collection_textos.find(query_textos);
                            try {
                                while (cursor_textos.hasNext()) {
                                    
                                    
                                    DBObject obj = cursor_textos.next();

                                    String titulo = (String) obj.get("titulo");
                                    titulo = titulo + "\n\n";
                                    String texto = (String) obj.get("texto");

                                    String texto_final = titulo + texto;
                                    aux = texto_final;
                                    lista_textos.add(texto_final);
                                }
                            } finally {
                                cursor_textos.close();
                            }*/
                            //System.out.println(doc.getId_documento());
                            //System.out.println(doc.getFrecuencia());

                        }// end for

                    }//end while query

                } finally {
                    cursor.close();
                }
                
                
                // ordeno la lista de menor a mayor
                Collections.sort(lista_doc,new Comparator<DocumentosDB>() {

                    @Override
                    public int compare(DocumentosDB o1, DocumentosDB o2) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        return o1.getFrecuencia().compareTo(o2.getFrecuencia());
                    }
                });
                int tam = lista_doc.size()-1;
                for(int j = tam; j>=0;j--)
                {
                    
                    
                    BasicDBObject query_textos = new BasicDBObject("id",(int)lista_doc.get(j).getId_documento().intValue());//query
                    DBCursor cursor_textos = collection_textos.find(query_textos);// lo busco
                    try {
                                while (cursor_textos.hasNext()) {
                                    
                                    
                                    DBObject obj = cursor_textos.next();
                                    String titulo = "*******************************";
                                     titulo  +=(String) obj.get("titulo");
                                     int f = (int)lista_doc.get(j).getFrecuencia().intValue();
                                     String strinf = Integer.toString(f);
                                    titulo  +="******************************* frecuencia:"+ strinf;
                                    titulo = titulo + "\n\n";
                                    
                                    String texto = (String) obj.get("texto");

                                    String texto_final = titulo + texto+"\n\n";
                                    aux = aux +texto_final;
                                    //lista_textos.add(texto_final);
                                }
                            } finally {
                                cursor_textos.close();
                            }
                    
                }
                
                //actualizar el cache
                try {
                    Socket cliente_cache = new Socket("localhost", 4500); // nos conectamos con el servidor
                    ObjectOutputStream mensaje_cache = new ObjectOutputStream(cliente_cache.getOutputStream()); // get al output del servidor, que es cliente : socket del cliente q se conecto al server
                    JSONObject actualizacion_cache = new JSONObject();
                    actualizacion_cache.put("actualizacion", 1);
                    actualizacion_cache.put("busqueda", b);
                    actualizacion_cache.put("respuesta", aux);
                    mensaje_cache.writeObject(actualizacion_cache); // envio el msj al servidor
                } catch (Exception ex) {

                }

                //RESPONDER DESDE EL SERVIDORIndex al FRONT
                ObjectOutputStream resp = new ObjectOutputStream(clienteNuevo.getOutputStream());// obtengo el output del cliente para mandarle un msj
                resp.writeObject(aux);
                System.out.println("msj enviado desde el servidor");

            }
        } catch (IOException ex) {
            Logger.getLogger(MainServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
