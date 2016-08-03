package com.example.networkdemo01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private Button sendRequest;
	private TextView textResponse;
	private static final int SHOW_RESPONSE = 0;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case SHOW_RESPONSE:
				String response = (String)msg.obj;
				textResponse.setText(response);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendRequest = (Button)findViewById(R.id.send_request);
		textResponse = (TextView)findViewById(R.id.text_response);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.send_request){
			sendRequestWithHttpUrlConnection();
		}
	}

	private void sendRequestWithHttpUrlConnection() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				URL url;  
                HttpURLConnection connection = null;  
                try {  
                    String cityName = URLEncoder.encode("广州", "utf-8");  
                    url = new URL(  
                            "http://v.juhe.cn/weather/index?format=2&cityname="  
                                    + cityName  
                                    + "&key=283a0a5758e7077da40b4fea4860a6e6");  
                    connection = (HttpURLConnection) url.openConnection();  
                    connection.setRequestMethod("GET");  
                    connection.setConnectTimeout(8000);  
                    connection.setReadTimeout(8000);  
                    InputStream in = connection.getInputStream();  
                    // 下面对获取到的输入流进行读取  
                    BufferedReader reader = new BufferedReader(  
                            new InputStreamReader(in));  
                    StringBuilder response = new StringBuilder();  
                    String line;  
                    while ((line = reader.readLine()) != null) {  
                        response.append(line);  
                    }  
                    System.out.println("response=" + response.toString());  
                    //parseWithJSON(response.toString());  
                    parseWeatherWithJSON(response.toString());  
                    parseWithJSON(response.toString());
                    Message message = new Message();  
                    message.what = SHOW_RESPONSE;  
                    // 将服务器返回的结果存放到Message中  
                    message.obj = response.toString();  
                    handler.sendMessage(message);  
                } catch (MalformedURLException e) {  
                    e.printStackTrace();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                } finally {  
                    if (connection != null) {  
                        connection.disconnect();  
                    }  
                }  
			}
        }).start();
  
    }  
  
    protected void parseWeatherWithJSON(String response) {  
        try {  
            JSONObject jsonObject=new JSONObject(response);  
            String resultcode=jsonObject.getString("resultcode");  
            if(resultcode.equals("200")){  
                JSONObject resultObject=jsonObject.getJSONObject("result");  
                JSONObject todayObject=resultObject.getJSONObject("today");  
                String date_y=todayObject.getString("date_y");  
                String week=todayObject.getString("week");  
                String temperature=todayObject.getString("temperature");  
                Log.d("MainActivity", "date_y="+date_y+"week="+week+"temp="+temperature);  
            }  
              
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private  void parseWithJSON(String response) {  
        try {  
            JSONArray jsonArray = new JSONArray(response);  
            for (int i = 0; i < jsonArray.length(); i++) {  
                JSONObject jsonObject = jsonArray.getJSONObject(i);  
                String id = jsonObject.getString("id");  
                String name = jsonObject.getString("name");  
                String version = jsonObject.getString("version");  
                Log.d("MainActivity", "id=" + id + "name=" + name + "version="  
                        + version);  
            }  
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
    }  
}