from Crypto.Cipher import AES
import os

# Leer la clave desde el archivo


def load_key(key_file):
    with open(key_file, 'rb') as f:
        key = f.read()  # Leer la clave en formato binario
    return key


def decrypt_file(input_file, output_file, key):
    with open(input_file, 'rb') as f:
        iv = f.read(16)
        ciphertext = f.read()

    cipher = AES.new(key, AES.MODE_CBC, iv)
    plaintext = cipher.decrypt(ciphertext)

    # Remover el padding
    pad_length = plaintext[-1]
    plaintext = plaintext[:-pad_length]

    with open(output_file, 'wb') as f:
        f.write(plaintext)


# Uso
key = load_key('./database/key.txt')

decrypt_file('./database/data_encrypted.txt',
             './database/data.txt', key)
