package com.example.main2activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] email;
    private final String[] balance;

    public MyAdapter(Activity context, String[] name, String[] email, String[] balance) {
        super(context, R.layout.list_item, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.name = name;
        this.email = email;
        this.balance = balance;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        TextView Name = rowView.findViewById(R.id.name);
        TextView Email = rowView.findViewById(R.id.email);
        TextView Bal = rowView.findViewById(R.id.balance);

        Name.setText(name[position]);
        Email.setText(email[position]);
        Bal.setText(balance[position]);

        return rowView;

    }

    ;
}
