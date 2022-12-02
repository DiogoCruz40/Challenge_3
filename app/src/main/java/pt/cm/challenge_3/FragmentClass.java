package pt.cm.challenge_3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pt.cm.challenge_3.Interfaces.ActivityInterface;
import pt.cm.challenge_3.Interfaces.FragmentInterface;
import pt.cm.challenge_3.database.entities.Point;
import pt.cm.challenge_3.dtos.PointDTO;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


public class FragmentClass extends Fragment implements FragmentInterface {

    private SharedViewModel mViewModel;
    private ActivityInterface activityInterface;
    private View view;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    private ScatterChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

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

        builder = new NotificationCompat.Builder(activityInterface.getmainactivity(), "WARNING_ID")
                .setSmallIcon(R.drawable.ic_warning_notif)
                .setContentTitle("Warning")
                .setContentText("Default")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel();

        this.mViewModel = new ViewModelProvider(activityInterface.getmainactivity()).get(SharedViewModel.class);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activityInterface.getmainactivity(), android.R.layout.simple_spinner_item, Arrays.asList("All", "Temperature", "Humidity"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Uncomment when needed
        //chart();
    }

    public void chart() throws ParseException {

        MainActivity a = activityInterface.getmainactivity();

        chart = a.findViewById(R.id.chart1);

        // below line is use to disable the description of our scatter chart.
        chart.getDescription().setEnabled(false);

        // below line is use to draw grid background
        // and we are setting it to false.
        chart.setDrawGridBackground(false);

        // below line is use to set touch enable for our chart.
        chart.setTouchEnabled(true);

        // below line is use to set maximum highlight distance for our chart.
        chart.setMaxHighlightDistance(85f);

        // below line is use to set dragging for our chart.
        chart.setDragEnabled(true);

        // below line is use to set scale to our chart.
        chart.setScaleEnabled(true);

        // below line is use to set maximum visible count to our chart.
        chart.setMaxVisibleValueCount(200);

        // below line is use to set
        // pinch zoom to our chart.
        chart.setPinchZoom(true);

        // below line we are getting the legend of our chart.
        Legend l = chart.getLegend();

        // after getting our chart we are setting our chart for vertical and horizontal
        // alignment to top, right and vertical.
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        // below line is use for setting draw inside to false.
        l.setDrawInside(false);

        // below line is use to set offset value for our legend.
        l.setXOffset(5f);

        // below line is use to get y-axis of our chart.
        YAxis yl = chart.getAxisLeft();

        // below line is use to set minimum axis to our y axis.
        yl.setAxisMinimum(0f);

        // below line is use to get axis right of our chart
        chart.getAxisRight().setEnabled(false);

        // below line is use to get x axis of our chart.
        XAxis xl = chart.getXAxis();

        // below line is use to enable
        // drawing of grid lines.
        xl.setDrawGridLines(false);

        // in below line we are creating an array list for each entry of our chart.
        // we will be representing three values in our charts.
        // below is the line where we are creating three lines for our chart.
        ArrayList<Entry> temp = new ArrayList<>();
        ArrayList<Entry> hum = new ArrayList<>();
        List<PointDTO> dataList = mViewModel.getPoints();

        // on below line we are adding data to our charts.
        for (PointDTO p : dataList) {
            String str = p.getTimestamp();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date date = df.parse(str);
            long epoch = date.getTime();
            System.out.println(epoch); // 1055545912454
            temp.add(new Entry(p.getTemperature(), p.getTemperature()/*epoch*/));
            hum.add(new Entry(p.getHumidity(), p.getHumidity()/*epoch*/));
        }

        // create a data set and give it a type
        ScatterDataSet set1 = new ScatterDataSet(temp, "Temperature");

        // below line is use to set shape for our point on our graph.
        set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);

        // below line is for setting color to our shape.
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);

        // below line is use to create a new point for our scattered chart.
        ScatterDataSet set2 = new ScatterDataSet(hum, "Humidity");

        // for this point we are setting our shape to circle
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);

        // below line is for setting color to our point in chart.
        set2.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);

        // below line is use to set hole
        // radius to our point in chart.
        set2.setScatterShapeHoleRadius(3f);

        // below line is use to set color to our set.
        set2.setColor(ColorTemplate.COLORFUL_COLORS[1]);

        // below line is use to set shape size
        // for our data set of the chart.
        set1.setScatterShapeSize(8f);
        set2.setScatterShapeSize(8f);

        // in below line we are creating a new array list for our data set.
        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();

        // in below line we are adding all
        // data sets to above array list.
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);

        // create a data object with the data sets
        ScatterData data = new ScatterData(dataSets);

        // below line is use to set data to our chart
        chart.setData(data);

        // at last we are calling
        // invalidate method on our chart.
        chart.invalidate();
    }

    public void insertPointFiltered(PointDTO pointDTO) {
        Spinner spinner = view.findViewById(R.id.spinner);
        double max_temp,max_hum;
        String str = ((EditText) view.findViewById(R.id.input_temp)).getText().toString();
        System.out.println(str);
        if(!str.isEmpty()){
            max_temp = Double.parseDouble(str);
            System.out.println("defined temperature in app");
        } else {
            max_temp = 50.0;
            System.out.println("hello");
        }
        str = ((EditText) view.findViewById(R.id.input_hum)).getText().toString();
        if(!str.isEmpty()){
            max_hum = Double.parseDouble(str);
        } else {
            max_hum = 50.0;
            System.out.println("hello2");
        }

        if(pointDTO.getTemperature() > max_temp && pointDTO.getHumidity() > max_hum){
            builder.setContentText("Both Temperature and Humidity are above the respective thresold ("+ max_temp + "ºC, " + max_hum + "%)!");
            mNotificationManager.notify(123, builder.build());
        }
        else if(pointDTO.getHumidity() > max_hum){
            builder.setContentText("Humidity is above the " + max_hum + "% thresold!");
            mNotificationManager.notify(123, builder.build());
        }
        else if(pointDTO.getTemperature() > max_temp){
            builder.setContentText("Temperature is above the " + max_temp + "ºC thresold!");
            mNotificationManager.notify(123, builder.build());
        }

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WARNING";
            String description = "ups";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("WARNING_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager = (NotificationManager) activityInterface.getmainactivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

}
