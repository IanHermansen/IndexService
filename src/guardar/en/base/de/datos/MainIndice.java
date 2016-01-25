/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardar.en.base.de.datos;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import static guardar.en.base.de.datos.funciones.lista;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;


/**
 *
 * @author Xiao
 */
public class MainIndice {

    
    
    
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        
         ArrayList lee = new ArrayList<>();
        lee = lista();
        //EJECUTAR MAIN PARA CREAR EL INDICE
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        File file = new File((String)lee.get(0));
        SaxManejadorIndice h = new SaxManejadorIndice();
        saxParser.parse(file,h );
        
        
    
    
}
}