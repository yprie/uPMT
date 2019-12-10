package application.Configuration;

import java.util.LinkedList;

public class RecentFirstList<E> extends LinkedList<E> {

    @Override
    public boolean add(E e) {
        for(int i = 0; i < size(); i++)
            if(get(i).equals(e)){
                remove(get(i));
                break;
            }
        add(0, e);
        return true;
    }

}
