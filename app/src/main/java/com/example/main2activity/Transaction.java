package com.example.main2activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Transaction extends AppCompatActivity {

    SQLiteDatabase db;
    String uid, uname, ubalance;
    String[] name, email;
    ListView lsq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        setTitle("User Detail");

        Intent i = getIntent();
        uid = i.getStringExtra("id");
        uname = i.getStringExtra("name");
        ubalance = i.getStringExtra("bal");

        name = i.getStringArrayExtra("names");
        email = i.getStringArrayExtra("emails");


        lsq = findViewById(R.id.list1);
        TextView Uname = findViewById(R.id.sname);
        TextView Uemail = findViewById(R.id.sEmail);
        TextView bal = findViewById(R.id.balance);

        Uname.setText("Name   ::  " + uname);
        Uemail.setText("E-mail ::  " + uid);
        bal.setText("Bal    ::  " + ubalance);





        lsq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!uname.equals(name[position])) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Transaction.this);
                    alertDialog.setTitle("Transaction").setMessage(uname + "-->" + name[position] + "\n\nEnter Credits to transfer").setCancelable(false);

                    final EditText input = new EditText(Transaction.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setLayoutParams(lp);

                    alertDialog.setView(input);
                    alertDialog.setIcon(R.drawable.ic_launcher_foreground);

                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!(input.getText().toString().isEmpty())) {
                                        double amount = Double.valueOf(input.getText().toString());
                                        transfer(email[position], amount);
                                    }

                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
            }
        });

    }


    public void transaction(View view) {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);

        lsq.setAdapter(adapter);



    }

    public void transfer(String sid, double amount) {
        if (amount <= Double.valueOf(ubalance)) {
            db = openOrCreateDatabase("Student", Context.MODE_PRIVATE, null);

            db.beginTransaction();
            try {
                if (db.isOpen()) {
                    Cursor result = db.rawQuery("SELECT balance FROM account where Email='" + sid + "'", null);
                    result.moveToFirst();
                    double sbalance = Double.valueOf(result.getString(0)) + amount;
                    result.close();

                    db.execSQL("UPDATE account SET balance = '" + sbalance + "' WHERE email = '" + sid + "'");
                    double uubalance = Double.valueOf(ubalance) - amount;
                    db.execSQL("UPDATE account SET balance = '" + uubalance + "' WHERE email = '" + uid + "'");
                    db.execSQL("insert into tt values('" + uid + "','" + sid + "','" + amount + "')");

                    Toast.makeText(Transaction.this, "Transaction SuccessFull!", Toast.LENGTH_SHORT).show();
                    db.setTransactionSuccessful();
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(Transaction.this, "Transaction not complete", Toast.LENGTH_SHORT).show();
            } finally {
                db.endTransaction();
            }

        } else Toast.makeText(Transaction.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
    }
}