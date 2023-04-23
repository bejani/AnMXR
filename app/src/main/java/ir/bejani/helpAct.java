package ir.bejani;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class helpAct extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MediaPlayer player=new MediaPlayer();
		player=MediaPlayer.create(this, R.raw.uclick);
		player.start();

		overridePendingTransition(R.anim.push_out_to_left,R.anim.hold);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	public void gotoMainAct(View v){
		Intent intent=new Intent(helpAct.this, MainActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();

	}
}
