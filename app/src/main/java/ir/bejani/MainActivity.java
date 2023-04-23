package ir.bejani;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.bejani.db.classCats;
import ir.bejani.db.classCredits;
import ir.bejani.db.dbAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {
    private ArrayList<File> fileList = new ArrayList<File>();
    private String[] mMonth = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    // constant for identifying the dialog
    private static final int DIALOG_ALERT = 10;
    private static final int DIALOG_About = 15;
    private static final int DIALOG_InsertCredit = 105;
    private static final int DIALOG_Backup_Write = 120;
    private static final int DIALOG_Backup_Restore = 130;

    private static final String LOGTAG = "activity";

    private String[] mFileList = {""}, MFileList = null;
    List<String> brwList = new ArrayList<String>();
    List<String> FList = null, MFList = null;

    private File mPath = null;
    public String BackupDir = "";
    private String mChosenFile;

    dbAdapter dbsource = null;

    @SuppressWarnings("deprecation")
    public void quit(View view) {
        showDialog(DIALOG_ALERT);
    }

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {

        String tmpPath = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = null;
        switch (id) {

            case DIALOG_InsertCredit:

                dialog = new AlertDialog.Builder(this).create();
                // Setting Dialog Title
                dialog.setTitle("انتخاب نوع هزینه");

                // Setting Dialog Message
                dialog.setMessage("نوع عملیات ثبت هزینه را انتخاب کنید ");

                // Setting Icon to Dialog
                builder.setIcon(R.drawable.ic_dbbk_upload);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "عملیات ثبت هزینه لغو شد", Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "ثبت هزینه روزانه", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(MainActivity.this, insertCredit.class);
                        startActivity(it);

                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, " ثبت قبوض پرداختی  ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(MainActivity.this, insertBill.class);
                        startActivity(it);

                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "انصراف", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
                    }
                });
                // Showing Alert Message
                dialog.show();

                dialog.show();

                break;
            case DIALOG_ALERT:
                builder.setMessage("آیا می خواهید از برنامه خارج شوید؟");
                builder.setTitle("خروج");
                builder.setIcon(R.drawable.ic_quitmini);
                builder.setCancelable(true);
                builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        ActivityCompat.finishAffinity(MainActivity.this);

                        //MainActivity.this.finish();
                    }
                });
                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "خروج از برنامه لغو شد ", Toast.LENGTH_LONG).show();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case DIALOG_About:
                String msg = "طراح و توسعه دهنده : محمود بجانی";
                msg += "\n @mahmudbejani";
                msg += "\n تست و اعلان باگها : مهدی عزیززاده خرمی";
                msg += "\n @M_azizi_amol";
                builder.setMessage(msg);
                builder.setTitle("درباره برنامه");
                builder.setIcon(R.drawable.ic_person_black_24dp);
                builder.setCancelable(true);
                builder.setPositiveButton("بسیار خوب", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        ActivityCompat.finishAffinity(MainActivity.this);

                        //MainActivity.this.finish();
                    }
                });

                dialog = builder.create();
                dialog.show();
                break;
            case DIALOG_Backup_Write:
                ///////////////////////////////////////////////////////////////////////////////////////
                //
                //														Baqckup Write
                //
                ////////////////////////////////////////////////////////////////////////////////////////

                tmpPath = "";

                for (int i = 0; i < brwList.size(); i++) {
                    tmpPath = tmpPath + "/" + brwList.get(i).toString();
                    Log.i(LOGTAG, "@@@ " + tmpPath);
                }

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
                    alb.setTitle("مسیر فایل را برای پشتیبان گیری در آن، انتخاب کنید");

                    alb.setItems(MFileList, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int which) {
                            // now user clicked on some items in the list

                            mChosenFile = MFileList[which];
                            if (mChosenFile.contains("+")) {
                                mChosenFile = mChosenFile.substring(2, mChosenFile.length());
                            }
                            File tpath = new File(mPath.toString() + "/" + mChosenFile);
                            Log.i(LOGTAG, "tpath is " + tpath.toString());
                            if (tpath.isDirectory()) {
                                Log.i(LOGTAG, "tpath is " + tpath.toString());
                                if (mChosenFile.equals("..")) {
                                    brwList = brwList.subList(0, brwList.size() - 1);
                                    Log.i(LOGTAG, brwList.toString());
                                } else if (!mChosenFile.contains("/")) {

                                    brwList.add(mChosenFile);
                                }

                            } else if (!tpath.toString().contains("./"))
                                showToast(R.string.msg_selectFileError, R.layout.toast_error_layout);
                            //Toast.makeText(MainActivity.this, "مورد انتخابی پوشه نیست",Toast.LENGTH_LONG).show();

                            showDialog(DIALOG_Backup_Write);
                        }

                    });

                    alb.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                        // user confrims the selected path for backup creation
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Log.i(LOGTAG, "now creating backup in user selected path :" + mPath.toString());
                            BackupDir = mPath.toString();

                            try {
                                Log.i(LOGTAG, "***==>" + BackupDir);
                                File currentDB = getDatabasePath("dragon.db");
                                File bkpath = new File(BackupDir);
                                if (bkpath.canWrite()) {
                                    String dbFileName = "dragonbk.db";
                                    File bkDBFile = new File(BackupDir, dbFileName);
                                    if (!bkDBFile.exists()) {
                                        Log.e(LOGTAG, "file dragonbk created");

                                        bkDBFile.createNewFile();
                                    } else {
                                        bkDBFile.delete();
                                        bkDBFile.createNewFile();
                                    }
                                    Log.e(LOGTAG, "file dragonbk.db not created because it was existed");
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(bkDBFile).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    Log.e(LOGTAG, ">> " + String.valueOf(src.size()));
                                    Log.e(LOGTAG, ">> " + String.valueOf(dst.size()));
                                    src.close();
                                    dst.close();


                                } else {
                                    Toast.makeText(MainActivity.this, " خطای عدم وجود مجوز نوشتن فایل در پوشه انتخابی رخ داده است   ", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                Toast.makeText(MainActivity.this, "خطا" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                            showToast(R.string.msg_backupDone, R.layout.toast_tmp_layout);
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("عملیات پشتیبان گیری");

                            // Setting Dialog Message
                            alertDialog.setMessage("فایل پشتیبان در مسیر انتخاب شده ذخیره شد آیا می خواهید نسخه ای از این فایل به ایملتان نیز ارسال گردد؟ ");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.ic_dbbk_upload);

                            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface arg0) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(getApplicationContext(), "عملیات ارسال فایل به ایمیل لغو شد", Toast.LENGTH_SHORT).show();

                                }
                            });

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "بله", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(MainActivity.this, sendReporttoEmail.class);
                                    intent.putExtra("sentFile", "dbBK");
                                    intent.putExtra("bkDir", BackupDir);
                                    startActivity(intent);

                                }
                            });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "خیر", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "عملیات ارسال فایل به ایمیل لغو شد", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();
                        }
                    });
                    //alb.setView(layout);
                    dialog = alb.create();

                    dialog.show();
                    //tmpPath="";
                    //brwList=brwList.subList(0,0);
                }
                break;
            case DIALOG_Backup_Restore:
                ///////////////////////////////////////////////////////////////////////////////////////
                //
                //														Baqckup Restore
                //
                ////////////////////////////////////////////////////////////////////////////////////////

                tmpPath = "";

                for (int i = 0; i < brwList.size(); i++) {
                    tmpPath = tmpPath + "/" + brwList.get(i).toString();
                    Log.i(LOGTAG, "@@@ " + tmpPath);
                }

                mPath = this.getExternalFilesDir(Environment.getRootDirectory().toString() + "/" + tmpPath);
                // mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                Toast.makeText(this, mPath.toString(), Toast.LENGTH_LONG).show();
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

                    AlertDialog.Builder bkRestoreDia = new AlertDialog.Builder(this);
                    bkRestoreDia.setTitle("مسیر فایل را برای بازنشانی پشتیبان انتخاب کنید");

                    bkRestoreDia.setItems(MFileList, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int which) {
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
                            //Toast.makeText(MainActivity.this, "مورد انتخابی پوشه نیست",Toast.LENGTH_LONG).show();

                            showDialog(DIALOG_Backup_Restore);
                        }
                    });
                    bkRestoreDia.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                        //user looked for backup file and select its dir to restore
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Log.i(LOGTAG, "now restoring backup from selected path by user : " + mPath.toString());
                            BackupDir = mPath.toString();

                            try {
                                Log.i(LOGTAG, "***==>" + BackupDir);
                                File currentDB = getDatabasePath("dragon.db");
                                File bkpath = new File(BackupDir);

                                String dbFileName = "dragonbk.db";
                                File bkDBFile = new File(BackupDir, dbFileName);
                                if (bkDBFile.exists()) {


                                    FileChannel src = new FileInputStream(bkDBFile).getChannel();
                                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    Log.e(LOGTAG, ">> " + String.valueOf(src.size()));
                                    Log.e(LOGTAG, ">> " + String.valueOf(dst.size()));
                                    src.close();
                                    dst.close();
                                    showToast(R.string.msg_restoreDone, R.layout.toast_tmp_layout);

                                } else {
                                    showToast(R.string.msg_restoreErrorPath, R.layout.toast_error_layout);
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                Toast.makeText(MainActivity.this, "خطا" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    bkRestoreDia.setIcon(R.drawable.ic_dbbk);

                    bkRestoreDia.show();
                }
                break;
            //new case here
        }

        return super.onCreateDialog(id);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //									onCreate Main Activity
    //
    //////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);

        MediaPlayer player = new MediaPlayer();
        player = MediaPlayer.create(this, R.raw.arraw);
        player.start();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(myToolbar);

        File root = null;

        root = new File(Environment.getExternalStorageDirectory() + "/Documents/DragonDB/AnMx");
        root.mkdirs();

        File[] t = root.listFiles();

        mPath = new File(Environment.getExternalStorageState());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);

        File bkDir = new File(this.getExternalFilesDir(null).toString());
        File dbDir = new File(this.getDatabasePath("dragon.db").toString());
        File oldDB = new File("/data/data/ir.openhealth/databases/dragon.db");

        Log.i(LOGTAG, bkDir.toString());
        Log.i(LOGTAG, dbDir.toString());
        if (oldDB.exists()) {
            Log.i(LOGTAG, "exist old db");
        }
        if (oldDB.canExecute()) {
            Log.i(LOGTAG, "can read old db");

        }

        super.onCreate(savedInstanceState);
    }

    public void goto_insert(View v) {

        showDialog(DIALOG_InsertCredit);

    }

    public void goto_define(View v) {
        Intent it = new Intent(MainActivity.this, CreditGroups.class);
        startActivity(it);
        finish();
    }

    public void goto_show(View v) {
        Intent it = new Intent(MainActivity.this, showActRecyc.class);
        startActivity(it);
        finish();
    }

    public void gotoSettings(View v) {
        Intent it = new Intent(MainActivity.this, Settings.class);
        startActivity(it);
        finish();

    }

    public void goto_backup() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("عملیات پشتیبان گیری");

        // Setting Dialog Message
        alertDialog.setMessage("اگر می خواهید عملیات پشتیبان گیری انجام دهید دکمه  [بله] را زده و در ادامه مسیر فایل پشتیبان را تعیین کنید و گرنه با کلیک روی [خیر] این پنجره را ببندید.  ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_dbbk_upload);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showDialog(DIALOG_Backup_Write);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
                //refreshList();
            }
        });
        alertDialog.show();
    }

    public void transfer_data_from_freeversion() {
        dbsource = new dbAdapter(this);
        dbsource.open();

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("انتقال اطلاعات از نسخه رایگان");

        // Setting Dialog Message
        alertDialog.setMessage("آیا می خواهید تمام اطلاعات اعم از ، گروههای هزینه ای تعریف شده و همچنین لیست هزینه های وارد شده  از نسخه رایگان به این نسخه انتقال یابد؟ ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_dbbk);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "عملیات لغو شد...", Toast.LENGTH_SHORT).show();

            }
        });

        // Setting delete Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                File bkDBFile = new File("/storage/sdcard0/Pictures/", "SentData.db");
                File currentDB = new File(getExternalFilesDir(null), "TFdragon.db");
                if (bkDBFile.exists()) {
                    try {
                        if (!currentDB.exists()) {
                            Log.e(LOGTAG, "file dragonbk created");

                            currentDB.createNewFile();
                        } else {
                            currentDB.delete();
                            currentDB.createNewFile();
                        }
                        Log.e(LOGTAG, "file dragonbk.db not created because it was existed");
                        FileChannel src = new FileInputStream(bkDBFile).getChannel();
                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        Log.e(LOGTAG, ">> " + String.valueOf(src.size()));
                        Log.e(LOGTAG, ">> " + String.valueOf(dst.size()));
                        src.close();
                        dst.close();

                    } catch (Exception e) {

                    }
                    TransferCats();
                    TransferCredits();
                    showToast(R.string.msg_transfer, R.layout.toast_tmp_layout);
                } else {
                    showToast(R.string.msg_notExistTFdragon, R.layout.toast_error_layout);
                }

            }
        });
        // Setting cancel Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message

        alertDialog.show();
        //dbsource.close();

    }

    public void del_all_db() {
        dbsource = new dbAdapter(this);
        dbsource.open();
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("حذف کل اطلاعات وارد شده");

        // Setting Dialog Message
        alertDialog.setMessage("آیا می خواهید تمام اطلاعات اعم از اطلاعات قبوض ، گروههای هزینه ای تعریف شده و همچنین لیست هزینه های وارد شده  حذف گردد؟ ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_delalldb);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "عملیات لغو شد...", Toast.LENGTH_SHORT).show();

            }
        });

        // Setting delete Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                dbsource.delAllData();

                showToast(R.string.msg_allDBDeleted, R.layout.toast_tmp_layout);

            }
        });
        // Setting cancel Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message

        alertDialog.show();
        dbsource.close();

    }

    public void restoreBackup() {

        //		final File currentDB=getDatabasePath("dragon.db");
        //		final File bkDir = new File(this.getExternalFilesDir(null), "bkDB");

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("بازنشانی پایگاه پشتیبان");

        // Setting Dialog Message
        alertDialog.setMessage(" اگر می خواهید پایگاه داده فعلی با فایل پشتیبان جایگزین گردد برای ادامه مراحل گزینه [بله] را انتخاب کنید و گرنه برای خروج روی دکمه [خیر] کلیک کنید . توجه داشته باشید که با این عملیات تمام داده های فعلی برنامه پاک شده و داده های فایل پشتیبان به جای آنها کپی خواهد شد ");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_dbbk);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "عملیات لغو شد...", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting delete Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //showDialog(DITMP);
                showDialog(DIALOG_Backup_Restore);

            }
        });
        // Setting cancel Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(getApplicationContext(), "عملیات لغو شد", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showToast(int msg, int toast_layout) {
        LayoutInflater inflator = getLayoutInflater();

        View layout = inflator.inflate(toast_layout, (ViewGroup) findViewById(R.id.toastRootLayout));
        TextView text = (TextView) layout.findViewById(R.id.vtoast);

        text.setText(msg);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(this, item.getItemId(), Toast.LENGTH_LONG).show();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.mnuGotoAbout:

                showDialog(DIALOG_About);

                break;
            case R.id.mnuGotoDefine:
                Intent it = new Intent(MainActivity.this, CreditGroups.class);
                startActivity(it);
                //			this.finish();
                break;
            case R.id.mnuGotoInsert:
                intent = new Intent(MainActivity.this, insertCredit.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuGotoSearch:
                intent = new Intent(MainActivity.this, searchActRecyc.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuGotoShow:
                intent = new Intent(MainActivity.this, showActRecyc.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuGotoHelp:
                intent = new Intent(MainActivity.this, helpAct.class);
                startActivity(intent);
                //			this.finish();
                break;
            case R.id.mnuGotoSettings:
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                //			this.finish();
                break;

            case R.id.mnuBackup:
                goto_backup();
                break;

            case R.id.mnuRestore:
                restoreBackup();
                break;

            case R.id.mnuDelDB:
                del_all_db();

                break;
            case R.id.mnuTransfer:
                transfer_data_from_freeversion();

                break;

            case R.id.btnExit:
                showDialog(DIALOG_ALERT);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //newConfig.orientation=Configuration.ORIENTATION_PORTRAIT;
            Toast.makeText(this, "تغییر خودکار به حالت پرتره", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Portrait.", Toast.LENGTH_LONG).show();
        }
    }

    public void TransferCredits() {

        List<classCats> oldCats = new ArrayList<classCats>();
        oldCats = new ArrayList<classCats>();
        oldCats = dbsource.findAllCatsFromFreeVersion();
        ArrayAdapter<classCats> oldAdapter = null;
        oldAdapter = new ArrayAdapter<classCats>(MainActivity.this, R.layout.row, R.id.secondLine, oldCats);

        List<classCats> newCats = new ArrayList<classCats>();
        newCats = dbsource.findAllCats();
        ArrayAdapter<classCats> newAdapter = null;
        newAdapter = new ArrayAdapter<classCats>(MainActivity.this, R.layout.row, R.id.secondLine, newCats);

        List<classCredits> credits = new ArrayList<classCredits>();
        credits = dbsource.findAllCreditsFromFreeVersion();
        ArrayAdapter<classCredits> adapter = null;
        adapter = new ArrayAdapter<classCredits>(MainActivity.this, R.layout.row, R.id.secondLine, credits);
        classCredits tcredit = new classCredits();
        for (int i = 0; i < adapter.getCount(); i++) {
            Log.i(LOGTAG, "@");
            tcredit.setCredit(credits.get(i).getCredit());
            tcredit.setCreditName(credits.get(i).getCreditName());
            if (credits.get(i).getCreditCat() == -1) {
                tcredit.setCreditCat(-1);
            } else {
                for (int j = 0; j < oldAdapter.getCount(); j++) {

                    if (credits.get(i).getCreditCat() == oldCats.get(j).getId()) {
                        Log.i(LOGTAG, String.valueOf(credits.get(i).getCreditCat()));
                        for (int k = 0; k < newAdapter.getCount(); k++) {
                            if (oldCats.get(j).getName().equals(newCats.get(k).getName())) {
                                tcredit.setCreditCat((int) newCats.get(k).getId());
                            }
                        }
                    }
                }
            }
            tcredit.setFlag(0);
            tcredit.setInsDate(credits.get(i).getInsDate());
            tcredit.setYear(credits.get(i).getYear());
            tcredit.setMonth(credits.get(i).getMonth());
            tcredit.setDay(credits.get(i).getDay());
            tcredit = dbsource.insertCredit(tcredit);
        }
    }

    public void TransferCats() {


        List<classCats> newCats = new ArrayList<classCats>();
        newCats = dbsource.findAllCats();
        ArrayAdapter<classCats> newAdapter = null, oldAdapter = null;
        newAdapter = new ArrayAdapter<classCats>(MainActivity.this, R.layout.row, R.id.secondLine, newCats);

        List<classCats> oldCats = new ArrayList<classCats>();
        oldCats = new ArrayList<classCats>();
        oldCats = dbsource.findAllCatsFromFreeVersion();
        oldAdapter = new ArrayAdapter<classCats>(MainActivity.this, R.layout.row, R.id.secondLine, oldCats);
        classCats cat = new classCats();
        Log.i(LOGTAG, "in the transfer cats ...");
        Log.i(LOGTAG, "new cats is " + String.valueOf(newAdapter.getCount()) + " --- old count is " + String.valueOf(oldAdapter.getCount()));
        for (int i = 0; i < oldAdapter.getCount(); i++) {
            Log.i(LOGTAG, String.valueOf(i) + " " + oldCats.get(i).getName());
            int found = 0;
            for (int j = 0; j < newAdapter.getCount(); j++) {
                Log.i(LOGTAG, "new cat = " + newCats.get(j).getName() + "  & old cat =" + oldCats.get(i).getName());

                if (oldCats.get(i).getName().equals(newCats.get(j).getName())) {
                    found = 1;
                }
            }
            if (found == 0) {
                cat.setName(oldCats.get(i).getName());
                cat = dbsource.insertcat(cat);
            }
        }
        Log.i(LOGTAG, "in the transfer cats ...");
    }

    public class DrawView extends View {
        Paint paint = new Paint();

        public DrawView(Context context) {
            super(context);
            paint.setColor(Color.BLACK);
        }


        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawLine(20, 100, 600, 600, paint);
            canvas.drawLine(50, 550, 770, 0, paint);
        }
    }
}
