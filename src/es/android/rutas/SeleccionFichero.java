package es.android.rutas;

import java.io.File;
import java.util.ArrayList;
//import java.util.List;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SeleccionFichero extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
   
 //   private  EditText textoArchivo;
  //  private TextView textoError ;
    private Button botonFichero;
 //   private File archivo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccionfichero);
        botonFichero = (Button) findViewById(R.id.examinar);
        botonFichero.setOnClickListener(this);
        
        Button botonSalir = (Button) findViewById(R.id.BotonSalir);
        botonSalir.setOnClickListener(this);
        
 //       textoError = (TextView) findViewById(R.id.error);
  //      textoArchivo=(EditText) findViewById(R.id.textoFile);
    }

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		if (arg0.getId() == botonFichero.getId()) {

//			String subrutaIni = "/sdcard/";
//			String subrutaFin = ".xml";
//			String ruta = subrutaIni + textoArchivo.getText().toString()
//					+ subrutaFin;

			// File archivo= new File(((TextView) texto).getText().toString());
		//	 archivo = new File(ruta);

//			if (!archivo.exists()) {
//				textoError.setText("El fichero no existe");
//			} else {

//				LectorFicheroXml lector = new LectorFicheroXml(ruta);

				Intent mIntent = new Intent(this, Examinar.class);
			
	//			ArrayList<String> categorias = (ArrayList<String>) lector.getCategorias();

	//			mIntent.putExtra("rutaXml", ruta);
	//			mIntent.putStringArrayListExtra("categorias", categorias);

				startActivity(mIntent);
				this.finish();
		//		archivo = null;

			//}
		} else {
	//		archivo = null;
			this.finish();

		}

	}
}