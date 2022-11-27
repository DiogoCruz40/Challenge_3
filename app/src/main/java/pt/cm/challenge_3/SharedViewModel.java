package pt.cm.challenge_3;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.cm.challenge_3.database.AppDatabase;

public class SharedViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    //private final MutableLiveData<List<NoteDTO>> notes = new MutableLiveData<List<NoteDTO>>();
    //private final MutableLiveData<String> toastMessageObserver = new MutableLiveData<String>();
    //private final MutableLiveData<List<String>> topics = new MutableLiveData<List<String>>();
    //private MQTTHelper mqttHelper;

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }


    public void startDB() {
        mDb = AppDatabase.getInstance(getApplication().getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // how to get all points
                //NoteMapperInterface noteMapperInterface = new NoteMapper();
                //List<NoteDTO> notesDTO = noteMapperInterface.toNotesDTO(mDb.notesDAO().getAll());

                /*
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (pointsDTO == null) {
                            points.setValue(new ArrayList<PointDTO>());
                        } else {
                            points.setValue(pointsDTO);
                        }

                    }
                });*/
            }
        });
    }

    /*
    private void deleteAllNotes() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // delete all notes
                List<Note> notesBD = mDb.notesDAO().getAll();

                for (Note n : notesBD) {
                    mDb.notesDAO().delete(n);
                }

            }
        });
    }*/

    /*
    public void connmqtt(ActivityInterface activityInterface) {
        mqttHelper = new MQTTHelper(getApplication().getApplicationContext(), MqttClient.generateClientId());

        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                toastMessageObserver.setValue("MQTT conn successful");
                Log.w("mqtt", "connected");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("mqtt", cause);
                toastMessageObserver.setValue(cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                NoteDTO noteDTO = new Gson().fromJson(message.toString(), NoteDTO.class);
//                Log.w("mqtt", noteDTO.getTitle());
//                {"title":"nota","description":"wazzup"}
                activityInterface.msgmqttpopup(topic,message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        mqttHelper.connect();
    }
    */

    /*
    public void disconmqtt()
    {
        mqttHelper.mqttAndroidClient.disconnect();
    }
    */

    /*
    public boolean subscribeToTopic(String topic) {
        try {
            mqttHelper.subscribeToTopic(topic);
            List<String> listaux = topics.getValue();

            if (listaux == null)
                listaux = new ArrayList<String>();
            if (listaux.contains(topic))
                return false;

            listaux.add(topic);
            topics.setValue(listaux);
            return true;
        } catch (Exception e) {
            toastMessageObserver.setValue(e.getMessage());
            return false;
        }
    }*/

    /*
    public boolean unsubscribeToTopic(String topic) {
        try {
            mqttHelper.unsubscribeToTopic(topic);
            List<String> listaux = topics.getValue();
            listaux.remove(topic);
            topics.setValue(listaux);
            return true;
        } catch (Exception e) {
            toastMessageObserver.setValue(e.getMessage());
            return false;
        }
    }*/

    /*
    public void publishMessage(NoteDTO noteDTO, String topic) {
        try {
            byte[] encodedPayload;

            String msg = "{\"title\":\"" + noteDTO.getTitle()+"\"" + ",\"description\":\"" + noteDTO.getDescription() + "\"}";
//            Log.w("mqtt",msg);
            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);

            mqttHelper.mqttAndroidClient.publish(topic, message);
            // view set text to null
        } catch (Throwable e) {
            Log.w("mqtt", e.getMessage());
        }
    }*/
}