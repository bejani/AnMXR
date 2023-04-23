package ir.bejani;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import ir.bejani.db.classEmail;
import ir.bejani.db.dbAdapter;

public class Settings extends AppCompatActivity {

	dbAdapter dbsource=null;

	List<classEmail> mails=null;

	String user,pw;	

	public void gotoMainAct(View v){
		Intent intent=new Intent(Settings.this, MainActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MediaPlayer player=new MediaPlayer();
		player=MediaPlayer.create(this, R.raw.uclick);
		player.start();

		overridePendingTransition(R.anim.push_out_to_left,R.anim.hold);
		setContentView(R.layout.settings);

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

		final CheckBox chPass=(CheckBox) findViewById(R.id.chboxchangePass);
		chPass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EditText txtPass=(EditText) findViewById(R.id.txtPass);
				if (chPass.isChecked()) {
					txtPass.setEnabled(true);
					txtPass.setBackgroundDrawable(getResources().getDrawable(R.drawable.medit_box));
				}
				else
				{
					txtPass.setEnabled(false);
					txtPass.setBackgroundDrawable(getResources().getDrawable(R.drawable.medit_box_gray));
				}

			}
		});

		Button btnSaveSettings=(Button) findViewById(R.id.btnSaveSettings);
		btnSaveSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dbsource=new dbAdapter(Settings.this);
				dbsource.open();

				List<classEmail> users=null;
				users=dbsource.findAllEmails();
				boolean found=false;
				if (!users.isEmpty()) {
					for (int i = 0; i < users.size(); i++) {
						String tmpID=users.get(i).getEname();
						if (tmpID.equals("user")) {
							found=true;
						}
					}
				}

				EditText pw=(EditText) findViewById(R.id.txtPass);
				Log.e("after 4 loop", String.valueOf(found));

				String pass=pw.getText().toString();
				pass.replaceAll(" ", "");
				CheckBox chChangePass=(CheckBox) findViewById(R.id.chboxchangePass);

				//user selects show login page
				CheckBox chShowLoginPage=(CheckBox) findViewById(R.id.chboxShowLogin);
				int flg;
				if(chShowLoginPage.isChecked())
				{
					flg=1;
					//set email table for safe login
					dbsource.UpdateEmailAsUserTbl("safe");
				}
				else
				{
					flg=0;
					//set email table for unsafe login
					dbsource.UpdateEmailAsUserTbl("unsafe");
				}
				
				//first check for change option selection by user
				if (chChangePass.isChecked()) {

					//then check if pass field is not empty
					if (pass.equals("")) {

						Toast.makeText(Settings.this, "در فیلد رمز مقداری وارد نکرده اید لذا در شروع برنامه صفحه درخواست رمز را مشاهده نخواهید کرد", Toast.LENGTH_LONG).show();
						//set email table for unsafe login
						dbsource.UpdateEmailAsUserTbl("unsafe");	
					} else {



						if (found) {

							dbsource.UpdateEmailSetPass(pass);
						}
						else
						{

							classEmail MLogins=new classEmail();
							MLogins.setEmail(pass);
							MLogins.setEname("user");

							dbsource.insertEmail(MLogins);

						}
					}

				}

			
				 

				Toast.makeText(Settings.this, "تنظیمات مورد نظر کاربر اعمال گردید", Toast.LENGTH_LONG).show();

			}
		});









		super.onCreate(savedInstanceState);
	}

}
