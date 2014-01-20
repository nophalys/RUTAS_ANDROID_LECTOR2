package es.android.rutas;

//import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
//import android.widget.Toast;

public class SeleccionRuta extends Activity implements OnClickListener {
	Spinner s;
	
	
	
	String horaSpinner,minutoSpinner,fechaSpinner;
	Spinner viewSpinnerFecha,viewSpinnerMinuto,viewSpinnerHora;
	@SuppressWarnings("unchecked")
	ArrayAdapter spinnerArrayAdapterFecha,spinnerArrayAdapterminutos,spinnerArrayAdapterhora;
	
	
	
	String rutaXml;
	
	private Button botonImagenes;
	private Button botonVolver;
    /** Called when the activity is first created. */



	private LectorFicheroXml lectorXML;
    
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionruta);
        
        
        botonImagenes = (Button) this.findViewById(R.id.porFecha);
        botonImagenes.setOnClickListener(this);
        
        
        botonVolver = (Button) this.findViewById(R.id.volver);
        botonVolver.setOnClickListener(this);
        
        
        
        //SE RECUPERAN LOS PARAMETROS
  //      ArrayList<String>   listaCategorias= (ArrayList<String>) this.getIntent().getStringArrayListExtra("categorias");
        
   //     rutaXml= this.getIntent().getStringExtra("rutaXml");
        
   //    System.out.println("Se recupera o que "+rutaXml);
       
       
       
       
		lectorXML = (LectorFicheroXml) this.getIntent().getSerializableExtra(
		"lectorXML");
ArrayList<String> listaCategorias = (ArrayList<String>) lectorXML
		.getCategorias();

ArrayList<String> listaNombres = lectorXML.getNombres(listaCategorias
		.get(0));
System.out.println("el primer nombre de la lista es "
		+ listaNombres.get(0));

//System.out.println(listaCategorias.get(0));

     
       
       
     // Toast.makeText(this, archivoRecuperado.getPath(),Toast.LENGTH_LONG).show();
     
        
        //fecha
        
        System.out.println(listaCategorias.get(0));
      //  String[] dias = (String [])archivoRecuperado.getCategorias().toArray();
        
        String[] dias= new String[listaCategorias.size()];
        for (int i=0;i<listaCategorias.size();i++){
        	dias[i]=listaCategorias.get(i);
        }
        
        
    //    String[] dias = (String [])archivoRecuperado.toArray();
        
        
        
         viewSpinnerFecha = (Spinner) findViewById(R.id.fecha);
      //  s  = (Spinner) findViewById(R.id.fecha);

         spinnerArrayAdapterFecha = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dias);
        		
        viewSpinnerFecha.setAdapter(spinnerArrayAdapterFecha);
        
        
      //  fechaSpinner=(String) spinnerArrayAdapterFecha.getItem(viewSpinnerFecha.getSelectedItemPosition()).toString();
        
        
        
        //hora
        String[] hora = new String[] { "01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","00"};
         viewSpinnerHora = (Spinner) findViewById(R.id.hora);
        
        spinnerArrayAdapterhora = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hora);
        viewSpinnerHora.setAdapter(spinnerArrayAdapterhora);
        
     //   horaSpinner= spinnerArrayAdapterhora.getItem(viewSpinnerHora.getSelectedItemPosition()).toString();
        
        //minuto
       
        String[] minutos = new String[] { "00","15","30","45",};
        viewSpinnerMinuto = (Spinner) findViewById(R.id.minuto);
        
        spinnerArrayAdapterminutos = new ArrayAdapter(this,android.R.layout.simple_spinner_item,minutos);
        viewSpinnerMinuto.setAdapter(spinnerArrayAdapterminutos);
        
       // minutoSpinner=(String) spinnerArrayAdapterminutos.getItem(viewSpinnerMinuto.getSelectedItemPosition()).toString();
    }
	
	
	
	public String CategoriaADia(String categoria){
		String dia=null;
		if (categoria.equals("Domingo de Ramos")){
			
			dia="28/03/2010";
		}
		else if (categoria.equals("domingo de ramos")){
			
			dia="28/03/2010";
		}
		
		else if (categoria.equals("Lunes Santo")){
			dia="29/03/2010";
		}
		else if (categoria.equals("lunes santo")){
			dia="29/03/2010";
		}

		return dia;
			
	}
    
    
    public void onClick(View arg0) {
    	if (arg0.getId() == botonImagenes.getId()) {
    		
    		
    	fechaSpinner=(String) spinnerArrayAdapterFecha.getItem(viewSpinnerFecha.getSelectedItemPosition()).toString();
    	horaSpinner= spinnerArrayAdapterhora.getItem(viewSpinnerHora.getSelectedItemPosition()).toString();
    	 minutoSpinner=(String) spinnerArrayAdapterminutos.getItem(viewSpinnerMinuto.getSelectedItemPosition()).toString();   
    		
    		
    		System.out.println(fechaSpinner+" "+horaSpinner+":"+minutoSpinner);
    		
    		
    	//	LectorFicheroXml lector = new LectorFicheroXml(rutaXml);
    	//ArrayList<String> arrayImagenes=	lector.getImagenes(fechaSpinner);
    		
    	
    	String diaCat= CategoriaADia(fechaSpinner);
    		
//    		 Intent mIntent = new Intent(this, SeleccionImagenes.class);
//             //	mIntent.
//    			String fechaParam=diaCat+" "+horaSpinner+":"+minutoSpinner;
//             	mIntent.putExtra("fecha", fechaParam);
//             	mIntent.putExtra("categoria", fechaSpinner);
//            	mIntent.putExtra("rutaXml", rutaXml);
//    		 
//    		 
//             	startActivity(mIntent);
//             	
//
//             	
//             	this.finish();
    	

		Intent mIntent = new Intent(this, SeleccionImagenes.class);

		String fechaParam = diaCat + " " + horaSpinner + ":"
				+ minutoSpinner;
		mIntent.putExtra("fecha", fechaParam);
		mIntent.putExtra("categoria", fechaSpinner);

		mIntent.putExtra("lectorXML", lectorXML);

		startActivity(mIntent);

		this.finish();
    	
    	
    	
   	}
    	else if (arg0.getId() == botonVolver.getId()){
    		
    		 Intent mIntent = new Intent(this, SeleccionFichero.class);
             //	mIntent.
             	startActivity(mIntent);
             	this.finish();
   		
   		
   	}
   }
    
    
    
}