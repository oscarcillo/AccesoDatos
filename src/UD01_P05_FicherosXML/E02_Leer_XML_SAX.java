package UD01_P05_FicherosXML;

import java.io.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class E02_Leer_XML_SAX 
{	
	//
	public static void main(String[] args)
              throws FileNotFoundException, IOException, SAXException
	{
		
	 XMLReader  procesadorXML = XMLReaderFactory.createXMLReader();
	 
	 GestionContenido gestor = new GestionContenido(); 
	 
	 procesadorXML.setContentHandler(gestor);
	 
 	 InputSource fileXML = new InputSource("src\\UD01_P05_FicherosXML\\ficheros\\canciones.xml");	
 	 
     procesadorXML.parse(fileXML);        	      
	}
	
}

class GestionContenido extends DefaultHandler 
{	 
	
	int contador;
	
	    public GestionContenido() {
	        super();
	    }	  
	    
	    public void startDocument() {
	        System.out.println("Comienzo del Documento XML");
	    }
	    
	    public void endDocument() {
	        System.out.println("Final del Documento XML");
	        
	        System.out.println("->El numero de canciones españolas es " + contador);
	    }	
	    
	    public void startElement(String uri, String nombre,
	              String nombreC, Attributes atts) 
	    {
	        System.out.printf("\tPrincipio Elemento: %s %n",nombre);
	    } 
	    
	    public void endElement(String uri, String nombre, String nombreC) 
	    {
	        System.out.printf("\tFin Elemento: %s %n", nombre);
	    }
	    
	    public void characters(char[] ch, int inicio, int longitud) 
                                            throws SAXException 
	    {
		   String car=new String(ch, inicio, longitud);
		   
           //quitar saltos de linea
		   car = car.replaceAll("[\t\n]","");	  
		   
		   System.out.printf ("\t Contenido: %s %n", car);	

	       if(car.equals("true"))
	       {
	        	contador++;
	       }
	    }	

}//fin GestionContenido
