package pl.pue.air.ab;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class WebViewActivity extends Activity {

    private String TAG = "WVA";
    public static String URL = "URL";
    private WebView webViewFullScreen;
   

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewfullscreen);
        webViewFullScreen = findViewById(R.id.webViewFullScreen);

        Bundle extras = getIntent().getExtras();
        String url=extras.getString(URL);

        displayCurrentView(url);
    }

    public void displayCurrentView(String singleUrl){
        final String sURL=singleUrl; // must be final to for the thread below
        MainActivity.activity.runOnUiThread (new Thread(new Runnable() {
            public void run() {
                String mimeType = "text/html";
                String encoding = "utf-8";
                webViewFullScreen.getSettings().setDefaultFontSize(18);
                webViewFullScreen.setBackgroundColor(
                        getResources().getColor(android.R.color.transparent));
                //webViewGlobalText.getSettings().setJavaScriptEnabled(true);
                //webViewGlobalText.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                //webViewGlobalText.getSettings().setSupportMultipleWindows(true);
                //webViewGlobalText.setWebChromeClient(new WebChromeClient());
                if (sURL.toLowerCase().startsWith("http")){
                    webViewFullScreen.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(
                                WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    webViewFullScreen.loadUrl(sURL);
                }
                else{
                    webViewFullScreen.loadDataWithBaseURL(
                            null, sURL, mimeType, encoding, null);
                }
            }
        }));
    }

    protected void onDestroy(){
        super.onDestroy();
        QRCodeActivity.activity.finishQRCodeActivity();
        MainActivity.activity.finish();
    }

}
