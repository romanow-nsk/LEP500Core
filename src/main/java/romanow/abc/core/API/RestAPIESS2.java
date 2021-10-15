package romanow.abc.core.API;

import romanow.abc.core.entity.EntityList;
import romanow.abc.core.constants.IntegerList;
import romanow.abc.core.entity.baseentityes.*;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.Account;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface RestAPIESS2 {
    //==================================  API ПРЕДМЕТНОЙ ОБЛАСТИ =======================================================
    /** Импортировать мета-данные из файла-артефакта */
    @POST("/api/ess/meta/import")
    Call<JString> importMetaDataXls(@Header("SessionToken") String token, @Query("pass") String pass,@Query("id") long id);
    /** Перезагрузить мета-данные сервера */
    @POST("/api/ess/meta/reload")
    Call<JEmpty> reloadMetaData(@Header("SessionToken") String token);
    /** Установить соединение с ПЛК */
    @POST("/api/ess/plm/connect")
    Call<JEmpty> connectToPLM(@Header("SessionToken") String token,@Query("id") long id);
    /** Разорвать соединение с ПЛК */
    @POST("/api/ess/plm/disconnect")
    Call<JEmpty> disconnectFromPLM(@Header("SessionToken") String token);
    /** Проверить наличие соединения с ПЛК */
    @GET("/api/ess/plm/ready")
    Call<JBoolean> isPLMReady(@Header("SessionToken") String token);
    /** Прочитать поле регистров ПЛК */
    @GET("/api/ess/registers/values/read")
    Call<ArrayList<Integer>> readRegistersValues(@Header("SessionToken") String token);
    /** Прочитать регистр ПЛК по номеру */
    @GET("/api/ess/register/value/read")
    Call<JInt> readRegisterValue(@Header("SessionToken") String token,@Query("regnum") int regnum);
    /** Записать регистр ПЛК по номеру */
    @POST("/api/ess/register/value/write")
    Call<JEmpty> writeRegisterValue(@Header("SessionToken") String token,@Query("regnum") int regnum,@Query("value") int value);
    /** Записать уставку ПЛК по номеру регистра */
    @POST("/api/ess/setting/write")
    Call<JEmpty> writeSettingValue(@Header("SessionToken") String token,@Query("regnum") int regnum,@Query("value") int value);
    /** Записать команду ПЛК по номеру регистра */
    @POST("/api/ess/command/write")
    Call<JEmpty> writeCommandValue(@Header("SessionToken") String token,@Query("regnum") int regnum,@Query("value") int value);
    /** Прочитать регистры ПЛК по заданному списку */
    @POST("/api/ess/registers/list/read")
    Call<IntegerList> readRegistersValues(@Header("SessionToken") String token, @Body IntegerList list);
    /** Квитировать аварию */
    @POST("/api/ess/failure/quit")
    Call<JEmpty> quitFailure(@Header("SessionToken") String token, @Query("setting") boolean setting, @Query("regnum") int regNum, @Query("bitnum") int bitNum);
    /** Квитировать все аварии */
    @POST("/api/ess/failure/quit/all")
    Call<JEmpty> quitAllFailures(@Header("SessionToken") String token);
    /** Добавить конфигурацию уставок  */
    @POST("/api/ess/config/add")
    Call<JLong> addConfig(@Header("SessionToken") String token, @Query("title") String title,@Query("comment") String comment);
    /** Удалить конфигурацию уставок  */
    @POST("/api/ess/config/remove")
    Call<JEmpty> removeConfig(@Header("SessionToken") String token, @Query("id") long id);
    /** Записать конфигурацию уставок в ПЛК */
    @POST("/api/ess/config/set")
    Call<JEmpty> setConfig(@Header("SessionToken") String token, @Query("id") long id);
    /** Получить данные аккаунта авторизованного пользователя */
    @GET("/api/user/account/get")
    Call<Account> getOwnAccount(@Header("SessionToken") String token);
}
