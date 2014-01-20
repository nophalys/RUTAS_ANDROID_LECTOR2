package es.android.rutas;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Parcelable;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;


public class SeleccionImagenes extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
//	private int posicionFoto=0;
	private Button botonVolver;
	private int totalImagenes;
//	private ArrayList<String> arrayImagenes;
	private ArrayList<String> arrayCalles;
	private ArrayList<String> arrayNombres;
//	private LectorFicheroXml lectorXml;
	private ArrayList<String> arrayCanciones;
	
	private String fecha,categoria,rutaXml;
	private LectorFicheroXml lectorXML;
	
 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.imagenes);
        
        
        botonVolver = (Button) this.findViewById(R.id.BotVolver1);
        botonVolver.setOnClickListener(this);
        
        
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        

        
        //RECUPERAR PARAMETROS
//       fecha= this.getIntent().getStringExtra("fecha");
//       categoria=  this.getIntent().getStringExtra("categoria");
//       rutaXml=   this.getIntent().getStringExtra("rutaXml");
//       
//       
//       System.out.println("La ruta es "+rutaXml);
//       System.out.println("La categoria es "+categoria);
//       System.out.println("La fecha es "+fecha);
//       
//       LectorFicheroXml lector = new LectorFicheroXml(rutaXml);
//       ArrayList<String> arrayImagenes =lector.getImagenes(categoria);
//       
//       arrayNombres=lector.getNombres(categoria);
//       System.out.println("El primer nombre es "+arrayNombres.get(0));
//       arrayCanciones =new ArrayList<String>(); 
//      
//     //  ArrayList<String> arrayCalles=null;
//       
//       arrayCalles = new ArrayList<String>();
//       String calle="";
//       for (int i=0;i<arrayNombres.size();i++){
//    	   System.out.println("El nombre en "+i+" es "+arrayNombres.get(i));
//    	   calle=lector.buscarLocalizacion(arrayNombres.get(i), fecha);
//    	   arrayCanciones.add(lector.buscarRecurso(arrayNombres.get(i), "musica"));
//    	   System.out.println("la calle es "+calle);
//    	   arrayCalles.add(calle);
//       
//       }

		fecha = this.getIntent().getStringExtra("fecha");
		categoria = this.getIntent().getStringExtra("categoria");

		lectorXML = (LectorFicheroXml) this.getIntent().getSerializableExtra(
				"lectorXML");

	//	System.out.println("La categoria es " + categoria);
	//	System.out.println("La fecha es " + fecha);

		ArrayList<String> arrayImagenes = lectorXML.getImagenes(categoria);
		arrayNombres = lectorXML.getNombres(categoria);

//		System.out.println("El primer nombre es " + arrayNombres.get(0));
		
		arrayCanciones =new ArrayList<String>(); 
		//lectorXML.buscarRecurso(nombre, recurso);
		arrayNombres = lectorXML.getNombres(categoria);
		

		arrayCalles = new ArrayList<String>();
		String calle = "";
		for (int i = 0; i < arrayNombres.size(); i++) {
		//	System.out.println("El nombre en " + i + " es "+ arrayNombres.get(i));

			calle = lectorXML.buscarLocalizacion(arrayNombres.get(i), fecha);
			arrayCanciones.add(lectorXML.buscarRecurso(arrayNombres.get(i), "musica"));

			System.out.println("la calle es " + calle);
			arrayCalles.add(calle);

		}
       
       
       
        
        totalImagenes=arrayImagenes.size();
        
        gridview.setAdapter(new AdaptadorImagen(this,totalImagenes,arrayImagenes));
        
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	//pinta la posicion del elemento seleccionado del grid
               
            	//Toast.makeText(HelloGridView.this, "Has pulsado la foto en la posicion: " + position, Toast.LENGTH_SHORT).show();

            
            	 Intent mIntent = new Intent(SeleccionImagenes.this, MapaFecha.class);
               
            	 mIntent.putExtra("posicion", String.valueOf(position));
            	 
            	 mIntent.putExtra("calle", arrayCalles.get(position));
            	 mIntent.putExtra("musica", arrayCanciones.get(position));
            	 mIntent.putExtra("rutaXml", rutaXml);
            	 mIntent.putExtra("nombre", arrayNombres.get(position));
            	 mIntent.putExtra("lectorXML", lectorXML);
            	 
            	 
            	 if (arrayCalles.get(position) != null){
            		 startActivity(mIntent);
            		 SeleccionImagenes.this.finish();

            	 }else{
            		 
            		Toast.makeText(SeleccionImagenes.this, "No existe ruta para esa hora", Toast.LENGTH_SHORT).show();

            	 }
            }

        }
        );
        
       
    }
    
    public void onClick(View arg0) {
    	if (arg0.getId() == botonVolver.getId()) {
    		
    		 Intent mIntent = new Intent(this, SeleccionRuta.class);
    		 mIntent.putExtra("lectorXML", lectorXML);
    		 
            
             	startActivity(mIntent);
             	this.finish();
    		
   	}
   }



}