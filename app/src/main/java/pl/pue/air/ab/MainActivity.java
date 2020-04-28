package pl.pue.air.ab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static MainActivity activity = null;
    private String TAG = "MA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        View blankView = new View(this);
        setContentView(blankView);

        Intent intent = new Intent(this, QRCodeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void finishMainActivity(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, MainActivity.activity.getString(
                        R.string.MainActivityClosed));
                finish();
            }
        });
    }
}
