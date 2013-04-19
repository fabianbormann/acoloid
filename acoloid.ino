int r = 10;
int g = 11; 
int b = 12; 

void setup() { 
pinMode(r, OUTPUT);
pinMode(g, OUTPUT);
pinMode(b, OUTPUT);
}

void loop() {
analogWrite(r, 155);
digitalWrite(g, 10);
digitalWrite(b, 10);
}

