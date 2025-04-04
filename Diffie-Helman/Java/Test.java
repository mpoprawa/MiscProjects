public class Test{
    public static void main(String[] args) {
        int n = 1234567891;
        Field f = new Field();
        Field m = new Field(7654321);
        DHSetup<Field> setup = new DHSetup<Field>(n,f);
        User<Field> alice = new User<Field>(setup);
        User<Field> bob = new User<Field>(setup);
        Field a=alice.getPublicKey();
        Field b=bob.getPublicKey();
        try{
            alice.encrypt(m);
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex);
        }
        try{
            bob.decrypt(m);
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex);
        }
        alice.setKey(b);
        bob.setKey(a);
        Field coded = alice.encrypt(m);
        Field decoded = bob.decrypt(coded);
        System.out.println("message: "+m);
        System.out.println("coded message: "+coded);
        System.out.println("decoded message: "+decoded);
    }
}