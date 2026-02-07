package main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

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
					aniadirJugador(fich1, fich2);
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
					editarEdad(fich1);
					// Se mostrara un listado de tosdos los jugadores para que pueda ver los
					// codigos. Despues se le preguntara el codigo del jugador que quiera modificar.
					// Se comprobara que el codigo que ha introducido existe.
					break;
				case 5:
					eliminarJugador(fich1, fich2);
					// Se mostrara un listado de tosdos los jugadores para que pueda ver los
					// codigos. Se comprobara que el jugador exista para poder eliminarlo.
					break;
				case 6:
					infoEquipo(fich2);
					// Se mostrara la info de un equipo.
					break;
				case 7:
					clasificacion();
					// muestra la clasificacion de la liga. Se ordenara por puntos. Se sumaran los
					// puntos de los jugadores de cada equipo y asi se obtendran los puntos de
					// un equipo.
					break;
				case 8:
					mostrarStaffs(fich1);
					// muestra el jugador que mas años haya estado en un equipo.
					break;
				// Opcionales si nos vemos bn de tiempo.
				case 9:
					traspasoJugador(fich1, fich2);
					// Se mostrara un listado de tosdos los jugadores para que pueda ver los
					// codigos. Se pedira el nombr del equipo al que quiera que sea traspasado y se
					// guardara la info para que se traspase.
					break;
				case 10:
					mostrarJugadoresPos(fich1);
					// Se le pedira al usuario una de las posiciones. Se comprobara que la posicion
					// este en el enum. Se mostraran los juadores de dicha posicion
					break;
				case 11:
					jugadoresPorPuntos(fich1);
					// Se mostrara la lista de jugadores ordenados por los puntos.
					break;
				case 12:
					eliminarEntrenador(fich1);
					break;
				case 13:
					simulacionJornada(fich1, fich2);
					break;
				case 14:
					System.out.println("Agurrr");
					break;

			}

		} while (opc != 14);
	}

	public static void jugadoresPorPuntos(File fich1) {
		ArrayList<Jugador> jugadores = new ArrayList<>();
		ObjectInputStream ois = null;
		boolean finArchivo = false;

		if (!fich1.exists()) {
			System.out.println("No hay jugadores registrados.");
		} else {

			try {
				ois = new ObjectInputStream(new FileInputStream(fich1));
				while (!finArchivo) {
					try {
						Staff st = (Staff) ois.readObject();
						if (st instanceof Jugador) {
							jugadores.add((Jugador) st);
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el fichero: " + e.getMessage());
			} finally {
				try {
					if (ois != null)
						ois.close();
				} catch (IOException e) {
				}
			}

			if (jugadores.isEmpty()) {
				System.out.println("No hay jugadores para mostrar.");
			}

			Collections.sort(jugadores);
			System.out.println("\n--- JUGADORES ORDENADOS POR PUNTOS ---");
			for (Jugador j : jugadores) {
				j.visualizar();
				System.out.println("-----------------------------");
			}
		}
	}

	private static void mostrarJugadoresPos(File fich1) {
		ObjectInputStream ois = null;
		String posi = "";
		POSICION p = null;
		boolean posicionCorrecta = false;
		boolean finArchivo = false;
		Staff s = null;
		Jugador j = null;

		if (!fich1.exists()) {
			System.out.println("No hay personal registrado. No se puede comprobar los jugadores por posición.");
		} else {
			while (!posicionCorrecta) {
				System.out.println("Introduce la posicion (BASE, ALERO, PIVOT): ");
				posi = Utilidades.introducirCadena().toUpperCase();
				try {
					p = POSICION.valueOf(posi);
					posicionCorrecta = true;
				} catch (IllegalArgumentException e) {
					System.out.println("Posicion incorrecta. Intentalo de nuevo.");
				}
			}

			try {
				ois = new ObjectInputStream(new FileInputStream(fich1));
				while (!finArchivo) {
					try {
						s = (Staff) ois.readObject();
						if (s instanceof Jugador) {
							j = (Jugador) s;
							if (j.getPosicion().equals(p)) {
								j.visualizar();
								System.out.println("-----------------------------");
							}
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el archivo: " + e.getMessage());
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						System.err.println("Error al cerrar el flujo: " + e.getMessage());
					}
				}
			}

		}

	}

	private static void traspasoJugador(File fich1, File fich2) {
		String nombre = "";
		String codigo = "";
		String codigoCandidato = "";
		String nombreEquipo = "";
		String codigoEquipoActual = "";
		String nuevoCodigoEquipo = "";
		int contCoincidencias = 0;
		boolean finArchivo = false;
		boolean jugadorEncontrado = false;
		boolean equipoValido = false;
		boolean equipoEncontrado = false;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		ArrayList<Staff> listaStaff = new ArrayList<>();
		Staff st = null;
		Jugador jug = null;
		Equipo eq = null;
		int i = 0;

		if (!fich1.exists()) {
			System.out.println("No hay jugadores registrados.");
		} else {
			System.out.println("Introduce el nombre del jugador que quieras traspasar: ");
			nombre = Utilidades.introducirCadena();

			// Buscar jugadores con ese nombre
			try {
				ois = new ObjectInputStream(new FileInputStream(fich1));
				while (!finArchivo) {
					try {
						st = (Staff) ois.readObject();
						if (st instanceof Jugador) {
							jug = (Jugador) st;
							if (jug.getNom_s().equalsIgnoreCase(nombre)) {
								jug.visualizar();
								System.out.println("-----------------------------");
								contCoincidencias++;
								codigoCandidato = jug.getCod_s();
								codigoEquipoActual = jug.getCod_e();
							}
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el fichero: " + e.getMessage());
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						System.err.println("Error al cerrar flujo: " + e.getMessage());
					}
				}
			}

			if (contCoincidencias > 0) {
				// Determinar qué jugador traspasar
				if (contCoincidencias == 1) {
					System.out.println("Se ha encontrado un único jugador con ese nombre.");
					codigo = codigoCandidato;
					jugadorEncontrado = true;
				} else {
					System.out.println("Se han encontrado " + contCoincidencias + " jugadores con ese nombre.");
					System.out.println("Introduce el codigo del jugador que quieres traspasar: ");
					codigo = Utilidades.introducirCadena();

					// Verificar que el código introducido existe
					finArchivo = false;
					try {
						ois = new ObjectInputStream(new FileInputStream(fich1));
						while (!finArchivo && !jugadorEncontrado) {
							try {
								st = (Staff) ois.readObject();
								if (st instanceof Jugador) {
									jug = (Jugador) st;
									if (jug.getCod_s().equalsIgnoreCase(codigo)) {
										jugadorEncontrado = true;
										codigoEquipoActual = jug.getCod_e();
									}
								}
							} catch (EOFException e) {
								finArchivo = true;
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						System.err.println("Error al leer el fichero: " + e.getMessage());
					} finally {
						if (ois != null) {
							try {
								ois.close();
							} catch (IOException e) {
								System.err.println("Error al cerrar flujo: " + e.getMessage());
							}
						}
					}

					if (!jugadorEncontrado) {
						System.out.println("No se encontró ningún jugador con ese código.");
					}
				}

				// Si tenemos un jugador válido, pedir el equipo de destino
				while (jugadorEncontrado && !equipoValido) {
					System.out.println("Introduce el nombre del equipo al que quieres traspasar al jugador: ");
					nombreEquipo = Utilidades.introducirCadena();

					// Buscar el equipo en el fichero de equipos
					finArchivo = false;
					equipoEncontrado = false;
					nuevoCodigoEquipo = "";

					if (!fich2.exists()) {
						System.out.println("No hay equipos registrados.");
						equipoValido = true; // Salir del bucle
					} else {
						try {
							ois = new ObjectInputStream(new FileInputStream(fich2));
							while (!finArchivo) {
								try {
									eq = (Equipo) ois.readObject();
									if (eq.getNom_e().equalsIgnoreCase(nombreEquipo)) {
										equipoEncontrado = true;
										nuevoCodigoEquipo = eq.getCod_e();
										finArchivo = true;
									}
								} catch (EOFException e) {
									finArchivo = true;
								}
							}
						} catch (IOException | ClassNotFoundException e) {
							System.err.println("Error al leer el fichero de equipos: " + e.getMessage());
						} finally {
							if (ois != null) {
								try {
									ois.close();
								} catch (IOException e) {
									System.err.println("Error al cerrar flujo: " + e.getMessage());
								}
							}
						}

						if (!equipoEncontrado) {
							System.out.println("ERROR: El equipo introducido no existe. Intentalo de nuevo.");
						} else if (nuevoCodigoEquipo.equalsIgnoreCase(codigoEquipoActual)) {
							System.out.println(
									"ERROR: El jugador ya pertenece a ese equipo. Introduce un equipo diferente.");
						} else {
							equipoValido = true;
						}
					}
				}

				// Si todo es válido, realizar el traspaso
				if (jugadorEncontrado && equipoValido && equipoEncontrado) {
					finArchivo = false;
					listaStaff.clear();

					// Leer todos los staffs y modificar el jugador correspondiente
					try {
						ois = new ObjectInputStream(new FileInputStream(fich1));
						while (!finArchivo) {
							try {
								st = (Staff) ois.readObject();
								if (st instanceof Jugador) {
									jug = (Jugador) st;
									if (jug.getCod_s().equalsIgnoreCase(codigo)) {
										jug.setCod_e(nuevoCodigoEquipo);
									}
								}
								listaStaff.add(st);
							} catch (EOFException e) {
								finArchivo = true;
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						System.err.println("Error al leer el fichero: " + e.getMessage());
					} finally {
						if (ois != null) {
							try {
								ois.close();
							} catch (IOException e) {
								System.err.println("Error al cerrar flujo: " + e.getMessage());
							}
						}
					}

					// Escribir todos los staffs de vuelta al fichero
					try {
						oos = new ObjectOutputStream(new FileOutputStream(fich1));
						for (i = 0; i < listaStaff.size(); i++) {
							st = listaStaff.get(i);
							oos.writeObject(st);
						}
						System.out.println("Traspaso realizado con éxito.");
					} catch (IOException e) {
						System.err.println("Error al guardar los cambios: " + e.getMessage());
					} finally {
						if (oos != null) {
							try {
								oos.close();
							} catch (IOException e) {
								System.err.println("Error al cerrar flujo de salida: " + e.getMessage());
							}
						}
					}
				}
			} else {
				System.out.println("No se ha encontrado ningún jugador con ese nombre.");
			}
		}
	}

	private static void clasificacion() {

	}

	private static void infoEquipo(File fich2) {
		ArrayList<Equipo> equipos = new ArrayList<>();
		ObjectInputStream ois = null;
		boolean finArchivo = false;

		if (!fich2.exists()) {
			System.out.println("No hay equipos registrados.");
		} else {

			try {
				ois = new ObjectInputStream(new FileInputStream(fich2));
				while (!finArchivo) {
					try {
						Equipo eq = (Equipo) ois.readObject();
						if (eq instanceof Equipo) {
							equipos.add((Equipo) eq);
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el fichero: " + e.getMessage());
			} finally {
				try {
					if (ois != null)
						ois.close();
				} catch (IOException e) {
					System.out.println("Error al cerrar el fichero");
				}
			}

			if (equipos.isEmpty()) {
				System.out.println("No hay equipos para mostrar.");
			}

			Collections.sort(equipos);
			System.out.println("\n--- EQUIPOS ORDENADOS POR PUNTOS ---");
			for (Equipo e : equipos) {
				e.mostrarInfo();
				System.out.println("-----------------------------");
			}
		}

	}

	public static void eliminarEntrenador(File fich1) {
		String nombre = "";
		String codigo = "";
		String codigoCandidato = "";
		int contCoincidencias = 0;
		boolean encontradoCodigo = false;
		boolean finArchivo = false;
		boolean esElBuscado;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		File fichTemp = new File("Temp.dat");
		Staff st = null;
		Entrenador ent = null;

		if (fich1.exists()) {
			System.out.println("Introduce el nombre del entrenador a eliminar: ");
			nombre = Utilidades.introducirCadena();

			try {
				ois = new ObjectInputStream(new FileInputStream(fich1));
				while (!finArchivo) {
					try {
						st = (Staff) ois.readObject();
						if (st instanceof Entrenador) {
							ent = (Entrenador) st;
							if (ent.getNom_s().equalsIgnoreCase(nombre)) {
								ent.visualizar();
								contCoincidencias++;
								codigoCandidato = ent.getCod_s();
							}
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el fichero: " + e.getMessage());
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						System.err.println("Error al cerrar flujo: " + e.getMessage());
					}
				}
			}

			if (contCoincidencias > 0) {
				if (contCoincidencias == 1) {
					System.out.println("Se ha encontrado un único entrenador. Procediendo a eliminarlo...");
					codigo = codigoCandidato;
				} else {
					System.out.println("Se han encontrado " + contCoincidencias + " entrenadores con ese nombre.");
					System.out.println("Introduce el codigo del entrenador que quieres eliminar: ");
					codigo = Utilidades.introducirCadena();
				}

				finArchivo = false;

				try {
					ois = new ObjectInputStream(new FileInputStream(fich1));
					oos = new ObjectOutputStream(new FileOutputStream(fichTemp));

					while (!finArchivo) {
						try {
							st = (Staff) ois.readObject();
							esElBuscado = false;
							if (st instanceof Entrenador) {
								if (st.getCod_s().equalsIgnoreCase(codigo)) {
									esElBuscado = true;
									encontradoCodigo = true;
								}
							}

							if (!esElBuscado) {
								oos.writeObject(st);
							}

						} catch (EOFException e) {
							finArchivo = true;
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Error durante el proceso de eliminación: " + e.getMessage());
				} finally {
					if (oos != null) {
						try {
							oos.close();
						} catch (IOException e) {
							System.err.println("Error al cerrar flujo de salida: " + e.getMessage());
						}
					}
					if (ois != null) {
						try {
							ois.close();
						} catch (IOException e) {
							System.err.println("Error al cerrar flujo de entrada: " + e.getMessage());
						}
					}
				}

				if (encontradoCodigo) {
					if (fich1.delete()) {
						if (fichTemp.renameTo(fich1)) {
							System.out.println("Entrenador eliminado correctamente.");
						} else {
							System.out.println("Error al renombrar el fichero temporal.");
						}
					} else {
						System.out.println("Error al borrar el fichero original.");
					}
				} else {
					fichTemp.delete();
					System.out.println("No se encontró ningún entrenador con ese código.");
				}

			} else {
				System.out.println("No se ha encontrado ningún entrenador con ese nombre.");
			}
		} else {
			System.out.println("El fichero no existe.");
		}
	}

	private static void eliminarJugador(File fich1, File fich2) {
		String nombre = "";
		String codigo = "";
		String codigoCandidato = "";
		int contCoincidencias = 0;
		boolean encontradoCodigo = false;
		boolean finArchivo = false;
		boolean esElBuscado;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		File fichTemp = new File("Temp.dat");
		Staff st = null;
		Jugador jug = null;
		int puntosDeleted = 0;
		String codEquipoDeleted = "";

		if (fich1.exists()) {
			System.out.println("Introduce el nombre del jugador a eliminar: ");
			nombre = Utilidades.introducirCadena();

			try {
				ois = new ObjectInputStream(new FileInputStream(fich1));
				while (!finArchivo) {
					try {
						st = (Staff) ois.readObject();
						if (st instanceof Jugador) {
							jug = (Jugador) st;
							if (jug.getNom_s().equalsIgnoreCase(nombre)) {
								jug.visualizar();
								contCoincidencias++;
								codigoCandidato = jug.getCod_s();
							}
						}
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer el fichero: " + e.getMessage());
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						System.err.println("Error al cerrar flujo: " + e.getMessage());
					}
				}
			}

			if (contCoincidencias > 0) {
				if (contCoincidencias == 1) {
					System.out.println("Se ha encontrado un único jugador. Procediendo a eliminarlo...");
					codigo = codigoCandidato;
				} else {
					System.out.println("Se han encontrado " + contCoincidencias + " jugadores con ese nombre.");
					System.out.println("Introduce el codigo del jugador que quieres eliminar: ");
					codigo = Utilidades.introducirCadena();
				}

				finArchivo = false;

				try {
					ois = new ObjectInputStream(new FileInputStream(fich1));
					oos = new ObjectOutputStream(new FileOutputStream(fichTemp));

					while (!finArchivo) {
						try {
							st = (Staff) ois.readObject();
							esElBuscado = false;
							if (st instanceof Jugador) {
								if (st.getCod_s().equalsIgnoreCase(codigo)) {
									esElBuscado = true;
									encontradoCodigo = true;
									Jugador jDel = (Jugador) st;
									puntosDeleted = jDel.getPuntos();
									codEquipoDeleted = jDel.getCod_e();
								}
							}

							if (!esElBuscado) {
								oos.writeObject(st);
							}

						} catch (EOFException e) {
							finArchivo = true;
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Error durante el proceso de eliminación: " + e.getMessage());
				} finally {
					if (oos != null) {
						try {
							oos.close();
						} catch (IOException e) {
							System.err.println("Error al cerrar flujo de salida: " + e.getMessage());
						}
					}
					if (ois != null) {
						try {
							ois.close();
						} catch (IOException e) {
							System.err.println("Error al cerrar flujo de entrada: " + e.getMessage());
						}
					}
				}

				if (encontradoCodigo) {

					if (fich1.delete()) {
						if (fichTemp.renameTo(fich1)) {
							System.out.println("Jugador eliminado correctamente.");

							// Actualizar puntos del equipo (Restar)
							if (fich2.exists()) {
								ArrayList<Equipo> equipos = new ArrayList<>();
								finArchivo = false;
								Equipo eq = null;
								try {
									ois = new ObjectInputStream(new FileInputStream(fich2));
									while (!finArchivo) {
										try {
											eq = (Equipo) ois.readObject();
											if (eq.getCod_e().equalsIgnoreCase(codEquipoDeleted)) {
												eq.setTotalPuntos(eq.getTotalPuntos() - puntosDeleted);
											}
											equipos.add(eq);
										} catch (EOFException e) {
											finArchivo = true;
										}
									}
									ois.close();

									// Reescribir fichero equipos
									try (ObjectOutputStream oos2 = new ObjectOutputStream(
											new FileOutputStream(fich2))) {
										for (Equipo e : equipos) {
											oos2.writeObject(e);
										}
									}
								} catch (IOException | ClassNotFoundException e) {
									System.err.println("Error al actualizar puntos del equipo: " + e.getMessage());
								}
							}

						} else {
							System.out.println("Error al renombrar el fichero temporal.");
						}
					} else {
						System.out.println("Error al borrar el fichero original.");
					}
				} else {
					fichTemp.delete();
					System.out.println("No se encontró ningún jugador con ese código.");
				}

			} else {
				System.out.println("No se ha encontrado ningún jugador con ese nombre.");
			}
		} else {
			System.out.println("El fichero no existe.");
		}
	}

	private static void aniadirEntrenamiento(File fich1) {
		boolean encontrado = false;
		boolean finArchivo = false;
		boolean error = false;
		String cod_entrenador = "", cod_ej = "", nom = "", descripcion = "";
		int duracion = 0, dificultad = 0;
		int i = 0;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		ArrayList<Staff> listaStaff = new ArrayList<>();
		Staff st = null;
		Entrenador ent = null;
		Ejercicio ejercicio = null;

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

			ejercicio = new Ejercicio(cod_ej, nom, descripcion, dificultad, duracion);

			do {
				System.out.println("Entrenadores disponibles:");
				mostrarEntrenadores(fich1);
				System.out.println("Introduce el codigo del Entrenador al que quieres añadirle el entrenamiento: ");
				cod_entrenador = Utilidades.introducirCadena();

				finArchivo = false;
				encontrado = false;
				error = false;
				listaStaff.clear();

				try {
					ois = new ObjectInputStream(new FileInputStream(fich1));
					while (!finArchivo) {
						try {
							st = (Staff) ois.readObject();
							if (st instanceof Entrenador) {
								ent = (Entrenador) st;
								if (ent.getCod_s().equalsIgnoreCase(cod_entrenador)) {
									encontrado = true;
									ent.getEjercicio().put(cod_ej, ejercicio);
								}
							}
							listaStaff.add(st);
						} catch (EOFException e) {
							finArchivo = true;
						}
					}
					ois.close();
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Error al procesar el archivo: " + e.getMessage());
					error = true;
				}

				if (!error && encontrado) {
					try {
						oos = new ObjectOutputStream(new FileOutputStream(fich1));
						for (i = 0; i < listaStaff.size(); i++) {
							st = listaStaff.get(i);
							oos.writeObject(st);
						}
						oos.close();
						System.out.println("Entrenamiento añadido con éxito.");
					} catch (IOException e) {
						System.err.println("Error al guardar cambios: " + e.getMessage());
					}
				} else if (!encontrado) {
					System.out.println("El código de entrenador no existe. Reintente.");
				}
			} while (!encontrado);
		}
	}

	private static void mostrarEntrenadores(File fich1) {
		boolean finArchivo = false;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fich1))) {
			while (!finArchivo) {
				Staff staff = (Staff) ois.readObject();
				if (staff instanceof Entrenador) {
					staff.visualizar();
				}
			}
		} catch (EOFException e) {
			finArchivo = true;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer equipos: " + e.getMessage());
		}
	}

	private static void editarEdad(File fich1) {
		String codigo = existeJugador(fich1);
		String mensaje = "Operación cancelada o jugador no encontrado.";

		if (!codigo.equals("-1")) {
			// Pedimos la nueva edad
			System.out.println("Jugador encontrado: " + codigo);
			System.out.print("Introduce la nueva edad: ");
			int nuevaEdad = Utilidades.leerInt(16, 45);

			if (fich1.exists() && fich1.length() > 0) {
				// Lista temporal para guardar todos los jugadores
				ArrayList<Staff> jugadores = new ArrayList<>();
				boolean encontrado = false;
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fich1))) {
					boolean finArchivo = false;
					while (!finArchivo) {
						try {
							Staff sf = (Staff) ois.readObject();
							if (sf.getCod_e().equalsIgnoreCase(codigo) && sf instanceof Jugador) {
								sf.setEdad(nuevaEdad);
								encontrado = true;
								mensaje = "Edad actualizada correctamente a " + nuevaEdad + " años.";
							}
							jugadores.add((Staff) sf);
						} catch (EOFException e) {
							finArchivo = true;
						}
					}
					if (encontrado) {
						// Reescribimos el fichero
						try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fich1))) {
							for (Staff j : jugadores) {
								oos.writeObject(j);
							}
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
		boolean finArchivo = false, error = false;
		boolean finCount = false;
		int entrenadoresEnEquipo = 0;

		System.out.println("Introduce el nombre: ");
		nom = Utilidades.introducirCadena();
		System.out.println("Introduce la edad: ");
		edad = Utilidades.leerInt(18, 80);
		System.out.println("Introduce la fecha de entrada: ");
		fecha = Utilidades.leerFechaAMD();
		while ((edad - 16) < (2026 - fecha.getYear())) {
			System.out.println("Error, La fecha de incorporación no puede ser anterior a los 16 años de edad.");
			System.out.println("Introduce la fecha de incorporación:(A/M/D) ");
			fecha = Utilidades.leerFechaAMD();
		}
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
								finArchivo = true;
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
						if (entrenadoresEnEquipo >= 3 && !error) {
							throw new MaxEntException("El equipo " + cod_e
									+ " ya tiene 3 entrenadores. Debe eliminar uno antes de añadir otro.");
						}
					} catch (MaxEntException e) {
						System.out.println(e.getMessage());
						error = true;
					}
				}

			} while (!encontrado);

			System.out.println("Introduce la jugada base: ");
			jug_base = Utilidades.introducirCadena();
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

	private static void aniadirJugador(File fich1, File fich2) {
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
		boolean continuar = true;
		boolean encontrado = false;

		System.out.println("Introduce el nombre del Jugador: ");
		nombre_s = Utilidades.introducirCadena();
		System.out.println("Introduce la edad: ");
		edad = Utilidades.leerInt(16, 70);
		System.out.println("Introduce la fecha de incorporación:(A/M/D) ");
		fechaIncor = Utilidades.leerFechaAMD();
		while ((edad - 16) < (2026 - fechaIncor.getYear())) {
			System.out.println("Error, La fecha de incorporación no puede ser anterior a los 16 años de edad.");
			System.out.println("Introduce la fecha de incorporación:(A/M/D) ");
			fechaIncor = Utilidades.leerFechaAMD();
		}
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
								finArchivo = true;
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
								if (oisCount != null) {
									oisCount.close();
								}
							} catch (IOException e) {

							}
						}
					}

					try {
						if (jugadoresEnEquipo >= 15) {
							throw new MaxJugException(
									"El equipo " + cod_e + " ya tiene 15 jugadores. No se pueden añadir más.");
						}
					} catch (MaxJugException e) {
						System.out.println(e.getMessage());
						continuar = false;
					}
				}
			} while (!encontrado);

			if (continuar) {
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
						try {
							if (ois != null)
								ois.close();
						} catch (IOException e) {
						}
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
						moos = new MyObjectOutputStream(new FileOutputStream(fich1, true));
						moos.writeObject(j);
						moos.close();
						System.out.println("Jugador añadido correctamente.");
					} catch (IOException e) {
						System.out.println("Error escribiendo el fichero");
					}
				}

				ArrayList<Equipo> equipos = new ArrayList<>();
				finArchivo = false;
				Equipo eq = null;

				if (fich2.exists()) {
					try {
						ois = new ObjectInputStream(new FileInputStream(fich2));
						while (!finArchivo) {
							try {
								eq = (Equipo) ois.readObject();
								if (eq.getCod_e().equalsIgnoreCase(cod_e)) {
									eq.setTotalPuntos(eq.getTotalPuntos() + puntos);
								}
								equipos.add(eq);
							} catch (EOFException e) {
								finArchivo = true;
							}
						}
						ois.close();

						try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(fich2))) {
							for (Equipo e : equipos) {
								oos2.writeObject(e);
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						System.err.println("Error al actualizar puntos del equipo: " + e.getMessage());
					}
				}
			}
		}
	}

	private static String existeJugador(File fich1) {
		String codJugador;
		String res = "-1"; // Variable para guardar el resultado final
		boolean encontrado = false;
		boolean finArchivo = false;
		ObjectInputStream ois = null;

		System.out.println("Introduce el código del jugador (ej: JUG - 001):");
		codJugador = Utilidades.introducirCadena();

		// Sólo entramos a buscar si el código no es vacío o "salir"
		if (!codJugador.isEmpty() && !codJugador.equalsIgnoreCase("salir")) {
			if (fich1.exists() && fich1.length() > 0) {
				try {
					ois = new ObjectInputStream(new FileInputStream(fich1));
					while (!finArchivo && !encontrado) {
						try {
							Staff sf = (Staff) ois.readObject();
							if (sf instanceof Jugador) {
								Jugador j = (Jugador) sf;
								// Comparamos el código del jugador
								if (j.getCod_s().equalsIgnoreCase(codJugador)) {
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
						} catch (IOException e) {
						}
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
		System.out.println("8. Mostrar Staffs");
		System.out.println("9. Traspaso de jugador");
		System.out.println("10. Mostrar todos los jugadores de una posicion");
		System.out.println("11. Mostrar jugadores ordenados por puntos");
		System.out.println("12. Eliminar entrenador");
		System.out.println("13. Simulacion de Jornada");
		System.out.println("14. Salir");
		System.out.println("¿Qué quieres hacer?");
		ele = Utilidades.leerInt(1, 14);
		return ele;
	}

	private static void fillData(File fich2) {
		ObjectOutputStream oos = null;

		try {
			// Creamos el flujo de escritura (sobrescribe si existe para empezar de cero)
			oos = new ObjectOutputStream(new FileOutputStream(fich2));
			Equipo eq;
			String nombre;
			String cod;
			String nombreArchivoCantico;
			File archivoCantico;
			FileWriter fw;
			LocalDate fecha;

			// --- MoraBanc Andorra ---
			nombre = "MoraBanc Andorra";
			cod = "MOR - 1";
			fecha = LocalDate.of(1970, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(150 + 200 + 300 + 120 + 450);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Real Madrid ---
			nombre = "Real Madrid";
			cod = "REA - 1";
			fecha = LocalDate.of(1931, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(80 + 500 + 600 + 350 + 210);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Barca ---
			nombre = "Barca";
			cod = "BAR - 1";
			fecha = LocalDate.of(1926, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(190 + 175 + 310 + 240 + 160);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Unicaja ---
			nombre = "Unicaja";
			cod = "UNI - 1";
			fecha = LocalDate.of(1977, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(130 + 180 + 140 + 150 + 170);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Valencia Basket ---
			nombre = "Valencia Basket";
			cod = "VAL - 1";
			fecha = LocalDate.of(1986, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(220 + 160 + 110 + 130 + 250);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Lenovo Tenerife ---
			nombre = "Lenovo Tenerife";
			cod = "LEN - 1";
			fecha = LocalDate.of(1939, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(200 + 150 + 160 + 140 + 280);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Gran Canaria ---
			nombre = "Gran Canaria";
			cod = "GRA - 1";
			fecha = LocalDate.of(1963, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(140 + 130 + 180 + 150 + 160);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Baskonia ---
			nombre = "Baskonia";
			cod = "BAS - 1";
			fecha = LocalDate.of(1959, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(250 + 180 + 170 + 160 + 210);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Baxi Manresa ---
			nombre = "Baxi Manresa";
			cod = "BAX - 1";
			fecha = LocalDate.of(1931, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(140 + 150 + 90 + 190 + 160);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Joventut Badalona ---
			nombre = "Joventut Badalona";
			cod = "JOV - 1";
			fecha = LocalDate.of(1930, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(180 + 140 + 100 + 90 + 250);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- UCAM Murcia ---
			nombre = "UCAM Murcia";
			cod = "UCA - 1";
			fecha = LocalDate.of(1985, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(160 + 190 + 170 + 150 + 210);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Casademont Zaragoza ---
			nombre = "Casademont Zaragoza";
			cod = "CAS - 1";
			fecha = LocalDate.of(2002, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(160 + 150 + 100 + 140 + 200);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Surne Bilbao Basket ---
			nombre = "Surne Bilbao Basket";
			cod = "SUR - 1";
			fecha = LocalDate.of(2000, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(170 + 180 + 130 + 120 + 190);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Rio Breogan ---
			nombre = "Rio Breogan";
			cod = "RIO - 1";
			fecha = LocalDate.of(1966, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(140 + 130 + 150 + 140 + 160);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Coviran Granada ---
			nombre = "Coviran Granada";
			cod = "COV - 1";
			fecha = LocalDate.of(2012, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(150 + 130 + 140 + 160 + 200);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			// --- Bilbao Basket ---
			nombre = "Bilbao Basket";
			cod = "BIL - 1";
			fecha = LocalDate.of(2000, 1, 1);
			nombreArchivoCantico = nombre.replace(" ", "_") + "_Cantico.txt";
			archivoCantico = new File(nombreArchivoCantico);
			if (!archivoCantico.exists()) {
				fw = new FileWriter(archivoCantico);
				fw.write("¡Vamos, vamos " + nombre + "!");
				fw.close();
			}
			eq = new Equipo(cod, nombre, fecha, nombreArchivoCantico);
			eq.setTotalPuntos(1600 + 150 + 1300 + 140 + 1200);
			oos.writeObject(eq);
			System.out.println("Equipo añadido: " + nombre + " [" + cod + "]");

			System.out.println("\n--- Proceso finalizado: 16 equipos guardados en Equipos.dat ---");

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

	public static void simulacionJornada(File fich1, File fich2) {
		ArrayList<Equipo> equipos = new ArrayList<>();
		ArrayList<Jugador> jugadoresEquipo1 = new ArrayList<>();
		ArrayList<Jugador> jugadoresEquipo2 = new ArrayList<>();
		ObjectInputStream ois = null;
		Staff st = null;
		Jugador jug = null;
		Equipo eq = null;
		Equipo equipo1 = null;
		Equipo equipo2 = null;
		Random random = new Random();
		int puntosEquipo1 = 0;
		int puntosEquipo2 = 0;
		int puntosJugador = 0;
		int i = 0;
		int j = 0;
		int numPartido = 1;
		boolean finArchivo = false;
		boolean procesarJornada = false;

		// 1. Leer todos los equipos del fichero
		if (fich2.exists()) {
			try {
				ois = new ObjectInputStream(new FileInputStream(fich2));
				while (!finArchivo) {
					try {
						eq = (Equipo) ois.readObject();
						equipos.add(eq);
					} catch (EOFException e) {
						finArchivo = true;
					}
				}
				procesarJornada = true;
			} catch (IOException | ClassNotFoundException e) {
				System.err.println("Error al leer los equipos: " + e.getMessage());
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
						System.err.println("Error al cerrar flujo: " + e.getMessage());
					}
				}
			}
		} else {
			System.out.println("No hay equipos registrados.");
		}

		if (procesarJornada) {
			if (equipos.size() != 16) {
				System.out.println("Se necesitan exactamente 16 equipos para la simulación. Hay: " + equipos.size());
			} else {
				// 3. Mezclar aleatoriamente los equipos
				Collections.shuffle(equipos);
				System.out.println("\n========================================");
				System.out.println("     SIMULACIÓN DE JORNADA");
				System.out.println("========================================\n");

				// 4. Enfrentar equipos de 2 en 2 (8 partidos)
				i = 0;
				while (i < equipos.size()) {
					equipo1 = equipos.get(i);
					equipo2 = equipos.get(i + 1);
					puntosEquipo1 = 0;
					puntosEquipo2 = 0;
					jugadoresEquipo1.clear();
					jugadoresEquipo2.clear();

					// Leer jugadores de ambos equipos
					finArchivo = false;
					try {
						if (fich1.exists()) {
							ois = new ObjectInputStream(new FileInputStream(fich1));
							while (!finArchivo) {
								try {
									st = (Staff) ois.readObject();
									if (st instanceof Jugador) {
										jug = (Jugador) st;
										if (jug.getCod_e().equalsIgnoreCase(equipo1.getCod_e())) {
											jugadoresEquipo1.add(jug);
										} else if (jug.getCod_e().equalsIgnoreCase(equipo2.getCod_e())) {
											jugadoresEquipo2.add(jug);
										}
									}
								} catch (EOFException e) {
									finArchivo = true;
								}
							}
						}
					} catch (IOException | ClassNotFoundException e) {
						System.err.println("Error al leer jugadores: " + e.getMessage());
					} finally {
						if (ois != null) {
							try {
								ois.close();
							} catch (IOException e) {
								System.err.println("Error al cerrar flujo: " + e.getMessage());
							}
						}
					}

					// Calcular puntos aleatorios para el equipo 1 y sumarlos al total del equipo
					j = 0;
					while (j < jugadoresEquipo1.size()) {
						puntosJugador = random.nextInt(101); // 0 a 100
						puntosEquipo1 = puntosEquipo1 + puntosJugador;
						// Sumar puntos al total histórico del equipo
						equipo1.setTotalPuntos(equipo1.getTotalPuntos() + puntosJugador);
						j++;
					}

					// Calcular puntos aleatorios para el equipo 2 y sumarlos al total del equipo
					j = 0;
					while (j < jugadoresEquipo2.size()) {
						puntosJugador = random.nextInt(101); // 0 a 100
						puntosEquipo2 = puntosEquipo2 + puntosJugador;
						// Sumar puntos al total histórico del equipo
						equipo2.setTotalPuntos(equipo2.getTotalPuntos() + puntosJugador);
						j++;
					}

					// Mostrar resultado del partido
					System.out.println("PARTIDO " + numPartido);
					System.out.println(equipo1.getNom_e() + " " + puntosEquipo1 + " - " + puntosEquipo2 + " "
							+ equipo2.getNom_e());

					// Determinar ganador y asignar puntos de clasificación
					if (puntosEquipo1 > puntosEquipo2) {
						equipo1.setTotalPuntos(equipo1.getTotalPuntos() + 2);
						System.out.println("Ganador: " + equipo1.getNom_e() + " (+2 puntos)");
					} else if (puntosEquipo2 > puntosEquipo1) {
						equipo2.setTotalPuntos(equipo2.getTotalPuntos() + 2);
						System.out.println("Ganador: " + equipo2.getNom_e() + " (+2 puntos)");
					} else {
						equipo1.setTotalPuntos(equipo1.getTotalPuntos() + 1);
						equipo2.setTotalPuntos(equipo2.getTotalPuntos() + 1);
						System.out.println("Empate (+1 punto cada uno)");
					}
					System.out.println("----------------------------------------");

					numPartido++;
					i = i + 2;
				}

				// 5. Mostrar clasificación final
				Collections.sort(equipos);
				System.out.println("\n========================================");
				System.out.println("     CLASIFICACIÓN TRAS LA JORNADA");
				System.out.println("========================================");
				i = 1;
				j = 0;
				while (j < equipos.size()) {
					eq = equipos.get(j);
					System.out.println(i + ". " + eq.getNom_e() + " - " + eq.getTotalPuntos() + " pts");
					i++;
					j++;
				}
			}
		}
	}

	public static void saveDataJugadores(File fich1) {
		int i = 1; // Contador para el autoincremento del código "JUG - X"
		int k = 1; // Contador para el autoincremento del código "ENT - X"

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

			// --- Equipo: UNI - 1 (Unicaja) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Alberto Díaz", 29, LocalDate.of(2016, 7, 1), "España", 2100,
					"UNI - 1", 130, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Kendrick Perry", 30, LocalDate.of(2022, 6, 15), "USA", 2400,
					"UNI - 1", 180, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Nihad Djedovic", 33, LocalDate.of(2022, 7, 10), "Bosnia", 2000,
					"UNI - 1", 140, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Melvin Ejim", 32, LocalDate.of(2022, 7, 12), "Canadá", 1900,
					"UNI - 1", 150, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "David Kravish", 30, LocalDate.of(2022, 6, 25), "USA", 2200,
					"UNI - 1", 170, POSICION.PIVOT));

			// --- Equipo: VAL - 1 (Valencia) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Chris Jones", 30, LocalDate.of(2022, 6, 20), "USA", 2800,
					"VAL - 1", 220, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Jared Harper", 26, LocalDate.of(2022, 7, 15), "USA", 2300,
					"VAL - 1", 160, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Josep Puerto", 24, LocalDate.of(2017, 8, 1), "España", 1500,
					"VAL - 1", 110, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Víctor Claver", 35, LocalDate.of(2021, 7, 15), "España", 2000,
					"VAL - 1", 130, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Brandon Davies", 32, LocalDate.of(2023, 7, 5), "USA", 3200,
					"VAL - 1", 250, POSICION.PIVOT));

			// --- Equipo: LEN - 1 (Tenerife) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Marcelinho Huertas", 40, LocalDate.of(2019, 7, 1), "Brasil",
					2500, "LEN - 1", 200, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Bruno Fitipaldo", 32, LocalDate.of(2020, 7, 10), "Uruguay",
					1800, "LEN - 1", 150, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Sasu Salin", 32, LocalDate.of(2019, 7, 12), "Finlandia", 1700,
					"LEN - 1", 160, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Aaron Doornekamp", 37, LocalDate.of(2020, 7, 15), "Canadá",
					1600, "LEN - 1", 140, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Giorgi Shermadini", 34, LocalDate.of(2019, 7, 5), "Georgia",
					3000, "LEN - 1", 280, POSICION.PIVOT));

			// --- Equipo: GRA - 1 (Gran Canaria) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Andrew Albicy", 33, LocalDate.of(2020, 7, 1), "Francia", 2000,
					"GRA - 1", 140, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Ferran Bassas", 31, LocalDate.of(2022, 7, 10), "España", 1700,
					"GRA - 1", 130, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Nico Brussino", 30, LocalDate.of(2021, 7, 5), "Argentina",
					2100, "GRA - 1", 180, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "John Shurna", 33, LocalDate.of(2019, 7, 20), "USA", 1900,
					"GRA - 1", 150, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Ben Lammers", 27, LocalDate.of(2023, 7, 15), "USA", 2200,
					"GRA - 1", 160, POSICION.PIVOT));

			// --- Equipo: BAS - 1 (Baskonia) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Markus Howard", 24, LocalDate.of(2022, 7, 15), "USA", 3500,
					"BAS - 1", 250, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Codi Miller-McIntyre", 29, LocalDate.of(2023, 7, 10), "USA",
					2800, "BAS - 1", 180, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Tadas Sedekerskis", 25, LocalDate.of(2018, 8, 1), "Lituania",
					2200, "BAS - 1", 170, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Vanja Marinkovic", 26, LocalDate.of(2021, 7, 15), "Serbia",
					2000, "BAS - 1", 160, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Matt Costello", 30, LocalDate.of(2021, 7, 10), "C. Marfil",
					2600, "BAS - 1", 210, POSICION.PIVOT));

			// --- Equipo: BAX - 1 (Manresa) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Dani Pérez", 33, LocalDate.of(2019, 7, 1), "España", 1600,
					"BAX - 1", 140, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Brancou Badio", 24, LocalDate.of(2022, 2, 1), "Senegal", 1500,
					"BAX - 1", 150, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Musa Sagnia", 20, LocalDate.of(2022, 8, 1), "Gambia", 1000,
					"BAX - 1", 90, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Devin Robinson", 28, LocalDate.of(2022, 7, 15), "USA", 2100,
					"BAX - 1", 190, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Martinas Geben", 29, LocalDate.of(2023, 1, 15), "Lituania",
					1800, "BAX - 1", 160, POSICION.PIVOT));

			// --- Equipo: JOV - 1 (Joventut) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Andrés Feliz", 26, LocalDate.of(2021, 7, 10), "Rep. Dom.",
					2300, "JOV - 1", 180, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Guillem Vives", 30, LocalDate.of(2021, 7, 5), "España", 1900,
					"JOV - 1", 140, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Pep Busquets", 24, LocalDate.of(2019, 8, 1), "España", 1200,
					"JOV - 1", 100, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Yannick Kraag", 21, LocalDate.of(2020, 9, 10), "Países Bajos",
					1100, "JOV - 1", 90, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Ante Tomic", 36, LocalDate.of(2020, 7, 1), "Croacia", 3500,
					"JOV - 1", 250, POSICION.PIVOT));

			// --- Equipo: UCA - 1 (Murcia) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Ludde Hakanson", 27, LocalDate.of(2023, 7, 1), "Suecia", 2100,
					"UCA - 1", 160, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Dylan Ennis", 32, LocalDate.of(2023, 6, 25), "Canadá", 2400,
					"UCA - 1", 190, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Howard Sant-Roos", 32, LocalDate.of(2023, 7, 20), "Cuba", 2300,
					"UCA - 1", 170, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Rodions Kurucs", 25, LocalDate.of(2023, 7, 25), "Letonia",
					2000, "UCA - 1", 150, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Simon Birgander", 25, LocalDate.of(2023, 7, 1), "Suecia", 2200,
					"UCA - 1", 210, POSICION.PIVOT));

			// --- Equipo: CAS - 1 (Zaragoza) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Trae Bell-Haynes", 28, LocalDate.of(2023, 7, 15), "Canadá",
					2000, "CAS - 1", 160, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Santi Yusta", 26, LocalDate.of(2021, 7, 10), "España", 1800,
					"CAS - 1", 150, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Miguel González", 24, LocalDate.of(2023, 7, 20), "España",
					1400, "CAS - 1", 100, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Emir Sulejmanovic", 28, LocalDate.of(2023, 7, 5), "Bosnia",
					1900, "CAS - 1", 140, POSICION.PIVOT));
			oos.writeObject(new Jugador("JUG - " + i++, "Jahlil Okafor", 27, LocalDate.of(2023, 8, 1), "Nigeria", 3000,
					"CAS - 1", 200, POSICION.PIVOT));

			// --- Equipo: SUR - 1 (Bilbao) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Alex Renfroe", 37, LocalDate.of(2023, 7, 1), "USA", 2100,
					"SUR - 1", 170, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Adam Smith", 30, LocalDate.of(2022, 7, 15), "USA", 2200,
					"SUR - 1", 180, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Xavi Rabaseda", 34, LocalDate.of(2022, 7, 10), "España", 1800,
					"SUR - 1", 130, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Denzel Andersson", 26, LocalDate.of(2022, 7, 20), "Suecia",
					1600, "SUR - 1", 120, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Sacha Killeya-Jones", 25, LocalDate.of(2023, 7, 5), "UK", 2300,
					"SUR - 1", 190, POSICION.PIVOT));

			// --- Equipo: RIO - 1 (Breogan) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Sergi García", 26, LocalDate.of(2022, 7, 1), "España", 1700,
					"RIO - 1", 140, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Stefan Momirov", 23, LocalDate.of(2022, 7, 10), "Serbia", 1600,
					"RIO - 1", 130, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Matas Jogela", 25, LocalDate.of(2023, 7, 15), "Lituania", 1800,
					"RIO - 1", 150, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Toni Nakic", 24, LocalDate.of(2022, 7, 20), "Croacia", 1700,
					"RIO - 1", 140, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Martynas Sajus", 27, LocalDate.of(2023, 7, 5), "Lituania",
					2000, "RIO - 1", 160, POSICION.PIVOT));

			// --- Equipo: COV - 1 (Granada) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Lluís Costa", 30, LocalDate.of(2020, 7, 1), "España", 1800,
					"COV - 1", 150, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Christian Díaz", 31, LocalDate.of(2020, 7, 5), "España", 1600,
					"COV - 1", 130, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Pere Tomàs", 33, LocalDate.of(2021, 7, 10), "España", 1700,
					"COV - 1", 140, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Kwan Cheatham", 27, LocalDate.of(2023, 7, 15), "USA", 1900,
					"COV - 1", 160, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Cristiano Felicio", 31, LocalDate.of(2022, 8, 1), "Brasil",
					2500, "COV - 1", 200, POSICION.PIVOT));

			// --- Equipo: BIL - 1 (Bilbao Basket) ---
			oos.writeObject(new Jugador("JUG - " + i++, "Alex Mumbrú", 44, LocalDate.of(2019, 7, 1), "España", 2000,
					"BIL - 1", 1600, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Adam Smith", 31, LocalDate.of(2022, 7, 10), "USA", 1800,
					"BIL - 1", 150, POSICION.BASE));
			oos.writeObject(new Jugador("JUG - " + i++, "Tomeu Rigo", 28, LocalDate.of(2023, 7, 15), "España", 1600,
					"BIL - 1", 1300, POSICION.BASE));
			oos.writeObject(
					new Jugador("JUG - " + i++, "Tyson Pérez", 25, LocalDate.of(2022, 7, 20), "República Dominicana",
							1700, "BIL - 1", 140, POSICION.ALERO));
			oos.writeObject(new Jugador("JUG - " + i++, "Sacha Katichenko", 22, LocalDate.of(2023, 7, 5), "Francia",
					1500, "BIL - 1", 1200, POSICION.PIVOT));

			// --- ENTRENADORES ---
			oos.writeObject(new Entrenador("ENT - " + k++, "Natxo Lezcano", 50, LocalDate.of(2022, 6, 1), "España",
					50000, "MOR - 1", "Defensa Zonal", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Chus Mateo", 54, LocalDate.of(2022, 7, 1), "España", 80000,
					"REA - 1", "Ataque Rápido", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Roger Grimau", 45, LocalDate.of(2023, 6, 26), "España",
					70000, "BAR - 1", "Pick and Roll", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Ibon Navarro", 47, LocalDate.of(2022, 2, 1), "España",
					55000, "UNI - 1", "Presión Alta", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Álex Mumbrú", 44, LocalDate.of(2022, 6, 14), "España",
					55000, "VAL - 1", "Juego Interior", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Txus Vidorreta", 57, LocalDate.of(2018, 6, 1), "España",
					60000, "LEN - 1", "Tiro Exterior", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Jaka Lakovic", 45, LocalDate.of(2022, 6, 17), "Eslovenia",
					58000, "GRA - 1", "Transición", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Joan Peñarroya", 54, LocalDate.of(2022, 6, 1), "España",
					65000, "BAS - 1", "Ritmo Alto", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Pedro Martínez", 62, LocalDate.of(2019, 6, 24), "España",
					60000, "BAX - 1", "Juego Dinámico", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Carles Duran", 47, LocalDate.of(2018, 2, 7), "España",
					55000, "JOV - 1", "Cantera", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Sito Alonso", 47, LocalDate.of(2019, 1, 28), "España",
					50000, "UCA - 1", "Defensa Agresiva", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Porfirio Fisac", 58, LocalDate.of(2022, 10, 1), "España",
					55000, "CAS - 1", "Táctica", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Jaume Ponsarnau", 52, LocalDate.of(2022, 6, 20), "España",
					50000, "SUR - 1", "Orden", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Veljko Mrsic", 52, LocalDate.of(2022, 1, 21), "Croacia",
					48000, "RIO - 1", "Físico", new HashMap<>()));
			oos.writeObject(new Entrenador("ENT - " + k++, "Pablo Pin", 40, LocalDate.of(2012, 8, 1), "España", 45000,
					"COV - 1", "Continuidad", new HashMap<>()));
			oos.writeObject(
					new Entrenador("ENT - " + k++, "Jose Luis Mendilibar", 52, LocalDate.of(2022, 6, 20), "España",
							50000, "BIL - 1", "Orden", new HashMap<>()));

			System.out.println("Escritura directa finalizada: 75 jugadores y 15 entrenadores guardados en Staffs.dat");
			oos.close();

		} catch (IOException e) {
			System.err.println("Error durante la escritura: " + e.getMessage());
		}

	}

	public static void mostrarStaffs(File fich1) {
		ObjectInputStream ois = null;
		boolean finArchivo = false;
		try {
			ois = new ObjectInputStream(new FileInputStream(fich1));
			while (!finArchivo) {
				Staff st = (Staff) ois.readObject();
				st.visualizar();
			}

			ois.close();
		} catch (EOFException e) {
			finArchivo = true;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al leer equipos: " + e.getMessage());
		}
	}

}
