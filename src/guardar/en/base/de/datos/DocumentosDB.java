/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guardar.en.base.de.datos;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 *
 * @author Xiao
 */
class DocumentosDB {
    
   private Integer id_documento;
    private Integer frecuencia;
    
    public DocumentosDB()
    {
        
    }
    
    public DocumentosDB(int id,int frecuencia)
    {
        this.id_documento = id;
        this.frecuencia = frecuencia;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }
    
    public  DBObject fn()
    {
       BasicDBObject doc = new BasicDBObject();
       doc.put("documento", this.id_documento);
       doc.put("frecuencia", this.frecuencia);
       
       return doc;
    
    }
    
    public  void makefn2( DBObject bson)
    {
        BasicDBObject b = ( BasicDBObject ) bson;
        this.id_documento = (int)b.get("documento");
        this.frecuencia = (int )b.get("frecuencia");
    }
}

