package es.android.rutas;
import java.util.ArrayList;

import android.widget.BaseAdapter;

import android.net.Uri;
import android.view.*;
import android.content.*;
import android.widget.*;

public class AdaptadorImagen extends BaseAdapter {
    private Context mContext;
    
   // private Uri[] imagenes ={Uri.parse("/sdcard/dcim/cena.jpg"),Uri.parse("/sdcard/dcim/lagrimas.jpg")};
    
    private Uri[] imagenes;
    

    public AdaptadorImagen(Context c, int totalImagenes, ArrayList<String> arrayImagenes) {
        mContext = c;
       // imagenes = new Uri[2];
        imagenes = new Uri[totalImagenes];
        for (int i=0;i<totalImagenes;i++){
        	imagenes[i]=Uri.parse(arrayImagenes.get(i));
        	
        }
       // imagenes[0]=Uri.parse("/sdcard/imagenes/cenaGrid.jpg");
       // imagenes[1]=Uri.parse("/sdcard/imagenes/lagrimasGrid.jpg");
    }

    public int getCount() {
      //  return mThumbIds.length;
    	return imagenes.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    
    public void SetCountGrid(int max){
    	

    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }

        //para insertar las imagenes desde la carpeta de res
        
        
       
        //imageView.setImageResource(mThumbIds[position]);
 
        
        imageView.setImageURI(imagenes[position]);
        
        
  //    Uri uriAnd=  Uri.parse("/sdcard/dcim/icon1.png");
  //      imageView.setImageURI(uriAnd);
        
        
        //imageView.setImageURI(uri)
        return imageView;
    }

    

    
    
    
    
    
    
    
    // references to our images
    
//    private Integer[] mThumbIds = {
//            R.drawable.sample_0, R.drawable.sample_1,
//            R.drawable.sample_2, R.drawable.sample_3,
//            R.drawable.sample_4, R.drawable.sample_5,
//            
//            
            
   //         R.drawable.sample_0, R.drawable.sample_1,
            
            
  //          R.drawable.sample_2, R.drawable.sample_3,
  //          R.drawable.sample_4, R.drawable.sample_5,
            
            
//            R.drawable.sample_6, R.drawable.sample_7,
//            R.drawable.sample_0, R.drawable.sample_1,
//            R.drawable.sample_2, R.drawable.sample_3,
//            R.drawable.sample_4, R.drawable.sample_5,
//            R.drawable.sample_6, R.drawable.sample_7
  

//};
}