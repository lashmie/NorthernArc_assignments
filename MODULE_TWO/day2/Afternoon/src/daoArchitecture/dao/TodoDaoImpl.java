package daoArchitecture.dao;
import daoArchitecture.entity.Todo;
import java.util.ArrayList;
import java.util.List;

public class TodoDaoImpl implements TodoDao {

    private List<Todo> todos = new ArrayList<>();

    @Override
    public void save(Todo todo) {
        todos.add(todo);
    }



    @Override
    public Todo findById(int id) {
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }

    @Override
    public void update(Todo updatedTodo) {

        for (int i = 0; i < todos.size(); i++) {

            if (todos.get(i).getId() == updatedTodo.getId()) {
                todos.set(i, updatedTodo);
                return;
            }
        }
    }

    @Override
    public void deleteById(int id) {

        Todo todo = findById(id);

        if (todo != null) {
            todos.remove(todo);
        }
    }

    @Override
    public List<Todo> findAll() {
        return todos;
    }

    @Override
    public void deleteAll() {
        todos.clear();
    }
}
