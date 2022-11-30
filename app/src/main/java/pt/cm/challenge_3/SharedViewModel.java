package pt.cm.challenge_3;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.cm.challenge_3.Interfaces.ActivityInterface;
import pt.cm.challenge_3.Interfaces.PointMapperInterface;
import pt.cm.challenge_3.database.AppDatabase;
import pt.cm.challenge_3.database.entities.Point;
import pt.cm.challenge_3.dtos.PointDTO;
import pt.cm.challenge_3.helpers.MQTTHelper;
import pt.cm.challenge_3.mappers.PointMapper;

public class SharedViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private final MutableLiveData<List<PointDTO>> points = new MutableLiveData<List<PointDTO>>();
    private final MutableLiveData<String> toastMessageObserver = new MutableLiveData<String>();
    private MQTTHelper mqttHelper;
    private static boolean trigger = true;
    public SharedViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<String> getToastMessageObserver() {
        return this.toastMessageObserver;
    }

    public void startDB() {
        mDb = AppDatabase.getInstance(getApplication().getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all points
                PointMapperInterface noteMapperInterface = new PointMapper();
                List<PointDTO> pointsDTO = noteMapperInterface.toPointsDTO(mDb.notesDAO().getAll());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (pointsDTO == null) {
                            points.setValue(new ArrayList<PointDTO>());
                        } else {
                            points.setValue(pointsDTO);
                        }

                    }
                });
            }
        });
    }


    public void insertPoint(Float temp, Float hum, String timestamp) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // insert point
                    PointMapperInterface noteMapperInterface = new PointMapper();
                    PointDTO pointsDTO = new PointDTO(timestamp,temp,hum);
                    pointsDTO.setId((int) mDb.notesDAO().insert(noteMapperInterface.toEntityPoint(pointsDTO)));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<PointDTO> aux = points.getValue();
                        if(aux != null) {
                            aux.add(pointsDTO);
                            points.setValue(aux);
                        }
                    }
                });
            }
        });
    }
    private void deleteAllPoints() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // delete all notes
                List<Point> pointsDB = mDb.notesDAO().getAll();

                for (Point n : pointsDB) {
                    mDb.notesDAO().delete(n);
                }

            }
        });
    }


    public void connmqtt(ActivityInterface activityInterface) {
        mqttHelper = new MQTTHelper(getApplication().getApplicationContext(), MqttClient.generateClientId());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                subscribeToTopic("TempHumADM");
                subscribeToTopic("LEDADM");
                toastMessageObserver.setValue("MQTT conn and sub successful");
                Log.w("mqtt", "connected");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("mqtt", cause);
                toastMessageObserver.setValue(cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (Objects.equals(topic, "TempHumADM")) {
                        if (trigger) {
                            publishMessage("state", "LEDADM");
                            trigger = false;
                        }
        //                {"humidity":12.56,"temperature":44.05,"timestamp":""}
                        PointDTO pointDTO = new Gson().fromJson(message.toString(), PointDTO.class);
                        if(pointDTO.getTimestamp() != null)
                        {
                        activityInterface.insertPointAct(pointDTO);
                        }
                    } else if (Objects.equals(topic, "LEDADM")) {
                        if(!message.toString().equals("state"))
                        {
                            activityInterface.setLedAct(message.toString());
                            unsubscribeToTopic("LEDADM");
                        }
                    }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        mqttHelper.connect();
    }


    public void disconmqtt() {
        mqttHelper.mqttAndroidClient.disconnect();
    }


    private boolean subscribeToTopic(String topic) {
        try {
            mqttHelper.subscribeToTopic(topic);
            return true;
        } catch (Exception e) {
            toastMessageObserver.setValue(e.getMessage());
            return false;
        }
    }

    private boolean unsubscribeToTopic(String topic) {
        try {
            mqttHelper.unsubscribeToTopic(topic);
            return true;
        } catch (Exception e) {
            toastMessageObserver.setValue(e.getMessage());
            return false;
        }
    }

    public void publishMessage(String signal, String topic) {
        try {
            byte[] encodedPayload;

//            Log.w("mqtt",signal);
            encodedPayload = signal.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);

            mqttHelper.mqttAndroidClient.publish(topic, message);
            // view set text to null
        } catch (Throwable e) {
            Log.w("mqtt", e.getMessage());
        }
    }
}