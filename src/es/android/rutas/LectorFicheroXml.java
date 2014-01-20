package es.android.rutas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
//import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.location.Address;

import com.google.android.maps.GeoPoint;

public class LectorFicheroXml implements Serializable {
		// Documento XML en memoria cargado desde fichero
	private Document documentoXML;
		// Ruta del documento XML
	private String ruta;
		// Indice de los nombres y alias de las rutas que aparecen en el fichero
	private List<Indice> indiceNombre;
		// Indice de las rutas por categoria
	private List<Indice> indiceCategoria;
		// Indica si la busqueda de localizacion es un geopoint
	private transient boolean hayGeopoint;
		// Indica si la busqueda de localizacion es una direccion
	private transient boolean hayDirecciones;
		// Numero de version para que la serializacion pueda identificar distintas versiones
	public static final long serialVersionUID = 1234567890;
	
	/**
	 * Constructor que inicializara un documento XML desde un archivo dado.
	 * 
	 * @param rutaArchivo ruta del archivo XML en la memoria del movil
	 */
    public LectorFicheroXml(String rutaArchivo) {
		try {
			this.ruta = rutaArchivo;
			documentoXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(ruta));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		hayGeopoint = false;
		hayDirecciones = false;
		
		indexar();
    }
    
    private void writeObject(ObjectOutputStream salida) throws IOException {
    	salida.writeObject(ruta);
    	salida.writeObject(new Integer(indiceNombre.size()));
    	for (int i = 0; i < indiceNombre.size(); i ++) {
    		salida.writeObject(indiceNombre.get(i));
    	}
    	
    	salida.writeObject(new Integer(indiceCategoria.size()));
    	for (int i = 0; i < indiceCategoria.size(); i ++) {
    		salida.writeObject(indiceCategoria.get(i));
    	}
    }
    
    private void readObject(ObjectInputStream entrada) throws IOException, ClassNotFoundException {
    	Integer nroElementos;
    	
    	ruta = (String) entrada.readObject();
    	nroElementos = (Integer) entrada.readObject();
    	indiceNombre = new ArrayList<Indice>();
    	for (int i = 0; i < nroElementos; i ++) {
    		indiceNombre.add((Indice) entrada.readObject());
    	}
    	
    	nroElementos = (Integer) entrada.readObject();
    	indiceCategoria = new ArrayList<Indice>();
    	for (int i = 0; i < nroElementos; i ++) {
    		indiceCategoria.add((Indice) entrada.readObject());
    	}
    	
    	try {
			documentoXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(ruta));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Despu�s de la b�squeda de localizaciones, mediante los m�todos buscarLocalizaciones 
     * o buscarLocalizacion, se devuelve si lo que se ley� eran puntos geogr�ficos 
     * (GeoPoint)
     * 
     * @return true si hay puntos geogr�ficos, false en caso contrario
     */
    public boolean hayGeopoint() {
    	return hayGeopoint;
    }
    
    /**
     * Despu�s de la b�squeda de localizaciones, mediante los m�todos buscarLocalizaciones 
     * o buscarLocalizacion, se devuelve si lo que se ley� era una localizaci�n
     * 
     * @return true si se recuper� una o varias direcciones, false en caso contrario
     */
    public boolean hayDirecciones() {
    	return hayDirecciones;
    }
    
    /**
     * Recopila un subconjunto de nombres de ruta que pertenecen a una categor�a dada
     * 
     * @param categoria categor�a por la que se quiere buscar
     * @return ArrayList con el conjunto de nombres
     */
    public ArrayList<String> getNombres(String categoria) {
    	ArrayList<String> nombres;
    	Indice categoriaActual;
    	
    	nombres = new ArrayList<String>();
    	
    	for (int i = 0; i < indiceCategoria.size(); i ++) {
    		categoriaActual = indiceCategoria.get(i);
    		
    		if (categoria.equals(categoriaActual.getClave())) {
    			nombres.add(this.getNombreIndice(categoriaActual.getIndice()));
    		}
    	}
    	
    	return nombres;
    }
    
    /**
     * Recopila un subconjunto de direcciones de im�gen contenidas en una ruta y 
     * pertenecientes a una categor�a dada
     * 
     * @param categoria categor�a por la que se quiere buscar
     * @return ArrayList con el conjunto de de direcciones de im�gen
     */
    public ArrayList<String> getImagenes(String categoria) {
    	ArrayList<String> imagenes;
    	Indice categoriaActual;
    	
    	imagenes = new ArrayList<String>();
    	
    	for (int i = 0; i < indiceCategoria.size(); i ++) {
    		categoriaActual = indiceCategoria.get(i);
    		
    		if (categoria.equals(categoriaActual.getClave())) {
    			imagenes.add(this.buscarRecurso(getNombreIndice(categoriaActual.getIndice()), "imagen"));
    		}
    	}
    	
    	return imagenes;
    }
    
    /**
     * Devuelve el nombre de la ruta, sabiendo su �ndice de indexaci�n
     * 
     * @param indice n�mero asignado a ese �ndice
     * @return nombre de la ruta
     */
    protected String getNombreIndice(int indice) {
    	String nombre;
    	Indice ind;
    	boolean salida;
    	
    	ind = null;
    	salida = false;
    	nombre = "";
    	for (int i = 0; i < indiceNombre.size() && !salida; i ++) {
    		ind = indiceNombre.get(i);
    		if (ind.getIndice() == indice) {
    			salida = true;
    		}
    	}
    	
    	if (ind != null) {
    		nombre = ind.getClave();
    	}
    	return nombre;
    }
    
    /**
     * Recorre el archivo para crear 2 �ndices, por nombre de ruta y por categor�a de ruta
     * de forma que se pueda leer de forma m�s eficiente cuando se sabe la posici�n, el 
     * nombre o la categor�a de una ruta
     */
    protected void indexar() {
    	NodeList descripcion, nodos, alias;
    	Node ruta, nodo;
    	String valor;
    	boolean salida;
    	
    	indiceNombre = new ArrayList<Indice>();
    	indiceCategoria = new ArrayList<Indice>();
    	nodos = documentoXML.getElementsByTagName("ruta");
    	
    	for (int i = 0; i < nodos.getLength(); i ++) {
    		ruta = nodos.item(i);
    		descripcion = ruta.getChildNodes();
    		
    		salida = false;
    		for (int j = 0; j < descripcion.getLength() && !salida; j ++) {
    			nodo = descripcion.item(j);
    				// Se leen los tags o nodos cuyo nombre coincide con "nombre", 
    			// "categoria" o "alias"
    			if (nodo.getNodeName().equals("nombre")) {
    				valor = nodo.getFirstChild().getNodeValue();
    				valor = limpiar(valor);
    				
    				Indice nuevoIndice = new Indice();
    				nuevoIndice.setClave(valor);
    				nuevoIndice.setIndice(i);
    				indiceNombre.add(nuevoIndice);
    			} else if (nodo.getNodeName().equals("categoria")) {
    				valor = nodo.getFirstChild().getNodeValue();
    				valor = limpiar(valor);
    				
    				Indice nuevoIndice = new Indice();
    				nuevoIndice.setClave(valor);
    				nuevoIndice.setIndice(i);
    				indiceCategoria.add(nuevoIndice);
    			} else if (nodo.getNodeName().equals("alias")) {
    				alias = nodo.getChildNodes();
    				
    					// Podemos tener varios alias 
    				for (int l = 0; l < alias.getLength(); l ++) {
    					valor = alias.item(l).getFirstChild().getNodeValue();
        				valor = limpiar(valor);
        				
        				Indice nuevoIndice = new Indice();
        				nuevoIndice.setClave(valor);
        				nuevoIndice.setIndice(i);
        				indiceNombre.add(nuevoIndice);
    				}
    			}
    		}
    	}
    }
    
    /**
     * Devuelve una lista con el nombre de las categor�as, sin repetirse, que aparece en 
     * el archivo XML
     * 
     * @return Lista con los nombres de categor�as del archivo
     */
    public List<String> getCategorias() {
    	Set<String> cat;
    	
    	cat = new HashSet<String>();
    	
    	for (int i = 0; i < indiceCategoria.size(); i ++) {
    		cat.add(indiceCategoria.get(i).getClave());
    	}
    	
    	return new ArrayList<String>(cat);
    }
    
    /**
     * Elimina caracteres extra�os, espacio, \n y \r de una cadena de texto al principio y 
     * al final de la misma, puede haber sido leida desde el archivo o introducida por el 
     * usuario.
     * 
     * @param cadena cadena que se quiere limpiar
     * @return cadena de texto sin los caracteres extra�os
     */
    protected String limpiar(String cadena) {
    	String limpiado;
    	
    	limpiado = cadena.toLowerCase();
    	limpiado = limpiado.replaceAll("^[\t| |\n|\r]*", "");
    	limpiado = limpiado.replaceAll("[\t| |\n|\r]*$", "");
    	
    	return limpiado;
    }
    
    /**
     * Calcula el �ndice de la ruta a partir de la lista de nodos y el nombre de la ruta 
     * que quermos buscar 
     * 
     * @param nodos lista de nodos sobre la que se realizar� la b�squeda
     * @param nombre nombre del nodo que queremos buscar
     * @return �ndice del nodo que se busca
     */
    protected int buscarRutaNombre(NodeList nodos, String nombre) {
    	Indice indiceActual;
    	int indice;
    	
    	indice = -1;
    	
    	for (int i = 0; i < indiceNombre.size(); i ++) {
    		indiceActual = indiceNombre.get(i);
    		
    		if (indiceActual.getClave().equals(nombre)) {
    			indice = indiceActual.getIndice();
    		}
    	}
    	
    	return indice;
    }
    
    /**
     * Busca la lista de localizaciones completa seg�n un nombre de ruta. El listado puede
     * ser de puntos geogr�ficos de la API de googlemaps (GeoPoint) o una cadena con el 
     * nombre de la direcci�n. Ej: "Calle Carreter�as" 
     * 
     * @param nombre cadena con el nombre de la ruta
     * @return lista de las localizaciones encontradas
     */
    public List buscarLocalizaciones(String nombre) {
    	NodeList nodosRuta, nodosDescripcion, localizaciones;
    	Node ruta, nodo, localizacion, direccion, puntoGeografico, nodoAux;
    	Node latitud, longitud, nombreDir;
    	List <String> nombresLocalizacion;
    	List <GeoPoint> puntosGeografico;
    	String cadenaAux;
    	int indiceRuta, ind, indiceLocalizaciones, lat, lon;
    	
    		// Se eliminan los caracteres extra�os al principio y al final de la cadena
    	// para facilitar las comparaciones. Se realiza lo mismo para el resto de cadenas 
    	// con las que se pretende comparar y con las cadenas leidas desde archivo XML
    	nombre = limpiar(nombre);
    	
    	nombresLocalizacion = new ArrayList<String>();
    	puntosGeografico = new ArrayList<GeoPoint>();
    	nodosRuta = documentoXML.getElementsByTagName("ruta");
    	
    		// Se busca la ruta
    	indiceRuta = this.buscarRutaNombre(nodosRuta, nombre);
    	
    		// Una vez encontrada se procede de la siguente manera
    	if (indiceRuta > -1) {
    		ruta = nodosRuta.item(indiceRuta);
    		nodosDescripcion = ruta.getChildNodes();
    		for (int i = 0; i < nodosDescripcion.getLength(); i ++) {
    			nodo = nodosDescripcion.item(i);
    				// se buscan las localizaciones
    			if (nodo.getNodeName().equals("localizaciones")) {
    				localizaciones = nodo.getChildNodes();
    				indiceLocalizaciones = 0;
    				do {
    						// por cada localizaci�n se busca ...
	    				do {
	    					localizacion = localizaciones.item(indiceLocalizaciones ++);
	    				} while (indiceLocalizaciones < localizaciones.getLength() &&
	    						 !localizacion.getNodeName().equals("localizacion"));
	    				
	    				if (localizacion != null && localizacion.getChildNodes().getLength() > 0) {
	    					ind = 0;
	    					direccion = null;
	    						// la direcci�n
	    					do {
	    						nodoAux = localizacion.getChildNodes().item(ind ++);
	    						
		    					if (nodoAux.getNodeName().equals("direccion")) {
	    							direccion = nodoAux;
	    						}
		    				} while (ind < localizacion.getChildNodes().getLength());
	    					
	    					ind = 0;
		    				if (direccion != null) {
		    					puntoGeografico = null;
		    					nombreDir = null;
		    					do {
		    						nodoAux = direccion.getChildNodes().item(ind ++);
		    							// Dentro de la direcci�n podemos encontrarnos 
		    						// puntos geogr�ficos, o nombres de direcci�n
		    						if (nodoAux.getNodeName().equals("geopoint")) {
		    							puntoGeografico = nodoAux;
		    						}
		    						else if (nodoAux.getNodeName().equals("nombreDir")) {
		    							nombreDir = nodoAux;
		    						} 
			    				} while (ind < direccion.getChildNodes().getLength());
		    					
		    					if  (nombreDir != null) {
				    				ind = 0;
				    					// Leemos los valores de la direcci�n si el nodo
				    				// es de ese tipo
					    			do {
					    				nodoAux = nombreDir.getChildNodes().item(ind ++);
					    			} while (ind < nombreDir.getChildNodes().getLength() &&
					    					nodoAux.getNodeType() != Node.TEXT_NODE);
					    			cadenaAux = nodoAux.getNodeValue();
					    			nombresLocalizacion.add(limpiar(cadenaAux));
			    				}
		    					
			    				if  (puntoGeografico != null) {
			    					latitud = null;
			    					longitud = null;
			    					ind = 0;
			    					do {
			    						nodoAux = puntoGeografico.getChildNodes().item(ind ++);
			    						
			    							// Obtenemos los nodos de latitud y longitud
			    						// si nos encontramos con una localizaci�n mediante 
			    						// un punto geogr�fico
			    						if (nodoAux.getNodeName().equals("latitud")) {
			    							latitud = nodoAux;
			    						} else if (nodoAux.getNodeName().equals("longitud")) {
			    							longitud = nodoAux;
			    						}
			    					} while (ind < puntoGeografico.getChildNodes().getLength());
			    					
			    					lat = 0;
				    				ind = 0;
					    			do {
					    				nodoAux = latitud.getChildNodes().item(ind ++);
					    				
					    					// Obtenemos el valor de la latitud
					    				if (nodoAux.getNodeType() == Node.TEXT_NODE) {
					    					lat = Integer.parseInt(limpiar(nodoAux.getNodeValue()));
					    				}
					    			} while (ind < latitud.getChildNodes().getLength());
					    			
					    			lon = 0;
					    			ind = 0;
					    			do {
					    				nodoAux = longitud.getChildNodes().item(ind ++);
					    				
					    					// Obtenemos el valor de la longitud
					    				if (nodoAux.getNodeType() == Node.TEXT_NODE) {
					    					lon = Integer.parseInt(limpiar(nodoAux.getNodeValue()));
					    				}
					    			} while (ind < latitud.getChildNodes().getLength());
					    			
					    			GeoPoint punto = new GeoPoint(lat, lon);
					    			puntosGeografico.add(punto);
			    				}
		    				}
    					}
    				} while (indiceLocalizaciones < localizaciones.getLength());
    			}
    		}
    	}
    	
    		// Se elige qu� tipo de puntos se devuelven, los que tiene el nombre de la 
    	// localizaci�n o los que tienen los puntos geogr�ficos
    	if (puntosGeografico.size() > nombresLocalizacion.size()) {
    		hayGeopoint = true;
    		hayDirecciones = false;
    		return puntosGeografico;
    	} else {
    		hayGeopoint = false;
    		hayDirecciones = true;
    		return nombresLocalizacion;
    	}
    }
    
    /**
     * Busca una localizaci�n seg�n un nombre de ruta y la fecha en formato 
     * "dd/MM/yyyy HH:mm", donde d es un d�gito de dia, M es un d�gito del mes, y es un 
     * d�gito del a�o, H es un d�gito de la hora y m es un d�gito del minuto. El listado 
     * puede ser de puntos geogr�ficos de la API de googlemaps (GeoPoint) o una cadena 
     * con el nombre de la direcci�n. Ej: "Calle Carreter�as" 
     * 
     * @param nombre cadena con el nombre de la ruta
     * @param fecha cadena con la fecha con el formato "dd/MM/yyyy HH:mm", en la cual se 
     * quiere conocer d�nde se encuentra la ruta.
     * @return lista de las localizaciones encontradas
     */
    public String buscarLocalizacion(String nombre, String fecha) {
    	NodeList nodosRuta, nodosDescripcion, localizaciones;
    	Node ruta, nodo, localizacion, direccion, nombreDir, nodoTexto; 
    	Node nodoFecha, nodoAux, geopoint, longitud, latitud;
    	List <String> nombres, fechas;
    	List <GeoPoint> puntosGeografico;
    	DateFormat formatoFecha;
    	Date fechaEntrada, fechaAux;
    	String cadenaAux;
    	int indiceRuta, ind, indiceLocalizaciones, lat, lon;
    	
    	formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	
    	nombre = limpiar(nombre);
    	
    	nombres = new ArrayList<String>();
    	fechas = new ArrayList<String>();
    	puntosGeografico = new ArrayList<GeoPoint>();
    	nodosRuta = documentoXML.getElementsByTagName("ruta");
    	
    	indiceRuta = this.buscarRutaNombre(nodosRuta, nombre);
    	
    	if (indiceRuta > -1) {
    		ruta = nodosRuta.item(indiceRuta);
    		nodosDescripcion = ruta.getChildNodes();
    		for (int i = 0; i < nodosDescripcion.getLength(); i ++) {
    			nodo = nodosDescripcion.item(i);
    			if (nodo.getNodeName().equals("localizaciones")) {
    				localizaciones = nodo.getChildNodes();
    				indiceLocalizaciones = 0;
    				do {
	    				do {
	    					localizacion = localizaciones.item(indiceLocalizaciones ++);
	    				} while (indiceLocalizaciones < localizaciones.getLength() &&
	    						 !localizacion.getNodeName().equals("localizacion"));
	    				
	    				if (localizacion != null && localizacion.getChildNodes().getLength() > 0) {
	    					ind = 0;
	    					direccion = null;
	    					nodoFecha = null;
	    					do {
	    						nodoAux = localizacion.getChildNodes().item(ind ++);
	    						
		    					if (nodoAux.getNodeName().equals("direccion")) {
	    							direccion = nodoAux;
	    						} else if (nodoAux.getNodeName().equals("fecha")) {
	    							nodoFecha = nodoAux;
	    						}
		    				} while (ind < localizacion.getChildNodes().getLength());
		    				
	    					ind = 0;
	    					do {
	    						nodoAux = nodoFecha.getChildNodes().item(ind ++);
	    						
	    						if (nodoAux.getNodeType() == Node.TEXT_NODE) {
	    							cadenaAux = nodoAux.getNodeValue();
	    							fechas.add(limpiar(cadenaAux));
	    						}
	    					} while (ind < nodoFecha.getChildNodes().getLength());
	    					
	    					ind = 0;
		    				if (direccion != null) {
		    					nombreDir = null;
		    					geopoint = null;
		    					do {
		    						nodoAux = direccion.getChildNodes().item(ind ++);
		    						if (nodoAux.getNodeName().equals("nombreDir")) {
		    							nombreDir = nodoAux;
		    						} 
		    						else if (nodoAux.getNodeName().equals("geopoint")) {
		    							geopoint = nodoAux;
		    						}
			    				} while (ind < direccion.getChildNodes().getLength());
		    					
			    				if  (nombreDir != null) {
				    				ind = 0;
					    			do {
					    				nodoTexto = nombreDir.getChildNodes().item(ind ++);
					    			} while (ind < nombreDir.getChildNodes().getLength() &&
					    					 nodoTexto.getNodeType() != Node.TEXT_NODE);
					    			cadenaAux = nodoTexto.getNodeValue();
					    			nombres.add(limpiar(cadenaAux));
			    				}
			    				
			    				if (geopoint != null) {
			    					ind = 0;
			    					latitud = null;
			    					longitud = null;
			    					do {
			    						nodoAux = geopoint.getChildNodes().item(ind ++);
			    						
			    						if (nodoAux.getNodeName().equals("latitud")) {
			    							longitud = nodoAux;
			    						} else if (nodoAux.getNodeName().equals("longitud")) {
			    							latitud = nodoAux;
			    						}
			    					} while(ind < geopoint.getChildNodes().getLength());
			    					
			    					lat = 0;
				    				ind = 0;
					    			do {
					    				nodoAux = latitud.getChildNodes().item(ind ++);
					    				
					    				if (nodoAux.getNodeType() == Node.TEXT_NODE) {
					    					lat = Integer.parseInt(limpiar(nodoAux.getNodeValue()));
					    				}
					    			} while (ind < latitud.getChildNodes().getLength());
					    			
					    			lon = 0;
					    			ind = 0;
					    			do {
					    				nodoAux = longitud.getChildNodes().item(ind ++);
					    				
					    				if (nodoAux.getNodeType() == Node.TEXT_NODE) {
					    					lon = Integer.parseInt(limpiar(nodoAux.getNodeValue()));
					    				}
					    			} while (ind < latitud.getChildNodes().getLength());
					    			
					    			GeoPoint punto = new GeoPoint(lat, lon);
					    			puntosGeografico.add(punto);
			    				}
		    				}
    					}
    				} while (indiceLocalizaciones < localizaciones.getLength());
    			}
    		}
    	}
    	
    	ind = 0;
    	
    	if (fechas.size() > 0) {
	    	try {
				fechaEntrada = formatoFecha.parse(fecha);
				
				boolean salida = false;
				for (int i = 0; i < fechas.size() && !salida; i ++) {
					fechaAux = formatoFecha.parse(fechas.get(i));
					
		    		if (fechaAux.compareTo(fechaEntrada) > 0) {
		    			salida = true;
		    			ind = i - 1;
		    		}
		    	}
				
				if (ind < 0) {
					fechaAux = formatoFecha.parse(fechas.get(0));
					fechaAux.setMinutes(fechaAux.getMinutes() - 30);
					if (fechaAux.compareTo(fechaEntrada) < 0) {
						ind = 0;
					} else {
						ind = -1;
					}
				}
				
				if (!salida) {
					fechaAux = formatoFecha.parse(fechas.get(fechas.size() - 1));
					fechaAux.setMinutes(fechaAux.getMinutes() + 30);
					if (fechaAux.compareTo(fechaEntrada) > 0) {
						ind = fechas.size() - 1;
					} else {
						ind = -1;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	} else {
    		ind = -1;
    	}
    	
    	if (ind > -1) {
    		if (nombres.size() >= ind) { 
    			hayGeopoint = false;
        		hayDirecciones = true;
    			cadenaAux = nombres.get(ind);
    		} else if (puntosGeografico.size() >= ind) {
    			hayGeopoint = true;
        		hayDirecciones = false;
        		
     		   	Address dir = new Address(Locale.ENGLISH);
     		   	dir.setLatitude(puntosGeografico.get(ind).getLatitudeE6());
     		   	dir.setLongitude(puntosGeografico.get(ind).getLongitudeE6());
     		   	cadenaAux = dir.getLocality();
        		
    			cadenaAux = puntosGeografico.get(ind).toString();
    		} else {
    			cadenaAux = null;
    		}
    	} else {
    		cadenaAux = null;
    	}
    	
    	return cadenaAux;
    }
    
    // Recursos: musica, texto, imagen
    public String buscarRecurso(String nombre, String recurso) {
    	NodeList nodosRuta, nodosDescripcion;
    	Node ruta, nodo, nombreRecurso;
    	String direccionRecurso;
    	int indiceRuta, ind;
    	
    	nombre = limpiar(nombre);
    	
    	direccionRecurso = nombre;
    	nodosRuta = documentoXML.getElementsByTagName("ruta");
    	
    	indiceRuta = this.buscarRutaNombre(nodosRuta, nombre);
    	
    	if (indiceRuta > -1) {
    		ruta = nodosRuta.item(indiceRuta);
    		nodosDescripcion = ruta.getChildNodes();
    		for (int i = 0; i < nodosDescripcion.getLength(); i ++) {
    			nodo = nodosDescripcion.item(i);
    			if (nodo.getNodeName().equals(recurso)) {
    				ind = 0;
    				do {
    					nombreRecurso = nodo.getChildNodes().item(ind ++);
    				} while (nodo.getNodeType() == Node.TEXT_NODE);
    				direccionRecurso = nombreRecurso.getNodeValue();
    				direccionRecurso = limpiar(direccionRecurso);
    			}
    		}
    		
    	}
    	
    	return direccionRecurso;
    }
}