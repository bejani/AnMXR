package ir.bejani.recyc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.bejani.R;
import ir.bejani.MainActivity;
import ir.bejani.db.dbAdapter;
import ir.bejani.editBill;
import ir.bejani.editCredit;

public class CrdtListAdapter extends RecyclerView.Adapter<CrdtListAdapter.MyViewHolder> {

    private List<clsCreditView> CrdtList;
    public Context c;
    Dialog myDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView credit, crdtname, crdtdate, crdtgrp;
        ItemClickListener itemClickListener;
        private LinearLayout item_credit;

        public MyViewHolder(View view) {
            super(view);
            item_credit = (LinearLayout) view.findViewById(R.id.itemCredit);
            credit = (TextView) view.findViewById(R.id.CreditTxt);
            crdtname = (TextView) view.findViewById(R.id.CreditnameTxt);
            crdtdate = (TextView) view.findViewById(R.id.CreditdateTxt);
            crdtgrp = (TextView) view.findViewById(R.id.CreditgrpTxt);

            Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/titr.ttf");
            crdtname.setTypeface(tf);
            tf = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/nazaninb.ttf");
            crdtgrp.setTypeface(tf);
            tf = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/iran_reg.ttf");
            credit.setTypeface(tf);
            //view.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

    }

    public CrdtListAdapter(Context ctx, List<clsCreditView> mCrdtList) {
        this.c = ctx;
        this.CrdtList = mCrdtList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyc_list_row_brief, parent, false);

        final MyViewHolder vHolder = new MyViewHolder(itemView);
        vHolder.item_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final clsCreditView Crdt = CrdtList.get(vHolder.getAdapterPosition());

                openOpDia(Crdt.getCid(), parent.getContext(), vHolder.getAdapterPosition(), Crdt.getCrdtFlag());

            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final clsCreditView Crdt = CrdtList.get(position);

        //for blind people remove 1000 separator
        String tmpCredit=Crdt.getCredit();

        // this option is for blind users
//        holder.credit.setText(tmpCredit.replace(",",""));
        holder.credit.setText(tmpCredit);
        holder.crdtname.setText(Crdt.getCrdtName());
        holder.crdtgrp.setText(Crdt.getCrdtGrp());
        String tmpDate = Crdt.getCrdtDate();
        /*String Y = tmpDate.substring(0, 4);
        String M = tmpDate.substring(4, 6);
        String D = tmpDate.substring(6, 8);
        */
        String Y = Crdt.getCrdtYear();
        String M = Crdt.getCrdtMonth();
        String D = Crdt.getCrdtDay();

        String tmpCrdtDate = Y + "/" + M + "/" + D;
        holder.crdtdate.setText(tmpCrdtDate);

        if ((position % 2) == 0) {
            holder.itemView.setBackgroundColor(Color.argb(255, 255, 201, 14));
            //holder.yourCardView.setCardBackgroundColor(Color.GREY);
        } else {
            holder.itemView.setBackgroundColor(Color.argb(255, 239, 228, 176));

            // holder.yourCardView.setCardBackgroundColor(Color.RED);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.i("POS", String.valueOf(position));
                Log.i("Credit ID :", String.valueOf(Crdt.getCid()));
                Dialog mDialog = new Dialog(c);
                //mDialog.show();
                Intent intent = new Intent(c, MainActivity.class);
                //c.startActivity(intent);

                myDialog = new Dialog(c);
                myDialog.setContentView(R.layout.dialog_frag);
                //myDialog.show();
                openOpDia(Crdt.getCid(), c, pos, Crdt.getCrdtFlag());
                Toast.makeText(c, "hello", Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.dialog_frag);
                // dialog.show();
            }

            @Override
            public void test(int x) {

            }
        });

    }

    public void openOpDia(final String CreditID, final Context ctx, final int position, final String Flag) {

        Log.i("Flag is ", Flag);
        final dbAdapter dbsource = new dbAdapter(ctx);

        clsCreditView CREDIT;

        dbsource.open();
        //Toast.makeText(c, "opendia method..."+ String.valueOf(ctx), Toast.LENGTH_SHORT).show();

        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();

        // Setting Dialog Title
        alertDialog.setTitle("عملیات بر روی هزینه های ثبت شده");

        // Setting Dialog Message
        alertDialog.setMessage("یکی از عملیات را انتخاب کنید ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_actiondialog);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(c, "You canceled...", Toast.LENGTH_SHORT).show();

            }
        });

        // Setting edit Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ویرایش", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Integer.parseInt(Flag) == 0) {
                    Intent intent = new Intent(ctx, editCredit.class);
                    intent.putExtra("cid", String.valueOf(CreditID));
                    ctx.startActivity(intent);

                } else {
                    Intent intent = new Intent(ctx, editBill.class);
                    intent.putExtra("btid", Flag);
                    intent.putExtra("creditID", String.valueOf(CreditID));
                    ctx.startActivity(intent);

                }
//            //showAct.this.finish();
            }
        });
        // Setting delete Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "حذف", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                dbsource.delCredit(Long.parseLong(CreditID));
                Toast.makeText(c, "هزینه مورد نظر از لیست حذف شد" + String.valueOf(CreditID), Toast.LENGTH_LONG).show();

                //notifyItemRemoved(position);
                //refreshList("today");

            }
        });
        // Setting cancel Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "انصراف", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(c, "عملیات لغو شد", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return CrdtList.size();
    }
}
