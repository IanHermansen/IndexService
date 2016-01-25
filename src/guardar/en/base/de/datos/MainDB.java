/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardar.en.base.de.datos;

import static guardar.en.base.de.datos.funciones.lista;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Xiao
 */
public class MainDB {
    
    
    
    public static void main(String[] args) throws SAXException, IOException,
        ParserConfigurationException, TransformerException {
        
        ArrayList lee = new ArrayList<>();
        lee = lista();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        File file = new File((String)lee.get(0));
        SaxManejadordb h = new SaxManejadordb();
        saxParser.parse(file,h );
        
        
       
        
        
    }
    
}
