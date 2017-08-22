package app.com.shalan.spacego.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import app.com.shalan.spacego.Handler.Utils;
import app.com.shalan.spacego.Models.User;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private String TAG = SignUpActivity.class.getSimpleName();

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

                // Check if internet connection is fine ?!
                if (Utils.isConnected(getApplicationContext())) {
                    signUp();
                } else {
                    Snackbar.make(view, R.string.check_ur_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.tryAgain, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    signUp();
                                }
                            });
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        final String username = usernameInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String email = emailInput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.enter_Password, Toast.LENGTH_SHORT).show();
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.passTooShort, Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.done, Toast.LENGTH_SHORT).show();
                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                            mDatabaseReference = mFirebaseDatabase.getReference("Users").child(task.getResult().getUser().getUid());
                            User user = new User(username, email, password);
                            mDatabaseReference.setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.v(TAG, "successful");
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Log.v(TAG, task.getException().getMessage());
                                            }

                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}
