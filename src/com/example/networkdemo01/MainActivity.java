package com.example.networkdemo01;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
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
			sendRequestWithHttpClient();
		}
	}

	private void sendRequestWithHttpClient() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet("http://v.juhe.cn/weather/citys?dtype=&key=283a0a5758e7077da40b4fea4860a6e6");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						//响应成功了,获取网络文本实体
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						response.split(",");
						Message message = new Message ();
						message.what = SHOW_RESPONSE;
						handler.sendMessage(message);
					}
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
	}

}
