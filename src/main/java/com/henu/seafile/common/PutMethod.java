package com.henu.seafile.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PutMethod {
	OkHttpClient client = null;

	public PutMethod() {
		client = new OkHttpClient();
	}

	/**
	 * 
	 * @param map put方法需要的参数；若是有token也传到里面
	 * @param url put方法请求的seafile路径
	 * @return 返回success或者error
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ServiceResponse<Map<String, Object>> run(Map<String, String> map, String url, String Token)
			throws IOException, InterruptedException {

//		String token = "6714e74ffb550817de71992ac4d35831058a49de";//windows下免费版的seafile
//		String token = "d5d034f1198122ed56992a6fc6fc33a331c10249";//centos下专业版的seafile
//		FormBody body = new FormBody.Builder()
//				.add("password", map.get("password"))
//				.build();
//		Request request = new Request.Builder()
//				.header("Authorization", "Token " + token)
//				.header("Content-Type", "application/json")
//				.url(url)
//				.put(body)
//				.build();

		Map<String, Object> tempMap = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 拼接put请求参数
				FormBody.Builder builder = new FormBody.Builder();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					builder.add(entry.getKey(), entry.getValue());
				}
				// 拼接request
				FormBody formBody = builder.build();
				Request request = null;
				if (Token == null) {
					request = new Request.Builder().header("Content-Type", "application/json").url(url).put(formBody)
							.build();
				} else {
					request = new Request.Builder().header("Authorization", "Token " + Token)
							.header("Content-Type", "application/json").url(url).put(formBody).build();
				}
				Response response;
				try {
					response = client.newCall(request).execute();
					if (response.isSuccessful()) {
						tempMap.put("data", response.body().string());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
		thread.join();
		if (tempMap.size() > 0) {
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage("请求失败");
		}
	}

	public ServiceResponse<Map<String, Object>> runAsync(Map<String, String> map, String url, String Token) {

		// 拼接put请求参数
		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		// 拼接request
		FormBody formBody = builder.build();
		Request request = null;
		if (Token == null) {
			request = new Request.Builder().header("Content-Type", "application/json").url(url).put(formBody).build();
		} else {
			request = new Request.Builder().header("Authorization", "Token " + Token)
					.header("Content-Type", "application/json").url(url).put(formBody).build();
		}
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onFailure(Call call, IOException e) {
				// TODO 自动生成的方法存根

			}
		});
		;
		return null;
	}

	public ServiceResponse<Map<String, Object>> createUserRun(Map<String, String> map, String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Object json = JSONObject.toJSON(map);
				System.out.println(json.toString());
				MediaType JSON = MediaType.parse("application/json; charset=utf-8");
				RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
				Request request = new Request.Builder().header("Authorization", "Token " + Token)
						.header("Accept", "application/json").header("indent", "4").url(url).put(requestBody).build();

				Response response;
				try {
					response = client.newCall(request).execute();
					if (response.isSuccessful()) {
						tempMap.put("data", response.body().string());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (tempMap.size() > 0) {
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage("请求失败");
		}
	}

	public ServiceResponse<Map<String, Object>> groupRun(Map<String, String> map, String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Object json = JSONObject.toJSON(map);
				if(map != null) {
					System.out.println(json.toString());
				}			
				MediaType JSON = MediaType.parse("application/json; charset=utf-8");
				RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
				Request request = new Request.Builder().header("Authorization", "Token " + Token).url(url)
						.put(requestBody).build();

				Response response;
				try {
					response = client.newCall(request).execute();
					String backStr = null;
					backStr = response.body().string();
					tempMap.put("data", backStr);
					System.out.println(backStr);
					if (response.isSuccessful()) {
						tempMap.put("flag", "1");
					} else {
						tempMap.put("flag", "0");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (tempMap.get("flag").equals("1")) {
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage((String) tempMap.get("data"));
		}
	}

	public ServiceResponse<Map<String, Object>> deleteRun(String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Request request = new Request.Builder().header("Authorization", "Token " + Token).url(url).delete()
						.build();

				Response response;
				try {
					String backStr = null;
					response = client.newCall(request).execute();
					backStr = response.body().string();
					tempMap.put("data", backStr);
					System.out.println(backStr);
					if (response.isSuccessful()) {
						tempMap.put("flag", "1");
					} else {
						tempMap.put("flag", "0");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (tempMap.get("flag").equals("1")) {
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage((String) tempMap.get("data"));
		}
	}

}
