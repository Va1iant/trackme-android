package edu.se581.trackme;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class WebProxy {

	private static final int CONNECTION_TIMEOUT = 3000;
	private static final int SOCKET_TIMEOUT = 5000;

	public interface OnFinishProcessHttp {
		public void onFinishProcessHttp(int statusCode, String responseBody);
	}

	// private HttpClient mHttpClient;
	private HttpParams mHttpParameters;

	public WebProxy() {

		mHttpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(mHttpParameters,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(mHttpParameters, SOCKET_TIMEOUT);
	}

	public void post(String uri, String content, OnFinishProcessHttp callback)
	{
		
		new HttpPostTask(callback).execute(uri, content);
	}

	private class HttpPostTask extends AsyncTask<String, Void, ArrayList<String>> {

		OnFinishProcessHttp mCallback;

		public HttpPostTask(OnFinishProcessHttp callback) {
			super();
			mCallback = callback;
		}

		@Override
		protected ArrayList<String> doInBackground(String... params)
		{
			// we expect at least 2 parameters here
			if (params == null || params.length < 2) {
				TMLog.err("Incorrect param");
				return null;
			}

			String uri = params[0];
			String content = params[1];

			HttpClient httpClient = new DefaultHttpClient(mHttpParameters);
			HttpContext localContext = new BasicHttpContext();

			// put default status code as bad gateway
			int statusCode = HttpStatus.SC_BAD_GATEWAY;

			try {

				// fill in the body
				StringEntity se = new StringEntity(content);
				se.setContentEncoding("UTF-8");
				se.setContentType("application/txt");

				// fill in the target address
				HttpPost postRequest = new HttpPost(uri);
				postRequest.setEntity(se);

				// execute
				HttpResponse response = httpClient.execute(postRequest,
						localContext);

				if (response != null) {
					statusCode = response.getStatusLine().getStatusCode();
					HttpEntity httpEntity = response.getEntity();
					String responseStr = EntityUtils.toString(httpEntity);
					
					TMLog.debug("statusCode " + statusCode);

					ArrayList<String> returns = new ArrayList<String>();
					returns.add(String.valueOf(statusCode));
					returns.add(responseStr);
					
					return returns;
					
				}
			} catch (Exception e) {
				TMLog.err("HttpPostTask exception");
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> returns)
		{
			if (returns != null && mCallback != null)
				mCallback.onFinishProcessHttp(Integer.valueOf(returns.get(0)), returns.get(1));
		}

	}

}
