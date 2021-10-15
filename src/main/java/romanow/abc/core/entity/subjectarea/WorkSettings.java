package romanow.abc.core.entity.subjectarea;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.base.WorkSettingsBase;

public class WorkSettings extends WorkSettingsBase {
    private int archiveDepthInDay=30;               // Глубина архива в днях
    private int streamDataPeriod=10;                // Цикл опроса потоковых данных (сек)
    private int streamDataLongPeriod=60;            // Длинный цикл опроса потоковых данных (сек)
    private int failureTestPeriod=10;               // Период обнаружения аварий (сек)
    private int GUIrefreshPeriod=10;                // Период обновления форм GUI
    private int eventTestPeriod=10;                 // Период опроса дисретных событий (сек)
    private int maxRegisterAge=100;                 // Период устаревания кэша регистров (мс)
    private boolean emulated=true;                  // Эмулятор ПЛК
    private String plmIP="10.155.2.201";           // Внутренний IP-адрес ПЛК ModbusTCP
    private int plmPort=502;                        // Порт ПЛК ModbusTCP
    private int plmTimeOut=5;                       // Тайм-аут (сек) ожидания  ModbusTCP
    private int plmRegGroupSize=80;                 // Размер группы регистров команды ModbusTCP
    private boolean mainServer=false;               // Режим главного сервера СМУ (Вступает в действие при перезагрузке)
    private int fileScanPeriod=10;                      // Цикл опроса источник файлов (сек)
    private int mainServerPeriod=20;                    // Цикл снятия данных главным сервером (сек)
    private int streamDataPeriodLimit=100;              // Лимит отсчетов потоковых данных в периоде
    private boolean plmConnected=false;                 // Состояние соединения с ПЛК
    private long metaSystemId=0;                        // Идентификатор мета-системы (соединение)
    private int compressMode= Values.CompressModeNone;  // Тип сжатия
    private int userSilenceTime=3;                      // Время "молчания" оператора (мин)
    //-------------------------------------------------------------------------------------------
    public int getUserSilenceTime() {
        return userSilenceTime; }
    public void setUserSilenceTime(int userSilenceTime) {
        this.userSilenceTime = userSilenceTime; }
    public int getCompressMode() {
        return compressMode; }
    public void setCompressMode(int compressMode) {
        this.compressMode = compressMode; }
    public int getArchiveDepthInDay() {
        return archiveDepthInDay; }
    public void setArchiveDepthInDay(int archiveDepthInDay) {
        this.archiveDepthInDay = archiveDepthInDay; }
    public int getStreamDataPeriod() {
        return streamDataPeriod; }
    public void setStreamDataPeriod(int streamDataPeriod) {
        this.streamDataPeriod = streamDataPeriod; }
    public int getStreamDataLongPeriod() {
        return streamDataLongPeriod; }
    public void setStreamDataLongPeriod(int streamDataLongPeriod) {
        this.streamDataLongPeriod = streamDataLongPeriod; }
    public int getFailureTestPeriod() {
        return failureTestPeriod; }
    public void setFailureTestPeriod(int failureTestPeriod) {
        this.failureTestPeriod = failureTestPeriod; }
    public int getGUIrefreshPeriod() {
        return GUIrefreshPeriod; }
    public void setGUIrefreshPeriod(int GUIrefreshPeriod) {
        this.GUIrefreshPeriod = GUIrefreshPeriod; }
    public int getEventTestPeriod() {
        return eventTestPeriod; }
    public void setEventTestPeriod(int eventTestPeriod) {
        this.eventTestPeriod = eventTestPeriod; }
    public int getMaxRegisterAge() {
        return maxRegisterAge; }
    public void setMaxRegisterAge(int maxRegisterAge) {
        this.maxRegisterAge = maxRegisterAge; }
    public boolean isEmulated() {
        return emulated; }
    public void setEmulated(boolean emulated) {
        this.emulated = emulated; }
    public String getPlmIP() {
        return plmIP; }
    public void setPlmIP(String plmIP) {
        this.plmIP = plmIP; }
    public int getPlmPort() {
        return plmPort; }
    public void setPlmPort(int plmPort) {
        this.plmPort = plmPort; }
    public int getPlmTimeOut() {
        return plmTimeOut; }
    public void setPlmTimeOut(int plmTimeOut) {
        this.plmTimeOut = plmTimeOut; }
    public int getPlmRegGroupSize() {
        return plmRegGroupSize; }
    public void setPlmRegGroupSize(int plmRegGroupSize) {
        this.plmRegGroupSize = plmRegGroupSize; }
    public boolean isMainServer() {
        return mainServer; }
    public void setMainServer(boolean mainServer) {
        this.mainServer = mainServer; }
    public int getFileScanPeriod() {
        return fileScanPeriod; }
    public void setFileScanPeriod(int fileScanPeriod) {
        this.fileScanPeriod = fileScanPeriod; }
    public int getMainServerPeriod() {
        return mainServerPeriod; }
    public void setMainServerPeriod(int mainServerPeriod) {
        this.mainServerPeriod = mainServerPeriod; }
    public int getStreamDataPeriodLimit() {
        return streamDataPeriodLimit; }
    public void setStreamDataPeriodLimit(int streamDataPeriodLimit) {
        this.streamDataPeriodLimit = streamDataPeriodLimit; }
    public boolean isPlmConnected() {
        return plmConnected; }
    public void setPlmConnected(boolean plmConnected) {
        this.plmConnected = plmConnected; }
    public long getMetaSystemId() {
        return metaSystemId; }
    public void setMetaSystemId(long metaSystemId) {
        this.metaSystemId = metaSystemId; }
}
