#include <iostream>
#include <math.h>
#include "Field.hpp"
#include "DHSetup.cpp"
using namespace std;

template <typename T>
User<T>::User(DHSetup<T> dhs){
    srand((unsigned) time(NULL));
    this->dhs=dhs;
    this->generator=dhs.getGenerator();
    this->secret=2+(rand() % (dhs.mod-4));
    this->publicKey=dhs.power(this->generator,this->secret);
}

template <typename T>
T User<T>::getPublicKey(){
    return this->publicKey;
}

template <typename T>
void User<T>::setKey(T x){
    this->key=this->dhs.power(x,this->secret);
}

template <typename T>
T User<T>::encrypt(T message){
    if (this->key==0){
        throw invalid_argument("key not set");
    }
    else{
        return message*this->key;
    }
}

template <typename T>
T User<T>::decrypt(T message){
    if (this->key==0){
        throw invalid_argument("key not set");
    }
    else{
        return message/this->key;
    }
}