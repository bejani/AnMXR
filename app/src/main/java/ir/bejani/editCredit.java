package ir.bejani;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;

public class editCredit extends AppCompatActivity {

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;

	ArrayAdapter<classCats> adapter=null;
	ArrayAdapter<String> customAdapter=null;

	private static final String LOGTAG = "activity";

	private List<classCats> cats=null;
	private  List<classCredits> credits=null;
	List<String> tmpCats;
	List<String> tmpCatIDs;
	String[] itemsCats;
	String[] itemsCatIDs;

	//Holds Old Credit ID
	long cid=0;
	private int  prev_insdate=0;
	private int  prev_year=0;
	private int  prev_month=0;
	private int  prev_day=0;
	private int sId=-1;
	private long i=0;
	private boolean catIsEmpty;
	EditText txtECredit=null;
	EditText txtECreditName=null;
	TextView txtECatSel= null;

	private static final int DIALOG_insDate = 10;

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
	View layout=null;
	LayoutInflater inflator=null;
	public String insdate="",insdateDisplay="",tmpCrTxt="";

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
		switch (id) {
		case DIALOG_insDate:
			AlertDialog.Builder alb0=new AlertDialog.Builder(this);
			inflator=getLayoutInflater();
			layout=inflator.inflate(R.layout.pic_date_layout, (ViewGroup) findViewById(R.id.rootPicDateLayout));
			Log.i(LOGTAG, "dialog");
			alb0.setTitle("تاریخ را انتخاب کنید");
			alb0.setView(layout);
			alb0.setPositiveButton("تایید", new  DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub


					int newIrYear = npYear.getValue();
					insdate=String.valueOf(newIrYear);
					insdateDisplay=insdate;
					int newIrMonth = npMonth.getValue();

					if (newIrMonth<10){
						insdate=insdate+"0"+String.valueOf(newIrMonth);
						insdateDisplay=insdateDisplay+"/0"+String.valueOf(newIrMonth);
					}
					else
					{
						insdate=insdate+String.valueOf(newIrMonth);
						insdateDisplay=insdateDisplay+"/"+String.valueOf(newIrMonth);						
					}

					int newIrDay = npDay.getValue();
					if (newIrDay<10){
						insdate=insdate+"0"+String.valueOf(newIrDay);
						insdateDisplay=insdateDisplay+"/0"+String.valueOf(newIrDay);
					}
					else
					{
						insdate=insdate+String.valueOf(newIrDay);
						insdateDisplay=insdateDisplay+"/"+String.valueOf(newIrDay);
					}
					Log.i(LOGTAG, "^^^^ "+String.valueOf(insdate));
					Log.i(LOGTAG, "^^^^ "+String.valueOf(insdateDisplay));

					TextView txtStartDate=(TextView) findViewById(R.id.txtInsDate);
					txtStartDate.setText(insdateDisplay);
					Log.i(LOGTAG, "click dialog end....");
				}

			});
			alb0.show();
			inflateDialog();
			break;
		}
		return super.onCreateDialog(id, args);
	}

	public void fetchCats() {
		cats=dbsource.findAllCats();
		
		tmpCats=new ArrayList<String>();
		tmpCatIDs=new ArrayList<String>();

		if (cats.isEmpty()==true) {
			showToast(R.string.str_emptyGroups);
			//Toast.makeText(this, "لیست  گروهها خالی است", Toast.LENGTH_LONG).show();
		} else {

			String cList="";
			for (int i = 0; i < cats.size(); i++) {
				cList=String.valueOf(i+1)+"- "+cats.get(i).getName();
				tmpCats.add(cList);
				tmpCatIDs.add(String.valueOf(cats.get(i).getId()));

			}
		}
	}
	public void opendia(View v)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(editCredit.this);
		alert.setTitle("یکی از گروهها را انتخاب کنید");

		alert.setCancelable(false);

		itemsCats=new String[tmpCats.size()];
		itemsCats=tmpCats.toArray(itemsCats);

		itemsCatIDs=new String[tmpCatIDs.size()];
		itemsCatIDs=tmpCatIDs.toArray(itemsCatIDs);
		Log.i("sID=>",tmpCatIDs.toString());
		final TextView txtCatSel= (TextView) findViewById(R.id.txtCatSel);

		alert.setItems(itemsCats,new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				txtCatSel.setText(itemsCats[which]);
				sId=Integer.parseInt(itemsCatIDs[which]);
				Log.i("sID=>",String.valueOf(sId));

			}
		});

		AlertDialog dialog = alert.create();
		dialog.show();

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_credit);


		Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
		setSupportActionBar(myToolbar);
		if(getSupportActionBar() != null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), showActRecyc.class));
				finish();
			}
		});


		List<classCats> cats=null;
		List<classCredits> credits=null;

		dbsource=new dbAdapter(this);
		dbsource.open();

		fetchCats();


		cid=Long.parseLong(getIntent().getExtras().getString("cid"));

		credits=dbsource.findFilteredCredits("cid = "+cid);
		sId=credits.get(0).getCreditCat();

		//store previous date
		prev_insdate=credits.get(0).getInsDate();
		prev_year=credits.get(0).getYear();
		prev_month=credits.get(0).getMonth();
		prev_day=credits.get(0).getDay();

		txtECreditName=(EditText) findViewById(R.id.txtCreditName);
		txtECreditName.setText(credits.get(0).getCreditName());

		txtECredit=(EditText) findViewById(R.id.txtCredit);
		txtECredit.setText(String.valueOf(credits.get(0).getCredit()));

		cats=dbsource.findFilteredCats("catID = "+credits.get(0).getCreditCat());
		txtECatSel= (TextView) findViewById(R.id.txtCatSel);
		//Toast.makeText(this,cats.get(0).getName(),Toast.LENGTH_LONG).show();

		if (cats.isEmpty()==false) {			
			txtECatSel.setText(cats.get(0).getName());
		}
		else
		{
			catIsEmpty=true;
		}
		Log.i(LOGTAG, String.valueOf(catIsEmpty));
		dbsource.close();
		
		/////////////////////////////////////////////////////////////////////////////////////
		//																s h o w :   select date
		
		TextView insDate=(TextView) findViewById(R.id.txtInsDate);
		insDate.setText(String.valueOf(prev_insdate));
		insDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_insDate);
			}
		});
		/////////////////////////////////////////////////////////////////////////////////////
		
		 final EditText txtCrdtName=(EditText) findViewById(R.id.txtCreditName);
		 
		 final EditText txtCrdt=(EditText) findViewById(R.id.txtCredit);
		 
		 txtCrdtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				DecimalFormat dc=new DecimalFormat("###,###,###");
				String tmm=txtCrdt.getText().toString();
				Log.e("0", tmm);
				tmm=tmm.replace(",", "");
				Log.e("0", tmm);
				if (!tmm.equals("")) {
					txtCrdt.setText(dc.format(Long.parseLong(tmm)));
				}

			}
		});

	}
	public void gotoMainAct(View v){
		Intent intent=new Intent(editCredit.this, showActRecyc.class);
		startActivity(intent);
		this.finish();
	}
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public void confirmUpdate(View v){

		dbsource=new dbAdapter(this);
		final classCredits credit=new classCredits();

		txtECredit=(EditText) findViewById(R.id.txtCredit);
		txtECreditName=(EditText) findViewById(R.id.txtCreditName);
		txtECatSel= (TextView) findViewById(R.id.txtCatSel);

		boolean vCatSel_Empty=true;
		Log.i(LOGTAG,"###-->"+String.valueOf(txtECatSel.toString().isEmpty()));
		if (txtECatSel.toString().equals("")) {

		} else {
			vCatSel_Empty=false;			
		}

		if (txtECredit.getText().toString().equals("") || txtECreditName.getText().toString().equals("") ) {
			showToast(R.string.str_emptyField);
			//Toast.makeText(this,"وارد کردن تمام فیلدها اجباری است", Toast.LENGTH_LONG).show();

		}
		else
		{
			//checking for category selection
			Log.i(LOGTAG,"^sID:"+String.valueOf(sId)+String.valueOf(vCatSel_Empty));
			if(txtECatSel.getText().toString().equals("")){
				showToast(R.string.str_selectCat);
				//Toast.makeText(this, " یک گروه هزینه ای برای هزینه وارد شده انتخاب نمایید", Toast.LENGTH_LONG).show();				
			}
			else {

				//credit.setCredit
				int creditVal=Integer.parseInt((txtECredit).getText().toString().replace(",",""));
				credit.setCredit(creditVal);	

				//credit.setCreditName
				credit.setCreditName(txtECreditName.getText().toString());

				//credit.setCreditType
				credit.setCreditCat(sId);

				/////////////////////////////////////////////////////////////////////
				//
				//////////////////////// date manipulation section
				//
				///////////////////////////////////////////////////////////////////////

				//take miladi date and insert it
				SimpleDateFormat sdf=new SimpleDateFormat();
				String midate=sdf.format(new Date());
				//credit.setInsDate(midate);

				//take shamsi date and insert it
				//shamsi date is split to year , month , and day
				SimplePersianCalendar prsCalendar=new SimplePersianCalendar();
				final int year=prsCalendar.getDateFields().getYear();
				final int month=prsCalendar.getDateFields().getMonth()+1;
				final int day=prsCalendar.getDateFields().getDay();


				//Plain date calculation and insertion
				final int plaindate=year*10000+month*100+day;


				//credit.setInsDate(plaindate);

				Log.i(LOGTAG,String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day));
				Log.i(LOGTAG,midate);

				//long i=dbsource.updateCredit(credit,cid);

				//SHOW DIALOG AFTER INSERT
				AlertDialog alertDialog = new AlertDialog.Builder(editCredit.this).create();

				// Setting Dialog Title
				alertDialog.setTitle("ویرایش هزینه");
				alertDialog.setIcon(R.drawable.ic_editcredit);
				Log.e("insdate",insdate);
				Log.e("prev-insdate",String.valueOf(prev_insdate));
				if (insdate.equals("")) {

					credit.setInsDate(prev_insdate);
					credit.setYear(prev_year);
					credit.setMonth(prev_month);
					credit.setDay(prev_day);
				}
				else
				{
					credit.setInsDate(Integer.parseInt(insdate));
					String sel_year=insdate.substring(0,4);
					String sel_month=insdate.substring(4,6);
					String sel_day=insdate.substring(6,8);
					credit.setYear(Integer.parseInt(sel_year));
					credit.setMonth(Integer.parseInt(sel_month));
					credit.setDay(Integer.parseInt(sel_day));

				}
					alertDialog.setMessage("هزینه قبلی با موفقیت به مقادیر جدید به روزشد.");			
					// Setting OK Button
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed
							i=dbsource.updateCredit(credit,cid);

							Intent intent=new Intent(editCredit.this, showActRecyc.class);
							startActivity(intent);
							editCredit.this.finish();

						}
					});




				// Showing Alert Message

				alertDialog.show();
				txtECredit.setText("");
				txtECreditName.setText("");
				txtECatSel.setText("");

				//Log.i(LOGTAG,"credit inserted with id = " + i);
			}

		}
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

}
