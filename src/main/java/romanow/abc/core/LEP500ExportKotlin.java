package romanow.abc.core;

import romanow.abc.core.API.RestAPILEP500;
import romanow.abc.core.ErrorList;
import romanow.abc.core.constants.Values;
import romanow.abc.core.mongo.ExportKotlin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class LEP500ExportKotlin {
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String aa[]) {
        Values.init();
        ErrorList errorList = new ErrorList();
        ExportKotlin.createKotlinClassSources(errorList);
        ExportKotlin.createJSAPIFile(RestAPILEP500.class, errorList);
        try {
            System.out.println(errorList.toString());
            File ff = new File("ExportKotlinErrors.kt");
            ff.delete();
            if (errorList.valid())
                return;
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("ExportKotlinErrors.kt"), "UTF-8");
            out.write(errorList.toString());
            out.flush();
            out.close();
        } catch (Exception ee) {
            System.out.println("ExportKotlinErrors.kt: " + ee.toString());
        }
    }
}
