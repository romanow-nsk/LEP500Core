package romanow.abc.core.API;

import romanow.abc.core.entity.EntityList;
import romanow.abc.core.constants.IntegerList;
import romanow.abc.core.entity.baseentityes.*;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.Account;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface RestAPILEP500 {
    //==================================  API ПРЕДМЕТНОЙ ОБЛАСТИ =======================================================
    /** Добавить измерение из загруженного артефакта */
    @POST("/api/lep500/measure/add")
    Call<MeasureFile> addMeasure(@Header("SessionToken") String token, @Query("artId") long id);
}
