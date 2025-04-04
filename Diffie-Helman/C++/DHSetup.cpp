#include <iostream>
#include <math.h>
#include "Field.hpp"
using namespace std;

template <typename T>
DHSetup<T>::DHSetup(int mod){
    if (!isPrime(mod)){
        throw invalid_argument("invalid data");
    }
    bool found=false;
    T g;
    srand((unsigned) time(NULL));
    while (!found){
        g=2 + (rand() % (mod-3));
        found=checkGenerator(g,mod-1);
    }
    this->generator=g;
    this->mod=mod;
}

template <typename T>
DHSetup<T>::DHSetup(){
    this->mod=0;
}

template <typename T>
bool DHSetup<T>::isPrime(int n){
        for (int i = 2; i <= sqrt(n); i ++) { 
            if (n % i == 0){ 
                return false;
            } 
        } 
        return true; 
    }

template <typename T>
bool DHSetup<T>::checkGenerator(T g, int n){
    int p=n;
    for (int i = 2; i <= sqrt(p)+1; i++){
        if (n%i == 0){
            if (power(g,(int)(p/i))==1){
                return false;
            }
            while (n%i == 0){
                n /= i;
            }
        }
    }
    return true; 
}

template <typename T>
T DHSetup<T>::getGenerator(){
    return this->generator;
}

template <typename T>
T DHSetup<T>::power(T x, unsigned long y){
    T res(1);
    while (y > 0){
        if ((y & 1) == 1){
            res*=x;
        }
        y = y >> 1;
        x*=x;
    }
    return res;
}