package ir.bejani;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.persiancalendar.PersianCalendarView;
import ir.mirrajabi.persiancalendar.core.PersianCalendarHandler;
import ir.mirrajabi.persiancalendar.core.interfaces.OnDayClickedListener;
import ir.mirrajabi.persiancalendar.core.models.PersianDate;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class insertCredit extends AppCompatActivity {

	dbAdapter dbsource;

	List<String> tmpCats;
	List<String> tmpCatIDs;
	String[] itemsCats;
	String[] itemsCatIDs;
	String PCV_Year="",PCV_Month="",PCV_Day="";

	private int sId=-1;
	private  List<classCats> cats=null;

	public EditText txtCrdt;
	public boolean flag,prevIF;

	public void opendia(View v)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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

		MediaPlayer player=new MediaPlayer();
		player=MediaPlayer.create(this, R.raw.uclick);
		player.start();

		overridePendingTransition(R.anim.push_out_to_left,R.anim.hold);

		flag=true;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_credit);

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
		LinearLayout rootLay= (LinearLayout) findViewById(R.id.rootLay);
		rootLay.setOnClickListener(null);


		dbsource=new dbAdapter(this);
		dbsource.open();

		refreshList();

		txtCrdt=(EditText) findViewById(R.id.txtCredit);


		final EditText txtCrdtName=(EditText) findViewById(R.id.txtCreditName);

		txtCrdtName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flag=false;				
			}
		});

		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/vazir.ttf");

		final PersianCalendarView PCV = (PersianCalendarView) findViewById(R.id.persian_calendar);
		final TextView txtInsDate=(TextView) findViewById(R.id.txtInsDate);
		final PersianCalendarHandler calendar = PCV.getCalendar();
		calendar.setTypeface(face);

		PCV.setOnDayClickedListener(new OnDayClickedListener() {
			@Override
			public void onClick(PersianDate date) {
				txtInsDate.setText(calendar.dateToString(date));
				Log.i("sel date :",String.valueOf(calendar.dateToString(date)));
				String[] strDate=String.valueOf(calendar.dateToString(date)).split(" ");

				Log.i("sel date day :",strDate[0]);
				Log.i("sel date month :",strDate[1]);
				switch (strDate[1])
				{
					case "فروردین":
						PCV_Month="01";
						break;
					case "اردیبهشت":
						PCV_Month="02";
						break;
					case "خرداد":
						PCV_Month="03";
						break;
					case "تیر":
						PCV_Month="04";
						break;
					case "مرداد":
						PCV_Month="05";
						break;
					case "شهریور":
						PCV_Month="06";
						break;
					case "مهر":
						PCV_Month="07";
						break;
					case "آبان":
						PCV_Month="08";
						break;
					case "آذر":
						PCV_Month="09";
						break;
					case "دی":
						PCV_Month="10";
						break;
					case "بهمن":
						PCV_Month="11";
						break;
					case "اسفند":
						PCV_Month="12";
						break;

				}
				Log.i("sel date year :",strDate[2]);
				PCV_Day=strDate[0];
				PCV_Year=strDate[2];
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.mnu_show_act, menu);

		return super.onCreateOptionsMenu(menu);
	}

	public void gotoMainAct(View v){
		Intent intent=new Intent(insertCredit.this, MainActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}

	//REFRESHLIST is a method for refreshing list from everywhere in the project
	public void refreshList() {
		cats=dbsource.findAllCats();

		tmpCats=new ArrayList<String>();
		tmpCatIDs=new ArrayList<String>();

		if (cats.isEmpty()==true) {
			showToast(R.string.str_emptyGroups);
			//Toast.makeText(this, "لیست  گروهها خالی است", Toast.LENGTH_LONG).show();
		} else {


			//adapter = new ArrayAdapter<classCats>(this,R.layout.listrow,R.id.rowlist, cats);
			//setListAdapter(adapter);
			String cList="";
			for (int i = 0; i < cats.size(); i++) {
				Log.i("cat: ",String.valueOf(cats.get(i).getId())+" => "+ String.valueOf(cats.get(i).getName()));
				cList=String.valueOf(i+1)+"- "+cats.get(i).getName();
				tmpCats.add(cList);
				tmpCatIDs.add(String.valueOf(cats.get(i).getId()));

			}

		}
	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings({ "deprecation", "unused" })
	public void confirmInsert(View v){

		dbsource=new dbAdapter(this);
		classCredits credit=new classCredits();

		TextView txtCatSel= (TextView) findViewById(R.id.txtCatSel);

		EditText txtCredit=(EditText) findViewById(R.id.txtCredit);
		EditText txtCreditName=(EditText) findViewById(R.id.txtCreditName);

		if (txtCredit.getText().toString().equals("") || txtCreditName.getText().toString().equals("") ) {
			showToast(R.string.str_emptyField);
		}
		else
		{
			//checking for category selection
			if (txtCatSel.getText().toString().equals("")) {
				showToast(R.string.str_selectCat);
			} else {

				//credit.setCredit
				//remove space and , from credit text value
				String crdtVal=txtCredit.getText().toString().replace(" ", "");
				crdtVal=crdtVal.replace(",", "");
				int creditVal=Integer.parseInt(crdtVal);
				credit.setCredit(creditVal);	

				//credit.setCreditName
				credit.setCreditName(txtCreditName.getText().toString());

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
				int year=prsCalendar.getDateFields().getYear();
				int month=prsCalendar.getDateFields().getMonth()+1;

				int day=prsCalendar.getDateFields().getDay();

				int plaindate=year*10000+month*100+day;
				Log.i("P Date:",String.valueOf(plaindate));
				if(PCV_Year==""){
					plaindate=year*10000+month*100+day;
					credit.setInsDate(plaindate);
					credit.setYear(year);
					credit.setMonth(month);
					credit.setDay(day);
				}
				else
				{
					plaindate=Integer.parseInt(PCV_Year)*10000+Integer.parseInt(PCV_Month)*100+Integer.parseInt(PCV_Day);
					credit.setInsDate(plaindate);
					String sel_year=PCV_Year;
					String sel_month=PCV_Month;
					String sel_day=PCV_Day;
					credit.setYear(Integer.parseInt(sel_year));
					credit.setMonth(Integer.parseInt(sel_month));
					credit.setDay(Integer.parseInt(sel_day));
				}

				//READY TO INSERT CREDIT TO TABLE "CREDIT"
				credit=dbsource.insertCredit(credit);

				//SHOW DIALOG AFTER INSERT
				AlertDialog alertDialog = new AlertDialog.Builder(insertCredit.this).create();

				// Setting Dialog Title
				alertDialog.setTitle("هزینه جدید");

				// Setting Dialog Message
				alertDialog.setMessage("هزینه وارد شده با موفقیت ذخیره گردید.");			

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
				txtCredit.setText("");
				txtCreditName.setText("");
				txtCatSel= (TextView) findViewById(R.id.txtCatSel);
				txtCatSel.setText("");
				txtCatSel.setHint("کلیک کنید");

			}

		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
