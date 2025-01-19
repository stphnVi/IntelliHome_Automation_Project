import re


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
            db_password = get_password_from_string(line)
            db_username = get_username_from_string(line)

            # Comparar el username y password con los del archivo
            print(f"Mensaje modificado: {modified_message}")
            print(f"Username extraído: {username}")
            print(f"Password extraído: {password}")
            if password == db_password and username == db_username:

                print("¡Coinciden!")
                return 1

    # Si no hay coincidencia
        print("No coinciden")
        return 0


def get_username_from_string(data):
    # Extraer el username (en este caso, mail) del string
    match = re.search(r"username:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None


def get_password_from_string(data):
    # Extraer el password del string
    match = re.search(r"password:\s*(\S+)", data)
    if match:
        return match.group(1)
    return None
