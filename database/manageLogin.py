import re
import math
from datetime import datetime, timedelta
from database.descifrado import *
from database.cifrado import *
import subprocess
import os
from database.norm import *
from geopy.geocoders import Nominatim
from geopy.exc import GeocoderTimedOut
import certifi
import ssl

ssl._create_default_https_context = ssl._create_unverified_context
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

#                                                        _____________________________________________________
# ______________________________________________________/Función que Agrega casas a la base de datos y las normaliza


def add_house(house_info):
    # print(f"Asi entra la info de la casa {house_info}")

    try:
        # Abrir el archivo en modo append (agregar al final)
        print("entra a add_house")
        house_info_normalizada = normalizar_casa(house_info)
        print(
            f'----------------->dato casa normalizado: {house_info_normalizada}')

        fechas = ", fechas: 02/04/2025-12/31/2025"
        house_info_final = house_info_normalizada + fechas
        with open('./database/casas.txt', 'a') as file:
            # Escribir la información de la casa en el archivo
            file.write("\n" + house_info_final)
        print("----------------->Casa agregada a la base de datos")
        return "1"
    except Exception as e:
        print(f"Error")


# texto = "nombre de la propiedad: CasaPruebaServer, ubi:Lat: 9.745042642538621, Lng: -84.09588088467201, reglas de uso: Ninguna, No perros, amenidades:Calefaccion, Lavadora y secadora, Piscina, capacidad maxima: 3, precio: 50000"
# ejemploNOSIRVE = "nombre de la propiedad: Casa1, Ubicacion:Lat: 9.745042642538621, Lng: -84.09588088467201, reglas de uso: Ninguna, amenidades:Calefaccion, Lavadora y secadora, Piscina, capacidad maxima: 3, precio: 50000"
# add_house(texto)
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
                    username = get_field_from_string(line, "username")
                    print(f"1: {username}")
                    return f"1: {username}"  # return de nombre de usuario
                    # return "1"
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
    # print(distancia)
    # print(f" distancia: {distancia}")
    return distancia

#                                                        ____________________________________________________
# ______________________________________________________/ Función auxiliar para Parseo de las fechas


def parsear_fechas(fechas_str):
    if not fechas_str.strip():  # Si la cadena está vacía, retornar una lista vacía
        return []

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
# ______________________________________________________/ Función auxiliar para Buscar las coordenadas de los
# cantones


def obtener_coordenadas_canton(canton):
    # print(canton)
    geolocator = Nominatim(scheme='https', user_agent="my_app")  # Usar HTTPS
    try:
        location = geolocator.geocode(canton + ", Costa Rica", timeout=3)
        if location:
            print(location)
            print(location.latitude, location.longitude)
            return (location.latitude, location.longitude)
        else:
            print("No se encontro ubicacion.")
            return (None, None)
    except GeocoderTimedOut as e:
        print("Error: geocode failed on input %s with message %s" %
              (canton, e.msg))
        return None

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
    # print(
    #    f"propiedades cargadas:  {propiedades}")
    return propiedades

#                                                        ____________________________________________________
# ______________________________________________________/ Función para Buscar propiedades de la base de datos
# e identificar coincidencias


def buscar_propiedades(propiedades, capacidad=None, precio=None, amenidades=None, ubi=None, fechas=None):
    resultados = []
    if ubi != -1 and ubi:

        lat_entrada, lon_entrada = obtener_coordenadas_canton(ubi)
        # print(
        #    f"coodenadas del canton {ubi} es:  {obtener_coordenadas_canton(ubi)}")

    for prop in propiedades:

        if capacidad != -1 and capacidad and prop.get("capacidad maxima") != str(capacidad):
            continue
        if precio != -1 and precio and int(prop.get("precio", 0)) > precio:
            continue

        if amenidades != -1 and amenidades:
            amenidades_db = prop.get("amenidades", [])
            if set(amenidades) != set(amenidades_db):
                continue

        if ubi != -1 and ubi:

            lat_prop, lon_prop = prop.get("ubi", (None, None))
            if lat_prop is not None and lon_prop is not None:
                distancia = calcular_distancia(
                    lat_entrada, lon_entrada, lat_prop, lon_prop)
                # print(lat_entrada, lon_entrada, lat_prop, lon_prop)
                if distancia > 50:
                    continue

        if fechas != -1 and fechas and not verificar_disponibilidad(prop.get("fechas", ""), fechas):
            continue

        resultados.append(
            f'nombre de la propiedad: {prop["nombre de la propiedad"]}, capacidad maxima: {prop["capacidad maxima"]}, precio: {prop["precio"]}, amenidades: {", ".join(prop["amenidades"])}, reglas:  {", ".join(prop["reglas"].split("; "))}')

    if not resultados:
        print("No se encontraron propiedades que cumplan con los criterios.")
        return "0"
    print(f"PROPIEDADES/PROPIEDAD ENCONTRADA----------------->: {resultados}")
    return "; ".join(resultados)

#                                                        _____________________________________________
# ______________________________________________________/ Función para recibir datos para su posterior
# consulta de alquileres


def recibir_datos_alquilar(entrada):
    # print(f'entro a recibir los datos normalizados para la búsqueda {entrada}')
    try:
        datos = eval(entrada)
        return buscar_propiedades(
            propiedades=cargar_propiedades("./database/casas.txt"),
            capacidad=datos.get('capacidad'),
            precio=datos.get('precio'),
            amenidades=datos.get('amenidades'),
            ubi=datos.get('ubi'),
            fechas=datos.get('fecha')
        )
    except Exception as e:
        return f"Error procesando los datosss: {e}"


# Ejemplo de uso
# datos_entrada = '{"capacidad": -1, "precio": -1, "amenidades": -1, "ubi": -1, "fecha": -1}'

# datos_entrada = "{'capacidad': -1, 'precio': 1000, 'amenidades': -1, 'ubi': 'cieneguita limon', 'fecha': -1}"

# datos_entrada = '{"capacidad": -1, "precio": 4000, "amenidades": -1, "ubi": "liberia", "fecha": -1}'
# print(recibir_datos_alquilar(datos_entrada))


#                                                        _____________________________________________
# ______________________________________________________/ Función para actualizar fechas en la base de
# datos según se solicitó
#

def actualizar_fechas_reservadas(nombre_archivo, nombre_propiedad, fecha_reservada):
    propiedades = cargar_propiedades(nombre_archivo)

    # Convertimos la fecha reservada en formato datetime
    if "-" in fecha_reservada:
        inicio_res, fin_res = fecha_reservada.split("-")
        fecha_reservada = (datetime.strptime(
            inicio_res, "%m/%d/%Y"), datetime.strptime(fin_res, "%m/%d/%Y"))
    else:
        fecha_reservada = datetime.strptime(fecha_reservada, "%m/%d/%Y")

    for prop in propiedades:
        if prop["nombre de la propiedad"] == nombre_propiedad:
            fechas_actuales = parsear_fechas(prop.get("fechas", ""))
            nuevas_fechas = []

            for fecha in fechas_actuales:
                if isinstance(fecha, tuple):  # Es un rango de fechas
                    if isinstance(fecha_reservada, tuple):  # Reservando un rango
                        # Si la reserva cubre completamente el rango, eliminarlo
                        if fecha_reservada[0] <= fecha[0] and fecha_reservada[1] >= fecha[1]:
                            continue

                        elif fecha_reservada[0] <= fecha[0] <= fecha_reservada[1]:
                            nuevo_inicio = fecha_reservada[1] + \
                                timedelta(days=1)
                            if nuevo_inicio == fecha[1]:
                                nuevas_fechas.append(nuevo_inicio.strftime(
                                    '%m/%d/%Y'))
                            else:
                                nuevas_fechas.append(
                                    f"{nuevo_inicio.strftime('%m/%d/%Y')}-{fecha[1].strftime('%m/%d/%Y')}")

                        # Ajuste si la reserva toca el final del rango
                        elif fecha_reservada[0] <= fecha[1] <= fecha_reservada[1]:
                            nuevo_fin = fecha_reservada[0] - timedelta(days=1)
                            if nuevo_fin == fecha[0]:
                                nuevas_fechas.append(nuevo_fin.strftime(
                                    '%m/%d/%Y'))
                            else:
                                nuevas_fechas.append(
                                    f"{fecha[0].strftime('%m/%d/%Y')}-{nuevo_fin.strftime('%m/%d/%Y')}")

                        # Si la reserva está dentro del rango, dividirlo
                        elif fecha[0] < fecha_reservada[0] and fecha_reservada[1] < fecha[1]:
                            nuevas_fechas.append(
                                f"{fecha[0].strftime('%m/%d/%Y')}-{(fecha_reservada[0] - timedelta(days=1)).strftime('%m/%d/%Y')}")
                            nuevas_fechas.append(
                                f"{(fecha_reservada[1] + timedelta(days=1)).strftime('%m/%d/%Y')}-{fecha[1].strftime('%m/%d/%Y')}")

                        else:
                            nuevas_fechas.append(
                                f"{fecha[0].strftime('%m/%d/%Y')}-{fecha[1].strftime('%m/%d/%Y')}")

                    else:  # Reservando una sola fecha dentro de un rango
                        if fecha[0] < fecha_reservada < fecha[1]:
                            nuevas_fechas.append(
                                f"{fecha[0].strftime('%m/%d/%Y')}-{(fecha_reservada - timedelta(days=1)).strftime('%m/%d/%Y')}")
                            nuevas_fechas.append(
                                f"{(fecha_reservada + timedelta(days=1)).strftime('%m/%d/%Y')}-{fecha[1].strftime('%m/%d/%Y')}")

                        elif fecha_reservada == fecha[0]:
                            nuevo_inicio = fecha[0] + timedelta(days=1)
                            if nuevo_inicio == fecha[1]:
                                nuevas_fechas.append(nuevo_inicio.strftime(
                                    '%m/%d/%Y'))  # Convertir a fecha única
                            else:
                                nuevas_fechas.append(
                                    f"{nuevo_inicio.strftime('%m/%d/%Y')}-{fecha[1].strftime('%m/%d/%Y')}")

                        elif fecha_reservada == fecha[1]:
                            nuevo_fin = fecha[1] - timedelta(days=1)
                            if nuevo_fin == fecha[0]:
                                nuevas_fechas.append(nuevo_fin.strftime(
                                    '%m/%d/%Y'))  # Convertir a fecha única
                            else:
                                nuevas_fechas.append(
                                    f"{fecha[0].strftime('%m/%d/%Y')}-{nuevo_fin.strftime('%m/%d/%Y')}")

                        else:
                            nuevas_fechas.append(
                                f"{fecha[0].strftime('%m/%d/%Y')}-{fecha[1].strftime('%m/%d/%Y')}")

                else:  # Es una fecha única
                    if fecha != fecha_reservada:
                        nuevas_fechas.append(fecha.strftime('%m/%d/%Y'))

            prop["fechas"] = "; ".join(nuevas_fechas)

    # Guardamos las propiedades actualizadas en el archivo
    with open(nombre_archivo, "w", encoding="utf-8") as file:
        for prop in propiedades:
            line = f'nombre de la propiedad: {prop["nombre de la propiedad"]}, capacidad maxima: {prop["capacidad maxima"]}, precio: {prop["precio"]}, amenidades: {", ".join(prop["amenidades"])}, ubi: {prop["ubi"][0]}; {prop["ubi"][1]}, fechas: {prop["fechas"]}\n'
            file.write(line)


# Reservar un solo día
# actualizar_fechas_reservadas("./database/test.txt", "caribe", "02/15/2025")

# Reservar un rango de fechas
# actualizar_fechas_reservadas("base_de_datos.txt", "Caribe", "02/10/2025-02/12/2025")
# actualizar_fechas_reservadas(
    # "./database/test.txt", "caribe", "02/11/2025")

#                                                        _____________________________________________
# ______________________________________________________/función Auxiliar de cambio de contraseña


def get_nueva_contrasena_from_string(data):
    match = re.search(r"nuevaC:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None
#                                                        _____________________________________________
# ______________________________________________________/función de cambio de contraseña


def change_password(datos_entrada):
    try:
        # Extraer el username y la nueva contraseña del string de entrada
        print(datos_entrada)
        username = get_username_from_string(datos_entrada)
        nueva_contrasena = get_nueva_contrasena_from_string(datos_entrada)
        print(username)
        print(nueva_contrasena)

        if not username or not nueva_contrasena:
            print("Formato de entrada inválido.")
            return "0"  # Fallo

        # Abrir el archivo en modo lectura para leer todas las líneas
        with open("./database/data.txt", "r") as file:
            lineas = file.readlines()

        usuario_encontrado = False
        for linea in lineas:
            current_username = get_username_from_string(linea)
            if current_username == username:
                usuario_encontrado = True
                break

        if not usuario_encontrado:
            print("El nombre de usuario no existe.")
            return "2"  # Usuario no encontrado

        # Abrir el archivo en modo escritura para actualizar los datos
        with open("./database/data.txt", "w") as file:
            for linea in lineas:

                current_username = get_username_from_string(linea)
                if current_username == username:
                    # Obtener la contraseña actual
                    password_actual = get_password_from_string(linea)
                    if password_actual:
                        # Reemplazar la contraseña
                        linea = linea.replace(
                            f"password: {password_actual}", f"password: {nueva_contrasena}")
                    else:
                        print(
                            f"No se encontró la contraseña para el usuario {username}.")
                        return "0"  # Fallo
                # Escribir la línea (actualizada o sin cambios) en el archivo
                file.write(linea)

        print("Contraseña actualizada correctamente.")
        return "1"
    except Exception as e:
        print(f"Error al cambiar la contraseña: {e}")
        return "0"


#                                                        _____________________________________________
# ______________________________________________________/función que recibe los mensajes del cliente


def receive_info(data):

    # desencriptar base de datos

    key = load_key('./database/key.txt')

    decrypt_file('./database/data_encrypted.txt',
                 './database/data.txt', key)
    decrypt_file('./database/casas_encrypted.txt',
                 './database/casas.txt', key)

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

    print(valor_func)

    if valor_func == "login":
        result = login_info(nuevo_data.strip())

    elif valor_func == "rec":
        print("entra")
        result = questions(nuevo_data.strip())
    elif valor_func == "cpass":
        print("cambiar contrasena")
        result = change_password(nuevo_data.strip())

    elif valor_func == "buscarCasa":
        print("ENTRA A BUSCAR CASAS")

        result = recibir_datos_alquilar(normalizar_datos(nuevo_data.strip()))

    elif valor_func == "regcasa":
        result = add_house(nuevo_data.strip())

    elif valor_func == "luzcuarto":
        print("cuarto")
        return 0

    elif valor_func == "luzbaño":
        print("baño")
        return 0

    elif valor_func == "luzsala":
        print("sala")
        return 0
    
    elif valor_func == "pgarage":
        print("pgarage")
        return 0

    else:
        result = add_user(nuevo_data.strip())

    encrypt_file('./database/data.txt',
                 './database/data_encrypted.txt', key, iv)
    encrypt_file('./database/casas.txt',
                 './database/casas_encrypted.txt', key, iv)

#                                                      _____________________________________________________________________________________
# _____________________________________________________/ SI SE REQUIERE VER EL CONTENIDO DE LA BASE DE DATOS PARA PRUEBAS COMENTAR ESTA LÍNEA

    os.remove('./database/data.txt')
    os.remove('./database/casas.txt')
# _______________________________________________________  ES DE SUMA IMPORTANCIA VOLVER A PONERLA PARA CUMPLIR CON LO QUE EL CLIENTE SOLICITA

    print(" bases de datos actualizadas y cifradas, bases en plaintext eliminada")

    return result

    print(f"Valor de 'func': {valor_func}")
    print(f"String modificado: {nuevo_data.strip()}")


# receive_info(
#    "func: login, userEmail: juan@example.com, password: 5678")


# receive_info("func: login, userEmail: Juan123, password: 5678")

#                                                        _____________________________________________
# ______________________________________________________/ PRUEBAS de mensajes del cliente


# receive_info(
#    "func: cpass, username: jose1234, nuevaC: 123454123")

# receive_info(
#    "func: rec, username: Tefa1, nombreProfe: Json, apodo: gogi, equipo: liga")


# receive_info(
#    "func: reg, username: Juan123, email: juan@example.com, password: 5678")


# recovery: "func: rec, username: Tefa1, nombreProfe: Json, apodo: gogi, equipo: liga"
# register: "func: reg, username: Juan123, email: juan@example.com, password: 5678"
# login: "func: login, username: Juan123, password: 5678"
# login: "func: login, email: juan@example.com, password: 5678"
