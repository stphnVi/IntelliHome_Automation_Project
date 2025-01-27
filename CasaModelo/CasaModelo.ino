#define PIN_LED1 2
#define PIN_LED2 3
#define PIN_LED3 4

//Declaración de la variable del mensaje recibido
String serverMessage;

void setup() {
  Serial.begin(9600); //Iniciar puerto serial a 9600 baund

  //Definiciones de los pines de los leds como salidas
  pinMode(PIN_LED1, OUTPUT); 
  pinMode(PIN_LED2, OUTPUT); 
  pinMode(PIN_LED3, OUTPUT); 
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
}
