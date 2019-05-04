package com.example.drivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText email,password;
     FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        Button RegBtn = findViewById(R.id.RegBtn);
        Button LoginBtn=findViewById(R.id.SignBtn);
       email=(EditText)findViewById(R.id.ID);
       password=(EditText)findViewById(R.id.Password);
       firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        if(user!=null)
        {
            finish();
            startActivity(new Intent(Login.this,MapsActivity.class));
            Login.this.finish();
        }
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Validate(email.getText().toString(),password.getText().toString());
            }
        });
    }
    private  void Validate(String emails,String passwords)
    {
        String emailcheck=email.getText().toString().trim();
        String passwordcheck=password.getText().toString().trim();
        if(emailcheck.isEmpty())
        {
            email.setError("Enter email");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailcheck).matches())
        {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        if(passwordcheck.isEmpty())
        {
            password.setError("Enter Your Password");
            password.requestFocus();
            return;
        }

        progressDialog.setMessage("In progress");
        progressDialog.show();
firebaseAuth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
if(task.isSuccessful()){
    progressDialog.dismiss();
    Toast.makeText(Login.this,"Login successful",Toast.LENGTH_SHORT).show();
    startActivity(new Intent(Login.this,MapsActivity.class));
    finish();
}
else
{
    progressDialog.dismiss();
    Toast.makeText(Login.this,"Login unsuccessful",Toast.LENGTH_SHORT).show();
}
    }
});
    }
}
