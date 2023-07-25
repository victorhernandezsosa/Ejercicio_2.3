package com.example.ejercicio_23;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Photograh> {

    private Context context;

    public CustomAdapter(Context context, List<Photograh> fotos) {
        super(context, 0, fotos);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_foto, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageViewItem = convertView.findViewById(R.id.captura);
            viewHolder.textViewItemDescription = convertView.findViewById(R.id.desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Photograh photograh = getItem(position);
        if (photograh != null) {
            viewHolder.textViewItemDescription.setText(photograh.getDescripcion());


            byte[] fotoBytes = photograh.getImagen();
            Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
            viewHolder.imageViewItem.setImageBitmap(bitmap);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageViewItem;
        TextView textViewItemDescription;
    }
}
