import re


def receive_info(message):
    if char in message == "@":
        modified_message = original_string.replace("userEmail", "mail")
    else:
        modified_string = original_string.replace("userEmail", "username")
        
        
        
    with open('userInfo.txt', 'r') as file:
        password = get_password_from_string(mesage)
        username = get_username_from_string(mesage)
        
        for line in file:
            line = line.strip()  #borra saltos de linea
            db_password = get_password_from_string(line)
            db_username = get_username_from_string(line)
            
            if password == db_password and username == db_username:
                #coinciden
                return 1
        #no coinciden
        return 0
            
            

def get_username_from_string(message):
    match = re.search(r"mail:\s*(\S+)", line)
    if match:
        return match.group(1)
    return None

def get_password_from_string(message):
    match = re.search(r"password:\s*(\S+)", line)
    if match:
        return match.group(1)
    return None

            
           
            
           
        
        