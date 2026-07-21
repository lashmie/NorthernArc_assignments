import { Injectable, Service } from '@angular/core';
import TodoDTO from '../dto/todoDTO';

@Injectable({
  providedIn: 'root',
})
export class TodoServices {

    private todos: TodoDTO[] = [
        {
            id: 1,
            title: 'Learn Angular',
            description: 'Learn the basics of Angular framework',
            completed: false
        },
        {
            id: 2,
            title: 'Build a Todo App',
            description: 'Create a simple todo application using Angular',
            completed: false
        },
        {
            id: 3,
            title: 'Learn TypeScript',
            description: 'Understand the basics of TypeScript language',
            completed: true
        }
    ];


    public getTodos(): TodoDTO[] {
        return this.todos;
    }

    public addTodo(todo: TodoDTO): void {
        this.todos.push(todo);
    }

    public updateTodo(updatedTodo: TodoDTO): void {
        const index = this.todos.findIndex(todo => todo.id === updatedTodo.id);
        if (index !== -1) {
            this.todos[index] = updatedTodo;
        }
    }

    public deleteTodo(id: number): void {
        this.todos = this.todos.filter(todo => todo.id !== id);
    }

    
  }



 