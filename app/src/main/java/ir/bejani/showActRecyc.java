package ir.bejani;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghasemkiani.util.SimplePersianCalendar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ir.bejani.db.classBill;
import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import ir.bejani.recyc.CrdtListAdapter;
import ir.bejani.recyc.clsCreditView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class showActRecyc extends AppCompatActivity {

    public int mProgressstatus = 0;
    private ProgressDialog progress;

    RecyclerView recyclerView;
    CrdtListAdapter mAdapter;
    dbAdapter dbsource;
    private List<clsCreditView> CrdtList = new ArrayList<>();

    private List<classCredits> credits = null;
    private List<classCats> cats = null;

    List<String[]> tmpCreditss = null;
    ArrayAdapter<String> customAdapter = null;

    private static final String LOGTAG = "activity";
    private static final int DIALOG_Excel_Path = 10, DIALOG_SHOW_ALL_CREDITS = 20;

    private String[] mFileList, MFileList = null;
    List<String> brwList = new ArrayList<String>();
    List<String> FList = null, MFList = null;

    private File mPath = null;
    private String mChosenFile;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void gotoMainAct(View v) {

        Intent intent = new Intent(showActRecyc.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }


    public int getToday() {
        //calculate todays date
        SimplePersianCalendar prsCalendar = new SimplePersianCalendar();
        int year = prsCalendar.getDateFields().getYear();
        int month = prsCalendar.getDateFields().getMonth();
        month = month + 1;
        int day = prsCalendar.getDateFields().getDay();

        return year * 10000 + month * 100 + day;
    }

    public void refreshList(String selection) {

        dbsource = new dbAdapter(this);
        dbsource.open();

        List<String> tmpCredits = new ArrayList<String>();
        tmpCreditss = new ArrayList<String[]>();

        if (selection == "all") {
            credits = dbsource.findAllCredits();
        } else {
            credits = dbsource.findTodayCredits("insdate=" + String.valueOf(getToday()));
        }
        if (credits.isEmpty() == true) {
            showToast(R.string.str_emptyTodayList);
        } else {


            int p = 0, i;
            List<classBill> tmpBill, tbill = new ArrayList<classBill>();
            //			classBill tBill=new classBill();

            for (i = 0; i < credits.size(); i++) {
                p = p + credits.get(i).getCredit();
                cats = dbsource.findFilteredCats("catID =" + credits.get(i).getCreditCat());
                String tmpCatName = "";
                if (cats.isEmpty() == false) {
                    tmpCatName = cats.get(0).getName();
                }
                String cList = "";
//                DecimalFormat dc = new DecimalFormat("#########");
                DecimalFormat dc = new DecimalFormat("###,###,###");

                //if flag=0 the credit is normal else is qabz
                if (credits.get(i).getFlag() == 0) {
                    cList = String.valueOf(i + 1) + "- مبلغ " +

                            dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit()))) +
                            " بابت " + credits.get(i).getCreditName() +
                            " -- [" + String.valueOf(credits.get(i).getYear()) + "/" +
                            String.valueOf(credits.get(i).getMonth()) + "/" +
                            String.valueOf(credits.get(i).getDay()) + "] - (  " +
                            tmpCatName + " )";
                    String clists[] = {String.valueOf(i + 1), String.valueOf(credits.get(i).getCredit()),
                            credits.get(i).getCreditName(), String.valueOf(credits.get(i).getInsDate()),
                            tmpCatName};
                    tmpCreditss.add(clists);


                } else {
                    tmpBill = dbsource.findFilteredBills("btid=" + credits.get(i).getFlag());
                    cList = String.valueOf(i + 1) + "- مبلغ " +

                            dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit()))) +
                            " بابت " + credits.get(i).getCreditName() +
                            " -- [" + String.valueOf(credits.get(i).getYear()) + "/" +
                            String.valueOf(credits.get(i).getMonth()) + "/" +
                            String.valueOf(credits.get(i).getDay()) + "] - (  " +
                            "ش.ق" + tmpBill.get(0).getBillID() + ".." + "ش.پ" + tmpBill.get(0).getPayID()

                            + " )";

                    tbill = dbsource.findFilteredBills("btid=" + credits.get(i).getFlag());
                    String clists[] = {String.valueOf(i + 1), String.valueOf(credits.get(i).getCredit()),
                            credits.get(i).getCreditName(), String.valueOf(credits.get(i).getInsDate())
                            , "قبض", tbill.get(0).getBillID(), tbill.get(0).getPayID(), String.valueOf(tbill.get(0).getBillStartDate()), String.valueOf(tbill.get(0).getBillStopDate())};
                    tmpCreditss.add(clists);

                }


                tmpCredits.add(cList);


            }
            customAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.secondLine, tmpCredits);
            //setListAdapter(customAdapter);
            TextView total = (TextView) findViewById(R.id.totals);
            //blind people format
//            DecimalFormat dc = new DecimalFormat("#########");

            //normal format
            DecimalFormat dc = new DecimalFormat("###,###,###");
            total.setText("جمع ت کل " + dc.format(Integer.parseInt(String.valueOf(p))) + " تومان");

        }
        dbsource.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MediaPlayer player = new MediaPlayer();
        player = MediaPlayer.create(this, R.raw.uclick);
        player.start();

        overridePendingTransition(R.anim.push_out_to_left, R.anim.hold);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_recyc);

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

        //refreshList("some");
        recyclerView =  findViewById(R.id.recycler_view);
        mAdapter = new CrdtListAdapter(getApplicationContext(), CrdtList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(mAdapter);

        prepareData("some");

        progress = new ProgressDialog(this);

    }

    private void prepareData(String selection) {
        dbsource = new dbAdapter(this);

        clsCreditView CREDIT;

        dbsource.open();

        List<String> tmpCredits = new ArrayList<String>();
        tmpCreditss = new ArrayList<String[]>();

        if (selection == "all") {
            credits = dbsource.findAllCredits();
        }
        else {
            credits = dbsource.findTodayCredits("insdate=" + String.valueOf(getToday()));
        }
        if (credits.isEmpty() == true) {
            showToast(R.string.str_emptyTodayList);
        }
        else
        {

            int p = 0, i;
            List<classBill> tbill = new ArrayList<classBill>();

            for (i = 0; i < credits.size(); i++) {

                p = p + credits.get(i).getCredit();
                cats = dbsource.findFilteredCats("catID =" + credits.get(i).getCreditCat());
                String tmpCatName = "";
                if (cats.isEmpty() == false) {
                    tmpCatName = cats.get(0).getName();
                }

                //blind people format
                DecimalFormat dc = new DecimalFormat("###,###,###");

                //normal format
                //DecimalFormat dc = new DecimalFormat("###,###,###");

                //if flag=0 the credit is normal else is qabz
                if (credits.get(i).getFlag() == 0) {

                    String clists[] = {String.valueOf(i + 1), String.valueOf(credits.get(i).getCredit()),
                            credits.get(i).getCreditName(), String.valueOf(credits.get(i).getInsDate()),
                            tmpCatName};
                    tmpCreditss.add(clists);


                } else {

                    tbill = dbsource.findFilteredBills("btid=" + credits.get(i).getFlag());
                    String clists[] = {String.valueOf(i + 1), String.valueOf(credits.get(i).getCredit()),
                            credits.get(i).getCreditName(), String.valueOf(credits.get(i).getInsDate())
                            , "قبض", tbill.get(0).getBillID(), tbill.get(0).getPayID(), String.valueOf(tbill.get(0).getBillStartDate()), String.valueOf(tbill.get(0).getBillStopDate())};
                    tmpCreditss.add(clists);

                }

                long tmpCrdtID = credits.get(i).getId();
//                String tmpCrdt = dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())));
                String tmpCrdt = dc.format(credits.get(i).getCredit());
                String tmpCrdtName = String.valueOf(i + 1) + ". " + credits.get(i).getCreditName();
                String tmpDate=String.valueOf(credits.get(i).getInsDate());
                String tmpCrdtYear = String.valueOf(credits.get(i).getYear());
                String tmpCrdtMOnth = String.valueOf(credits.get(i).getMonth());
                String tmpCrdtDay = String.valueOf(credits.get(i).getDay());

                String tmpCrdtDate =tmpDate;
                //String tmpCrdtDate =Y+"/"+M+"/"+D;

                // for bills there is no cat so it is init as QABZ
                if (tmpCatName.equals(""))
                    tmpCatName = "قبض";
                String tmpCrdtGrp = tmpCatName;

                CREDIT = new clsCreditView(String.valueOf(tmpCrdtID),tmpCrdt, tmpCrdtName, tmpCrdtDate,tmpCrdtYear,tmpCrdtMOnth,tmpCrdtDay, tmpCrdtGrp,String.valueOf(credits.get(i).getFlag()));

                CrdtList.add(CREDIT);
            }
            //customAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.secondLine,tmpCredits);
            //setListAdapter(customAdapter);
            TextView total = (TextView) findViewById(R.id.totals);

            //blind people format
            DecimalFormat dc = new DecimalFormat("###,###,###");

            //normal formt
            //DecimalFormat dc = new DecimalFormat("###,###,###");
            total.setText("جمع کل " + dc.format(Integer.parseInt(String.valueOf(p))) + " تومان");

        }
        dbsource.close();
        // mAdapter.notify();
        mAdapter.notifyDataSetChanged();

    }

    public void showToast(int msg) {
        LayoutInflater inflator = getLayoutInflater();
        View layout = inflator.inflate(R.layout.toast_tmp_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
        TextView text = (TextView) layout.findViewById(R.id.vtoast);

        text.setText(msg);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showToast(int msg, int toast_layout) {
        LayoutInflater inflator = getLayoutInflater();

        View layout = inflator.inflate(toast_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
        TextView text = (TextView) layout.findViewById(R.id.vtoast);

        text.setText(msg);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_Excel_Path:

                String tmpPath = "";
                if (!brwList.isEmpty()) {

                    for (int i = 0; i < brwList.size(); i++) {
                        tmpPath = tmpPath + "/" + brwList.get(i).toString();
                        Log.i(LOGTAG, "@@@ " + tmpPath);
                    }
                }
                //mPath=new File(Environment.getExternalStorageDirectory().toString()+"/"+tmpPath);
                mPath = this.getExternalFilesDir(Environment.getRootDirectory().toString() + "/" + tmpPath);


                mFileList = mPath.list();
                if (!mFileList.equals(null)) {

                    FList = new ArrayList<String>(Arrays.asList(mFileList));
                    MFList = new ArrayList<String>();

                    if (tmpPath.equals("")) {
                        for (int i = 0; i < FList.size(); i++) {
                            File DirTest = new File(mPath.toString() + "/" + FList.get(i));

                            if (DirTest.isDirectory())
                                MFList.add("+ " + FList.get(i));
                            else
                                MFList.add(FList.get(i));
                        }
                    } else {
                        MFList.add("." + mPath.toString());
                        MFList.add("..");
                        for (int i = 0; i < FList.size(); i++) {
                            File DirTest = new File(mPath.toString() + "/" + FList.get(i));

                            if (DirTest.isDirectory()) {
                                MFList.add("+ " + FList.get(i));
                            } else {
                                MFList.add(FList.get(i));
                            }
                        }
                    }
                    Log.i(LOGTAG, "FList is ==> " + FList.toString());
                    Log.i(LOGTAG, "MFList is ==> " + MFList.toString());

                    MFileList = MFList.toArray(new String[MFList.size()]);

                    AlertDialog.Builder alb = new AlertDialog.Builder(this);
                    alb.setTitle("مسیر فایل اکسل را انتخاب کنید");

                    alb.setItems(MFileList, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int which) {
                            //user selects the path for excel file being saved
                            mChosenFile = MFileList[which];
                            if (mChosenFile.contains("+")) {
                                mChosenFile = mChosenFile.substring(2, mChosenFile.length());
                            }
                            File tpath = new File(mPath.toString() + "/" + mChosenFile);
                            if (tpath.isDirectory()) {

                                if (mChosenFile.equals("..")) {
                                    brwList = brwList.subList(0, brwList.size() - 1);
                                    Log.i(LOGTAG, brwList.toString());
                                } else if (!mChosenFile.contains("/")) {

                                    brwList.add(mChosenFile);
                                }

                            } else if (!tpath.toString().contains("./")) {
                                showToast(R.string.msg_selectFileError, R.layout.toast_error_layout);
                            }

                            showDialog(DIALOG_Excel_Path);
                        }

                    });

                    alb.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                        // user confrims the selected path for excel file being saved there
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            asyncTaskXSL MAT = new asyncTaskXSL();
                            MAT.execute("mahmud bejani");
                            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    Toast.makeText(getApplicationContext(), "عملیات توسط کاربر لغو گردید", Toast.LENGTH_LONG).show();

                                }
                            });


                        }
                    });
                    alb.create();

                    alb.show();
                    //brwList.remove(arg0);
                    brwList.removeAll(Collections.singleton(null));

                }

                break;
            case DIALOG_SHOW_ALL_CREDITS:
                AlertDialog.Builder alb = new AlertDialog.Builder(this);
                alb.setTitle("آیا می خواهید تمام هزینه های ثبت شده بارگذاری شود؟ این کار ممکن است زمان زیادی طول بکشد باید تا اتمام کامل بارگذاری از پایگاه داده ها صبور باشد");
                alb.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "عملیات توسط کاربر لغو شد", Toast.LENGTH_LONG).show();
                    }
                });
                alb.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    // user confrims the selected path for excel file being saved there
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        asyncTaskShowAllCrdts MAT = new asyncTaskShowAllCrdts();
                        MAT.execute("mahmud bejani");


                        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface arg0) {

                                Toast.makeText(getApplicationContext(), "عملیات توسط کاربر لغو گردید", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });
                alb.create();

                alb.show();
        }
        return super.onCreateDialog(id);
    }

    public void upXSL() {
        AlertDialog alertDialog = new AlertDialog.Builder(showActRecyc.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("ذخیره سازی موفقیت آمیز فایل اکسل");

        // Setting Dialog Message
        alertDialog.setMessage("گزارش لیست هزینه های انتخاب شده در مسیر مورد نظر ذخیره شد . آیا می خواهید نسخه ای گزارش به ایمیلتان نیز ارسال گردد؟ ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_upload_report);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "عملیات ارسال فایل به ایمیل لغو شد", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (tmpCreditss.isEmpty()) {
                    showToast(R.string.str_emptyList);
                } else {
                    Intent intent = new Intent(showActRecyc.this, sendReporttoEmail.class);
                   // String str_path = mPath.toString() + "/" + getString(R.string.app_name) + ".xls";
                    String str_path = mPath.toString() + "/" +  "anmxr.xls";
                    Log.i("sent file is", str_path);
                    intent.putExtra("sentFile", str_path);
                    startActivity(intent);
                }
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "عملیات ارسال فایل به ایمیل لغو شد", Toast.LENGTH_SHORT).show();

                refreshList("all");

            }
        });
        alertDialog.show();

    }

    protected void onResume() {
        super.onResume();
        dbsource.open();

    }

    @Override
    protected void onPause() {
        super.onPause();
        dbsource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_show_act, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.mnuCurMonthReport:
                Log.d("....", "curmonth");
                intent = new Intent(showActRecyc.this, showCurMonth.class);
                startActivity(intent);
                //			this.finish();
                break;

            case R.id.mnuMonthReport:
                intent = new Intent(showActRecyc.this, CustomMonthRecyc.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuCatsReport:
                Log.d("....", "cats report");

                intent = new Intent(showActRecyc.this, showCatsReport.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuReportSomeDate:
                intent = new Intent(showActRecyc.this, showSomeDateReport.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuAnualReport:

                intent = new Intent(showActRecyc.this, yearReportChart.class);
                startActivity(intent);
                break;

            case R.id.mnuUploadReport:

                //uploadExcelReport();
                if (tmpCreditss.isEmpty()) {
                    showToast(R.string.str_emptyList);
                } else {
                    showDialog(DIALOG_Excel_Path);
                }
                break;
            case R.id.mnuShowAllCredits:
                showDialog(DIALOG_SHOW_ALL_CREDITS);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        progress.dismiss();
        super.onDestroy();
    }

    //########################################################################################################################
    //Async task
    public class asyncTaskXSL extends AsyncTask<String, Integer, String> {
        public int p = 0;

        @Override
        protected String doInBackground(String... arg0) {

            publishProgress(0);


            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFSheet firstSheet = workbook.createSheet("گزارش هزینه های روزانه");

            HSSFRow row1 = firstSheet.createRow(0);

            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(new HSSFRichTextString("ردیف"));


            //writer.writeAll(tmpCreditss);

            HSSFCell cell2 = row1.createCell(1);
            cell2.setCellValue(new HSSFRichTextString("مبلغ"));
            HSSFCell cell3 = row1.createCell(2);
            cell3.setCellValue(new HSSFRichTextString("عنوان هزینه"));
            HSSFCell cell4 = row1.createCell(3);
            cell4.setCellValue(new HSSFRichTextString("تاریخ"));
            HSSFCell cell5 = row1.createCell(4);
            cell5.setCellValue(new HSSFRichTextString("گروه هزینه ای"));
            HSSFCell cell6 = row1.createCell(5);
            cell6.setCellValue(new HSSFRichTextString("شناسه قبض"));
            HSSFCell cell7 = row1.createCell(6);
            cell7.setCellValue(new HSSFRichTextString("شناسه پرداخت"));
            HSSFCell cell8 = row1.createCell(7);
            cell8.setCellValue(new HSSFRichTextString("از تاریخ"));
            HSSFCell cell9 = row1.createCell(8);
            cell9.setCellValue(new HSSFRichTextString("تا تاریخ"));

            int j = 0;
            for (int i = 0; i < tmpCreditss.size(); i++) {


                int interval = tmpCreditss.size() / 100;
                //Log.d("interval", String.valueOf(interval));
                if (interval > 0) {

                    if (i % interval == 0)
                        j++;
                } else {
                    j += 3;

                }
                publishProgress(j);

                HSSFRow rowA = firstSheet.createRow(i + 1);

                HSSFCell cellA = rowA.createCell(0);
                cellA.setCellValue(new HSSFRichTextString(String.valueOf(i + 1)));
                HSSFCell cellB = rowA.createCell(1);
                cellB.setCellValue(new HSSFRichTextString(String.valueOf(tmpCreditss.get(i)[1])));
                HSSFCell cellC = rowA.createCell(2);
                cellC.setCellValue(new HSSFRichTextString(String.valueOf(tmpCreditss.get(i)[2])));
                HSSFCell cellD = rowA.createCell(3);
                cellD.setCellValue(new HSSFRichTextString(String.valueOf(tmpCreditss.get(i)[3])));
                HSSFCell cellE = rowA.createCell(4);
                cellE.setCellValue(new HSSFRichTextString(String.valueOf(tmpCreditss.get(i)[4])));
                //HSSFCell cellF = rowA.createCell(5);
                //cellF.setCellValue(new HSSFRichTextString(String.valueOf(tmpCreditss.get(i)[5])));
                //HSSFCell cellG = rowA.createCell(6);
                //cellG.setCellValue(new HSSFRichTextString(String.valueOf(tmpCreditss.get(i)[6])));

            }


            FileOutputStream fos = null;
            try {

                String str_path = mPath.toString();
                File file;
                file = new File(str_path, "anmxr.xls");
                fos = new FileOutputStream(file);


                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                        //workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(Reports.this, "گزارش اکسل مورد نظر در محل انتخاب شده ذخیره گردید", Toast.LENGTH_LONG).show();

            }

            return "progress...";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            progress.dismiss();
            Toast.makeText(showActRecyc.this, "گزارش اکسل مورد نظر در محل انتخاب شده ذخیره گردید", Toast.LENGTH_LONG).show();
            upXSL();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressstatus = values[0];
            progress.setProgress(mProgressstatus);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setMessage(" لطفا صبر کنید.در حال آماده سازی و ذخیره گزارش خروجی هزینه ها");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("آماده سازی گزارش اکسل به خروجی");

            progress.show();

        }

        @Override
        protected void onCancelled(String result) {
            // TODO Auto-generated method stub
            super.onCancelled(result);
            cancel(true);
        }
    }

    public class asyncTaskShowAllCrdts extends AsyncTask<String, Integer, List<clsCreditView>> {
        public int p = 0;

        @Override
        public List<clsCreditView> doInBackground(String... arg0) {

            dbsource = new dbAdapter(showActRecyc.this);
            dbsource.open();

            List<String> tmpCredits = new ArrayList<String>();
            tmpCreditss = new ArrayList<String[]>();
            clsCreditView CREDIT;

            //calculate todays date
            SimplePersianCalendar prsCalendar = new SimplePersianCalendar();
            int year = prsCalendar.getDateFields().getYear();
            int month = prsCalendar.getDateFields().getMonth();
            month = month + 1;
            int day = prsCalendar.getDateFields().getDay();

            //Plain date calculation and insertion
            int plaindate = year * 10000 + month * 100 + day;

            credits = dbsource.findAllCredits();

            int i;
            List<classBill> tmpBill, tbill = new ArrayList<classBill>();
            classBill tBill = new classBill();

            //calc and publish progress
            int recordCount = credits.size();
            int j = 0;
            for (i = 0; i < credits.size(); i++) {
                int interval = recordCount / 100;
                if (interval > 0) {
                    if (i % interval == 0)
                        j++;
                } else {
                    j += 3;
                }
                publishProgress(j);

                //Blind people format
                DecimalFormat dc = new DecimalFormat("###,###,###");

                //Normal Format
                //DecimalFormat dc = new DecimalFormat("###,###,###");

                cats = new ArrayList<>();
                cats = dbsource.findFilteredCats("catID =" + credits.get(i).getCreditCat());
                String tmpCatName = "";
                if (!cats.isEmpty()) {
                    tmpCatName = cats.get(0).getName();
                }
                p = p + credits.get(i).getCredit();
                String cLists[] = {String.valueOf(i + 1), String.valueOf(credits.get(i).getCredit()),
                        credits.get(i).getCreditName(), String.valueOf(credits.get(i).getInsDate()), tmpCatName};
                tmpCreditss.add(cLists);
                long tmpCrdtID = credits.get(i).getId();
//                String tmpCrdt = dc.format(Integer.parseInt(String.valueOf(credits.get(i).getCredit())));
                String tmpCrdt = dc.format(credits.get(i).getCredit());
                String tmpCrdtName = String.valueOf(i + 1) + ". " + credits.get(i).getCreditName();
                String tmpCrdtDate = String.valueOf(credits.get(i).getInsDate());
                String tmpCrdtYear = String.valueOf(credits.get(i).getYear());
                String tmpCrdtMOnth = String.valueOf(credits.get(i).getMonth());
                String tmpCrdtDay = String.valueOf(credits.get(i).getDay());
                String tmpCrdtGrp = tmpCatName;

                CREDIT = new clsCreditView(String.valueOf(tmpCrdtID),tmpCrdt, tmpCrdtName, tmpCrdtDate,tmpCrdtYear,tmpCrdtMOnth,tmpCrdtDay, tmpCrdtGrp,String.valueOf(credits.get(i).getFlag()));

                CrdtList.add(CREDIT);
            }

            return CrdtList;
        }

        @Override
        protected void onPostExecute(List<clsCreditView> result) {
            super.onPostExecute(result);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            CrdtList = new ArrayList<>();
            mAdapter = new CrdtListAdapter(getApplicationContext(), result);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setAdapter(mAdapter);


            TextView total = (TextView) findViewById(R.id.totals);

            //Blind poeple format
            DecimalFormat dc = new DecimalFormat("#########");

            //Normal Format
           // DecimalFormat dc = new DecimalFormat("###,###,###");

            total.setText("جمع کل " + dc.format(Integer.parseInt(String.valueOf(p))) + " تومان");
            progress.dismiss();

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressstatus = values[0];
            progress.setProgress(mProgressstatus);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setMessage(" لطفا صبر کنید.در حال خواندن رکوردهای ذخیره شده در پایگاه داده ها");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("فراخوانی داده از پایگاه");

            progress.show();

        }

        @Override
        protected void onCancelled(List<clsCreditView> result) {
            // TODO Auto-generated method stub
            super.onCancelled(result);
            cancel(true);
            //showToast(R.string.convertMsgCancel);
        }
    }


}

