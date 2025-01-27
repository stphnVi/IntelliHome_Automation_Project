import re
import math

#                                                        ________________________________________________
# ______________________________________________________/función revisa si los credenciales son correctos


def login_info(message):
    print(f"Mensaje recibido: {message}")

    # Normalizar el mensaje
    message = message.lower().strip()
    is_email = "@" in message

    if is_email:
        print("El string es un correo")
        modified_message = message.replace("useremail", "email")
        identifier = get_email_from_string(modified_message)
    else:
        print("El string no es un correo, probablemente es un username")
        modified_message = message.replace("useremail", "username")
        identifier = get_username_from_string(modified_message)

    password = get_password_from_string(modified_message)

    if not identifier or not password:
        print("Error: Datos incompletos en el mensaje.")
        return "0"

    try:
        with open('./database/data.txt', 'r') as file:
            for line in file:
                line = line.strip().lower()
                db_password = get_password_from_string(line)
                db_identifier = get_email_from_string(
                    line) if is_email else get_username_from_string(line)

                if db_password and db_identifier and db_password == password and db_identifier == identifier:
                    print("¡Coinciden las credenciales!")
                    return "1"
    except FileNotFoundError:
        print("Error: Archivo de base de datos no encontrado.")

    print("No coinciden las credenciales")
    return "0"


#                                                        ____________________________________________________
# ______________________________________________________/Auxiliar de login, obtiene email


def get_email_from_string(data):
    match = re.search(r"email:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None


#                                                        ____________________________________________________
# ______________________________________________________/Auxiliar de login, obtiene username


def get_username_from_string(data):

    match = re.search(r"username:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None
#                                                        ____________________________________________________
# ______________________________________________________/Auxiliar de login, obtiene password


def get_password_from_string(data):
    # Extraer el password del string
    match = re.search(r"password:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None

#                                                        _____________________________________________________
# ______________________________________________________/Función que Agrega usuarios a la base de datos


def add_user(user_info):
    try:
        # Abrir el archivo en modo append (agregar al final)
        with open('./database/data.txt', 'a') as file:
            # Escribir la información del usuario en el archivo
            file.write("\n" + user_info)
        print("Usuario agregado")
        return "1"
    except Exception as e:
        print(f"Error")

#                                                        _______________________________________________________
# ______________________________________________________/Auxiliar para tomar datos de recuperacion de contraseña

# Función para obtener un dato


def get_field_from_string(data, field):
    match = re.search(rf"{field}:\s*([^\n,]+)", data)
    if match:
        return match.group(1).strip()  # Eliminamos espacios adicionales
    return None


#                                                        _______________________________________________________
# ______________________________________________________/Función que maneja la Recuperacion de contraseña

def questions(user_info):
    try:

        with open("./database/data.txt", "r") as file:
            for line in file:

                if all(get_field_from_string(user_info, field) == get_field_from_string(line, field)
                       for field in ["username", "nombreProfe", "apodo", "equipo"]):
                    print("SI es el usuario")
                    return "1"  # Todos los datos coinciden
        print("NO es el usuario")
        return "0"  # No se encontraron coincidencias
    except Exception as e:
        print(f"Error: {e}")
        return "0"

#                                                        _____________________________________________
# ______________________________________________________/función que recibe los mensajes del cliente


def receive_info(data):

    print(f"String original: {data}")

    inicio = data.find("func:")
    if inicio == -1:
        print("No se encontró 'func'")
        return  # Si no hay "func:", termina la función

    fin = data.find(",", inicio)  # Busca la coma después de "func:"

    if fin == -1:
        valor_func = data[inicio + 6:].strip()  # Último elemento
        nuevo_data = ""
    else:
        valor_func = data[inicio + 6:fin].strip()
        nuevo_data = data[:inicio].strip() + " " + data[fin+1:].strip()

    if valor_func == "login":
        return login_info(nuevo_data.strip())
    elif valor_func == "rec":
        print("entra")
        return questions(nuevo_data.strip())
    else:
        return add_user(nuevo_data.strip())

    print(f"Valor de 'func': {valor_func}")
    print(f"String modificado: {nuevo_data.strip()}")


#                                                        ____________________________________________________
# ______________________________________________________/ Función auxiliar para el cálculo de las coordenadas

def calcular_distancia(lat1, lon1, lat2, lon2):
    lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])

    dlat = lat2 - lat1
    dlon = lon2 - lon1

    # Fórmula de Haversine
    a = math.sin(dlat / 2)**2 + math.cos(lat1) * \
        math.cos(lat2) * math.sin(dlon / 2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

    # Radio de la Tierra en kilómetros
    R = 6371

    # Distancia final en km
    distancia = R * c
    return distancia

#                                                        _____________________________________________
# ______________________________________________________/ Buscar propiedades en la base de datos


def cargar_propiedades(nombre_archivo):
    propiedades = []
    with open(nombre_archivo, "r", encoding="utf-8") as file:
        contenido = file.read().strip()
        registros = contenido.split("\n")  # Separa cada propiedad por línea

        datos = {}
        for linea in registros:
            if "nombre de la propiedad" in linea and datos:
                # Guardar la propiedad anterior antes de iniciar una nueva
                propiedades.append(datos)
                datos = {}

            atributos = linea.split(", ")  # Divide los atributos de la línea
            for atributo in atributos:
                if ": " in atributo:
                    clave, valor = atributo.split(": ", 1)
                    clave = clave.strip()
                    valor = valor.strip()

                    if clave == "amenidades":

                        valores_amenidades = valor.split(
                            "; ")
                        # Guardar lista completa de amenidades
                        datos[clave] = valores_amenidades

                        # print(f"Procesando amenidades: {valores_amenidades}")
                    elif clave == "ubi":
                        valores_ubi = valor.split("; ")
                        # Guardar coordenadas como (latitud, longitud)
                        datos[clave] = tuple(map(float, valores_ubi))

                    else:
                        datos[clave] = valor  # Guardar valores normales

        if datos:
            propiedades.append(datos)

    print(propiedades)  # Verificar la salida
    return propiedades


def buscar_propiedades(propiedades, capacidad=None, precio=None, amenidades=None, ubi=None):
    resultados = []
    # print(propiedades)
    if ubi:
        # Convertir de string a float
        lat_entrada, lon_entrada = map(float, ubi)

    for prop in propiedades:
        # Filtros de capacidad y precio
        if capacidad and prop.get("capacidad maxima") != str(capacidad):
            continue
        if precio and int(prop.get("precio", 0)) > precio:
            continuej

        # Filtro de amenidades (debe coincidir exactamente las solicitadas)
        if amenidades:
            amenidades_db = prop.get("amenidades", [])

            # print(amenidades_db)

            if set(amenidades) != set(amenidades_db):
                continue  # Si no coinciden exactamente, descartamos la propiedad

        # Filtro de ubicaciones  filtrará aquellas que esten dentro de un trango de 50km de distancia respecto a la solicitada

        if ubi:
            lat_prop, lon_prop = prop.get("ubi", (None, None))
            if lat_prop is not None and lon_prop is not None:
                distancia = calcular_distancia(
                    lat_entrada, lon_entrada, lat_prop, lon_prop)
                if distancia > 50:  # Filtrar solo propiedades dentro de 30 km
                    continue  # Si la propiedad está fuera del radio, la descartamos
        # Si pasa todos los filtros, añadimos la propiedad al resultado
        resultados.append(
            f'{prop["nombre de la propiedad"]}, capacidad maxima: {prop["capacidad maxima"]}, precio: {prop["precio"]}, amenidades: {", ".join(prop["amenidades"])}')

    # Si no hay resultados, imprimir mensaje y devolver 0
    if not resultados:
        print(" No se encontraron propiedades que cumplan con los criterios.")
        return 0

    return "; ".join(resultados)


# EJEMPLO DE USO
propiedades = cargar_propiedades("./database/test.txt")

capacidad_buscada = 2
precio_buscado = 4000
# Ingresar exactamente las amenidades requeridas
amenidades_buscadas = ["perros", "wifi"]
# amenidades_buscadas = ["gatos", "ducha"]
ubicacion_buscada = ["10.666966", "-85.648673"]
# Buscar propiedades
resultado = buscar_propiedades(
    propiedades, capacidad=capacidad_buscada, precio=precio_buscado, amenidades=amenidades_buscadas, ubi=ubicacion_buscada
)

print(resultado)


# receive_info(
#    "func: login, userEmail: juan@example.com, password: 5678")


# receive_info("func: login, userEmail: Juan123, password: 5678")

# recovery: "func: rec, username: Tefa1, nombreProfe: Json, apodo: gogi, equipo: liga"
# register: "func: reg, username: Juan123, email: juan@example.com, password: 5678"
# login: "func: login, username: Juan123, password: 5678"
# login: "func: login, email: juan@example.com, password: 5678"
