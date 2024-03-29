package pt.cm.challenge_3;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import pt.cm.challenge_3.Interfaces.ActivityInterface;
import pt.cm.challenge_3.dtos.PointDTO;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private FragmentManager fm;
    private SharedViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.targetcontainer, new FragmentClass())
                .commit();

        model  = new ViewModelProvider(this).get(SharedViewModel.class);
        model.startDB();
//        model.deleteAllPoints();
        model.getToastMessageObserver().observe(this, message -> {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        });

        model.connmqtt(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //model.startDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.disconmqtt();
    }

    @Override
    public MainActivity getMainActivity() {
        return this;
    }


    @Override
    public void insertPointAct(PointDTO pointDTO) {
        FragmentClass fragment = (FragmentClass) fm.findFragmentById(R.id.targetcontainer);

        assert fragment != null;
        fragment.insertPointFiltered(pointDTO);
    }

    @Override
    public void setLedAct(String state) {
            FragmentClass fragment = (FragmentClass) fm.findFragmentById(R.id.targetcontainer);

            assert fragment != null;
            fragment.setInitLedState(state);

    }
}