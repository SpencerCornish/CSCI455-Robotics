import time
import os
import signal
import sys
from threading import Thread
from Maestro import Controller
import tkinter as tk

#flags for driving
go_forward = False
go_backward = False

global forward_speed
forward_speed = 4500

global backward_speed
backward_speed = 7500

global head_angle
head_angle = 6000

global neck_angle
neck_angle = 6000

#driving forward speeds
one_forward = 5300
two_forward = 4800
three_forward = 3000

#driving backward speeds
one_backward = 6700
two_backward = 7200
three_backward = 8000

#neutral speed
neutral = 6000

#target numbers
body_target = 0
drive_target = 1
turn_target = 2
neck_target = 3
head_target = 4

class Move:
    def __init__(self):
        self.servo = Controller()
        #self.root = tk.Tk()
        os.system('xset r off')
        #set targets to neutral
        for i in range(17):
            self.servo.setTarget(i,neutral)
            self.servo.setSpeed(i,0)
            self.servo.setAccel(i,60)

        self.servo.setAccel(drive_target, 6)

    #move head up
    def w_pressed(self):
        global head_angle
        if head_angle < 9000:
            head_angle += 1500
        self.servo.setTarget(head_target, head_angle)
    #move head down
    def s_pressed(self):
        global head_angle
        if head_angle > 3000:
            head_angle -= 1500
        self.servo.setTarget(head_target, head_angle)
    #move neck left
    def a_pressed(self):
        global neck_angle
        if neck_angle < 9000:
            neck_angle += 1500
        self.servo.setTarget(neck_target, neck_angle)
    #move neck right
    def d_pressed(self):
        global neck_angle
        if neck_angle > 3000:
            neck_angle -= 1500
        self.servo.setTarget(neck_target, neck_angle)
    #body turn left
    def q_pressed(self):
        self.servo.setTarget(body_target,8600)
    def q_released(self):
        self.servo.setTarget(body_target,neutral)
    #body turn right
    def e_pressed(self):
        self.servo.setTarget(body_target,3400)
    def e_released(self):
        self.servo.setTarget(body_target,neutral)
    #drive forward
    def one_forward(self, seconds):
        self.servo.setTarget(drive_target,one_forward)
        time.sleep(seconds)
        self.servo.setTarget(drive_target,neutral)
    #drive backwards
    def one_backward(self, seconds):
        self.servo.setTarget(drive_target,one_backward)
        time.sleep(seconds)
        self.servo.setTarget(drive_target,neutral)
    #turn left
    def turn_left(self, seconds):
        self.servo.setTarget(turn_target,7000)
        time.sleep(seconds)
        self.servo.setTarget(turn_target,neutral)
    #turn right
    def turn_right(self, seconds):
        self.servo.setTarget(turn_target,5000)
        time.sleep(seconds)
        self.servo.setTarget(turn_target,neutral)

    def fight(self):
        self.servo.setTarget(head_target, head_angle)
        self.servo.setTarget(6, 3000)
        self.servo.setTarget(12, 3000)
        time.sleep(.5)
        self.servo.setTarget(head_target, neutral)
        self.servo.setTarget(head_target, 3000)
        self.servo.setTarget(6, 8000)
        self.servo.setTarget(12, 8000)
        time.sleep(.5)
        self.servo.setTarget(head_target, neutral)
        self.servo.setTarget(head_target, head_angle)
        self.servo.setTarget(6, 3000)
        self.servo.setTarget(12, 3000)
        time.sleep(.5)
        self.servo.setTarget(head_target, neutral)
        self.servo.setTarget(head_target, 3000)
        self.servo.setTarget(6, 8000)
        self.servo.setTarget(12, 8000)
        time.sleep(.5)
        self.servo.setTarget(head_target, neutral)
        self.servo.setTarget(head_target, head_angle)
        self.servo.setTarget(6, neutral)
        self.servo.setTarget(12, neutral)

    def recharge(self):
        self.servo.setTarget(neck_target, head_angle)
        self.servo.setTarget(9, 3000)
        self.servo.setTarget(16, 3000)
        time.sleep(.5)
        self.servo.setTarget(neck_target, neutral)
        self.servo.setTarget(neck_target, 3000)
        self.servo.setTarget(9, 8000)
        self.servo.setTarget(16, 8000)
        time.sleep(.5)
        self.servo.setTarget(neck_target, neutral)
        self.servo.setTarget(neck_target, head_angle)
        self.servo.setTarget(9, 3000)
        self.servo.setTarget(16, 3000)
        time.sleep(.5)
        self.servo.setTarget(neck_target, neutral)
        self.servo.setTarget(neck_target, 3000)
        self.servo.setTarget(9, 8000)
        self.servo.setTarget(16, 8000)
        time.sleep(.5)
        self.servo.setTarget(neck_target, neutral)
        self.servo.setTarget(neck_target, head_angle)
        self.servo.setTarget(9, neutral)
        self.servo.setTarget(16, neutral)


    #set all servos/motors to neutral
    def space_pressed(self):
        for i in range(5):
            self.servo.setTarget(i,neutral)
    def executeMotion(self, mid, direction, seconds):
        #HEAD
        if mid == 1:
            #if up
            if direction == 1:
                #move head up
                self.w_pressed()
            else:
                #move head down
                self.s_pressed()
        #NECK
        elif mid == 2:
            if direction == 1:
                #neck turn left
                self.a_pressed()
            else:
                #neck turn right
                self.d_pressed()
        #BODY
        elif mid == 3:
            if direction ==1:
                #body turn left
                self.q_pressed()
            else:
                #body turn right
                self.e_pressed()
        #DRIVE
        elif mid == 4:
            if direction == 1:
                #drive forward
                self.one_forward(seconds)
            else:
                #drive backward
                self.one_backward(seconds)
        #TURN
        elif mid == 5:
            if direction == 1:
                self.turn_left(seconds)
            else:
                self.turn_right(seconds)
        #PAUSE
        elif mid == 6:
            time.sleep(seconds)



# root.bind('<KeyPress>', key_pressed)
# root.bind('<KeyRelease>', key_released)
# root.mainloop()


#head looks up
# def w_pressed():turn
#     servo.setTarget(head_target,9000)
# def w_released():
#     servo.setTarget(head_target,neutral)
#
# #head looks down
# def s_pressed():
#     servo.setTarget(head_target,3000)
# def s_released():
#     servo.setTarget(head_target,neutral)
#
# #neck turn left
# def a_pressed():
#     servo.setTarget(neck_target,8900)
# def a_released():
#     servo.setTarget(neck_target,neutral)
# #neck turn right
# def d_pressed():
#     servo.setTarget(neck_target,3100)
# def d_released():
#     servo.setTarget(neck_target,neutral)

# #go forwards -- driving
# def up_pressed():
#     go_forward = True
#     servo.setTarget(drive_target,forward_speed)
# def up_released():
#     go_forward = False
#     servo.setTarget(drive_target,neutral)
#
# #go backwards -- driving
# def down_pressed():
#     go_backward = True
#     servo.setTarget(drive_target,backward_speed)
# def down_released():
#     go_backward = False
#     servo.setTarget(drive_target,neutral)

# #turn left -- driving
# def left_pressed():
#     servo.setTarget(turn_target,7000)
# def left_released():
#     servo.setTarget(turn_target,neutral)
#
#
#
# #turn right -- driving
# def right_pressed():
#     servo.setTarget(turn_target,5000)
# def right_released():
#     servo.setTarget(turn_target,neutral)

# #set driving speed to speed 1
# def one_pressed():
#     global forward_speed
#     global backward_speed
#
#     forward_speed = one_forward
#     backward_speed = one_backward
#     if go_forward:
#         up_released()
#         up_pressed()
#     elif go_backward:
#         down_released()
#         down_pressed()
#
#
#
# #set driving speed to speed 2
# def two_pressed():
#     global forward_speed
#     global backward_speed
#     forward_speed = two_forward
#     backward_speed = two_backward
#     if go_forward:
#         up_released()
#         up_pressed()
#     elif go_backward:
#         down_released()
#         down_pressed()
#
# #set driving speed to speed 3
# def three_pressed():
#     forward_speed = three_forward
#     backward_speed = three_backward
#     if go_forward:
#         up_released()
#         up_pressed()
#     elif go_backward:
#         down_released()
#         down_pressed()
#
# #get value of key pressed, execute correct function
# def key_pressed(e):
#     key = e.keysym
#     print(key)
#     if(key == 'Escape'):
#         os.system('xset r on')
#         sys.exit(0)
#     elif(key == 'Up'):
#         up_pressed()
#     elif(key == 'Down'):
#         down_pressed()
#     elif(key == 'Left'):
#         left_pressed()
#     elif(key == 'Right'):
#         right_pressed()
#     elif(key == 'w'):
#         w_pressed()
#     elif(key == 's'):
#         s_pressed()
#     elif(key == 'a'):
#         a_pressed()
#     elif(key == 'd'):
#         d_pressed()
#     elif(key == 'q'):
#         q_pressed()
#     elif(key == 'e'):
#         e_pressed()
#     elif(key == 'space'):
#         space_pressed()
#     elif(key == '1'):
#         one_pressed()
#     elif(key == '2'):
#         two_pressed()
#     elif(key == '3'):
#         three_pressed()
#
# #get value of key released, execute correct function
# def key_released(e):
#     key = e.keysym
#     if(key == 'Up'):
#         up_released()
#     elif(key == 'Down'):
#         down_released()
#     elif(key == 'Left'):
#         left_released()
#     elif(key == 'Right'):
#         right_released()
#     elif(key == 'w'):
#         w_released()
#     elif(key == 's'):
#         s_released()
#     elif(key == 'a'):
#         a_released()
#     elif(key == 'd'):
#         d_released()
#     elif(key == 'q'):
#         q_released()
#     elif(key == 'e'):
#         e_released()
