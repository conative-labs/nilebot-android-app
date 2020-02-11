package com.soleeklab.nilebot;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.soleeklab.nilebot.api.RestClient;
import com.soleeklab.nilebot.features.SplashActivity;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CrashReportActivity extends ParentActivity {

    private Button _btnclose;
    public static String TAG = CrashReportActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_report);
        Intent intent = getIntent();
        System.out.println(intent.getStringExtra("stackTrace"));
        sendError(intent.getStringExtra("stackTrace"));
        _btnclose = (Button) findViewById(R.id.btnclose);

        _btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CrashReportActivity.this, SplashActivity.class));
                finish();
            }
        });
    }

    @Override
    protected View getView() {
        return null;
    }

    private void sendError(String report) {
        String email_from ="no-reply@soleeklab.com";
        String email_to ="shirt@soleeklab.com";
        String email_subject ="Crash report from "+getString(R.string.app_name);
        String body ="{\"personalizations\":[{\"to\":[{\"email\":\""+email_to+"\"}],\"subject\":\""+email_subject+"\"}],\"from\":{\"name\":\"Crash Report\",\"email\":\""+email_from+"\"},\"content\":[{\"type\":\"text/plain\",\"value\": \""+escapeForJava(report) +"\"}]}";

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json"), body);

        RestClient.getClient().sendGridApi(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("Response","Response: "+response.message());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "error"+t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });


    }

    public static String escapeForJava( String value)
    {
        StringBuilder builder = new StringBuilder();

        for( char c : value.toCharArray() )
        {

            if( c == '\'' )
                builder.append( "\\'" );
            else if ( c == '\"' )
                builder.append( "\\\"" );
            else if( c == '\r' )
                builder.append( "\\r" );
            else if( c == '\n' )
                builder.append( "\\n" );
            else if( c == '\t' )
                builder.append( "\\t" );
            else if( c < 32 || c >= 127 )
                builder.append( String.format( "\\u%04x", (int)c ) );
            else
                builder.append( c );

        }

        return builder.toString();
    }

    @Override
    public void showToast(String message) {

    }
}