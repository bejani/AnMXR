package ir.bejani.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class dbAdapter {

	private static final String LOGTAG="activity";

	SQLiteOpenHelper dbOpenHelper,dbOldOpenHelper;
	SQLiteDatabase dbase,database;

	private static final String[] allCatColumns={
		classOpenHelper.COLUMN_ID,
		classOpenHelper.COLUMN_NAME
	};
	private static final String[] allMailColumns={
		classOpenHelper.COLUMN_EID,
		classOpenHelper.COLUMN_EMAIL,
		classOpenHelper.COLUMN_ENAME
	};
	private static final String[] allCreditColumns={
		classOpenHelper.COLUMN_CID,
		classOpenHelper.COLUMN_CREDIT,
		classOpenHelper.COLUMN_CREDITCAT,
		classOpenHelper.COLUMN_CREDITNAME,
		classOpenHelper.COLUMN_INSDATE,
		classOpenHelper.COLUMN_YEAR,
		classOpenHelper.COLUMN_MONTH,
		classOpenHelper.COLUMN_DAY,
		classOpenHelper.COLUMN_FLAG
	};
	private static final String[] allOldCreditColumns={
		classOpenHelper.COLUMN_CID,
		classOpenHelper.COLUMN_CREDIT,
		classOpenHelper.COLUMN_CREDITCAT,
		classOpenHelper.COLUMN_CREDITNAME,
		classOpenHelper.COLUMN_INSDATE,
		classOpenHelper.COLUMN_YEAR,
		classOpenHelper.COLUMN_MONTH,
		classOpenHelper.COLUMN_DAY,
	};
	private static final String[] allBillColumns={
		classOpenHelper.COLUMN_BTID,
		classOpenHelper.COLUMN_BID,
		classOpenHelper.COLUMN_BILLPAYABLE,
		classOpenHelper.COLUMN_BILLTYPE,
		classOpenHelper.COLUMN_BSTARTDATE,
		classOpenHelper.COLUMN_BSTOPDATE,
		classOpenHelper.COLUMN_BPAYDATE,
		classOpenHelper.COLUMN_PAYID
	};
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
	public dbAdapter(Context ctx){
		//Log.i(LOGTAG,"in the dbAdapter constructor...");
		dbOpenHelper=new classOpenHelper(ctx);

	}



	public void open() {
		Log.i(LOGTAG,"Database opened");
		//THIS COMMAND CALLS onCreate method in the classOpenHelper
		database=dbOpenHelper.getWritableDatabase();

		Log.i(LOGTAG,"+++++++++++++++++++++++++++++++++++++++++++");

	}public void openOld() {
		Log.i(LOGTAG,"Old Database opened");
		//THIS COMMAND CALLS onCreate method in the classOpenHelper
		database=dbOldOpenHelper.getWritableDatabase();
		Log.i(LOGTAG,"+++++++++++++++++++++++++++++++++++++++++++");

	}
	public void close(){
		Log.i(LOGTAG,"Database closed");
		dbOpenHelper.close();
		Log.i(LOGTAG,"----------------------------------------------");

	}

	///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// INSERT BILL
	//
	//////////////////////////////////////////////////////////////////////////////////

	public classBill insertBill(classBill bill){
		database=dbOpenHelper.getWritableDatabase();

		Log.i(LOGTAG,bill.toString());

		ContentValues values=new ContentValues();
		values.put(classOpenHelper.COLUMN_BID, bill.getBillID());
		values.put(classOpenHelper.COLUMN_PAYID, bill.getPayID());
		values.put(classOpenHelper.COLUMN_BILLTYPE, bill.getBillType());
		values.put(classOpenHelper.COLUMN_BSTARTDATE, bill.getBillStartDate());
		values.put(classOpenHelper.COLUMN_BSTOPDATE, bill.getBillStopDate());
		values.put(classOpenHelper.COLUMN_BILLPAYABLE, bill.getBillPayable());
		values.put(classOpenHelper.COLUMN_BPAYDATE, bill.getPayDate());


		//Log.i(LOGTAG,"parameter mail is :"+mail.toString());
		long insertid=database.insert(classOpenHelper.TABLE_BILLS, null, values);
		Log.i(LOGTAG,">>> insert id is "+ insertid );

		classCredits tcredit=new classCredits();

		tcredit.setCredit((int) bill.getBillPayable());
		tcredit.setCreditName(" قبض "+bill.getBillType());
		tcredit.setCreditCat(9999);
		tcredit.setInsDate(bill.getPayDate());
		String bpd=String.valueOf(bill.getPayDate());
		int bpd_year=Integer.parseInt(bpd.substring(0,4));
		int bpd_month=Integer.parseInt(bpd.substring(4,6));
		int bpd_day=Integer.parseInt(bpd.substring(6,8));
		tcredit.setYear(bpd_year);
		tcredit.setMonth(bpd_month);
		tcredit.setDay(bpd_day);
		tcredit.setFlag( insertid);
		tcredit=insertCredit(tcredit);
		database.close();
		return bill;

	}
	//////////////////////////////////////////
	public void delEmail()
	{
		database.execSQL("delete from emails ");
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// INSERT EMAIL
	//
	//////////////////////////////////////////////////////////////////////////////////

	public classEmail insertEmail(classEmail mail){

		ContentValues values=new ContentValues();
		values.put(classOpenHelper.COLUMN_EMAIL, mail.getEmail());
		String name=mail.getEname();
		if (name.isEmpty()) {	
			Log.d("//", name);
			values.put(classOpenHelper.COLUMN_ENAME, "user");
		}
		else
		{
			Log.d("==", name);
			values.put(classOpenHelper.COLUMN_ENAME, name);
		}	

		Log.i(LOGTAG,"parameter mail is :"+mail.toString());
		long insertid=database.insert(classOpenHelper.TABLE_EMAILS, null, values);
		Log.i(LOGTAG,">>> insert id is "+ insertid );
		return mail;

	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// Update EMAIL as User table set login mode
	//
	//////////////////////////////////////////////////////////////////////////////////

	public int UpdateEmailAsUserTbl( String loginmode){

		String updateQuery="update emails set email='" +loginmode + "' where ename='loginmode'" ;
		database.execSQL(updateQuery);

		return 0;

	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// Update EMAIL as User table set login mode
	//
	//////////////////////////////////////////////////////////////////////////////////

	public int UpdateEmailSetPass( String userPass){

		String updateQuery="update emails set email='" +userPass + "' where ename='user'" ;
		database.execSQL(updateQuery);

		return 0;

	}
	///////////////////// update Cats
	public int UpdateCats( String newCat,String oldCat){

		String updateQuery="update cats set catName='" +newCat + "' where catName='"+oldCat+"'" ;
		database.execSQL(updateQuery);

		return 0;

	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  INSERT CAT
	//
	//////////////////////////////////////////////////////////////////////////////////

	public classCats insertcat(classCats cat){

		ContentValues values=new ContentValues();
		values.put(classOpenHelper.COLUMN_NAME, cat.getName());
		Log.i(LOGTAG,values.toString());
		long insertid=database.insert(classOpenHelper.TABLE_CATS, null, values);
		cat.setId(insertid);
		Log.i(LOGTAG,"insert id is "+ insertid );
		return cat;

	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  DEL CREDIT
	//
	//////////////////////////////////////////////////////////////////////////////////

	public int delCredit(long id){

		database=dbOpenHelper.getWritableDatabase();

		//first delete from bills if the credits is bill
		List<classCredits> credits=new ArrayList<classCredits>();
		credits=findFilteredCredits("cid="+id);
		long tmpid=credits.get(0).getFlag();
		if(tmpid!=0)
			database.execSQL("delete from bills where btid =" + tmpid );

		//now delete from credits
		database.execSQL("delete from credits where cid =" + id );

		return 0;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  DEL MAIL
	//
	//////////////////////////////////////////////////////////////////////////////////

	public int delMail(long id){

		database=dbOpenHelper.getWritableDatabase();
		database.execSQL("delete from emails where eid =" + id );
		return 0;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  DEL CAT
	//
	//////////////////////////////////////////////////////////////////////////////////

	public int delCat(long id){

		database=dbOpenHelper.getWritableDatabase();
		database.execSQL("delete from cats where catid =" + id );
		database.execSQL("update credits set creditcat='-1' where creditcat=" + id );
		return 0;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  DEL ALL DATA
	//
	//////////////////////////////////////////////////////////////////////////////////

	public int delAllData(){

		database=dbOpenHelper.getWritableDatabase();

		database.execSQL("drop table if exists cats");
		database.execSQL(TABLE_CREATE_CATS);
		database.execSQL("insert into cats(catname) values('ãÊÝÑÞå')");

		database.execSQL("drop table if exists credits");
		database.execSQL(TABLE_CREATE_CREDIT);

		database.execSQL("drop table if exists emails");
		database.execSQL(TABLE_CREATE_EMAILS);

		database.execSQL("drop table if exists bills");
		database.execSQL(TABLE_CREATE_BILLS);

		return 0;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  UPDATE CREDIT
	//
	//////////////////////////////////////////////////////////////////////////////////

	public long updateCredit(classCredits credit, long oldID){

		Log.i(LOGTAG,"^^^"+oldID+"///"+String.valueOf(credit.getInsDate()));
		database=dbOpenHelper.getWritableDatabase();

		//database.execSQL("delete from credits where cid = "+oldID);
		String updateQuery="update credits set credit='" +credit.getCredit()+
				"', creditname='"+credit.getCreditName()+
				"', cid='"+oldID+
				"', creditcat='"+credit.getCreditCat()+
				"', insdate='"+credit.getInsDate()+
				"', year='"+credit.getYear()+
				"', month='"+credit.getMonth()+
				"', day='"+credit.getDay()+

				"' where cid="+oldID;
		database.execSQL(updateQuery);

		return 0;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// INSERT CREDIT
	//
	//////////////////////////////////////////////////////////////////////////////////

	public classCredits insertCredit(classCredits credit){
		database=dbOpenHelper.getWritableDatabase();

		Log.i(LOGTAG,credit.getCredit()+"~"+credit.getCreditName()+"~"
				+credit.getCreditCat()+"~"+credit.getInsDate()+"~"+
				credit.getYear()+"~"+credit.getMonth()+"~"+
				credit.getDay()	);

		ContentValues values=new ContentValues();
		values.put(classOpenHelper.COLUMN_CREDIT, credit.getCredit());
		values.put(classOpenHelper.COLUMN_CREDITNAME,credit.getCreditName());
		values.put(classOpenHelper.COLUMN_CREDITCAT,credit.getCreditCat());
		values.put(classOpenHelper.COLUMN_INSDATE, credit.getInsDate());
		values.put(classOpenHelper.COLUMN_YEAR, credit.getYear());
		values.put(classOpenHelper.COLUMN_MONTH, credit.getMonth());
		values.put(classOpenHelper.COLUMN_DAY, credit.getDay());
		values.put(classOpenHelper.COLUMN_FLAG, credit.getFlag());

		long insertid2=database.insert(classOpenHelper.TABLE_CREDITS, null, values);

		credit.setId(insertid2);
		Log.i(LOGTAG,"insert id is "+ insertid2 );
		return credit;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND ALL CATS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classCats> findAllCats(){
		List<classCats> cats=new ArrayList<classCats>();
		Cursor cursor=database.query(classOpenHelper.TABLE_CATS, allCatColumns, null, null, null, null, classOpenHelper.COLUMN_NAME+" ASC");
		//Log.i(LOGTAG,"Returned " + cursor.getCount()+"Cats.");
		if (cursor.getCount()>0) {
			cursorToCats(cats, cursor);
		}
		cursor.close();
		return cats;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND ALL EMAILS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classEmail> findAllEmails(){
		List<classEmail> emails=new ArrayList<classEmail>();
		Cursor cursor=database.query(classOpenHelper.TABLE_EMAILS, allMailColumns, null, null, null, null, classOpenHelper.COLUMN_EMAIL+" asc");
		//Log.i(LOGTAG,"Returned " + cursor.getCount()+" Emails.");
		if (cursor.getCount()>0) {
			cursorToEmails(emails, cursor);
		}
		cursor.close();
		return emails;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  CURSOR TO EMAILS
	//
	//////////////////////////////////////////////////////////////////////////////////

	private void cursorToEmails(List<classEmail> emails, Cursor cursor) {
		while (cursor.moveToNext()) {
			classEmail mail=new classEmail();
			mail.setId(cursor.getLong(0));
			mail.setEmail(cursor.getString(1));
			mail.setEname(cursor.getString(2));
			emails.add(mail);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  CURSOR TO CATS
	//
	//////////////////////////////////////////////////////////////////////////////////

	private void cursorToCats(List<classCats> cats, Cursor cursor) {
		while (cursor.moveToNext()) {
			classCats cat=new classCats();
			cat.setId(cursor.getLong(0));
			cat.setName(cursor.getString(1));
			cats.add(cat);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND FILTERED EMAILS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classEmail> findFilteredEmails(String selection){
		List<classEmail> mails=new ArrayList<classEmail>();
		Cursor cursor=database.query(classOpenHelper.TABLE_EMAILS, allMailColumns, selection, null, null, null, null);
		//Log.i(LOGTAG,"Returned Filtered mails" + cursor.getCount()+" rows.");
		if (cursor.getCount()>0) {
			cursorToEmails(mails, cursor);
		}
		cursor.close();
		return mails;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND FILTERED CATS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classCats> findFilteredCats(String selection){
		List<classCats> cats=new ArrayList<classCats>();
		Cursor cursor=database.query(classOpenHelper.TABLE_CATS, allCatColumns, selection, null, null, null, null);
		//Log.i(LOGTAG,"Returned Filtered cats" + cursor.getCount()+"rows.");
		if (cursor.getCount()>0) {
			cursorToCats(cats, cursor);
		}
		cursor.close();
		return cats;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// FIND ALL CREDITS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classCredits> findAllCredits(){
		List<classCredits> credits=new ArrayList<classCredits>();
		Cursor cursor=database.query(classOpenHelper.TABLE_CREDITS, allCreditColumns, null, null, null, null, classOpenHelper.COLUMN_INSDATE+" DESC,"+ classOpenHelper.COLUMN_CREDITCAT);
		//Log.i(LOGTAG,"Returned " + cursor.getCount()+"rows.!!!");

		if (cursor.getCount()>0) {
			cursorToList(credits, cursor);
		}
		cursor.close();
		return credits;
	}///////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////// FIND ALL CREDITS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classCredits> findTodayCredits(String selection){
		List<classCredits> credits=new ArrayList<classCredits>();
		Cursor cursor=database.query(classOpenHelper.TABLE_CREDITS, allCreditColumns, selection, null, null, null, classOpenHelper.COLUMN_INSDATE+" DESC,"+ classOpenHelper.COLUMN_CREDITCAT);
		//Log.i(LOGTAG,"Returned " + cursor.getCount()+"rows.!!!");

		if (cursor.getCount()>0) {
			cursorToList(credits, cursor);
		}
		cursor.close();
		//database.close();
		return credits;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	//
	//									All Credits From Free Version
	//
	//////////////////////////////////////////////////////////////////////////////////////
	public List<classCredits> findAllCreditsFromFreeVersion(){
		List<classCredits> credits=new ArrayList<classCredits>();

		//database=dbOpenHelper.getWritableDatabase();
		SQLiteDatabase db = null;
		try{
			db=SQLiteDatabase.openDatabase("/storage/sdcard0/Android/data/ir.bejani/files/TFdragon.db",null,0);

			Cursor cursor=db.query(classOpenHelper.TABLE_CREDITS, allOldCreditColumns, null, null, null, null, classOpenHelper.COLUMN_INSDATE+" DESC,"+ classOpenHelper.COLUMN_CREDITCAT);

			//Log.i(LOGTAG,"Returned " + cursor.getCount()+"rows.!!!");

			if (cursor.getCount()>0) {
				cursorToOldList(credits, cursor);
			}
			cursor.close();
		}
		catch (SQLiteException e) {
			Log.i(LOGTAG, e.toString());
		}
		//db.endTransaction();

		db.close();
		return credits;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	//
	//									All Cats From Free Version
	//
	//////////////////////////////////////////////////////////////////////////////////////
	public List<classCats> findAllCatsFromFreeVersion(){
		List<classCats> cats=new ArrayList<classCats>();

		SQLiteDatabase db = null;

		try{
			db=SQLiteDatabase.openDatabase("/storage/sdcard0/Android/data/ir.bejani/files/TFdragon.db",null,0);

			Cursor cursor=db.query(classOpenHelper.TABLE_CATS, allCatColumns, null, null, null, null, null);


			//	Log.i(LOGTAG,"Returned " + cursor.getCount()+"rows.!!!");

			if (cursor.getCount()>0) {
				cursorToCats(cats, cursor);
			}
			cursor.close();
		}
		catch (SQLiteException e) {
			// TODO: handle exception
			Log.i(LOGTAG, e.toString());
		}
		//db.endTransaction();

		db.close();
		return cats;
	}

	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  CURSOR TO LIST
	//
	//////////////////////////////////////////////////////////////////////////////////

	private void cursorToList(List<classCredits> credits, Cursor cursor) {
		while (cursor.moveToNext()) {
			classCredits credit=new classCredits();
			credit.setId(cursor.getLong(cursor.getColumnIndex("cid")));
			credit.setCredit(cursor.getInt(cursor.getColumnIndex("credit")));
			credit.setCreditCat(cursor.getInt(cursor.getColumnIndex("creditcat")));
			credit.setCreditName(cursor.getString(cursor.getColumnIndex("creditname")));
			credit.setInsDate(cursor.getInt(cursor.getColumnIndex("insdate")));
			credit.setYear(cursor.getInt(cursor.getColumnIndex("year")));
			credit.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			credit.setDay( cursor.getInt(cursor.getColumnIndex("day")));
			credit.setFlag(cursor.getInt(cursor.getColumnIndex("flag")));
			credits.add(credit);
		}
	}
	private void cursorToOldList(List<classCredits> credits, Cursor cursor) {
		while (cursor.moveToNext()) {
			classCredits credit=new classCredits();
			credit.setId(cursor.getLong(cursor.getColumnIndex("cid")));
			credit.setCredit(cursor.getInt(cursor.getColumnIndex("credit")));
			credit.setCreditCat(cursor.getInt(cursor.getColumnIndex("creditcat")));
			credit.setCreditName(cursor.getString(cursor.getColumnIndex("creditname")));
			credit.setInsDate(cursor.getInt(cursor.getColumnIndex("insdate")));
			credit.setYear(cursor.getInt(cursor.getColumnIndex("year")));
			credit.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			credit.setDay( cursor.getInt(cursor.getColumnIndex("day")));
			credits.add(credit);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND FILTERED CREDITS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classCredits> findFilteredCredits(String selection) {
		database=dbOpenHelper.getWritableDatabase();

		List<classCredits> credits=new ArrayList<classCredits>();

		Cursor cursor=database.query(classOpenHelper.TABLE_CREDITS, allCreditColumns, selection, null, null, null, classOpenHelper.COLUMN_INSDATE+" DESC,"+ classOpenHelper.COLUMN_CREDITCAT);
		//Log.i(LOGTAG,"Returned " + cursor.getCount()+"rows.!!!");

		if (cursor.getCount()>0) {
			cursorToList(credits, cursor);
		}
		cursor.close();
		return credits;
	}	
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  CURSOR TO BILLS
	//
	//////////////////////////////////////////////////////////////////////////////////

	private void cursorToBill(List<classBill> bills, Cursor cursor) {
		while (cursor.moveToNext()) {
			classBill bill=new classBill();
			bill.setbtid(cursor.getLong(cursor.getColumnIndex("btid")));
			bill.setBillID(cursor.getString(cursor.getColumnIndex("billid")));
			bill.setPayID(cursor.getString(cursor.getColumnIndex("payid")));
			bill.setBillType(cursor.getString(cursor.getColumnIndex("billtype")));
			bill.setBillStartDate(cursor.getInt(cursor.getColumnIndex("billstartdate")));
			bill.setBillStopDate(cursor.getInt(cursor.getColumnIndex("billstopdate")));
			bill.setBillPayable(cursor.getInt(cursor.getColumnIndex("billpayable")));
			bill.setPayDate(cursor.getInt(cursor.getColumnIndex("billpaydate")));

			bills.add(bill);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND FILTERED BILLS
	//
	//////////////////////////////////////////////////////////////////////////////////
	public List<classBill> findFilteredBills(String selection) {
		database=dbOpenHelper.getWritableDatabase();

		List<classBill> bills=new ArrayList<classBill>();

		Cursor cursor=database.query(classOpenHelper.TABLE_BILLS, allBillColumns, selection, null, null, null, null);
		//Log.i(LOGTAG,"Returned " + cursor.getCount()+" bill rows.!!!");

		if (cursor.getCount()>0) {
			cursorToBill(bills, cursor);
		}
		cursor.close();
		return bills;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  UPDATE BILL
	//
	//////////////////////////////////////////////////////////////////////////////////

	public long updateBill(classBill bill, long oldbtid, long creditID){

		Log.i(LOGTAG," in the updateBill function : btid is "+String.valueOf(oldbtid)+"///and credit id is "+String.valueOf(creditID));
		database=dbOpenHelper.getWritableDatabase();

		//database.execSQL("delete from credits where cid = "+oldID);
		String updateQuery="update bills set billid='" +bill.getBillID()+
				"', payid='"+bill.getPayID()+
				"', billtype='"+bill.getBillType()+
				"', billstartdate='"+bill.getBillStartDate()+
				"', billstopdate='"+bill.getBillStopDate()+
				"', billpayable='"+bill.getBillPayable()+
				"', billpaydate='"+bill.getPayDate()+

				"' where btid="+oldbtid;
		database.execSQL(updateQuery);

		classCredits tcredit=new classCredits();
		tcredit.setCredit((int) bill.getBillPayable());
		tcredit.setCreditName(" قبض "+bill.getBillType());
		tcredit.setCreditCat(9999);
		tcredit.setInsDate(bill.getPayDate());
		String bpd=String.valueOf(bill.getPayDate());
		int bpd_year=Integer.parseInt(bpd.substring(0,4));
		int bpd_month=Integer.parseInt(bpd.substring(4,6));
		int bpd_day=Integer.parseInt(bpd.substring(6,8));
		tcredit.setYear(bpd_year);
		tcredit.setMonth(bpd_month);
		tcredit.setDay(bpd_day);
		tcredit.setFlag( oldbtid);
		updateCredit(tcredit, creditID);
		return 0;
	}
	///////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////  FIND SOME DATE CREDITS
	//
	//////////////////////////////////////////////////////////////////////////////////

	public List<classCredits> findSomeDateCredits(String sp_query) {
		dbOpenHelper.getWritableDatabase();

		List<classCredits> credits=new ArrayList<classCredits>();
		Cursor cursor=database.rawQuery(sp_query, null);
		//		//Cursor cursor=database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
		//				Log.i(LOGTAG,"Returned " + cursor.getCount()+"rows.!!!");
		//
		if (cursor.getCount()>0) {
			cursorToList(credits, cursor);
		}
		cursor.close();

		return credits;
	}

}
