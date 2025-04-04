class DHSetup<T extends Field>{
    private T generator;
    public int mod;
    
    public DHSetup(int mod,T g){
        if (mod<=1 || !isPrime(mod)){
            throw new IllegalArgumentException();
        }
        boolean found=false;
        while (!found){
            g.assign((int)(Math.random()*(mod-2)+2));
            found=checkGenerator(g,mod-1);
        }
        this.generator=g;
        this.mod=mod;
    }

    private static boolean isPrime(int n){
        for (int i = 2; i <= Math.sqrt(n); i ++) { 
            if (n % i == 0){ 
                return false;
            } 
        } 
        return true; 
    }

    private boolean checkGenerator(T g, int n){
        int p=n;
        for (int i = 2; i <= Math.sqrt(p)+1; i++){
            if (n%i == 0){
                if (power(g,(int)(p/i)).eq(1)){
                    return false;
                }
                while (n%i == 0){
                    n /= i;
                }
            }
        }
        return true; 
    } 

    public T getGenerator(){
        return (T)this.generator.copy();
    }

    public T power(T g, long y){
        T res=(T)g.copy();
        res.assign(1);
        T x=(T)g.copy(); 
        while (y > 0){
            if ((y & 1) == 1){
                res.imul(x);
            }
            y = y >> 1;
            x.imul(x);
        }
        return res;
    }
}