package ir.bejani;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import com.ghasemkiani.util.SimplePersianCalendar;

import java.util.List;

import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;

@SuppressLint("DefaultLocale") 
public class pmxrWidget extends AppWidgetProvider {
	private List<classCredits> credits=null;
	ArrayAdapter<classCredits> adapter=null;
	dbAdapter dbsource;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context,pmxrWidget.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		dbsource=new dbAdapter(context);
		dbsource.open();
		credits=dbsource.findAllCredits();
		adapter = new ArrayAdapter<classCredits>(context,R.layout.row,R.id.secondLine, credits);
		
		for (int widgetId : allWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.pmxr_widget_layout);
			
			String total="";
			String totalmonth="";
			String totalyear="";
			
			int p=0;
					//p=showCrdt.showCredit();
			//calculate todays date
			SimplePersianCalendar prsCalendar=new SimplePersianCalendar();
			int year=prsCalendar.getDateFields().getYear();
			int month=prsCalendar.getDateFields().getMonth();
			month=month+1;
			int Cyear=0,Cmonth=0,sum_month=0,sum_year=0;
			for (int i = 0; i < adapter.getCount(); i++) {
				
				Cyear=credits.get(i).getYear();
				Cmonth=credits.get(i).getMonth();
				
				//calculate total current month
				if((Cmonth==month)&&(Cyear==year))
					sum_month+=credits.get(i).getCredit();
				
				//calculate total current year
				if(Cyear==year)
					sum_year+=credits.get(i).getCredit();
				
				//calculate total credits.
				p=p+credits.get(i).getCredit();
			}								
			 			
			//dc.format(Long.parseLong(String.valueOf(p)));
			total=String.valueOf(p);
			total=String.format("%,d",p);
			
			totalmonth=String.format("%,d",sum_month);
			
			totalyear=String.format("%,d",sum_year);
			
			remoteViews.setTextViewText(R.id.txtWidgetTotalHolder,total );
			remoteViews.setTextViewText(R.id.txtWidgetTolalYearHolder, totalyear);
			remoteViews.setTextViewText(R.id.txtWidgetTolalMonthHolder, totalmonth);
			
			
			Intent pi = new Intent(context, insertCredit.class);
			PendingIntent cpi = PendingIntent.getActivity(context, 0, pi, 0);			    			    
			remoteViews.setOnClickPendingIntent(R.id.imgViewWidgetInsertAct, cpi);
			    
			Intent pi2 = new Intent(context, showActRecyc.class);
			PendingIntent cpi2 = PendingIntent.getActivity(context, 0, pi2, 0);			    			    
			remoteViews.setOnClickPendingIntent(R.id.imgViewWidgetshowAct, cpi2);
			

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
			super.onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
}
