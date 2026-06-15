package Dao_workedExamples.dao;

import Dao_workedExamples.entity.todo;

import java.util.ArrayList;
import java.util.List;

public class todoImpl implements todoDao{

    List<todo> list = new ArrayList<>();
    @Override
    public void save(todo t) {
    list.add(t);
    }

    @Override
    public todo findbyId(int id) {
        for(todo to:list){
            if(to.getId()==id){
                return to;
            }
        }
        return null;
    }

    @Override
    public void update(todo t) {
        for(todo to:list){
            if(to.getId()==t.getId()){
                to.setCompleted(t.isCompleted());
                break;
            }
        }
    }

    @Override
    public void deletebyid(int id) {
    for(todo t:list){
        if(id==t.getId()){
            list.remove(t);
            break;
        }
    }
    }

    @Override
    public void deleteall() {
list=new ArrayList<>();
    }

    @Override
    public void findall() {

    }
}
