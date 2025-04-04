class User<T extends Field>{
    private T key;
    private T generator;
    private long secret;
    private T publicKey;
    private DHSetup dhs;


    public User(DHSetup dhs){
        this.dhs=dhs;
        this.generator=(T)dhs.getGenerator();
        this.secret=(long)(Math.random()*(dhs.mod-2)+2);
        this.publicKey=(T)this.dhs.power(this.generator,this.secret);
    }

    public T getPublicKey(){
        return (T)this.publicKey;
    }

    public void setKey(T x){
        this.key=(T)this.dhs.power(x,this.secret);
    }

    public T encrypt(T message){
        if (this.key==null){
            throw new IllegalArgumentException("key not set");
        }
        else{
            return (T)message.mul(this.key);
        }
    }

    public T decrypt(T message){
        if (this.key==null){
            throw new IllegalArgumentException("key not set");
        }
        else{
            return (T)message.div(this.key);
        }
    }
}