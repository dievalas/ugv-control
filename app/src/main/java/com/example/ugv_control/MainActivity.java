package com.example.ugv_control;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnW, btnA, btnS, btnD, btnX;
    Button btnI, btnJ, btnK, btnL, btnO;

    private UsbSerialPort serialPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnW = findViewById(R.id.btnW);
        btnA = findViewById(R.id.btnA);
        btnS = findViewById(R.id.btnS);
        btnD = findViewById(R.id.btnD);
        btnX = findViewById(R.id.btnX);

        btnI = findViewById(R.id.btnI);
        btnJ = findViewById(R.id.btnJ);
        btnK = findViewById(R.id.btnK);
        btnL = findViewById(R.id.btnL);
        btnO = findViewById(R.id.btnO);

        initUsb();

        btnW.setOnClickListener(v -> send("W"));
        btnA.setOnClickListener(v -> send("A"));
        btnS.setOnClickListener(v -> send("S"));
        btnD.setOnClickListener(v -> send("D"));
        btnX.setOnClickListener(v -> send("X"));

        btnI.setOnClickListener(v -> send("I"));
        btnJ.setOnClickListener(v -> send("J"));
        btnK.setOnClickListener(v -> send("K"));
        btnL.setOnClickListener(v -> send("L"));
        btnO.setOnClickListener(v -> send("O"));
    }

    private void initUsb() {

        try {

            UsbManager manager =
                    (UsbManager) getSystemService(USB_SERVICE);

            List<UsbSerialDriver> drivers =
                    UsbSerialProber.getDefaultProber()
                            .findAllDrivers(manager);

            if (drivers.isEmpty()) {
                Toast.makeText(this,
                        "FT232 nerastas",
                        Toast.LENGTH_LONG).show();
                return;
            }

            UsbSerialDriver driver = drivers.get(0);

            UsbDeviceConnection connection =
                    manager.openDevice(driver.getDevice());

            if (connection == null) {
                Toast.makeText(this,
                        "Nera USB leidimo",
                        Toast.LENGTH_LONG).show();
                return;
            }

            serialPort = driver.getPorts().get(0);

            serialPort.open(connection);

            serialPort.setParameters(
                    9600,
                    8,
                    UsbSerialPort.STOPBITS_1,
                    UsbSerialPort.PARITY_NONE
            );

            Toast.makeText(this,
                    "FT232 prijungtas",
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    "USB klaida: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void send(String cmd) {

        try {

            if (serialPort != null) {

                serialPort.write(
                        (cmd + "\n").getBytes(),
                        1000
                );
            }

            Toast.makeText(this,
                    "Siunciu: " + cmd,
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    "Klaida: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        try {

            if (serialPort != null) {
                serialPort.close();
            }

        } catch (Exception ignored) {
        }
    }
}