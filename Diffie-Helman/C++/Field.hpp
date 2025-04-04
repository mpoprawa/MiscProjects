#pragma once
using namespace std;

class Field{
    private:
        int mod=1234567891;
        int val;
        int inverse(int a,int b);
    public:
        Field();
        Field(int n);
        Field(string n);
        Field operator+(const Field &x);
        Field operator-(const Field &x);
        Field operator*(const Field &x);
        Field operator/(const Field &x);
        void operator+=(const Field &x);
        void operator-=(const Field &x);
        void operator*=(const Field &x);
        void operator/=(const Field &x);
        bool operator<(const Field &x);
        bool operator>(const Field &x);
        bool operator<=(const Field &x);
        bool operator>=(const Field &x);
        bool operator==(const Field &x);
        bool operator!=(const Field &x);
        void operator=(const int &n);
        friend ostream &operator<<( ostream &output, const Field &x);
        void showChar();
        void setChar(int n);
};

template <typename T>
class DHSetup{
    private:
        T generator;
        bool isPrime(int n);
        bool checkGenerator(T g, int n);
    public:
        int mod;
        DHSetup();
        DHSetup(int mod);
        T getGenerator();
        T power(T x, unsigned long y);
};

template <typename T>
class User{
    private:
        T key;
        T generator;
        unsigned long secret;
        T publicKey;
        DHSetup<T> dhs;
    public:
        User(DHSetup<T> dhs);
        T getPublicKey();
        void setKey(T x);
        T encrypt(T message);
        T decrypt(T message);
};
;