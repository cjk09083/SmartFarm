#include <Wire.h>
#include <BH1750.h>
#include <SyRenSimplified.h>
#include <SoftwareSerial.h>
BH1750 alight(0x23);
BH1750 blight(0x5C);
SoftwareSerial SWSerial(NOT_A_PIN, 3 ); // RX on no pin (unused), TX on pin 11 (to S1).
SyRenSimplified SR(SWSerial); // Use SWSerial as the serial port.

////////////////////////////////////////////////////////////////////////
///////////////////////////초기값 설정//////////////////////////////////
////////////////////////////////////////////////////////////////////////
int oldl = 0;
int tg = 0;
int tg_d = 0;
int count = 0;
int moter_time = 0;
int timetext = 20;
int time_count = 500;
int swich_pin = 5;
long int newl;
int rl[30];
int ll[30];
int Iset;
int old;
String date;  
int angle[100];
int angle_count = 30;
int moter_power = -35;
int angle_delay = 500;
int top_lux = 0;
int top_angle = 0;
int ccw;
///////////////////////////////////////////////////////////////////////
///////////////////////////함수 설정///////////////////////////////////
///////////////////////////////////////////////////////////////////////
void angle_swich();
void lux_measure();
void old_save();
void state_time();
void moter_con();
void clear_int();
void setup() {
  Serial.begin(57600);
  SWSerial.begin(9600);
  Wire.begin();
  pinMode(swich_pin, INPUT);
  alight.begin();
  blight.begin();
  SR.motor(-1 * moter_power);
  //Serial.println("+++++++++");
  //Serial.println("motor set");
  angle_swich();
  /*SR.motor(0);
  delay(1000);
  //Serial.println("motor set");
  //lux_measure("first");
  // Serial.println("");
  Serial.println("swoch");
  SR.motor(80);
  //Serial.println("---------");
  int k = 0;
  while ((rl < 400) && (ll < 400)) {
    lux_measure("while");
    //Serial.println(newl);
    SR.motor(-80);
    delay(450);
    SR.motor(0);
    delay(100);
    k++;
    Serial.println(rl);
    Serial.println(ll);
    Serial.println(newl);
    if (k > 100) {
      break;
    }
  }*/
  //Serial.println("while text end");
  SR.motor(0);
  Serial.println("moter_con");
  //moter_con();
  old_save();
  Serial.println("setup");
}

void loop() {
  // lux_measure(""); //딜레이 횟수(200,50)
moter_con();
  // clear_int();
/*
  //if (count < 2) {
  lux_measure("  AA");
  if (oldl < newl) {
    tg = 0;
  }
  if (oldl > newl) {
    tg = 1;
    count++;
  }
  if (tg == 0 && tg_d == 0) {
    SR.motor(-1 * moter_power);
    //Serial.println("---------");
  }
  if (tg == 0 && tg_d == 1) {
    SR.motor(moter_power);
    //Serial.println("++++++++++++");
  }
  if (tg == 1 && tg_d == 0) {
    SR.motor(moter_power);
    tg_d = 1;
    //Serial.println("++++++++++++");
  }
  else if (tg == 1 && tg_d == 1) {
    SR.motor(-1 * moter_power);

    // Serial.println("---------");
    tg_d = 0;
  }
  if (count == 2) {
    SR.motor(0);
    state_time(20, 50);
    moter_time = 0;
    //Serial.println("stop");
  }
  //Serial.print("oldl :");
  //Serial.println(oldl);
  old_save();
  tg = 0;
  moter_time++;

  state_time(20, 50);
  SR.motor(0);
  state_time(20, 50);
  //}
  /*else {
    count = 0;
    }*/

}

//////////////////////////////////////////////////////
///////////////////////함수///////////////////////////
//////////////////////////////////////////////////////
void angle_swich() {//80
  int s = 0;
  Iset = map(analogRead(A0), 0, 1023, 0, 180);
  Serial.println(Iset);
  while (Iset < 180) {
    Iset = map(analogRead(A0), 0, 1023, 0, 180);
    Serial.print("Iset: ");
    Serial.println(Iset);
    /*  Serial.print("lset : ");
        Serial.print(Iset);
        Serial.print(" (");
        Serial.print(s);
        Serial.println(")");*/
    /*
          SR.motor(70);
          delay(1000);
          SR.motor(0);
          delay(3000);*/
    s++;
    delay(1);
    if (s > 20000) {
      break;
    }
  }
}
void lux_measure(int a) {
  ll[a] = alight.readLightLevel();
  rl[a] = blight.readLightLevel();
  newl = ll[a] + rl[a];

  /* Serial.print(names);
    Serial.print("new : ");
    Serial.print(newl);
    Serial.print(", ll : ");
    Serial.print(ll);
    Serial.print(", rl : ");
    Serial.print(rl);
    Serial.print(", old:");
    Serial.println(old);
  */
}
void old_save() {
  old = newl;
}
void state_time(int a, int b) {
  for (int i = 0; i < b; i++) { //Serial.println("2");
    if (Serial.available() > 0) {//Serial.println("1");
      char input_date = Serial.read();
      if (input_date == 's') {
        date = String(rl[top_angle]) + 'l' + String(ll[top_angle]) + ';'+String(top_angle)+'.';
        Serial.print(date);
      }
    }
    delay(a);
  }
}

void moter_con() {
  for (int i = 0; i < 16; i++) {
    SR.motor(moter_power);
    Iset = map(analogRead(A0), 0, 1023, 0, 180);
    while (Iset > 180 - (i * 5 + 10)) {
      Iset = map(analogRead(A0), 0, 1023, 0, 180);
    }
    SR.motor(0);
    state_time(20, 50);
    SR.motor(-1 * moter_power);
    Iset = map(analogRead(A0), 0, 1023, 0, 180);
    while (Iset < 180 - (i * 5)) {
      Iset = map(analogRead(A0), 0, 1023, 0, 180);
    }
    SR.motor(0);
    state_time(20, 125);
    lux_measure(i);
    angle[i] = newl;
    Serial.print(Iset);
    Serial.print(" : ");
    Serial.print(newl);
    Serial.print("  ");
  }
  top_lux=0;
  top_angle=0;
  for (int j = 0; j < 16; j++) {
    if (angle[j] > top_lux) {
      top_lux = angle[j];
      top_angle = j;
    }
  }
  ccw = angle_count - top_angle;
  SR.motor(moter_power * -1);
  while (Iset < 180 - (top_angle * 5)) {
    Iset = map(analogRead(A0), 0, 1023, 0, 180);

  }
  SR.motor(0);
  Serial.println("");
  Serial.print("top_angle: ");
  Serial.print(top_angle);
  Serial.print("Iset: ");
  Serial.print(Iset);
  Serial.print("top_lux: ");
  Serial.println(top_lux);
  state_time(20, 500*6);
  //delay(10000);
  SR.motor(moter_power * -1);
  while (Iset < 180) {
    Iset = map(analogRead(A0), 0, 1023, 0, 180);

  }
  SR.motor(0);
}
void clear_int() {
  top_lux = 0;
  top_angle = 0;

}
