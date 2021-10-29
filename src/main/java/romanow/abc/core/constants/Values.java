package romanow.abc.core.constants;

import romanow.abc.core.UniException;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.User;
import romanow.lep500.LEP500Params;


public class Values extends ValuesBase {
    // 1. Константы наследуются (аннотации)
    // 2. Массивы строк перекрываются
    // 3. статическая инициализация наследуется
    private final static int LEP500ReleaseNumber=3;                  // номер сборки сервера
    //----------- Данные ПЛК вне мета-системы -------------------------------------
    public final static String ESSStateIcon[]={"/ess_none.png","/ess_off.png","/ess_on.png"};
    //-----------------------------------------------------------------------------
    public static void init() {
        ValuesBase.init();
        }
    public final static int PopupMessageDelay=6;                // Тайм-аут всплывающего окна
    public final static int PopupLongDelay=20;                  // Тайм-аут всплывающего окна
    private  final static String lep500ClassNames[]={
            "romanow.abc.core.constants.Values",
            "romanow.abc.core.entity.subjectarea.WorkSettings",
            "romanow.abc.dataserver.LEP500DataServer",
            "romanow.abc.dataserver.LEP500ConsoleServer",
            "romanow.abc.desktop.LEP500Cabinet",
            "romanow.abc.desktop.LEP500Client",
            "",""};
    private  final static String lep500AppNames[]={
            "lep500",
            "lep500",
            "lep500",
            "lep500",
            "LEP500.apk",
            "LEP500Dataserver.jar",
            "romanow.abc.desktop.module",
            "/drawable/lep500min.png",
            "Опоры России"
            };
    static {
        env = new I_Environment() {
            @Override
            public String applicationClassName(int classType) {
                return lep500ClassNames[classType];
                }
            @Override
            public String applicationName(int nameNype) {
                return lep500AppNames[nameNype];
            }
            @Override
            public User superUser() {
                return new User(UserSuperAdminType, "Система", "", "", "ESSDataserver", "pi31415926","9130000000");
                }
            @Override
            public Class applicationClass(int classType) throws UniException {
                return createApplicationClass(classType,lep500ClassNames);
                }
            @Override
            public Object applicationObject(int classType) throws UniException {
                return createApplicationObject(classType,lep500ClassNames);
                }
            @Override
            public int releaseNumber() { return LEP500ReleaseNumber; }
            @Override
            public WorkSettingsBase currentWorkSettings() { return new WorkSettings(); }
            };
        EntityFactory.put(new TableItem("Настройки", WorkSettings.class));
        EntityFactory.put(new TableItem("Измерение", MeasureFile.class));
        EntityFactory.put(new TableItem("Опора", Support.class).add("name"));
        EntityFactory.put(new TableItem("Линия", PowerLine.class).add("name"));
        EntityFactory.put(new TableItem("Параметры", LEP500Params.class));
        PrefixMap.put("MeasureFile.importDate","i");
        PrefixMap.put("MeasureFile.measureDate","m");
        PrefixMap.put("MeasureFile.gps","g");
        /*
        EntityFactory.put(new TableItem("Мета:Внешняя подсистема", MetaExternalSystem.class));
        EntityFactory.put(new TableItem("Мета:Подсистемы", MetaSubSystem.class));
        EntityFactory.put(new TableItem("Мета:Состояние", MetaState.class).add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("Мета:Источник данных", MetaDataRegister.class).add("regNum").add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("Мета:Команда", MetaCommand.class).add("MetaCommandRegister"));
        EntityFactory.put(new TableItem("Мета:Регистр команд", MetaCommandRegister.class).add("regNum").add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("Мета:Уставка", MetaSettingRegister.class).add("regNum").add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("Мета:Бит", MetaBit.class).add("MetaBitRegister"));
        EntityFactory.put(new TableItem("Мета:Битовый регистр", MetaBitRegister.class).add("regNum").add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("ЧМИ: форма", MetaGUIForm.class).add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("ЧМИ: элемент", MetaGUIElement.class).add("register").add("MetaGUIForm"));
        EntityFactory.put(new TableItem("Конфигурация СНЭ", PLMConfig.class).add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("Параметр конфигурации", PLMConfigValue.class));
        EntityFactory.put(new TableItem("Событие СНЭ", ArchESSEvent.class));
        //EntityFactory.put(new TableItem("ПД - значение", ArchStreamDataValue.class).noExportXLS().add("ArchStreamDataSet"));
        EntityFactory.put(new TableItem("Регистры ПЛК", PLMRegisterValue.class).add("regNum"));
        EntityFactory.put(new TableItem("Авария-бит", FailureBit.class));
        EntityFactory.put(new TableItem("Авария-уставка", FailureSetting.class));
        EntityFactory.put(new TableItem("ПД - группа", ArchStreamDataSet.class).disableExportXLS());
        EntityFactory.put(new TableItem("ПД - период", ArchStreamPeriod.class).disableExportXLS());
        EntityFactory.put(new TableItem("Источник файлов", MetaFileModule.class).add("MetaExternalSystem"));
        EntityFactory.put(new TableItem("СНЭ", ESSNode.class));
        //------------------------------ Убраны -----------------------------------------------
        PrefixMap.put("MetaBit.beginTime","b");
        PrefixMap.put("MetaBit.endTime","e");
        PrefixMap.put("PLMConfig.createDate","c");
        PrefixMap.put("PLMConfig.changeDate","d");
        PrefixMap.put("ArchStreamDataSet.createTime","c");
        PrefixMap.put("ArchESSEvent.arrivalTime","a");
        PrefixMap.put("ArchESSEvent.endTime","e");
        PrefixMap.put("FailureBit.arrivalTime","a");
        PrefixMap.put("FailureBit.endTime","e");
        PrefixMap.put("FailureSetting.arrivalTime","a");
        PrefixMap.put("FailureSetting.endTime","e");
        PrefixMap.put("ESSNode.stateTestTime","s");
        PrefixMap.put("ESSNode.innerTestTime","i");
         */
       }
    //------------- Типы заключений о состоянии опоры --------------------------------------
    @CONST(group = "MState", title = "Не определено")
    public final static int MSUndefined = 0;
    @CONST(group = "MState", title = "Норма")           // Единственный пик
    public final static int MSNormal = 1;
    @CONST(group = "MState", title = "Зашумлено")       // После фильтрации нет пиков
    public final static int MSNoise = 2;
    @CONST(group = "MState", title = "Слабый сигнал")   // Низкий уровень относительного мнимого
    public final static int MSLowLevel = 3;
    @CONST(group = "MState", title = "Невыраженный пик")// Первый слабо отличается от остальных
    public final static int MSNoPeak = 4;
    @CONST(group = "MState", title = "Второй пик++")    // Второй пик рядом (сильно)
    public final static int MSSecond1 = 5;
    @CONST(group = "MState", title = "Второй пик+")     // Второй пик рядом (слабо)
    public final static int MSSecond2 = 6;
    @CONST(group = "MState", title = "Сумма пиков++")   // Первый пик не проходит интегрально (сильно)
    public final static int MSSumPeak1 = 7;
    @CONST(group = "MState", title = "Сумма пиков+")    // Первый пик не проходит интегрально (слабо)
    public final static int MSSumPeak2 = 8;
    @CONST(group = "MState", title = "Норма-")          // Первый пик проходит
    public final static int MSNormalMinus = 9;
    //------------- Типы пользователей -----------------------------------------------------
    @CONST(group = "User", title = "Аналитик")
    public final static int UserLEP500Analytic = 3;
    @CONST(group = "User", title = "Техник")
    public final static int UserLEP500Technician = 4;
    //-------------------------------------------------------------------------------------------
    public static void main(String a[]){
        Values.init();
        System.out.println(title("User", UserAdminType));
        System.out.print(constMap.toString());
        }
}
