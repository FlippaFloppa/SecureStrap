import socket
import os
from dotenv import load_dotenv

load_dotenv()

client = socket.socket(socket.AF_BLUETOOTH, socket.SOCK_STREAM, socket.BTPROTO_RFCOMM)

client.connect((os.getenv("BLUETOOTH_SERVER_MAC_ADDR"), 4))

try:
    while True:
        message = input("Enter message: ")
        if message == "quit":
            break
        client.send(message.encode("utf-8"))
        
except OSError:
    pass

client.close()