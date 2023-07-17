package com.example.mentalhub;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentalhub.PsychologistSide.PsychologistActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private long mLastClickTime = 0;
    EditText editTextEmail, editTextPassword;
    Button loginButton, googleLoginBtn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    TextView textView;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;

    boolean ispatient = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        editTextEmail = findViewById(R.id.emailAdd);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
        googleLoginBtn = findViewById(R.id.googleLogin);

        // Single Sign On Google.
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginBtn.setOnClickListener((v) -> {
            signIn();
        });

        textView.setOnClickListener((v) -> {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
        });

        loginButton.setOnClickListener((v) -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Enter your email");
                } else if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter your password");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            String userId = user.getUid();
                                            DatabaseReference userRef = database.getReference().child("Users").child(userId);
                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        String userType = snapshot.child("userType").getValue(String.class);
                                                        if (userType != null && (userType.equals("psychologist") || userType.equals("patient"))) {
                                                            Toast.makeText(Login.this, "Login Success!.", Toast.LENGTH_SHORT).show();
                                                            if (userType.equals("psychologist")) {
                                                                ispatient = false;
                                                                Intent intent = new Intent(Login.this, PsychologistActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                ispatient = true;
                                                                Intent intent = new Intent(Login.this, MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Login.this, "Invalid user type.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(Login.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e(TAG, "onCancelled: ", error.toException());
                                                    Toast.makeText(Login.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(Login.this, "User not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                                        Toast.makeText(Login.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
        });
    }

    // Sign in INT
    int RC_SIGN_IN = 40;

    private void signIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    // Google SSO
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Authentication in firebase
    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Users users = new Users();
                            assert user != null;
                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfile(Objects.requireNonNull(user.getPhotoUrl()).toString());

                            database.getReference().child("Users")
                                    .child(user.getUid()).setValue(users);

                            Intent intent;
                            if(ispatient){
                                intent = new Intent(Login.this, MainActivity.class);
                            }
                            else {
                                intent = new Intent(Login.this, PsychologistActivity.class);
                            }
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithGoogle:failure", task.getException());
                            Toast.makeText(Login.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent;
            if(ispatient){
                intent = new Intent(Login.this, MainActivity.class);
            }
            else {
                intent = new Intent(Login.this, PsychologistActivity.class);
            }
            startActivity(intent);
        }
    }
}
