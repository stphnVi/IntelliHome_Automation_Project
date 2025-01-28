import re
import math
from datetime import datetime
from database.descifrado import *
from database.cifrado import *
import subprocess
import os


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
                     # Llamar al archivo Notificaciones.py como un script independiente
                    #ruta_notificaciones = os.path.join("database", "Notificaciones.py")
                    #subprocess.run(["python", ruta_notificaciones])
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

#                                                        _____________________________________________________
# ______________________________________________________/Función que Agrega usuarios a la base de datos


def add_house(house_info):
    try:
        # Abrir el archivo en modo append (agregar al final)
        with open('./database/casas.txt', 'a') as file:
            # Escribir la información de la casa en el archivo
            file.write("\n" + house_info)
        print("Casa agregada")
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
                    return "10"  # Todos los datos coinciden
        print("NO es el usuario")
        return "20"  # No se encontraron coincidencias
    except Exception as e:
        print(f"Error: {e}")
        return "30"


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

#                                                        ____________________________________________________
# ______________________________________________________/ Función auxiliar para Parseo de las fechas


def parsear_fechas(fechas_str):
    fechas = []
    for fecha in fechas_str.split("; "):
        if "-" in fecha:
            inicio, fin = fecha.split("-")
            fechas.append((datetime.strptime(inicio, "%m/%d/%Y"),
                          datetime.strptime(fin, "%m/%d/%Y")))
        else:
            fechas.append(datetime.strptime(fecha, "%m/%d/%Y"))
    return fechas
#                                                        _____________________________________________________
# ______________________________________________________/ Función auxiliar para Verificar disponibilidad
# de las fechas


def verificar_disponibilidad(fechas_disponibles, fechas_busqueda):
    fechas_db = parsear_fechas(fechas_disponibles)
    fechas_buscadas = parsear_fechas("; ".join(fechas_busqueda))

    for fecha_busqueda in fechas_buscadas:
        if isinstance(fecha_busqueda, tuple):
            for fecha in fechas_db:
                if isinstance(fecha, tuple):
                    if fecha_busqueda[0] >= fecha[0] and fecha_busqueda[1] <= fecha[1]:
                        return True
                else:
                    if fecha_busqueda[0] <= fecha <= fecha_busqueda[1]:
                        return True
        else:
            for fecha in fechas_db:
                if isinstance(fecha, tuple):
                    if fecha[0] <= fecha_busqueda <= fecha[1]:
                        return True
                elif fecha == fecha_busqueda:
                    return True
    return False

#                                                        _____________________________________________________
# ______________________________________________________/ Función auxiliar para Cargar las propiedades de la
# base de datos


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
                    elif clave == "fechas":
                        datos[clave] = valor
                    else:
                        datos[clave] = valor  # Guardar valores normales

        if datos:
            propiedades.append(datos)

    print(propiedades)
    return propiedades

#                                                        ____________________________________________________
# ______________________________________________________/ Función para Buscar propiedades de la base de datos
# e identificar coincidencias


def buscar_propiedades(propiedades, capacidad=None, precio=None, amenidades=None, ubi=None, fechas=None):
    resultados = []
    if ubi:
        lat_entrada, lon_entrada = map(float, ubi)

    for prop in propiedades:
        if capacidad and prop.get("capacidad maxima") != str(capacidad):
            continue
        if precio and int(prop.get("precio", 0)) > precio:
            continue

        if amenidades:
            amenidades_db = prop.get("amenidades", [])
            if set(amenidades) != set(amenidades_db):
                continue

        if ubi:
            lat_prop, lon_prop = prop.get("ubi", (None, None))
            if lat_prop is not None and lon_prop is not None:
                distancia = calcular_distancia(
                    lat_entrada, lon_entrada, lat_prop, lon_prop)
                if distancia > 50:
                    continue

        if fechas and not verificar_disponibilidad(prop.get("fechas", ""), fechas):
            continue

        resultados.append(
            f'{prop["nombre de la propiedad"]}, capacidad maxima: {prop["capacidad maxima"]}, precio: {prop["precio"]}, amenidades: {", ".join(prop["amenidades"])}')

    if not resultados:
        print("No se encontraron propiedades que cumplan con los criterios.")
        return 0

    return "; ".join(resultados)

#                                                        _____________________________________________
# ______________________________________________________/ Función para recibir datos para su posterior
# consulta de alquileres


def recibir_datos_alquilar(entrada):
    try:
        datos = eval(entrada)
        return buscar_propiedades(
            propiedades=cargar_propiedades("./database/test.txt"),
            capacidad=datos.get("capacidad"),
            precio=datos.get("precio"),
            amenidades=datos.get("amenidades"),
            ubi=datos.get("ubi"),
            fechas=datos.get("fecha")
        )
    except Exception as e:
        return f"Error procesando los datos: {e}"


# Ejemplo de uso
datos_entrada = '{"capacidad": 2, "precio": 4000, "amenidades": ["perros", "wifi"], "ubi": ["10.666966", "-85.648673"], "fecha": ["02/03/2025-02/15/2025"]}'
print(recibir_datos_alquilar(datos_entrada))


#                                                        _____________________________________________
# ______________________________________________________/función que recibe los mensajes del cliente


def receive_info(data):

    # desencriptar base de datos

    key = load_key('./database/key.txt')

    decrypt_file('./database/data_encrypted.txt',
                 './database/data.txt', key)

    key = os.urandom(32)  # AES-256
    iv = os.urandom(16)

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
        result = login_info(nuevo_data.strip())

    elif valor_func == "rec":
        print("entra")
        return questions(nuevo_data.strip())

    elif valor_func == "regcasa":
        print("entra")
        return add_house(nuevo_data.strip())
    
    elif valor_func == "luzcuarto":
        print("cuarto")
        return 0
    
    elif valor_func == "luzbaño":
        print("baño")
        return 0
    
    elif valor_func == "luzsala":
        print("sala")
        return 0

    else:
        result = add_user(nuevo_data.strip())

    encrypt_file('./database/data.txt',
                 './database/data_encrypted.txt', key, iv)

#                                                      _____________________________________________________________________________________
# _____________________________________________________/ SI SE REQUIERE VER EL CONTENIDO DE LA BASE DE DATOS PARA PRUEBAS COMENTAR ESTA LÍNEA
    os.remove('./database/data.txt')
# _______________________________________________________  ES DE SUMA IMPORTANCIA VOLVER A PONERLA PARA CUMPLIR CON LO QUE EL CLIENTE SOLICITA

    print(" base de datos actualizada y cifrada, bade en plaintext eliminada")

    return result

    print(f"Valor de 'func': {valor_func}")
    print(f"String modificado: {nuevo_data.strip()}")


# receive_info(
#    "func: login, userEmail: juan@example.com, password: 5678")


# receive_info("func: login, userEmail: Juan123, password: 5678")

#                                                        _____________________________________________
# ______________________________________________________/ PRUEBAS de mensajes del cliente

# receive_info("func: rec, username: Tefa1, nombreProfe: Json, apodo: gogi, equipo: liga")


# receive_info(
#    "func: reg, username: Juan123, email: juan@example.com, password: 5678")


# recovery: "func: rec, username: Tefa1, nombreProfe: Json, apodo: gogi, equipo: liga"
# register: "func: reg, username: Juan123, email: juan@example.com, password: 5678"
# login: "func: login, username: Juan123, password: 5678"
# login: "func: login, email: juan@example.com, password: 5678"
