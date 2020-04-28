package pl.pue.air.ab;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeActivity extends Activity {

    public static QRCodeActivity activity = null;
    private String TAG = "QCA";
    private IntentIntegrator integrator=null;
    final int GRANT_REQ_CODE=111;
    String PERMISSIONS[] = {"INTERNET", "ACCESS_NETWORK_STATE"};
    private boolean atLeastOnePermissionNotGranted=false;

    public void finishQRCodeActivity(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, QRCodeActivity.activity.getString(
                        R.string.QRCodeActivityClosed));
                finish();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //23 ) {
            requestPermissions(PERMISSIONS, GRANT_REQ_CODE);
        }

        if (integrator == null) {
            integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt(MainActivity.activity.getString(R.string.scanBarcode));
            integrator.setCameraId(0); // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        switch (requestCode) {
            case GRANT_REQ_CODE:
                for (int i=0; i<grantResults.length; i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(
                                this,
                                getString(R.string.noPermissionGrantedSorry)+"=" + i,
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (result != null) {
                String resultString = result.getContents();
                if (resultString != null) {
                    // activating WebView activity?
                    String resultStringLC = resultString.toLowerCase();
                    if ( resultStringLC.startsWith("http://") ||
                            resultStringLC.startsWith("https://")
                    ) {
                        //URL - trying to display with a webview
                        Intent webViewIntent= new Intent(
                                this, WebViewActivity.class);
                        webViewIntent.putExtra(
                                WebViewActivity.URL, resultStringLC);
                        startActivity(webViewIntent);
                    }
                    finish();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Log.d(TAG, MainActivity.activity.getString(
                    R.string.QRcodeActivityCancelled));
            finish();
            MainActivity.activity.finishMainActivity();
        }
        else{
            Log.d(TAG, MainActivity.activity.getString(
                    R.string.QRcodeActivityCancelled)+": "+resultCode);
            finish();
            MainActivity.activity.finishMainActivity();
        }
    }
}
