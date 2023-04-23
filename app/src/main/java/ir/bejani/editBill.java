package ir.bejani;

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
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;


public class editBill extends AppCompatActivity {

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
	ArrayAdapter<String> customAdapter=null;
	String[] tmpBillList={"آب","برق","گاز","تلفن","موبایل","شهرداری","دارایی","سایر"};
	public String[] opts = {""};


	ArrayAdapter<classCredits> adapter=null;
	List<String> tmpCredits=null;
	List<String> altCredits=null;

	private static final String LOGTAG = "activity";
	public String startdate="0",stopdate="0",startdateDisplay="",stopdateDisplay="";
	public String prev_start="0",prev_stop="0";
	LayoutInflater inflator=null;
	View layout=null;
	EditText txtBillPayable=null;
	EditText txtBillStartDate=null;
	EditText txtBillStopDate=null;
	TextView txtBillTypeSel=null;
	EditText txtBillID=null;
	EditText txtPayID=null;

	public int prev_insdate=0;
	String prev_year,prev_month,prev_day;
	long btid=0,cid=0;
	public classBill clsbill;
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
		int iranianMonth = jdf.getDateFields().getMonth();
		int iranianDay = jdf.getDateFields().getDay();

		npYear.setMinValue(1385);
		npYear.setMaxValue(1400);
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
					Log.i(LOGTAG, "^^^^ "+String.valueOf(stopdate));
					Log.i(LOGTAG, "^^^^ "+String.valueOf(stopdateDisplay));

					EditText txtStartDate=(EditText) findViewById(R.id.billStopDate);
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

					EditText txtStartDate=(EditText) findViewById(R.id.billStartDate);
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

	public void gotoShowAct(View v){

		Intent intent=new Intent(editBill.this, showActRecyc.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbsource=new dbAdapter(this);

		List<classBill> bill=null;
		setContentView(R.layout.edit_bill);

		clsbill=new classBill();
		btid=Long.parseLong(getIntent().getExtras().getString("btid"));
		cid=Long.parseLong(getIntent().getExtras().getString("creditID"));
		Log.i(LOGTAG, "--()--"+String.valueOf(btid));

		bill=dbsource.findFilteredBills("btid = "+btid);

		//store previous date
		prev_insdate=bill.get(0).getPayDate();
		prev_year=String.valueOf(prev_insdate).substring(0,4);
		prev_month=String.valueOf(prev_insdate).substring(4,6);
		prev_day=String.valueOf(prev_insdate).substring(6,8);

		txtBillPayable=(EditText) findViewById(R.id.txtBillPayable);
		txtBillPayable.setText(String.valueOf(bill.get(0).getBillPayable()));

		txtBillStartDate=(EditText) findViewById(R.id.billStartDate);
		if (bill.get(0).getBillStartDate()!=0) {			
			String tmpStartRaw=String.valueOf(bill.get(0).getBillStartDate());
			prev_start=tmpStartRaw;
			String tmpStart=tmpStartRaw.substring(0,4)+"/"+tmpStartRaw.substring(4, 6)+"/"+tmpStartRaw.substring(6, 8);
			txtBillStartDate.setText(tmpStart);
		}

		txtBillStopDate=(EditText) findViewById(R.id.billStopDate);
		if (bill.get(0).getBillStopDate()!=0) {	
			String tmpStopRaw=String.valueOf(bill.get(0).getBillStopDate());
			prev_stop=tmpStopRaw;
			String tmpStop=tmpStopRaw.substring(0,4)+"/"+tmpStopRaw.substring(4, 6)+"/"+tmpStopRaw.substring(6, 8);
			txtBillStopDate.setText(tmpStop);
		}

		txtBillTypeSel=(TextView) findViewById(R.id.txtBillTypeSel);
		txtBillTypeSel.setText(String.valueOf(bill.get(0).getBillType()));

		txtBillID=(EditText) findViewById(R.id.txtBillId);
		txtBillID.setText(String.valueOf(bill.get(0).getBillID()));

		txtPayID=(EditText) findViewById(R.id.txtPayID);
		txtPayID.setText(String.valueOf(bill.get(0).getPayID()));

//		customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpBillList);
//		setListAdapter(customAdapter);
		dbsource.close();

		EditText etStart=(EditText) findViewById(R.id.billStartDate);
		etStart.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_startDate);
			}
		});
		etStart=(EditText) findViewById(R.id.billStopDate);
		etStart.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_stopDate);
			}
		});

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
	public void OpenDia(View v) {

		opts = new String[7];
		opts[0] = "آب";
		opts[1] = "برق";
		opts[2] = "گاز";
		opts[3] = "تلفن";
		opts[4] = "موبایل";
		opts[5] = "شهرداری";
		opts[6] = "سایر";

		AlertDialog.Builder alert = new AlertDialog.Builder(editBill.this);
		alert.setTitle("نوع قبض را انتخاب کنید");
		alert.setCancelable(false);
		alert.setItems(opts, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				TextView tv = (TextView) findViewById(R.id.txtBillTypeSel);
				tv.setText(opts[which]);
				tv.setTextColor(getResources().getColor(R.color.bgDark));

			}
		});

		AlertDialog dialog = alert.create();
		dialog.show();
	}

	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  CONFIRM UPDATE BILLS PROC
	//
	//////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("deprecation")
	public void confirmUpdateBill(View v){

		dbsource=new dbAdapter(this);
		//final classBill bill=new classBill();

		EditText txtBillPayable=(EditText) findViewById(R.id.txtBillPayable);
		EditText txtBillStartDate=(EditText) findViewById(R.id.billStartDate);
		EditText txtBillStopDate=(EditText) findViewById(R.id.billStopDate);
		TextView txtBillTypeSel= (TextView) findViewById(R.id.txtBillTypeSel);
		EditText txtBillId=(EditText) findViewById(R.id.txtBillId);
		EditText txtPayId=(EditText) findViewById(R.id.txtPayID);

		if (txtBillPayable.getText().toString().equals("") || txtBillTypeSel.getText().toString().equals("") ) {
			showToast(R.string.msg_billEmptyField);
			//Toast.makeText(this,"وارد کردن تمام فیلدها اجباری است", Toast.LENGTH_LONG).show();
		}
		else
		{
			//bill.setbillpayable
			int billPayable=Integer.parseInt((txtBillPayable).getText().toString());
			clsbill.setBillPayable(billPayable);

			//SHOW DIALOG AFTER INSERT
			AlertDialog alertDialog = new AlertDialog.Builder(editBill.this).create();
			
			Log.i(LOGTAG, startdate+"&&&&&&&&&&&&&&&&&"+stopdate);
			Log.i(LOGTAG, prev_start+"***********************"+prev_stop);
			if (startdate.equals("0")) {
				startdate=prev_start;
			} 
			if (stopdate.equals("0")) {
				stopdate=prev_stop;
			}
			clsbill.setBillStartDate(Integer.parseInt(startdate));
			clsbill.setBillStopDate(Integer.parseInt(stopdate));

			String billType=txtBillTypeSel.getText().toString();
			clsbill.setBillType(billType);

			String billID=txtBillId.getText().toString();
			clsbill.setBillID(billID);

			String payID=txtPayId.getText().toString();
			clsbill.setPayID(payID);

			/////////////////////////////////////////////////////////////////////
			//
			//////////////////////// date manipulation section
			//
			///////////////////////////////////////////////////////////////////////

			//take shamsi date and insert it
			//shamsi date is split to year , month , and day
			SimplePersianCalendar prsCalendar=new SimplePersianCalendar();
			int year=prsCalendar.getDateFields().getYear();
			int month=prsCalendar.getDateFields().getMonth();
			month=month+1;
			int day=prsCalendar.getDateFields().getDay();

			//Plain date calculation and insertion
			int plaindate=year*10000+month*100+day;
			clsbill.setPayDate(plaindate);
			Log.i(LOGTAG,clsbill.toString());
			//READY TO UPDATE BILL TO TABLE "BILLS" but first must check insertion date

			// Setting Dialog Title
			alertDialog.setTitle("ویرایش قبوض");

			// Setting Icon to Dialog
			alertDialog.setIcon(R.drawable.ic_editcredit);
			Log.i(LOGTAG, String.valueOf(prev_insdate)+"-->"+String.valueOf(plaindate));
			if (prev_insdate==plaindate) {

				clsbill.setPayDate(prev_insdate);
				alertDialog.setMessage("هزینه قبلی با موفقیت به مقادیر جدید به روزشد.");			
				// Setting OK Button
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Log.i(LOGTAG, String.valueOf(btid)+" $ "+String.valueOf(cid)+" $ PID= "+bill.getPayID()+" $ BID="+bill.getBillID());
						long i=dbsource.updateBill(clsbill,btid,cid);

						Intent intent=new Intent(editBill.this, showActRecyc.class);
						startActivity(intent);
						editBill.this.finish();
					}
				});

			} 
			else 
			{
				// Setting Dialog Message
				String prv=String.valueOf(prev_year)+"/"+
						String.valueOf(prev_month)+"/"+
						String.valueOf(prev_day);
				alertDialog.setMessage("  پرداخت قبض قبلی در این تاریخ صورت گرفته است"+"["+prv+"]");			

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.ic_editcredit);

				// Setting edit Button
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"ثبت پرداخت با تاریخ قبلی", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed
						clsbill.setPayDate(prev_insdate);
						Log.i(LOGTAG, "===>>>"+String.valueOf(prev_insdate)+"<---->"+String.valueOf(clsbill.getPayDate()));
						long i=dbsource.updateBill(clsbill,btid,cid);

						Intent intent=new Intent(editBill.this, showActRecyc.class);

						startActivity(intent);
						editBill.this.finish();
					}
				});
				// Setting delete Button
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"ثبت پرداخت به تاریخ امروز", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						//bill.setPayDate(plaindate);
						long i=dbsource.updateBill(clsbill,btid,cid);
						Intent intent=new Intent(editBill.this, showActRecyc.class);
						startActivity(intent);
						editBill.this.finish();
					}
				});
			}
			alertDialog.show();
		}
	}
}
