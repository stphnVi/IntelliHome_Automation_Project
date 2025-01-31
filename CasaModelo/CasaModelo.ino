#define PIN_LED1 2
#define PIN_LED2 3
#define PIN_LED3 4
#define SENSOR_FLAME_PIN 10
#define SWITCH_PIN 11  // Pin del switch de inclinación

// Variables de estado
String serverMessage;
bool flameSensor;
bool fire = false;
bool lastSwitchState = HIGH;  // Estado previo del switch

void setup() {
  Serial.begin(9600);  // Iniciar comunicación serie

  // Configurar pines de los LEDs como salida
  pinMode(PIN_LED1, OUTPUT);
  pinMode(PIN_LED2, OUTPUT);
  pinMode(PIN_LED3, OUTPUT);
  pinMode(SENSOR_FLAME_PIN, INPUT);
  
  // Configurar el switch como entrada
  pinMode(SWITCH_PIN, INPUT);
}

void loop() {
  // Leer mensaje del servidor
  serverMessage = Serial.readStringUntil('\n');

  // Control de LEDs según el mensaje recibido
  if (serverMessage == "func: luzsalaLED1_ON") {
    digitalWrite(PIN_LED1, HIGH);
  } else if (serverMessage == "func: luzsalaLED1_OFF") {
    digitalWrite(PIN_LED1, LOW);
  } else if (serverMessage == "func: luzcuartoLED2_ON") {
    digitalWrite(PIN_LED2, HIGH);
  } else if (serverMessage == "func: luzcuartoLED2_OFF") {
    digitalWrite(PIN_LED2, LOW);
  } else if (serverMessage == "func: luzbañoLED3_ON") {
    digitalWrite(PIN_LED3, HIGH);
  } else if (serverMessage == "func: luzbañoLED3_OFF") {
    digitalWrite(PIN_LED3, LOW);
  }

  // Lectura del sensor de llama
  flameSensor = digitalRead(SENSOR_FLAME_PIN);
  if (flameSensor && !fire) {
    Serial.println("Llama detectada!");
    fire = true;
  }
  if (!flameSensor && fire) {
    Serial.println("Llama apagada!");
    fire = false;
  }

  // Lectura del switch de inclinación
  bool switchState = digitalRead(SWITCH_PIN);
  if (switchState != lastSwitchState) {  // Solo enviar mensaje si cambia el estado
    if (switchState == HIGH) {
      Serial.println("No hay inclinación (LOW)");
    } else {
      Serial.println("Inclinación detectada (HIGH)");
    }
    lastSwitchState = switchState;  // Actualizar estado previo
  }

  delay(200);
}

