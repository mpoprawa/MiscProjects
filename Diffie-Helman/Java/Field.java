import java.math.BigInteger;

public class Field implements FieldInterface{
    private int mod=1234567891;
    private int val;
    public Field(){
        this.val=0;
    }
    public Field(int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.val=n;
    }
    public Field(String s){
        int n;
        try {
            n=Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            throw new IllegalArgumentException();
        }
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.val=n;
    }
    @Override
    public String toString() {
        return String.valueOf(this.val);
    }
    public void assign(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.val=n;
    }
    public void assign(final Field x){
        this.val=x.val;
    }
    public void showChar(){
        System.out.println(this.mod);
    }
    public void setChar(int n){
        if(n<=1){
            throw new IllegalArgumentException();
        }
        this.mod=n;
    }

    public Field copy(){
        Field f = new Field(this.val);
        return f;
    }

    private int inverse(int a, int b){
	    int q = 0;
	    int r = 1;
	    int s1 = 1; 
	    int s2 = 0;
	    int s3 = 1; 
	    int t1 = 0; 
	    int t2 = 1;
	    int t3 = 0;
	    while(r > 0){
		    q = (int)Math.floor(a/b);
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

    public Field add(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        Field f = new Field();
        f.val=(this.val+n)%this.mod;
        return f;
    }
    public Field add(final Field x){
        Field f = new Field();
        f.val=(this.val+x.val)%this.mod;
        return f;
    }
    public void iadd(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.assign(this.add(n));
    }
    public void iadd(final Field x){
        this.assign(this.add(x));
    }

    public Field sub(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        Field f = new Field();
        f.val=(this.val+(this.mod-n))%this.mod;
        return f;
    }
    public Field sub(final Field x){
        Field f = new Field();
        f.val=(this.val+(this.mod-x.val))%this.mod;
        return f;
    }
    public void isub(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.assign(this.sub(n));
    }
    public void isub(final Field x){
        this.assign(this.sub(x));
    }

    public Field mul(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        Field f = new Field();
        //BigInteger val = new BigInteger(String.valueOf(this.val));
        //val.multiply(BigInteger.valueOf(n));
        //BigInteger res=val.mod(BigInteger.valueOf(this.mod));
        //f.val=res.intValue();
        f.val=(int)(((long)this.val*(long)n)%(long)this.mod);
        return f;
    }
    public Field mul(final Field x){
        Field f = new Field();
        f.val=(int)(((long)this.val*(long)x.val)%(long)this.mod);
        return f;
    }
    public void imul(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.assign(this.mul(n));
    }
    public void imul(final Field x){
        this.assign(this.mul(x));
    }

    public Field div(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        int inv=inverse(n,this.mod);
        if (inv<0){
            inv=this.mod+inv;
        }
        return this.mul(inv);
    }
    public Field div(final Field x){
        int inv=inverse(x.val,x.mod);
        if (inv<0){
            inv=x.mod+inv;
        }
        return this.mul(inv);
    }
    public void idiv(final int n){
        if(n<0 || n>=this.mod){
            throw new IllegalArgumentException();
        }
        this.assign(this.div(n));
    }
    public void idiv(final Field x){
        this.assign(this.div(x));
    }

    public boolean lt(final Field x){
        if(this.val<x.val){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean le(final Field x){
        if(this.val<=x.val){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean gt(final Field x){
        if(this.val>x.val){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean ge(final Field x){
        if(this.val>=x.val){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean ne(final Field x){
        if(this.val!=x.val){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean eq(final Field x){
        if(this.val==x.val){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean eq(final int n){
        if(this.val==n){
            return true;
        }
        else{
            return false;
        }
    }
}