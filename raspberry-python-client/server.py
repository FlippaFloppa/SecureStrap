import bluetooth
import os
import time
import board
import busio
from PIL import Image, ImageDraw, ImageFont
import adafruit_ssd1306
import RPi.GPIO as GPIO # Import Raspberry Pi GPIO library
import datetime

#----------------------------------------
#-----VARIABLES--------------------------
button_wait =2.5
button_pin = 15
rasp_mac_address = "DC:A6:32:5A:54:E0"
uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"
#----------------------------------------

trigger = False

def draw_text(text):
	global draw
	oled.fill(0)

	draw.rectangle((0, 0, oled.width, oled.height), outline=255, fill=255)

	draw.rectangle(
		(BORDER, BORDER, oled.width - BORDER - 1, oled.height - BORDER - 1),
		outline=0,
		fill=0,
	)

	font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 18)

	(font_width, font_height) = font.getsize(text)
	draw.text(
		(oled.width // 2 - font_width // 2, oled.height // 2 - font_height // 2),
		text,
		font=font,
		fill=255,
	)

	oled.image(image)
	oled.show()


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
#GPIO.setmode(GPIO.BOARD) # Use physical pin numbering
GPIO.setup(button_pin, GPIO.IN, pull_up_down=GPIO.PUD_DOWN) # Set pin 10 to be an input pin and set initial value to be pulled low (off)
GPIO.add_event_detect(button_pin,GPIO.BOTH,callback=button_handler)

i2c = busio.I2C(board.SCL, board.SDA)
oled = adafruit_ssd1306.SSD1306_I2C(128, 32, i2c)
BORDER = 2
image = Image.new("1", (oled.width, oled.height))
draw = ImageDraw.Draw(image)


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
	draw_text("Waiting...")

	client_sock, client_info = server_sock.accept()
	print("Accepted connection from", client_info)

	draw_text("Connected!")
	time.sleep(2)

	try:	
		while client_sock is not None:

			current_time = datetime.datetime.now()
			draw_text(f'{current_time.hour}:{current_time.minute}:{current_time.second}')

			if trigger:

				draw_text("-- SOS --")

				message = "notifica#"
				client_sock.send(message.encode("utf-8"))
				time.sleep(5/10)

				# message = "sms#"
				# client_sock.send(message.encode("utf-8"))
				# time.sleep(1/10)
				
				message = "chiama#"
				client_sock.send(message.encode("utf-8"))
				time.sleep(2/10)

				trigger = False
				time.sleep(3)
			
			time.sleep(1/10)
	except ConnectionResetError:
		print("Disconnected.")
		client_sock.close()
		break
	except OSError:
		pass

server_sock.close()
print("All done.")