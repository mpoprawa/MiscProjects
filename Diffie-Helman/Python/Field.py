class Field:
    mod=1234567891

    def __init__(self, val=0):
        if val<0 or val>=self.mod or not type(val) is int:
            raise Exception()
        self.val = val

    def __str__(self):
        return str(self.val)
    
    def inverse(a, b): 
        if a == 0 : 
            return b,0,1
        gcd,x1,y1 = Field.inverse(b%a, a) 
        x = y1 - (b//a) * x1 
        y = x1 
        return gcd,x,y 

    def assign(self,n):
        if isinstance(n,int) and n>=0 and n<self.mod:
            self.val=n
        else:
            raise Exception()

    def __add__(self, x):
        if isinstance(x,Field):
            f=Field((self.val + x.val)%self.mod)
            return f
        elif isinstance(x,int) and x>=0 and x<self.mod:
            f=Field((self.val + x)%self.mod)
            return f
        else:
            raise Exception()
        
    def __radd__(self, x):
        return self + x
    
    def __sub__(self, x):
        if isinstance(x,Field):
            f=Field((self.val + (self.mod-x.val))%self.mod)
            return f
        elif isinstance(x,int) and x>=0 and x<self.mod:
            f=Field((self.val + (self.mod-x))%self.mod)
            return f
        else:
            raise Exception()
        
    def __rsub__(self, x):
        return Field(x)-self
    
    def __mul__(self, x):
        if isinstance(x,Field):
            f=Field((self.val*x.val)%self.mod)
            return f
        elif isinstance(x,int) and x>=0 and x<self.mod:
            f=Field((self.val*x)%self.mod)
            return f
        else:
            raise Exception()
        
    def __rmul__(self, x):
        return self*x
    
    def __truediv__(self, x):
        if isinstance(x,Field):
            inv=Field.inverse(x.val,self.mod)[1]
            if inv<0:
                inv=self.mod+inv
            f=Field((self.val*inv)%self.mod)
            return f
        elif isinstance(x,int) and x>=0 and x<self.mod:
            inv=Field.inverse(x,self.mod)[1]
            if inv<0:
                inv=self.mod+inv
            f=Field((self.val*inv)%self.mod)
            return f
        else:
            raise Exception()
        
    def __rtruediv__(self, x):
        return Field(x)/self
    
    def __eq__(self, x):
        if isinstance(x,Field):
            if self.val==x.val:
                return True
        elif isinstance(x,int):
            if self.val==x:
                return True
        return False
        
    def __gt__(self, x):
        if self.val>x.val:
            return True
        else:
            return False
    
    def __ge__(self, x):
        if self.val>=x.val:
            return True
        else:
            return False
        
    def __lt__(self, x):
        if self.val<x.val:
            return True
        else:
            return False
        
    def __le__(self, x):
        if self.val<=x.val:
            return True
        else:
            return False
        
    def show(self):
        print("charakterystyka ciala",self.mod)

    def showChar(self):
        print(self.mod)

    def setChar(self,val):
        if val<=1 or not type(val) is int:
            raise Exception()
        self.mod=val