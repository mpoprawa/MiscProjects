import random

class Node:
    def __init__(self, name, pos, l, chance):
        self.name = name
        self.pos = pos
        self.chance = chance
        self.on = False
        self.onL = False
        self.onR = False
        self.dist = 0
        self.time = 2*max(l-pos,pos+1)
        self.distL = 0
        self.distR = 0

        self.clean = False
        self.timeOff = 0
        self.colision = False
        self.retry = 0
        self.cooldown = 0
        self.waiting = True

class Colision:
    def __init__(self, pos, l):
        self.pos = pos
        self.dist = 0
        self.time = max(l-pos,pos+1)
        self.L = False
        self.R = False

def CSMA(nodes,length,time):
    data=[]
    network=[" "]*length
    colisions=[]

    for i in range(time):
        for node in nodes:
            if node.waiting:
                k=random.random()
                if k<node.chance:
                    node.on = True
                    node.onL = True
                    node.onR = True
                    node.waiting = False
                    node.dist = 0
                    node.distL = 0
                    node.distR = 0
                    node.retry = 0

            if network[node.pos] == "J" and node.on == True:
                node.collision = True
                network[node.pos] = " "

            elif node.on == True and (network[node.pos] == " " or network[node.pos] == node.name):
                r=node.pos+node.distR
                l=node.pos-node.distL

                if node.distR == 0:
                    network[node.pos]=node.name
                    node.distR+=1
                    node.distL+=1
                    node.dist+=1
                
                else:
                    if node.onR == True:
                        if r<length:
                            if network[r]==" ":
                                network[r]=node.name
                                node.distR+=1
                            elif network[r]=="J":
                                node.onR=False
                            elif network[r]!=node.name:
                                for x in nodes:
                                    if x.name==network[r]:
                                        x.onL=False
                                network[r]="J"
                                node.onR=False
                                colisions.append(Colision(r, length))
                        else:
                            node.onR=False
                    if node.onL == True:
                        if l>=0:
                            if network[l]==" ":
                                network[l]=node.name
                                node.distL+=1
                            elif network[l]=="J":
                                node.onL=False
                            elif network[l]!=node.name:
                                for x in nodes:
                                    if x.name==network[l]:
                                        x.onR=False
                                network[l]="J"
                                node.onL=False
                                colisions.append(Colision(l, length))
                        else:
                            node.onL=False

                    node.dist+=1
                    if node.onL == False and node.onR == False:
                        if node.colision:
                            node.cooldown = random.randint(0,(2**node.retry)-1)*3*(node.time/2)
                            node.retry+=1
                            node.on = False
                        elif node.dist == node.time:
                            node.clean=True
                            node.on = False

            elif node.clean:
                r=node.pos+node.timeOff
                l=node.pos-node.timeOff
                if r<length and network[r]==node.name:
                    network[r]=" "
                if l>=0 and network[l]==node.name:
                    network[l]=" "

                node.timeOff+=1
                if node.timeOff==node.time/2:
                    node.clean=False
                    node.timeOff=0
                    node.waiting=True

            elif node.cooldown > 0:
                #print(i,node.cooldown)
                node.cooldown -=1
                if node.cooldown == 0:
                    node.on = True
                    node.onL = True
                    node.onR = True
                    node.dist = 0
                    node.distL = 0
                    node.distR = 0
                    node.colision = False



        for colision in colisions:
            r=colision.pos+colision.dist
            l=colision.pos-colision.dist
            if r<length:
                network[r]="J"
                if r!=l:
                    network[r-1]=" "
            elif colision.R == False:
                network[length-1]=" "
                colision.R = True

            if l>=0 and l!=r:
                network[l]="J"
                network[l+1]=" "
            elif colision.L == False and l!=r:
                network[0]=" "
                colision.L = True

            if colision.dist==colision.time:
                colisions.remove(colision)
            colision.dist+=1

        print(network)
        data.append(network.copy())
    
    with open("file2.txt", 'w') as output:
        for row in data:
            output.write(str(row) + '\n')

l=25
time=200
A=Node("A",3,l,0.05)
B=Node("B",17,l,0.05)
C=Node("C",24,l,0.01)
nodes=[A,B,C]
CSMA(nodes,l,time)