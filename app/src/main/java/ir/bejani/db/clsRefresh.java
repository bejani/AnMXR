package ir.bejani.db;


import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.R;

public class clsRefresh extends Activity {
	dbAdapter dbsource;
	ArrayList<String[]> tmpCreditss;
	private List<classCredits> credits=null;
	private List<classCats> cats=null;
	clsShowToast shwToast;
	ArrayAdapter<classCredits> adapter=null;
	ArrayAdapter<String> customAdapter=null;


	
	public void refreshList(String selection){

		dbsource=new dbAdapter(this);
		dbsource.open();

		List<String> tmpCredits=new ArrayList<String>();
		tmpCreditss=new ArrayList<String[]>();
		
		//calculate todays date
		SimplePersianCalendar prsCalendar=new SimplePersianCalendar();
		int year=prsCalendar.getDateFields().getYear();
		int month=prsCalendar.getDateFields().getMonth();
		month=month+1;
		int day=prsCalendar.getDateFields().getDay();

		//Plain date calculation and insertion
		int plaindate=year*10000+month*100+day;
		if (selection=="all") {			
			credits=dbsource.findAllCredits();
		} else {
			//credits=dbsource.findTodayCredits("insdate="+String.valueOf(plaindate));
			credits=dbsource.findAllCredits();
		}
		if (credits.isEmpty()==true) {
			
			shwToast.showToast(R.string.str_emptyList);
			//Toast.makeText(this, "لیست هزینه ها خالی است", Toast.LENGTH_LONG).show();
		} else {


			adapter = new ArrayAdapter<classCredits>(this,R.layout.row,R.id.secondLine, credits);
			//setListAdapter(adapter);
			int p=0,i;
			List<classBill> tmpBill,tbill=new ArrayList<classBill>();
			classBill tBill=new classBill();

			for ( i = 0; i < adapter.getCount(); i++) {
				p=p+credits.get(i).getCredit();
				cats= dbsource.findFilteredCats("catID ="+credits.get(i).getCreditCat());
				String tmpCatName="";
				if (cats.isEmpty()==false) {
					tmpCatName=cats.get(0).getName();
				}
				String cList="";
				DecimalFormat dc=new DecimalFormat("###,###,###");
				if (credits.get(i).getFlag()==0) {
					cList=String.valueOf(i+1)+"- مبلغ "+ 

							dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())))+
							" بابت "+credits.get(i).getCreditName()+
							" -- ["+String.valueOf(credits.get(i).getYear())+"/"+
							String.valueOf(credits.get(i).getMonth())+"/"+
							String.valueOf(credits.get(i).getDay())+"] - (  "+
							tmpCatName+" )";
					String clists[]={String.valueOf(i+1),String.valueOf(credits.get(i).getCredit()),
							credits.get(i).getCreditName(),String.valueOf(credits.get(i).getInsDate()),
							tmpCatName};
					tmpCreditss.add(clists);


				} else {
					tmpBill=dbsource.findFilteredBills("btid="+credits.get(i).getFlag());
					cList=String.valueOf(i+1)+"- مبلغ "+

							dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())))+
							" بابت "+credits.get(i).getCreditName()+
							" -- ["+String.valueOf(credits.get(i).getYear())+"/"+
							String.valueOf(credits.get(i).getMonth())+"/"+
							String.valueOf(credits.get(i).getDay())+"] - (  "+
							"ش.ق"+tmpBill.get(0).getBillID()+".."+"ش.پ"+tmpBill.get(0).getPayID()

							+" )";

					tbill=dbsource.findFilteredBills("btid="+credits.get(i).getFlag());
					String clists[]={String.valueOf(i+1),String.valueOf(credits.get(i).getCredit()),
							credits.get(i).getCreditName(),String.valueOf(credits.get(i).getInsDate())
							,"قبض",tbill.get(0).getBillID(),tbill.get(0).getPayID(),String.valueOf(tbill.get(0).getBillStartDate()),String.valueOf(tbill.get(0).getBillStopDate())};
					tmpCreditss.add(clists);

				}


				tmpCredits.add(cList);
				

			}
			customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpCredits);
			//setListAdapter(customAdapter);
			TextView total=(TextView) findViewById(R.id.totals);
			DecimalFormat dc=new DecimalFormat("###,###,###");
			total.setText("جمع کل "+dc.format(Integer.parseInt(String.valueOf(p)))+" تومان");

		}
		dbsource.close();
	}
}
