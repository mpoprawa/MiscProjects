import threading
import time
import random

n = 5
Repeats = 3

printer = threading.Lock()
forks = [threading.Lock() for i in range(n)]
semaphore = threading.Semaphore(n-1)

def philosopher(id):
    left = forks[id]
    right = forks[(id + 1) % n]
    for _ in range(Repeats):
        printer.acquire()
        print(f"Philosopher {id+1} is thinking")
        printer.release()
        time.sleep(random.randint(0,1)+1)
        printer.acquire()
        print(f"Philosopher {id+1} is hungry")
        printer.release()
        
        semaphore.acquire()
        
        left.acquire()
        right.acquire()
        
        printer.acquire()
        print(f"Philosopher {id+1} is eating")
        printer.release()
        time.sleep(random.randint(0,1)+1)
        
        left.release()
        right.release()

        semaphore.release()
    printer.acquire()
    print(f"Philosopher {id+1} finished")
    printer.release()

philosophers = []
for i in range(n):
    philosophers.append(threading.Thread(target=philosopher, args=(i,)))
    
for thread in philosophers:
    thread.start()
    
for thread in philosophers:
    thread.join()