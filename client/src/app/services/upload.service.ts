import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  constructor(private http: HttpClient) { }

  postPictureWithComments(formData: FormData): Promise<any> {

    return firstValueFrom(
      this.http.post('http://localhost:8080/api/post', formData)
    );

  }

}
