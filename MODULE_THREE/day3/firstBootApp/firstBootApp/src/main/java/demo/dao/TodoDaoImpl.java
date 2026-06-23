package demo.dao;

import demo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Service

public class TodoDaoImpl implements TodoDao {
    HashMap<Integer, Todo> map = new HashMap<>();

    @Override
    public void save(Todo todo)
    {
        map.put(todo.getId(), todo);
    }

    @Override
    public Todo findById(int id) {
        return map.get(id);
    }

    @Override
    public List<Todo> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(int id) {
        map.remove(id);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }
}
