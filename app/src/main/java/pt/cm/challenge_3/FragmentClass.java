package pt.cm.challenge_3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;

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
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activityInterface.getmainactivity(), android.R.layout.simple_spinner_item, Arrays.asList("All", "Temperature", "Humidity"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;
    }

    public void insertPointFiltered(PointDTO pointDTO) {
        Spinner spinner = view.findViewById(R.id.spinner);
        if(spinner.getSelectedItem().toString().equals("All"))
        {
            mViewModel.insertPoint(pointDTO.getTemperature(),pointDTO.getHumidity(),pointDTO.getTimestamp());
        }
        else if(spinner.getSelectedItem().toString().equals("Temperature"))
        {
            mViewModel.insertPoint(pointDTO.getTemperature(),null,pointDTO.getTimestamp());
        }
        else if(spinner.getSelectedItem().toString().equals("Humidity"))
        {
            mViewModel.insertPoint(null,pointDTO.getHumidity(),pointDTO.getTimestamp());
        }

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

}
