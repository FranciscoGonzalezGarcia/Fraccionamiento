package com.example.fraccionamiento.Controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionController {
    public static boolean checkInternetConnection(Context context){

        boolean connected = true;

        ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connection!=null){
            NetworkInfo[] networksInfo = connection.getAllNetworkInfo();

            for (int i = 0; i < networksInfo.length; i++){
                if (networksInfo[i].getState() == NetworkInfo.State.CONNECTED){
                    connected = false;
                }
            }

        }

        return connected;

    }
}
