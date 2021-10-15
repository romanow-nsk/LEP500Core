package romanow.abc.core.API;

import com.google.gson.Gson;
import romanow.abc.core.DBRequest;
import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.Values;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.baseentityes.JLong;
import romanow.abc.core.entity.users.Account;
import romanow.abc.core.entity.users.User;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RestAPIProxy  implements I_APIConnector{
    private RestAPIESS2 api;
    private boolean on=false;
    private RestAPIBase service=null;
    public abstract class APICall<T> {
        public abstract Call<T> apiFun();
        //---------------------------------------------------------------------------------------------------
        public APICall(final I_APICallBack back) {
            try {
                if (!on) {
                    back.onError(ValuesBase.HTTPAuthorization, "Не стартанул клиент", null);
                    return;
                }
                long tt = System.currentTimeMillis();
                Response<T> res = apiFun().execute();
                if (!res.isSuccessful()) {
                    if (res.code() == Values.HTTPAuthorization) {
                        back.onException(UniException.user("Сеанс закрыт"));
                    } else {
                        back.onError(res.code(), res.message(), res.errorBody());
                    }
                    return;
                }
                //System.out.println("time=" + (System.currentTimeMillis() - tt) + " мс");
                back.onSuccess(res.body());
            } catch (Exception ex) {
                back.onException(ex);
                Utils.printFatalMessage(ex);
            }
        }
    }
    //--------------------------------------------------------------------------------
    public RestAPIProxy(String ip, String port) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + ":" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(RestAPIBase.class);
        on = true;
        }
   //---------------------------------------------------------------------------------
    @Override
    public void addEntity(final String token, final DBRequest body, final int level, final I_APICallBack back) {
        new APICall<JLong>(back){
            @Override
            public Call<JLong> apiFun() {
                return service.addEntity(token,body,level);
                }
            };
        }
    @Override
    public void getEntity(final String token, final String className, final long id, final int level, final I_APICallBack<Entity> back) {
        final I_APICallBack<DBRequest> back2 = new I_APICallBack<DBRequest>() {
            @Override
            public void onSuccess(DBRequest result) {
                try {
                    back.onSuccess(result.get(new Gson()));
                    } catch (Exception ee){
                        back.onException(ee);
                        }
                }

            @Override
            public void onError(int code, String message, ResponseBody body) {
                back.onError(code,message,body); }
            @Override
            public void onException(Exception ee) {
                back.onException(ee);}
            };
        new APICall<DBRequest>(back2){
            @Override
            public Call<DBRequest> apiFun() {
                return service.getEntity(token,className,id,level);
                }
            };
        }

    @Override
    public void ping(I_APICallBack back) {
        new APICall<JEmpty>(back){
            @Override
            public Call<JEmpty> apiFun() {
                return service.ping();
            }
            };
        }

    @Override
    public void login(final Account account, I_APICallBack<User> back) {
        new APICall<User>(back){
            @Override
            public Call<User> apiFun() {
                return service.login(account);
            }
        };
    }

    //-------------------------------------------------------------------------------------------
    abstract static class Back<T> implements  I_APICallBack<T> {
        @Override
        public void onError(int code, String message, ResponseBody body) {
            System.out.println(""+code+" "+message);
            }
        @Override
        public void onException(Exception ee) {
            System.out.println(ee.toString());
            }
        }
    static User user;
    static String token;
    static RestAPIProxy proxy;
    public static void main(String ss[]){
        Values.init();          // Для локальной фабрики
        //proxy = new RestAPIProxy("217.71.138.9","4567");
        proxy = new RestAPIProxy("localhost","4567");
        Account account = new Account("Гость","9335555555","112233");
        proxy.login(account, new Back<User>() {
            @Override
            public void onSuccess(User result) {
                user = result;
                token = user.getSessionToken();
                }
            });
        }
}
