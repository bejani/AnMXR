package ir.bejani;


import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;

public class showCatsReport extends ListActivity {
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;

	private static final String LOGTAG = "activity";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_cats);
		
		dbsource=new dbAdapter(this);
		dbsource.open();
		
		List<classCats> cats=new ArrayList<classCats>();
		cats=dbsource.findAllCats();
		
		ArrayAdapter<classCats> cats_adapter = new ArrayAdapter<classCats>(this,R.layout.row, cats);
		
		Long catID=(long) 0;
		List<classCredits> credits=null;

		ArrayAdapter<classCredits> adapter = null;
		ArrayAdapter<String> str_adapter = null;

		int p=0,i=0,j=0,sum=0;
		
		List<String> ac=new ArrayList<String>();
		for ( i = 0; i < cats_adapter.getCount(); i++) {
			//Log.i(LOGTAG,"$$$$$$$$$$$$==>"+i);
			catID=cats.get(i).getId();
			credits=dbsource.findFilteredCredits("creditcat ="+catID);
			
			adapter=new ArrayAdapter<classCredits>(this,R.layout.row, credits);
			p=0;
			for ( j = 0; j < adapter.getCount(); j++) {
				p=p+credits.get(j).getCredit();
			}
			sum=sum+p;		
			DecimalFormat dc=new DecimalFormat("###,###,###");

			ac.add(String.valueOf(i+1)+"- "+cats.get(i).getName()+" : " + dc.format(p));

		}
		Log.i(LOGTAG,String.valueOf(sum));
		List <classBill> bills=new ArrayList<classBill>();
		bills=dbsource.findFilteredBills("1=1");
		ArrayAdapter<classBill> bill_adapter=new ArrayAdapter<classBill>(this, R.layout.row,bills);
		
		Log.i(LOGTAG, "number of bills is : "+String.valueOf(bill_adapter.getCount()));
		p=0;
		for (int k = 0; k < bill_adapter.getCount(); k++) {
			p=p+(int)bills.get(k).getBillPayable();
		}
		sum=sum+p;
		ac.add(String.valueOf(i+1)+"- "+"تومان"+" : " + p);
		
		TextView total=(TextView) findViewById(R.id.catsTotal);	
		DecimalFormat dc=new DecimalFormat("###,###,###");

		total.setText("جمع کل "+dc.format(sum));
		str_adapter=new ArrayAdapter<String>(this,R.layout.listrow,R.id.rowlist, ac);
		setListAdapter(str_adapter);
		dbsource.close();
		
	}
	public void gotoShowAct(View v){
		Intent intent=new Intent(this, showActRecyc.class);
		startActivity(intent);
		this.finish();
	}
}
