package app.com.shalan.spacego.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.com.shalan.spacego.Models.User;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class signUpActivity extends AppCompatActivity {
    private String TAG = signUpActivity.class.getSimpleName() ;
    @BindView(R.id.username_signup)
    EditText usernameInput;
    @BindView(R.id.email_signup)
    EditText emailInput;
    @BindView(R.id.password_signup)
    EditText passwordInput;
    @BindView(R.id.signup_account)
    Button signUpBtn;
    @BindView(R.id.login_account)
    Button loginBtn;
    @BindView(R.id.signUp_progressBar)
    ProgressBar progressBar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String email = emailInput.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    mDatabaseReference = mFirebaseDatabase.getReference("Users").child(task.getResult().getUser().getUid());
                                    User user = new User(username,email,password) ;
                                    mDatabaseReference.setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.v(TAG,"successful") ;
                                            } else {
                                                Log.v(TAG,task.getException().getMessage()) ;
                                            }

                                        }
                                    });
                                }else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUpActivity.this,loginActivity.class) ;
                startActivity(intent);
            }
        });
    }
}
