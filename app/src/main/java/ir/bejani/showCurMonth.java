package ir.bejani;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghasemkiani.util.SimplePersianCalendar;
import com.ghasemkiani.util.icu.PersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import ir.bejani.recyc.CrdtListAdapter;
import ir.bejani.recyc.clsCreditView;

public class showCurMonth extends AppCompatActivity {

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    RecyclerView recyclerView;
    CrdtListAdapter mAdapter;

    dbAdapter dbsource;
    private List<clsCreditView> CrdtList = new ArrayList<>();

    ListView listview;
    private static final String LOGTAG = "activity";

    PersianCalendar nc = new PersianCalendar();
    Date dt = new Date();

    private List<classCredits> credits = null;
    private List<classCats> cats = null;

    ArrayAdapter<classCredits> adapter = null;
    List<String> tmpCredits = null;
    ArrayAdapter<String> customAdapter = null;

    public int getToday() {
        //calculate todays date
        SimplePersianCalendar prsCalendar = new SimplePersianCalendar();
        int year = prsCalendar.getDateFields().getYear();
        int month = prsCalendar.getDateFields().getMonth();
        month = month + 1;
        int day = prsCalendar.getDateFields().getDay();

        return year * 10000 + month * 100 + day;
    }

    public void gotoShowAct(View v) {
        Intent intent = new Intent(showCurMonth.this, showActRecyc.class);
        startActivity(intent);
        this.finish();
    }

    public void showCatsMonthReport(View v) {
        Intent intent = new Intent(showCurMonth.this, showCatsCurMonth.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_cur_month);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), showActRecyc.class));
                finish();
            }
        });

        Log.i("in the curMonth","Yes");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new CrdtListAdapter(getApplicationContext(), CrdtList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(mAdapter);

        prepareData("some");


    }

    private void prepareData(String selection) {
        dbsource = new dbAdapter(this);
        clsCreditView CREDIT;
        dbsource.open();
        SimplePersianCalendar prsCalendar = new SimplePersianCalendar();
        int year = prsCalendar.getDateFields().getYear();
        int month = prsCalendar.getDateFields().getMonth();
        month = month + 1;

        if (selection == "all") {
            credits = dbsource.findAllCredits();
        } else {
            credits = dbsource.findFilteredCredits("year=" + year + " and month=" + month);

            //credits=dbsource.findTodayCredits("insdate="+String.valueOf(getToday()));
        }
        if (credits.isEmpty() == true) {
            showToast(R.string.str_emptyTodayList);
        } else {

            int p = 0, i;
            for (i = 0; i < credits.size(); i++) {
                p = p + credits.get(i).getCredit();
                cats = dbsource.findFilteredCats("catID =" + credits.get(i).getCreditCat());
                String tmpCatName = "";
                if (cats.isEmpty() == false) {
                    tmpCatName = cats.get(0).getName();
                }
                DecimalFormat dc = new DecimalFormat("###,###,###");


                String tmpCrdt = dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())));
                String tmpCrdtName = String.valueOf(i + 1) + ". " +credits.get(i).getCreditName();
                String tmpCrdtDate = String.valueOf(credits.get(i).getInsDate());
                String tmpCrdtYear = String.valueOf(credits.get(i).getYear());
                String tmpCrdtMOnth = String.valueOf(credits.get(i).getMonth());
                String tmpCrdtDay = String.valueOf(credits.get(i).getDay());
                String tmpCrdtGrp = tmpCatName;
                String tmpCrdtID=String.valueOf(credits.get(i).getId());

                CREDIT = new clsCreditView(String.valueOf(tmpCrdtID),tmpCrdt, tmpCrdtName, tmpCrdtDate,tmpCrdtYear,tmpCrdtMOnth,tmpCrdtDay, tmpCrdtGrp,String.valueOf(credits.get(i).getFlag()));

                CrdtList.add(CREDIT);


            }
            TextView total = (TextView) findViewById(R.id.total);
            DecimalFormat dc = new DecimalFormat("###,###,###");
            total.setText("جمع کل " + dc.format(Integer.parseInt(String.valueOf(p))) + " تومان");

        }
        dbsource.close();
        mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        dbsource.open();

    }

    @Override
    protected void onPause() {
        super.onPause();
        dbsource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_full_cats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnReportMonth:
                Intent intent = new Intent(showCurMonth.this, showCatsCurMonth.class);
                startActivity(intent);
                this.finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}	


