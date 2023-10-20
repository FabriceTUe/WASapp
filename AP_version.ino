// Import required libraries
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <Hash.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebServer.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <user_interface.h>

typedef struct{
  const char* password;
  const char* ssid;
} networkStore;

const char* apSSID     = "ESP8266-Access-Point";
const char* apPassword = "4wbb01111";
const char* localSSID = "Fabrice";
const char* localPassword = "4wbb01111";

#define DHTPIN 5     // Digital pin connected to the DHT sensor

// Uncomment the type of sensor in use:
//#define DHTTYPE    DHT11     // DHT 11
#define DHTTYPE    DHT22     // DHT 22 (AM2302)
//#define DHTTYPE    DHT21     // DHT 21 (AM2301)

DHT dht(DHTPIN, DHTTYPE);

// current temperature & humidity, updated in loop()
float t = 0.0;
float h = 0.0;

// Create AsyncWebServer object on port 80
AsyncWebServer server(80);
IPAddress espIP(192,168,43,82);
IPAddress apIP(192,168,43,81);
IPAddress gateway(192,168,1,1);
IPAddress subnet(255,255,255,0);

// Generally, you should use "unsigned long" for variables that hold time
// The value will quickly become too large for an int to store
unsigned long previousMillis = 0;    // will store last time DHT was updated

// Updates DHT readings every 10 seconds
const long interval = 10000;  

// Replaces placeholder with DHT values
String processor(const String& var){
  //Serial.println(var);
  if(var == "TEMPERATURE"){
    return String(t);
  }
  return String();
}

void setup(){
  // Serial port for debugging purposes
  Serial.begin(115200);
  
  dht.begin(); // set up DHT
  pinMode(LED_BUILTIN, OUTPUT); // set up LED as output

  // Configure static AP IP address:
  //if (!WiFi.softAPConfig(apIP, gateway, subnet)) {
  //  Serial.println("Configuration IP failed to configure.");
  //}

  // Configures static local IP address
  if (!WiFi.config(espIP, gateway, subnet)) {
    Serial.println("ESP IP failed to configure.");
  }

  // Connect to wifi network:
  WiFi.begin(localSSID, localPassword);
  while (WiFi.status() != WL_CONNECTED) { // if no connection has been found yet...
    delay(1000); // wait a sec
    Serial.println("."); // print a dot.
  } // try again

  // Print ESP8266 Local IP address
  espIP = WiFi.localIP();
  Serial.print("ESP8266 IP address:");
  Serial.println(espIP);

  // Route for root / web page
  server.on("/", HTTP_GET, [](AsyncWebServerRequest *request){
    request->send_P(200, "text/plain", "online");
  });
  server.on("/temperature", HTTP_GET, [](AsyncWebServerRequest *request){
    request->send_P(200, "text/plain", String(t).c_str());
  });

  // Start server
  server.begin();
}
 
void loop(){  
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval && WiFi.status() == WL_CONNECTED) {
    digitalWrite(LED_BUILTIN, LOW); // set LED to low
    delay(200); // wait for 200 ms
    digitalWrite(LED_BUILTIN, HIGH); // set LED to high
    // save the last time you updated the DHT values
    previousMillis = currentMillis;

    // Print info on state:
    Serial.print("AP IP address: ");
    Serial.println(apIP);
    Serial.print("ESP8266 IP address: ");
    Serial.println(espIP);

    float newT = dht.readTemperature(); // reads in celsius by default
    // if temperature read failed, don't change t value
    if (isnan(newT)) {
      t = rand() % 20; // assign t to random number between 0 and 19 (inclusive)
      Serial.println("Failed to read from DHT sensor!");
    }
    else {
      t = newT;
      Serial.println(t);
    }
  } else if (currentMillis - previousMillis >= interval && WiFi.status() != WL_CONNECTED) {
    // try to re-establish connection...
    previousMillis = currentMillis;
    Serial.println("."); // print a dot.
  }
}