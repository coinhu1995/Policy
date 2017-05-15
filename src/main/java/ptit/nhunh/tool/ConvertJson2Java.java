package ptit.nhunh.tool;

import com.google.gson.Gson;

import ptit.nhunh.model.ResponseObject;

public class ConvertJson2Java {
	public ResponseObject convert(String json) {
		ResponseObject data = new Gson().fromJson(json, ResponseObject.class);
		return data;
	}
}
