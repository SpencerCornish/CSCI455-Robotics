import socket
import threading
import time
import binascii
import socket

# create a socket object


class Network:

    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.serverSocket = socket.socket(
            socket.AF_INET, socket.SOCK_STREAM)
        self.host = socket.gethostname()
        self.phoneIp = None

    def startListening(self):
        print("Starting net stuff")
        print(self.host)
        print(self.ip)
        print(self.port)
        print(self.serverSocket)
        self.serverSocket.bind((self.ip, self.port))
        self.serverSocket.listen(5)
        while True:
            self.clientSocket, self.phoneIp = self.serverSocket.accept()
            print("Got a connection from %s" % str(self.phoneIp))
            incomingString = ""
            while True:
                print("recv")
                incoming = self.clientSocket.recv(64).decode("utf8")
                print("incoming: " + incoming)
                incomingString += incoming
                break

            print(incomingString)
            print("Sendit")
            bytesToSend = "Holy mold"
            self.sendMessage(bytesToSend.encode("utf8"))

    def closeSocket(self):
        self.clientSocket.close()

    def sendMessage(self, message):
        if self.phoneIp is None:
            print("No Handshake yet! Yikes!")
            return
        print("Sending a message")
        print(self.clientSocket)
        print(self.phoneIp)
        self.clientSocket.sendall(message)


if __name__ == '__main__':
    net = Network("", 8091)
    recThread = threading.Thread(target=net.startListening,)
    recThread.start()
    recThread.join()
    print("thread finished...exiting")

  # else:
    #     # Wait here for incoming data after we've established stuff
    #     dataRecieved = self.clientSocket.recv(1024)
    #     print(dataRecieved)
    #     msg = 'OK' + "\r\n"
    #     self.clientSocket.send(msg.encode('ascii'))
    #     # This is where we should process the incoming data from the Pi
