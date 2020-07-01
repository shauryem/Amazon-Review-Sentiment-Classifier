
public class fraction {
    public double posNum;
    public double posDenom;
    public double negNum;
    public double negDenom;

    public fraction(){
        posNum = 1;
        posDenom = 1;
        negNum = 1;
        negDenom = 1;
    }

    public fraction(double posNum, double posDenom, double negNum, double negDenom){
        this.posNum = posNum;
        this.posDenom = posDenom;
        this.negNum = negNum;
        this.negDenom = negDenom;
    }

    public String toString(){
        return String.format(posDenom + " " + negDenom);
    }

    public void setDenom(int a, int b){
        this.posDenom = a;
        this.negDenom = b;
    }
}