#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#include<Wire.h>
#include <stdlib.h>
#include "SparkFun_SCD30_Arduino_Library.h"
#include<SoftwareSerial.h>
#include <Arduino.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>
SoftwareSerial UNO(0, 14);  //D3,D5

//#define ssid "D109-1" //wifi id
//#define pass "113333555555"      //wifi pass

#define ssid "iptime" //wifi id
#define pass "30813081" 

///////////SCD30//////////////
float temp = 0, humi = 0, r_lux = 0, l_lux = 0;
float temp_total = 0, humi_total = 0, co2_total = 0, r_lux_total = 0, l_lux_total = 0, top_angle_total = 0;
int temp_flag = 0, humi_flag = 0, co2_flag = 0, r_lux_flag = 0, l_lux_flag = 0, top_angle_flag =0;
int top_angle_int;
int temphumi_flag = 0;
int co2 = 0;
int delay_second = 300;  // 업로드 딜레이(s)
int temphumi_delay = 30;  // 온습도 측정 주기(s)
int co2_gen = 0;
SCD30 airSensor;
String R_LUX;
String L_LUX;
String top_angle;
WiFiClient wificlient;
WiFiClient client2;
ESP8266WiFiMulti WiFiMulti;

PubSubClient client(wificlient);

/////////////id 및 세팅값////////////////
String id = "1" ;       // 디바이스 id
int delay_flag = 0;
int k = 0;
int wifi_timeout = 0;
int mintemp = 20;
int maxtemp = 40;
int minhumid = 30;
int maxhumid = 60;
int minco2 = 200;
int maxco2 = 1200;

//#define ssid "olleh_WiFi_9DF0"
//#define pass  "0000004810"
const char* mqtt_server = "postman.cloudmqtt.com";    // port 17181
const char* outTopic = "SMFoutTopic";
const char* inTopic = "SMFinTopic";
const char* mqtt_username = "mzxzkerr";
const char* mqtt_password = "6ePJPdo2Nkr6";
String dataIn = "";
const char* pubdata = "";
int pin = 12; //디지털핀 6번 => gpio12
int pin2 = 13; //디지털핀 7번 => gpio13
int err = 0;
int sendvalue = 0;
char msg[50];
IRsend irsend(15);  //D8

void setup_wifi();
void callback(char* topic, byte* payload, unsigned int length);
void reconnect();
void Http_post();
void wifi_check();
void scd30_check();
void wifi_scan();
void flag_set();
void clear_msg();
void LUX();
void Http_get();

////////////////////////////////////////////////////////////
/////////////////////////// Setup //////////////////////////
////////////////////////////////////////////////////////////

void setup() {
  Serial.begin(115200);
  UNO.begin(57600);

  Wire.begin();   //sda scl
  airSensor.begin(Wire);
  setup_wifi();
  pinMode(LED_BUILTIN, OUTPUT);     // Initialize the LED_BUILTIN pin as an output
  digitalWrite(LED_BUILTIN, LOW);   // Turn the LED on
  pinMode(pin, OUTPUT);
  pinMode(pin2, OUTPUT);
  digitalWrite(pin2, LOW);
  client.setServer(mqtt_server, 17181);
  client.setCallback(callback);
  wifi_check();        //Wifi 상태 체크 함수
  UNO.setTimeout(5);
  //  UNO.write("connected");


}


void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  if (temphumi_flag == temphumi_delay) {
    digitalWrite(LED_BUILTIN, LOW);   // Turn the LED on
    //temp_humid(); //DHT11 함수
    LUX();
    scd30_check(); temphumi_flag = 0; WiFiMulti.run();
    digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off
  }

  if (delay_flag == delay_second) {
    if ((WiFiMulti.run() == WL_CONNECTED)) {    // wait for WiFi connection
      digitalWrite(LED_BUILTIN, LOW);   // Turn the LED on
      Http_post();
      digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off
    }
    else {
      wifi_check();
    }
    k = 0; delay_flag = 0;  temphumi_flag = 0;
    temp_total = 0; temp_flag = 0; humi_total = 0; humi_flag = 0; co2_total = 0; co2_flag = 0;
  }

  if (Serial.available()) {
    int k = 0;
    while (Serial.available()) {
      char myChar = (char)Serial.read();
      msg[k] = myChar;
      k++;
      delay(5);
    }
    Serial.println();
    Serial.print("Publish message: ");
    Serial.println(msg);
    Serial.println();
    client.publish(outTopic, msg);
    clear_msg();
  }

  
  flag_set();
}


////////////////////////////////////////////////////////////
///////////////////////////관련함수//////////////////////////
////////////////////////////////////////////////////////////

void LUX() {
   Serial.println("LUX req");
  UNO.write('s');
  delay(10);
  int x = 0;
  for (int j = 0; j < 10; j++) {
    if (UNO.available() > 0) {
      String date_LUX = UNO.readStringUntil('.');
      String R_LUX_str = date_LUX.substring(0, date_LUX.indexOf('l'));
      String L_LUX_str = date_LUX.substring(date_LUX.indexOf('l') + 1, date_LUX.indexOf(';'));
      String   top_angle_str = date_LUX.substring(date_LUX.indexOf(';')+1,date_LUX.length());
      Serial.println();

      Serial.print("[Measure] R_LUX(lux):");
      Serial.print(R_LUX_str);
      Serial.print("( before : "); Serial.print(R_LUX); Serial.print(")");

      int R_LUX_int = R_LUX_str.toInt();
      if (R_LUX_int > 0) {
        R_LUX = R_LUX_str;
      }
      r_lux_total += R_LUX.toInt();
      r_lux_flag++;
      Serial.print(", Total : "); Serial.print(r_lux_total);
      Serial.print("("); Serial.print(r_lux_flag); Serial.println(")");

      Serial.print("[Measure] L_LUX(lux):");
      Serial.print(L_LUX_str);
      Serial.print("( before : "); Serial.print(L_LUX); Serial.print(")");

      
      int L_LUX_int = L_LUX_str.toInt();
      if (L_LUX_int > 0) {
        L_LUX = L_LUX_str;
      }
      l_lux_total += L_LUX.toInt();
      l_lux_flag++;
      Serial.print(", Total : "); Serial.print(l_lux_total);
      Serial.print("("); Serial.print(l_lux_flag); Serial.println(")");
    
    
      top_angle_int = top_angle_str.toInt();

      Serial.print("[Measure] top_angle(lux):");
      Serial.print(top_angle_str);
      Serial.print("( before : "); Serial.print(top_angle); Serial.print(")");
      break;
    }
    if (j > 3) {
      Serial.println("request again");
      UNO.write('s');
      j = 0;
      x++;
    }

    if (x > 10) {
      break;
    }
    flag_set() ;
  }}
void flag_set() {
  delay_flag++;
  temphumi_flag++;
  Serial.print(delay_flag);
  Serial.print(",");
  k++;
  if ((k >= 10) && (temphumi_delay > 10)) {
    Serial.println("");
    k = 0;
  }
  delay(1000);                      // Wait
}

void clear_msg() {
  char buf[50] = "";
  for (int k = 0; k < 50; k++) {
    msg[k] = buf[k] ;
  }
}


void scd30_check() {
  if (airSensor.dataAvailable())
  {
    Serial.println();
    Serial.print("[Measure] Co2(ppm):");
    Serial.print(airSensor.getCO2());
    Serial.print("("); Serial.print(co2_gen); Serial.print(")");
    co2 = airSensor.getCO2();
    co2_total += co2;    //온도 데이터 더하기
    co2_flag++;                  //온도 데이터 더한 횟수
    Serial.print(", Total : "); Serial.print(co2_total);
    Serial.print("("); Serial.print(co2_flag); Serial.println(")");

    Serial.print("[Measure] Temperature(C):");
    Serial.print(airSensor.getTemperature(), 2);
    temp = airSensor.getTemperature();
    temp_total += temp;    //온도 데이터 더하기
    temp_flag++;                  //온도 데이터 더한 횟수
    Serial.print(", Total : "); Serial.print(temp_total);
    Serial.print("("); Serial.print(temp_flag); Serial.println(")");

    Serial.print("[Measure] Humidity(%):");
    Serial.print(airSensor.getHumidity(), 2);
    humi = airSensor.getHumidity();
    humi_total += humi;    //습도 데이터 더하기
    humi_flag++;                  //습도 데이터 더한 횟수
    Serial.print(",    Total : ");  Serial.print(humi_total);
    Serial.print("("); Serial.print(humi_flag);  Serial.println(")");
    String alarmdata = "id:" + id;
    int alarm_flag = 0;
    if ((temp < mintemp) || (temp > maxtemp)) {
      temp_total = temp, temp_flag = 1;
      alarmdata = alarmdata + ",temp:" + temp;
      alarm_flag++;
      if( temp > maxtemp){
        digitalWrite(pin2, HIGH);
        irsend.sendNEC(0xFF6897, 32, 2);//값 변경할것  sendNEC,sendSony,SendRaw,sendSamsungAC
        }
    }else if(temp < maxtemp){
        digitalWrite(pin2, LOW);
        irsend.sendNEC(0xFF6897, 32, 2);//값 변경할것
        }
    if ((humi < minhumid) || (humi > maxhumid)) {
      humi_total = humi, humi_flag = 1;
      alarmdata = alarmdata + ",humid:" + humi;
      alarm_flag++;
    }
    if ((co2 < minco2) && (co2_gen == 0) && (co2 != 0)) {
      Serial.println("[Measure] Not enough Co2 , Turn on Co2 generator");
      co2_gen = 1;
      digitalWrite(pin, HIGH);
      alarmdata = alarmdata + ",co2:" + co2;
      alarm_flag++;
    }
    if ((co2 > maxco2) && (co2_gen == 1)) {
      Serial.println("[Measure] enough Co2 , Turn off Co2 generator");
      co2_gen = 0;
      digitalWrite(pin, LOW);
      alarmdata = alarmdata + ",co2:" + co2;
      alarm_flag++;
    }

    if (alarm_flag > 0) { //알람이 있으면
      Http_post();
      Serial.print("alarm!! ");
      Serial.println(alarmdata);
      Serial.println();
      strcpy(msg, alarmdata.c_str());
      client.publish(outTopic, msg);
      clear_msg();
    }
    Serial.println();

  } else  {
    Serial.println();
    Serial.print("Error ");
    Serial.println();
  }
}




void setup_wifi() {
  WiFi.disconnect();
  // Serial.setDebugOutput(true);
  Serial.println();
  Serial.println("[SETUP] Wifi Connecting ");
  //Serial.println(ssid,pass);
  WiFi.mode(WIFI_STA);              // Station 모드로 설정
  //wifi_scan();
   WiFiMulti.addAP(ssid, pass);
  //WiFiMulti.addAP(ssid);     // Wifi 공유기 추가 및 자동연결
  // WiFi.begin(ssid2, pass2);
  //WiFiMulti.addAP(ssid2, pass2);      // 스마트폰AP 추가 및 자동연결

  Serial.print("[SETUP] WAIT ");
  while (WiFiMulti.run() != WL_CONNECTED) {
    delay(500); Serial.print("."); wifi_timeout++;
    if (wifi_timeout == 40) {
      wifi_timeout = 0;
      break;
    }
  }
  Serial.println();
}


void wifi_check() {
  //WiFiMulti.run();
  Serial.print("[SETUP] Wifi status : ");
  Serial.print(WiFi.status());
  Serial.print(" => ");
  if ((WiFi.status() == WL_CONNECTED)) {
    Serial.println("WL_CONNECTED");
  }
  else if ((WiFi.status() == WL_IDLE_STATUS)) {
    Serial.println("WL_IDLE_STATUS");
  }
  else if ((WiFi.status() == WL_CONNECTION_LOST)) {
    Serial.println("WL_CONNECTION_LOST");
  }
  else if ((WiFi.status() == WL_NO_SSID_AVAIL)) {
    Serial.println("WL_NO_SSID_AVAIL");
  }
  else if ((WiFi.status() == WL_CONNECT_FAILED)) {
    Serial.println("WL_CONNECT_FAILED");
  }
  else if ((WiFi.status() == WL_DISCONNECTED)) {
    Serial.println("WL_DISCONNECTED");
    if (!WiFi.getAutoConnect()) {
      WiFi.reconnect();
      Serial.print("[Reconnect] WAIT ");
      while (WiFi.status() != WL_CONNECTED) {
        delay(500); Serial.print(".");
        wifi_timeout++;
        if (wifi_timeout == 200) {
          wifi_timeout = 0;
          break;
        }
      }
      Serial.println();
    }
  }
  else {
    Serial.println("WL_ERROR");
  }
  Serial.print("[SETUP] WifiMulti status : ");
  Serial.print(WiFiMulti.run());
  Serial.print(" => ");
  if ((WiFiMulti.run() == WL_CONNECTED)) {
    Serial.println("WL_CONNECTED");
  }
  else if ((WiFiMulti.run() == WL_IDLE_STATUS)) {
    Serial.println("WL_IDLE_STATUS");
  }
  else if ((WiFiMulti.run() == WL_CONNECTION_LOST)) {
    Serial.println("WL_CONNECTION_LOST");
  }
  else if ((WiFiMulti.run() == WL_NO_SSID_AVAIL)) {
    Serial.println("WL_NO_SSID_AVAIL");
  }
  else if ((WiFiMulti.run() == WL_CONNECT_FAILED)) {
    Serial.println("WL_CONNECT_FAILED");
  }
  else if ((WiFiMulti.run() == WL_DISCONNECTED)) {
    Serial.println("WL_DISCONNECTED");
  }
  else {
    Serial.println("WL_ERROR");
  }
  Serial.print("[SETUP] WifiAutoConnect : ");
  Serial.print(WiFi.getAutoConnect());
  Serial.print(" => ");
  if (WiFi.getAutoConnect()) {
    Serial.println("True");
  }
  else {
    Serial.println("False");
  }
  Serial.print("[SETUP] Connected to ");        //연결된 wifi ID
  Serial.println(WiFi.SSID());
  Serial.print("[SETUP] localIP address:\t");   //할당된 IP 주소
  Serial.println(WiFi.localIP());

  if (delay_second < temphumi_delay) {
    temphumi_delay = delay_second; // 측정주기가 업로드주기보다 길면 업로드주기로 통일
  }
  delay_flag = delay_second; //시작하자마자 한번 POST 작동하도록
  temphumi_flag = temphumi_delay; //시작하자마자 한번 측정하도록
  Serial.print("[SETUP] Device ID : ");
  Serial.print(id);
  Serial.print(", delay_second : ");
  Serial.print(delay_second);
  Serial.print("s, temphumi_delay : ");
  Serial.print(temphumi_delay);
  Serial.println("s");
  Serial.println("");
}

void wifi_scan() {
  Serial.println("[SETUP] scan start");
  // WiFi.scanNetworks will return the number of networks found
  int n = WiFi.scanNetworks();
  Serial.println("[SETUP] scan done");
  if (n == 0) {
    Serial.println("[SETUP] no networks found");
  } else {
    Serial.print("[SETUP] ");
    Serial.print(n);
    Serial.println(" networks found");
    for (int i = 0; i < n; ++i) {
      // Print SSID and RSSI for each network found
      Serial.print(i + 1);
      Serial.print(": ");
      Serial.print(WiFi.SSID(i));
      Serial.print(" (");
      Serial.print(WiFi.RSSI(i));
      Serial.print(")");
      Serial.println((WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*");
      delay(10);
    }
  }
  Serial.println("");
}


void callback(char* topic, byte* payload, unsigned int length) {
  Serial.println();
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    dataIn += (char)payload[i];
  }
  Serial.print(dataIn);
  Serial.println();

  if (dataIn == "1") {
    digitalWrite(LED_BUILTIN, LOW);
    //digitalWrite(pin2, HIGH);
    digitalWrite(pin, HIGH);
    Serial.println("LED ON");
  } else if (dataIn == "0") {
    digitalWrite(LED_BUILTIN, HIGH);
    Serial.println("LED OFF");
    //digitalWrite(pin2, LOW);
    digitalWrite(pin, LOW);
  } else {

    if (dataIn.startsWith("mintemp")) {
      mintemp = dataIn.substring(8).toInt();
      Serial.print("mintemp set : ");
      Serial.println(mintemp);
    } else if (dataIn.startsWith("maxtemp")) {
      maxtemp = dataIn.substring(8).toInt();
      Serial.print("maxtemp set : ");
      Serial.println(maxtemp);
      temphumi_flag = temphumi_delay;
      delay_flag = (delay_second - 5);
    } else if (dataIn.startsWith("minhumid")) {
      minhumid = dataIn.substring(9).toInt();
      Serial.print("minhumid set : ");
      Serial.println(minhumid);
    } else if (dataIn.startsWith("maxhumid")) {
      maxhumid = dataIn.substring(9).toInt();
      Serial.print("maxhumid set : ");
      Serial.println(maxhumid);
      temphumi_flag = temphumi_delay;
      delay_flag = (delay_second - 5);
    } else if (dataIn.startsWith("minco2")) {
      minco2 = dataIn.substring(7).toInt();
      Serial.print("minco2 set : ");
      Serial.println(minco2);
    } else if (dataIn.startsWith("maxco2")) {
      maxco2 = dataIn.substring(7).toInt()  ;
      Serial.print("maxco2 set : ");
      Serial.println(maxco2);
      temphumi_flag = temphumi_delay;
      delay_flag = (delay_second - 5);
    }




  }

  dataIn = "";
}

void reconnect() {
  char idbuf[50];
  String geneid;
  char testsend[75];
  for (int k = 0; k <= 32; k++)
  {
    geneid += random(1, 9);
  }
  geneid.toCharArray(idbuf, geneid.length());
  char char_id[10];
  id.toCharArray(char_id, id.length());
  sprintf(testsend, "%s번 SMF Mqtt : %s", char_id, idbuf);
  const char* mqttid = testsend;

  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(mqttid, mqtt_username, mqtt_password)) {
      Serial.print("connected with id : ");
      Serial.println(idbuf);
      // Once connected, publish an announcement...
      client.publish(outTopic, mqttid);
      client.publish("test_connection", mqttid);
      // ... and resubscribe
      client.subscribe(inTopic);
      Http_get();
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      wifi_check();
      delay(5000);
    }
  }
}

void Http_post() {
  temp = temp_total / temp_flag; //온도 평균 구하기
  humi = humi_total / humi_flag; //습도 평균 구하기
  co2 = co2_total / co2_flag; //이산화탄소 평균 구하기
  l_lux = l_lux_total / l_lux_flag; //좌측 조도값 평균 구하기
  r_lux = r_lux_total / r_lux_flag; //우측 조도값 평균 구하기

  if (co2 > 3000) {
    Serial.printf("[Error} Sensing : ");
    String post_content = "co2=" + String(co2) + "&co2gen=" + String(co2_gen) + "&temp=" + String(temp) + "&humid=" + String(humi) + "&l_lux=" + String(l_lux) + "&r_lux=" + String(r_lux)  + "&id=" + String(id) ; //전달할 데이터
    Serial.println(post_content);
  }
  else {
    HTTPClient http;           //  WiFiClient client;
    http.setTimeout(3000);
    Serial.print("[HTTP] begin... Connected to ");
    Serial.println(WiFi.SSID());
    if (http.begin(client2, "http://cjk09083.cafe24.com/smf/temphumidinsert.php")) {  // Wifi를 통한 HTTP 통신
      http.addHeader("Content-Type", "application/x-www-form-urlencoded");
      Serial.print("[HTTP] POST... ");
      // start connection and send HTTP header
      String post_content = "co2=" + String(co2) + "&co2gen=" + String(co2_gen) + "&temp=" + String(temp) + "&humid=" + String(humi) + "&l_lux=" + String(l_lux) + "&r_lux=" + String(r_lux) + "&id=" + String(id)
                            + "&mintemp=" + String(mintemp) + "&maxtemp=" + String(maxtemp) + "&minhumid=" + String(minhumid) + "&maxhumid=" + String(maxhumid) + "&minco2=" + String(minco2) + "&maxco2=" + String(maxco2); //전달할 데이터
      int httpCode = http.POST(post_content); //Http 통신. Code가 200이면 정상, 음수면 실패
      Serial.println(post_content);
      // httpCode will be negative on error
      if (httpCode > 0) {
        // HTTP header has been send and Server response header has been handled
        Serial.printf("[HTTP] POST... success, code : %d\n", httpCode);
        String payload = http.getString();
        Serial.println(payload);
      } else {
        Serial.printf("[HTTP] POST... failed, error : %s\n", http.errorToString(httpCode).c_str());
      }
      http.end();
    } else {
      Serial.printf("[HTTP} Unable to connect\n");
    }
  }
  delay_flag = 0; temphumi_flag = 0;
  temp_total = 0; temp_flag = 0;
  humi_total = 0; humi_flag = 0;
  co2_total = 0; co2_flag = 0;
  l_lux_total = 0; l_lux_flag = 0;
  r_lux_total = 0; r_lux_flag = 0;

}

void Http_get() {
   HTTPClient http;
    Serial.print("[HTTP] begin...\n");
    if (http.begin(client2, "http://cjk09083.cafe24.com/smf/get_recent_data.php?userID="+id)) {  // HTTP
      Serial.print("[HTTP] GET...\n");
      // start connection and send HTTP header
      int httpCode = http.GET();
      // httpCode will be negative on error
      if (httpCode > 0) {
        // HTTP header has been send and Server response header has been handled
        Serial.printf("[HTTP] GET... code: %d\n", httpCode);
        // file found at server
        if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
          String payload = http.getString();
          Serial.print("payload : ");
          Serial.println(payload);
          int id_index= payload.indexOf('/');
          int temp_index= payload.indexOf('/',id_index+1);
          int humid_index= payload.indexOf('/', temp_index+1);
          int co2_index= payload.indexOf('/', humid_index+1);
          int co2gen_index= payload.indexOf('/', co2_index+1);
          int time_index= payload.indexOf('/', co2gen_index+1);
          int mintemp_index= payload.indexOf('/', time_index+1);
          int maxtemp_index= payload.indexOf('/', mintemp_index+1);
          int minhumid_index= payload.indexOf('/', maxtemp_index+1);
          int maxhumid_index= payload.indexOf('/', minhumid_index+1);
          int minco2_index= payload.indexOf('/', maxhumid_index+1);
          int maxco2_index= payload.indexOf('/', minco2_index+1);

          
          mintemp=payload.substring(time_index+1,mintemp_index).toInt();
          maxtemp=payload.substring(mintemp_index+1,maxtemp_index).toInt();
          minhumid=payload.substring(maxtemp_index+1,minhumid_index).toInt();
          maxhumid=payload.substring(minhumid_index+1,maxhumid_index).toInt();
          minco2=payload.substring(maxhumid_index+1,minco2_index).toInt();
          maxco2=payload.substring(minco2_index+1,maxco2_index).toInt();

          Serial.print("Mintemp : ");
          Serial.println(mintemp);
          Serial.print("Maxtemp : ");
          Serial.println(maxtemp);
          Serial.print("Minhumid  : ");
          Serial.println(minhumid);
          Serial.print("Maxhumid  : ");
          Serial.println(maxhumid);
          Serial.print("Minco2  : ");
          Serial.println(minco2);
          Serial.print("Maxco2  : ");
          Serial.println(maxco2);
            
          Serial.println("ref set comp");
          }
      } else {
        Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
      }
      http.end();
    } else {
      Serial.printf("[HTTP} Unable to connect\n");
    }
}
