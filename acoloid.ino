int r = 10;
int g = 9; 
int b = 8; 

void setup() { 
pinMode(r, OUTPUT);
pinMode(g, OUTPUT);
pinMode(b, OUTPUT);
}

void loop() {
analogWrite(r, 0);
analogWrite(g, 255);
analogWrite(b, 255);
}

