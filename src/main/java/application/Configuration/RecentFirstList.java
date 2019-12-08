package application.Configuration;

import java.util.LinkedList;

public class RecentFirstList<E> extends LinkedList<E> {

    @Override
    public boolean add(E e) {
        if(indexOf(e) >= 0)
            remove(e);
        add(0, e);
        return true;
    }

    public RecentFirstList<E> getFirsts(int n) { return (RecentFirstList<E>) subList(0, n); }

}
