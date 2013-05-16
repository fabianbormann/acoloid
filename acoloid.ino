const int VALID_HANDSHAKE_VALUE = 42;
const int FIRST_RGB_LED_PINS[] = {11,10,9};
const int SECOUND_RGB_LED_PINS[] = {3,5,6};
const int VALUE_OF_LEDS = 2;
const byte INSTRUCTION_WAITING_MODE[] = {99,98,97,99};

byte rgbValues[3];
byte incommingBytes[4];
boolean handshake = false;
int selectedLED = 0;

void setup() { 
  //define the r,g,b pins as an output 
  pinMode(FIRST_RGB_LED_PINS[0], OUTPUT);
  pinMode(FIRST_RGB_LED_PINS[1], OUTPUT);
  pinMode(FIRST_RGB_LED_PINS[2], OUTPUT);
  
  pinMode(SECOUND_RGB_LED_PINS[0], OUTPUT);
  pinMode(SECOUND_RGB_LED_PINS[1], OUTPUT);
  pinMode(SECOUND_RGB_LED_PINS[2], OUTPUT);
  
  //start serial
  Serial.begin(9600);
  
  //initial the led 1,2 with red
  analogWrite(FIRST_RGB_LED_PINS[0], 255);
  analogWrite(FIRST_RGB_LED_PINS[1], 0);
  analogWrite(FIRST_RGB_LED_PINS[2], 0);
  
  analogWrite(SECOUND_RGB_LED_PINS[0], 255);
  analogWrite(SECOUND_RGB_LED_PINS[1], 0);
  analogWrite(SECOUND_RGB_LED_PINS[2], 0);
}

void loop() {
  if(!handshake){
    waitForHandshake();
  }
  else{
    if(Serial.available())
    {
      //read the values if serial is available
      for(int currentByte = 0; currentByte <= sizeof(incommingBytes); currentByte++){
        incommingBytes[currentByte] = Serial.read();
      }
      handleIncommingBytes();
    }
    
    // refresh the selected led
    switch(selectedLED){
    case 1:
        analogWrite(FIRST_RGB_LED_PINS[0], rgbValues[0]);
        analogWrite(FIRST_RGB_LED_PINS[1], rgbValues[1]);
        analogWrite(FIRST_RGB_LED_PINS[2], rgbValues[2]);
        delay(100);
        break;
    case 2:
        analogWrite(SECOUND_RGB_LED_PINS[0], rgbValues[0]);
        analogWrite(SECOUND_RGB_LED_PINS[1], rgbValues[1]);
        analogWrite(SECOUND_RGB_LED_PINS[2], rgbValues[2]);
        delay(100);
        break;
    default:
        delay(100);
    }
  }
}

/**
  * This function waits for vaild signal from android phone
  * and sends the value of LEDs which are connected with this arduino
  */
void waitForHandshake(){
  while(!handshake){
    if(Serial.available()){
      if(Serial.read() == VALID_HANDSHAKE_VALUE){
        Serial.write(VALUE_OF_LEDS);
        handshake = true;
      } 
    } 
  }
}

/**
 * Interprets the incomming bytes as RGB Values or instructions
 */
void handleIncommingBytes(){
  
  int instruction = checkForInstruction();
  
  switch(instruction){
    case 0: 
      rgbValues[0] = incommingBytes[0];
      rgbValues[1] = incommingBytes[1];
      rgbValues[2] = incommingBytes[2];
      //set the selected LED
      selectedLED = incommingBytes[3];
      break;
    case 1:
      //receive instruction for waiting mode  
      setInWaitingMode();
      break;
  }
}

/**
 * return an instructionnumber or 0 in the case of no match
 */
int checkForInstruction(){
  int byteMatches = 0;
  for(int currentByte; currentByte <= sizeof(incommingBytes); currentByte++){
    if (incommingBytes[currentByte] != INSTRUCTION_WAITING_MODE[currentByte])
      byteMatches++;
  }
  
 if(byteMatches == sizeof(incommingBytes))
   return 1;
 else
   return 0; 
}

/**
 * Android App was terminated and will need a new handshake at 
 * the next start. This function set boolean handshake to false and 
 * this arduino waits for new handshake value
 */
void setInWaitingMode(){
  handshake = false;
}
