import time
import os
import signal
import sys
from threading import Thread
from Maestro import Controller
import tkinter as tk

servo = Controller()
root = tk.Tk()
os.system('xset r off')

for i in range(5):
    servo.setTarget(i,6000)
    servo.setSpeed(i,0)
    servo.setAccel(i,60)

servo.setAccel(1, 6)


def w_pressed():
    servo.setTarget(4,9000)
def w_released():
    servo.setTarget(4,6000)

def s_pressed():
    servo.setTarget(4,3000)
def s_released():
    servo.setTarget(4,6000)

def a_pressed():
    servo.setTarget(3,9000)
def a_released():
    servo.setTarget(3,6000)
def d_pressed():
    servo.setTarget(3,3000)
def d_released():
    servo.setTarget(3,6000)

def q_pressed():
    servo.setTarget(0,9000)
def q_released():
    servo.setTarget(0,6000)

def e_pressed():
    servo.setTarget(0,3000)
def e_released():
    servo.setTarget(0,6000)

def up_pressed():
    servo.setTarget(1,4500)
def up_released():
    servo.setTarget(1,6000)

def down_pressed():
    servo.setTarget(1,7500)
def down_released():
    servo.setTarget(1,6000)

def left_pressed():
    servo.setTarget(2,7000)
def left_released():
    servo.setTarget(2,6000)


def right_pressed():
    servo.setTarget(2,5000)
def right_released():
    servo.setTarget(2,6000)







def key_pressed(e):
    key = e.keysym
    print(key)
    if(key == 'escape'):
        os.system('xset r on')
        sys.exit(0)
    elif(key == 'Up'):
        up_pressed()
    elif(key == 'Down'):
        down_pressed()
    elif(key == 'Left'):
        left_pressed()
    elif(key == 'Right'):
        right_pressed()
    elif(key == 'w'):
        w_pressed()
    elif(key == 's'):
        s_pressed()
    elif(key == 'a'):
        a_pressed()
    elif(key == 'd'):
        d_pressed()
    elif(key == 'q'):
        q_pressed()
    elif(key == 'e'):
        e_pressed()




def key_released(e):
        key = e.keysym
        if(key == 'Up'):
            up_released()
        elif(key == 'Down'):
            down_released()
        elif(key == 'Left'):
            left_released()
        elif(key == 'Right'):
            right_released()
        elif(key == 'w'):
            w_released()
        elif(key == 's'):
            s_released()
        elif(key == 'a'):
            a_released()
        elif(key == 'd'):
            d_released()
        elif(key == 'q'):
            q_released()
        elif(key == 'e'):
            e_released()




root.bind('<KeyPress>', key_pressed)
root.bind('<KeyRelease>', key_released)
root.mainloop()





# for i in range(5):
#     print(i)
#     servo.setTarget(i,0)
#     servo.setSpeed(i,0)
#     servo.setAccel(i,2)
#
#
# servo.setTarget(1,6000)
# servo.setTarget(2,6000)
# time.sleep(1)
# i = 6000
# while i <= 8000:
#     print(i)
#     servo.setTarget(1, i)
#     i = i + 250
#     time.sleep(.5)
# while i >= 4000:
#     print(i)
#     servo.setTarget(1, i)
#     i = i - 250
#     time.sleep(.5)
#
# servo.setTarget(1, 6000)
# servo.setTarget(2, 6000)
#
# time.sleep(2)
# print("stage 1")
# time.sleep(3)
#
# servo.setTarget(0,8000)
# time.sleep(.5)
# servo.setTarget(3,8000)
# time.sleep(.5)
# servo.setTarget(4,8000)
# time.sleep(.5)
#
# print("stage 2")
# time.sleep(3)
#
# servo.setTarget(0,3000)
# time.sleep(.5)
# servo.setTarget(3,3000)
# time.sleep(.5)
# servo.setTarget(4,3000)
# time.sleep(.5)
#
# print("stage 3")
# time.sleep(3)
# servo.setTarget(0,6000)
# time.sleep(.5)
# servo.setTarget(1,6000)
# time.sleep(.5)
# servo.setTarget(2,6000)
# time.sleep(.5)
# servo.setTarget(3,6000)
# time.sleep(.5)
# servo.setTarget(4,6000)
#
# servo.close
