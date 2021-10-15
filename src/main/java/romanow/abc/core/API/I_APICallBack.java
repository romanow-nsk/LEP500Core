package romanow.abc.core.API;

import okhttp3.ResponseBody;

public interface I_APICallBack<T> {
    public void onSuccess(T result);
    public void onError(int code, String message, ResponseBody body);
    public void onException(Exception ee);
}
