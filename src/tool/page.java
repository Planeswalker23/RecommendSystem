package tool;

public class page {
	private NewsList data;
	
	
	public NewsList getData() {
		return data;
	}
	public void setData(NewsList data) {
		this.data = data;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private String errorCode;
	private boolean success;
	private String msg;
}
