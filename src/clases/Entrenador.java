package clases;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Entrenador extends Staff{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jugadaBase;
	private HashMap <String, Ejercicio> Ejercicio;
	
	public Entrenador(String cod_s, String nom_s, int edad, LocalDate fechaIncor, String pais, double sueldo,
			String jugadaBase, HashMap<String, clases.Ejercicio> ejercicio) {
		super(cod_s, nom_s, edad, fechaIncor, pais, sueldo);
		this.jugadaBase = jugadaBase;
		Ejercicio = ejercicio;
	}

	@Override
	public void visualizar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int calcularAntiguedad() {
		// TODO Auto-generated method stub
		return 0;
	}

}
