import { Component, inject } from '@angular/core';
import { CountService } from '../../services/count-service';

@Component({
  selector: 'app-show-count',
  imports: [],
  templateUrl: './show-count.html',
  styleUrl: './show-count.css',
})
export class ShowCount {
  countService:CountService=inject(CountService);
}
