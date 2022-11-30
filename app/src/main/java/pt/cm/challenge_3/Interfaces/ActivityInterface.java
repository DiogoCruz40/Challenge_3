package pt.cm.challenge_3.Interfaces;


import pt.cm.challenge_3.MainActivity;
import pt.cm.challenge_3.dtos.PointDTO;

public interface ActivityInterface {
    MainActivity getmainactivity();
    void insertPointAct(PointDTO pointDTO);
    void setLedAct(String state);
}
