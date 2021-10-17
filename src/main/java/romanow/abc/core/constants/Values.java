package romanow.abc.core.constants;

import romanow.abc.core.UniException;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.User;


public class Values extends ValuesBase {
    // 1. Константы наследуются (аннотации)
    // 2. Массивы строк перекрываются
    // 3. статическая инициализация наследуется
    private final static int ESSReleaseNumber=31;                  // номер сборки сервера
    //----------- Данные ПЛК вне мета-системы -------------------------------------
    public final static String ESSStateIcon[]={"/ess_none.png","/ess_off.png","/ess_on.png"};
    //-----------------------------------------------------------------------------
    public static void init() {
        }
    public final static int PopupMessageDelay=6;                // Тайм-аут всплывающего окна
    public final static int PopupLongDelay=20;                  // Тайм-аут всплывающего окна
    private  final static String ess2ClassNames[]={
            "romanow.abc.core.constants.Values",
            "romanow.abc.core.entity.subjectarea.WorkSettings",
            "romanow.abc.dataserver.LEP500DataServer",
            "romanow.abc.dataserver.LEP500ConsoleServer",
            "romanow.abc.desktop.LEP500Cabinet",
            "romanow.abc.desktop.LEP500Client",
            "",""};
    private  final static String ess2AppNames[]={
            "LEP500",
            "LEP500",
            "LEP500",
            "LEP500",
            "LEP500.apk",
            "LEP500Dataserver.jar",
            "romanow.abc.desktop.module",
            "/drawable/battery.png",
            "СМУ СНЭ (v2)"
            };
    static {
        env = new I_Environment() {
            @Override
            public String applicationClassName(int classType) {
                return ess2ClassNames[classType];
                }
            @Override
            public String applicationName(int nameNype) {
                return ess2AppNames[nameNype];
            }
            @Override
            public User superUser() {
                return new User(UserSuperAdminType, "Система", "", "", "ESSDataserver", "pi31415926","9130000000");
                }
            @Override
            public Class applicationClass(int classType) throws UniException {
                return createApplicationClass(classType,ess2ClassNames);
                }
            @Override
            public Object applicationObject(int classType) throws UniException {
                return createApplicationObject(classType,ess2ClassNames);
                }
            @Override
            public int releaseNumber() { return ESSReleaseNumber; }
            @Override
            public WorkSettingsBase currentWorkSettings() { return new WorkSettings(); }
            };
        EntityFactory.put(new TableItem("Настройки", WorkSettings.class));
        EntityFactory.put(new TableItem("Данные датчик", MeasureFile.class));
        EntityFactory.put(new TableItem("Опора", MeasureGroup.class));
        EntityFactory.put(new TableItem("Линия", PowerLine.class));
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
        PrefixMap.put("ESSNode.streamDataTime","d");
        PrefixMap.put("ArchStreamPeriod.startServerTime","b");
        PrefixMap.put("ArchStreamPeriod.shutDownServerTime","e");
        PrefixMap.put("ArchStreamPeriod.firstStreamTime","f");
        PrefixMap.put("ArchStreamPeriod.lastStreamTime","l");
         */
       }
    //-----------------------------------------------------------------------------------
    @CONST(group = "GUIType", title = "Надпись")
    public final static int GUILabel=0;                 // Надпись
    @CONST(group = "GUIType", title = "Группа состояний")
    public final static int GUIMultiBitState=1;         // Состояние по группе разрядов
    @CONST(group = "GUIType", title = "Данные")
    public final static int GUIData=2;                  // Регистр - источник данных
    @CONST(group = "GUIType", title = "Уставка")
    public final static int GUISetting=3;               // Уставка
    @CONST(group = "GUIType", title = "Команда")
    public final static int GUIButton=5;                // Команда
    @CONST(group = "GUIType", title = "Флажок(2)")
    public final static int GUI2StateBox=6;             // Цветовой индикатор - 2 состояния
    @CONST(group = "GUIType", title ="Флажок(3)")
    public final static int GUI3StateBox=7;             // Цветовой индикатор - 3 состояния
    @CONST(group = "GUIType", title = "Флажок(3-)")
    public final static int GUI3StateBoxSmall=8;        // Цветовой индикатор - 3 состояния (малый)
    @CONST(group = "GUIType", title = "Состояние")
    public final static int GUIStateSet=9;              // Регистр состояний
    @CONST(group = "GUIType", title = "Время")
    public final static int GUITimeSetting=10;          // Индикатор - время
    @CONST(group = "GUIType", title = "Кнопка-форма")
    public final static int GUIFormButton=11;           // Прямой переход на форму
    @CONST(group = "GUIType", title ="Селектор формы")
    public final static int GUIFormSelector=12;         // Выбор формы для групповых элементов
    @CONST(group = "GUIType", title = "Уровень")
    public final static int GUILevelIndicator=15;       // Индикатор уровня
    @CONST(group = "GUIType", title = "Мультиуровень")
    public final static int GUILevelMultiIndicator=16;  // Мультииндикатор уровня
    @CONST(group = "GUIType", title = "Флажок(2-)")
    public final static int GUI2StateBoxSmall=17;       // Цветовой индикатор - 2 состояния (малый)
    @CONST(group = "GUIType", title = "Надпись-данные")
    public final static int GUIDataLabel=19;            // Надпись с форматированным значением
    @CONST(group = "GUIType", title = "Настройка (int)")
    public final static int GUIESSSettingInt=20;        // Параметр настройки сервера - целое
    @CONST(group = "GUIType", title = "Настройка (String)")
    public final static int GUIESSSettingString=21;     // Параметр настройки сервера - строка
    @CONST(group = "GUIType", title = "Настройка (bool)")
    public final static int GUIESSSettingBoolean=22;    // Параметр настройки сервера - логическое
    //-------------- Форматы регистров -----------------------
    @CONST(group = "ValueType", title = "Маш.слово")
    public final static int BitSet = 0;         // Не определен (Битовый, команд)
    @CONST(group = "ValueType", title = "Целое 16")
    public final static int IntValue = 1;       // Целое со знаком 16 разрядов
    @CONST(group = "ValueType", title = "Вещ. 32")
    public final static int FloatValue = 2;     // Вещественное 32 разряда
    @CONST(group = "ValueType", title = "Целое 32")
    public final static int Int32Value = 3;     // Целое со знаком 32 разряда
    @CONST(group = "ValueType", title = "Без знака 16")
    public final static int UIntValue = 4;      // Беззнаковое целое 16 разрядов
    //-------------- Типы регистров -------------------------
    @CONST(group = "RegType", title = "Команда/состояние")
    public final static int RegCommandState=0;  // Команда/состояние
    @CONST(group = "RegType", title = "Битовый")
    public final static int RegBitSet=1;        // Битовый
    @CONST(group = "RegType", title = "Источник")
    public final static int RegData=2;          // Источник данных
    @CONST(group = "RegType", title = "Уставка")
    public final static int RegSetting=3;       // Уставка
    public final static String formatNames[] = {"bitSet", "int16", "float16","int32"};
    //------------- Типы и подтипы событий -----------------
    @CONST(group = "EventType", title = "Смена состояния")
    public final static int EventState=5;           // Смена состояния
    @CONST(group = "EventType", title = "Авария")
    public final static int EventFailure=6;         // Авария/предупреждение
    @CONST(group = "EventType", title = "Команда ПЛК")
    public final static int EventCommand=7;         // Выполение команды ПЛК
    @CONST(group = "EventType", title = "Уставка")
    public final static int EventSetting=8;         // Запись уставки ПЛК
    @CONST(group = "EventType", title = "Файл-артефакт")
    public final static int EventFile=9;            // Источник файлов-артефактов
    //------------- Подтипы событий ----------------- EventFailure
    @CONST(group = "EventType", title = "ДС регистровое")
    public final static int EventDEStateReg=11;     // Дискретное - регистр состояний
    @CONST(group = "EventType", title = "ДС битовое")
    public final static int EventDEBitReg=12;       // Дискретное - битовый регистр (бит)
    @CONST(group = "EventType", title = "Авария-бит")
    public final static int EventFailBitReg=13;     // Авария - битовый регистр (бит)
    @CONST(group = "EventType", title = "Авария-уставка")
    public final static int EventFailSettingReg=14;
    //--------------- Типы потоковых данных для источников данных
    @CONST(group = "DataStream", title = "")
    public final static int DataStreamNone=0;       //
    @CONST(group = "DataStream", title = "Частый")
    public final static int DataStreamFrequent=1;   // Частый
    @CONST(group = "DataStream", title = "Редкий")
    public final static int DataStreamRare=2 ;      // Редкий
    @CONST(group = "DataStream", title = "Суточный")
    public final static int DataStreamDayly=3 ;     // 1 раз в сутки
    //---------------- Типы сжатия ------------------
    @CONST(group = "CompressMode", title = "")
    public final static int CompressModeNone=0;     // Без сжатия
    @CONST(group = "CompressMode", title = "ZIP")
    public final static int CompressModeZIP=1;      // ZIP
    @CONST(group = "CompressMode", title = "Битовый формат")
    public final static int CompressModeBit=2;      // Битовый формат
    @CONST(group = "CompressMode", title = "Битовый - приращения")
    public final static int CompressModeBitDelta=3; // Битовый формат - приращения
    //---------------- Типы битовых регистров
    public final static int BitSetNone=0;
    public final static int BitSetFailure=1;        // Аварии
    public final static int BitSetDEvent=2;         // Дискретные события
    public final static int BitSetMixed=3;          // Смешанный 1 и 2
    //---------------- Типы битов
    @CONST(group = "BitType", title = "")
    public final static int BitNone=0;
    @CONST(group = "BitType", title = "Предупреждения")
    public final static int BitWarning=1;           // Предупреждения
    @CONST(group = "BitType", title = "Аварии")
    public final static int BitFailure=2;           // Аварии
    //---------------- Типы аварий по превышению уставок
    @CONST(group = "SettingOver", title = "")
    public final static int SettingNone=0;          // Нет аварии
    @CONST(group = "SettingOver", title = "Предупреждение")
    public final static int SettingWarning=1;       // Предупреждения
    @CONST(group = "SettingOver", title = "Авария")
    public final static int SettingFailure=2;       // Аварии
    //--------------- Типы изменений конфигурации ----------------
    public final static int ConfigChangeSetting=0;  // Изменение уставки
    //------------- Типы пользователей -----------------------------------------------------
    @CONST(group = "User", title = "Сервисный инженер")
    public final static int UserESSServiceEngeneer = 3;
    @CONST(group = "User", title = "Инженер")
    public final static int UserESSEngeneer = 4;
    @CONST(group = "User", title = "Оператор")
    public final static int UserESSOperator = 5;
    @CONST(group = "AccessLevel", title = "Разработчик")
    public final static int AccessLevel0=0;    // Любые операции, игнорируется запрет удаленных
    @CONST(group = "AccessLevel", title = "Сервисный инж.")
    public final static int AccessLevel1=1;    // Уровень доступа сервисного инженера
    @CONST(group = "AccessLevel", title = "Инженер")
    public final static int AccessLevel2=2;    // Уровень доступа инженера
    @CONST(group = "AccessLevel", title = "Оператор")
    public final static int AccessLevel3=3;    // Уровень доступа оператора
    @CONST(group = "AccessLevel", title = "Наблюдатель")
    public final static int AccessLevel4=4;    // Уровень доступа наблюдателя
    public final static int AccessLevelForUserType[]={
            AccessLevel4,AccessLevel0,AccessLevel4,AccessLevel1,AccessLevel2,AccessLevel3};
    //-------------------------------------------------------------------------------------------
    public static void main(String a[]){
        Values.init();
        System.out.println(title("User", UserAdminType));
        System.out.println(title("AccessLevel",Values.AccessLevel2));
        System.out.print(constMap.toString());
        }
}
