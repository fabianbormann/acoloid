int red1 = 11; 
int green1 = 10; 
int blue1 = 9;

int red2 = 3; 
int green2 = 5; 
int blue2 = 6;

byte redByte = 0;
byte greenByte = 255;
byte blueByte = 255;

boolean handshake = 0;
byte handshakeValue = 0;
int selectedLED = 0;

void setup() { 
  //define the r,g,b pins as an output 
  pinMode(red1, OUTPUT);
  pinMode(green1, OUTPUT);
  pinMode(blue1, OUTPUT);
  
  pinMode(red2, OUTPUT);
  pinMode(green2, OUTPUT);
  pinMode(blue2, OUTPUT);
  
  //start serial
  Serial.begin(9600);
  
  //initial the led 1,2 with red
  analogWrite(red1, 255);
  analogWrite(green1, 0);
  analogWrite(blue1, 0);
  
  analogWrite(red2, 255);
  analogWrite(green2, 0);
  analogWrite(blue2, 0);
}

void loop() {
  if(handshake == 0){
    waitForHandshake();
  }
  else{
    if(Serial.available())
    {
      //read the values if serial is available
      redByte = Serial.read();
      greenByte = Serial.read();
      blueByte = Serial.read();
      //get the selected LED
      selectedLED = Serial.read();
    }
    
    // refresh the selected led
    switch(selectedLED){
    case 1:
        analogWrite(red1, redByte);
        analogWrite(green1, greenByte);
        analogWrite(blue1, blueByte);
        delay(100);
        break;
    case 2:
        analogWrite(red2, redByte);
        analogWrite(green2, greenByte);
        analogWrite(blue2, blueByte);
        delay(100);
        break;
    default:
        delay(100);
    }
  }
}

/**
  * This function waits for signal from android phone equals (42)
  * and sends the value of LEDs which are connected with this arduino
  */
void waitForHandshake(){
  while(handshake == 0){
    
    if(Serial.available()){
      
      handshakeValue = Serial.read();
      
      if(handshakeValue == 42){
        //change led 1,2 to green
        analogWrite(red1, 0);
        analogWrite(green1, 255);
        analogWrite(blue1, 0);
        
        analogWrite(red2, 0);
        analogWrite(green2, 255);
        analogWrite(blue2, 0);

        Serial.write(2);
        handshake = true;
      } 
    }   
  }
}
