import move
import time
import network
import threading



def setup():
    mover = move.Move()
    net = network.Network("", 8081)
    recThread = threading.Thread(target=net.startListening)
    recThread.start()
    mover.fight(2)
    time.sleep(1000)
    mover.recharge(2)
    while True:
        if net.incomingVoiceText is not None:
            if net.incomingVoiceText is "forward":
                mover.one_forward(1)
            if net.incomingVoiceText is "backwards":
                mover.one_backward(1)
            if net.incomingVoiceText is "left":
                mover.turn_left(1)
                mover.one_forward(1)
            if net.incomingVoiceText is "right":
                mover.turn_right(1)
                mover.one_forward(1)
            if net.incomingVoiceText is "fight":
                mover.fight(2)
            if net.incomingVoiceText is "recharge":
                mover.recharge(2)

if __name__ == '__main__':
    setup()
