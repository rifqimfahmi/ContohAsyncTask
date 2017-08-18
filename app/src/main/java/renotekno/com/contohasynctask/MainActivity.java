package renotekno.com.contohasynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private TextView kontenInternet;
    private ProgressBar progressBar;
    private Button tombolRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kontenInternet = (TextView) findViewById(R.id.kontenInternet);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tombolRequest = (Button) findViewById(R.id.tombolRequest);

        tombolRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask().execute("https://jsonplaceholder.typicode.com/posts/");
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kontenInternet.setText("Loading....");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonResponse = null;
            
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream in = httpURLConnection.getInputStream();
                jsonResponse = bacaData(in);

                httpURLConnection.disconnect();
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                kontenInternet.setText(s);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }

        private String bacaData(InputStream in) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            if (in != null) {
                InputStreamReader inReader = new InputStreamReader(in, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inReader);
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = reader.readLine();
                }
                inReader.close();
                reader.close();
            }

            return stringBuilder.toString();
        }
    }

}
