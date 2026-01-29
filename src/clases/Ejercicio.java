package clases;

public class Ejercicio {
	private String cod_ej;
	private String nom_ej;
	private String descrip;
	private int dificultad; //del 1 al 5, a saber como lo  hacemos.
	private int duracion;
	
	public Ejercicio(String cod_ej, String nom_ej, String descrip, int dificultad, int duracion) {
		this.cod_ej = cod_ej;
		this.nom_ej = nom_ej;
		this.descrip = descrip;
		this.dificultad = dificultad;
		this.duracion = duracion;
	}
	
	public Ejercicio() {
		this.cod_ej = "";
		this.nom_ej = "";
		this.descrip = "";
		this.dificultad = 0;
		this.duracion = 0;
	}

	public String getCod_ej() {
		return cod_ej;
	}

	public void setCod_ej(String cod_ej) {
		this.cod_ej = cod_ej;
	}

	public String getNom_ej() {
		return nom_ej;
	}

	public void setNom_ej(String nom_ej) {
		this.nom_ej = nom_ej;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public int getDificultad() {
		return dificultad;
	}

	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
}
