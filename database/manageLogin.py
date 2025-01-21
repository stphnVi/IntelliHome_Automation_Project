import re


#                                                        ________________________________________________
# ______________________________________________________/función revisa si los credenciales son correctos


def login_info(message):
    print(f"Mensaje recibido: {message}")

    # Verificar si el mensaje contiene un correo electrónico
    if "@" in message:
        print("El string es un correo")
        modified_message = message.replace("userEmail", "email")
        email = get_email_from_string(modified_message)
    else:
        print("El string no es un correo, probablemente es un username")
        modified_message = message.replace("userEmail", "username")
        username = get_username_from_string(modified_message)

    password = get_password_from_string(modified_message)

    # Abrir el archivo
    with open('./database/data.txt', 'r') as file:
        for line in file:
            print(f"Línea de la base de datos: {line}")
            line = line.strip()  # Quitar saltos de línea

            # Obtener los valores de la base de datos
            if "@" in message:
                db_password = get_password_from_string(line)
                db_email = get_email_from_string(line)
                if password == db_password and email == db_email:
                    print("¡Coinciden las credenciales de correo!")
                    return "1"
                print(f"Correo extraído: {email}")
                print(f"Password extraído: {password}")
                print(f"db_pass: {db_password}")
                print(f"db_email: {db_email}")
            else:
                db_password = get_password_from_string(line)
                db_username = get_username_from_string(line)
                if password == db_password and username == db_username:
                    print("¡Coinciden las credenciales de usuario!")
                    return "1"
                print(f"Username extraído: {username}")
                print(f"Password extraído: {password}")
                print(f"db_pass: {db_password}")
                print(f"db_user: {db_username}")

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
                    return 1  # Todos los datos coinciden
        print("NO es el usuario")
        return 0  # No se encontraron coincidencias
    except Exception as e:
        print(f"Error: {e}")
        return 0


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
        login_info(nuevo_data.strip())
    elif valor_func == "rec":
        print("entra")
        questions(nuevo_data.strip())
    else:
        add_user(nuevo_data.strip())

    print(f"Valor de 'func': {valor_func}")
    print(f"String modificado: {nuevo_data.strip()}")


#                                                        _____________________________________________
# ______________________________________________________/ Pruebas de mensajes del cliente
receive_info("func: login, username: Juan123, password: 5678")

receive_info(
    "func: login, email: juan@example.com, password: 578")

# recovery: "func: rec, username: Tefa1, nombreProfe: Json, apodo: gogi, equipo: liga"
# register: "func: reg, username: Juan123, email: juan@example.com, password: 5678"
# login: "func: login, username: Juan123, password: 5678"
# login: "func: login, email: juan@example.com, password: 5678"
