package inozemtsev.teststproject2.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static inozemtsev.teststproject2.myapplication.utils.NetworkUtils.GenerateURL;
import static inozemtsev.teststproject2.myapplication.utils.NetworkUtils.GetResponseFromURL;

public class MainActivity extends AppCompatActivity {
    private final String USER_NOT_FOUND_MESSAGE = "Пользователя с данным id не существует";

    private EditText searchField_;
    private Button searchButton_;
    private TextView result_;
    private TextView errorMessage_;
    private ProgressBar loadingIndicator_;

    private void showResultWithTextView(){
        result_.setVisibility(View.VISIBLE);
        errorMessage_.setVisibility(View.INVISIBLE);
    }

    private void showErrorWithTextView(){
        errorMessage_.setVisibility(View.VISIBLE);
        result_.setVisibility(View.INVISIBLE);
    }

    class VkQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = GetResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute(){
            loadingIndicator_.setVisibility(View.VISIBLE);

        }


        @Override
        protected void onPostExecute(String response){
            String firstName = null;
            String lastName = null;
            if(response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    StringBuilder result = new StringBuilder();

                    for(int i = 0; i < jsonArray.length();++i){
                        JSONObject userInfo = jsonArray.getJSONObject(i);
                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");

                        result.append("Имя: ")
                                .append(firstName)
                                .append(" Фамилия:")
                                .append(lastName)
                                .append("\n")
                                .append("\n");
                    }

                    result_.setText(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                showResultWithTextView();
            }else{
                showErrorWithTextView();
            }

            loadingIndicator_.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField_ = findViewById(R.id.et_search_field);
        searchButton_ = findViewById(R.id.b_search_vk);
        result_ = findViewById(R.id.tv_result);
        errorMessage_ = findViewById(R.id.tv_error_message);
        loadingIndicator_ = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL generatedURL = GenerateURL(searchField_.getText().toString());
                new VkQueryTask().execute(generatedURL);
            }
        };

        searchButton_.setOnClickListener(onClickListener);
    }
}