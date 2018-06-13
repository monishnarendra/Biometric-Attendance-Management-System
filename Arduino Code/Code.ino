#include <ESP8266WiFi.h>
#include <ESP8266WiFiAP.h>
#include <ESP8266WiFiGeneric.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266WiFiScan.h>
#include <ESP8266WiFiSTA.h>
#include <ESP8266WiFiType.h>
#include <WiFiClient.h>
#include <WiFiClientSecure.h>
#include <WiFiServer.h>
#include <WiFiUdp.h>

#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>

#include "FPS_GT511C3.h"
#include "SoftwareSerial.h"

#include <Wire.h> //I2C library
#include <RtcDS3231.h> //RTC library

RtcDS3231<TwoWire> rtcObject(Wire);

#define FIREBASE_HOST "biometric-system-db0bb.firebaseio.com"
#define FIREBASE_AUTH "q1CnSLTsMruXmpQUgU77ycAF6A1VByN4onzNih0T"
#define WIFI_SSID "Monish"
#define WIFI_PASSWORD "123456789"

#define RX_PIN  D3
#define TX_PIN  D4

FPS_GT511C3 fps(RX_PIN, TX_PIN);
String Arrival_Time,Departed_Time;
void setup() {
  
    Serial.begin(115200);
    delay(5000);            // 5 seconds
    
    Serial.println("Booting....");

    rtcObject.Begin(); //Starts I2C
    //RtcDateTime currentTime = RtcDateTime(18,06,10,16,50,0); //define date and time object
    //rtcObject.SetDateTime(currentTime);                      //configure the RTC with object

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("connecting");
    
    while (WiFi.status() != WL_CONNECTED) { 
        Serial.print(".");  
        delay(500);
    }

    int retries = 0;
    while ((WiFi.status() != WL_CONNECTED) && (retries < 30)) {
        retries++;
        delay(500);
        Serial.print(".");
    }

    if (WiFi.status() == WL_CONNECTED) {
        Serial.println("WiFi connected");
    }
  
    Serial.println("Ready!");
   
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    fps.Open();
    fps.SetLED(true);
    ESP.wdtDisable();
    ESP.wdtEnable(1000);
}

void loop() {
  delay(0);
  if (fps.IsPressFinger()) {
    fps.CaptureFinger(false);
    int id = fps.Identify1_N();
    if (id < 200){
      Serial.print("Verified ID:");
      Serial.println(id);
      Serial.println("Please Remove Finger");
    
      String a = String(id);
      SendData(a);
      
      if (Firebase.failed()){
        Serial.print("setting /number failed:");
        Serial.println(Firebase.error());
        return;
      }
    } else {
      Serial.println("Finger not found");
    }
  } else {
    Serial.println("Please press finger");
  }
  delay(15000);
  ESP.wdtFeed();
}

void SendData(String uid){
  RtcDateTime currentTime = rtcObject.GetDateTime();    //get the time from the RTC
  char Date[20];
  char Time[20];
  
  sprintf(Date, "%d-%d-%d",
          currentTime.Day(),
          currentTime.Month(),
          currentTime.Year()
         );
  sprintf(Time, "%d",
          currentTime.Hour()
         );

  Serial.print("\n");
  Serial.print("Date: ");
  Serial.print(Date);
  Serial.print("\nTime: ");
  Serial.print(Time);

  Firebase.setString(uid + "/" + Date + "/" + "Date" ,Date);
  
  Arrival_Time = Firebase.getString(uid + "/" + Date + "/" + "Arrival Time");
  Departed_Time = Firebase.getString(uid + "/" + Date + "/" + "Departed Time");

  if (Arrival_Time != NULL){
    if(Arrival_Time != Time){
      Firebase.setString(uid + "/" + Date + "/" + "Departed Time" ,Time);
      Serial.println("\nUploaded Departed_Time");
    }else{
        Serial.println("\nwait an hour");
    }
    
  }
  if (Arrival_Time == NULL && Departed_Time == NULL){
    Firebase.setString(uid + "/" + Date + "/" + "Arrival Time" ,Time);  
    Serial.println("\nUploaded Arrival_Time");
  }
  if (Arrival_Time != NULL && Departed_Time != NULL){
    Serial.println("\nDone For the Day");  
    }   
    Serial.print("\nUploaded\n");
}