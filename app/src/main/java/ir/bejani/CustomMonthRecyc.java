package ir.bejani;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import ir.bejani.recyc.CrdtListAdapter;
import ir.bejani.recyc.clsCreditView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomMonthRecyc extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    dbAdapter dbsource;
    CrdtListAdapter mAdapter;

    private List<clsCreditView> CrdtList = new ArrayList<>();

    ListView listview;

    private List<classCredits> credits = null;
    private List<classCats> cats = null;
    private int year = -1, month = -1;
    public String[] opts = {""};
    public TextView txtCommon;
    public boolean FM;
    ArrayAdapter<classCredits> adapter = null;
    List<String> tmpCredits = null;
    ArrayAdapter<String> customAdapter = null;

    private static final String LOGTAG = "activity";

    public void gotoshowActRecyc(View v) {
        Intent intent = new Intent(CustomMonthRecyc.this, showActRecyc.class);
        startActivity(intent);
        this.finish();
    }
    public void showCustomMonthCats(View v) {
        Log.i("year and month",String.valueOf(year));
        Log.i("year and month",String.valueOf(month));
        if((year>-1)&&(month>-1)) {
            Intent intent = new Intent(CustomMonthRecyc.this, CustomMonthCatsRecyc.class);
            intent.putExtra("year", String.valueOf(year));
            intent.putExtra("month", String.valueOf(month));
            startActivity(intent);
            this.finish();
        }
        else
            showToast(R.string.sel_year_month_err);
    }

    public void showCustomReport(View v) {

        TextView Year = (TextView) findViewById(R.id.gtxtYear);
        TextView Month = (TextView) findViewById(R.id.gtxtMonth);

        if (Year.getText().toString().equals("") || Month.getText().toString().equals("")) {
            showToast(R.string.str_emptyEntry);
        } else {
            year = Integer.parseInt(Year.getText().toString());
            month = Integer.parseInt(Month.getText().toString());


            Log.i("YM", String.valueOf(year) + ".." + String.valueOf(month));
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            mAdapter = new CrdtListAdapter(getApplicationContext(), CrdtList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setAdapter(mAdapter);
            prepData(year, month);

        }

    }

    public void prepData(int year, int month) {

        clsCreditView CREDIT;

        //make list empty
        CrdtList.clear();

        dbsource = new dbAdapter(this);
        dbsource.open();

        List<String> tmpCredits = new ArrayList<String>();

        credits = dbsource.findFilteredCredits("year=" + year + " and month=" + month);
        if (credits.isEmpty() == true) {
            showToast(R.string.str_emptyList);
            //getListView().setVisibility(listview.INVISIBLE);
            //Toast.makeText(this, "لیست هزینه های خالی است", Toast.LENGTH_LONG).show();
        } else {

            int p = 0, i;
            DecimalFormat dc = new DecimalFormat("###,###,###");
            for (i = 0; i < credits.size(); i++) {
                p = p + credits.get(i).getCredit();
                cats = dbsource.findFilteredCats("catID =" + credits.get(i).getCreditCat());
                String tmpCatName = "";
                if (!cats.isEmpty()) {
                    tmpCatName = cats.get(0).getName();
                }
                //else
                  //  tmpCatName=cats.get(i).getName();


                String tmpCrdt = dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())));
                String tmpCrdtName = String.valueOf(i + 1) + ". " + credits.get(i).getCreditName();
                String tmpCrdtDate = String.valueOf(credits.get(i).getInsDate());
                String tmpCrdtYear = String.valueOf(credits.get(i).getYear());
                String tmpCrdtMOnth = String.valueOf(credits.get(i).getMonth());
                String tmpCrdtDay = String.valueOf(credits.get(i).getDay());
                String tmpCrdtGrp = tmpCatName;
                String tmpCrdtID=String.valueOf(credits.get(i).getId());

                CREDIT = new clsCreditView(tmpCrdtID,tmpCrdt, tmpCrdtName, tmpCrdtDate,tmpCrdtYear,tmpCrdtMOnth,tmpCrdtDay, tmpCrdtGrp,String.valueOf(credits.get(i).getFlag()));

                CrdtList.add(CREDIT);

            }
            TextView total = (TextView) findViewById(R.id.gtotals);

            total.setText("جمع  " + dc.format(p) + " تومان");

        }
        dbsource.close();
    }

    public void showToast(int msg) {
        LayoutInflater inflator = getLayoutInflater();
        View layout = inflator.inflate(R.layout.toast_tmp_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
        TextView text = (TextView) layout.findViewById(R.id.vtoast);

        text.setText(msg);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void opendia(View v) {
        switch (v.getId()) {
            case R.id.lly:
                FM = false;
                opts = new String[16];
                opts[0] = "1409";
                opts[1] = "1408";
                opts[2] = "1407";
                opts[3] = "1406";
                opts[4] = "1405";
                opts[5] = "1404";
                opts[6] = "1404";
                opts[7] = "1403";
                opts[8] = "1402";
                opts[9] = "1401";
                opts[10] = "1400";
                opts[11] = "1399";
                opts[12] = "1398";
                opts[13] = "1397";
                opts[14] = "1396";
                opts[15] = "1395";
                txtCommon = (TextView) findViewById(R.id.gtxtYear);
                break;
            case R.id.llm:
                FM = true;
                opts = new String[12];
                opts[0] = "فروردین";
                opts[1] = "اردیبهشت";
                opts[2] = "خرداد";
                opts[3] = "تیر";
                opts[4] = "مرداد";
                opts[5] = "شهریور";
                opts[6] = "مهر";
                opts[7] = "آبان";
                opts[8] = "آذر";
                opts[9] = "دی";
                opts[10] = "بهمن";
                opts[11] = "اسفند";
                txtCommon = (TextView) findViewById(R.id.gtxtMonth);

        }

        AlertDialog.Builder alert = new AlertDialog.Builder(CustomMonthRecyc.this);
        alert.setTitle("انتخاب کنید");
        alert.setCancelable(false);
        alert.setItems(opts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (FM) {
                    int MONTH = 0;
                    switch (opts[which]) {
                        case "فروردین":
                            MONTH = 1;
                            break;
                        case "اردیبهشت":
                            MONTH = 2;
                            break;
                        case "خرداد":
                            MONTH = 3;
                            break;
                        case "تیر":
                            MONTH = 4;
                            break;
                        case "مرداد":
                            MONTH = 5;
                            break;
                        case "شهریور":
                            MONTH = 6;
                            break;
                        case "مهر":
                            MONTH = 7;
                            break;
                        case "آبان":
                            MONTH = 8;
                            break;
                        case "آذر":
                            MONTH = 9;
                            break;
                        case "دی":
                            MONTH = 10;
                            break;
                        case "بهمن":
                            MONTH = 11;
                            break;
                        case "اسفند":
                            MONTH = 12;
                    }
                    txtCommon.setText(String.valueOf(MONTH));
                } else
                    txtCommon.setText(opts[which]);

            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_month);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
//        setSupportActionBar(myToolbar);
//
//        //myToolbar.inflateMenu(R.menu.mnu_show_act);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), showActRecyc.class));
//                finish();
//            }
//        });
    }

}
