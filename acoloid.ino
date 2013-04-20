String values; // contains the RGB values
int received; // this 
int r = 10; 
int g = 9; 
int b = 8;
int rValue = 0;
int gValue = 0;
int bValue = 0;

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
/*
  if(Serial.available())
  {
    //read the values if serial is available
    received = Serial.read(); 
  }
*/   
  values = "133;255;12"; // example data
  
  // decode the values
  rValue = getRValue(values);
  gValue = getGValue(values);
  bValue = getBValue(values);
  
  // refresh the led
  analogWrite(r, rValue);
  analogWrite(g, gValue);
  analogWrite(b, bValue);
  
  delay(100);
}

int getRValue(String s){
  s = s.substring(0,findDelimiter(s));
  return s.toInt();
}
 
int getGValue(String s){
  s = s.substring(findDelimiter(s)+1);
  s = s.substring(0,findDelimiter(s));
  return s.toInt();
} 

int getBValue(String s){
  s = s.substring(findDelimiter(s)+1);
  s = s.substring(findDelimiter(s)+1);
  return s.toInt();
} 
 
int findDelimiter(String s){
  int pos = 0;
  while(s.charAt(pos) != ';'){
    pos++;
  }
  return pos;
}
