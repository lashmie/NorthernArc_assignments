package dao;

import entity.todo;

import java.util.LinkedHashMap;
import java.util.Map;

public class DaoTodoImplColl implements DaoTodo {
    Map<Integer, todo> map = new LinkedHashMap<>();

    @Override
    public void save(todo t) {
        map.put(t.getId(), t);
    }

    @Override
    public todo findbyId(int id) {
        return map.get(id);
    }

    @Override
    public void update(todo t) {
        map.put(t.getId(), t);
    }

    @Override
    public void deletebyid(int id) {
        map.remove(id);
    }

    @Override
    public void deleteall() {
        map.clear();
    }

    @Override
    public Map<Integer, todo> findall() {
        return map;
    }
}
