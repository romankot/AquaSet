package com.kremor.aquaset;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class ConnectActivity extends AppCompatActivity {

    static final String LOGGER_TAG = "ConnectActivity";
    static final int REQUEST_ENABLE_BT = 1;

    ArrayList<BluetoothDevice> m_devices;
    ArrayList<String> m_deviceNames;
    ArrayAdapter<String> m_arrayAdapter;

    public void connectDevice(int position) {
        Log.i(LOGGER_TAG, String.format("Connect to device: %d", position));
        BluetoothDevice device = m_devices.get(position);
        Intent connectIntent = new Intent(this, DeviceActivity.class);
        connectIntent.putExtra("device", device);
        startActivity(connectIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectactivity_main);

        m_devices = new ArrayList<BluetoothDevice>();
        m_deviceNames = new ArrayList<String>();
        m_deviceNames.add("No devices");
        m_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, m_deviceNames);
        ListView view = (ListView)findViewById(R.id.deviceList);
        view.setAdapter(m_arrayAdapter);
        view.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Log.i(LOGGER_TAG, String.format("Item clicked: %d", position));
                connectDevice(position);
            }
        });

        // Enable Bluetooth
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            Log.i(LOGGER_TAG, "Found Bluetooth adapter");
            if (!adapter.isEnabled()) {
                Log.i(LOGGER_TAG, "Bluetooth disabled, launch enable intent");
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            }
            if (adapter.isEnabled()) {
                Log.i(LOGGER_TAG, "Bluetooth enabled, find paired devices");
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                if (!devices.isEmpty()) {
                    m_devices.clear();
                    m_arrayAdapter.clear();
                    for (BluetoothDevice device : devices) {
                        Log.i(LOGGER_TAG, String.format("Found bluetooth device: name %s", device.getName()));
                        m_devices.add(device);
                        m_arrayAdapter.add(device.getName());
                    }
                }
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
