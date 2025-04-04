package main

import (
	"fmt"
	"sync"
	"time"
	"math/rand"
)

const Repeats = 3
const n = 5
var wg sync.WaitGroup
var host = make(chan bool, n-1)

type Fork struct{ sync.Mutex }

type Philosopher struct {
	id      int
	left  *Fork
	right *Fork
}

func (p Philosopher) eat() {

	for i := 0; i < Repeats; i++ {
		fmt.Println("Philosopher", p.id+1, "is thinking")
		time.Sleep(time.Duration(rand.Float64()+1.0)*time.Second)
		fmt.Println("Philosopher", p.id+1, "is hungry")

		host <- true
		p.left.Lock()
		p.right.Lock()

		fmt.Println("Philosopher", p.id+1, "is eating")
		time.Sleep(time.Duration(rand.Float64()+1.0)*time.Second)

		p.left.Unlock()
		p.right.Unlock()

		<-host
	}
	fmt.Println("Philosopher", p.id+1, "finished")
	wg.Done()
}

func main() {
	rand.Seed(time.Now().UnixNano())

	Forks := make([]*Fork, n)
	for i := 0; i < n; i++ {
		Forks[i] = new(Fork)
	}

	Philosophers := make([]*Philosopher, n)
	for i := 0; i < n; i++ {
		Philosophers[i] = &Philosopher{i, Forks[i], Forks[(i+1)%n]}
	}

	for i := 0; i < n; i++ {
		wg.Add(1)
		go Philosophers[i].eat()
	}

	wg.Wait()
}