package pt.cm.challenge_3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import pt.cm.challenge_3.Interfaces.ActivityInterface;
import pt.cm.challenge_3.Interfaces.FragmentInterface;
import pt.cm.challenge_3.dtos.PointDTO;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


public class FragmentClass extends Fragment implements FragmentInterface, OnChartGestureListener, OnChartValueSelectedListener {

    private SharedViewModel mViewModel;
    private ActivityInterface activityInterface;
    private View view;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    private LineChart chart1, chart2;
    private long firstDate;

    private List<PointDTO> chartPoints;

    private LineData tempData;
    private LineData humData;

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

        builder = new NotificationCompat.Builder(activityInterface.getMainActivity(), "WARNING_ID")
                .setSmallIcon(R.drawable.ic_warning_notif)
                .setContentTitle("Warning")
                .setContentText("Default")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel();

        this.mViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(SharedViewModel.class);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, Arrays.asList("All", "Temperature", "Humidity"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               TextView textView = ((TextView)adapterView.getChildAt(0));
               textView.setTextColor(Color.parseColor("#FFFFFF"));

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart("temp");
        chart("hum");

        mViewModel.getPoints().observe(getViewLifecycleOwner(), points -> {
            //TODO: Uncomment when needed inside observer
            try {
                if(!points.isEmpty()){

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    Date date = df.parse(points.get(0).getTimestamp());
                    firstDate = date.getTime()/1000; //drop milliseconds

                    chartPoints = points;

                    chartData();
                }

            } catch (ParseException e) {
                Log.w("chart",e.getMessage());
            }
        });
    }

    public void chart(String dataType){

        MainActivity a = activityInterface.getMainActivity();

        LineChart chartUI;
        if(Objects.equals(dataType, "temp")){
            chartUI = a.findViewById(R.id.chart1);
        }
        else if(Objects.equals(dataType, "hum")){
            chartUI = a.findViewById(R.id.chart2);
        }
        else{return;}

        chartUI.setOnChartValueSelectedListener(this);

        chartUI.setDrawGridBackground(false);
        chartUI.getDescription().setEnabled(false);
        chartUI.setDrawBorders(false);

        chartUI.getAxisLeft().setEnabled(false);
        chartUI.getAxisRight().setDrawAxisLine(false);
        chartUI.getAxisRight().setDrawGridLines(false);
        //chart.getXAxis().setAxisMinimum(firstDate);
        chartUI.getXAxis().setDrawAxisLine(false);
        chartUI.getXAxis().setDrawGridLines(false);
//        chartUI.getAxisRight().setTextColor(Color.rgb(255, 255, 255));
        chartUI.setTouchEnabled(true);
        chartUI.setTouchEnabled(true);
        chartUI.setDragEnabled(true);
        chartUI.setScaleEnabled(false); //VOLTAR
        //scaleX = chartUI.getScaleX();

        chartUI.setPinchZoom(false);

        Legend l = chartUI.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYOffset(10f);
        l.setTextSize(15);
        l.setTextColor(Color.rgb(0, 0, 0));

        chartUI.setVisibleXRangeMaximum(60);

        chartUI.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        chartUI.getXAxis().setCenterAxisLabels(true);
        chartUI.getXAxis().setGranularity(10f); // 10 seconds
        chartUI.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

            @Override
            public String getFormattedValue(float value) {

                long millis = TimeUnit.SECONDS.toMillis((long) value);
                long actualDate = (long) millis + firstDate*1000;
                return mFormat.format(new Date(actualDate));
            }
        });

        if(Objects.equals(dataType, "temp")){
            chart1  = chartUI;
            //chart1.getXAxis().setEnabled(false);
        }
        else if(Objects.equals(dataType, "hum")){
            chart2 = chartUI;
        }

    }

    public void chartData(){

        ArrayList<Entry> temp = new ArrayList<>();
        ArrayList<Entry> hum = new ArrayList<>();

        for (PointDTO p : chartPoints) {

            String str = p.getTimestamp();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

            try {

                Date date = df.parse(str);
                long epoch = date.getTime()/1000 - firstDate; //divide by 1000 - milliseconds
                //Dps posso converter para a data normal again

                if(p.getTemperature() != null) {
                    temp.add(new Entry(epoch, p.getTemperature()));
                }

                if(p.getHumidity() != null){
                    hum.add(new Entry(epoch, p.getHumidity()));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long diff = (long) hum.get(hum.size()-1).getX();

        LineDataSet tempLine = new LineDataSet(temp, "Temperature");

        tempLine.setLineWidth(2f);
        tempLine.setCircleRadius(3f);
        tempLine.setColor(colors[2]);
        tempLine.setCircleColor(colors[2]);
        tempLine.setDrawValues(false);

        tempData = new LineData(tempLine);

        float centerX, scaleX = 60;

        if (diff < 60){
            centerX = chart1.getCenter().getX();
        }
        else{
            scaleX = 60 + (float) (diff/(0.001*diff));
            centerX = chart1.getCenter().getX() + diff*scaleX;
            System.out.println("TESTE  " + diff + " " + chart1.getCenterOffsets().getX() + " " + centerX );
        }
        //chart1.getCenterOffsets().getX();
        //TODO: verificar se isto está bem
        //TODO: 2 fazer que os dois gráficos se movam ao mesmo tempo
        chart1.zoom(scaleX, 1, centerX, chart1.getCenter().getY());

        chart1.resetTracking();
        chart1.setData(tempData);
        chart1.invalidate();

        LineDataSet humLine = new LineDataSet(hum, "Humidity");

        humLine.setLineWidth(2f);
        humLine.setCircleRadius(3f);
        humLine.setColor(colors[3]);
        humLine.setCircleColor(colors[3]);
        humLine.setDrawValues(false);

        humData = new LineData(humLine);

        chart2.resetTracking();
        chart2.setData(humData);
        chart2.invalidate();
    }

    private final int[] colors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2],
            ColorTemplate.VORDIPLOM_COLORS[3],
    };

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap

        /*
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            chart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)*/

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart long pressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart fling. VelocityX: " + velocityX + ", VelocityY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {}

    public void insertPointFiltered(PointDTO pointDTO) {
        Spinner spinner = view.findViewById(R.id.spinner);
        double max_temp, max_hum;
        String str = ((EditText) view.findViewById(R.id.input_hum)).getText().toString();
        System.out.println(str);
        if (!str.isEmpty()) {
            max_temp = Double.parseDouble(str);
            System.out.println("defined temperature in app");
        } else {
            max_temp = 50.0;
        }
        str = ((EditText) view.findViewById(R.id.input_temp)).getText().toString();
        if (!str.isEmpty()) {
            max_hum = Double.parseDouble(str);
        } else {
            max_hum = 50.0;
        }

        if (pointDTO.getTemperature() > max_temp && pointDTO.getHumidity() > max_hum) {
            builder.setContentText("Both Temperature and Humidity are above the respective thresold (" + max_temp + "ºC, " + max_hum + "%)!");
            mNotificationManager.notify(123, builder.build());
        } else if (pointDTO.getHumidity() > max_hum) {
            builder.setContentText("Humidity is above the " + max_hum + "% thresold!");
            mNotificationManager.notify(123, builder.build());
        } else if (pointDTO.getTemperature() > max_temp) {
            builder.setContentText("Temperature is above the " + max_temp + "ºC thresold!");
            mNotificationManager.notify(123, builder.build());
        }

        if (spinner.getSelectedItem().toString().equals("All")) {
            mViewModel.insertPoint(pointDTO.getTemperature(), pointDTO.getHumidity(), pointDTO.getTimestamp());
        } else if (spinner.getSelectedItem().toString().equals("Temperature")) {
            mViewModel.insertPoint(pointDTO.getTemperature(), null, pointDTO.getTimestamp());
        } else if (spinner.getSelectedItem().toString().equals("Humidity")) {
            mViewModel.insertPoint(null, pointDTO.getHumidity(), pointDTO.getTimestamp());
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

        CharSequence name = "WARNING";
        String description = "ups";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("WARNING_ID", name, importance);
        channel.setDescription(description);

        mNotificationManager = (NotificationManager) activityInterface.getMainActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
    }

}
