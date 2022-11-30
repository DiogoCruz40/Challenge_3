package pt.cm.challenge_3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.cm.challenge_3.Interfaces.ActivityInterface;
import pt.cm.challenge_3.Interfaces.FragmentInterface;
import pt.cm.challenge_3.dtos.PointDTO;


public class FragmentClass extends Fragment implements FragmentInterface {

    private SharedViewModel mViewModel;
    private ActivityInterface activityInterface;
    private View view;

    public FragmentClass() {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityInterface = (ActivityInterface) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_class, container, false);

        this.mViewModel = new ViewModelProvider(activityInterface.getmainactivity()).get(SharedViewModel.class);
        return view;
    }

    public void insertPointFiltered(PointDTO pointDTO) {

    }

    public void setInitLedState(String state) {
        Button button = (Button) view.findViewById(R.id.button);
        if (state.equals("true")) {
            button.setText("Led On");
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_bulb_on, 0, 0, 0);
        } else {
            button.setText("Led Off");
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_bulb_off, 0, 0, 0);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals("Led Off")) {
                    mViewModel.publishMessage("on", "LEDADM");
                    button.setText("Led On");
                    button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_bulb_on, 0, 0, 0);
                } else {
                    mViewModel.publishMessage("off", "LEDADM");
                    button.setText("Led Off");
                    button.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_bulb_off, 0, 0, 0);
                }
            }
        });
    }

    /*
    public void mqttMsgPopUp(String topic, MqttMessage message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getmainactivity());
        final View mqttmesagePopUp = getLayoutInflater().inflate(R.layout.mqtt_message_popup, null);
        Button confirm = (Button) mqttmesagePopUp.findViewById(R.id.confirmmsgbtnmqtt);
        Button cancel = (Button) mqttmesagePopUp.findViewById(R.id.cancelmsgbtnmqtt2);
        TextView topico = (TextView) mqttmesagePopUp.findViewById(R.id.topicmsgmqtt);
        TextView titulo = (TextView) mqttmesagePopUp.findViewById(R.id.titlemsgmqtt);
        NoteDTO noteDTO = new Gson().fromJson(message.toString(), NoteDTO.class);
        topico.setText(topic);
        titulo.setText(noteDTO.getTitle());

        dialogBuilder.setView(mqttmesagePopUp);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.insertMqttNote(noteDTO.getTitle(), noteDTO.getDescription());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }*/

}
