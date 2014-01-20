package es.android.rutas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class FormatearImagen {
    /** Called when the activity is first created. */
    public ImageView recuperarImagen(String ruta, ImageView imageView) {
      
		//   BitmapFactory.decodeFile(pathName)
		// load the origial BitMap (500 x 500 px)
		Bitmap bitmapOrg = BitmapFactory.decodeFile(ruta);
		 
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
	//	int newWidth = 150;
	//	int newHeight = 150;
		
		int newWidth = 300;
		int newHeight = 300;
		
		
		
		 
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		 
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// rotate the Bitmap
		//matrix.postRotate(45);
		
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
		                      width, height, matrix, true);
		 
		// make a Drawable from Bitmap to allow to set the BitMap
		// to the ImageView, ImageButton or what ever
		BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		  
		imageView.setImageDrawable(bmd);
		//imageView.setScaleType(ScaleType.CENTER);
		
		return imageView;
    }
}