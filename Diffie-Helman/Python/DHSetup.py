from Field import Field
import random
from math import sqrt

class DHSetup:

    def __init__(self,mod):
        if not type(mod) is int or mod<=1 or not self.isPrime(mod):
            raise Exception()
        found=False
        while not found:
            g=Field(random.randint(2,mod-1))
            found=self.checkGenerator(g,mod-1)
        self.__generator=g
        self.mod=mod

    def isPrime(self,n):
        for i in range(2,int(sqrt(n)+1)):
            if n%i==0:
                return False
        return True

    def checkGenerator(self,g,n):
        factors=[]
        p=n
        if n % 2 == 0:
            factors.append(2)
            n = n / 2
            while n % 2 == 0:
                n = n / 2
        for i in range(3,int(sqrt(n))+1,2):
            if n % i == 0:
                factors.append(i)
                n = n / i
                while n % i == 0:
                    n = n / i
        for i in factors:
            if self.power(g,int(p/i))==1:
                return False
        return True

    def getGenerator(self):
        return self.__generator
    
    def power(self, x, y):
        res = Field(1)
        while (y > 0):
            if ((y & 1) == 1) :
                res = res * x
            y = y >> 1
            x = x * x
        return res