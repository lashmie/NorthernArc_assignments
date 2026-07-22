import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'An unexpected error occurred. Please try again.';

      if (error.status === 0) {
        message = 'Cannot reach the server. Is the backend running on port 8080?';
      } else if (error.status === 404) {
        message = 'Requested resource was not found.';
      } else if (error.error?.message) {
        message = error.error.message;
      } else if (error.message) {
        message = error.message;
      }

      console.error('HTTP Error:', error);
      return throwError(() => new Error(message));
    })
  );
};
