from DHSetup import DHSetup
from Field import Field
from User import User

n=1234567891
m=Field(1234567)
setup=DHSetup(n)
alice=User(setup)
bob=User(setup)
try:
    alice.encrypt(m)
except:
    print("key not set")
try:
    bob.decrypt(m)
except:
    print("key not set")
a=alice.getPublicKey()
b=bob.getPublicKey()
alice.setKey(b)
bob.setKey(a)
coded=alice.encrypt(m)
decoded=bob.decrypt(coded)
print("message:",m)
print("coded message:",coded)
print("decoded message:",decoded)