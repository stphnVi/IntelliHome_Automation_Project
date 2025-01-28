from Crypto.Cipher import AES
import os

key = os.urandom(32)  # AES-256
iv = os.urandom(16)   # Vector de inicialización


def encrypt_file(input_file, output_file, key, iv):
    cipher = AES.new(key, AES.MODE_CBC, iv)
    with open(input_file, 'rb') as f:
        plaintext = f.read()

    # Padding para que sea múltiplo de 16 bytes
    pad_length = 16 - (len(plaintext) % 16)
    plaintext += bytes([pad_length]) * pad_length

    ciphertext = cipher.encrypt(plaintext)

    with open(output_file, 'wb') as f:
        f.write(iv + ciphertext)
        # Guardar la clave en un archivo de texto
    with open('./database/key.txt', 'wb') as key_file:
        key_file.write(key)


encrypt_file('./database/data.txt',
             './database/data_encrypted.txt', key, iv)
