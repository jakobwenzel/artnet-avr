void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

#define NUMCHANNELS 1
void updateChannel(int channel, unsigned char data) {
  if (channel==0)
    analogWrite(6,data);
}

void loop() {
  for (int i=0;i<NUMCHANNELS;i++) {
    while(!Serial.available());
    unsigned char data = Serial.read();
    updateChannel(i,data);  
  }
}
