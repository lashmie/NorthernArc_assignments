import { Component, inject, Input } from '@angular/core';
import TodoDTO from '../../dto/todoDTO';
import { TodoServices } from '../../services/todo-services';
import { TodoItem } from '../todo-item/todo-item';
import { AddTodo } from '../add-todo/add-todo';
import { UpdateTodo } from '../update-todo/update-todo';

@Component({
  selector: 'app-todo-component',
  imports: [TodoItem, AddTodo, UpdateTodo],
  templateUrl: './todo-component.html',
  styleUrl: './todo-component.css',
})
export class TodoComponent {
todoService:TodoServices = inject(TodoServices);
}
