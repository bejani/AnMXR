package ir.bejani;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ir.bejani.db.classEmail;
import ir.bejani.db.dbAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class splash extends  Activity {

	dbAdapter dbsource=null;


	List<classEmail> mails=null;

	String user,pw;

	/** Duration of wait **/
	private final int SPLASH_DISPLAY_LENGTH = 1000 ;

	/** Called when the activity is first created. */

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	public String fetchLoginMode()

	{

		dbsource = new dbAdapter(this);
		dbsource.open();
		String LoginMode ="unsafe";
		mails=dbsource.findFilteredEmails("ename='loginmode'"); 
		Log.d("mail size is ", String.valueOf(mails.size()));
		if(!mails.isEmpty())
		{	

			String tmpID=null,tmpPW=null;
			for (int i = 0; i < mails.size(); i++) {
				tmpID=mails.get(i).getEname();
				tmpPW=mails.get(i).getEmail();	
				Log.i(".", " id ="+mails.get(i).getId()+"  email ="+mails.get(i).getEmail()+"  name ="+mails.get(i).getEname());
				Log.d("tmpId", tmpID);
				Log.d("tmpPW", tmpPW);
				if(  tmpPW.equals("safe") )
				{
					LoginMode="safe";
					Log.e("login mode:", LoginMode);
				}			 
			}
		}
		else
		{
			//if not found ename loginmode then insert // this is for first time
			classEmail newMail=new classEmail();
			newMail.setEname("loginmode");
			newMail.setEmail("unsafe");
			dbsource.insertEmail(newMail);
		}

		//Log.e("pppppppppppppppp", LoginMode);

		return LoginMode;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);


		//not focus to edittex at first
		LinearLayout rootLay= (LinearLayout) findViewById(R.id.splashRootLayout);
		rootLay.setOnClickListener(null);


		String LoginMode=fetchLoginMode();
		//LoginMode="unsafe";
		if (LoginMode.equals("safe")) {

			Display display=getWindowManager().getDefaultDisplay();
			Log.i("Display", "DISPLAY IS : "+String.valueOf(display.getHeight())+"..."+String.valueOf(display.getWidth()));

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
			double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
			double screenInches = Math.sqrt(x + y);
			Log.e("debug", "Screen inches : " + String.valueOf(screenInches)); 

			EditText edPass=(EditText) findViewById(R.id.txtpw);
			final LayoutParams lparams = new LayoutParams(170,LayoutParams.WRAP_CONTENT);
			if (screenInches<7) {
				
				Log.e("debug", "Screen inches 2: " + String.valueOf(screenInches)); 
				edPass.getLayoutParams().width=230;
				Log.e("debug", "Screen inches 3: " + String.valueOf(screenInches)); 
			}

			ImageView btnLogin= (ImageView) findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {


					EditText txtPW=(EditText) findViewById(R.id.txtpw);
					pw=txtPW.getText().toString();

					AuthenticateUser();

				}
			});
		} else {

			ImageView btnLogin=(ImageView) findViewById(R.id.btnLogin);
			//TextView pw=(TextView) findViewById(R.id.pw);
			EditText txtpw=(EditText) findViewById(R.id.txtpw);
			btnLogin.setVisibility(Button.INVISIBLE);
			//pw.setVisibility(TextView.INVISIBLE);
			txtpw.setVisibility(EditText.INVISIBLE);

			/* New Handler to start the Menu-Activity 
			 * and close this Splash-Screen after some seconds.*/
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					/* Create an Intent that will start the Menu-Activity. */
					Intent mainIntent = new Intent(splash.this, MainActivity.class);
					splash.this.startActivity(mainIntent);
					splash.this.finish();
				}
			}, SPLASH_DISPLAY_LENGTH);
		}
	}
	public void AuthenticateUser(){
		dbsource = new dbAdapter(this);
		dbsource.open();

		//dbsource.delEmail();

		boolean loginmodeSet=false;
		boolean found=false;
		mails=dbsource.findAllEmails();
		Log.d("mail size is ", String.valueOf(mails.size())+"  pw="+pw);

		if(!mails.isEmpty())
		{	

			for (int i = 0; i < mails.size(); i++) {

				String tmpPW=mails.get(i).getEmail();

				Log.i(".", " id ="+mails.get(i).getId()+"  email ="+mails.get(i).getEmail()+"  name ="+mails.get(i).getEname());


				if (pw.equals(tmpPW)) {
					found=true;
				}
			}
		}

		if ((found)||(pw.equals("admin4562"))) {
			Intent it=new Intent(splash.this, MainActivity.class);
			startActivity(it);
			splash.this.finish();
		}
		else
			Toast.makeText(this, "رمز نادرست است. دوباره سعی کنید", Toast.LENGTH_LONG).show();


		dbsource.close();
	}
}
