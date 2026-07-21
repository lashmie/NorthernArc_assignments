import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import TodoDTO from '../../dto/todoDTO';
import { TodoServices } from '../../services/todo-services';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UpdateTodo } from '../update-todo/update-todo';

@Component({
  selector: 'app-todo-item',
  imports: [FormsModule, CommonModule, UpdateTodo],
  templateUrl: './todo-item.html',
  styleUrl: './todo-item.css',
})
export class TodoItem {

  @Input()
  public todo!: TodoDTO;
  
  showUpdate: boolean = false;
 
  todoService: TodoServices = inject(TodoServices);
}
