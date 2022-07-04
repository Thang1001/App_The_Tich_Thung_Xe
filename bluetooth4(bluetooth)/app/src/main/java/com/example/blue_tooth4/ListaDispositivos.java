package com.example.blue_tooth4;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Set;

public class ListaDispositivos extends ListActivity {

    private BluetoothAdapter meuBluetoothAdpater2 = null; //myBluetoothAdapter
    static String ENDERECO_MAC = null;// Mac_address

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        meuBluetoothAdpater2 = BluetoothAdapter.getDefaultAdapter();
        @SuppressLint("MissingPermission") Set<BluetoothDevice> dispositivosPareados = meuBluetoothAdpater2.getBondedDevices();

        if(dispositivosPareados.size()>0){ //Thiết bị được ghép nối Paired_Device
            for(BluetoothDevice dispositivo : dispositivosPareados){
                @SuppressLint("MissingPermission")
                String nomeBt=dispositivo.getName();
                String macBt=dispositivo.getAddress();
                ArrayBluetooth.add(nomeBt+"\n"+macBt);
            }
        }
        setListAdapter(ArrayBluetooth);
    }
//lay dia chi mac va ten thiet bi de ket noi....
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView) v).getText().toString();
       // Toast.makeText(getApplicationContext(), "Info: " + informacaoGeral,Toast.LENGTH_SHORT).show();

        String enderreoMac = informacaoGeral.substring(informacaoGeral.length() - 17);
       // Toast.makeText(getApplicationContext(), "mac: " + informacaoGeral,Toast.LENGTH_SHORT).show();

        Intent retornaMac  = new Intent();
        retornaMac.putExtra(ENDERECO_MAC,enderreoMac);

        setResult(RESULT_OK,retornaMac);
        finish();

    }
}
