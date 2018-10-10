package P04_FicherosBytes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Ejercicio_Movimientos_Escritura {
	
	static Scanner teclado;

	public static void main(String[] args) throws IOException {
		
		teclado = new Scanner(System.in);

		File fichero = new File("src\\P04_FicherosBytes\\ficheros\\movimientos.dat");
		fichero.createNewFile();
		
		FileOutputStream fileOut = new FileOutputStream(fichero);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		
		String opcion = "si";
		
		Movimiento_bancario mov;
		
		while(opcion.equals("si"))
		{
			System.out.println("Introduce la fecha");
				String fecha = teclado.nextLine();
				
			System.out.println("Introduce el concepto");
				String concepto = teclado.nextLine();
				
			System.out.println("Introduce la cantidad");
				int cantidad = teclado.nextInt();
			
			System.out.println("Introduce el tipo (boolean)");
				boolean tipo = teclado.nextBoolean();
				teclado.nextLine();
				
				mov = new Movimiento_bancario(fecha, concepto, cantidad, tipo);
			
				objectOut.writeObject(mov);
				
			System.out.println("Quieres seguir introduciendo movimientos?(si/no)");
			
				opcion = teclado.nextLine();
		}
		
		objectOut.close();

	}

}