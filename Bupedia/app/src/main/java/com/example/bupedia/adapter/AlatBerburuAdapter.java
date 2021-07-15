package com.example.bupedia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bupedia.R;
import com.example.bupedia.model.AlatModel;

import java.util.ArrayList;

public class AlatBerburuAdapter extends RecyclerView.Adapter<AlatBerburuAdapter.MahasiswaViewHolder> {

    private ArrayList<AlatModel> dataList;
    private Context context;

    public AlatBerburuAdapter(Context context) {
        this.context = context;
    }

    public void addData(ArrayList<AlatModel> dataList) {
        this.dataList = dataList;

    }

    @Override
    public AlatBerburuAdapter.MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_item_alat, parent, false);
        return new AlatBerburuAdapter.MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlatBerburuAdapter.MahasiswaViewHolder holder, int position) {
        holder.tvDesc.setText(dataList.get(position).getDeskripsiAlat());
        holder.tvJenisAlat.setText(dataList.get(position).getJenis());
        holder.tvNamaAlat.setText(dataList.get(position).getNamaAlat());
        byte [] image = dataList.get(position).getFotoAlat();
        Bitmap bmp= BitmapFactory.decodeByteArray(image,0,image.length);
        holder.ivAlat.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaAlat, tvJenisAlat, tvDesc;
        private ImageView ivAlat;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            ivAlat = itemView.findViewById(R.id.ivAlat);
            tvNamaAlat = itemView.findViewById(R.id.tvNamaAlat);
            tvJenisAlat = itemView.findViewById(R.id.tvJenisAlat);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
