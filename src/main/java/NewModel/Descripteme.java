package NewModel;

public class Descripteme implements IDescriptemeAdapter {

    //All this model will be changed ! It is only here for refactoring needs !

    private String mText;

    public Descripteme(String text){
        this.mText = text;
    }

    @Override
    public Object clone() {
        Descripteme ret = null;
        try {
            ret = (Descripteme) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if(!o.getClass().equals(Descripteme.class)) {
            return false;
        }
        Descripteme d = (Descripteme) o;
        return d.mText.equals(mText);
    }

    @Override
    public void setTexte(String t) {
        this.mText = t;
    }

    @Override
    public String getTexte(){
        return this.mText;
    }

    @Override
    public String toString(){
        return this.mText;
    }
}
