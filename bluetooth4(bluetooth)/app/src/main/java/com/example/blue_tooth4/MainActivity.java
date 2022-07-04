package com.example.blue_tooth4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int SOLICITA_CONEXAO = 2;
    private static final int MESSAGE_READ =3;

    StringBuilder dadosBluetooth = new StringBuilder();
    String dadosCompletos = new String(" ");
    //String dadosFinals = new String(" ");
    public String title = " ";

    public String[]names= title.split(" ");

    public String nhan = new String(" ");
  //  public String[]tach= new String[3];
  public String[]tach= new String[]{};


    ConnectedThread connectedThread;

    GridView gridView;
    Button btnConexao,btnLed1;
    ImageView mBlueIV;
    Button mOnBtn, mOffBtn;
    TextView thetich,percent;
    //EditText setx,sety, settheta;

    BluetoothAdapter meuBluetoothAdapter = null;
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSocket = null;
    boolean conexao = false;
    CustomAdapter customAdapter;

    private static String MAC = null;
    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    Handler mhandler;

    public void call(){
        if (customAdapter==null){
            customAdapter= new CustomAdapter(names,this);
            gridView.setAdapter(customAdapter);
        }else {
            customAdapter.setImageNames(names);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        mBlueIV = findViewById(R.id.bluetoothIv);
        mOnBtn = findViewById(R.id.onButn);
        mOffBtn = findViewById(R.id.offButn);
        btnConexao=(Button)findViewById(R.id.btnConexao);
        btnLed1=findViewById(R.id.start);
        thetich=findViewById(R.id.tvthe_tich);
        percent=findViewById(R.id.tvphan_tram);
        gridView = findViewById(R.id.gridView);
//        setx = findViewById(R.id.setx);
//        sety = findViewById(R.id.sety);
//        settheta = findViewById(R.id.settheta);
        //btnLed2=(Button)findViewById(R.id.the_tich);
        //btnLed3=(Button)findViewById(R.id.phan_tram);

        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //call();

        //call();
//        if (bluetoothAdapter == null) {
//            mStatusBleTv.setText("Bluetooth is not available");
//        } else {
//            mStatusBleTv.setText("Bluetooth is  available");


        if (meuBluetoothAdapter.isEnabled()) {
            mBlueIV.setImageResource(R.drawable.bluetooth_on);
        } else {
            mBlueIV.setImageResource(R.drawable.bluetooth_off);
        }

        btnConexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(conexao){
                    try {
                        meuSocket.close();
                        conexao = false;
                        btnConexao.setText("kết nối thiết bị");
                        Toast.makeText(getApplicationContext(),"đã ngắt kết nối thiết bị", Toast.LENGTH_SHORT).show();

                    }catch (IOException erro){
                        Toast.makeText(getApplicationContext(),"rất tiếc có lỗi đã xảy ra: " + erro, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Intent abreLista = new Intent(MainActivity.this,ListaDispositivos.class);
                    startActivityForResult(abreLista,SOLICITA_CONEXAO);
                }
            }
        });

        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override

            public void onClick(View v) {
               // btnLed1.setText("NHẤN VÀO ĐỂ TÍNH TOÁN");
                if (!meuBluetoothAdapter.isEnabled()) {
                    showToast("Turning on Bluetooth..");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);

                } else {
                    showToast("Bluetooth is already on");

                }
            }
        });

//            mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("MissingPermission")
//                @Override
//                public void onClick(View v) {
//                    if (!bluetoothAdapter.isDiscovering()) {
//                        showToast("Making Your Device Discoverable");
//                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                        startActivityForResult(intent, REQUEST_DISCOVER_BT);
//                    }
//                }
//            });
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                btnConexao.setText("Kết nối thiết bị");
                conexao = false;



                if (meuBluetoothAdapter.isEnabled()) {
                    meuBluetoothAdapter.disable();
                   // btnLed1.setText("MỞ LẠI BLUETOOTH");
                    showToast("Turning  Bluetooth off");
                    mBlueIV.setImageResource(R.drawable.bluetooth_off);

                } else {

                    showToast("Bluetooth is already off");


                }
            }
        });

//            mPairedBtn.setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("MissingPermission")
//                @Override
//                public void onClick(View v) {
//                    if (bluetoothAdapter.isEnabled()) {
//                        mPairedTv.setText("Paired Devices");
//                        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
//                        for (BluetoothDevice device : devices) {
//                            mPairedTv.append("\n Device : " + device.getName() + " , " + device);
//                        }
//                    } else {
//                        showToast("Turn On bluetooth to get paired devices");
//                    }
//                }
//            });
//        }
        btnLed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dadosBluetooth.delete(0,dadosBluetooth.length());
                 if(conexao){
//                     String X = setx.getText().toString();
//                     String Y = sety.getText().toString();
//                     String Theta = settheta.getText().toString();
//                     String truyen = X + Y + Theta;
//                     String te = "{1;1;90}";
//                     connectedThread.enviar(te);
//                     connectedThread.enviar("\n");
//                     connectedThread.enviar(truyen);
//                     connectedThread.enviar("\n");
                    connectedThread.enviar("\n");
                    connectedThread.enviar("led1\n");
                    btnLed1.setText("Đang tính , không ấn vô");
                }else {
                    Toast.makeText(getApplicationContext(),"Vui lòng kết nối thiết bị trước tiên", Toast.LENGTH_SHORT).show();
                    //bluetooth nao estaconetado
                }
            }
        });

        mhandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == MESSAGE_READ);

                String recebiodos = (String) msg.obj;
                dadosBluetooth.append(recebiodos);


                int fimInfomaco = dadosBluetooth.indexOf("]");

                if(fimInfomaco >0){
                    dadosCompletos = dadosBluetooth.substring(0,  fimInfomaco);
                    nhan=dadosCompletos;
                    tach=nhan.split("/");

                    int tamInfomacao =  tach[0].length();
                    int tamInfomacao1 = tach[1].length();
                    int tamInfomacao2 = tach[2].length();

                    if(tach[0].charAt(0) == '{'){
                        String dadosFinals = tach[0].substring(1, tamInfomacao);
                        title = dadosFinals;
                        names=title.split(" ");
                        Log.e("tienld", "handleMessage: " + names );
                        call();

                  //      System.out.println(title);
//                        if(dadosFinals.contains("ledon")){
//                            btnLed1.setText("đang tính toán");
//
//                        }else if(dadosFinals.contains("ledoff")){
//                            btnLed1.setText("hoàn thành tính toán");
//                        }
                    }
                     if(tach[1].charAt(0) == 'a'){
                        String dadosFinals1 = tach[1].substring(1,tamInfomacao1);
                        thetich.setText(dadosFinals1);
                    }
                     if(tach[2].charAt(0) == 'c'){
                        String dadosFinals2 = tach[2].substring(1,tamInfomacao2);
                        percent.setText(dadosFinals2);
                    }
//                    dadosBluetooth.replace(0,dadosBluetooth.length(), " ") ;
//                    dadosCompletos = dadosBluetooth.substring(0,fimInfomaco);

                }
                //dadosBluetooth.delete(0,dadosBluetooth.length());
                btnLed1.setText("XONG, NHẤN ĐỂ TÍNH LẠI");

               // dadosBluetooth.replace(0,dadosBluetooth.length(), " ") ;


            }
        };

    }

    @SuppressLint({"MissingPermission", "MissingSuperCall"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    mBlueIV.setImageResource(R.drawable.bluetooth_on);
                    showToast("Bluetooth is On");
                } else {
                    showToast("Bluetooth is Off");

                }
                break;
            case SOLICITA_CONEXAO:
                if(resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    Toast.makeText(getApplicationContext(),"MAC FINAL" +MAC, Toast.LENGTH_SHORT).show();
                    meuDevice = meuBluetoothAdapter.getRemoteDevice(MAC);


                    try {
                        meuSocket = meuDevice.createRfcommSocketToServiceRecord(MEU_UUID);
                        meuSocket.connect();

                        conexao = true;
                        connectedThread = new ConnectedThread(meuSocket);
                        connectedThread.start();

                        btnConexao.setText("hủy kết nối");//desconetar
                        Toast.makeText(getApplicationContext(),"kết nối thành công", Toast.LENGTH_SHORT).show();

                    }catch (IOException erro){
                        conexao = false;
                        Toast.makeText(getApplicationContext(),"kết nối thất bại" + erro, Toast.LENGTH_SHORT).show();

                    }


                }else {
                    Toast.makeText(getApplicationContext(),"không tìm thấy địa chỉ MAC", Toast.LENGTH_SHORT).show();
                }
        }
       // super.onActivityResult(requestCode, resultCode, data);

    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();

                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
//                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    String dodasBt = new String(mmBuffer,0,numBytes);

                    mhandler.obtainMessage(MESSAGE_READ,numBytes, -1, dodasBt).sendToTarget();
                } catch (IOException e) {

                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void enviar(String dadosEnviar) {
            byte[] msgBuffer = dadosEnviar.getBytes();

            try {
                mmOutStream.write(msgBuffer);


            } catch (IOException e) {

            }
        }


    }
    public class CustomAdapter extends BaseAdapter {
        private String[] imageNames;
        private Context context;
        private final LayoutInflater layoutInflater;

        public CustomAdapter(String[] imageNames, Context context) {
            this.imageNames = imageNames;
            this.context = context;
            this.layoutInflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        public void setImageNames(String[] imageNames){
            this.imageNames = imageNames;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return imageNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=layoutInflater.inflate(R.layout.row_items,parent,false);
            }
            TextView tvName=convertView.findViewById(R.id.tvName);
            tvName.setText(imageNames[position]);
            return convertView;
        }
    }

}