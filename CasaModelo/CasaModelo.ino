#define PIN_LED1 2
#define PIN_LED2 3
#define PIN_LED3 4
#define SENSOR_FLAME_PIN 10
//Define el pin del switch de inclinacion
const int switchPin = 11;

//Declaración de la variable del mensaje recibido
String serverMessage;

//Estado del sensor flame
bool flameSensor;
bool fire;

void setup() {
  Serial.begin(9600); //Iniciar puerto serial a 9600 baund

  //Definiciones de los pines de los leds como salidas
  pinMode(PIN_LED1, OUTPUT); 
  pinMode(PIN_LED2, OUTPUT); 
  pinMode(PIN_LED3, OUTPUT); 
  pinMode(SENSOR_FLAME_PIN, INPUT);

  //Configura el pin del switch como entrada
  pinMode(switchPin, INPUT);
}

void loop() {
  serverMessage =Serial.readStringUntil('\n'); //Leer strings del puerto serial hasta encontrar el caracter de nueva línea

  //Se usan claves para hacer diferentes cosas con base a los mensajes que se reciban, en este caso, encender o apagar los leds
  if(serverMessage == "func: luzsalaLED1_ON"){
    digitalWrite(PIN_LED1, 1);
  } else if (serverMessage == "func: luzsalaLED1_OFF"){
    digitalWrite(PIN_LED1, 0);
  } else if (serverMessage == "func: luzcuartoLED2_ON"){
    digitalWrite(PIN_LED2, 1);
  } else if (serverMessage == "func: luzcuartoLED2_OFF"){
    digitalWrite(PIN_LED2, 0);
  } else if (serverMessage == "func: luzbañoLED3_ON"){
    digitalWrite(PIN_LED3, 1);
  } else if (serverMessage == "func: luzbañoLED3_OFF"){
    digitalWrite(PIN_LED3, 0);
  }


  //Lectura del pin del sensor flame
  flameSensor = digitalRead(SENSOR_FLAME_PIN);

  //Comportamiento con base al valor del sensor flame
  if(flameSensor && !fire){
    Serial.write("Llama detectada!\n");
    fire = true;
  }

  if(!flameSensor && fire){
    Serial.write("Llama apagada!\n");
    fire = false;
  }

  delay(200);

  //Lee el estado del switch de inclinacion
  int switchState = digitalRead(switchPin);

  //Imprime el estado del Switch en el Monitor Serie
  if(switchState == HIGH){
    Serial.println("No hay inclinacion (LOW)");
  } else {
    Serial.println("Inclinacion detectada (HIGH)");
  }

  // Espera un momento para evitar lecturas demasiado rápidas
  delay(500);


}
