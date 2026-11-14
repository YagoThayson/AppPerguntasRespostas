
package com.example.appperguntasrespostas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.widget.Toast;

public class ConexaoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager gerenciador = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Network rede = gerenciador.getActiveNetwork();

        NetworkCapabilities capacidades =
                gerenciador.getNetworkCapabilities(rede);

        if (capacidades != null &&
                capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {

            Log.d("CONEXAO", "Internet disponível!");
            Toast.makeText(context, "Internet disponível!", Toast.LENGTH_SHORT).show();

        } else {

            Log.d("CONEXAO", "Sem conexão com a internet!");
            Toast.makeText(context, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
        }
    }
}
