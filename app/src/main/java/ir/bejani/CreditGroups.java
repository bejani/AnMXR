package ir.bejani;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classCats;
import ir.bejani.db.dbAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreditGroups extends ListActivity {

	private static final String LOGTAG = "activity";
	private static final int DIALOG_editCatName = 10;
	private static final int DIALOG_Excel_Path = 10;



	SQLiteOpenHelper dbHelper;
	SQLiteDatabase database;
	dbAdapter dbsource;
	private List<classCats> cats=null;
	ArrayAdapter<classCats> adapter=null;
	List<String> tmpCredits=null;
	ArrayAdapter<String> customAdapter=null;

	public String oldCat="";

	//REFRESHLIST is a method for refreshing list from everywhere in the project
	public void refreshList() {
		cats=dbsource.findAllCats();

		List<String> tmpCats=new ArrayList<String>();

		if (cats.isEmpty()==true) {
			showToast(R.string.str_emptyGroups);
			//Toast.makeText(this, "لیست  گروهها خالی است", Toast.LENGTH_LONG).show();
			getListView().setVisibility(ListView.INVISIBLE);
		} else {

			getListView().setVisibility(ListView.VISIBLE);

			adapter = new ArrayAdapter<classCats>(this,R.layout.row,R.id.secondLine, cats);
			setListAdapter(adapter);
			String cList="";
			for ( int i = 0; i < adapter.getCount(); i++) {
				cList=String.valueOf(i+1)+"- "+cats.get(i).getName();
				tmpCats.add(cList);

			}
			customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpCats);
			setListAdapter(customAdapter);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MediaPlayer player=new MediaPlayer();
		player=MediaPlayer.create(this, R.raw.uclick);
		player.start();

		overridePendingTransition(R.anim.push_out_to_left,R.anim.hold);

		setContentView(R.layout.define_crdtgrp);

		//not focus to edittex at first
		RelativeLayout rootLay= findViewById(R.id.rootLay);
		rootLay.setOnClickListener(null);

		dbsource=new dbAdapter(this);

		dbsource.open();

		refreshList();

		//showToast(R.string.str_listMessage);
		ListView lsview=getListView();
		//lsview.setOnItemClickListener();
		lsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int itemPos=arg2;

				final long id=cats.get(itemPos).getId();
				Log.i(LOGTAG,"-->"+String.valueOf(id));
				final String catname=cats.get(itemPos).getName();

				oldCat=catname;

				AlertDialog alertDialog = new AlertDialog.Builder(CreditGroups.this).create();

				// Setting Dialog Title
				alertDialog.setMessage("گزینه عملیاتی مورد نظر را انتخاب کنید");

				// Setting Dialog Message
				alertDialog.setTitle("عملیات روی گروههای هزینه ای");			

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
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"مشاهده جزئیات", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed
						//Toast.makeText(CreditGroups.this, " گروه مورد نظر از لیست حذف شد"+String.valueOf(id), Toast.LENGTH_LONG).show();

						Intent intent=new Intent(CreditGroups.this, showDetailsRecyc.class);
						Log.i(LOGTAG,"-->"+String.valueOf(id));

						intent.putExtra("catid", String.valueOf(id));
						intent.putExtra("catname", catname);
						startActivity(intent);

					}
				});
				// Setting delete Button
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"حذف", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						// Write your code here to execute after dialog closed

						dbsource.delCat(id);
						showToast(R.string.msg_GroupDeleted);
						//Toast.makeText(CreditGroups.this, " گروه مورد نظر از لیست حذف شد"+String.valueOf(id), Toast.LENGTH_LONG).show();
						refreshList();

					}
				});
				// Setting cancel Button
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"ویرایش", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed

						//Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
						showDialog(DIALOG_editCatName);
					}
				});

				// Showing Alert Message

				alertDialog.show();
				//return false;
			}
		});
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	public void gotoMainAct(View v){
		Intent intent=new Intent(CreditGroups.this, MainActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}
	@SuppressWarnings("deprecation")
	public void confrimDefine(View v){

		dbsource=new dbAdapter(this);

		dbsource.open();
		classCats cat=new classCats();

		refreshList();

		EditText txtCats=(EditText) findViewById(R.id.txtCategories);

		if (txtCats.getText().toString().equals("") ) {
			showToast(R.string.cats_empty_error);
			//Toast.makeText(this,"وارد کردن فیلد عنوان گروه هزینه ای الزامی است", Toast.LENGTH_LONG).show();
		}
		else
		{

			//cat.setName			
			cat.setName(txtCats.getText().toString());

			cat=dbsource.insertcat(cat);

			AlertDialog alertDialog = new AlertDialog.Builder(CreditGroups.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("تعریف گروه هزینه ها");

			// Setting Dialog Message
			alertDialog.setMessage("گروه هزینه ای وارد شده با موفقیت ایجاد شد");

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
			txtCats.setText("");
			refreshList();
			Log.i(LOGTAG,"cat inserted with id = " + cat);

		}

	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Toast.makeText(this,R.string.str_listMessage, Toast.LENGTH_LONG).show();

		//showToast(R.string.str_listMessage);
		super.onListItemClick(l, v, position, id);
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
	//	@Override
	//	protected void onResume() {
	//		super.onResume();
	//		dbsource.open();
	//		
	//	}
	@Override
	protected void onPause() {
		super.onPause();
		dbsource.close();
	}
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DIALOG_editCatName:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("ویرایش عنوان گروه هزینه ای");
			alertDialog.setMessage("عنوان جدیدی وارد کنید");

			final EditText newCat = new EditText(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			newCat.setLayoutParams(lp);
			alertDialog.setView(newCat);
			alertDialog.setIcon(R.drawable.ic_editcredit);

			alertDialog.setPositiveButton("تایید",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					int upd=dbsource.UpdateCats(newCat.getText().toString(), oldCat);
					refreshList();
					if(upd==0)
					{
						Toast.makeText(getApplicationContext(), "ویرایش عنوان گروه هزینه ای با موفقیت انجام گرفت", Toast.LENGTH_LONG).show();						
					}
					else
					{
						Toast.makeText(getApplicationContext(), "به روزرسانی عنوان گروه هزینه ای با خطا متوقف شد - لطفا از ابتدا دوباره سعی نمایید", Toast.LENGTH_LONG).show();
					}
				}
			});

			alertDialog.setNegativeButton("لغو",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_LONG).show();
					dialog.cancel();
				}
			});

			alertDialog.show();
		}


		return super.onCreateDialog(id);
	}
	
}
