import socket
import os
from dotenv import load_dotenv

load_dotenv()

server = socket.socket(socket.AF_BLUETOOTH, socket.SOCK_STREAM, socket.BTPROTO_RFCOMM)

server.bind((os.getenv("BLUETOOTH_MAC_ADDRESS"), 4))
server.listen(1)

client, address = server.accept()

try:
    while True:
        data = client.recv(1024)
        if not data:
            break
        print(f"Message:  {data.decode()}")
    
except OSError:
    pass

client.close()
server.close()