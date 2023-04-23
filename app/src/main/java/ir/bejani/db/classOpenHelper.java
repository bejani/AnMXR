package ir.bejani.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class classOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG="activity";


	private static final String DATABASE_NAME="dragon.db";
	private static final  int DATABASE_VERSION = 12;

	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  TABLE : BILLS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public static final String TABLE_BILLS="BILLS";

	public static final String COLUMN_BTID="btid";
	public static final String COLUMN_BID="billid";
	public static final String COLUMN_PAYID="payid";
	public static final String COLUMN_BILLTYPE="billtype";
	public static final String COLUMN_BSTARTDATE="billstartdate";
	public static final String COLUMN_BSTOPDATE="billstopdate";
	public static final String COLUMN_BILLPAYABLE="billpayable";
	public static final String COLUMN_BPAYDATE="billpaydate";

	private static final String TABLE_CREATE_BILLS=
			"CREATE TABLE " + TABLE_BILLS + " ("+
					COLUMN_BTID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
					COLUMN_BID + " TEXT," +
					COLUMN_PAYID + " TEXT," + 	
					COLUMN_BILLTYPE + " TEXT," + 	
					COLUMN_BSTARTDATE + " integer," + 	
					COLUMN_BSTOPDATE + " integer," + 	
					COLUMN_BILLPAYABLE + " integer," + 	
					COLUMN_BPAYDATE + " integer" + 	" )";
	
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  TABLE : EMAILS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public static final String TABLE_EMAILS="EMAILS";
	public static final String COLUMN_EID="eid";
	public static final String COLUMN_EMAIL="email";
	public static final String COLUMN_ENAME="ename";

	private static final String TABLE_CREATE_EMAILS=
			"CREATE TABLE " + TABLE_EMAILS + " ("+
					COLUMN_EID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
					COLUMN_EMAIL + " TEXT," +
					COLUMN_ENAME + " TEXT" + 	" )";

	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  TABLE : CATEGORIES
	//
	//////////////////////////////////////////////////////////////////////////////////

	public static final String TABLE_CATS="cats";
	public static final String COLUMN_ID="catID";
	public static final String COLUMN_NAME="catName";

	private static final String TABLE_CREATE_CATS=
			"CREATE TABLE " + TABLE_CATS + " ("+
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
					COLUMN_NAME + " TEXT" + 	" )";

	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  TABLE : CREDITS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public static final String TABLE_CREDITS="credits";
	public static final String COLUMN_CID="cid";
	public static final String COLUMN_CREDIT="credit";
	public static final String COLUMN_CREDITNAME="creditname";
	public static final String COLUMN_CREDITCAT="creditcat";
	public static final String COLUMN_INSDATE="insdate";
	public static final String COLUMN_YEAR="year";
	public static final String COLUMN_MONTH="month";
	public static final String COLUMN_DAY="day";
	public static final String COLUMN_FLAG="flag";

	private static final String TABLE_CREATE_CREDIT=
			"CREATE TABLE  " + TABLE_CREDITS + " ("+
					COLUMN_CID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
					COLUMN_CREDIT + " INTEGER, " +
					COLUMN_CREDITNAME + " TEXT, " +
					COLUMN_CREDITCAT + " INTEGER,"+
					COLUMN_INSDATE + " INTEGER,"+
					COLUMN_YEAR + " INTEGER,"+
					COLUMN_MONTH + " INTEGER,"+
					COLUMN_DAY + " INTEGER,"+
					COLUMN_FLAG + " INTEGER"+
					" )";

	public classOpenHelper(Context context) {

		super(context, DATABASE_NAME,null , DATABASE_VERSION);
		//super(context, OLD_DATABASE_NAME,null , OLD_DATABASE_VERSION);
		//Log.i(LOGTAG,"constructor of class OpenHelper...............");

	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i(LOGTAG,"-------------called oncreate---------------");

		db.execSQL(TABLE_CREATE_CATS);
		db.execSQL("insert into cats(catname) values('متفرقه')");

		db.execSQL(TABLE_CREATE_BILLS);
		db.execSQL(TABLE_CREATE_EMAILS);
		db.execSQL(TABLE_CREATE_CREDIT);
		Log.i(LOGTAG,"Table has been created.");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int newversion, int oldversion) {

		Log.i(LOGTAG,"--------------called onupgrade------------------");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDITS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
		onCreate(db);
	}

}
