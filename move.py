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

#flags for driving
go_forward = False
go_backward = False

#driving forward speeds
one_forward = 5500
two_forward = 4500
three_forward = 3500

#driving backward speeds
one_backward = 6500
two_backward = 7500
three_backward = 8500

#neutral speed
neutral = 6000

#target numbers
body_target = 0
drive_target = 1
turn_target = 2
neck_target = 3
head_target = 4

#set targets to neutral
for i in range(5):
    servo.setTarget(i,neutral)
    servo.setSpeed(i,0)
    servo.setAccel(i,60)

servo.setAccel(drive_target, 6)

#head looks up
def w_pressed():
    servo.setTarget(head_target,9000)
def w_released():
    servo.setTarget(head_target,neutral)

#head looks down
def s_pressed():
    servo.setTarget(head_target,3000)
def s_released():
    servo.setTarget(head_target,neutral)

#neck turn left
def a_pressed():
    servo.setTarget(neck_target,8900)
def a_released():
    servo.setTarget(neck_target,neutral)
#neck turn right
def d_pressed():
    servo.setTarget(neck_target,3100)
def d_released():
    servo.setTarget(neck_target,neutral)

#body turn left
def q_pressed():
    servo.setTarget(body_target,8900)
def q_released():
    servo.setTarget(body_target,neutral)

#body turn right
def e_pressed():
    servo.setTarget(body_target,3100)
def e_released():
    servo.setTarget(body_target,neutral)

#go forwards -- driving
def up_pressed(speed=4500):
    go_forward = True
    servo.setTarget(drive_target,speed)
def up_released():
    go_forward = False
    servo.setTarget(drive_target,neutral)

#go backwards -- driving
def down_pressed(speed=7500):
    go_backward = True
    servo.setTarget(drive_target,speed)
def down_released():
    go_backward = False
    servo.setTarget(drive_target,neutral)

#turn left -- driving
def left_pressed():
    servo.setTarget(turn_target,7000)
def left_released():
    servo.setTarget(turn_target,neutral)

#turn right -- driving
def right_pressed():
    servo.setTarget(turn_target,5000)
def right_released():
    servo.setTarget(turn_target,neutral)

#set all servos/motors to neutral
def space_pressed():
    for i in range(5):
        servo.setTarget(i,neutral)

#set driving speed to speed 1
def one_pressed():
    if go_forward:
        up_pressed(one_forward)
    elif go_backward:
        down_pressed(one_backward)

#set driving speed to speed 2
def two_pressed():
    if go_forward:
        up_pressed(two_forward)
    elif go_backward:
        down_pressed(two_backward)

#set driving speed to speed 3
def three_pressed():
    if go_forward:
        up_pressed(three_forward)
    elif go_backward:
        down_pressed(three_backward)

#get value of key pressed, execute correct function
def key_pressed(e):
    key = e.keysym
    print(key)
    if(key == 'Escape'):
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
    elif(key == 'space'):
        space_pressed()
    elif(key == '1'):
        one_pressed()
    elif(key == '2'):
        two_pressed()
    elif(key == '3'):
        three_pressed()

#get value of key released, execute correct function
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
