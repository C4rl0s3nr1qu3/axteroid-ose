package com.axteroid.ose.server.tools.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class ListUtil {
	private final static Logger log = LoggerFactory.getLogger(ListUtil.class);

    public static <E> List<E> getRepeatList(Collection<E> base, Collection<E> list) {
        List<E> result = new ArrayList<E>();
        int count = 0;
        for (E o : base) {
            count = 0;
            for (E item : list) {
                if (o.equals(item)) {
                    count++;
                }
            }
            if (count > 1) {
                result.add(o);
            }
        }
        return result;
    }

    public static List<List> getGroups(Collection list, int maximo) {
        int i = 0;
        List<List> result = new ArrayList<List>();
        List<Object> parcial = new ArrayList<Object>();
        for (Object bean : list) {
            i++;
            if (i < maximo) {
                parcial.add(bean);
            } else {
                parcial.add(bean);
                result.add(parcial);
                parcial = new ArrayList<Object>();
                i = 0;
            }
        }
        if (parcial.size() > 0) {
            result.add(parcial);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> list= Arrays.asList("A", "E", "A", "O", "1", "1", "2", "3", "4", "1");
        List<String> base = Arrays.asList("A", "E", "I", "O", "U", "1", "2", "3", "4", "5");

        System.out.println(getRepeatList(base,list));

    }
    
    public static boolean isBlankOrNull(List<String> lst)
    {
        if (lst==null)
                return true;
        else 
                return lst.isEmpty(); 
    }
    
    public static boolean isNotBlankOrNull(List<String> lst)
    {
        return  !isBlankOrNull(lst);
    }
    
    public static <T> boolean isBlankOrNull_T(List<T> lst)
    {
        if (lst==null)
                return true;
        else 
                return lst.isEmpty(); 
    }
    public static <T> boolean isNotBlankOrNull_T(List<T> lst)
    {
        return  !isBlankOrNull_T(lst);
    }
}
