package romanow.abc.core.API;

import romanow.abc.core.constants.OidList;
import romanow.abc.core.entity.EntityList;
import romanow.abc.core.constants.IntegerList;
import romanow.abc.core.entity.baseentityes.*;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.Account;
import retrofit2.Call;
import retrofit2.http.*;
import romanow.lep500.AnalyseResult;

import java.util.ArrayList;

public interface RestAPILEP500 {
    //==================================  API ПРЕДМЕТНОЙ ОБЛАСТИ =======================================================
    /** Добавить измерение из загруженного артефакта */
    @POST("/api/lep500/measure/add")
    Call<MeasureFile> addMeasure(@Header("SessionToken") String token, @Query("artId") long id);
    /** Анализ группы измерений */
    @POST("/api/lep500/analyse")
    Call<ArrayList<AnalyseResult>> analyse(@Header("SessionToken") String token, @Query("paramId") long paramId, @Body OidList body);
    /** Удалить БОМ-БОМ */
    @POST("/api/lep500/measure/split")
    Call <MFSelection> splitMeasure(@Header("SessionToken") String token, @Query("measureId") long measureId, @Query("size32768") boolean size32768,
            @Query("startOver") int startOver, @Query("startLevelProc") int startLevelProc, @Query("skipTimeMS") int skipTimeMS);
    /** Изменить экспертную оценку  */
    @POST("/api/lep500/measure/expertnote/set")
    Call<JEmpty> setExpertNote(@Header("SessionToken") String token, @Query("measureId") long id, @Query("note") int note);
    @GET("/api/lep500/measure/select")
    Call<ArrayList<MeasureFile>> getMeasureSelection(@Header("SessionToken") String token, @Query("note") int note,
        @Query("userId") long usetId, @Query("line") String line, @Query("support") String support);
    }
