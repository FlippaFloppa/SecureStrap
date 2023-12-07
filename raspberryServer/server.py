import bluetooth
import os

server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_sock.bind(("DC:A6:32:5A:54:E0", bluetooth.PORT_ANY))
server_sock.listen(1)
port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
bluetooth.advertise_service(
server_sock, 
"SampleServer", 
service_id=uuid,
service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
profiles=[bluetooth.SERIAL_PORT_PROFILE],
# protocols=[bluetooth.OBEX_UUID]
)

while True:

	os.system('sudo bluetoothctl discoverable on')
	print("Waiting for connection on RFCOMM channel", port)
	client_sock, client_info = server_sock.accept()
	print("Accepted connection from", client_info)
	try:	
		while True:
			message = input("Enter message:")
			if message == "quit":
				break
			message = message + "#"
			client_sock.send(message.encode("utf-8"))
	except OSError:
		pass
	print("Disconnected.")
	client_sock.close()

server_sock.close()
print("All done.")