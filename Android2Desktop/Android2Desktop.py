#!/usr/bin/python 
# Server side python script to the companion Android 2 Desktop app  
# File location https://raw.github.com/pla1/Android2Desktop/master/Android2Desktop/Android2Desktop.py       

import socket,urlparse,sys,struct               
from subprocess import Popen

def getUrlString(the_socket):
    #data length is packed into 4 bytes
    total_len=0;total_data=[];size=sys.maxint
    size_data=sock_data='';recv_size=8192
    while total_len<size:
        sock_data=the_socket.recv(recv_size)
        if not total_data:
            if len(sock_data)>4:
                size_data+=sock_data
                size=struct.unpack('>i', size_data[:4])[0]
                recv_size=size
                if recv_size>524288:recv_size=524288
                total_data.append(size_data[4:])
            else:
                size_data+=sock_data
        else:
            total_data.append(sock_data)
        total_len=sum([len(i) for i in total_data ])
    return ''.join(total_data)
def startBrowser(urlString):
    urlString=urlString.replace("http://en.m.wikipedia.org","http://en.wikipedia.org")
    Popen(["chromium-browser", "--kiosk", urlString])
    #Popen(["google-chrome", "--kiosk", urlString])
    #Popen(["firefox", urlString])
def startYouTubePlayer(urlString):
    urlData=urlparse.urlparse(urlString)
    videoId=urlparse.parse_qs(urlData.query)["v"][0]
    print 'Video id is: ', videoId
    Popen(["/home/htplainf/youtubemplayer.sh", videoId])
    #Popen(["vlc","--fullscreen", urlString]) # If you don't want to mess with the YouTube mplayer script uncomment this line for VLC and comment the line above it. 

serverSocket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
PORT = 12345 
HOST = ''
serverSocket.bind((HOST, PORT))
serverSocket.listen(5)
while True:
   clientSocket, clientAddress = serverSocket.accept()
   print 'Connection from: ', clientAddress
   urlString=getUrlString(clientSocket)
   print 'Data from: ', clientAddress, urlString
   if "youtube" in urlString: 
     startYouTubePlayer(urlString)
   else:
     startBrowser(urlString)
   clientSocket.close()

