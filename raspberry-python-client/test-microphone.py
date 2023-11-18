import speech_recognition as sr

r = sr.Recognizer()
print(sr.Microphone.list_microphone_names())
dev_index = int(input("Seleziona il microfono: "))
print("Selected device " + str(dev_index))
mic = sr.Microphone(device_index = dev_index) 

with mic as source:
    r.adjust_for_ambient_noise(source)
    print("Listening ...")
    audio = r.listen(source)

try:
    print("Messaggio: " + r.recognize_google(audio))
except Exception as e:
    print(e)