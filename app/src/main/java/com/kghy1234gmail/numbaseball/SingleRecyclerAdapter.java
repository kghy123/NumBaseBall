package com.kghy1234gmail.numbaseball;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<HitTheRound> hitTheRounds;

    public SingleRecyclerAdapter(Context context, ArrayList<HitTheRound> hitTheRounds) {
        this.context = context;
        this.hitTheRounds = hitTheRounds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.single_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder mHolder = (ViewHolder) holder;

        mHolder.round.setText(position+1+" 회 \n" + hitTheRounds.get(position).num);

        Log.d("strikesImage", hitTheRounds.get(position).strikes + "");
        for(int i = 0; i < hitTheRounds.get(position).strikes; i++){

            ImageView img = new ImageView(context);
            img.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            img.setImageResource(R.drawable.strike);
            mHolder.strikesLayout.addView(img);

        }

        Log.d("strikesImage", hitTheRounds.get(position).balls + "");
        for(int i = 0; i < hitTheRounds.get(position).balls; i++){

            ImageView img = new ImageView(context);
            img.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            img.setImageResource(R.drawable.ball);
            mHolder.ballsLayout.addView(img);

        }

        Log.d("strikesImage", hitTheRounds.get(position).outs + "");
        for(int i = 0; i < hitTheRounds.get(position).outs; i++){

            ImageView img = new ImageView(context);
            img.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            img.setImageResource(R.drawable.out);
            mHolder.outsLayout.addView(img);

        }

    }

    @Override
    public int getItemCount() {
        return hitTheRounds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView round;
        LinearLayout strikesLayout, ballsLayout, outsLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            round = (TextView)itemView.findViewById(R.id.single_recycler_item_round);
            strikesLayout = (LinearLayout)itemView.findViewById(R.id.single_recycler_item_strikes);
            ballsLayout = (LinearLayout)itemView.findViewById(R.id.single_recycler_item_balls);
            outsLayout = (LinearLayout)itemView.findViewById(R.id.single_recycler_item_outs);

        }


    }
}
