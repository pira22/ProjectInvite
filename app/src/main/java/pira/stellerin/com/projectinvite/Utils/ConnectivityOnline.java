package pira.stellerin.com.projectinvite.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityOnline {
	Context mContext;

	public ConnectivityOnline(Context mContext) {
		this.mContext = mContext;
	}

	public boolean isOnline() {
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		//For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		            .isConnectedOrConnecting();
		//For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		            .isConnectedOrConnecting();
		if (!is3g && !isWifi) 
		{ 
			return false;
		} 
		 else 
		{ 
		       return true ;
		} 
		

	}

	
	/**
	 * Get the network info
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(Context context){
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    return cm.getActiveNetworkInfo();
	}
	
	/**
	 * Check if there is any connectivity
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context){
	    NetworkInfo info = ConnectivityOnline.getNetworkInfo(context);
	    return (info != null && info.isConnected());
	}
	
	/**
	 * Check if there is any connectivity to a Wifi network
	 * @param context
	 * @param type
	 * @return
	 */
	public static boolean isConnectedWifi(Context context){
	    NetworkInfo info = ConnectivityOnline.getNetworkInfo(context);
	    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
	}
	
	/**
	 * Check if there is any connectivity to a mobile network
	 * @param context
	 * @param type
	 * @return
	 */
	public static boolean isConnectedMobile(Context context){
	    NetworkInfo info = ConnectivityOnline.getNetworkInfo(context);
	    return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
	}
	
	

}
