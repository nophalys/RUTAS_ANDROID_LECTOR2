package es.android.rutas;

import java.util.ArrayList;

//import android.content.Context;
import android.graphics.drawable.Drawable;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
//import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PintaPuntos extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	//private Context contexto;
	
	
	public PintaPuntos(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	public void addOverlay(OverlayItem overlay) {
		//public void addOverlay(OverlayItem overlay, Context contexto) {
	    mOverlays.add(overlay);
	    populate();
	//    this.contexto=contexto;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
//	@Override
//	protected boolean onTap(int i) {
//		Toast toast= Toast.makeText(contexto, mOverlays.get(i).getSnippet(), 1000);
//		//toast.setGravity(Gravity.BOTTOM, 0, 0);
//		toast.show();
//	
//
//	return(true);
//	}


	
	

}
