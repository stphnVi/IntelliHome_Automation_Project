import unicodedata
import json
import re

#                                                        ___________________________________________________________
# ______________________________________________________/Normalización de los datos que llegan del cliente
# para consultas


def normalizar_texto(texto: str):

    if texto == "-1":
        return texto
    texto = texto.strip().lower()  # Convertir a minúsculas y eliminar espacios extra
    texto = unicodedata.normalize('NFKD', texto).encode(
        'ASCII', 'ignore').decode("utf-8")  # Elimina acentos
    return texto


def procesar_amenidades(valor: str):

    if valor == "-1":
        return -1
    return [normalizar_texto(a.strip()) for a in valor.split(",") if a.strip()]


def procesar_fecha(valor: str):

    if valor == "-1":
        return -1
    return [valor]  # La fecha debe ser una lista con un solo elemento


def normalizar_datos(entrada: str):
    # print(f"entrada------------: {entrada}")

    datos_normalizados = {
        "capacidad": -1,
        "precio": -1,
        "amenidades": -1,
        "ubi": -1,
        "fecha": -1
    }

    elementos = entrada.split(";")
    temp_dict = {}

    for elemento in elementos:
        if ":" in elemento:
            clave, valor = elemento.split(":", 1)
            clave = normalizar_texto(clave.strip())
            valor = valor.strip()

            if clave in temp_dict:

                temp_dict[clave] += "; " + valor
            else:
                temp_dict[clave] = valor

    for clave, valor in temp_dict.items():
        if clave == "ubi":
            datos_normalizados["ubi"] = normalizar_texto(
                valor) if valor != "-1" else -1
        elif clave == "capacidad":
            datos_normalizados["capacidad"] = int(
                valor) if valor.isdigit() else -1
        elif clave == "precio":
            datos_normalizados["precio"] = int(
                valor) if valor.isdigit() else -1
        elif clave == "fecha":
            datos_normalizados["fecha"] = procesar_fecha(valor)
        elif clave == "amenidades":
            datos_normalizados["amenidades"] = procesar_amenidades(valor)
    resultado = json.dumps(datos_normalizados, ensure_ascii=False)
    print(
        f"LOS DATOS HAN SIDO NORMALIZADOS----------------------->: {datos_normalizados}")
    return resultado


# Ejm
# entrada1 = 'Ubicacion:San José; amenidades:Lavadora y secadora, Aire acondicionado, Barbacoa o parrilla, Gimnasio en casa, Wi-Fi gratuito; fecha: 20/02/2025-14/02/2025; capacidad:7; precio:25000'
# entrada2 = 'Ubicacion:-1; amenidades:-1; fecha: -1; capacidad:-1; precio:-1'
# entrada3 = 'Ubicacion:liberia; amenidades:Lavadora y secadora, Barbacoa o parrilla, Wi-Fi gratuito, Terraza o balcón; fecha: -1; capacidad:5; precio:-1'
# entrada4 = 'Ubicacion:liberia; amenidades:Lavadora y secadora, Barbacoa o parrilla, Wi-Fi gratuito, Terraza o balcón; fecha: 20/02/2025-21/02/2025; capacidad:5; precio:-1'
# print(entrada2)
# print(normalizar_datos(entrada1))
# print(normalizar_datos(entrada2))
# print(normalizar_datos(entrada3))
# print(normalizar_datos(entrada4))

# entrada4 = "ubi:-1; amenidades:-1; fecha: -1; capacidad:-1; precio:-1"

# print(normalizar_datos(entrada4))


#                                                        ________________________________________________
# ______________________________________________________/ NORMALIZACIÓN PARA LA BASE DE DATOS DE LAS CASAS


def formatear_lista(texto):
    items = [item.strip() for item in texto.split(",")]
    # Concatenar los items y eliminar el último punto y coma, si es necesario
    return "; ".join(items).rstrip("; ")


def extraer_amenidades(texto):
    match = re.search(r"amenidades:(.*?)capacidad maxima", texto)
    if match:
        return formatear_lista(match.group(1).strip())
    return ""


def extraer_reglas(texto):
    match = re.search(r"reglas de uso: (.*?)amenidades", texto)
    if match:
        return formatear_lista(match.group(1).strip())
    return ""


def normalizar_casa(texto):
    # Extraer nombre de la propiedad
    nombre = re.search(r"nombre de la propiedad: (.*?),", texto).group(1)

    # Extraer latitud y longitud
    ubi_match = re.search(r"ubi:Lat: ([\d.\-]+), Lng: ([\d.\-]+)", texto)
    lat, lng = ubi_match.groups()

    # Extraer reglas de uso
    reglas = extraer_reglas(texto)

    # Extraer y formatear amenidades
    amenidades = extraer_amenidades(texto)

    # Extraer capacidad máxima
    capacidad = re.search(r"capacidad maxima: (\d+),", texto).group(1)

    # Extraer precio
    precio = re.search(r"precio: (\d+)", texto).group(1)

    # Formatear el string
    resultado = (f"nombre de la propiedad: {nombre}, "
                 f"ubi: {lat}; {lng}, "
                 f"reglas: {reglas}, "
                 f"amenidades: {amenidades}, "
                 f"capacidad maxima: {capacidad}, "
                 f"precio: {precio}")

    return resultado


# Ejemplo de uso
# texto = "nombre de la propiedad: Casa1, ubi:Lat: 9.745042642538621, Lng: -84.09588088467201, reglas de uso: Ninguna, No perros, amenidades:Calefaccion, Lavadora y secadora, Piscina, capacidad maxima: 3, precio: 50000"
# print(normalizar_casa(texto))
