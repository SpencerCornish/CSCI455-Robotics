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
        self.phonePort = 8082

    def startListening(self):
        print("Starting net stuff")
        print(self.host)
        print(self.ip)
        print(self.port)
        print(self.serverSocket)
        self.serverSocket.bind((self.ip, self.port))
        self.serverSocket.listen(0)
        addr = None
        while True:
            clientSocket, addr = self.serverSocket.accept()
            print("Got a connection from %s" % str(addr))
            self.phoneIp = addr
            incoming = clientSocket.recv(1024)
            incomingString = binascii.b2a_uu(incoming)
            print(incomingString)
            print(incoming)
            msg = 'Recieved your message!' + "\r\n"
            clientSocket.send(msg.encode('ascii'))
            clientSocket.close()

    def sendMessage(self, message):
        if self.phoneIp is None:
            print("No Handshake yet! Yikes!")
            return
        print("Sending string " + message + "to " +
              self.phoneIp + ":" + self.phonePort)
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((self.phoneIp, self.phonePort))
        sock.send(message)
        sock.close()


# if __name__ == '__main__':
    # net = Network("", 8081)
    # recThread = threading.Thread(target=net.startListening,)
    # recThread.start()
#     recThread.join()
#     print("thread finished...exiting")

  # else:
    #     # Wait here for incoming data after we've established stuff
    #     dataRecieved = self.clientSocket.recv(1024)
    #     print(dataRecieved)
    #     msg = 'OK' + "\r\n"
    #     self.clientSocket.send(msg.encode('ascii'))
    #     # This is where we should process the incoming data from the Pi
