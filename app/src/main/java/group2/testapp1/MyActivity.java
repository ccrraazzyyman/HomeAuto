package group2.testapp1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private ListView lv;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();
    private BluetoothSocket mmSocket[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView1);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        if(mBluetoothAdapter.isEnabled()) {
            pairedDevices = mBluetoothAdapter.getBondedDevices();

            ArrayList tList = new ArrayList();
            ArrayList<BluetoothDevice> tDevice = new ArrayList<>();
            for (BluetoothDevice bt : pairedDevices) {
                tList.add(bt.getName());
                tDevice.add(bt);
                Log.d("","Device name"+bt.getName()+": toString "+bt.toString());
                //Toast.makeText(getApplicationContext(), bt.getName(), Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tList);
            lv.setAdapter(adapter);
            btDeviceArray = tDevice;
        }
        mBluetoothAdapter.cancelDiscovery();
        Log.d("", "Size of bdDeviceArray = " + btDeviceArray.size());
        mmSocket = new BluetoothSocket[btDeviceArray.size()];
        for (int device = 0; device < btDeviceArray.size(); device++) {
            BluetoothDevice mmDevice = btDeviceArray.get(device);
                try {
                    String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
                    mmSocket[device] = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
                    mmSocket[device].connect();
                    Log.d("","Connected to device "+device);
                } catch (Exception e) {
                    Log.d("","Failed to connect to device "+device);
            }
        }
    }

    protected void reconnect(){
        for (int device = 0; device < btDeviceArray.size(); device++) {
            BluetoothDevice mmDevice = btDeviceArray.get(device);
            BluetoothSocket tSocket;
            try {
                String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
                tSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
                tSocket.connect();
                mmSocket[device] = tSocket;
                Log.d("","Connected to device "+device);
            } catch (Exception e) {
                Log.d("","Failed to connect to device "+device);
            }
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        for (int i = 0; i < btDeviceArray.size(); i++){
            try{mmSocket[i].close();
                Log.d("","Closed socket '"+i+"' onPause()");
            }
            catch (Exception e){
                Log.d("", "Failed to close socket "+ i);
            }
        }
    }
/*    @Override
    protected void onResume(){
        super.onResume();
        reconnect();
    }*/
/*    @Override
    protected void onStop(){
        super.onStop();
        for (int i = 0; i < btDeviceArray.size(); i++){
            try{mmSocket[i].close();
                Log.d("","Closed socket '"+i+"' onPause()");
            }
            catch (Exception e){
                Log.d("", "Failed to close socket "+ i);
            }
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.reconnect){
            reconnect();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void openLights(View view) {
        Intent intent = new Intent(this, Lights.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }

    public void startBT(View view){
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            //Toast didn't make sense as it was displaying the same time as the confirmation window
            //Toast.makeText(getApplicationContext(),"Bluetooth Enabled",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(),"Bluetooth Already Enabled",Toast.LENGTH_SHORT).show();
    }
    public void discoverableBT(View view){
        if (mBluetoothAdapter.isEnabled()) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
            startActivity(discoverableIntent);
        }
        //Toast didn't make sense as it was displayed before accepting discoverable
        //Toast.makeText(getApplicationContext(),"Discoverable for 30 Seconds",Toast.LENGTH_SHORT).show();
    }
    public void disableBT(View view){
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(),"Bluetooth Disabled",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(),"Bluetooth Already Disabled",Toast.LENGTH_SHORT).show();
    }

    public void discoverBT(View view){
        Toast.makeText(getApplicationContext(),"Not Implemented Yet",Toast.LENGTH_SHORT).show();
    }
    public void listPairedDevices(View view){
        //Toast.makeText(getApplicationContext(),"Not Implemented Yet",Toast.LENGTH_SHORT).show();
        if(mBluetoothAdapter.isEnabled()) {
            pairedDevices = mBluetoothAdapter.getBondedDevices();

            ArrayList tList = new ArrayList();
            ArrayList<BluetoothDevice> tDevice = new ArrayList<>();
            for (BluetoothDevice bt : pairedDevices) {
                tList.add(bt.getName());
                tDevice.add(bt);
                Toast.makeText(getApplicationContext(), bt.getName(), Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tList);
            lv.setAdapter(adapter);
            btDeviceArray = tDevice;
        }
        else
            Toast.makeText(getApplicationContext(),"Please Enable Bluetooth First",Toast.LENGTH_SHORT).show();
    }
    public void lightOn(View view){
        //pairedDevices = mBluetoothAdapter.getBondedDevices();
        try {
/*            String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
            mmSocket.connect();*/
            char[] buffer = new char[1];
            buffer[0] = '1';
            byte [] buffer2 = new byte[1];
            //Toast.makeText(getApplicationContext(),String.valueOf(buffer[0]),Toast.LENGTH_SHORT).show();
            OutputStream mmOutStream = mmSocket[0].getOutputStream();
            InputStream mmInStream = mmSocket[0].getInputStream();
            mmOutStream.write(buffer[0]);
            mmInStream.read(buffer2);
            Log.d("","Read the following : '"+(char)buffer2[0]+"'");
/*            mmSocket.close();*/
        } catch (Exception e) {
            Log.d("","Failed to Turn On");
            reconnect();
            Toast.makeText(getApplicationContext(),"Error: Device not connected",Toast.LENGTH_SHORT).show();
        }
    }
    public void lightOff(View view){
        //pairedDevices = mBluetoothAdapter.getBondedDevices();
/*        BluetoothSocket mmSocket;
        BluetoothDevice mmDevice = btDeviceArray.get(0);*/
        try {
/*            String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
            mmSocket.connect();*/
            char[] buffer = new char[1];
            buffer[0] = '2';
            byte [] buffer2 = new byte[1];
            //Toast.makeText(getApplicationContext(),String.valueOf(buffer[0]),Toast.LENGTH_SHORT).show();
            OutputStream mmOutStream = mmSocket[0].getOutputStream();
            InputStream mmInStream = mmSocket[0].getInputStream();
            mmOutStream.write(buffer[0]);
            mmInStream.read(buffer2);
            Log.d(""," Read the following : '"+(char)buffer2[0]+"'");
/*            mmSocket.close();*/
        } catch (Exception e) {
            Log.d("","Failed to Turn Off");
            reconnect();
            Toast.makeText(getApplicationContext(),"Error: Device not connected",Toast.LENGTH_SHORT).show();
        }
    }
    public void toggleLight(View view){
        //pairedDevices = mBluetoothAdapter.getBondedDevices();
/*        BluetoothSocket mmSocket;
        BluetoothDevice mmDevice = btDeviceArray.get(0);*/
        try {
            char[] buffer = new char[1];
            switch(view.getId()){
                case R.id.Button1: //light on
                    buffer[0] = '1';
                    break;
                case R.id.Button2: //light off
                    buffer[0] = '2';
            }
/*            String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
            mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
            mmSocket.connect();*/
            byte [] buffer2 = new byte[1];
            //Toast.makeText(getApplicationContext(),String.valueOf(buffer[0]),Toast.LENGTH_SHORT).show();
            OutputStream mmOutStream = mmSocket[0].getOutputStream();
            InputStream mmInStream = mmSocket[0].getInputStream();
            mmOutStream.write(buffer[0]);
            mmInStream.read(buffer2);
            Log.d(""," Read the following : '"+(char)buffer2[0]+"'");
/*            mmSocket.close();*/
        } catch (Exception e) {
            Log.d("","Failed to toggle light");
            reconnect();
            Toast.makeText(getApplicationContext(),"Error: Device not connected",Toast.LENGTH_SHORT).show();
        }
    }
}

