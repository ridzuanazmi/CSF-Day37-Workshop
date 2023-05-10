import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UploadService } from '../services/upload.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit{

  // Create a reference to the input element in HTML using its name
  // it is a full file object. The formControlName "picture" is only a path
  // hence we use uploaded picture instead
  @ViewChild('file') imageFile!: ElementRef;

  uploadForm!: FormGroup
  uploadStatus: string = "Pending upload"
  
  constructor(private fb: FormBuilder, private uploadSrvc: UploadService) { }

  ngOnInit(): void {
    this.uploadForm = this.createUploadForm();
  }

  upload() {
    console.log(">>>upload(): ",this.uploadForm.value);
    console.log(">>>upload(): ",this.imageFile);

    const formData = new FormData(); // use form data here because its multipart form data value type
    formData.set('comments', this.uploadForm.get('comments')?.value);
    formData.set('picture', this.imageFile.nativeElement.files[0]); // retrieves the actual picture rather than the path

    this.uploadSrvc.postPictureWithComments(formData) // Sends the comment and picture file as multipart form data 
      .then((response) => {
        console.info(">>>upload(): response = ", response)
        this.uploadStatus = response.message;
      })
      .catch((err) => {
        console.error("upload(): error = ", err)
        this.uploadStatus = err;
      })
    this.uploadForm.reset();
  }

  // Helper function to create form
  private createUploadForm(): FormGroup {
    return this.fb.group({
      comments: this.fb.control<string>('', [ Validators.required ]),
      picture: this.fb.control('', [ Validators.required ]),
    })
  }

}
