from tkinter import dnd, messagebox
import tkinter as tkinter
import move
import time
import network
import threading


#class for window main gui runs on
#overrides methods in the tkinter.dnd class
class RobotGui:

    def __init__(self, root):
        self.top = tkinter.Toplevel(root)
        self.canvas = tkinter.Canvas(self.top, width=700, height=400)
        self.canvas.pack(fill="both", expand=1)
        self.canvas.dnd_accept = self.dnd_accept

    def dnd_accept(self, source, event):
        return self

    def dnd_enter(self, source, event):
        x, y = source.where(self.canvas, event)
        x1, y1, x2, y2 = source.canvas.bbox(source.id)
        dx, dy = x2-x1, y2-y1
        self.dndid = self.canvas.create_rectangle(x, y, x+dx, y+dy, fill="#6eb1d7",outline="#3090C7", width=1.5)
        self.dnd_motion(source, event)

    def dnd_motion(self, source, event):
        x, y = source.where(self.canvas, event)
        x1, y1, x2, y2 = self.canvas.bbox(self.dndid)
        self.canvas.move(self.dndid, x-x1, y-y1)

    def dnd_leave(self, source, event):
        self.top.focus_set() # Hide highlight border
        self.canvas.delete(self.dndid)
        self.dndid = None

    def dnd_commit(self, source, event):
        self.dnd_leave(source, event)
        x, y = source.where(self.canvas, event)
        target = self.canvas.find_closest(x+37,y+10)

        if target[0] < 17 and target[0] > 8:
            blocks[target[0]-8].changeMotionName(event.widget['text'], self.canvas)
        source.putback()

#class for blocks that motion labels can be dragged into
#initialize as empty with motionid of -1
#can only be changed once
class Block:
    def __init__(self, number, x, y, name="EMPTY", motionid=-1):
        self.number = number
        self.name = name
        self.motionid = motionid
        self.canvas = self.label = self.id = None
        self.x = x
        self.y = y
        self.degrees = None
        self.seconds = None
        self.direction = None

    #add block to canvas with empty setting
    def addBlock(self, canvas):
        if canvas is self.canvas:
            self.canvas.coords(self.id, self.x, self.y)
            return
        label = tkinter.Label(canvas, text=self.name,
                              borderwidth=2, bg="grey", relief="sunken", highlightcolor="#6eb1d7", width="8", height="6")
        id = canvas.create_window(self.x, self.y, window=label, anchor="nw")
        self.canvas = canvas
        self.label = label
        self.id = id

    #update block id/name when motion dragged into it
    #update label to reflect new name
    def changeMotionName(self, motionName, canvas):
        self.name = motionName
        label = tkinter.Label(canvas, text=self.name,
                               borderwidth=2, bg="#6eb1d7", relief="sunken", highlightcolor="#6eb1d7", width="8", height="6")
        id = canvas.create_window(self.x, self.y, window=label, anchor="nw")
        self.canvas = canvas
        self.label = label
        self.motionid = motions[self.name]

#class for draggable labels that represent motions robot can complete
#overrides methods in the tkinter.dnd class
class Motion:
    def __init__(self, name):
        self.name = name
        self.canvas = None
        self.label = None
        self.id = None

    #add motion to canvas
    def addMotion(self, canvas, x, y):
        if canvas is self.canvas:
            self.canvas.coords(self.id, x, y)
            return
        if self.canvas:
            self.putback()
        if not canvas:
            return
        label = tkinter.Label(canvas, text=self.name,
                              borderwidth=2, relief="raised", width="8", height="2",bg="#3090C7",fg="white")
        id = canvas.create_window(x, y, window=label, anchor="nw")
        self.canvas = canvas
        self.label = label
        self.id = id
        label.bind("<ButtonPress>", self.drag)

    def putback(self):
        self.canvas.coords(self.id, self.x_orig, self.y_orig)

    def move(self, event):
        x, y = self.where(self.canvas, event)
        self.canvas.coords(self.id, x, y)

    def drag(self, event):
        if tkinter.dnd.dnd_start(self, event):
            self.x_off = event.x
            self.y_off = event.y
            self.x_orig, self.y_orig = self.canvas.coords(self.id)
    def dnd_end(self, target, event):
        pass
    #find coordinates of motion icon
    def where(self, canvas, event):
        x_org = canvas.winfo_rootx()
        y_org = canvas.winfo_rooty()
        x = event.x_root - x_org
        y = event.y_root - y_org
        return x - self.x_off, y - self.y_off

#class for configuring motions in blocks
class Driver:
    def __init__(self, canvas, root, net):
        self.canvas = canvas
        self.root = root
        self.var = tkinter.IntVar()
        self.motionList = {}
        self.up = {}
        self.down = {}
        self.forward = {}
        self.backward = {}
        self.left = {}
        self.right = {}
        self.seconds = {}
        self.talk = {}
        self.listen = {}
        self.net = net

    #Get motion ids for all blocks
    def getMotionList(self):
        for i in range(1,9):
            self.motionList[i] = blocks[i].motionid
        self.getParameters(self.motionList)

    #open new window
    #provide user options based on motions selected
    def getParameters(self, motionList):
        optionWindow = tkinter.Toplevel(self.root)

        for i in range(1,9):
            #for NECK, BODY, TURN
            if motionList[i] == 2 or motionList[i] == 3 or motionList[i] == 5:
                tkinter.Label(optionWindow, text="Left or Right for " + blocks[i].name).grid(row=i-1, column=0, sticky=tkinter.W)
                self.left[i] = tkinter.IntVar()
                tkinter.Checkbutton(optionWindow, text="Left", variable = self.left[i]).grid(row=i-1, column = 1, sticky=tkinter.W)
                self.right[i] = tkinter.IntVar()
                tkinter.Checkbutton(optionWindow, text="Right", variable=self.right[i]).grid(row=i-1, column=2, sticky=tkinter.W)
            #for TURN
            if motionList[i] == 5:
                tkinter.Label(optionWindow,text="Seconds: "+blocks[i].name).grid(row=i-1, column=3, sticky=tkinter.W)
                self.seconds[i] = tkinter.Scale(optionWindow, from_=0, to=10, orient=tkinter.HORIZONTAL)
                self.seconds[i].grid(row=i-1, column =4, sticky=tkinter.W)
            #for HEAD
            elif motionList[i] == 1:
                tkinter.Label(optionWindow, text="Up or Down for " + blocks[i].name).grid(row=i-1, column=0, sticky=tkinter.W)
                self.up[i] = tkinter.IntVar()
                self.down[i] = tkinter.IntVar()
                tkinter.Checkbutton(optionWindow, text="Up", variable=self.up[i]).grid(row=i-1, column=1, sticky=tkinter.W)
                tkinter.Checkbutton(optionWindow, text="Down", variable=self.down[i]).grid(row=i-1, column=2, sticky=tkinter.W)
            #for DRIVE
            elif motionList[i] == 4:
                tkinter.Label(optionWindow, text="Forward or Backward for "+ blocks[i].name).grid(row=i-1,column=0, sticky=tkinter.W)
                self.forward[i] = tkinter.IntVar()
                self.backward[i] = tkinter.IntVar()
                tkinter.Checkbutton(optionWindow, text="Forward", variable=self.forward[i]).grid(row=i-1, column=1, sticky=tkinter.W)
                tkinter.Checkbutton(optionWindow, text="Backward", variable=self.backward[i]).grid(row=i-1, column=2, sticky=tkinter.W)
                tkinter.Label(optionWindow,text="Seconds: ").grid(row=i-1, column=3, sticky=tkinter.W)
                self.seconds[i] = tkinter.Scale(optionWindow, from_=0, to=10, orient=tkinter.HORIZONTAL)
                self.seconds[i].grid(row=i-1, column =4, sticky=tkinter.W)
            #for PAUSE
            elif motionList[i] == 6:
                tkinter.Label(optionWindow,text="Seconds for "+blocks[i].name).grid(row=i-1, column=0, sticky=tkinter.W)
                self.seconds[i] = tkinter.Scale(optionWindow, from_=0, to=10, orient=tkinter.HORIZONTAL)
                self.seconds[i].grid(row=i-1, column =1, sticky=tkinter.W)
            #for TALK
            elif motionList[i] == 7:
                tkinter.Label(optionWindow, text="Say: ").grid(row=i-1, column=0, sticky=tkinter.W)
                self.talk[i] = tkinter.StringVar()
                tkinter.Radiobutton(optionWindow, text="Nothing Here", variable=self.talk[i], value="Nothing Here").grid(row=i-1, column=1, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="OH WOW", variable=self.talk[i], value="Oh wow").grid(row=i-1, column=2, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="I'm left", variable=self.talk[i], value="I'm left").grid(row=i-1, column=3, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="I'm right", variable=self.talk[i], value="I'm right").grid(row=i-1, column=4, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="Oops", variable=self.talk[i], value="Oops").grid(row=i-1, column=5, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="That's cool", variable=self.talk[i], value="That's cool").grid(row=i-1, column=6, sticky=tkinter.W)
            #for listen
            elif motionList[i] == 8:
                tkinter.Label(optionWindow, text="Listen for: ").grid(row=i-1,column=0, sticky=tkinter.W)
                self.listen[i] = tkinter.StringVar()
                tkinter.Radiobutton(optionWindow, text="Go home", variable=self.listen[i], value="Go home").grid(row=i-1, column=3, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="Look left", variable=self.listen[i], value="look left").grid(row=i-1, column=5, sticky=tkinter.W)
                tkinter.Radiobutton(optionWindow, text="Look right", variable=self.listen[i], value="look right").grid(row=i-1, column=6, sticky=tkinter.W)

        okButton = tkinter.Button(optionWindow, command=self.setValues, text="START")
        cancelButton = tkinter.Button(optionWindow, command=lambda: self.var.set(1), text="CANCEL")
        okButton.grid(row=9,column=1)
        cancelButton.grid(row=9,column=0)
        okButton.wait_variable(self.var)
        optionWindow.destroy()

    #send commands to method in Move class that executes motions
    def setValues(self):
        arg = "start"
        self.net.getSpeechInput(arg)
        mover = move.Move()
        self.var.set(1)
        for i in range(1,9):
            #for HEAD
            if self.motionList[i] == 1:
                if self.up[i].get() == 1:
                    print("move head up")
                    mover.executeMotion(self.motionList[i], self.up[i].get(), 0)
                    time.sleep(1)
                    # mover.reset()
                #default down
                else:
                    print("move head down")
                    mover.executeMotion(self.motionList[i], 0, 0)
                    time.sleep(1)
                    # mover.reset()
            #for NECK
            elif self.motionList[i] == 2:
                if self.left[i].get() == 1:
                    print("move head left")
                    mover.executeMotion(self.motionList[i], self.left[i].get(), 0)
                    time.sleep(1)
                    # mover.reset()
                #default right
                else:
                    print("move head right")
                    mover.executeMotion(self.motionList[i], 0, 0)
                    time.sleep(1)
                    # mover.reset()
            #for BODY
            elif self.motionList[i] == 3:
                if self.left[i].get() == 1:
                    print("move body left")
                    mover.executeMotion(self.motionList[i], self.left[i].get(), 0)
                    time.sleep(1)
                    # mover.reset()
                #default right
                else:
                    print("move body right")
                    mover.executeMotion(self.motionList[i], 0, 0)
                    time.sleep(1)
                    # mover.reset()
            #For DRIVE
            elif self.motionList[i] == 4:
                seconds = self.seconds[i].get()
                if self.forward[i].get() == 1:
                    print("drive forward seconds", seconds)
                    mover.executeMotion(self.motionList[i], self.forward[i].get(), seconds)
                    time.sleep(1)
                #default Backwards
                else:
                    print("drive backwards Seconds", seconds)
                    mover.executeMotion(self.motionList[i], 0, seconds)
                    time.sleep(1)
            #for TURN
            elif self.motionList[i] == 5:
                seconds = self.seconds[i].get()
                if self.left[i].get() == 1:
                    print("turn left for seconds", seconds)
                    mover.executeMotion(self.motionList[i], self.left[i].get(), seconds)
                    time.sleep(1)
                #default right
                else:
                    print("turn right for seconds", seconds)
                    mover.executeMotion(self.motionList[i], 0, seconds)
                    time.sleep(1)
            #for pause
            elif self.motionList[i] == 6:
                seconds = self.seconds[i].get()
                print("sleep seconds", seconds)
                mover.executeMotion(self.motionList[i], 0, seconds)
            #for talk
            # '''EDIT HERE'''
            ###############
            elif self.motionList[i] == 7:
                print(str(self.talk[i].get()))
                arg = str(self.talk[i].get())
                netThread = threading.Thread(target=self.net.sendMessage, args=(arg,))
                netThread.start()
            #for listen
            # '''EDIT HERE'''
            ###############

            elif self.motionList[i] == 8:
                self.net.getSpeechInput(str(self.listen[i].get()))
                if str(self.listen[i].get()) == "Look right":
                    print("move head right")
                    mover.executeMotion(2, 0, 0)
                    time.sleep(1)
                    # mover.reset()
                elif str(self.listen[i].get()) == "Look left":
                    print("move head left")
                    mover.executeMotion(2, 1, 0)
                    time.sleep(1)
                    # mover.reset()
            #reset robot servos to neutral positon
            # mover.reset()


#8 blocks  to drag motions into. Default is empty
blocks = {}
for i in range(1,9):
    blocks[i] = Block(i, (i-1)*85+10, 100)
#list of motion names with motion ids
motions = {"HEAD U/D":1, "NECK L/R":2, "BODY L/R":3, "DRIVE F/B":4, "TURN L/R":5, "PAUSE":6, "TALK":7, "LISTEN":8}


def setup():
    net = network.Network("", 8081)
    recThread = threading.Thread(target=net.startListening)
    recThread.start()
    #add root window
    root = tkinter.Tk()
    root.geometry("0x0")
    root.geometry("+0+0")
    #add new window to add everything to.
    t1 = RobotGui(root)
    #add motion labels
    motion1 = Motion("HEAD U/D")
    motion2 = Motion("NECK L/R")
    motion3 = Motion("BODY L/R")
    motion4 = Motion("DRIVE F/B")
    motion5 = Motion("TURN L/R")
    motion6 = Motion("PAUSE")
    motion7 = Motion("TALK")
    motion8 = Motion("LISTEN")
    motion1.addMotion(t1.canvas, 10, 10)
    motion2.addMotion(t1.canvas, 95, 10)
    motion3.addMotion(t1.canvas, 180, 10)
    motion4.addMotion(t1.canvas, 265, 10)
    motion5.addMotion(t1.canvas, 350, 10)
    motion6.addMotion(t1.canvas, 435, 10)
    motion7.addMotion(t1.canvas, 520, 10)
    motion8.addMotion(t1.canvas, 605, 10)
    driver = Driver(t1.canvas, root, net)
    quitButton = tkinter.Button(t1.canvas, command=root.quit, text="QUIT")
    #doesn't work yet
    clearButton = tkinter.Button(t1.canvas, text="CLEAR")
    configureButton = tkinter.Button(t1.canvas, command=driver.getMotionList, text="CONFIGURE")
    quitButton.place(x=200,y=250)
    clearButton.place(x=300,y=250)
    configureButton.place(x=400,y=250)
    #add blocks motions can be dragged into
    for i in range(1,9):
        blocks[i].addBlock(t1.canvas)
    root.mainloop()

if __name__ == '__main__':
    setup()
