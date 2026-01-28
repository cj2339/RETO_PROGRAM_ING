package main;

import utilidades.Utilidades;

public class Main {

	public static void main(String[] args) {
		int opc;
		do {
			opc = menu();
			switch (opc) {
			case 1:
				aniadirJugador();
				// El codigo se autogenerara (Ejemplo: JUG - 001) los demas datos NO se iran seteando. Se pediran todos los datos y se guardaran al final.
				break;
			case 2:
				aniadirEntrenador();
				//El codigo se autogenerara (Ejemplo: ENT - 001) los demas datos NO se iran seteando. Se pediran todos los datos y se guardaran al final.
				break;
			case 3:
				aniadirEntrenamiento();
				//Se le preguntara el codigo y se comprobara que no exista. Antes de que lo añada, advertiremos de que el formato del codigo deberan de ser minimo 
				//2 letras y 3 numeros. los demas datos NO se iran seteando. Se pediran todos los datos y se guardaran al final.
				break;
			case 4:
				editarEdad();
				//Se mostrara un listado de tosdos los jugadores para que pueda ver los codigos. Despues se le preguntara el codigo del jugador que quiera modificar.
				// Se comprobara que el codigo que ha introducido existe. 
				break;
			case 5:
				eliminarJugador();
				//Se mostrara un listado de tosdos los jugadores para que pueda ver los codigos. Se comprobara que el jugador exista para poder eliminarlo. 
				break;
			case 6:
				infoEquipo();
				//Se mostrara la info de un equipo.
				break;
			case 7:
				clasificacion();
				//muestra la clasificacion de la liga. Se ordenara por puntos. Se sumaran los puntos de los jugadores de cada equipo y asi se obtendran los puntos de 
				// un equipo.
				break;
			case 8:
				mostrarMasAntiguo();
				//muestra el jugador que mas años haya estado en un equipo.
				break;
				// Opcionales si nos vemos bn de tiempo.
			case 9:
				traspasoJugador();
				//Se mostrara un listado de tosdos los jugadores para que pueda ver los codigos. Se pedira el nombr del equipo al que quiera que sea traspasado y se 
				// guardara la info para que se traspase.
				break;
			case 10:
				mostrarJugadoresPos();
				//Se le pedira al usuario una de las posiciones. Se comprobara que la posicion este en el enum. Se mostraran los juadores de dicha posicion
				break;
			case 11:
				jugadoresPorPuntos();
				//Se mostrara la lista de jugadores ordenados por los puntos.
				break;
			case 12:
				System.out.println("Agurrr");
				break;
				
			}

		} while (opc != 12);


	}

	private static void jugadoresPorPuntos() {
		// TODO Auto-generated method stub
		
	}

	private static void mostrarJugadoresPos() {
		// TODO Auto-generated method stub
		
	}

	private static void traspasoJugador() {
		// TODO Auto-generated method stub
		
	}

	private static void mostrarMasAntiguo() {
		// TODO Auto-generated method stub
		
	}

	private static void clasificacion() {
		// TODO Auto-generated method stub
		
	}

	private static void infoEquipo() {
		// TODO Auto-generated method stub
		
	}

	private static void eliminarJugador() {
		// TODO Auto-generated method stub
		
	}

	private static void aniadirEntrenamiento() {
		// TODO Auto-generated method stub
		
	}

	private static void editarEdad() {
		// TODO Auto-generated method stub
		
	}

	private static void aniadirEntrenador() {
		// TODO Auto-generated method stub
		
	}

	private static void aniadirJugador() {
		
	}
	
	private static String existeJugador() {
		//se usa para los metoddos editarEdad y eliminarJugador para comprobar que exista el codigo. Si el codigo existe lo devuelve, sino, devuelve -1.
		return "";
	}

	public static int menu() {
		int ele;
		System.out.println("\n1. Añadir Jugador");
		System.out.println("2. Añadir entrenador");
		System.out.println("3. Añadir entrenamiento");
		System.out.println("4. Editar la edad de un jugador por su codigo");
		System.out.println("5. Eliminar jugador");
		System.out.println("6. Mostrar info equipo");
		System.out.println("7. Mostrar clasificacion");
		System.out.println("8. Mostrar jugador con mas tiempo en un equipo");
		System.out.println("9. Traspaso de jugador");
		System.out.println("10. Mostrar todos los jugadores de una posicion");
		System.out.println("11. Mostrar jugadores ordenados por puntos");
		System.out.println("12. Salir");
		System.out.println("¿Qué quieres hacer?");
		ele = Utilidades.leerInt(1, 5);
		return ele;

	}
}
