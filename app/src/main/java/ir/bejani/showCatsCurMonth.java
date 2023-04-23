package ir.bejani;


import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;

public class showCatsCurMonth extends ListActivity {
	private static final String LOGTAG = "activity";
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;

	//private static final String LOGTAG = "activity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_cats_cur_month);



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

		List<String> ac=new ArrayList<String>();
		
		
		//take shamsi date and insert it
		//shamsi date is split to year , month , and day
		SimplePersianCalendar prsCalendar=new SimplePersianCalendar();
		int year=prsCalendar.getDateFields().getYear();
		int month=prsCalendar.getDateFields().getMonth();
		month=month+1;
		
		for ( i = 0; i < adapter2.getCount(); i++) {
			catID=cats.get(i).getId();
			credits=dbsource.findFilteredCredits(" year="+year+" and month="+month+" and creditcat ="+catID);

					adapter=new ArrayAdapter<classCredits>(this,R.layout.row, credits);
					setListAdapter(adapter);
					ListView lsview=getListView();
					lsview.setVisibility(ListView.INVISIBLE);
					p=0;
					for ( j = 0; j < adapter.getCount(); j++) {
						p=p+credits.get(j).getCredit();
					}
					sum=sum+p;			
					DecimalFormat dc=new DecimalFormat("###,###,###");
					ac.add(String.valueOf(i+1)+"- "+cats.get(i).getName()+" : " + dc.format(p));

		}

		List <classBill> bills=new ArrayList<classBill>();
		
		String pdate=String.valueOf(year)+String.valueOf(month);
		if (month<10) {
			pdate=String.valueOf(year)+"0"+String.valueOf(month);
		}
		String tpdate=String.valueOf(Integer.parseInt(pdate)*100);
		
		String pidate=String.valueOf(year)+String.valueOf(month+1);
		if (month<10) {
			pidate=String.valueOf(year)+"0"+String.valueOf(month+1);
		}
		String tpidate=String.valueOf(Integer.parseInt(pidate)*100);
		
		Log.i(LOGTAG, ">>>()"+pidate+"."+tpidate+" / "+pdate+"."+tpdate);
		bills=dbsource.findFilteredBills("billpaydate>" +tpdate +" and billpaydate<"+tpidate);
		//bills=dbsource.findFilteredBills("1=1");
		ArrayAdapter<classBill> bill_adapter=new ArrayAdapter<classBill>(this, R.layout.row,bills);
		
		Log.i(LOGTAG, "number of bills is : "+String.valueOf(bill_adapter.getCount()));
		p=0;
		for (int k = 0; k < bill_adapter.getCount(); k++) {
			Log.d("bill", String.valueOf(bills.get(k).getPayDate()));
			p=p+(int)bills.get(k).getBillPayable();
		}
		sum=sum+p;
		DecimalFormat dc=new DecimalFormat("###,###,###");

		ac.add(String.valueOf(i+1)+"- "+"قبوض"+" : " +dc.format(p));

		
		TextView total=(TextView) findViewById(R.id.catsMonthTotal);	
		total.setText("جمع کل ماه جاری: "+dc.format(sum)+" تومان ");
		adapter5=new ArrayAdapter<String>(this,R.layout.listrow,R.id.rowlist, ac);
		setListAdapter(adapter5);
		ListView lsview=getListView();
		lsview.setVisibility(ListView.VISIBLE);
		dbsource.close();



	}
	public void gotoShowCurMonthReport(View v){
		Intent intent=new Intent(this, showCurMonth.class);
		startActivity(intent);
		this.finish();
	}
}
