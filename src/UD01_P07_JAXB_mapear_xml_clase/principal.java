package UD01_P07_JAXB_mapear_xml_clase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import UD01_P07_JAXB_mapear_xml_clase.Ventas.Venta;


public class principal {

	public static void main(String[] args) {

		visualizarxml();
		// M�todo para a�adir una venta, recibe el n�mero de venta,
		// las unidades
		// el nombre cliente, la fecha
		// Comprobar que el nm�mero de venta no exista
		
		insertarVenta(31, "Cliente 2", 10, "16-10-2018");
		
		visualizarxml();
		
		eliminarVenta(31);
		
		modificarStock(40);
		
		modificarVenta(30, 20, "23/11/2018");
		
		visualizarxml();
	}

	////////////////////////////////////////
	public static void visualizarxml() {

		/*System.out.println("------------------------------ ");
		System.out.println("-------VISUALIZAR XML--------- ");
		System.out.println("------------------------------ ");*/
		
		try {
			// JAXBContext jaxbContext = JAXBContext.newInstance("datosclases");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

			// Crear un objeto de tipo Unmarshaller para convertir datos XML en
			// un �rbol de objetos Java
			Unmarshaller u = jaxbContext.createUnmarshaller();

			// La clase JAXBElement representa a un elemento de un documento XML--
			// en este caso a un elemento del documento ventasarticulos.xml
			JAXBElement jaxbElement = (JAXBElement) u.unmarshal(new FileInputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

			// Visualizo el documento
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			//m.marshal(jaxbElement, System.out);

			// Si queremos operar con el documento obtenemos los
			// objetos del jaxbElement
			// El método getValue() retorna el modelo de contenido (content
			// model) y el valor de los atributos del elemento

			VentasType miventa = (VentasType) jaxbElement.getValue();

			// Obtenemos una instancia para obtener todas las ventas
			Ventas vent = miventa.getVentas();

			// Guardamos las ventas en la lista
			List listaVentas = new ArrayList();
			listaVentas = vent.getVenta();

			System.out.println("---------------------------- ");
			System.out.println("---VISUALIZAR LOS OBJETOS--- ");
			System.out.println("---------------------------- ");
			// Datos del art�culo
			DatosArtic miartic = (DatosArtic) miventa.getArticulo();

			System.out.println("Nombre art: " + miartic.getDenominacion());
			System.out.println("Codigo art: " + miartic.getCodigo());
			System.out.println("Stock art: " + miartic.getStock());
			System.out.println("Ventas  del art�culo , hay: " + listaVentas.size());

			for (int i = 0; i < listaVentas.size(); i++) {
				Ventas.Venta ve = (Venta) listaVentas.get(i);
				System.out.println("N�mero de venta: " + ve.getNumventa() + ". Nombre cliente: " + ve.getNombrecliente()
						+ ", unidades: " + ve.getUnidades() + ", fecha: " + ve.getFecha());
			}

		} catch (JAXBException je) {
			System.out.println(je.getCause());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

	}

	/////////////////////////////////////////////////
	private static void insertarVenta(int numeventa, String nomcli, int uni, String fecha) {

		System.out.println("---------------------------- ");
		System.out.println("-------A�ADIR VENTA--------- ");
		System.out.println("---------------------------- ");
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			JAXBElement jaxbElement = (JAXBElement) u.unmarshal(new FileInputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

			VentasType miventa = (VentasType) jaxbElement.getValue();

			// Obtenemos una instancia para obtener todas las ventas
			Ventas vent = miventa.getVentas();

			// Guardamos las ventas en la lista
			List listaVentas = new ArrayList();
			listaVentas = vent.getVenta();

			// comprobar si existe el n�mero de venta, reccorriendo el arraylist
			int existe = 0; // si no existe, 1 si existe
			for (int i = 0; i < listaVentas.size(); i++) {
				Ventas.Venta ve = (Venta) listaVentas.get(i);
				if (ve.getNumventa().intValue() == numeventa) {
					existe = 1;
					break;
				}
			}

			if (existe == 0) {
				// Crear el objeto Ventas.Venta, y si no existe se añade a la
				// lista

				Ventas.Venta ve = new Ventas.Venta();
				ve.setNombrecliente(nomcli);
				ve.setFecha(fecha);
				ve.setUnidades(uni);
				BigInteger nume = BigInteger.valueOf(numeventa);
				ve.setNumventa(nume);

				// a�adimos la venta a la lista

				listaVentas.add(ve);

				// crear el Marshaller, volcar la lista al fichero XML
				Marshaller m = jaxbContext.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(jaxbElement, new FileOutputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

				System.out.println("Venta a�adida: " + numeventa);

			} else

				System.out.println("En número de venta ya existe: " + numeventa);

		} catch (JAXBException je) {
			System.out.println(je.getCause());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

	}
	/////////////////////////////
	
	//ELIMINAR VENTA
	private static boolean eliminarVenta(int numeventa) {

		System.out.println("---------------------------- ");
		System.out.println("------ELIMINAR VENTA-------- ");
		System.out.println("---------------------------- ");
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			JAXBElement jaxbElement = (JAXBElement) u.unmarshal(new FileInputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

			VentasType miventa = (VentasType) jaxbElement.getValue();

			// Obtenemos una instancia para obtener todas las ventas
			Ventas vent = miventa.getVentas();

			// Guardamos las ventas en la lista
			List listaVentas = new ArrayList();
			listaVentas = vent.getVenta();

			// comprobar si existe el n�mero de venta, reccorriendo el arraylist
			int existe = 0; // si no existe, 1 si existe
			
			Ventas.Venta ve = new Ventas.Venta();
			
			for (int i = 0; i < listaVentas.size(); i++) 
			{
				ve = (Venta) listaVentas.get(i);
				
				if (ve.getNumventa().intValue() == numeventa) 
				{
					existe = 1; 
					
					break;
				}
			}

			if (existe == 1) 
			{
				// borrar la venta

				listaVentas.remove(ve);


				// crear el Marshaller, volcar la lista al fichero XML
				Marshaller m = jaxbContext.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(jaxbElement, new FileOutputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

				System.out.println("Venta eliminada " + numeventa);
				
				return true;
			} 
			else
			{
				System.out.println("El número de venta " + numeventa + " no existe"
						+ " y no se puede borrar");
				
				return false;	
			}	

		} catch (JAXBException je) {
			System.out.println(je.getCause());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
		return false;

	}
	//////////////////
	
	//MODIFICAR STOCK
		private static boolean modificarStock(int stock) {

			System.out.println("---------------------------- ");
			System.out.println("------MODIFICAR STOCK------- ");
			System.out.println("---------------------------- ");
			try {

				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
				Unmarshaller u = jaxbContext.createUnmarshaller();
				JAXBElement jaxbElement = (JAXBElement) u.unmarshal(new FileInputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

				VentasType miventa = (VentasType) jaxbElement.getValue();
					
				//cargamos datos del articulo
				DatosArtic datosArticulo = miventa.getArticulo();
				
				BigInteger stockActual = datosArticulo.getStock();
				BigInteger stockAnadir = BigInteger.valueOf(stock);
				int stockTotal;
				
				stockTotal = stockActual.intValue() + stockAnadir.intValue();
				
				datosArticulo.setStock(BigInteger.valueOf(stockTotal));

				// crear el Marshaller, volcar la lista al fichero XML
				Marshaller m = jaxbContext.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(jaxbElement, new FileOutputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

				System.out.println("Stock actualizado");
				
				return true;
				

			} 
			catch (JAXBException je) 
			{
				System.out.println(je.getCause());
				System.out.println("Stock no actualizado");
				
				return false;
			} 
			catch (IOException ioe) 
			{
				System.out.println(ioe.getMessage());
				System.out.println("Stock no actualizado");
				
				return false;
			}
			
			

		}
		//////////////
		
		
		//MODIFICAR VENTA
		private static boolean modificarVenta(int numeventa, int unidades, String fecha) {

			System.out.println("---------------------------- ");
			System.out.println("------MODIFICAR VENTA-------- ");
			System.out.println("---------------------------- ");
			try {

				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
				Unmarshaller u = jaxbContext.createUnmarshaller();
				JAXBElement jaxbElement = (JAXBElement) u.unmarshal(new FileInputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

				VentasType miventa = (VentasType) jaxbElement.getValue();

				// Obtenemos una instancia para obtener todas las ventas
				Ventas vent = miventa.getVentas();

				// Guardamos las ventas en la lista
				List listaVentas = new ArrayList();
				listaVentas = vent.getVenta();

				// comprobar si existe el n�mero de venta, reccorriendo el arraylist
				int existe = 0; // si no existe, 1 si existe
				
				Ventas.Venta ve = new Ventas.Venta();
				
				for (int i = 0; i < listaVentas.size(); i++) 
				{
					ve = (Venta) listaVentas.get(i);
					
					if (ve.getNumventa().intValue() == numeventa) 
					{
						existe = 1; 
						
						break;
					}
				}

				if (existe == 1) 
				{
					// modificar la venta

					ve.setUnidades(unidades);
					ve.setFecha(fecha);

					// crear el Marshaller, volcar la lista al fichero XML
					Marshaller m = jaxbContext.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					m.marshal(jaxbElement, new FileOutputStream("src\\UD01_P07_JAXB_mapear_xml_clase\\ventasarticulos.xml"));

					System.out.println("Venta modificada " + numeventa 
							+ " - Unidades: " + unidades + "- Fecha: " + fecha);
					
					return true;
				} 
				else
				{
					System.out.println("El número de venta " + numeventa + " no existe"
							+ " y no se puede modificar");
					
					return false;	
				}	

			} catch (JAXBException je) {
				System.out.println(je.getCause());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
			
			return false;

		}
		//////////////////

}