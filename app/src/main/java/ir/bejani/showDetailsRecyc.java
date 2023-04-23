package ir.bejani;


import android.app.ProgressDialog;
import android.content.Context;
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

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import ir.bejani.recyc.CrdtListAdapter;
import ir.bejani.recyc.clsCreditView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class showDetailsRecyc extends AppCompatActivity {

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;
	List<String[]> tmpCreditss=null;
	private ProgressDialog progress;
	public int mProgressstatus = 0;

	private String[] mFileList,MFileList=null;
	List<String> brwList=new ArrayList<String>();
	List<String> FList=null,MFList=null;
	private String mChosenFile;
	 
	
	private File mPath = null;
	RecyclerView recyclerView;
	private List<clsCreditView> CrdtList = new ArrayList<>();

	ListView listview;
	private static final int DIALOG_Excel_Path = 10;

	private List<classCredits> credits=null;
	private List<classCats> cats=null;

	ArrayAdapter<classCredits> adapter=null;
	List<String> tmpCredits=null;
	ArrayAdapter<String> customAdapter=null;
	CrdtListAdapter mAdapter;

	private static final String LOGTAG = "activity";
	public String catID,catName;

	public void gotodefineAct(View v){

		Intent intent=new Intent(showDetailsRecyc.this, CreditGroups.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@SuppressWarnings("static-access")
//	public void refreshList(String catid){
//
//		dbsource=new dbAdapter(this);
//		dbsource.open();
//
//		List<String> tmpCredits=new ArrayList<String>();
//
//		tmpCreditss=new ArrayList<String[]>();
//
//
//		credits=dbsource.findFilteredCredits("creditcat="+catid);
//		if (credits.isEmpty()==true) {
//			showToast(R.string.str_emptyList);
//			//Toast.makeText(this, "لیست هزینه ها خالی است", Toast.LENGTH_LONG).show();
//			getListView().setVisibility(listview.INVISIBLE);
//		}
//		else
//		{
//			List<classBill> tmpBill,tbill=new ArrayList<classBill>();
//
//			getListView().setVisibility(listview.VISIBLE);
//
//			adapter = new ArrayAdapter<classCredits>(this,R.layout.row,R.id.secondLine, credits);
//			setListAdapter(adapter);
//			int p=0,i;
//			DecimalFormat dc=new DecimalFormat("###,###,###");
//
//			for ( i = 0; i < adapter.getCount(); i++) {
//				p=p+credits.get(i).getCredit();
//
//				String cList=String.valueOf(i+1)+"- مبلغ "+
//						dc.format(credits.get(i).getCredit())+
//						" بابت "+credits.get(i).getCreditName()+
//						" -- ["+String.valueOf(credits.get(i).getYear())+"/"+
//						String.valueOf(credits.get(i).getMonth())+"/"+
//						String.valueOf(credits.get(i).getDay())+"]";
//						//+"] - (  "+	tmpCatName+" )";
//
//				String clists[]={String.valueOf(i+1),String.valueOf(credits.get(i).getCredit()),
//						credits.get(i).getCreditName(),String.valueOf(credits.get(i).getInsDate())
//						//,"قبض",tbill.get(0).getBillID(),tbill.get(0).getPayID(),String.valueOf(tbill.get(0).getBillStartDate()),String.valueOf(tbill.get(0).getBillStopDate())
//						};
//				tmpCredits.add(cList);
//
//				tmpCreditss.add(clists);
//				Log.i(LOGTAG,">"+String.valueOf(p));
//
//			}
//			customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpCredits);
//			setListAdapter(customAdapter);
//			TextView total=(TextView) findViewById(R.id.dtotals);
//
//			total.setText("جمع کل "+dc.format(p)+" تومان");
//
//		}
//		dbsource.close();
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_recyc);

		//EditText ts=(EditText) findViewById(R.id.vs1);
		//ts.setTextSize("12dip");
		TextView gtxt=(TextView) findViewById(R.id.dvs2);
		
		String catid=getIntent().getExtras().getString("catid");
		String catname=getIntent().getExtras().getString("catname");
		gtxt.setText("( "+catname+" )");
		
		catID=catid;
		catName=catname;
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		CrdtList=new ArrayList<>();
		mAdapter = new CrdtListAdapter(getApplicationContext(),CrdtList);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		final LinearLayoutManager layoutManager = new  LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

		recyclerView.setAdapter(mAdapter);

		showDetails(catid,catname);


	}
	public void showDetails(String catid,String catname){

		dbsource=new dbAdapter(this);
		dbsource.open();
		clsCreditView CREDIT;

		List<classBill> tmpBill=new ArrayList<classBill>();
		tmpCredits=new ArrayList<String>();


		credits=dbsource.findFilteredCredits("creditcat="+catid);
		if (credits.isEmpty()==true) {
			showToast(R.string.str_emptyList);

		} else {

			int p=0,i;


			for ( i = 0; i < credits.size(); i++) {
				p=p+credits.get(i).getCredit();
				DecimalFormat dc=new DecimalFormat("###,###,###");

				cats= dbsource.findFilteredCats("catID ="+credits.get(i).getCreditCat());
				String tmpCatName="";
				if (cats.isEmpty()==false) {
					tmpCatName=cats.get(0).getName();
				}
				String cList="";

				tmpCredits.add(cList);
				Log.i(LOGTAG,">"+String.valueOf(p));

				long tmpCrdtID=credits.get(i).getId();
				String tmpCrdt=dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())));
				String tmpCrdtName=String.valueOf(i + 1) + ". " +credits.get(i).getCreditName();
				String tmpCrdtDate= String.valueOf(credits.get(i).getInsDate());
				String tmpCrdtYear = String.valueOf(credits.get(i).getYear());
				String tmpCrdtMOnth = String.valueOf(credits.get(i).getMonth());
				String tmpCrdtDay = String.valueOf(credits.get(i).getDay());
				String tmpCrdtGrp=tmpCatName;

				CREDIT = new clsCreditView(String.valueOf(tmpCrdtID),tmpCrdt, tmpCrdtName, tmpCrdtDate,tmpCrdtYear,tmpCrdtMOnth,tmpCrdtDay, tmpCrdtGrp,String.valueOf(credits.get(i).getFlag()));

				CrdtList.add(CREDIT);

			}
			TextView total=(TextView) findViewById(R.id.dtotals);
			DecimalFormat dc=new DecimalFormat("###,###,###");
			total.setText("جمع کل "+dc.format(Integer.parseInt(String.valueOf(p)))+" تومان");

		}
		dbsource.close();
	}

	public void showToast(int msg) {
		LayoutInflater inflator=getLayoutInflater();
		View layout=inflator.inflate(R.layout.toast_tmp_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
		TextView text = (TextView) layout.findViewById(R.id.vtoast);

		text.setText(msg);

		// Toast...
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	public void showToast(int msg,int toast_layout) {
		LayoutInflater inflator=getLayoutInflater();

		View layout=inflator.inflate(toast_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
		TextView text = (TextView) layout.findViewById(R.id.vtoast);

		text.setText(msg);

		// Toast...
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

}




