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

    def startListening(self):
        print("Starting net stuff")
        print(self.host)
        print(self.ip)
        print(self.port)
        print(self.serverSocket)
        self.serverSocket.bind((self.ip, self.port))
        self.serverSocket.listen()
        clientsocket = None
        addr = None
        while True:
            # establish a connection
            clientsocket, addr = self.serverSocket.accept()

            print("Got a connection from %s" % str(addr))

            msg = 'Greetings!' + "\r\n"
            clientsocket.send(msg.encode('ascii'))
            time.sleep(2)
            msg = 'Greetings!' + "\r\n"
            clientsocket.send(msg.encode('ascii'))
            clientsocket.close()


if __name__ == '__main__':
    net = Network("", 8081)
    thread = threading.Thread(target=net.startListening)
    thread.start()
    thread.join()
    print("thread finished...exiting")
