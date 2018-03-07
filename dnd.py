from tkinter import dnd
import tkinter as tkinter

global animation_window
global animation
class Motion:

    def __init__(self, name):
        self.name = name
        self.canvas = self.label = self.id = None

    def addMotion(self, canvas, x, y):
        if canvas is self.canvas:
            self.canvas.coords(self.id, x, y)
            return
        if self.canvas:
            self.putback()
        if not canvas:
            return
        label = tkinter.Label(canvas, text=self.name,
                              borderwidth=2, relief="raised", width="6", height="2",bg="#3090C7",fg="white")
        id = canvas.create_window(x, y, window=label, anchor="nw")
        self.canvas = canvas
        self.label = label
        self.id = id
        label.bind("<ButtonPress>", self.drag)

    def drag(self, event):
        if tkinter.dnd.dnd_start(self, event):
            # where the pointer is relative to the label widget:
            self.x_off = event.x
            self.y_off = event.y
            # where the widget is relative to the canvas:
            self.x_orig, self.y_orig = self.canvas.coords(self.id)

    def move(self, event):
        x, y = self.where(self.canvas, event)
        self.canvas.coords(self.id, x, y)

    def putback(self):
        self.canvas.coords(self.id, self.x_orig, self.y_orig)

    def where(self, canvas, event):
        # where the corner of the canvas is relative to the screen:
        x_org = canvas.winfo_rootx()
        y_org = canvas.winfo_rooty()
        # where the pointer is relative to the canvas widget:
        x = event.x_root - x_org
        y = event.y_root - y_org
        # compensate for initial pointer offset
        return x - self.x_off, y - self.y_off

    def dnd_end(self, target, event):
        pass

class RobotGui:

    def __init__(self, root):
        self.top = tkinter.Toplevel(root)
        self.canvas = tkinter.Canvas(self.top, width=600, height=400)
        self.canvas.pack(fill="both", expand=1)
        self.canvas.dnd_accept = self.dnd_accept

    def dnd_accept(self, source, event):
        return self

    def dnd_enter(self, source, event):
        self.canvas.focus_set() # Show highlight border
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
        target = self.canvas.find_closest(x+30,y+10)
        blocks[target[0]-5].changeMotionName(event.widget['text'], self.canvas)
        source.putback()

class Block:
    def __init__(self, number, x, y, name="EMPTY", motionid=-1):
        self.number = number
        self.name = name
        self.motionid = motionid
        self.canvas = self.label = self.id = None
        self.x = x
        self.y = y

    def addBlock(self, canvas):
        if canvas is self.canvas:
            self.canvas.coords(self.id, self.x, self.y)
            return
        label = tkinter.Label(canvas, text=self.name,
                              borderwidth=2, bg="grey", relief="sunken", highlightcolor="#6eb1d7", width="6", height="5")
        id = canvas.create_window(self.x, self.y, window=label, anchor="nw")
        self.canvas = canvas
        self.label = label
        self.id = id

    def changeMotionName(self, motionName, canvas):
        self.name = motionName
        label = tkinter.Label(canvas, text=self.name,
                               borderwidth=2, bg="#6eb1d7", relief="sunken", highlightcolor="#6eb1d7", width="6", height="5")
        canvas.create_window(self.x, self.y, window=label, anchor="nw")
        self.canvas = canvas
        self.label = label
        self.motionid = motions[self.name]

blocks = {}
for i in range(1,9):
    blocks[i] = Block(i, (i-1)*70+10, 100)
motions = {"HEAD":1, "NECK":2, "BODY":3, "DRIVE":4, "TURN":5}

class Animation:
    def __init__(self, root):
        self.top = tkinter.Toplevel(root)
        self.canvas = tkinter.Canvas(self.top, width=600, height=400)
        self.canvas.pack(fill="both", expand=1)
        self.canvas.create_oval(75, 75, 285, 285, fill='black')
        self.canvas.create_oval(325, 75, 535, 285, fill='black')
        self.shape_eyeball_l = self.canvas.create_oval(80, 80, 280, 280, fill='white')
        self.shape_eyeball_r = self.canvas.create_oval(330, 80, 530, 280, fill='white')
        self.pupil_l = self.canvas.create_oval(120,120,140,140, fill='blue')
        self.pupil_r = self.canvas.create_oval(390,180,410,200, fill='blue')
        self.speedy_l = 8
        self.speedy_r = 8
        self.active = True
        self.move_active()

    def pupil_update(self):
        self.canvas.move(self.pupil_l, 0, self.speedy_l)
        self.canvas.move(self.pupil_r, 0, self.speedy_r)
        pos = self.canvas.coords(self.pupil_l)
        if pos[3] >= 220 or pos[1] <= 120:
            self.speedy_l *= -1
        pos = self.canvas.coords(self.pupil_r)

        if pos[3] >= 220 or pos[1] <= 120:
            self.speedy_r *= -1

    def move_active(self):
        if self.active:
            self.pupil_update()
            self.canvas.after(40, self.move_active) # changed from 10ms to 30ms




def render_fun_animation():
    animation = tkinter.Tk()
    animation.geometry("0x0")
    animation.geometry("+0+0")
    animation_window = Animation(animation)
    animation.mainloop()



def test():
    root = tkinter.Tk()
    root.geometry("0x0")
    root.geometry("+0+0")
    tkinter.Button(command=root.quit, text="Quit").pack()
    t1 = RobotGui(root)
    motion1 = Motion("HEAD")
    motion2 = Motion("NECK")
    motion3 = Motion("BODY")
    motion4 = Motion("DRIVE")
    motion5 = Motion("TURN")
    motion1.addMotion(t1.canvas, 10, 10)
    motion2.addMotion(t1.canvas, 80, 10)
    motion3.addMotion(t1.canvas, 150, 10)
    motion4.addMotion(t1.canvas, 220, 10)
    motion5.addMotion(t1.canvas, 290, 10)
    quit = tkinter.Button(t1.canvas, command=root.quit, text="QUIT")
    start = tkinter.Button(t1.canvas,command=render_fun_animation, text="START")
    quit.place(x=300,y=250)
    start.place(x=400,y=250)
    for i in range(1,9):
        blocks[i].addBlock(t1.canvas)

    root.mainloop()

if __name__ == '__main__':
    test()
