import speech_recognition as sr

r = sr.Recognizer()
print(sr.Microphone.list_microphone_names())
input("Premi enter")
mic = sr.Microphone() 

try:
    with mic as source:
        r.adjust_for_ambient_noise(source)
        while(True):
            print("Listening ...")
            audio = r.listen(source = source, timeout = 10, phrase_time_limit = 5)

            #mess = r.recognize_sphinx(audio, 'en-US')
            mess = r.recognize_sphinx(audio, 'it-IT')
            print("Messaggio: " + mess)

            if(mess=='basta'):
                break
        
except Exception as e:
    print(e)
