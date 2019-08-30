package com.henu.seafile.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostMethod {
	OkHttpClient client = null;

	public PostMethod() {
		client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
				.writeTimeout(15, TimeUnit.SECONDS).build();
	}

	public ServiceResponse<Map<String, Object>> run(Map<String, String> map, String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		// 如果这个线程没有启动成功呢
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					OkHttpClient client = new OkHttpClient();
					Object json = JSONObject.toJSON(map);
					System.out.println(json.toString());
					MediaType JSON = MediaType.parse("application/json; charset=utf-8");
					RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

					Request request = new Request.Builder().header("Authorization", "Token " + Token)
							.header("Accept", "application/json").header("indent", "4").url(url).post(requestBody)
							.build();
					try {
						Response response = client.newCall(request).execute();
						String backStr = response.body().string();
						System.out.println("\n" + backStr + "\n");
						tempMap.put("data", backStr);// 不论是否上传成功都将返回信息拿出来
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
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}

		// 说明线程启动有问题
		if (tempMap.size() == 0) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}
		if (tempMap.get("flag").equals("1")) {// 复制成功
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage(tempMap.get("data").toString());
		}
	}

	// 异步请求
	public ServiceResponse<Map<String, Object>> runAsync(Map<String, String> map, String url, String Token)
			throws IOException {
		Map<String, Object> tempMap = new HashMap<>();

		Object json = JSONObject.toJSON(map);
		System.out.println(json.toString());
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

		Request request = null;
		if (Token == null) {
			request = new Request.Builder().url(url).post(requestBody).build();
		} else {
			request = new Request.Builder().header("Authorization", "Token " + Token)
					.header("Content-Type", "application/json").url(url).post(requestBody).build();
		}
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful())
					tempMap.put("data", response.body().string());
			}

			@Override
			public void onFailure(Call call, IOException e) {

			}
		});
		if (tempMap.size() > 0) {
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage("请求失败");
		}
	}

	/*
	 * 同步请求上传文件
	 */
	public ServiceResponse<Map<String, Object>> fileUploadRun(File file, String url, String filePath) {
		System.out.println("what");
		Map<String, Object> tempMap = new HashMap<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();

				RequestBody requestBody = new MultipartBody.Builder()
						.setType(MultipartBody.FORM).addFormDataPart("parent_dir", filePath).addFormDataPart("file",
								file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
						.build();

				Request request = new Request.Builder().url(url).post(requestBody).build();
				try {
					Response response = client.newCall(request).execute();
					String backStr = response.body().string();
					tempMap.put("data", backStr);// 不论是否上传成功都将返回信息拿出来
					if (response.isSuccessful()) {
						System.out.println("上传成功后的文件序列号：" + backStr);
						tempMap.put("flag", "1");// 表示上传成功
						delete(file);// 删除项目路径下的文件
					} else {
						System.out.println("上传失败" + backStr);
						tempMap.put("flag", "0");// 表示上传失败
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
		System.out.println("judge");
		if (tempMap.get("flag").equals("1")) {// 说明上传成功了
			System.out.println("success to upload the file");
			return ServiceResponse.createBySuccessMessageAndData("上传成功", tempMap);
		} else {
			System.out.println("error to upload file");
			return ServiceResponse.createByErrorMessage("上传文件失败" + tempMap.get("data"));
		}

	}

	private static void delete(File file) {
		if (!file.exists())
			return;

		if (file.isFile() || file.list() == null) {
			file.delete();
			System.out.println("删除了" + file.getName());
		} else {
			File[] files = file.listFiles();
			for (File a : files) {
				delete(a);
			}
			file.delete();
			System.out.println("删除了" + file.getName());
		}

	}

	/*
	 * 拷贝文件
	 */
	public ServiceResponse<Map<String, Object>> fileCopyRun(Map<String, String> map, String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		// 如果这个线程没有启动成功呢
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					OkHttpClient client = new OkHttpClient();
					Object json = JSONObject.toJSON(map);
					System.out.println(json.toString());
					MediaType JSON = MediaType.parse("application/json; charset=utf-8");
					RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

					Request request = new Request.Builder().header("Authorization", "Token " + Token)
							.header("Accept", "application/json").header("indent", "4").url(url).post(requestBody)
							.build();
					try {
						Response response = client.newCall(request).execute();
						String backStr = response.body().string();
						System.out.println("\n" + backStr + "\n");
						tempMap.put("data", backStr);// 不论是否上传成功都将返回信息拿出来
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
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}

		// 说明线程启动有问题
		if (tempMap.size() == 0) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}
		if (tempMap.get("flag").equals("1")) {// 复制成功
			System.out.println("success to copy the file");
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			System.out.println("PM:error to copy the file");
			return ServiceResponse.createByErrorMessage(tempMap.get("data").toString());
		}

	}

	public ServiceResponse<Map<String, Object>> groupRun(Map<String, String> map, String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		// 如果这个线程没有启动成功呢
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					OkHttpClient client = new OkHttpClient();
					Object json = JSONObject.toJSON(map);
					System.out.println(json.toString());
					MediaType JSON = MediaType.parse("application/json; charset=utf-8");
					RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

					Request request = new Request.Builder().header("Authorization", "Token " + Token).url(url)
							.post(requestBody).build();
					try {
						Response response = client.newCall(request).execute();
						String backStr = response.body().string();
						System.out.println("\n" + backStr + "\n");
						tempMap.put("data", backStr);// 不论是否上传成功都将返回信息拿出来
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
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}

		// 说明线程启动有问题
		if (tempMap.size() == 0) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}
		if (tempMap.get("flag").equals("1")) {// 复制成功
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage(tempMap.get("data").toString());
		}

	}
	
	public ServiceResponse<Map<String, Object>> directoryRun(Map<String, String> map, String url, String Token) {

		Map<String, Object> tempMap = new HashMap<>();
		// 如果这个线程没有启动成功呢
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					OkHttpClient client = new OkHttpClient();

					FormBody.Builder builder = new FormBody.Builder();
					for (Map.Entry<String, String> entry : map.entrySet()) {
						builder.add(entry.getKey(), entry.getValue());
					}
					RequestBody requestBody = builder.build();

					Request request = new Request.Builder().header("Authorization", "Token " + Token).url(url)
							.post(requestBody).build();
					try {
						Response response = client.newCall(request).execute();
						String backStr = response.body().string();
						System.out.println("\n" + backStr + "\n");
						tempMap.put("data", backStr);// 不论是否上传成功都将返回信息拿出来
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
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}

		// 说明线程启动有问题
		if (tempMap.size() == 0) {
			return ServiceResponse.createByErrorMessage("线程启动异常");
		}
		if (tempMap.get("flag").equals("1")) {// 复制成功
			return ServiceResponse.createBySuccessData(tempMap);
		} else {
			return ServiceResponse.createByErrorMessage(tempMap.get("data").toString());
		}

	}

}
