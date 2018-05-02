import move
import time
import network
import threading



def setup():
    mover = move.Move()
    mover.fight()
    mover.recharge()
    net = network.Network("", 8081)
    
    recThread = threading.Thread(target=net.startListening)
    recThread.start()

    while True:
        time.sleep(.1)
        if net.incomingVoiceText is not None:
            word = net.incomingVoiceText.lower()
            if "north" in word:
                mover.one_forward(2)
            if "south" in word:
                mover.one_backward(2)
            if "west" in word:
                mover.turn_left(1)
                mover.one_forward(2.5)
                mover.turn_right(.9)
            if "east" in word:
                mover.turn_right(1)
                mover.one_forward(2.5)
                mover.turn_left(1.1)
            if "fight" in word:
                mover.fight()
            if "recharge" in word:
                mover.recharge()
            net.incomingVoiceText = None

def testing():
    mover = move.Move()
    time.sleep(2)
    mover.one_forward(2)
    time.sleep(2)
    mover.one_backward(1.8)
    time.sleep(2)
    mover.turn_left(1)
    mover.one_forward(2.5)
    mover.turn_right(.8)
    time.sleep(2)
    mover.turn_right(1)
    mover.one_forward(2.5)
    mover.turn_left(1.1)
    time.sleep(2)
    mover.fight()
    time.sleep(2)
    mover.recharge()






if __name__ == '__main__':
    setup()
