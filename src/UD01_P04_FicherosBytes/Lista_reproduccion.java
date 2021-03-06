package UD01_P04_FicherosBytes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Lista_reproduccion {
	
	private String ruta;
	
	//variables internas
	
	private File f;
	private RandomAccessFile raf;
	private long posicion;
	private StringBuffer sb;
	private Cancion cancion;
	private char[] aux;
	
	Lista_reproduccion(String vpath)
	{
		ruta = vpath;
		aux = new char [20];
	}
	
	//GETTERS Y SETTERS
	
	public void setPath(String vpath)
	{
		ruta = vpath;
	}
	
	public String getPath()
	{
		return ruta;
	}
	
	//METODOS
	
	public void crearLista() throws IOException
	{
		f = new File(ruta);
		f.createNewFile();
	}
	
	public void insertarCancion(Cancion cancion) throws IOException
	{
		raf = new RandomAccessFile(ruta, "rw");
		
		//
		
		posicion = raf.length();
		raf.seek(posicion);
		
		raf.writeInt(cancion.getId());
		raf.writeInt(cancion.getAno());
		
		sb = new StringBuffer(cancion.getTitulo());
		sb.setLength(20);
		raf.writeChars(sb.toString());
		
		sb = new StringBuffer(cancion.getArtista());
		sb.setLength(20);
		raf.writeChars(sb.toString());
		
		sb = new StringBuffer(cancion.getDuracion());
		sb.setLength(20);
		raf.writeChars(sb.toString());
		
		raf.writeBoolean(cancion.isCancion_espanola());
		
		raf.close();
		
	}

	public Cancion[] consultarCanciones() throws IOException
	{
		
		raf = new RandomAccessFile(ruta, "r");
		posicion = 0;
		
		Cancion canciones[] = new Cancion [(int)(raf.length()/129)];
		
		int x = 0;
		int j = 0;
		
		//
		
		while(j<(int)(raf.length()/129))
		{
			
			raf.seek(posicion);
			
			
			if(raf.readInt()!=-1) // <--- 
			{
				canciones[x] = new Cancion();
				
				raf.seek(posicion);
				//
				canciones[x].setId(raf.readInt());
				//
				canciones[x].setAno(raf.readInt());
				//
				for(int i = 0; i < 20; i++) 
				{	
					aux[i] = raf.readChar();
					canciones[x].setTitulo(new String(aux));
				}
				//
				for(int i = 0; i < 20; i++) 
				{	
					aux[i] = raf.readChar();
					canciones[x].setArtista(new String(aux));
				}
				//
				for(int i = 0; i < 20; i++) 
				{	
					aux[i] = raf.readChar();
					canciones[x].setDuracion(new String(aux));
				}
				//
				canciones[x].setCancion_espanola(raf.readBoolean());
				
				
				x++;
				
			}
			
			j++;
				
			posicion += 129;
			
			
		}
		
		raf.close();
		
		Cancion canciones_return[] = new Cancion[x];
		
		for(int i = 0; i < x; i++)
		{
			canciones_return[i]=canciones[i];
		}
		
		
		
		return canciones_return;
		
		
		
	}
	
	public Cancion consultarCancion(Cancion[] canciones, int id) throws IOException
	{
		
		for(int i = 0; i < canciones.length; i++)
		{
			if(canciones[i].getId()==id)
			{
				return canciones[i];
			}
		}
		
		return cancion;
		
	}
	
	public boolean borrarCancion(int id) throws IOException
	{
		
		
		raf = new RandomAccessFile(ruta, "rw");
		posicion = 0;
		
		Cancion canciones[] = new Cancion [(int)(raf.length()/129)];
		
		int x = 0;
		
		//
		
		while(x<(int)(raf.length()/129))
		{
			raf.seek(posicion);
			
			canciones[x] = new Cancion();
			
			if(raf.readInt()==id)
			{
				raf.seek(posicion);
				raf.writeInt(-1);
				raf.writeInt(id);
				raf.close();
				
				return true;
				
			}
			
			posicion += 129;
			x++;
		}
		
		raf.close();
		
		return true;
		
	}
	
	public Cancion[] mostrarCancionesBorradas() throws IOException
	{
		
		raf = new RandomAccessFile(ruta, "r");
		posicion = 0;
		
		Cancion canciones[] = new Cancion [(int)(raf.length()/129)];
		
		int x = 0;
		int j = 0;
		
		//
		
		while(j<(int)(raf.length()/129))
		{
			
			raf.seek(posicion);
			
			
			if(raf.readInt()==-1) // <--- 
			{
				canciones[x] = new Cancion();
				
				raf.seek(posicion);
				//
				canciones[x].setId(raf.readInt());
				//
				canciones[x].setAno(raf.readInt());
				//
				for(int i = 0; i < 20; i++) 
				{	
					aux[i] = raf.readChar();
					canciones[x].setTitulo(new String(aux));
				}
				//
				for(int i = 0; i < 20; i++) 
				{	
					aux[i] = raf.readChar();
					canciones[x].setArtista(new String(aux));
				}
				//
				for(int i = 0; i < 20; i++) 
				{	
					aux[i] = raf.readChar();
					canciones[x].setDuracion(new String(aux));
				}
				//
				canciones[x].setCancion_espanola(raf.readBoolean());
				
				
				x++;
				
			}
			
			j++;
				
			posicion += 129;
			
			
		}
		
		raf.close();
		
		Cancion canciones_return[] = new Cancion[x];
		
		for(int i = 0; i < x; i++)
		{
			canciones_return[i]=canciones[i];
		}
		
		
		return canciones_return;
		
		
	}
	
	
	public boolean modificarCancion(int id, int nuevo_ano) throws IOException
	{

		raf = new RandomAccessFile(ruta, "rw");
		posicion = 0;
		
		Cancion canciones[] = new Cancion [(int)(raf.length()/129)];
		
		int x = 0;
		
		//
		
		while(x<(int)(raf.length()/129))
		{
			raf.seek(posicion);
			
			canciones[x] = new Cancion();
			
			if(raf.readInt()==id)
			{
				raf.seek(posicion+4);
				raf.writeInt(nuevo_ano);
				
				return true;
				
			}
			
			posicion += 129;
			x++;
		}
		
		raf.close();
		
		return false;
		
	}
}
