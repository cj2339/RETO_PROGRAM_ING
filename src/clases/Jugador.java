package clases;

import java.time.LocalDate;

public class Jugador extends Staff implements Comparable<Jugador> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int puntos;
	private POSICION posicion;

	public Jugador(String cod_s, String nom_s, int edad, LocalDate fechaIncor, String pais, double sueldo, String cod_e,
			int puntos,
			POSICION posicion) {
		super(cod_s, nom_s, edad, fechaIncor, pais, sueldo, cod_e);
		this.puntos = puntos;
		this.posicion = posicion;
	}

	public Jugador() {
		super();
		this.puntos = 0;
		this.posicion = null;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public POSICION getPosicion() {
		return posicion;
	}

	public void setPosicion(POSICION posicion) {
		this.posicion = posicion;
	}

	@Override
	public void visualizar() {
		System.out.println("Codigo: " + getCod_s());
		System.out.println("Nombre: " + getNom_s());
		System.out.println("Edad: " + getEdad());
		System.out.println("Fecha de incorporacion: " + getFechaIncor());
		System.out.println("Pais: " + getPais());
		System.out.println("Sueldo: " + getSueldo());
		System.out.println("Codigo de equioo: " + getCod_e());
		System.out.println("Puntos: " + getPuntos());
		System.out.println("Posicion: " + getPosicion());

	}
	@Override
    public int compareTo(Jugador otro) {
        return Integer.compare(otro.getPuntos(), this.puntos);
    }
}
