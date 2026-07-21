// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-update-todo',
//   imports: [],
//   templateUrl: './update-todo.html',
//   styleUrl: './update-todo.css',
// })
// export class UpdateTodo {}
import { Component, inject, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import TodoDTO from '../../dto/todoDTO';
import { TodoServices } from '../../services/todo-services';

@Component({
  selector: 'app-update-todo',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-todo.html',
  styleUrl: './update-todo.css'
})
export class UpdateTodo {

  protected todoService :TodoServices = inject(TodoServices);

  @Input()
  todo!: TodoDTO;


}