package ptit.nhunh.model;

public class ResponseObject {
	private String error;

	private Data data;

	private String errorDescription;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	@Override
	public String toString() {
		return "ClassPojo [error = " + error + ", data = " + data + ", errorDescription = " + errorDescription + "]";
	}
}
