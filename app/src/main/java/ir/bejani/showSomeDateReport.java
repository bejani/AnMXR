package ir.bejani;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;

public class showSomeDateReport extends ListActivity {

	private static final int DIALOG_startDate = 10;
	private static final int DIALOG_stopDate = 20;


	public static final String G_DAY = "gDay";
	public static final String G_MONTH = "gMonth";
	public static final String G_YEAR = "gYear";
	public static final String J_DAY = "jDay";
	public static final String J_MONTH = "jMonth";
	public static final String J_YEAR = "jYear";
	private String[] monthNames = { "فروردین", "اردیبهشت", "خرداد", "تیر",
			"مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند" };
	private NumberPicker npDay;
	private NumberPicker npMonth;
	private NumberPicker npYear;

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;

	ListView listview;

	private List<classCredits> credits=null;
	private List<classCats> cats=null;
	private String startDate="",stopDate="";

	ArrayAdapter<classCredits> adapter=null;
	List<String> tmpCredits=null;
	List<String> altCredits=null;
	ArrayAdapter<String> customAdapter=null;

	private static final String LOGTAG = "activity";
	@SuppressWarnings("unused")
	private boolean validInsDate=false;
	@SuppressWarnings("unused")
	private String startdate="",stopdate="",startdateDisplay="",stopdateDisplay="";
	private String fromDate="",toDate="";
	LayoutInflater inflator=null;
	View layout=null;

	public void inflateDialog(){

		NumberPicker.OnValueChangeListener onChangeListener = new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (picker == npMonth) {
					if (newVal <= 6) {
						npDay.setMaxValue(31);
					} else {
						npDay.setMaxValue(30);
					}
				}


			}
		};
		npYear = (NumberPicker) layout.findViewById(R.id.npYear);
		npMonth = (NumberPicker) layout.findViewById(R.id.npMonth);
		npDay = (NumberPicker) layout.findViewById(R.id.npDay);

		npMonth.setOnValueChangedListener(onChangeListener);
		SimplePersianCalendar jdf = new SimplePersianCalendar();
		int iranianYear = jdf.getDateFields().getYear();
		int iranianMonth = jdf.getDateFields().getMonth()+1;
		int iranianDay = jdf.getDateFields().getDay();

		npYear.setMinValue(1390);
		npYear.setMaxValue(1440);
		npYear.setWrapSelectorWheel(true);
		npMonth.setMinValue(1);
		npMonth.setMaxValue(12);
		npMonth.setDisplayedValues(monthNames);

		npDay.setMinValue(1);
		npDay.setMaxValue(31);

		npYear.setValue(iranianYear);
		npMonth.setValue(iranianMonth);
		npDay.setValue(iranianDay);

	}
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub

		switch (id) {
		case DIALOG_stopDate:
			AlertDialog.Builder alb0=new AlertDialog.Builder(this);
			inflator=getLayoutInflater();
			layout=inflator.inflate(R.layout.pic_date_layout, (ViewGroup) findViewById(R.id.rootPicDateLayout));
			Log.i(LOGTAG, "dialog");
			alb0.setTitle("تاریخ پایان را انتخاب کنید");
			alb0.setView(layout);
			alb0.setPositiveButton("تایید", new  DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub


					int newIrYear = npYear.getValue();
					stopdate=String.valueOf(newIrYear);
					stopdateDisplay=stopdate;
					int newIrMonth = npMonth.getValue();

					if (newIrMonth<10){
						stopdate=stopdate+"0"+String.valueOf(newIrMonth);
						stopdateDisplay=stopdateDisplay+"/0"+String.valueOf(newIrMonth);
					}
					else
					{
						stopdate=stopdate+String.valueOf(newIrMonth);
						stopdateDisplay=stopdateDisplay+"/"+String.valueOf(newIrMonth);						
					}

					int newIrDay = npDay.getValue();
					if (newIrDay<10){
						stopdate=stopdate+"0"+String.valueOf(newIrDay);
						stopdateDisplay=stopdateDisplay+"/0"+String.valueOf(newIrDay);
					}
					else
					{
						stopdate=stopdate+String.valueOf(newIrDay);
						stopdateDisplay=stopdateDisplay+"/"+String.valueOf(newIrDay);
					}
					//Log.i(LOGTAG, "^^^^ "+String.valueOf(stopdate));
					//Log.i(LOGTAG, "^^^^ "+String.valueOf(stopdateDisplay));

					EditText txtStartDate=(EditText) findViewById(R.id.stopDate);
					txtStartDate.setText(stopdateDisplay);
					Log.i(LOGTAG, "click dialog end....");
				}

			});
			alb0.show();
			inflateDialog();
			break;
		case DIALOG_startDate:
			AlertDialog.Builder alb=new AlertDialog.Builder(this);
			inflator=getLayoutInflater();
			layout=inflator.inflate(R.layout.pic_date_layout, (ViewGroup) findViewById(R.id.rootPicDateLayout));
			//View layout=inflator.inflate(R.layout.file_browse_layout, (ViewGroup) findViewById(R.id.fileBrowseRootElement));

			Log.i(LOGTAG, "dialog");
			alb.setTitle("تاریخ شروع را انتخاب کنید");
			alb.setView(layout);
			alb.setPositiveButton("تایید", new  DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					int newIrYear = npYear.getValue();
					startdate=String.valueOf(newIrYear);
					startdateDisplay=startdate;
					int newIrMonth = npMonth.getValue();

					if (newIrMonth<10){
						startdate=startdate+"0"+String.valueOf(newIrMonth);
						startdateDisplay=startdateDisplay+"/0"+String.valueOf(newIrMonth);
					}
					else
					{
						startdate=startdate+String.valueOf(newIrMonth);
						startdateDisplay=startdateDisplay+"/"+String.valueOf(newIrMonth);						
					}

					int newIrDay = npDay.getValue();
					if (newIrDay<10){
						startdate=startdate+"0"+String.valueOf(newIrDay);
						startdateDisplay=startdateDisplay+"/0"+String.valueOf(newIrDay);
					}
					else
					{
						startdate=startdate+String.valueOf(newIrDay);
						startdateDisplay=startdateDisplay+"/"+String.valueOf(newIrDay);
					}
					Log.i(LOGTAG, "^^^^ "+String.valueOf(startdate));
					Log.i(LOGTAG, "^^^^ "+String.valueOf(startdateDisplay));

					EditText txtStartDate=(EditText) findViewById(R.id.startDate);
					txtStartDate.setText(startdateDisplay);
					Log.i(LOGTAG, "click dialog end....");
				}

			});

			alb.show();
			inflateDialog();

			break;
		default:
			break;
		}
		return super.onCreateDialog(id, args);
	}

	public void gotoMainAct(View v){
		Intent intent=new Intent(showSomeDateReport.this, showActRecyc.class);
		startActivity(intent);
		this.finish();
	}

	public void showPeriReport(View v){

		customAdapter=null;
		EditText txtStartDate=(EditText) findViewById(R.id.startDate);
		EditText txtStopDate=(EditText) findViewById(R.id.stopDate);

		String _startdate=txtStartDate.getText().toString();
		String _stopdate=txtStopDate.getText().toString();

		//check startdate and endDate is true and not empty nor less than 8 numbers like : 13930523
		if(_startdate.equals("") | _stopdate.equals("")){
			showToast(R.string.str_emptyDate);
		}
		else
		{
			
				Log.i(LOGTAG, "start date="+startDate+"  stopdate="+stopdate+" _startdate="+_startdate+" _stopdate="+_stopdate);
				refreshList(startdate,stopdate);

				ListView lsview=getListView();

				lsview.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						final int itemPos=arg2;

						final long id=credits.get(itemPos).getId();
						final long flag=credits.get(itemPos).getFlag();

						AlertDialog alertDialog = new AlertDialog.Builder(showSomeDateReport.this).create();

						// Setting Dialog Title
						alertDialog.setTitle("عملیات بر روی هزینه های ثبت شده");

						// Setting Dialog Message
						alertDialog.setMessage("یکی از عملیات را انتخاب کنید ");			

						// Setting Icon to Dialog
						alertDialog.setIcon(R.drawable.ic_actiondialog);

						alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface arg0) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "You canceled...", Toast.LENGTH_SHORT).show();

							}
						});

						// Setting edit Button
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"ویرایش", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								if (flag==0) {
									Intent intent=new Intent(showSomeDateReport.this, editCredit.class);
									intent.putExtra("cid", String.valueOf(id));
									startActivity(intent);
									
								} else {
									Intent intent=new Intent(showSomeDateReport.this, editBill.class);
									intent.putExtra("btid", String.valueOf(flag));
									intent.putExtra("creditID", String.valueOf(id));
									startActivity(intent);

								}
							}
						});
						// Setting delete Button
						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"حذف", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) { 
								// Write your code here to execute after dialog closed
								//dbsource.open();

								dbsource.delCredit(id);
								Toast.makeText(getApplicationContext(), "هزینه مورد نظر از لیست حذف شد"+String.valueOf(id), Toast.LENGTH_LONG).show();
								refreshList(startDate,stopDate);

							}
						});
						// Setting cancel Button
						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"انصراف", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed
								Toast.makeText(getApplicationContext(), "You clicked on cancel", Toast.LENGTH_SHORT).show();
							}
						});

						// Showing Alert Message

						alertDialog.show();
						return false;
					}
				});
			}
		}

	
	@SuppressWarnings("static-access")
	public void refreshList(String startDate,String stopDate){

		dbsource=new dbAdapter(this);
		dbsource.open();


		List<String> tmpCredits=new ArrayList<String>();

		Log.i(LOGTAG,startDate+"---"+stopDate);
		//int _startDate=Integer.parseInt(startDate)*

		credits=dbsource.findFilteredCredits("insdate>="+startDate+" and insdate<="+stopDate);

		if (credits.isEmpty()==true) {
			//Toast.makeText(this, R.string.str_emptyList, Toast.LENGTH_LONG).show();

			showToast(R.string.str_emptyList);

			getListView().setVisibility(listview.INVISIBLE);
		} else {

			getListView().setVisibility(listview.VISIBLE);

			adapter = new ArrayAdapter<classCredits>(this,R.layout.row,R.id.secondLine, credits);
			setListAdapter(adapter);
			int p=0,i;
			DecimalFormat dc=new DecimalFormat("###,###,###");

			for ( i = 0; i < adapter.getCount(); i++) {

				p=p+credits.get(i).getCredit();
				//Log.i(LOGTAG, ">>>"+String.valueOf(credits.get(i).getInsDate()));
				cats= dbsource.findFilteredCats("catID ="+credits.get(i).getCreditCat());
				String tmpCatName="";
				if (cats.isEmpty()==false) {
					tmpCatName=cats.get(0).getName();
				}


				String cList=String.valueOf(i+1)+"-"+
						" "+credits.get(i).getCreditName()+
						dc.format(credits.get(i).getCredit())+
						" --- ["+String.valueOf(credits.get(i).getYear())+"/"+
						String.valueOf(credits.get(i).getMonth())+"/"+
						String.valueOf(credits.get(i).getDay())+"] - (  "+
						tmpCatName+" )";

				tmpCredits.add(cList);

				Log.i(LOGTAG,">"+String.valueOf(p));

			}			

			//valid start and stop date so here initialize these vars to send to catsreport via intent 
			validInsDate=true;
			startdate=startDate;
			stopdate=stopDate;

			customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpCredits);
			setListAdapter(customAdapter);
			TextView total=(TextView) findViewById(R.id.gstotals);

			total.setText("جمع  "+dc.format(p)+" تومان");

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
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sel_date);
		EditText et=(EditText) findViewById(R.id.startDate);
		et.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_startDate);
			}
		});
		et=(EditText)findViewById(R.id.stopDate);
		et.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_stopDate);
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.mnu_some_date_act, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent=null;
		switch (item.getItemId()) {
		case R.id.mnuSomeDateCatsReport:

			Log.i(LOGTAG, fromDate+"..."+toDate);
			fromDate=startdate;
			toDate=stopdate;
			if (fromDate.equals("") || toDate.equals("")) {
				showToast(R.string.str_emptyDate);
			} else {


				if (fromDate.length()<8 || (toDate.length()<8)) {
					showToast(R.string.str_invalidDateMsg);
				} else {
					intent=new Intent(showSomeDateReport.this, showSomeDateCatsReport.class);
					intent.putExtra("startdate", fromDate);
					intent.putExtra("enddate", toDate);
					startActivity(intent);
				}
			}

			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPause() {
		Log.i(LOGTAG, "activity paused...");
		super.onPause();
	}
	@Override
	protected void onResume() {
		Log.i(LOGTAG, "activity resumed...");
		super.onResume();
	}
}
