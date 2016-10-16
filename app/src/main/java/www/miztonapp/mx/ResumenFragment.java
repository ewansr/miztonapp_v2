package www.miztonapp.mx;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ResumenFragment extends Fragment {
    private static Context context;
    public void initTabFragmentGeneral(Context context){
        this.context = context;
    }

    public ResumenFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate( R.layout.fragment_resumen, container, false );
//        PieChart chart = (PieChart) rootView.findViewById(R.id.chart);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(10, "Liquidadas"));
        entries.add(new PieEntry(20, "Objetadas"));
        entries.add(new PieEntry(30, "Retornadas"));
        entries.add(new PieEntry(40, "Quejas"));

        PieChart chart = (PieChart) view.findViewById(R.id.chart);
        PieDataSet dataSet = new PieDataSet(entries, "fibra optica");
        dataSet.setColors(new int[] { R.color.red, R.color.green, R.color.blue, R.color.orange }, context);
        dataSet.setValueTextColor(R.color.colorAccent);

        PieData lineData = new PieData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        // set custom labels and colors
        l.setCustom(new int[] { R.color.red, R.color.green, R.color.blue, R.color.orange }, new String[] { "Liquidadas", "Objetadas", "Retornadas", "Quejas" });


    }
}
