import socket
import os
from dotenv import load_dotenv

load_dotenv()

server = socket.socket(socket.AF_BLUETOOTH, socket.SOCK_STREAM, socket.BTPROTO_RFCOMM)

server.bind((os.getenv("BLUETOOTH_SERVER_MAC_ADDR"), 4))
server.listen(1)
print("Listening for connections...")

client, address = server.accept()
print(f"Connected to {address}")
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