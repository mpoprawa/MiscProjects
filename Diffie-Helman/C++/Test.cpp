#include <iostream>
#include <string>
#include <stdlib.h>
#include <assert.h>
#include "Field.hpp"
#include "User.cpp"

using namespace std;

int main(int argc, char *argv[]) {
    int n = 1234567891;
    Field m(7654321);
    DHSetup<Field> setup(n);
    User<Field> alice(setup);
    User<Field> bob(setup);
    Field a=alice.getPublicKey();
    Field b=bob.getPublicKey();
    try{
        alice.encrypt(m);
    }
    catch (const exception& e) { 
        cerr << "Error: " << e.what() << endl; 
    }
    try{
        bob.decrypt(m);
    }
    catch (const exception& e) { 
        cerr << "Error: " << e.what() << endl; 
    }
    alice.setKey(b);
    bob.setKey(a);
    Field coded = alice.encrypt(m);
    Field decoded = bob.decrypt(coded);
    cout<<"message: "<<m<<endl;
    cout<<"coded message: "<<coded<<endl;
    cout<<"decoded message: "<<decoded<<endl;
}