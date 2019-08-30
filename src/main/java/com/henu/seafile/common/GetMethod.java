package com.henu.seafile.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetMethod {

	public ServiceResponse<Map<String, Object>> downloadFile(String url, String Token) {

		Map<String, Object> map = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder().header("Authorization", "Token " + Token)
						.header("Accept", "application/json").header("indent", "4").url(url).build();
				Response response = null;
				String backStr = null;
				try {
					response = client.newCall(request).execute();
					backStr = response.body().string();
					System.out.println(backStr);
					map.put("data", backStr);
					if (response.isSuccessful()) {
						map.put("flag", "1");
					} else {
						map.put("flag", "0");
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
			e.printStackTrace();
		}
		if (map.get("flag").equals("1")) {
			return ServiceResponse.createBySuccessData(map);
		} else {
			return ServiceResponse.createByErrorMessage(map.get("data").toString());
		}
	}

	public ServiceResponse<Map<String, Object>> deleteFile(String url, String Token) {

		Map<String, Object> map = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder().header("Authorization", "Token " + Token)
						.header("Accept", "application/json").header("indent", "4").url(url).delete().build();
				Response response = null;
				String backStr = null;
				try {
					response = client.newCall(request).execute();
					backStr = response.body().string();
					map.put("data", backStr);
					if (response.isSuccessful()) {
						map.put("flag", "1");
					} else {
						map.put("flag", "0");
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
			e.printStackTrace();
		}
		if (map.get("flag").equals("1")) {
			return ServiceResponse.createBySuccessData(map);
		} else {
			return ServiceResponse.createByErrorMessage(map.get("data").toString());
		}
	}

	// 异步请求
	public ServiceResponse<Map<String, Object>> runAsync(String url, String Token) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Map<String, Object> map = new HashMap<>();

		Request request = null;
		if (Token == null) {
			request = new Request.Builder().url(url).build();
		} else {
			request = new Request.Builder().header("Authorization", "Token " + Token)
					.header("Content-Type", "application/json").url(url).build();
		}
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful())
					map.put("data", response.body().string());
			}

			@Override
			public void onFailure(Call call, IOException e) {

			}
		});
		if (map.size() > 0) {
			return ServiceResponse.createBySuccessData(map);
		} else {
			return ServiceResponse.createByErrorMessage("请求失败");
		}
	}

	/**
	 * 后续在进行封装，这个方法会得到上传文件路径 上面的异步请求也带了参数，可以用那个，也可以用这个
	 * 异步请求获取数据有问题，因为异步的无法让线程join，所以有问题！！！
	 * 
	 * @param Token
	 * @return
	 */
	public ServiceResponse<Map<String, Object>> runAsyncUploadFile(String Token, String url) {
		OkHttpClient client = new OkHttpClient();
		Map<String, Object> map = new HashMap<>();
		System.out.println(123123);
		Request request = new Request.Builder().header("Authorization", "Token " + Token)
				.header("Accept", "application/json").header("indent", "4").url(url).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				if (response.isSuccessful()) {
					map.put("data", response.body().string());
					System.out.println(response.body().string() + "success");
				} else {
					System.out.println(response.body().string() + "error");
				}
			}

			@Override
			public void onFailure(Call call, IOException e) {

			}
		});

		if (map.size() > 0) {
			return ServiceResponse.createBySuccessData(map);
		} else {
			System.out.println(1243324324);
			return ServiceResponse.createByErrorMessage("请求失败");
		}

	}

	public ServiceResponse<Map<String, Object>> runUploadFile(String Token, String url) {
		System.out.println("r1");
		Map<String, Object> map = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder().header("Authorization", "Token " + Token)
						.header("Accept", "application/json").header("indent", "4").url(url).build();
				Response response = null;
				try {
					response = client.newCall(request).execute();
					if (response.isSuccessful()) {
						map.put("data", response.body().string());
					}
//					System.out.println(response.body().string());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "fileUpload请求线程");
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		if (map.size() > 0) {
			System.out.println("r2");
			return ServiceResponse.createBySuccessData(map);
		} else {
			System.out.println(13);
			return ServiceResponse.createByErrorMessage("请求失败");
		}
	}

	public ServiceResponse<Map<String, Object>> uploadFile(String Token, String url) {
		System.out.println(11);
		Map<String, Object> map = new HashMap<>();
		OkHttpClient client = new OkHttpClient();
		System.out.println(12);
		Request request = new Request.Builder().header("Authorization", "Token " + Token)
				.header("Accept", "application/json").header("indent", "4").url(url).build();
		Response response = null;
		System.out.println(13);
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				map.put("data", response.body().string());
			}
			System.out.println(response.body().string());
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		;
		System.out.println(14);
		if (map.size() > 0) {
			System.out.println(15);
			return ServiceResponse.createBySuccessData(map);
		} else {
			System.out.println(16);
			return ServiceResponse.createByErrorMessage("请求失败");
		}
	}

	public ServiceResponse<Map<String, Object>> run(String url, String Token) {

		Map<String, Object> map = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder().header("Authorization", "Token " + Token)
						.header("Accept", "application/json").header("indent", "4").url(url).build();
				Response response = null;
				String backStr = null;
				try {
					response = client.newCall(request).execute();
					backStr = response.body().string();
					System.out.println("backStr" + backStr);
					map.put("data", backStr);
					if (response.isSuccessful()) {
						map.put("flag", "1");
					} else {
						map.put("flag", "0");
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
			e.printStackTrace();
		}
		if (map.get("flag").equals("1")) {
			return ServiceResponse.createBySuccessData(map);
		} else {
			return ServiceResponse.createByErrorMessage(map.get("data").toString());
		}
	}

	public ServiceResponse<Map<String, Object>> deleteRun(Map<String, String> tempMap, String url, String Token) {

		Map<String, Object> map = new HashMap<>();

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();
				Object json = JSONObject.toJSON(tempMap);
				if (json != null) {
					System.out.println(json.toString());
				}
				MediaType JSON = MediaType.parse("application/json; charset=utf-8");
				RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
				Request request = new Request.Builder().header("Authorization", "Token " + Token).url(url)
						.delete(requestBody).build();
				Response response = null;
				String backStr = null;
				try {
					response = client.newCall(request).execute();
					backStr = response.body().string();
					System.out.println("backStr" + backStr);
					map.put("data", backStr);
					if (response.isSuccessful()) {
						map.put("flag", "1");
					} else {
						map.put("flag", "0");
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
			e.printStackTrace();
		}
		if (map.get("flag").equals("1")) {
			return ServiceResponse.createBySuccessData(map);
		} else {
			return ServiceResponse.createByErrorMessage(map.get("data").toString());
		}
	}

}
