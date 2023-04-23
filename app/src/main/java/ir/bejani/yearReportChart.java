package ir.bejani;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class yearReportChart extends Activity {

    dbAdapter dbsource;
    private List<classCredits> credits = null;
    ArrayAdapter<classCredits> adapter = null;
    ArrayAdapter<String> customAdapter = null;
    LineGraphSeries<DataPoint> sr;
    int total = 0, month, cmonth1 = 0, cmonth2 = 0, cmonth3 = 0, cmonth4 = 0, cmonth5 = 0, cmonth6 = 0, cmonth7 = 0, cmonth8 = 0, cmonth9 = 0, cmonth10 = 0, cmonth11 = 0, cmonth12 = 0;
    int selYear;

    public void gotoShowAct(View v) {

        Intent intent = new Intent(yearReportChart.this, showActRecyc.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    public int[] getCreditsByMonth(int selYear) {

        //init Month
        int[] Month = new int[12];
        for(int i=0;i<12;i++)
            Month[i]=0;

        //init cmonthes to zero
        cmonth1=0;cmonth2=0;cmonth3=0;cmonth4=0;cmonth5=0;cmonth6=0;cmonth7=0;cmonth8=0;cmonth9=0;cmonth10=0;cmonth11=0;cmonth12=0;

        dbsource = new dbAdapter(yearReportChart.this);
        dbsource.open();
        credits = dbsource.findAllCredits();

        EditText editTxtYear = (EditText) findViewById(R.id.cyEditTextyear);
        String year = editTxtYear.getText().toString();

        int cyear = 0;
        for (int i = 0; i < credits.size(); i++) {

            cyear = credits.get(i).getYear();
            if (cyear == selYear) {

                month = credits.get(i).getMonth();
                switch (month) {
                    case 1:
                        cmonth1 += credits.get(i).getCredit();
                        break;
                    case 2:
                        cmonth2 += credits.get(i).getCredit();
                        break;
                    case 3:
                        cmonth3 += credits.get(i).getCredit();
                        break;
                    case 4:
                        cmonth4 += credits.get(i).getCredit();
                        break;
                    case 5:
                        cmonth5 += credits.get(i).getCredit();
                        break;
                    case 6:
                        cmonth6 += credits.get(i).getCredit();
                        break;
                    case 7:
                        cmonth7 += credits.get(i).getCredit();
                        break;
                    case 8:
                        cmonth8 += credits.get(i).getCredit();
                        break;
                    case 9:
                        cmonth9 += credits.get(i).getCredit();
                        break;
                    case 10:
                        cmonth10 += credits.get(i).getCredit();
                        break;
                    case 11:
                        cmonth11 += credits.get(i).getCredit();
                        break;
                    case 12:
                        cmonth12 += credits.get(i).getCredit();
                        break;
                }
                total += credits.get(i).getCredit();

                Month[0] = cmonth1;
                Month[1] = cmonth2;
                Month[2] = cmonth3;
                Month[3] = cmonth4;
                Month[4] = cmonth5;
                Month[5] = cmonth6;
                Month[6] = cmonth7;
                Month[7] = cmonth8;
                Month[8] = cmonth9;
                Month[9] = cmonth10;
                Month[10] = cmonth11;
                Month[11] = cmonth12;
            }
        }
        dbsource.close();
        return Month;
    }

    public void showYearReportBar(View v) {

        //init Month
        int[] Month = new int[12];
        for(int i=0;i<12;i++)
            Month[i]=0;

        //init total
        total=0;

        EditText editTxtYear = (EditText) findViewById(R.id.cyEditTextyear);
        String year = editTxtYear.getText().toString();
        if (!year.isEmpty()) {

            int selyear =Integer.parseInt(year);

            Month = getCreditsByMonth(selyear);

            Log.i("array Month=",String.valueOf(Month[0]));
            TextView txtTotal = (TextView) findViewById(R.id.cytotals);
            String tp = String.format("%,d", total);
            txtTotal.setText(tp);

            GraphView graph = (GraphView) findViewById(R.id.graph);
            graph.removeAllSeries();

            // Each series represents one bar.
            BarGraphSeries<DataPoint> series1 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(1, Month[0])});
            BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(2, Month[1])});
            BarGraphSeries<DataPoint> series3 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(3, Month[2])});
            BarGraphSeries<DataPoint> series4 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(4, Month[3])});
            BarGraphSeries<DataPoint> series5 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(5, Month[4])});
            BarGraphSeries<DataPoint> series6 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(6, Month[5])});
            BarGraphSeries<DataPoint> series7 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(7, Month[6])});
            BarGraphSeries<DataPoint> series8 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(8, Month[7])});
            BarGraphSeries<DataPoint> series9 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(9, Month[8])});
            BarGraphSeries<DataPoint> series10 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(10, Month[9])});
            BarGraphSeries<DataPoint> series11 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(11, Month[10])});
            BarGraphSeries<DataPoint> series12 = new BarGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(12, Month[11])});

// Add titles to be displayed in the legend.
            series1.setTitle("فروردین");
            series2.setTitle("اردیبهشت");
            series3.setTitle("خرداد");
            series4.setTitle("تیر");
            series5.setTitle("مرداد");
            series6.setTitle("شهریور");
            series7.setTitle("مهر");
            series8.setTitle("آبان");
            series9.setTitle("آذر");
            series10.setTitle("دی");
            series11.setTitle("بهمن");
            series12.setTitle("اسفند");

// Add color to your bars.
            series1.setColor(getResources().getColor(R.color.farvardin));
            series2.setColor(getResources().getColor(R.color.ordibehesht));
            series3.setColor(getResources().getColor(R.color.xordad));
            series4.setColor(getResources().getColor(R.color.tir));
            series5.setColor(getResources().getColor(R.color.mordad));
            series6.setColor(getResources().getColor(R.color.shahrivar));
            series7.setColor(getResources().getColor(R.color.mehr));
            series8.setColor(getResources().getColor(R.color.aban));
            series9.setColor(getResources().getColor(R.color.azar));
            series10.setColor(getResources().getColor(R.color.dey));
            series11.setColor(getResources().getColor(R.color.bahman));
            series12.setColor(getResources().getColor(R.color.isfand));

// Add each series to your graph.
            graph.addSeries(series1);
            graph.addSeries(series2);
            graph.addSeries(series3);
            graph.addSeries(series4);
            graph.addSeries(series5);
            graph.addSeries(series6);
            graph.addSeries(series7);
            graph.addSeries(series8);
            graph.addSeries(series9);
            graph.addSeries(series10);
            graph.addSeries(series11);
            graph.addSeries(series12);

            float colwidth = graph.getGraphContentWidth() / (12 - 1);

            graph.setTitle("مخارج ماههای سال");
            graph.setBackgroundColor(getResources().getColor(R.color.white_color));
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"فر", "2", "3", "تیر", "5", "6", "مهر", "8", "9", "دی", "11", "12"});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
//            graph.getLegendRenderer().setVisible(true);
//            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        }
    }

    public void showYearReportLine(View v) {

        LineGraphSeries sr;

        //init Month
        int[] Month = new int[12];
        for(int i=0;i<12;i++)
            Month[i]=0;

        //init total
        total=0;

        EditText editTxtYear = (EditText) findViewById(R.id.cyEditTextyear);

        String year = editTxtYear.getText().toString();
        if (!year.isEmpty()) {
            int selyear = Integer.parseInt(year);

            Month = getCreditsByMonth(selyear);

            sr = new LineGraphSeries<>();
            sr.appendData(new DataPoint(1, Month[0]), true, 12);

            sr.appendData(new DataPoint(2, Month[1]), true, 12);

            sr.appendData(new DataPoint(3, Month[2]), true, 12);

            sr.appendData(new DataPoint(4, Month[3]), true, 12);

            sr.appendData(new DataPoint(5, Month[4]), true, 12);

            sr.appendData(new DataPoint(6, Month[5]), true, 12);

            sr.appendData(new DataPoint(7, Month[6]), true, 12);

            sr.appendData(new DataPoint(8, Month[7]), true, 12);

            sr.appendData(new DataPoint(9, Month[8]), true, 12);

            sr.appendData(new DataPoint(10, Month[9]), true, 12);

            sr.appendData(new DataPoint(11, Month[10]), true, 12);

            sr.appendData(new DataPoint(12, Month[11]), true, 12);

            TextView txtTotal = (TextView) findViewById(R.id.cytotals);
            String tp = String.format("%,d", total);
            txtTotal.setText(tp);

            GraphView graph = (GraphView) findViewById(R.id.graph);
            graph.removeAllSeries();

            //sr.setBackgroundColor(getResources().getColor(R.color.bgDarkg));
            //sr.setDrawBackground(true);

            graph.addSeries(sr);
            //graph.setBackgroundColor(getResources().getColor(R.color.bglight));
            //graph.setScaleX(1);

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"فر", "2", "3", "تیر", "5", "6", "مهر", "8", "9", "دی", "11", "12"});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setScrollable(true);
           // graph.setDrawingCacheBackgroundColor(getResources().getColor(R.color.bgDarkg));
            //graph.getLegendRenderer().setVisible(true);

            graph.setTitle("مخارج ماههای سال");

           // graph.getLegendRenderer().setBackgroundColor(Color.rgb(220, 12, 167));
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.custom_year_chart);

        super.onCreate(savedInstanceState);
    }

}
