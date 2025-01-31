from twilio.rest import Client 

# Tus credenciales de Twilio
account_sid = ''
auth_token = ''
client = Client(account_sid, auth_token)

mensaje = 'Su casa se está incendiada'
mensaje_de = 'whatsapp:+14155238886'
mensaje_para = 'whatsapp:+50687127929'


#Enviar mensaje de WhatsApp
mensaje = client.messages.create(
    body=mensaje,
    from_=mensaje_de, #Número de WhatsApp de Twilio
    to=mensaje_para
)

print(f"Mensaje enviado: {mensaje.sid}")