from Field import Field
from DHSetup import DHSetup
import random

class User:
    __key=None

    def __init__(self,dhs):
        if not type(dhs) is DHSetup:
            raise Exception()
        self.dhs=dhs
        self.__generator=dhs.getGenerator()
        self.__secret=random.randint(2,dhs.mod-2)
        self.__publicKey=self.dhs.power(self.__generator,self.__secret)

    def getPublicKey(self):
        return self.__publicKey
    
    def setKey(self,x):
        self.__key=self.dhs.power(x,self.__secret)

    def encrypt(self,message):
        if self.__key==None:
            raise Exception()
        else:
            return message*self.__key
        
    def decrypt(self,message):
        if self.__key==None:
            raise Exception()
        else:
            return message/self.__key