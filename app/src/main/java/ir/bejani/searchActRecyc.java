package ir.bejani;


import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class searchActRecyc extends AppCompatActivity {

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	dbAdapter dbsource;
	RecyclerView recyclerView;
	CrdtListAdapter mAdapter;

	private List<clsCreditView> CrdtList = new ArrayList<>();

	private List<classCredits> credits=null;
	private List<classCats> cats=null;

	ArrayAdapter<classCredits> adapter=null;
	List<String> tmpCredits=null;
	ArrayAdapter<String> customAdapter=null;

	private static final String LOGTAG = "activity";

	public void gotoMainAct(View v){

		Intent intent=new Intent(searchActRecyc.this, MainActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
		setContentView(R.layout.search_recyc);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		});

	}

	public void showSearchResult(View v){

		EditText txtSearch=(EditText) findViewById(R.id.txtCreditName);
		if(txtSearch.getText().toString().equals("") || txtSearch.getText().toString().contains("'") ){
			showToast(R.string.str_emptyOrInvalidEntry);
		}
		else
		{
			recyclerView = findViewById(R.id.recycler_view);
			CrdtList=new ArrayList<>();
			mAdapter = new CrdtListAdapter(getApplicationContext(),CrdtList);
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
			recyclerView.setLayoutManager(mLayoutManager);
			recyclerView.setItemAnimator(new DefaultItemAnimator());

			final LinearLayoutManager layoutManager = new  LinearLayoutManager(this);
			layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

			recyclerView.setAdapter(mAdapter);

			fetchResults(txtSearch.getText().toString());

		}

	}

	@SuppressWarnings("static-access")
	public void fetchResults(String creditName){

		dbsource=new dbAdapter(this);
		dbsource.open();
		clsCreditView CREDIT;

		List<classBill> tmpBill=new ArrayList<classBill>();
		tmpCredits=new ArrayList<String>();

		Log.i(LOGTAG, creditName);

		credits=dbsource.findFilteredCredits("creditname like '%"+creditName+"%'");
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

//				long tmpCrdtID=credits.get(i).getId();
				String tmpCrdt=dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())));
				String tmpCrdtName=String.valueOf(i + 1) + ". " +credits.get(i).getCreditName();
				String tmpCrdtDate= String.valueOf(credits.get(i).getInsDate());
				String tmpCrdtYear = String.valueOf(credits.get(i).getYear());
				String tmpCrdtMOnth = String.valueOf(credits.get(i).getMonth());
				String tmpCrdtDay = String.valueOf(credits.get(i).getDay());
				String tmpCrdtGrp=tmpCatName;
				String tmpCrdtID=String.valueOf(credits.get(i).getId());


				CREDIT = new clsCreditView(tmpCrdtID,tmpCrdt, tmpCrdtName, tmpCrdtDate,tmpCrdtYear,tmpCrdtMOnth,tmpCrdtDay, tmpCrdtGrp,String.valueOf(credits.get(i).getFlag()));

				CrdtList.add(CREDIT);

			}
			TextView total=(TextView) findViewById(R.id.srtotals);
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




}




