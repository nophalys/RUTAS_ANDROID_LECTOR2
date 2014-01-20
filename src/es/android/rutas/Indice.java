package es.android.rutas;

import java.io.Serializable;

public class Indice implements Serializable {
		// Cadena con la clave de un registro del indice
	private String clave;
		// Entero con la posicion relativa a la estructura utilizada
	private int indice;
		// Version del indice para la serializacion
	public static final long serialVersionUID = 987654321;
	
	/**
	 * Constructor por defecto del indice
	 */
	public Indice() {
		clave = "";
		indice = -1;
	}
	
	/**
	 * Establece el valor de la clave del indice
	 * 
	 * @param clave cadena con la clave de un registro del indice
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	/**
	 * Devuelve el valor de la clave del indice
	 * 
	 * @return cadena con la clave de un registro del indice
	 */
	public String getClave() {
		return clave;
	}
	
	/**
	 * Establece el valor de indexacion
	 * 
	 * @param indice entero que representa el orden relativo a la estructura que se utilice
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}
	
	/**
	 * Devuelve el valor de indexacion
	 * 
	 * @return entero que representa el orden relativo a la estructura que se utilice
	 */
	public int getIndice() {
		return indice;
	}
}
