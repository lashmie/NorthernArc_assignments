import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TodoServices } from '../../services/todo-services';
import TodoDTO from '../../dto/todoDTO';

@Component({
 selector: 'app-add-todo',
 imports: [FormsModule,AddTodo],
 templateUrl: './add-todo.html',
 styleUrl: './add-todo.css',
})
export class AddTodo {
todoService: TodoServices = inject(TodoServices);
newTodo: TodoDTO = {
 id: 0,
 title: '',
 description: '',
 completed: false
};
addTodo() {
 if (this.newTodo.title.trim() && this.newTodo.description.trim() && this.newTodo.id > 0) {
   this.todoService.addTodo({ ...this.newTodo });
   this.newTodo = {
     id: 0,
     title: '',
     description: '',
     completed: false
   };
 }
}
}