import bluetooth
import os
import time
import Adafruit_SSD1306
import Adafruit_GPIO

import RPi.GPIO as GPIO # Import Raspberry Pi GPIO library

#----------------------------------------
#-----VARIABLES--------------------------
button_wait =2.5
button_pin = 10
rasp_mac_address = "DC:A6:32:5A:54:E0"
uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
#----------------------------------------

trigger = False

def button_handler(channel):
	global start_time, trigger
	if GPIO.input(button_pin):
		print("Button pressed")
		start_time = time.time()
	else:
		diff = time.time() - start_time
		print("Button released after " + str(diff))
		if diff >= button_wait:
			trigger = True

GPIO.setwarnings(False) # Ignore warning for now
GPIO.setmode(GPIO.BOARD) # Use physical pin numbering
GPIO.setup(button_pin, GPIO.IN, pull_up_down=GPIO.PUD_DOWN) # Set pin 10 to be an input pin and set initial value to be pulled low (off)
GPIO.add_event_detect(button_pin,GPIO.BOTH,callback=button_handler)

server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_sock.bind((rasp_mac_address, bluetooth.PORT_ANY))
server_sock.listen(1)
port = server_sock.getsockname()[1]

os.system('sudo bluetoothctl discoverable on')
bluetooth.advertise_service(
server_sock, 
"SampleServer", 
service_id=uuid,
service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
profiles=[bluetooth.SERIAL_PORT_PROFILE],
# protocols=[bluetooth.OBEX_UUID]
)

while True:

	print("Waiting for connection on RFCOMM channel", port)
	client_sock, client_info = server_sock.accept()
	print("Accepted connection from", client_info)
	try:	
		while True:

			if trigger:

				message = "notifica#"
				client_sock.send(message.encode("utf-8"))
				time.sleep(1/10)

				# message = "sms#"
				# client_sock.send(message.encode("utf-8"))
				# time.sleep(1/10)
				
				message = "chiama#"
				client_sock.send(message.encode("utf-8"))
				time.sleep(1/10)

				trigger = False
			
			time.sleep(1/100)
	except OSError:
		pass
	print("Disconnected.")
	client_sock.close()

server_sock.close()
print("All done.")