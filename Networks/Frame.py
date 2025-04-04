import random
import textwrap
import zlib
import numpy as np
from tabulate import tabulate

def genData(n,filename):
    msg=""
    for i in range(n):
        msg += str(random.randint(0, 1))
    file=open(filename, "w")
    file.write(msg)

def pack(dataFile,targetFile,length,frameStart,frameEnd,crcLen):
    file=open(dataFile)
    data=file.read()
    frames=textwrap.wrap(data,length)

    for i in range(len(frames)):
        byteFrame=bytes(frames[i],'utf-8')
        crc=zlib.crc32(byteFrame)
        crc=format(crc, '0b')
        while len(crc)<crcLen:
            crc="0"+crc
        frames[i]=frames[i]+crc

    for i in range(len(frames)):
        frames[i]=frames[i].replace("011111","0111110")

    msg=""
    for i in range(len(frames)):
        frames[i]=frameStart+frames[i]+frameEnd
        msg+=frames[i]

    file = open(targetFile, "w")
    file.write(msg)

def unpack(dataFile,copyFile,targetFile,frameStart,frameEnd,crcLen):
    unpacked=[]
    file=open(dataFile)
    data=file.read()
    frames=data.split(frameStart)
    frames.pop(0)

    for i in range(len(frames)):
        frame=frames[i]
        if frame[-len(frameEnd):]==frameEnd:
            frames[i]=frame[:-len(frameEnd)]
        else:
            frames[i]="failure"

    for i in range(len(frames)):
        frames[i]=frames[i].replace("0111110","011111")

    for i in range(len(frames)):
        if frames[i]!="failure":
            msg=frames[i][:-crcLen]
            crc=frames[i][-crcLen:]
            byteMsg=bytes(msg,'utf-8')
            crc2=zlib.crc32(byteMsg)
            crc2=format(crc2,'0b')
            while len(crc2)<32:
                crc2="0"+crc2
            if crc!=crc2:
                msg="failure"
            unpacked.append(msg)
        else:
            unpacked.append("failure")

    file = open(copyFile, "w")
    for msg in unpacked:
        file.write(msg)
    #    print(msg)
    np.savetxt(targetFile,unpacked,fmt='%s')

def compare1(file1,file2):
    file=open(file1)
    data1=file.read()
    file=open(file2)
    data2=file.read()
    if data1==data2:
        return True
    else:
        return False

def compare2(file1,file2,frameLength):
    file=open(file1)
    data1=file.read()
    messages=textwrap.wrap(data1,frameLength)
    file=open(file2)
    data2=file.read().split()
    received=[x for x in data2]
    comparision=[]
    success=0
    
    while len(messages)<len(received):
        messages.append("")
    while len(messages)>len(received):
        received.append("")

    for i in range(len(messages)):
        comparision.append([messages[i],received[i]])
        if received[i]!="failure" and received[i]!=" ":
            success+=1

    print(tabulate(comparision, headers=["sent","received"]))
    print(success,"successes")

dataLength=500
dataFile="data.txt"
packedDataFile="packedData.txt"
unpackedDataFile="unpackedData.txt"
copyFile="copyFile.txt"
frameLength=32
frameStart="01111110"
frameEnd="011111110"
crcLen=32

#genData(dataLength,dataFile)
#pack(dataFile,packedDataFile,frameLength,frameStart,frameEnd,crcLen)
unpack(packedDataFile,copyFile,unpackedDataFile,frameStart,frameEnd,crcLen)
print(compare1(dataFile,copyFile))
compare2(dataFile,unpackedDataFile,frameLength)