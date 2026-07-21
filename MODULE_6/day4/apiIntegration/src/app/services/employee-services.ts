import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import EmployeeResponseDTO from '../components/dto/EmployeeResponseDTO';
import EmployeeRequestDTO from '../components/dto/EmployeeRequestDTO';


@Injectable({
  providedIn: 'root'
})
export class EmployeeServices {
  private http:HttpClient=inject(HttpClient);
  employees: WritableSignal<EmployeeResponseDTO[]> = signal([]);

  getAll():Observable<EmployeeResponseDTO[]>{
    return this.http.get<EmployeeResponseDTO[]>('http://localhost:8080/api/employees');
  }
  getById(id:number):Observable<EmployeeResponseDTO>{
    return this.http.get<EmployeeResponseDTO>(`http://localhost:8080/api/employees/${id}`);
  }
  add(employee: EmployeeRequestDTO): Observable<EmployeeRequestDTO> {
    return this.http.post<EmployeeRequestDTO>('http://localhost:8080/api/employees', employee);
  }
  update(id:number,employee: EmployeeRequestDTO): Observable<EmployeeRequestDTO> {
    return this.http.put<EmployeeRequestDTO>(`http://localhost:8080/api/employees/${id}`, employee);
  }

  remove(id:number): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/api/employees/${id}`);
  }

  delete(id:number): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/api/employees/${id}`);
  }
}
 