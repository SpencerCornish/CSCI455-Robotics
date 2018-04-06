import socket
import threading
import time
# create a socket object


class Network:

    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.serverSocket = socket.socket(
            socket.AF_INET, socket.SOCK_STREAM)
        self.host = socket.gethostname()
        self.clientSocket = None

    def startListening(self):
        print("Starting net stuff")
        print(self.host)
        print(self.ip)
        print(self.port)
        print(self.serverSocket)
        self.serverSocket.bind((self.ip, self.port))
        self.serverSocket.listen()
        addr = None
        while True:
            # establish a connection
            self.clientSocket, addr = self.serverSocket.accept()
            print("Got a connection from %s" % str(addr))
            msg = 'Recieved your message!' + "\r\n"
            self.clientSocket.send(msg.encode('ascii'))

    def closeClientSocket(self):
        self.clientSocket.close()


if __name__ == '__main__':
    net = Network("", 8081)
    recThread = threading.Thread(target=net.startListening)
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
