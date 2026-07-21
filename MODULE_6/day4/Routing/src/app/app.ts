// import { Component, signal } from '@angular/core';
// import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

// @Component({
//   selector: 'app-root',
//   imports: [RouterLink, RouterLinkActive, RouterOutlet],
//   templateUrl: './app.html',
//   styleUrls: ['./app.css']
// })
// export class App {
  
// }
import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
 
@Component({
  selector: 'app-root',
  imports: [RouterOutlet,RouterLink,RouterLinkActive,FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
router=inject(Router);
searchTerm=signal('');
showPerson(){
  if(this.searchTerm()=='Love'){
    this.router.navigate(['/person', 1]);
  }
}
}
