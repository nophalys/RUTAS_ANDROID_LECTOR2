package es.android.rutas;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

//import es.android.mapa.LectorFicheroXml;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Examinar extends ListActivity  {
    
	private void rellenarConElRaiz() {
        rellenar(new File("/").listFiles());
    } 

	
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int IDFilaSeleccionada = position;
        if (IDFilaSeleccionada==0){
            //rellenarConElRaiz();
        	Intent mIntent = new Intent(this, SeleccionFichero.class);
			startActivity(mIntent);
			this.finish();        	
        } else {
            File archivo = new File(elementos.get(IDFilaSeleccionada));
            if (archivo.isDirectory()){
                rellenar(archivo.listFiles());
            }
             else{            	 
 				Intent mIntent = new Intent(this, SeleccionRuta.class);
			//	mIntent.putExtra("nombreFicheroExaminado", archivo.getPath());
				
			System.out.println("la ruta es "+archivo.getPath());
 				
				mIntent.putExtra("rutaXml", archivo.getPath());
				
				//System.out.println("la ruta es "+archivo.getPath());
				
				LectorFicheroXml lector = new LectorFicheroXml(archivo.getPath());
				ArrayList<String> categorias = (ArrayList<String>) lector.getCategorias();
				
				mIntent.putStringArrayListExtra("categorias", categorias);
				
				mIntent.putExtra("lectorXML", lector);
				

				startActivity(mIntent);
				this.finish();
             }
        }
    }


	private List<String> elementos = null; 
	 private void rellenar(File[] archivos) {
	        elementos = new ArrayList<String>();
	        elementos.add(getString(R.string.raiz));
	        if (archivos != null) {
	        	for( File archivo: archivos)
	        		elementos.add(archivo.getPath());
	        }
	       
	        ArrayAdapter<String> listaArchivos= new ArrayAdapter<String>(this, R.layout.fila, elementos);
	        setListAdapter(listaArchivos);
	    }



	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.listado);
        rellenarConElRaiz();
    
    }
}