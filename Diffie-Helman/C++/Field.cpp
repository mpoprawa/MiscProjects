#include <iostream>
#include <math.h>
#include "Field.hpp"
using namespace std;

int Field::inverse(int a, int b){
	int q = 0;
	int r = 1;
	int s1 = 1; 
	int s2 = 0;
	int s3 = 1; 
	int t1 = 0; 
	int t2 = 1;
	int t3 = 0;
	while(r > 0){
		q = floor(a/b);
		r = a - q * b;
		s3 = s1 - q * s2;
		t3 = t1 - q * t2;
		if(r > 0){
			a = b;
			b = r;
			s1 = s2;
			s2 = s3;
			t1 = t2;
			t2 = t3;
		}
	}
	return s2;
}
Field::Field(){
    this->val=0;
}
Field::Field(int n) {
    int m=this->mod;
    if (n < 0 || n > m){
        throw invalid_argument("Invalid data"); 
    }
    this->val=n;
}
Field::Field(string s) {
    for (char ch : s) {
        if (!isdigit(ch)){
            throw invalid_argument("Invalid data"); 
        }
    }
    int n=stoi(s);
    int m=this->mod;
    if (n < 0 || n > m){
        throw invalid_argument("Invalid data"); 
    }
    this->val=n;
}
Field Field::operator+(const Field &x){
    int res=(this->val+x.val)%this->mod;
    Field n(res);
    return n;
}
Field Field::operator-(const Field &x){
    int res=(this->val+(x.mod-x.val))%this->mod;
    Field n(res);
    return n;
}
Field Field::operator*(const Field &x){
    int res=(int)(((long long)this->val*(long long)x.val)%(long long)this->mod);
    Field n(res);
    return n;
}
Field Field::operator/(const Field &x){
    int inv=inverse(x.val,x.mod);
    if (inv<0){
        inv=x.mod+inv;
    }
    Field n(inv);
    return *this*inv;
}
void Field::operator+=(const Field &x){
    *this=*this+x;
}
void Field::operator-=(const Field &x){
    *this=*this-x;
}
void Field::operator*=(const Field &x){
    *this=*this*x;
}
void Field::operator/=(const Field &x){
    *this=*this/x;
}
bool Field::operator<(const Field &x){ 
    if(this->val<x.val){
        return true;
    }
    else{
        return false;
    }
}
bool Field::operator>(const Field &x){ 
    if(this->val>x.val){
        return true;
    }
    else{
        return false;
    }
}
bool Field::operator<=(const Field &x){ 
    if(this->val<=x.val){
        return true;
    }
    else{
        return false;
    }
}
bool Field::operator>=(const Field &x){ 
    if(this->val>=x.val){
        return true;
    }
    else{
        return false;
    }
}
bool Field::operator==(const Field &x){ 
    if(this->val==x.val){
        return true;
    }
    else{
        return false;
    }
}
bool Field::operator!=(const Field &x){ 
    if(this->val!=x.val){
        return true;
    }
    else{
        return false;
    }
}
void Field::operator=(const int &n){
    if (n < 0 || n > this->mod){ 
            throw invalid_argument("Invalid data"); 
    }
    this->val = n;
}
ostream &operator<<( ostream &output, const Field &x){ 
    output<<x.val;
    return output;            
}
void Field::showChar(){
    cout<<this->mod<<endl;
}
void Field::setChar(int n){
    if (n <= 1){
        throw invalid_argument("Invalid data"); 
    }
    this->mod=n;
}