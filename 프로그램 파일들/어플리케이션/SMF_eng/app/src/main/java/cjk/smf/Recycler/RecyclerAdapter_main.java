package cjk.smf.Recycler;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import cjk.smf.R;


/**
 * Created by charlie on 2017. 4. 24..
 */

public class RecyclerAdapter_main extends RecyclerView.Adapter<RecyclerAdapter_main.ItemViewHolder> {
    ArrayList<RecyclerItem> mItems;
    private int focusedItem = 0;

    public RecyclerAdapter_main(ArrayList<RecyclerItem> items){
        mItems = items;
    }


    public void setFocusedItem(int position){
        notifyItemChanged(focusedItem);
        focusedItem = position;
        notifyItemChanged(focusedItem);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_main,parent,false);
        return new ItemViewHolder(view);
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String userData=mItems.get(position).getName();
        StringTokenizer stok = new StringTokenizer(userData, "/", false);
        String userId=  "Farm "+stok.nextToken();
        float tempvalueint = Float.valueOf(stok.nextToken());
        float humidvalueint = Float.valueOf(stok.nextToken());
        float co2valueint = Float.valueOf(stok.nextToken());
        String gen = stok.nextToken();
        String mtime = stok.nextToken();
        float mintemp= Float.valueOf(stok.nextToken());
        float maxtemp = Float.valueOf(stok.nextToken());
        float minhumid = Float.valueOf(stok.nextToken());
        float maxhumid = Float.valueOf(stok.nextToken());
        float minco2 = Float.valueOf(stok.nextToken());
        float maxco2 = Float.valueOf(stok.nextToken());
        float lux = Float.valueOf(stok.nextToken());

        String tempvalue = tempvalueint +"˚";
        String humidvalue = humidvalueint +"%";
        String co2value = co2valueint +"";


        holder.mIDTv.setText(userId);
        holder.luxTV.setText(lux+"");

        holder.tempTV.setText(tempvalue);
        if(tempvalueint>maxtemp){
            holder.tempTV.setTextColor(Color.parseColor("#FFC93437"));  // 더우면 빨강
        } else if (tempvalueint<mintemp){
            holder. tempTV.setTextColor(Color.parseColor("#FF375BF1"));  // 추우면 파랑
        } else {
            holder. tempTV.setTextColor(Color.parseColor("#FF000000"));  // 정상시 검정
        }

        holder.humidTV.setText(humidvalue);
        if(humidvalueint>maxhumid){
            holder.humidTV.setTextColor(Color.parseColor("#FFC93437"));  // 높으면 빨강
        } else if (humidvalueint<minhumid){
            holder.humidTV.setTextColor(Color.parseColor("#FF375BF1"));  // 낮으면 파랑
        } else {
            holder.humidTV.setTextColor(Color.parseColor("#FF000000"));  // 정상시 검정
        }

        holder.co2TV.setText(co2value);
        if(co2valueint>maxco2){
            holder. co2TV.setTextColor(Color.parseColor("#FFC93437"));  // 높으면 빨강
        } else if (co2valueint<minco2){
            holder.co2TV.setTextColor(Color.parseColor("#FF375BF1"));  // 낮으면 파랑
        } else {
            holder.co2TV.setTextColor(Color.parseColor("#FF000000"));  // 정상시 검정
        }

        if(gen.equals("1")){
        holder.genTv.setText("Run");
        holder.genTv.setTextColor(Color.parseColor("#0a7c27"));  // 동작시 초록
        }else{
        holder.genTv.setText("Stop");
        holder.genTv.setTextColor(Color.parseColor("#FF000000"));  // 정지 검정
        }
        holder.itemView.setSelected(focusedItem == position);
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tempTV;
        private TextView mIDTv;
        private TextView humidTV;
        private TextView genTv;
        private TextView co2TV;
        private TextView luxTV;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tempTV = (TextView) itemView.findViewById(R.id.temp);
            humidTV = (TextView) itemView.findViewById(R.id.humid);
            mIDTv = (TextView) itemView.findViewById(R.id.clientId);
            co2TV = (TextView) itemView.findViewById(R.id.co2);
            genTv = (TextView) itemView.findViewById(R.id.gen);
            luxTV = (TextView) itemView.findViewById(R.id.lux);

        }
    }

}
