package ir.bejani;

import android.app.ListActivity;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;

public class showSomeDateCatsReport extends ListActivity{
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;

	private static final String LOGTAG = "activity";
	private String fromdate="",todate="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sel_period_cats);

		String startdate=getIntent().getExtras().getString("startdate");
		String enddate=getIntent().getExtras().getString("enddate");
		fromdate=startdate;
		todate=enddate;
		Log.i(LOGTAG, "FROM DATE==>"+fromdate+".."+todate);
		
		if (startdate.equals("") || enddate.equals("")) {
			showToast(R.string.str_emptyDate);
		} else {

		
		dbsource=new dbAdapter(this);
		dbsource.open();

		List<classCats> cats=new ArrayList<classCats>();
		cats=dbsource.findAllCats();

		ArrayAdapter<classCats> adapter2 = new ArrayAdapter<classCats>(this,android.R.layout.simple_expandable_list_item_1, cats);
		setListAdapter(adapter2);

		Long catID=(long) 0;
		List<classCredits> credits=null;

		ArrayAdapter<classCredits> adapter = null;
		ArrayAdapter<String> adapter5 = null;


		int p=0,i=0,j=0,sum=0;
		DecimalFormat dc=new DecimalFormat("###,###,###");

		List<String> ac=new ArrayList<String>();
		
		for ( i = 0; i < adapter2.getCount(); i++) {
			catID=cats.get(i).getId();
			credits=dbsource.findFilteredCredits(" insdate>="+startdate+" and insdate<="+enddate+" and creditcat ="+catID);

					adapter=new ArrayAdapter<classCredits>(this,R.layout.row, credits);
					setListAdapter(adapter);
					ListView lsview=getListView();
					lsview.setVisibility(ListView.INVISIBLE);
					p=0;
					for ( j = 0; j < adapter.getCount(); j++) {
						p=p+credits.get(j).getCredit();
					}
					sum=sum+p;			

					ac.add(String.valueOf(i+1)+"- "+cats.get(i).getName()+" : " + dc.format(p));

		}
		List <classBill> bills=new ArrayList<classBill>();
		bills=dbsource.findFilteredBills("1=1");
		ArrayAdapter<classBill> bill_adapter=new ArrayAdapter<classBill>(this, R.layout.row,bills);
		
		Log.i(LOGTAG, "number of bills is : "+String.valueOf(bill_adapter.getCount()));
		p=0;
		for (int k = 0; k < bill_adapter.getCount(); k++) {
			p=p+(int)bills.get(k).getBillPayable();
		}
		sum=sum+p;
		ac.add(String.valueOf(i+1)+"- "+"قبوض"+" : " + dc.format(p));

		TextView total=(TextView) findViewById(R.id.total_somedatecats);	
		total.setText("جمع کل دوره انتخابی: "+dc.format(sum)+" تومان ");
		
		//String syear=startdate.substring(0, 2);
		
		TextView periDate=(TextView) findViewById(R.id.tvPeriDate);	
		
		periDate.setText("( "+startdate+" - "+enddate+" )");
		
		
		adapter5=new ArrayAdapter<String>(this,R.layout.listrow,R.id.rowlist, ac);
		setListAdapter(adapter5);
		ListView lsview=getListView();
		lsview.setVisibility(ListView.VISIBLE);
		dbsource.close();

		}
		
	}
	public void showToast(int msg) {
		LayoutInflater inflator=getLayoutInflater();
		View layout=inflator.inflate(R.layout.toast_tmp_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
		TextView text = (TextView) layout.findViewById(R.id.vtoast);
		
		text.setText(msg);
		
    // Toast...
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	public void gotoSomeDateAct(View v){
		Intent intent=new Intent(this, showSomeDateReport.class);
		intent.putExtra("fromdate", fromdate);
		intent.putExtra("todate", todate);
		startActivity(intent);
		this.finish();
	}
}
