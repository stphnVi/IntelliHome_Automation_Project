import re

#                                                        _____________________________________________
# ______________________________________________________/función que recibe los mensajes del cliente


def receive_info(message):
    print(message)
    # Reemplazar 'userEmail' por 'mail' en todo el mensaje
    modified_message = message.replace("userEmail", "username")

    # Obtener los valores de username y password del mensaje modificado
    username = get_username_from_string(modified_message)
    password = get_password_from_string(modified_message)

    if not username or not password:
        print("Error: Datos inválidos")
        return 0

    # Abrir el archivo para comprobar las credenciales
    with open('./database/data.txt', 'r') as file:
        for line in file:
            print(line)
            line = line.strip()  # Quitar saltos de línea
            # obtener los valores de la base de datos
            db_password = get_password_from_string(line)
            db_username = get_username_from_string(line)

            # Comparar el username y password con los del archivo
            print(f"Mensaje modificado: {modified_message}")
            print(f"Username extraído: {username}")
            print(f"Password extraído: {password}")
            print(f"db_pass: {db_password}")
            print(f"db_user: {db_username}")
            if password == db_password and username == db_username:

                print("¡Coinciden!")
                return "1"

    # Si no hay coincidencia
        print("No coinciden")
        return "0"

#                                                        _____________________________________________
# ______________________________________________________/Auxiliar de login, obtiene username


def get_username_from_string(data):
    # Extraer el username (en este caso, mail) del string
    match = re.search(r"username:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None
#                                                        _____________________________________________
# ______________________________________________________/Auxiliar de login, obtiene password


def get_password_from_string(data):
    # Extraer el password del string
    match = re.search(r"password:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None

#                                                        _____________________________________________
# ______________________________________________________/Agregar usuario a la base de datos


def add_user(user_info):
    try:
        # Abrir el archivo en modo append (agregar al final)
        with open('./database/data.txt', 'a') as file:
            # Escribir la información del usuario en el archivo
            file.write(user_info + "\n")
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
# ______________________________________________________/Recuperacion de contraseña

def questions(user_info):
    try:

        with open("./database/data.txt", "r") as file:
            for line in file:
                # Comparar cada campo del usuario con la línea en el archivo
                if all(get_field_from_string(user_info, field) == get_field_from_string(line, field)
                       for field in ["username", "nombreProfe", "apodo", "equipo"]):
                    print("si es el usuario")
                    return 1  # Todos los datos coinciden
        print("no es el usuario")
        return 0  # No se encontraron coincidencias
    except Exception as e:
        print(f"Error: {e}")
        return 0


# Ejemplo de uso:
# questions("username: Tefa1, nombreProfe: json, apodo: gogi, equipo: liga")

# add_user("username: Tefa1, email: tefa@protonmail.com, password: 1234")
# add_user("username: Juan123, email: juan@example.com, password: 5678")
