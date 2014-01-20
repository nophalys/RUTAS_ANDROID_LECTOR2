package es.android.rutas;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;




import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapaFecha extends MapActivity implements OnClickListener {//implements OnClickListener {
	private MapView vistaMapa;
	private Button botonVolver;
	//private EditText textoEntrada;
//	private TextView textoSalida;
	private Geocoder localizador;
//	private FormatearImagen img;
	private MediaPlayer mp;
//	private LectorFicheroXml lectorXml;
	
	private Button botonInfo;
  private	String rutaXml;
  private	String nombre;
  private	String imagen;
  private	String musica;
	
	
	
	//PARA PINTAR PUNTOS
	 List<Overlay> mapOverlays;
	 Drawable drawable;
	 PintaPuntos itemizedOverlay;
	private Serializable lectorXML;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        
        MapController controlador;
        
//        botonConsulta = (Button) this.findViewById(R.id.botonConsulta);
//  //      botonConsulta.setOnClickListener(this);
//        
//       textoEntrada = (EditText) this.findViewById(R.id.textoEntradaConsulta);
//       
//       
//       
       
  //     textoSalida = (TextView) this.findViewById(R.id.textoRespuesta);
       botonVolver = (Button) this.findViewById(R.id.botonVolverMapa);
       botonVolver.setOnClickListener(this);
       
       botonInfo = (Button) this.findViewById(R.id.botonInfo);
       botonInfo.setOnClickListener(this);
       
        
        vistaMapa = (MapView) findViewById(R.id.mapView);
        vistaMapa.setBuiltInZoomControls(true);
        
        controlador = vistaMapa.getController();
        
        
        //PARA PINTAR PUNTO
        mapOverlays = vistaMapa.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.puntochico);
        itemizedOverlay = new PintaPuntos(drawable);
        
        
        
        
        //esto se puede quitar
	//	GeoPoint malaga = new GeoPoint(36719444, -4420000);
	//	controlador.setCenter(malaga);
        
      //  controlador.setZoom(19);
        
        
        
        localizador = new Geocoder(this.getApplicationContext());
        
     //   img = new FormatearImagen();
        
        mp = new MediaPlayer();
        

        
     // SE RECUPERAN LOS PARAMETROS
        String localizacion=  this.getIntent().getStringExtra("calle");
       System.out.println(localizacion);
        musica = this.getIntent().getStringExtra("musica");
        nombre = this.getIntent().getStringExtra("nombre");
        
        lectorXML = this.getIntent().getSerializableExtra("lectorXML");
       
       
       
       rutaXml=   this.getIntent().getStringExtra("rutaXml");
       
       

		//	String localizacion = ((TextView)textoEntrada).getText().toString();
			
			
			if (localizacion.equals("")){
				localizacion="calle marmoles Malaga";
			}
			
			try {
			//	lectorXml.buscarLocalizacion();
				
				
				
				Address direccion = localizador.getFromLocationName(localizacion, 1).get(0);
				int latitud, longitud;
				latitud = (int) (direccion.getLatitude() * 1000000);
				longitud = (int) (direccion.getLongitude() * 1000000);
				//textoSalida.setText(latitud + ", " + longitud);
				GeoPoint situacion = new GeoPoint(latitud, longitud);
				controlador.setZoom(18);
				controlador.setCenter(situacion);
				
				//PARA EL PUNTO
				OverlayItem overlayitem = new OverlayItem(situacion, localizacion, localizacion);
			//	System.out.println(overlayitem.getSnippet());
			//	System.out.println(overlayitem.getTitle());
	        	itemizedOverlay.addOverlay(overlayitem);
	        	mapOverlays.add(itemizedOverlay);
	        	
	        	Toast toast=Toast.makeText(this, localizacion, Toast.LENGTH_LONG);
	        //	toast.setDuration(5000);
	        	toast.show();
				
				
				
				
//				ImageView imageView = (ImageView) findViewById(R.id.imagen1);	
//				imageView = img.recuperarImagen("/sdcard/imagenes/pollinica.jpg", imageView);
//				
				
				
				try {
					if (mp.isPlaying()) {
						System.err.println("entro en parar");						
						mp.stop();
						mp.reset();
					}
					//mp.setDataSource("/sdcard/canciones/Sinfonia9.wma");
				//	mp.setDataSource("/sdcard/canciones/lagrimasdesanjuan.mp3");
					
					mp.setDataSource(musica);
					//mp.setDataSource("/sdcard/canciones/micristodebronce.mp3");
					mp.prepare();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		        mp.start();
			
			} catch (IOException ioe) {
				
				Toast toast=Toast.makeText(this, "FALLA EL MAPA", Toast.LENGTH_LONG);
		        //	toast.setDuration(5000);
		        	toast.show();
				
			}
			
				
    }
	

	 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
	

	@Override
	protected void onDestroy() {
		
		mp.stop();
		super.onDestroy();
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		if (v.getId()==botonVolver.getId()){
		//los parametros de SeleccionRuta añadelos si no falla porque se pierden
		//	lectorXML
		 Intent mIntent = new Intent(this, SeleccionRuta.class);
			mIntent.putExtra("lectorXML", lectorXML);
        
         	startActivity(mIntent);
         	this.finish();
		}else if (v.getId()==botonInfo.getId()){
			
			 Intent mIntent = new Intent(this, PantallaInfo.class);
			 mIntent.putExtra("rutaXml", rutaXml);
			 mIntent.putExtra("imagen",imagen);
			 mIntent.putExtra("nombre",nombre);
			 mIntent.putExtra("lectorXML",lectorXML);
			 
			 
			 
	         startActivity(mIntent);
	         mp.reset();
	         //	this.finish();
		}
		
		
	}
	
	
}