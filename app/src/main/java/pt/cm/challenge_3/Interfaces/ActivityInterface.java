package pt.cm.challenge_3.Interfaces;


import pt.cm.challenge_3.MainActivity;
import pt.cm.challenge_3.dtos.PointDTO;

public interface ActivityInterface {
    MainActivity getMainActivity();
    void insertPointAct(PointDTO pointDTO);
    void setLedAct(String state);
}
