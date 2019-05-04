package com.example.drivers;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {
    private long backPressedTime;
    private ProgressDialog progressDialog;
    private EditText ename,ecnic,econtact,eemail,epassword,epointno;
    private EditText eText;
    private Button RegBtn;
    private FirebaseAuth mAuth;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        ename = (EditText) findViewById(R.id.name);
        ecnic = (EditText) findViewById(R.id.cnic);
        eText = (EditText) findViewById(R.id.editText1);
        econtact = (EditText) findViewById(R.id.contact);
        eemail=(EditText)findViewById(R.id.email);
        epassword=(EditText)findViewById(R.id.Password);
        epointno=(EditText)findViewById(R.id.point);
        RegBtn=(Button) findViewById(R.id.RegBtn);
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        eText = (EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }
    public void register() {
        final String name=ename.getText().toString().trim();
        final String cnic=ecnic.getText().toString().trim();
        String pointno=epointno.getText().toString().trim();
        final String joindate=eText.getText().toString().trim();
        final String contact=econtact.getText().toString().trim();
        final String email=eemail.getText().toString().trim();
        String password=epassword.getText().toString().trim();
        try {
            int num = Integer.parseInt(cnic);
            Log.i("",num+" is a number");
        } catch (NumberFormatException e) {
            Log.i("",cnic+" is not a number");
        }
        if(name.isEmpty())
        {
            ename.setError("Name required");
            ename.requestFocus();
            return;
        }
        if(cnic.isEmpty())
        {
            ecnic.setError("CNIC is required");
            ecnic.requestFocus();
            return;
        }
        if(cnic.length()!=14)
        {
            ename.setError("Invalid cnic");
            ecnic.requestFocus();
        }
        if(contact.isEmpty())
        {
            econtact.setError("Contact number required");
            econtact.requestFocus();
            return;
        }
        if(contact.length()!=11)
        {
            econtact.setError("Enter a valid contact number");
            econtact.requestFocus();
            return;
        }
        if(pointno.isEmpty())
        {
            epointno.setError("Point no Required");
            epointno.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            eemail.setError("Email required");
            eemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            eemail.setError("Enter a valid email");
            eemail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            epassword.setError("Create Password");
            epassword.requestFocus();
            return;
        }
        if(password.length()<8)
        {
            epassword.setError("Password shouold be atleast 8 characters long");
            epassword.requestFocus();
            return;
        }
        progressDialog.setMessage("In progress");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            //store data in firebase
                            DriverInfo driverInfo=new DriverInfo(name,contact,cnic,email,joindate);
                            FirebaseDatabase.getInstance().getReference("Driver Details")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(driverInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Registeration Successful",Toast.LENGTH_SHORT).show();
                                        startActivity( new Intent(Register.this,Login.class));
                                        Register.this.finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Registeration Unsuccessful",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            //handle the already login user
        }
    }
    public void onBackPressed()
    {
if(backPressedTime + 2000 > System.currentTimeMillis())
{
    super.onBackPressed();
    return;
}
else
{
    Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
}
backPressedTime=System.currentTimeMillis();
    }
}