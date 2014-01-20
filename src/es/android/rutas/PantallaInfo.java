package es.android.rutas;

/*import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;*/
import java.io.IOException;
/*import java.io.InputStream;
import java.io.Serializable;*/

//import es.android.pantallaInfo.R;

import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PantallaInfo extends Activity implements OnClickListener {
		// Vista con el texto que se quiere mostrar al usuario
	private TextView textoSalida;
		// Se encarga de recuperar y dar formato a la imagen
	private FormatearImagen img;
		// Reproduce el archivo de audio
	private MediaPlayer mp;
//	private LectorFicheroXml lectorXml;
//	private String rutaXml;
//	private String imagen;
//	private String nombre;
private LectorFicheroXml lectorXML;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        String nombre;
        
        textoSalida = (TextView) this.findViewById(R.id.textoRespuestaInfo);
        
        img = new FormatearImagen();
        
        mp = new MediaPlayer();
        
//        rutaXml=    this.getIntent().getStringExtra("rutaXml");
  //      imagen= this.getIntent().getStringExtra("imagen");
        nombre= this.getIntent().getStringExtra("nombre");
        
        lectorXML=    (LectorFicheroXml) this.getIntent().getSerializableExtra("lectorXML");
        
        
        //System.out.println("la ruta del xml es "+rutaXml);
        
       // lectorXml = new LectorFicheroXml(rutaXml);
		
        textoSalida.setText(lectorXML.buscarRecurso(nombre, "texto"));
					
		ImageView imageView = (ImageView) findViewById(R.id.imagenInfo);
	//	InputStream is;
		imageView = img.recuperarImagen(lectorXML.buscarRecurso(nombre, "imagen"), imageView);
		
		try {
			if (mp.isPlaying()) {
				mp.reset();
			}
			mp.setDataSource(lectorXML.buscarRecurso(nombre, "musica"));
			mp.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        mp.start();
			
    }
 
    public void onDestroy() {
    	mp.stop();
    	super.onDestroy();
    }

	public void onClick(View arg0) {
		
	}
}