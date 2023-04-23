package ir.bejani;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class insertBill extends AppCompatActivity {

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

	public String[] opts = {""};

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;

	ListView listview;
	ArrayAdapter<String> customAdapter=null;
	String[] tmpBillList={"آب","برق","گاز","تلفن","موبایل","شهرداری","دارایی","سایر"};


	ArrayAdapter<classCredits> adapter=null;
	List<String> tmpCredits=null;
	List<String> altCredits=null;

	private static final String LOGTAG = "activity";
	@SuppressWarnings("unused")
	private boolean validInsDate=false;
	public String startdate="0",stopdate="0",startdateDisplay="",stopdateDisplay="";
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
		int iranianMonth = jdf.getDateFields().getMonth();
		int iranianDay = jdf.getDateFields().getDay();

		npYear.setMinValue(1385);
		npYear.setMaxValue(1420);
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
	public void OpenDia(View v) {

		opts = new String[7];
		opts[0] = "آب";
		opts[1] = "برق";
		opts[2] = "گاز";
		opts[3] = "تلفن";
		opts[4] = "موبایل";
		opts[5] = "شهرداری";
		opts[6] = "سایر";

		AlertDialog.Builder alert = new AlertDialog.Builder(insertBill.this);
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

	@Override

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
//					Log.i(LOGTAG, "^^^^ "+String.valueOf(startdate));
//					Log.i(LOGTAG, "^^^^ "+String.valueOf(startdateDisplay));

					EditText txtStartDate=(EditText) findViewById(R.id.billStartDate);
					txtStartDate.setText(startdateDisplay);
//					Log.i(LOGTAG, "click dialog end....");
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

		Intent intent=new Intent(insertBill.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MediaPlayer player=new MediaPlayer();
		player=MediaPlayer.create(this, R.raw.uclick);
		player.start();




		overridePendingTransition(R.anim.push_out_to_left,R.anim.hold);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_bill);


		Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
		setSupportActionBar(myToolbar);

		if(getSupportActionBar() != null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});


		//not focus to edittex at first
		RelativeLayout rootLay= (RelativeLayout) findViewById(R.id.RootLay);
		rootLay.setOnClickListener(null);


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

	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  INSERT BILLS PROC
	//
	//////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	public void confirmInsertBill(View v){

		dbsource=new dbAdapter(this);
		classBill bill=new classBill();

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
			bill.setBillPayable(billPayable);
//			if (startdate.equals("")) {
//				startdate="0";
//			} else {
//
//			}
			bill.setBillStartDate(Integer.parseInt(startdate));
			bill.setBillStopDate(Integer.parseInt(stopdate));

			String billType=txtBillTypeSel.getText().toString();
			bill.setBillType(billType);

			String billID=txtBillId.getText().toString();
			bill.setBillID(billID);

			String payID=txtPayId.getText().toString();
			bill.setPayID(payID);

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
			bill.setPayDate(plaindate);
			Log.i(LOGTAG,bill.toString());

			//READY TO INSERT CREDIT TO TABLE "CREDIT"
			bill=dbsource.insertBill(bill);

			//SHOW DIALOG AFTER INSERT
			AlertDialog alertDialog = new AlertDialog.Builder(insertBill.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("ثبت قبض جدید");

			// Setting Dialog Message
			alertDialog.setMessage("مشخصات قبض پرداختی وارد شده با موفقیت ذخیره گردید.");			

			// Setting Icon to Dialog
			alertDialog.setIcon(R.drawable.ic_insertact);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
				}
			});

			// Showing Alert Message
			alertDialog.show();
//			txtBillTypeSel.setText("");
			txtBillId.setText("");
			txtBillPayable.setText("");
			txtBillStartDate.setText("");
			txtBillStopDate.setText("");
			txtPayId.setText("");

		}
	}
}


