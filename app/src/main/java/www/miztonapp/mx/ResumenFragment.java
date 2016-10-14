package www.miztonapp.mx;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
        entries.add(new PieEntry(1, 1));
        entries.add(new PieEntry(2, 2));
        entries.add(new PieEntry(3, 3));
        entries.add(new PieEntry(4, 4));

        PieChart chart = (PieChart) view.findViewById(R.id.chart);
        PieDataSet dataSet = new PieDataSet(entries, "fibra optica");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setValueTextColor(R.color.colorAccent);

        PieData lineData = new PieData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

    }
}
