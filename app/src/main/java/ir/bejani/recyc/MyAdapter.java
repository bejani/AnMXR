package ir.bejani.recyc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.bejani.R;

/**
 * Created by ulusen on 10/30/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<String> languages;

    public MyAdapter(Context c, ArrayList<String> languages) {
        this.c = c;
        this.languages = languages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.activity_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //BIND DATA
        holder.nameTxt.setText(languages.get(position));

        //ITEM CLICK
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Toast.makeText(c, languages.get(pos), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void test(int x) {
                Toast.makeText(c, languages.get(x), Toast.LENGTH_SHORT).show();
            }

        });



    }

    @Override
    public int getItemCount() {
        return languages.size();
    }
}
