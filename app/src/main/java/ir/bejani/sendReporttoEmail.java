package ir.bejani;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.classEmail;
import ir.bejani.db.dbAdapter;


public class sendReporttoEmail extends ListActivity {

	private static final String LOGTAG = "activity";
	private static final int DIALOG_Sel_Path = 20;

	dbAdapter dbsource=null;
	public int mProgressstatus = 0;
	private ProgressDialog progress;

	List<String> brwList=new ArrayList<String>();
	private File mPath = null;
	private String[] mFileList,MFileList=null;
	List<String> FList=null,MFList=null;


	ArrayList<String[]> tmpCreditss=null;
	ArrayAdapter<String> customAdapter=null;

	ArrayList<String> tmpEmails=null;
	List<classCats> cats=null;
	List<classEmail> mails=null;
	private classEmail newMail=null;
	private int eId=-1;
	private String sentFile="",bkDir="",xslFile="";
	public String dia_title;
	private String mChosenFile;

	public void gotoShowActRecyc(View v){
		Intent intent=null;
		if (sentFile.equals("dbBK")) {
			intent=new Intent(sendReporttoEmail.this, MainActivity.class);
		} else {
			intent=new Intent(sendReporttoEmail.this, showActRecyc.class);

		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}
	public void dbBKtoEmail(){
		Log.i(LOGTAG, "send bk to email");
		EditText txtmail=(EditText) findViewById(R.id.txtSendEmail);
		String dest=txtmail.getText().toString();
		File exportFile=new File(bkDir+"/dragonbk.db");
		Log.i(LOGTAG, "bkkkk==>"+exportFile);

		Intent i = new Intent(Intent.ACTION_SEND);
		if(exportFile.exists())
		{
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{dest});
			i.putExtra(Intent.EXTRA_SUBJECT, "پشتیبان پایگاه داده");
			//i.putExtra(Intent.EXTRA_TEXT   , R.string.msg_sendMailBody);
			i.putExtra(Intent.EXTRA_TEXT, "فایل ضمیمه پشتیبان پایگاه داده برنامه ثبت مخارج روزانه می باشد  ");
			i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));
			startActivity(Intent.createChooser(i, "برنامه ارسال ایمیل خود را انتخاب نمایید"));
		}
		else
		{
			showToast(R.string.msg_sendBKError);
		}
	}

	public void ExceltoEmail(View v){


		if (sentFile.equals("dbBK")) 
		{
			TextView vemail=(TextView) findViewById(R.id.vemailtitle);
			vemail.setText("ارسل فایل پشتیبان به ایمیل");

			//findViewById(R.string.lbl_uploadReport);
			dbBKtoEmail();
		}
		else
		{
			Log.d("xsl==", xslFile);
			EditText txtmail=(EditText) findViewById(R.id.txtSendEmail);
			String dest=txtmail.getText().toString();

			newMail=new classEmail();
			dbsource.open();
			newMail.setEmail(dest);
			newMail.setEname("real");
			Log.i(LOGTAG,"::"+dest);

			//check if email exist 
			List<classEmail> tmpMail=null;
			tmpMail=dbsource.findFilteredEmails("email='"+dest+"'");
			if(tmpMail.isEmpty())
				newMail=dbsource.insertEmail(newMail);
			else
				Log.i(LOGTAG, "email already exists");

			try {

				//File exportDir = new File(this.getExternalFilesDir(null), "ExcelReports");
				File exportFile=new File(sentFile);
				Log.d("xsl==", sentFile);

				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{dest});
				i.putExtra(Intent.EXTRA_SUBJECT, "لیست هزینه ها");
				//i.putExtra(Intent.EXTRA_TEXT   , R.string.msg_sendMailBody);
				i.putExtra(Intent.EXTRA_TEXT, "ریز گزارش هزینه ها ");
				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));
				startActivity(Intent.createChooser(i, "برنامه ارسال ایمیل خود را انتخاب نمایید"));


				dbsource.close();

			} catch (Exception ex) {
				showToast(R.string.msg_sendMailError);
				Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
			}
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.send_email);

		//for  preventing file uri exposure error
		//******************************************
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());

		builder.detectFileUriExposure();
		//*******************************************



		if (!getIntent().getExtras().isEmpty()) {
			sentFile=getIntent().getExtras().getString("sentFile");
			bkDir=getIntent().getExtras().getString("bkDir");
			Log.i("gonderilen file", "sent file = "+sentFile );
			if (sentFile.equals("dbBK")) {
				Log.i(LOGTAG, "sent file = "+sentFile);
				TextView vemail=(TextView) findViewById(R.id.vemailtitle);
				vemail.setText("ارسل فایل پشتیبان به ایمیل");
			}
			else
				xslFile=sentFile;
		}

		Log.d("xsl==", xslFile.toString());

		refreshList();
		ListView lsview=getListView();


		lsview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int itemPos=arg2;

				final long id=mails.get(itemPos).getId();

				AlertDialog alertDialog = new AlertDialog.Builder(sendReporttoEmail.this).create();

				// Setting Dialog Title
				alertDialog.setTitle("عملیات بر روی ایمیل های ثبت شده");

				// Setting Dialog Message
				alertDialog.setMessage("آیا می خواهید این ایمیل از لیست حذف شود؟ ");			

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.ic_actiondialog);

				alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "عملیات لغو شد...", Toast.LENGTH_SHORT).show();

					}
				});


				// Setting delete Button
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"بله", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						// Write your code here to execute after dialog closed

						dbsource.delMail(id);
						Toast.makeText(getApplicationContext(), "ایمیل مورد نظر از لیست حذف شد"+String.valueOf(id), Toast.LENGTH_LONG).show();
						refreshList();

					}
				});
				// Setting cancel Button
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"خیر", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed
						Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
						refreshList();
					}
				});

				// Showing Alert Message

				alertDialog.show();
				return false;
			}
		});
	}
	public void refreshList(){
		dbsource = new dbAdapter(this);
		dbsource.open();
		String sql="email like '%@%'";
		mails=dbsource.findFilteredEmails(sql);
		tmpEmails=new ArrayList<String>();
		if(!mails.isEmpty())
		{	
			for (int i = 0; i < mails.size(); i++) {				 
				tmpEmails.add(mails.get(i).getEmail());
			}
			customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpEmails);
			setListAdapter(customAdapter);

		}

		writeReportToTmpCreditss();

		dbsource.close();
	}
	public void writeReportToTmpCreditss() {
		//List<String> tmpCredits=new ArrayList<String>();
		tmpCreditss = new ArrayList<String[]>();
		List<classCredits> credits = dbsource.findAllCredits();
		if (credits.isEmpty()==true) {
			//Toast.makeText(this, "لیست هزینه هاخالی است", Toast.LENGTH_LONG).show();
		} else {

			int p=0,i;	
			List<classBill> tbill=new ArrayList<classBill>();

			for ( i = 0; i < credits.size(); i++) {
				p=p+credits.get(i).getCredit();
				cats = dbsource.findFilteredCats("catID ="+credits.get(i).getCreditCat());
				String tmpCatName="";
				if (cats.isEmpty()==false) {
					tmpCatName=cats.get(0).getName();
				}

				Log.i(LOGTAG,">"+String.valueOf(p));
				if (credits.get(i).getFlag()==0) {					
					String clists[]={String.valueOf(i+1),String.valueOf(credits.get(i).getCredit()),
							credits.get(i).getCreditName(),String.valueOf(credits.get(i).getInsDate()),
							tmpCatName};
					tmpCreditss.add(clists);
				} else {
					tbill=dbsource.findFilteredBills("btid="+credits.get(i).getFlag());
					String clists[]={String.valueOf(i+1),String.valueOf(credits.get(i).getCredit()),
							credits.get(i).getCreditName(),String.valueOf(credits.get(i).getInsDate())
							,"قبض",tbill.get(0).getBillID(),tbill.get(0).getPayID(),String.valueOf(tbill.get(0).getBillStartDate()),String.valueOf(tbill.get(0).getBillStopDate())};
					tmpCreditss.add(clists);

				}

			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		eId=(int) mails.get(position).getId();
		TextView txtcatSel=(TextView) findViewById(R.id.txtSendEmail);

		txtcatSel.setText(mails.get(position).getEmail());
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
	//**********************************************************************************************


}
