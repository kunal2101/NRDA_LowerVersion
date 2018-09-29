package com.nrda.smart_card;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nrda.ItemOffsetDecoration;
import com.nrda.Nrda_Eta_Second;
import com.nrda.Nrda_Route_Eta;
import com.nrda.R;
import com.nrda.database.DatabaseRoute;
import com.nrda.database.DatabaseSmartCardDetailModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Smart_View_Card extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    private RecyclerView cardList;
    private DatabaseRoute dbroute;
    ArrayList<DatabaseSmartCardDetailModel> getDatalist ;
    LinearLayout ly_noitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart__view__card);

        dbroute = new DatabaseRoute(this);

        tool_title = (TextView) findViewById(R.id.appTitle);
        //card_one = (CardView)findViewById(R.id.card_one);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        cardList = (RecyclerView) findViewById(R.id.card_list);
        ly_noitem = (LinearLayout) findViewById(R.id.ly_noitem);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_ImageGallery.this,MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.smart_card_view));
        /*card_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(Smart_View_Card.this,SmartCardDetail.class);
                startActivity(inty);

            }
        });*/

        cardList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cardList.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        cardList.addItemDecoration(itemDecoration);

        getDatalist = dbroute.Get_AllRecord();
        if (getDatalist.size()!=0){
            Myadapter mAdapter = new Myadapter();
            cardList.setAdapter(mAdapter);

            ly_noitem.setVisibility(View.GONE);
            cardList.setVisibility(View.VISIBLE);
        }else{
            ly_noitem.setVisibility(View.VISIBLE);
            cardList.setVisibility(View.GONE);
        }
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            DatabaseSmartCardDetailModel dbSmartCard = getDatalist.get(position);
            final String color_code = dbSmartCard.getColor();

            if (color_code.equalsIgnoreCase("Green")){
                holder.rightmark1.setBackgroundColor(Color.GREEN);
                holder.footerimg.setBackgroundColor(Color.GREEN);
            }else  if (color_code.equalsIgnoreCase("Blue")){
                holder.rightmark1.setBackgroundColor(Color.BLUE);
                holder.footerimg.setBackgroundColor(Color.BLUE);
            }else  if (color_code.equalsIgnoreCase("Red")){
                holder.rightmark1.setBackgroundColor(Color.RED);
                holder.footerimg.setBackgroundColor(Color.RED);
            }

            holder.txt_name.setText("Name : " +dbSmartCard.getName());
            holder.txt_tagid.setText("Card Id : " +dbSmartCard.getTag_id());
            holder.txt_validity.setText("Valid Up to : " +dbSmartCard.getValidity());

            final String tag_id = dbSmartCard.getTag_id();

            holder.ln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inty = new Intent(Smart_View_Card.this, SmartCardDetail_View.class);
                    inty.putExtra("tagid", tag_id);
                    //Toast.makeText(Smart_View_Card.this, "Tag ID: " + tag_id, Toast.LENGTH_SHORT).show();
                    startActivity(inty);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            });
        }
        @Override
        public int getItemCount() {
            return getDatalist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView txt_name, txt_validity;
            protected TextView txt_tagid;
            protected LinearLayout ln;
            protected ImageView rightmark1, footerimg;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                txt_name        = (TextView) itemLayputView.findViewById(R.id.txt_name);
                txt_tagid       = (TextView) itemLayputView.findViewById(R.id.txt_tagid);
                txt_validity    = (TextView) itemLayputView.findViewById(R.id.txt_validity);
                ln              = (LinearLayout) itemLayputView.findViewById(R.id.ln);
                rightmark1      = (ImageView) itemLayputView.findViewById(R.id.rightmark1);
                footerimg      = (ImageView) itemLayputView.findViewById(R.id.footerimg);
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
       // finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
