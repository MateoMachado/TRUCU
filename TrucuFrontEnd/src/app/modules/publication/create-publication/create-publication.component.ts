import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Image } from 'src/app/core/models/Image';
import { Publication } from 'src/app/core/models/Publication';
import { HttpService } from 'src/app/core/services/http.service';

@Component({
  selector: 'app-create-publication',
  templateUrl: './create-publication.component.html',
  styleUrls: ['./create-publication.component.css']
})
export class CreatePublicationComponent implements OnInit {
  files: File[] | null = null;
  newPublication : Publication = new Publication();
  confirmPassword : string;

  constructor(public http : HttpService, public toastr : ToastrService) { }

  ngOnInit(): void {
  }

  handleFileInput(files: FileList) {
    if(this.files == null){
      this.files = [];
    }
    this.files.push(files.item(0));
  }

  removeFile(index : number){
    this.files.splice(index,1);
  }

  createPublication(){
    let images : Image[] = [];
    
    if(this.files == null){
      this.toastr.warning('Debe agregar fotos a la publicación','Advertencia');
    }
    this.files.forEach(file => {
      let reader = new FileReader();

      // Setup onload event for reader
      reader.onload = () => {
          // Store base64 encoded representation of file
          let image = new Image();
          if(reader.result != null)
            image.imageBytes = reader.result.toString();
          
            images.push(image);
      } 
      
      reader.readAsDataURL(file);
    });

    this.newPublication.images = images;

    this.http.CreatePublication(this.newPublication).subscribe(data => {
      this.newPublication.idPublication = data;
      this.newPublication.images.forEach(image => {
        image.idPublication = this.newPublication.idPublication;
        this.http.CreatePublication(image); // CAMBIAR ESTO POR CREATE IMAGE
      })
    });
  }

}
