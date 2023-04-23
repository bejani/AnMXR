package ir.bejani.db;


import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ir.bejani.R;

public class clsShowToast  extends Activity{
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
	public void showToastLong(int msg) {
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
