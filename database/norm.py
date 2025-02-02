import unicodedata
import json

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
    print(entrada)

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
        if clave == "ubicacion":
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
    print(f"LOS DATOS HAN SIDO NORMALIZADOS: {datos_normalizados}")
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
