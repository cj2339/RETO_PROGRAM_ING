package main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import clases.*;
import excepciones.MaxEntException;
import excepciones.MaxJugException;
import utilidades.Utilidades;
import utilidades.MyObjectOutputStream;

public class Main {

	public static void main(String[] args) {
		int opc;
		File fich1 = new File("Staffs.dat");
		File fich2 = new File("Equipos.dat");
		if (!fich2.exists()) {
			fillData(fich2);
			saveDataJugadores(fich1);

		}

		do {
			opc = menu();
			switch (opc) {
			case 1:
				aniadirJugador(fich1);
				// El codigo se autogenerara (Ejemplo: JUG - 001) los demas datos NO se iran
				// seteando. Se pediran todos los datos y se guardaran al final.
				break;
			case 2:
				aniadirEntrenador(fich1, fich2);
				// El codigo se autogenerara (Ejemplo: ENT - 001) los demas datos NO se iran
				// seteando. Se pediran todos los datos y se guardaran al final.
				break;
			case 3:
				aniadirEntrenamiento(fich1);
				// Se le preguntara el codigo y se comprobara que no exista. Antes de que lo
				// añada, advertiremos de que el formato del codigo deberan de ser minimo
				// 2 letras y 3 numeros. los demas datos NO se iran seteando. Se pediran todos
				// los datos y se guardaran al final.
				break;
			case 4:
				editarEdad();
				// Se mostrara un listado de tosdos los jugadores para que pueda ver los
				// codigos. Despues se le preguntara el codigo del jugador que quiera modificar.
				// Se comprobara que el codigo que ha introducido existe.
				break;
			case 5:
				eliminarJugador();
				// Se mostrara un listado de tosdos los jugadores para que pueda ver los
				// codigos. Se comprobara que el jugador exista para poder eliminarlo.
				break;
			case 6:
				infoEquipo();
				// Se mostrara la info de un equipo.
				break;
			case 7:
				clasificacion();
				// muestra la clasificacion de la liga. Se ordenara por puntos. Se sumaran los
				// puntos de los jugadores de cada equipo y asi se obtendran los puntos de
				// un equipo.
				break;
			case 8:
				mostrarMasAntiguo();
				// muestra el jugador que mas años haya estado en un equipo.
				break;
				// Opcionales si nos vemos bn de tiempo.
			case 9:
				traspasoJugador();
				// Se mostrara un listado de tosdos los jugadores para que pueda ver los
				// codigos. Se pedira el nombr del equipo al que quiera que sea traspasado y se
				// guardara la info para que se traspase.
				break;
			case 10:
				mostrarJugadoresPos();
				// Se le pedira al usuario una de las posiciones. Se comprobara que la posicion
				// este en el enum. Se mostraran los juadores de dicha posicion
				break;
			case 11:
				jugadoresPorPuntos();
				// Se mostrara la lista de jugadores ordenados por los puntos.
				mostrarEntrenadores(fich1);
				break;
			case 12:
				System.out.println("Agurrr");
				break;

			}

		} while (opc != 12);
	}

	private static void jugadoresPorPuntos() {


	}

	private static void mostrarJugadoresPos() {

	}

	private static void traspasoJugador() {

	}

	private static void mostrarMasAntiguo() {

	}

	private static void clasificacion() {

	}

	private static void infoEquipo() {

	}

	private static void eliminarJugador() {

	}

	private static void aniadirEntrenamiento(File fich1) {
		boolean encontrado = false;
		String cod_e, cod_ej, nom, descripcion;
		int duracion, dificultad;
		ObjectInputStream ois = null;
		boolean finArchivo = false, error=false;

		if (!fich1.exists()) {
			System.out.println("No hay personal registrado. No se puede añadir un entrenamiento.");
		} else {
			System.out.println("Introduce el codigo del entrenamiento: ");
			cod_ej = Utilidades.introducirCadena();
			System.out.println("Introduce el nombre del entrenamiento: ");
			nom = Utilidades.introducirCadena();

			System.out.println("Introduce la duración del entrenamiento (minutos): ");
			duracion = Utilidades.leerInt(); 

			System.out.println("Introduce la dificultad del ejercicio (1-5): ");
			dificultad = Utilidades.leerInt(1, 5);
			System.out.println("Introduce la descripcion del entrenamiento: ");
			descripcion = Utilidades.introducirCadena();

			Ejercicio ejercicio = new Ejercicio(cod_ej, nom, descripcion, dificultad, duracion);

			do {
				System.out.println("Entrenadores disponibles:");
				mostrarEntrenadores(fich1);
				System.out.println("Introduce el codigo del Entrenador al que quieres añadirle el entrenamiento: ");
				cod_e = Utilidades.introducirCadena();
				finArchivo = false;
				while(!error) {
					try {
						ois = new ObjectInputStream(new FileInputStream(fich1));
						while (!finArchivo && !encontrado) {
							try {
								Object obj = ois.readObject();
								if (obj instanceof Entrenador) {
									Entrenador ent = (Entrenador) obj;
									if (ent.getCod_e().equalsIgnoreCase(cod_e)) {
										encontrado = true;
										ent.getEjercicio().put(cod_ej, ejercicio);
										System.out.println("Entrenamiento añadido con éxito.");
									}
								}
							} catch (EOFException e) {
								finArchivo = true;
							}
						}
						ois.close();

					} catch (IOException | ClassNotFoundException e) {
						System.err.println("Error al procesar el archivo: " + e.getMessage());
						error=true;	
					}
				}
				if (!encontrado) {
					System.out.println("El código de entrenador no existe. Reintente.");
				}
			} while (!encontrado);
		}
	}

	private static void mostrarEntrenadores(File fich1) {
		ObjectInputStream ois = null;
		boolean finArchivo = false;
		try {
			ois = new ObjectInputStream(new FileInputStream(fich1));
			while (!finArchivo) {
				Staff staff = (Staff) ois.readObject();
				if (staff instanceof Entrenador) {
					staff.visualizar();
				}
			}
			ois.close();
		} catch (EOFException e) {
			finArchivo = true;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer equipos: " + e.getMessage());
		}
	} 

	private static void editarEdad() {
		String codigo = existeJugador();
		boolean operacionExitosa = false;
		String mensaje = "Operación cancelada o jugador no encontrado.";

		if (!codigo.equals("-1")) {
			// Pedimos la nueva edad
			System.out.println("Jugador encontrado: " + codigo);
			System.out.print("Introduce la nueva edad: ");
			int nuevaEdad = Utilidades.leerInt(16, 45);
			File fichJugadores = new File("Jugadores.dat");

			if (fichJugadores.exists() && fichJugadores.length() > 0) {
				// Lista temporal para guardar todos los jugadores
				ArrayList<Jugador> jugadores = new ArrayList<>();
				boolean encontrado = false;
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichJugadores))) {
					boolean finArchivo = false;
					while (!finArchivo) {
						try {
							Jugador jugador = (Jugador) ois.readObject();
							if (jugador.getCod_e().equalsIgnoreCase(codigo)) {
								jugador.setEdad(nuevaEdad);
								encontrado = true;
								mensaje = "Edad actualizada correctamente a " + nuevaEdad + " años.";
							}
							jugadores.add(jugador);
						} catch (EOFException e) {
							finArchivo = true;
						}
					}
					if (encontrado) {
						// Reescribimos el fichero
						try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichJugadores))) {
							for (Jugador j : jugadores) {
								oos.writeObject(j);
							}
							operacionExitosa = true;
							mensaje = "Cambios guardados correctamente en el fichero.\n" + mensaje;
						} catch (IOException e) {
							mensaje = "Error al guardar los cambios: " + e.getMessage();
						}
					} else {
						mensaje = "Error inesperado: el jugador ya no se encuentra en el fichero.";
					}
				} catch (IOException | ClassNotFoundException e) {
					mensaje = "Error al leer el fichero de jugadores: " + e.getMessage();
				}
			} else {
				mensaje = "No hay jugadores registrados.";
			}
		}
		System.out.println(mensaje);
	}

	private static void aniadirEntrenador(File fich1, File fich2) {
	    ObjectInputStream ois = null;
	    String nom, pais, jug_base, cod_e = null, cod;
	    LocalDate fecha;
	    double sueldo;
	    int edad, cont = 0;
	    HashMap<String, Ejercicio> ejercicios = new HashMap<>();
	    boolean encontrado = false;
	    boolean finArchivo = false, error=false;
	    boolean finCount = false;
	    int entrenadoresEnEquipo = 0;

	    System.out.println("Introduce el nombre: ");
	    nom = Utilidades.introducirCadena();
	    System.out.println("Introduce la edad: ");
	    edad = Utilidades.leerInt(18, 80);
	    System.out.println("Introduce la fecha de entrada: ");
	    fecha = Utilidades.leerFechaAMD();
	    System.out.println("Introduce el pais: ");
	    pais = Utilidades.introducirCadena();
	    System.out.println("Introduce el sueldo: ");
	    sueldo = Utilidades.leerDouble();

	    if (!fich2.exists()) {
	        System.out.println("No hay equipos registrados. No se puede añadir un entrenador.");
	    } else {

	        do {
	            System.out.println("Equipos disponibles:");
	            mostrarEquipos(fich2);

	            System.out.println("Introduce el codigo del equipo: ");
	            cod_e = Utilidades.introducirCadena();

	            try {
	                ois = new ObjectInputStream(new FileInputStream(fich2));
	                finArchivo = false; 
	                while (!finArchivo) {
	                    try {
	                        Equipo equipo = (Equipo) ois.readObject();
	                        if (equipo.getCod_e().equalsIgnoreCase(cod_e)) {
	                            encontrado = true;
	                            finArchivo = true; // Salimos del bucle
	                        }
	                    } catch (EOFException e) {
	                        finArchivo = true;
	                    }
	                }
	                ois.close();
	            } catch (IOException | ClassNotFoundException e) {
	                System.err.println("Error al leer equipos: " + e.getMessage());
	            }

	            if (!encontrado) {
	                System.out.println("El equipo no existe. Reintente.");
	            } else {
	                
	                // --- BLOQUE DE VALIDACIÓN DE ENTRENADORES MÁXIMOS ---
	                
	                if (fich1.exists()) {
	                    ObjectInputStream oisCount = null;
	                    try {
	                        oisCount = new ObjectInputStream(new FileInputStream(fich1));
	                        
	                        while (!finCount) {
	                            try {
	                                Object obj = oisCount.readObject();
	                                if (obj instanceof Entrenador) {
	                                    Entrenador trainer = (Entrenador) obj;
	                                    if (trainer.getCod_e().equalsIgnoreCase(cod_e)) {
	                                        entrenadoresEnEquipo++;
	                                    }
	                                }
	                            } catch (EOFException e) {
	                                finCount = true;
	                            }
	                        }
	                    } catch (IOException | ClassNotFoundException e) {
	                         System.err.println("Error verificando entrenadores: " + e.getMessage());
	                    } finally {
	                        try {
	                        	if (oisCount != null) {
	                        	oisCount.close();
	                        	}
	                        	} catch (IOException e) {
	                        		
	                        	}
	                    }
	                }

	                try {
	                    if (entrenadoresEnEquipo >= 3&&!error) {
	                        throw new MaxEntException();
	                    }
	                } catch (MaxEntException e) {
	                    System.out.println("ERROR: El equipo " + cod_e + " ya tiene 3 entrenadores. Debe eliminar uno antes de añadir otro.");
	                    error=true;
	                }
	            }

	        } while (!encontrado);

	        System.out.println("Introduce la jugada base: ");
	        jug_base = Utilidades.introducirCadena();

	        // Contar para generar ID (ENT - X)
	        finArchivo = false;
	        if (fich1.exists()) {
	            try {
	                ois = new ObjectInputStream(new FileInputStream(fich1));
	                while (!finArchivo) {
	                    try {
	                        ois.readObject();
	                        cont++;
	                    } catch (EOFException e) {
	                        finArchivo = true;
	                    }
	                }
	                ois.close();
	            } catch (Exception e) {
	                System.err.println("Error al contar registros: " + e.getMessage());
	            }
	        }

	        cod = "ENT - " + (cont + 1);
	        Entrenador ent = new Entrenador(cod, nom, edad, fecha, pais, sueldo, cod_e, jug_base, ejercicios);

	        try {
	            if (!fich1.exists()) {
	                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fich1))) {
	                    oos.writeObject(ent);
	                }
	            } else {
	                try (MyObjectOutputStream moos = new MyObjectOutputStream(new FileOutputStream(fich1, true))) {
	                    moos.writeObject(ent);
	                }
	            }
	            System.out.println("Entrenador añadido con éxito. Código: " + cod);
	        } catch (IOException e) {
	            System.err.println("Error al escribir: " + e.getMessage());
	        }
	    }
	}

	private static void mostrarEquipos(File fich) {
		boolean finArchivo = false;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fich))) {
			while (!finArchivo) {
				Equipo eq = (Equipo) ois.readObject();
				eq.mostrarInfo();
			}
		} catch (EOFException e) {
			finArchivo = true;
		} catch (Exception e) {
			System.err.println("Error al visualizar equipos.");
		}
	}

	private static void aniadirJugador(File fich1) {
		String nombre_s, pais, cod_e, tipo, cod_j = "JUG - ", cod_ju;
		int edad, puntos, cont = 0;
		double sueldo;
		LocalDate fechaIncor;
		ObjectOutputStream oos;
		MyObjectOutputStream moos;
		boolean finArchivo = false;
		ObjectInputStream ois = null;
		POSICION posicion = null;
		boolean correcto = true;
		int jugadoresEnEquipo = 0;
		boolean finCount = false;

		System.out.println("Introduce el nombre del Staff: ");
		nombre_s = Utilidades.introducirCadena();
		System.out.println("Introduce la edad: ");
		edad = Utilidades.leerInt();
		System.out.println("Introduce la fecha de incorporación:(A/M/D) ");
		fechaIncor = Utilidades.leerFechaAMD();
		System.out.println("Introduce el pais: ");
		pais = Utilidades.introducirCadena();
		System.out.println("Introduce el sueldo: ");
		sueldo = Utilidades.leerDouble();
		System.out.println("Introduce el codigo del equipo al que pertenece: ");
		cod_e = Utilidades.introducirCadena();


		if (fich1.exists()) {
			ObjectInputStream oisCount = null;
			try {
				oisCount = new ObjectInputStream(new FileInputStream(fich1));
				while (!finCount) {
					try {
						Staff st = (Staff) oisCount.readObject();
						if (st instanceof Jugador) {
							Jugador jug = (Jugador) st;
							if (jug.getCod_e().equalsIgnoreCase(cod_e)) {
								jugadoresEnEquipo++;
							}
						}
					} catch (EOFException e) {
						finCount = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al verificar cantidad de jugadores: " + e.getMessage());
			} finally {
				try { 
					if (oisCount != null){
					oisCount.close();
				} 
				} catch (IOException e) {
					
				}
			}
		}

		try {
			if (jugadoresEnEquipo >= 15) {
				throw new MaxJugException();
			}
		} catch (MaxJugException e) {
			System.out.println("ERROR: El equipo " + cod_e + " ya tiene 15 jugadores. No se pueden añadir más.");
			return; // Salimos del método para no guardar datos
		}

		System.out.println("Introduce los puntos del jugador: ");
		puntos = Utilidades.leerInt();
		do {
			System.out.println("Introduce la posicion del jugador: (BASE, ALERO, PIVOT)");
			tipo = Utilidades.introducirCadena();
			try {
				posicion = POSICION.valueOf(tipo);
				correcto = true;
			} catch (IllegalArgumentException e) {
				System.err.println("El valor '" + tipo + "' no es una posicion válida.");
				correcto = false;
			}
		} while (!correcto);

		// Contar total de registros para generar el ID (JUG - X)
		finArchivo = false;
		if (fich1.exists()) {
			try {
				ois = new ObjectInputStream(new FileInputStream(fich1));
				while (!finArchivo) {
					ois.readObject();
					cont++;
				}
			} catch (EOFException e) {
				finArchivo = true;
			} catch (Exception e) {
				System.err.println("Error al contar registros: " + e.getMessage());
			} finally {
				try { if (ois != null) ois.close(); } catch (IOException e) {}
			}
		}

		cod_ju = cod_j + (cont + 1);
		System.out.println("El codigo del jugador es " + cod_ju);
		Jugador j = new Jugador(cod_ju, nombre_s, edad, fechaIncor, pais, sueldo, cod_e, puntos, posicion);

		if (!fich1.exists()) {
			try {
				oos = new ObjectOutputStream(new FileOutputStream(fich1));
				oos.writeObject(j);
				oos.close();
				System.out.println("Jugador añadido correctamente.");
			} catch (IOException e) {
				System.out.println("Error escribiendo el fichero");
			}
		} else {
			try {
				moos = new MyObjectOutputStream(new FileOutputStream(fich1, true)); // true para append
				moos.writeObject(j);
				moos.close();
				System.out.println("Jugador añadido correctamente.");
			} catch (IOException e) {
				System.out.println("Error escribiendo el fichero");
			}
		}
	}

	private static String existeJugador() {
		String codJugador;
		String res = "-1"; // Variable para guardar el resultado final
		boolean encontrado = false;
		boolean finArchivo = false;
		ObjectInputStream ois = null;
		File fichJugadores = new File("Staffs.dat");

		System.out.println("Introduce el código del jugador (ej: JUG - 001):");
		codJugador = Utilidades.introducirCadena().trim();

		// Sólo entramos a buscar si el código no es vacío o "salir"
		if (!codJugador.isEmpty() && !codJugador.equalsIgnoreCase("salir")) {
			if (fichJugadores.exists() && fichJugadores.length() > 0) {
				try {
					ois = new ObjectInputStream(new FileInputStream(fichJugadores));
					while (!finArchivo && !encontrado) {
						try {
							Object obj = ois.readObject();
							if (obj instanceof Jugador) {
								Jugador j = (Jugador) obj;
								// Comparamos el código del jugador
								if (j.getCod_e().equalsIgnoreCase(codJugador)) {
									encontrado = true;
									res = codJugador; // Si lo encontramos, actualizamos res
								}
							}
						} catch (EOFException e) {
							finArchivo = true;
						}
					}
				} catch (Exception e) {
					System.err.println("Error al leer el fichero: " + e.getMessage());
				} finally {
					if (ois != null) {
						try {
							ois.close();
						} catch (IOException e) {}
					}
				}
			} else {
				System.out.println("No hay jugadores registrados todavía.");
			}
		}
		// Si al final no se encontró, devolvemos -1
		if (!encontrado && !res.equals("-1")) {
			System.out.println("El jugador con código " + codJugador + " no existe.");
		}
		return res; 
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
		ele = Utilidades.leerInt(1, 12);
		return ele;
	}

	private static void fillData(File fich2) {
		ObjectOutputStream oos = null;

		// Listado de 15 equipos de la ACB
		String[] nombres = {
				"Real Madrid", "Barca", "Unicaja", "Valencia Basket", "Lenovo Tenerife",
				"Gran Canaria", "Baskonia", "Baxi Manresa", "Joventut Badalona", "UCAM Murcia",
				"Casademont Zaragoza", "Surne Bilbao Basket", "Rio Breogan", "Coviran Granada", "MoraBanc Andorra"
		};

		try {
			// Creamos el flujo de escritura (sobrescribe si existe para empezar de cero)
			oos = new ObjectOutputStream(new FileOutputStream(fich2));

			for (String nombre : nombres) {
				// 1. Generar código: 3 primeras letras + " - 1"
				String cod = nombre.substring(0, 3).toUpperCase() + " - 1";

				// 2. Definir fecha (aleatoria entre 1920 y 2000 para variedad)
				LocalDate fecha = LocalDate.of(1920 + (int) (Math.random() * 80), 1, 1);

				// 3. Manejo del Cántico:
				// Dado que FileWriter no es serializable, enviamos null en el objeto.
				// Pero creamos físicamente el archivo del cántico para que exista.
				String nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
				File archivoCantico = new File(nombreArchivoCantico);

				if (!archivoCantico.exists()) {
					FileWriter fw = new FileWriter(archivoCantico);
					fw.write("¡Vamos, vamos " + nombre + "!");
					fw.close();
				}

				// 4. Crear el objeto Equipo
				// Importante: El constructor pide un FileWriter, pasamos null por ahora
				// para evitar errores de serialización, ya que el archivo físico ya se creó.
				Equipo eq = new Equipo(cod, nombre, fecha, null);

				// 5. Escribir el equipo 1 por 1 en el fichero .dat
				oos.writeObject(eq);
				System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");
			}

			System.out.println("\n--- Proceso finalizado: 15 equipos guardados en Equipos.dat ---");

		} catch (IOException e) {
			System.err.println("Error al procesar los equipos: " + e.getMessage());
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveDataJugadores(File fich1) {
		int i = 1; // Contador para el autoincremento del código "JUG - X"

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fich1))) {

			// --- Equipo: MOR - 1 ---
			oos.writeObject(new Jugador("JUG - " + i++, "Juan Pérez", 25, LocalDate.of(2020, 5, 10), "España", 2500,
					"MOR - 1", 150, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Luka Modric", 28, LocalDate.of(2019, 8, 15), "Croacia", 3000,
					"MOR - 1", 200, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Marc Gasol", 32, LocalDate.of(2021, 1, 20), "España", 4500,
					"MOR - 1", 300, POSICION.PIVOT));
			oos.writeObject(new Jugador("JUG - " + i++, "Ricky Rubio", 24, LocalDate.of(2022, 3, 12), "España", 2200,
					"MOR - 1", 120, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Kevin Durant", 30, LocalDate.of(2018, 11, 5), "USA", 5000,
					"MOR - 1", 450, POSICION.ALERO));

			// --- Equipo: REA - 1 ---
			oos.writeObject(new Jugador("JUG - " + i++, "Carlos Alocén", 22, LocalDate.of(2023, 6, 1), "España", 1800,
					"REA - 1", 80, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Sergio Llull", 34, LocalDate.of(2010, 9, 10), "España", 4000,
					"REA - 1", 500, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Walter Tavares", 31, LocalDate.of(2017, 10, 15), "Cabo Verde",
					4800, "REA - 1", 600, POSICION.PIVOT));
			oos.writeObject(new Jugador("JUG - " + i++, "Rudy Fernández", 36, LocalDate.of(2012, 7, 22), "España", 3500,
					"REA - 1", 350, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Mario Hezonja", 27, LocalDate.of(2022, 8, 30), "Croacia", 2900,
					"REA - 1", 210, POSICION.ALERO));

			// --- Equipo: BAR - 1 ---
			oos.writeObject(new Jugador("JUG - " + i++, "Nico Laprovittola", 33, LocalDate.of(2021, 7, 1), "Argentina",
					2700, "BAR - 1", 190, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Álex Abrines", 29, LocalDate.of(2019, 9, 12), "España", 2600,
					"BAR - 1", 175, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Willy Hernangómez", 29, LocalDate.of(2023, 7, 15), "España",
					4200, "BAR - 1", 310, POSICION.PIVOT));
			oos.writeObject(new Jugador("JUG - " + i++, "Jabari Parker", 28, LocalDate.of(2023, 8, 5), "USA", 3800,
					"BAR - 1", 240, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Tomas Satoransky", 31, LocalDate.of(2022, 6, 20), "Rep. Checa",
					3100, "BAR - 1", 160, POSICION.BASE));

			System.out.println("Escritura directa finalizada: 15 jugadores guardados en Staffs.dat");
			oos.close();

		} catch (IOException e) {
			System.err.println("Error durante la escritura: " + e.getMessage());
		}

	}

}
