package gub.app.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //declare classes and Interface
    SensorManager sensorManager;
    Sensor light, proximity, accelerometer, gyroscope;
    TextView txtls, txp, txacm, txgyr;

    CardView cdv1, cdv2, cdv3, cdv4;

    AsyncTask<Void, Void, Void> myAsyncTask = new MyAsyncTask();

    DB_Charts_Activity.MyDbHelper dbHandler = new DB_Charts_Activity.MyDbHelper(MainActivity.this);
    Light_DataActivity.MyDbHelper2 dbHandler2 = new Light_DataActivity.MyDbHelper2(MainActivity.this);
    accelerometerActivity.MyDbHelper3 dbhandler3 = new accelerometerActivity.MyDbHelper3(MainActivity.this);
    gyroscopeActivity.MyDbHelper4 dbhandler4 = new gyroscopeActivity.MyDbHelper4(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Find Text view id
        txtls = findViewById(R.id.lightID);
        txp = findViewById(R.id.proximityID);
        txacm = findViewById(R.id.accelerometerID);
        txgyr = findViewById(R.id.gyroscopeID);


        //Find Id for Card view
        cdv1 = findViewById(R.id.lihgtSensor);
        cdv2 = findViewById(R.id.proximity);
        cdv3 = findViewById(R.id.accelerometer);
        cdv4 = findViewById(R.id.gyroscope);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        myAsyncTask.execute();


        //Intent for going another activity
        cdv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Light_DataActivity.class);
                startActivity(intent);
            }
        });
        cdv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, DB_Charts_Activity.class);
                startActivity(intent2);
            }
        });
        cdv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, accelerometerActivity.class);
                startActivity(intent3);
            }
        });
        cdv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this, gyroscopeActivity.class);
                startActivity(intent4);
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Do some long-running task
            // ...

            LightSensor();
            ProximitySensor();
            AccelerometerSensor();
            Gyroscope();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update the user interface
            // ...
        }
    }


    public void LightSensor() {
        SensorEventListener listener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                txtls.setText(String.valueOf(event.values[0]) + " lx");
                int grayShade = (int) event.values[0];
                if (grayShade > 255) grayShade = 255;

                txtls.setTextColor(Color.rgb(255 - grayShade, 255 - grayShade, 255 - grayShade));
                txtls.setBackgroundColor(Color.rgb(grayShade, grayShade, grayShade));

                Timer t = new Timer();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        dbHandler2.insertLightData(String.valueOf(event.values[0] + " Lx"));
                    }
                };
                t.scheduleAtFixedRate(tt, 0, 300000);


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(listener, light, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void ProximitySensor() {

        SensorEventListener proximitySensorEventListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // method to check accuracy changed in sensor.
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    if (event.values[0] == 0) {

                        // if sensor event return 0 then object is closed
                        // to sensor else object is away from sensor.
                        txp.setText(String.valueOf("Near"));

                        Timer t = new Timer();
                        TimerTask tt = new TimerTask() {
                            @Override
                            public void run() {

                                dbHandler.insertUserDetailsProx(String.valueOf("Near"));


                            }
                        };
                        t.scheduleAtFixedRate(tt, 0, 300000);


                    } else {
                        txp.setText(String.valueOf("Away"));
                        Timer t = new Timer();
                        TimerTask tt = new TimerTask() {
                            @Override
                            public void run() {

                                dbHandler.insertUserDetailsProx(String.valueOf("Away"));


                            }
                        };
                        t.scheduleAtFixedRate(tt, 0, 300000);


                    }
                }
            }
        };
        sensorManager.registerListener(proximitySensorEventListener, proximity, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void AccelerometerSensor() {

        SensorEventListener ACM = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float xValue = Math.abs(event.values[0]);
                float yValue = Math.abs(event.values[1]);
                float zValue = Math.abs(event.values[2]);
                if (xValue > 15 || yValue > 15 || zValue > 15) {
                    // message
                    txacm.setText(String.valueOf("Moving"));

                    Timer t = new Timer();
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            dbhandler3.insertAcceloData(String.valueOf("Moving"));

                        }
                    };
                    t.scheduleAtFixedRate(tt, 0, 300000);

                } else {
                    txacm.setText(String.valueOf("Not Moving"));

                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        sensorManager.registerListener(ACM, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void Gyroscope() {
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[2] > 0.5f) {
                    txgyr.setText(String.valueOf("Rotate:AntiClockwise"));

                    Timer t = new Timer();
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            dbhandler4.insertLightData(String.valueOf("Rotate:AntiClockwise"));
                        }
                    };
                    t.scheduleAtFixedRate(tt, 0, 300000);


                } else if (sensorEvent.values[2] < -0.5f) {
                    txgyr.setText(String.valueOf("Rotate:Clockwise"));
                    Timer t = new Timer();
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            dbhandler4.insertLightData(String.valueOf("Rotate:Clockwise"));
                        }
                    };
                    t.scheduleAtFixedRate(tt, 0, 300000);

                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(gyroscopeSensorListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }


}