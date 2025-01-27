import socket
import threading
import tkinter as tk
from tkinter import scrolledtext
from database.manageLogin import *
import serial


class ChatServer:
    def __init__(self, host='0.0.0.0', port=1717):
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.bind((host, port))
        self.server_socket.listen(5)
        self.clients = []
        self.arduino = None
        
        #Conectarse al puerto serial
        serialPort = 'COM6'
        try:
            self.arduino = serial.Serial(serialPort, 9600)
            print(f"Conectado al puerto serial {serialPort}")
        except serial.SerialException as e:
            print(f"Error al abrir el puerto serial: {e}")
            

        # Configuración de la interfaz gráfica
        self.root = tk.Tk()
        self.root.title("Servidor de Chat")

        self.chat_display = scrolledtext.ScrolledText(
            self.root, state='disabled', width=50, height=20)
        self.chat_display.pack(pady=10)

        self.message_entry = tk.Entry(self.root, width=40)
        self.message_entry.pack(pady=5)

        self.send_button = tk.Button(
            self.root, text="Enviar", command=self.send_message_thread)
        self.send_button.pack(pady=5)

        self.quit_button = tk.Button(
            self.root, text="Salir", command=self.close_server)
        self.quit_button.pack(pady=5)

        # Para que sea en hilo separado
        self.thread = threading.Thread(target=self.accept_connections)
        self.thread.start()

        self.root.protocol("WM_DELETE_WINDOW", self.close_server)
        self.root.mainloop()

    def accept_connections(self):
        while True:  # Este while es para siempre escuchar nuevos clientes
            client_socket, addr = self.server_socket.accept()
            self.clients.append(client_socket)
            self.chat_display.config(state='normal')
            self.chat_display.insert(tk.END, f"Conexión de {addr}\n")
            self.chat_display.config(state='disabled')
            threading.Thread(target=self.handle_client,
                             args=(client_socket,)).start()
#                                                        ___________________________________________________________
# ______________________________________________________/Inicializacion de la ecucha de mensajes

    def handle_client(self, client_socket):

        while True:  # Siempre estar atento a recibir mensajes de cualquier cliente
            try:
                message = client_socket.recv(1024).decode(
                    'utf-8')  # recibe los mensajes
                if message:

                    self.broadcast(message, client_socket)

#                                                        ___________________________________________________________
# ______________________________________________________/llamada a la función que envia mensajes directos al cliente
# con el mensaje de retorno de la función que maneja el mensaje del cliente

                    resultado = receive_info(message) + "\n"
                    self.send_direct_message(
                        client_socket, resultado)

                else:
                    break
                
            except:
                break

        client_socket.close()
        self.clients.remove(client_socket)

#                                                        ___________________________________________________________
# ______________________________________________________/Envía un mensaje a todos los clientes conectados,
# excepto al que lo envió.

    def broadcast(self, message, sender_socket):
        self.chat_display.config(state='normal')
        self.chat_display.insert(tk.END, f"Cliente: {message}\n")
        
        if(self.arduino != None):
            self.arduino.write(message.encode('utf-8')) #Enviar los mensajes recividos via puerto serial
        
        self.chat_display.config(state='disabled')

        for client in self.clients:  # para cada cliente que haya
            if client != sender_socket:  # No enviar al remitente
                try:
                    client.send(message.encode('utf-8'))  # envia el mensaje
                except:
                    client.close()
                    self.clients.remove(client)

#                                                        ___________________________________________________________
# ______________________________________________________/Envía un mensaje a todos los clientes conectados,
# incluyendo al remitente.

    def broadcast1(self, message, sender_socket):
        self.chat_display.config(state='normal')
        self.chat_display.insert(tk.END, f"Servidor: {message}\n")
        
        if(self.arduino != None):
            self.arduino.write(message.encode('utf-8')) #Enviar los mensajes recividos via puerto serial
        
        self.chat_display.config(state='disabled')

        for client in self.clients:
            try:
                client.send(message.encode('utf-8'))
            except:
                client.close()
                self.clients.remove(client)

#                                                        ___________________________________________________________
# ______________________________________________________/Envía un mensaje del servidor a todos los clientes en un
#  hilo separado.

    def send_message_thread(self):
        # Se debe agregar \n para que termine la cadena que se requiere enviar
        threading.Thread(target=self.broadcast1(
            self.message_entry.get() + "\n", None)).start()
        self.message_entry.delete(0, tk.END)

#                                                        ___________________________________________________________
# ______________________________________________________/Envía un mensaje del servidor a todos los clientes desde
# la interfaz gráfica.

    def send_message_to_clients(self):  # parte grafica
        message = self.message_entry.get()
        if message:
            self.broadcast(f"Servidor: {message}", None)
            self.message_entry.delete(0, tk.END)

#                                                        ___________________________________________________________
# ______________________________________________________/ función que envia mensajes directos al cliente

    def send_direct_message(self, client_socket, message):

        try:
            client_socket.send(message.encode('utf-8'))
            self.chat_display.config(state='normal')
            self.chat_display.insert(
                tk.END, f"Mensaje enviado a cliente: {message}\n")
            self.chat_display.config(state='disabled')
        except:
            self.chat_display.config(state='normal')
            self.chat_display.insert(
                tk.END, f"No se pudo enviar el mensaje a este cliente\n")
            self.chat_display.config(state='disabled')

    def close_server(self):
        for client in self.clients:
            client.close()
        if(self.arduino != None):
            self.arduino.close()
        self.server_socket.close()
        self.root.destroy()


if __name__ == "__main__":
    server = ChatServer()
