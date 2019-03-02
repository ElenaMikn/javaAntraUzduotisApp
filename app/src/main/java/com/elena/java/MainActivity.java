package com.elena.java;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );
            FirebaseAuth auth = FirebaseAuth.getInstance();
            startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setAvailableProviders(providers)
                            .build(),
                    1
            );
        } catch (Exception ex) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(ex.getMessage());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("duomenys");
                    TextView date = (TextView) findViewById(R.id.date);
                    myRef.setValue(date.getText().toString());

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            TextView textView = (TextView) findViewById(R.id.textView);
                            textView.setText("Doumenis yra: " + value);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            TextView textView = (TextView) findViewById(R.id.textView);
                            textView.setText("Skaitymo klaida:" + error.toException());
                        }
                    });
                } catch (Exception ex) {
                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setText(ex.getMessage());
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
