int r = 10; 
int g = 9; 
int b = 8;
byte rByte = 0;
byte gByte = 255;
byte bByte = 255;

void setup() { 
  // define the r,g,b pins as an output 
  pinMode(r, OUTPUT);
  pinMode(g, OUTPUT);
  pinMode(b, OUTPUT);
  // start serial
  Serial.begin(9600);
  // initial the led with cyan
  analogWrite(r, 0);
  analogWrite(g, 255);
  analogWrite(b, 255);
}

void loop() {

  if(Serial.available())
  {
    //read the values if serial is available
    rByte = Serial.read();
    gByte = Serial.read();
    bByte = Serial.read();
  }
     
  // refresh the led
  analogWrite(r, rByte);
  analogWrite(g, gByte);
  analogWrite(b, bByte);
  
  delay(100);
}
